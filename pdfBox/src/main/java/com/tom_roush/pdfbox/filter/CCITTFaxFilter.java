package com.tom_roush.pdfbox.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.filter.ccitt.CCITTFaxG31DDecodeInputStream;
import com.tom_roush.pdfbox.filter.ccitt.FillOrderChangeInputStream;
import com.tom_roush.pdfbox.filter.ccitt.TIFFFaxDecoder;
import com.tom_roush.pdfbox.io.IOUtils;

import android.util.Log;

/**
 * Decodes image data that has been encoded using either Group 3 or Group 4
 * CCITT facsimile (fax) encoding.
 *
 * @author Ben Litchfield
 * @author Marcel Kammer
 * @author Paul King
 */
final class CCITTFaxFilter extends Filter
{
    @Override
    public DecodeResult decode(InputStream encoded, OutputStream decoded,
                                         COSDictionary parameters, int index) throws IOException
    {
        DecodeResult result = new DecodeResult(new COSDictionary());
        result.getParameters().addAll(parameters);

        // get decode parameters
        COSDictionary decodeParms = getDecodeParams(parameters, index);

        // parse dimensions
        int cols = decodeParms.getInt(COSName.COLUMNS, 1728);
        int rows = decodeParms.getInt(COSName.ROWS, 0);
        int height = parameters.getInt(COSName.HEIGHT, COSName.H, 0);
        if (rows > 0 && height > 0)
        {
            // ensure that rows doesn't contain implausible data, see PDFBOX-771
            rows = Math.min(rows, height);
        }
        else
        {
            // at least one of the values has to have a valid value
            rows = Math.max(rows, height);
        }

        // decompress data
        int k = decodeParms.getInt(COSName.K, 0);
        boolean encodedByteAlign = decodeParms.getBoolean(COSName.ENCODED_BYTE_ALIGN, false);
        int arraySize = (cols + 7) / 8 * rows;
        TIFFFaxDecoder faxDecoder = new TIFFFaxDecoder(1, cols, rows);
        // TODO possible options??
        long tiffOptions = 0;
        byte[] compressed = IOUtils.toByteArray(encoded);
        byte[] decompressed = null;
        if (k == 0)
        {
            InputStream in = new CCITTFaxG31DDecodeInputStream(
                    new ByteArrayInputStream(compressed), cols, encodedByteAlign);
            in = new FillOrderChangeInputStream(in); //Decorate to change fill order
            decompressed = IOUtils.toByteArray(in);
            in.close();
        }
        else if (k > 0)
        {
            decompressed = new byte[arraySize];
            faxDecoder.decode2D(decompressed, compressed, 0, rows, tiffOptions);
        }
        else if (k < 0)
        {
            decompressed = new byte[arraySize];
            faxDecoder.decodeT6(decompressed, compressed, 0, rows, tiffOptions, encodedByteAlign);
        }

        // invert bitmap
        boolean blackIsOne = decodeParms.getBoolean(COSName.BLACK_IS_1, false);
        if (!blackIsOne)
        {
            // Inverting the bitmap
            // Note the previous approach with starting from an IndexColorModel didn't work
            // reliably. In some cases the image wouldn't be painted for some reason.
            // So a safe but slower approach was taken.
            invertBitmap(decompressed);
        }

        // repair missing color space
        if (!parameters.containsKey(COSName.COLORSPACE))
        {
            result.getParameters().setName(COSName.COLORSPACE, COSName.DEVICEGRAY.getName());
        }

        decoded.write(decompressed);
        return new DecodeResult(parameters);
    }

    private static void invertBitmap(byte[] bufferData)
    {
        for (int i = 0, c = bufferData.length; i < c; i++)
        {
            bufferData[i] = (byte) (~bufferData[i] & 0xFF);
        }
    }

    @Override
    protected void encode(InputStream input, OutputStream encoded, COSDictionary parameters)
            throws IOException
    {
    	Log.w("PdfBoxAndroid", "CCITTFaxDecode.encode is not implemented yet, skipping this stream.");
    }
}
