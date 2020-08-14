package com.tom_roush.pdfbox.pdmodel.font;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tom_roush.fontbox.cff.CFFCIDFont;
import com.tom_roush.fontbox.cff.CFFFont;
import com.tom_roush.fontbox.cff.CFFParser;
import com.tom_roush.fontbox.cff.CFFType1Font;
import com.tom_roush.fontbox.cff.Type2CharString;
import com.tom_roush.fontbox.util.BoundingBox;
import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.io.IOUtils;
import com.tom_roush.pdfbox.pdmodel.common.PDStream;
import com.tom_roush.pdfbox.util.Matrix;
import com.tom_roush.pdfbox.util.awt.AffineTransform;

import android.graphics.PointF;
import android.util.Log;

/**
 * Type 0 CIDFont (CFF).
 * 
 * @author Ben Litchfield
 * @author John Hewson
 */
public class PDCIDFontType0 extends PDCIDFont
{
	private final CFFCIDFont cidFont;  // Top DICT that uses CIDFont operators
	private final CFFType1Font t1Font; // Top DICT that does not use CIDFont operators

	private final Map<Integer, Float> glyphHeights = new HashMap<Integer, Float>();
	private final boolean isEmbedded;
	private final boolean isDamaged;

	private Float avgWidth = null;
	private Matrix fontMatrix;
	private AffineTransform fontMatrixTransform;

	/**
	 * Constructor.
	 * 
	 * @param fontDictionary The font dictionary according to the PDF specification.
	 */
	public PDCIDFontType0(COSDictionary fontDictionary, PDType0Font parent) throws IOException
	{
		super(fontDictionary, parent);

		PDFontDescriptor fd = getFontDescriptor();
		byte[] bytes = null;
		if (fd != null)
		{
			PDStream ff3Stream = fd.getFontFile3();
			if (ff3Stream != null)
			{
				bytes = IOUtils.toByteArray(ff3Stream.createInputStream());
			}
		}

		boolean fontIsDamaged = false;
		CFFFont cffFont = null;
		if (bytes != null && bytes.length > 0 && (bytes[0] & 0xff) == '%')
		{
			// todo: PDFBOX-2642 contains a Type1 PFB font in a CIDFont, but we can't handle it yet
			Log.e("PdfBoxAndroid", "Unsupported: Type1 font instead of CFF in " + fd.getFontName());
			fontIsDamaged = true;
		}
		else if (bytes != null)
		{
			CFFParser cffParser = new CFFParser();
			try
			{
				cffFont = cffParser.parse(bytes).get(0);
			}
			catch (IOException e)
			{
				Log.e("PdfBoxAndroid", "Can't read the embedded CFF font " + fd.getFontName(), e);
				fontIsDamaged = true;
			}
		}

		if (cffFont != null)
		{
			// embedded
			if (cffFont instanceof CFFCIDFont)
			{
				cidFont = (CFFCIDFont)cffFont;
				t1Font = null;
			}
			else
			{
				cidFont = null;
				t1Font = (CFFType1Font)cffFont;
			}
			isEmbedded = true;
			isDamaged = false;
		}
		else
		{
			// substitute
			CFFCIDFont cidSub = ExternalFonts.getCFFCIDFont(getBaseFont());
			if (cidSub != null)
			{
				cidFont = cidSub;
				t1Font = null;
			}
			else
			{
				COSDictionary cidSystemInfo = (COSDictionary)
						dict.getDictionaryObject(COSName.CIDSYSTEMINFO);

				String registryOrdering = null;
				if (cidSystemInfo != null)
				{
					String registry = cidSystemInfo.getNameAsString(COSName.REGISTRY);
					String ordering = cidSystemInfo.getNameAsString(COSName.ORDERING);
					if (registry != null && ordering != null)
					{
						registryOrdering = registry + "-" + ordering;
					}
				}

				cidSub = ExternalFonts.getCFFCIDFontFallback(registryOrdering, getFontDescriptor());
				cidFont = cidSub;
				t1Font = null;

				if (cidSub.getName().equals("AdobeBlank"))
				{
					// this error often indicates that the user needs to install the Adobe Reader
					// Asian and Extended Language Pack
					if (!fontIsDamaged)
					{
						Log.e("PdfBoxAndroid", "Missing CID-keyed font " + getBaseFont());
					}
				}
				else
				{
					Log.w("PdfBoxAndroid", "Using fallback for CID-keyed font " + getBaseFont());
				}
			}
			isEmbedded = false;
			isDamaged = fontIsDamaged;
		}
		fontMatrixTransform = getFontMatrix().createAffineTransform();
		fontMatrixTransform.scale(1000, 1000);
	}

