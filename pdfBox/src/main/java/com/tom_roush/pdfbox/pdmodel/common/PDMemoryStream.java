package com.tom_roush.pdfbox.pdmodel.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.List;

import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSStream;

import com.tom_roush.pdfbox.pdmodel.common.filespecification.PDFileSpecification;

/**
 * A PDStream represents a stream in a PDF document.  Streams are tied to a single
 * PDF document.
 *
 * @author Ben Litchfield
 */
public class PDMemoryStream extends PDStream
{
    private final byte[] data;

    /**
     * This will create a new PDMemoryStream object.
     *
     * @param buffer The data for this in memory stream.
     */
    public PDMemoryStream( byte[] buffer )
    {
        data = buffer;
    }



    /**
     * If there are not compression filters on the current stream then this
     * will add a compression filter, flate compression for example.
     */
    @Override
    public void addCompression()
    {
        //no compression to add
    }



    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    @Override
    public COSBase getCOSObject()
    {
        throw new UnsupportedOperationException( "not supported for memory stream" );
    }

    /**
     * This will get a stream that can be written to.
     *
     * @return An output stream to write data to.
     *
     * @throws IOException If an IO error occurs during writing.
     */
    @Override
    public OutputStream createOutputStream() throws IOException
    {
        throw new UnsupportedOperationException( "not supported for memory stream" );
    }

    /**
     * This will get a stream that can be read from.
     *
     * @return An input stream that can be read from.
     *
     * @throws IOException If an IO error occurs during reading.
     */
    @Override
    public InputStream createInputStream() throws IOException
    {
        return new ByteArrayInputStream( data );
    }

    /**
     * This will get a stream with some filters applied but not others.  This is useful
     * when doing images, ie filters = [flate,dct], we want to remove flate but leave dct
     *
     * @param stopFilters A list of filters to stop decoding at.
     * @return A stream with decoded data.
     * @throws IOException If there is an error processing the stream.
     */
    @Override
    public InputStream getPartiallyFilteredStream( List stopFilters ) throws IOException
    {
        return createInputStream();
    }

    /**
     * Get the cos stream associated with this object.
     *
     * @return The cos object that matches this Java object.
     */
    @Override
    public COSStream getStream()
    {
        throw new UnsupportedOperationException( "not supported for memory stream" );
    }

    /**
     * This will get the length of the filtered/compressed stream.  This is readonly in the
     * PD Model and will be managed by this class.
     *
     * @return The length of the filtered stream.
     */
    @Override
    public int getLength()
    {
        return data.length;
    }

    /**
     * This will get the list of filters that are associated with this stream.  Or
     * null if there are none.
     * @return A list of all encoding filters to apply to this stream.
     */
    @Override
    public List getFilters()
    {
        return null;
    }

    /**
     * This will set the filters that are part of this stream.
     *
     * @param filters The filters that are part of this stream.
     */
    @Override
    public void setFilters( List filters )
    {
        throw new UnsupportedOperationException( "not supported for memory stream" );
    }

    /**
     * Get the list of decode parameters.  Each entry in the list will refer to
     * an entry in the filters list.
     *
     * @return The list of decode parameters.
     *
     * @throws IOException if there is an error retrieving the parameters.
     */
    public List getDecodeParams() throws IOException
    {
        return null;
    }

    /**
     * This will set the list of decode params.
     *
     * @param decodeParams The list of decode params.
     */
    public void setDecodeParams( List decodeParams )
    {
        //do nothing
    }

    /**
     * This will get the file specification for this stream.  This is only
     * required for external files.
     *
     * @return The file specification.
     */
    @Override
    public PDFileSpecification getFile()
    {
        return null;
    }

    /**
     * Set the file specification.
     * @param f The file specification.
     */
    @Override
    public void setFile( PDFileSpecification f )
    {
        //do nothing.
    }

    /**
     * This will get the list of filters that are associated with this stream.  Or
     * null if there are none.
     * @return A list of all encoding filters to apply to this stream.
     */
    @Override
    public List getFileFilters()
    {
        return null;
    }

    /**
     * This will set the filters that are part of this stream.
     *
     * @param filters The filters that are part of this stream.
     */
    @Override
    public void setFileFilters( List filters )
    {
        //do nothing.
    }

    /**
     * Get the list of decode parameters.  Each entry in the list will refer to
     * an entry in the filters list.
     *
     * @return The list of decode parameters.
     *
     * @throws IOException if there is an error retrieving the parameters.
     */
    @Override
    public List getFileDecodeParams() throws IOException
    {
        return null;
    }

    /**
     * This will set the list of decode params.
     *
     * @param decodeParams The list of decode params.
     */
    @Override
    public void setFileDecodeParams( List decodeParams )
    {
        //do nothing
    }

    /**
     * This will copy the stream into a byte array.
     *
     * @return The byte array of the filteredStream
     * @throws IOException When getFilteredStream did not work
     */
    @Override
    public byte[] getByteArray() throws IOException
    {
        return data;
    }

    /**
     * Get the metadata that is part of the document catalog.  This will
     * return null if there is no meta data for this object.
     *
     * @return The metadata for this object.
     */
    @Override
    public PDMetadata getMetadata()
    {
        return null;
    }

    /**
     * Set the metadata for this object.  This can be null.
     *
     * @param meta The meta data for this object.
     */
    @Override
    public void setMetadata( PDMetadata meta )
    {
        //do nothing
    }
}
