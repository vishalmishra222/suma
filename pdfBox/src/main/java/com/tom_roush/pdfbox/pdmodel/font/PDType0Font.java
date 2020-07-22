package com.tom_roush.pdfbox.pdmodel.font;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.tom_roush.fontbox.cmap.CMap;
import com.tom_roush.fontbox.util.BoundingBox;
import com.tom_roush.pdfbox.cos.COSArray;
import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.util.Matrix;
import com.tom_roush.pdfbox.util.Vector;

import android.util.Log;

/**
 * A Composite (Type 0) font.
 *
 * @author Ben Litchfield
 */
public class PDType0Font extends PDFont
{
	private final PDCIDFont descendantFont;
	private CMap cMap, cMapUCS2;
	private boolean isCMapPredefined;
	private PDCIDFontType2Embedder embedder;

	/**
	 * Loads a TTF to be embedded into a document.
	 *
	 * @param doc The PDF document that will hold the embedded font.
	 * @param file A TrueType font.
	 * @return A Type0 font with a CIDFontType2 descendant.
	 * @throws IOException If there is an error reading the font file.
	 */
	public static PDType0Font load(PDDocument doc, File file) throws IOException
	{
		return new PDType0Font(doc, new FileInputStream(file), true);
	}

	/**
	 * Loads a TTF to be embedded into a document.
	 *
	 * @param doc The PDF document that will hold the embedded font.
	 * @param input A TrueType font.
	 * @return A Type0 font with a CIDFontType2 descendant.
	 * @throws IOException If there is an error reading the font stream.
	 */
	public static PDType0Font load(PDDocument doc, InputStream input) throws IOException
	{
		return new PDType0Font(doc, input, true);
	}

	/**
	 * Loads a TTF to be embedded into a document.
	 *
	 * @param doc The PDF document that will hold the embedded font.
	 * @param input A TrueType font.
	 * @param embedSubset True if the font will be subset before embedding
	 * @return A Type0 font with a CIDFontType2 descendant.
	 * @throws IOException If there is an error reading the font stream.
	 */
	public static PDType0Font load(PDDocument doc, InputStream input, boolean embedSubset)
			throws IOException
	{
		return new PDType0Font(doc, input, embedSubset);
	}

	/**
	 * Constructor for reading a Type0 font from a PDF file.
	 * 
	 * @param fontDictionary The font dictionary according to the PDF specification.
	 */
	public PDType0Font(COSDictionary fontDictionary) throws IOException
	{
		super(fontDictionary);
		COSArray descendantFonts = (COSArray)dict.getDictionaryObject(COSName.DESCENDANT_FONTS);
		COSDictionary descendantFontDictionary = (COSDictionary) descendantFonts.getObject(0);

		if (descendantFontDictionary == null)
		{
			throw new IOException("Missing descendant font dictionary");
		}

		readEncoding();
		fetchCMapUCS2();
		descendantFont = PDFontFactory.createDescendantFont(descendantFontDictionary, this);
	}

	/**
	 * Private. Creates a new TrueType font for embedding.
	 */
	private PDType0Font(PDDocument document, InputStream ttfStream, boolean embedSubset)
			throws IOException
	{
		embedder = new PDCIDFontType2Embedder(document, dict, ttfStream, embedSubset, this);
		descendantFont = embedder.getCIDFont();
		readEncoding();
		fetchCMapUCS2();
	}

	@Override
	public void addToSubset(int codePoint)
	{
		if (!willBeSubset())
		{
			throw new IllegalStateException("This font was created with subsetting disabled");
		}
		embedder.addToSubset(codePoint);
	}

	@Override
	public void subset() throws IOException
	{
		if (!willBeSubset())
		{
			throw new IllegalStateException("This font was created with subsetting disabled");
		}
		embedder.subset();
	}
	
	 @Override
	 public boolean willBeSubset()
	{
		 return embedder.needsSubset();
	}

	/**
	 * Reads the font's Encoding entry, which should be a CMap name/stream.
	 */
	private void readEncoding() throws IOException
    {
        COSBase encoding = dict.getDictionaryObject(COSName.ENCODING);
        if (encoding instanceof COSName)
        {
            // predefined CMap
            COSName encodingName = (COSName) encoding;
            cMap = CMapManager.getPredefinedCMap(encodingName.getName());
            if (cMap != null)
            {
                isCMapPredefined = true;
            }
            else
            {
                throw new IOException("Missing required CMap");
            }
        }
        else if (encoding != null)
        {
            cMap = readCMap(encoding);
            if (cMap == null)
            {
                throw new IOException("Missing required CMap");
            }
            else if (!cMap.hasCIDMappings())
            {
            	Log.w("PdfBoxAndroid", "Invalid Encoding CMap in font " + getName());
            }
        }
    }