	@Override
	public Matrix getFontMatrix()
	{
		if (fontMatrix == null)
		{
			List<Number> numbers;
			if (cidFont != null)
			{
				numbers = cidFont.getFontMatrix();
			}
			else
			{
				numbers = t1Font.getFontMatrix();
			}

			if (numbers != null && numbers.size() == 6)
			{
				fontMatrix = new Matrix(numbers.get(0).floatValue(), numbers.get(1).floatValue(),
						numbers.get(2).floatValue(), numbers.get(3).floatValue(),
						numbers.get(4).floatValue(), numbers.get(5).floatValue());
			}
			else
			{
				fontMatrix = new Matrix(0.001f, 0, 0, 0.001f, 0, 0);
			}
		}
		return fontMatrix;
	}

	@Override
	public BoundingBox getBoundingBox()
	{
		if (cidFont != null)
		{
			return cidFont.getFontBBox();
		}
		else
		{
			return t1Font.getFontBBox();
		}
	}

	/**
	 * Returns the embedded CFF CIDFont.
	 */
	public CFFFont getCFFFont()
	{
		if (cidFont != null)
		{
			return cidFont;
		}
		else
		{
			return t1Font;
		}
	}

	/**
	 * Returns the Type 2 charstring for the given CID.
	 *
	 * @param cid CID
	 * @throws IOException if the charstring could not be read
	 */
	public Type2CharString getType2CharString(int cid) throws IOException
	{
		if (cidFont != null)
		{
			return cidFont.getType2CharString(cid);
		}
		else
		{
			return t1Font.getType2CharString(cid);
		}
	}

	/**
	 * Returns the CID for the given character code. If not found then CID 0 is returned.
	 *
	 * @param code character code
	 * @return CID
	 */
	public int codeToCID(int code)
	{
		return parent.getCMap().toCID(code);
	}

	@Override
	public int codeToGID(int code)
	{
		int cid = codeToCID(code);
		if (cidFont != null)
		{
			// The CIDs shall be used to determine the GID value for the glyph procedure using the
			// charset table in the CFF program
			return cidFont.getCharset().getGIDForCID(cid);
		}
		else
		{
			// The CIDs shall be used directly as GID values
			return cid;
		}
	}

	@Override
	public byte[] encode(int unicode)
	{
		// todo: we can use a known character collection CMap for a CIDFont
		// and an Encoding for Type 1-equivalent
		throw new UnsupportedOperationException();
	}

	@Override
	public float getWidthFromFont(int code) throws IOException
	{
		int cid = codeToCID(code);
		int width = getType2CharString(cid).getWidth();

		PointF p = new PointF(width, 0);
		fontMatrixTransform.transform(p, p);
		return p.x;
	}

	@Override
	public boolean isEmbedded()
	{
		return isEmbedded;
	}

	@Override
	public boolean isDamaged()
	{
		return isDamaged;
	}

	@Override
	public float getHeight(int code) throws IOException
	{
		int cid = codeToCID(code);

		float height = 0;
		if (!glyphHeights.containsKey(cid))
		{
			height =  (float) getType2CharString(cid).getBounds().height();
			glyphHeights.put(cid, height);
		}
		return height;
	}

	@Override
	public float getAverageFontWidth()
	{
		if (avgWidth == null)
		{
			avgWidth = getAverageCharacterWidth();
		}
		return avgWidth;
	}

	// todo: this is a replacement for FontMetrics method
	private float getAverageCharacterWidth()
	{
		// todo: not implemented, highly suspect
		return 500;
	}
}