	/**
	 * Fetches the corresponding UCS2 CMap if the font's CMap is predefined.
	 */
	private void fetchCMapUCS2() throws IOException
	{
		// if the font is composite and uses a predefined cmap (excluding Identity-H/V) then
		// or if its decendant font uses Adobe-GB1/CNS1/Japan1/Korea1
		if (isCMapPredefined)
		{
			// a) Map the character code to a CID using the font's CMap
			// b) Obtain the ROS from the font's CIDSystemInfo
			// c) Construct a second CMap name by concatenating the ROS in the format "R-O-UCS2"
			// d) Obtain the CMap with the constructed name
			// e) Map the CID according to the CMap from step d), producing a Unicode value

			String cMapName = null;

			// get the encoding CMap
			COSBase encoding = dict.getDictionaryObject(COSName.ENCODING);
			if (encoding instanceof COSName)
			{
				cMapName = ((COSName)encoding).getName();
			}

			// try to find the corresponding Unicode (UC2) CMap
			if (cMapName != null && !cMapName.equals("Identity-H") &&
					!cMapName.equals("Identity-V"))
			{
				CMap cMap = CMapManager.getPredefinedCMap(cMapName);
				if (cMap != null)
				{
					String ucs2Name = cMap.getRegistry() + "-" + cMap.getOrdering() + "-UCS2";
					CMap ucs2CMap = CMapManager.getPredefinedCMap(ucs2Name);
					if (ucs2CMap != null)
					{
						cMapUCS2 = ucs2CMap;
					}
				}
			}
		}
	}

	/**
	 * Returns the PostScript name of the font.
	 */
	public String getBaseFont()
	{
		return dict.getNameAsString(COSName.BASE_FONT);
	}

	/**
	 * Returns the descendant font.
	 */
	public PDCIDFont getDescendantFont()
	{
		return descendantFont;
	}

	/**
	 * Returns the font's CMap.
	 */
	public CMap getCMap()
	{
		return cMap;
	}

	/**
	 * Returns the font's UCS2 CMap, only present this font uses a predefined CMap.
	 */
	public CMap getCMapUCS2()
	{
		return cMapUCS2;
	}

	@Override
	public PDFontDescriptor getFontDescriptor()
	{
		return descendantFont.getFontDescriptor();
	}

	@Override
	public Matrix getFontMatrix()
	{
		return descendantFont.getFontMatrix();
	}

	@Override
	public boolean isVertical()
	{
		return cMap.getWMode() == 1;
	}

	@Override
	public float getHeight(int code) throws IOException
	{
		return descendantFont.getHeight(code);
	}

	@Override
	protected byte[] encode(int unicode) throws IOException
	{
		return descendantFont.encode(unicode);
	}

	@Override
	public float getAverageFontWidth()
	{
		return descendantFont.getAverageFontWidth();
	}

	@Override
	public Vector getPositionVector(int code)
	{
		// units are always 1/1000 text space, font matrix is not used, see FOP-2252
		return descendantFont.getPositionVector(code).scale(-1 / 1000f);
	}

	@Override
	public Vector getDisplacement(int code) throws IOException
	{
		if (isVertical())
		{
			return new Vector(0, descendantFont.getVerticalDisplacementVectorY(code) / 1000f);
		}
		else
		{
			return super.getDisplacement(code);
		}
	}

	@Override
	public float getWidth(int code) throws IOException
	{
		return descendantFont.getWidth(code);
	}

	@Override
	public float getWidthFromFont(int code) throws IOException
	{
		return descendantFont.getWidthFromFont(code);
	}

	@Override
	public boolean isEmbedded()
	{
		return descendantFont.isEmbedded();
	}

	@Override
	public String toUnicode(int code) throws IOException
	{
		// try to use a ToUnicode CMap
		String unicode = super.toUnicode(code);
		if (unicode != null)
		{
			return unicode;
		}

		if (isCMapPredefined && cMapUCS2 != null)
		{
			// if the font is composite and uses a predefined cmap (excluding Identity-H/V) then
			// or if its decendant font uses Adobe-GB1/CNS1/Japan1/Korea1

			// a) Map the character code to a character identifier (CID) according to the font?s CMap
			int cid = codeToCID(code);

			// e) Map the CID according to the CMap from step d), producing a Unicode value
			return cMapUCS2.toUnicode(cid);
		}
		else
		{
			// if no value has been produced, there is no way to obtain Unicode for the character.
			return null;
		}
	}

	@Override
	public String getName()
	{
		return getBaseFont();
	}

	@Override
	public BoundingBox getBoundingBox() throws IOException
	{
		return descendantFont.getBoundingBox();
	}

	@Override
	public int readCode(InputStream in) throws IOException
	{
		return cMap.readCode(in);
	}

	/**
	 * Returns the CID for the given character code. If not found then CID 0 is returned.
	 *
	 * @param code character code
	 * @return CID
	 */
	public int codeToCID(int code)
	{
		return descendantFont.codeToCID(code);
	}

	/**
	 * Returns the GID for the given character code.
	 *
	 * @param code character code
	 * @return GID
	 */
	public int codeToGID(int code) throws IOException
	{
		return descendantFont.codeToGID(code);
	}

	@Override
	public boolean isStandard14()
	{
		return false;
	}

	@Override
	public boolean isDamaged()
	{
		return descendantFont.isDamaged();
	}

	@Override
	public String toString()
	{
		String descendant = null;
		if (getDescendantFont() != null)
		{
			descendant = getDescendantFont().getClass().getSimpleName();
		}
		return getClass().getSimpleName() + "/" + descendant + " " + getBaseFont();
	}
}
