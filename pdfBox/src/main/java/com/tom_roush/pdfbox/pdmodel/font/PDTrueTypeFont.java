package com.tom_roush.pdfbox.pdmodel.font;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.tom_roush.fontbox.ttf.CmapSubtable;
import com.tom_roush.fontbox.ttf.CmapTable;
import com.tom_roush.fontbox.ttf.GlyphData;
import com.tom_roush.fontbox.ttf.TTFParser;
import com.tom_roush.fontbox.ttf.TrueTypeFont;
import com.tom_roush.fontbox.util.BoundingBox;
import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.common.PDStream;
import com.tom_roush.pdfbox.pdmodel.font.encoding.Encoding;
import com.tom_roush.pdfbox.pdmodel.font.encoding.GlyphList;
import com.tom_roush.pdfbox.pdmodel.font.encoding.MacOSRomanEncoding;

import android.util.Log;

/**
 * TrueType font.
 * 
 * @author Ben Litchfield
 */
public class PDTrueTypeFont extends PDSimpleFont
{
	private static final int START_RANGE_F000 = 0xF000;
	private static final int START_RANGE_F100 = 0xF100;
	private static final int START_RANGE_F200 = 0xF200;

	private static final Map<String, Integer> INVERTED_MACOS_ROMAN = new HashMap<String, Integer>();
	static
	{
		Map<Integer, String> codeToName = MacOSRomanEncoding.INSTANCE.getCodeToNameMap();
		for (Map.Entry<Integer, String> entry : codeToName.entrySet())
		{
			if (!INVERTED_MACOS_ROMAN.containsKey(entry.getValue()))
			{
				INVERTED_MACOS_ROMAN.put(entry.getValue(), entry.getKey());
			}
		}
	}

	/**
	 * Loads a TTF to be embedded into a document.
	 *
	 * @param doc The PDF document that will hold the embedded font.
	 * @param file a ttf file.
	 * @return a PDTrueTypeFont instance.
	 * @throws IOException If there is an error loading the data.
	 * 
	 * @deprecated Use {@link PDType0Font#load(PDDocument, File)} instead.
	 */
	@Deprecated
	public static PDTrueTypeFont loadTTF(PDDocument doc, File file) throws IOException
	{
		return new PDTrueTypeFont(doc, new FileInputStream(file));
	}

	/**
	 * Loads a TTF to be embedded into a document.
	 *
	 * @param doc The PDF document that will hold the embedded font.
	 * @param input a ttf file stream
	 * @return a PDTrueTypeFont instance.
	 * @throws IOException If there is an error loading the data.
	 * 
	 * @deprecated Use {@link PDType0Font#load(PDDocument, InputStream)} instead.
	 */
	@Deprecated
	public static PDTrueTypeFont loadTTF(PDDocument doc, InputStream input) throws IOException
	{
		return new PDTrueTypeFont(doc, input);
	}

	private CmapSubtable cmapWinUnicode = null;
	private CmapSubtable cmapWinSymbol = null;
	private CmapSubtable cmapMacRoman = null;
	private boolean cmapInitialized = false;

	private final TrueTypeFont ttf;
	private final boolean isEmbedded;
	private final boolean isDamaged;

	/**
	 * Creates a new TrueType font from a Font dictionary.
	 *
	 * @param fontDictionary The font dictionary according to the PDF specification.
	 */
	public PDTrueTypeFont(COSDictionary fontDictionary) throws IOException
	{
		super(fontDictionary);

		TrueTypeFont ttfFont = null;
		boolean fontIsDamaged = false;
		if (getFontDescriptor() != null)
		{
			PDFontDescriptor fd = super.getFontDescriptor();
			PDStream ff2Stream = fd.getFontFile2();
			if (ff2Stream != null)
			{
				try
				{
					// embedded
					TTFParser ttfParser = new TTFParser(true);
					ttfFont = ttfParser.parse(ff2Stream.createInputStream());
				}
				catch (NullPointerException e) // TTF parser is buggy
				{
					Log.w("PdfBoxAndroid", "Could not read embedded TTF for font " + getBaseFont(), e);
					fontIsDamaged = true;
				}
				catch (IOException e)
				{
					Log.w("PdfBoxAndroid", "Could not read embedded TTF for font " + getBaseFont(), e);
					fontIsDamaged = true;
				}
			}
		}
		isEmbedded = ttfFont != null;
		isDamaged = fontIsDamaged;

		// substitute
		if (ttfFont == null)
		{
			ttfFont = ExternalFonts.getTrueTypeFont(getBaseFont());

			// fallback
			if (ttfFont == null)
			{
				ttfFont = ExternalFonts.getTrueTypeFallbackFont(getFontDescriptor());
				Log.w("PdfBoxAndroid", "Using fallback font '" + ttfFont + "' for '" + getBaseFont() + "'");
			}
		}
		ttf = ttfFont;
		readEncoding();
	}

	/**
	 * Returns the PostScript name of the font.
	 */
	public String getBaseFont()
	{
		return dict.getNameAsString(COSName.BASE_FONT);
	}

	@Override
	protected Encoding readEncodingFromFont() throws IOException
	{
		// for symbolic fonts the (3, 0) (Windows, Symbol) cmap is the font's built-in encoding
		// but this is handled by codeToGID
		return null;
	}

	/**
	 * Creates a new TrueType font for embedding.
	 */
	private PDTrueTypeFont(PDDocument document, InputStream ttfStream) throws IOException
	{
		PDTrueTypeFontEmbedder embedder = new PDTrueTypeFontEmbedder(document, dict, ttfStream);
		encoding = embedder.getFontEncoding();
		ttf = embedder.getTrueTypeFont();
		setFontDescriptor(embedder.getFontDescriptor());
		isEmbedded = true;
		isDamaged = false;
		glyphList = GlyphList.getAdobeGlyphList();
	}

	@Override
	public int readCode(InputStream in) throws IOException
	{
		return in.read();
	}

	@Override
	public String getName()
	{
		return getBaseFont();
	}

	@Override
	public BoundingBox getBoundingBox() throws IOException
	{
		return ttf.getFontBBox();
	}

	@Override
	public boolean isDamaged()
	{
		return isDamaged;
	}

	/**
	 * Returns the embedded or substituted TrueType font.
	 */
	public TrueTypeFont getTrueTypeFont()
	{
		return ttf;
	}

	//    @Override TODO
	public float getWidthFromFont(int code) throws IOException
	{
		if (getStandard14AFM() != null && getEncoding() != null)
		{
			return getStandard14Width(code);
		}

		int gid = codeToGID(code);
		float width = ttf.getAdvanceWidth(gid);
		float unitsPerEM = ttf.getUnitsPerEm();
		if (unitsPerEM != 1000)
		{
			width *= 1000f / unitsPerEM;
		}
		return width;
	}

	//    @Override TODO
	public float getHeight(int code) throws IOException
	{
		int gid = codeToGID(code);
		GlyphData glyph = ttf.getGlyph().getGlyph(gid);
		if (glyph != null)
		{
			return glyph.getBoundingBox().getHeight();
		}
		return 0;
	}

	@Override
	protected byte[] encode(int unicode) throws IOException
	{
		if (!getEncoding().contains(getGlyphList().codePointToName(unicode)))
		{
			throw new IllegalArgumentException("This font type only supports 8-bit code points");
		}

		int gid = codeToGID(unicode);
		if (gid == 0)
		{
			throw new IllegalArgumentException(
					String.format("U+%04X is not available in this font's Encoding", unicode));
		}

		return new byte[] { (byte)unicode };
	}

	@Override
	public boolean isEmbedded()
	{
		return isEmbedded;
	}

	/**
	 * Returns the GID for the given character code.
	 *
	 * @param code character code
	 * @return GID (glyph index)
	 */
	public int codeToGID(int code) throws IOException
	{
		extractCmapTable();
		int gid = 0;

		if (!isSymbolic()) // non-symbolic
		{
			String name = encoding.getName(code);
			if (name.equals(".notdef"))
			{
				return 0;
			}
			else
			{
				// (3, 1) - (Windows, Unicode)
				if (cmapWinUnicode != null)
				{
					String unicode = GlyphList.getAdobeGlyphList().toUnicode(name);
					if (unicode != null)
					{
						int uni = unicode.codePointAt(0);
						gid = cmapWinUnicode.getGlyphId(uni);
					}
				}

				// (1, 0) - (Macintosh, Roman)
				if (gid == 0 && cmapMacRoman != null)
				{
					Integer macCode = INVERTED_MACOS_ROMAN.get(name);
					if (macCode != null)
					{
						gid = cmapMacRoman.getGlyphId(macCode);
					}
				}

				// 'post' table
				if (gid == 0)
				{
					gid = ttf.nameToGID(name);
				}
			}
		}
		else // symbolic
		{
			// (3, 0) - (Windows, Symbol)
			if (cmapWinSymbol != null)
			{
				gid = cmapWinSymbol.getGlyphId(code);
				if (code >= 0 && code <= 0xFF)
				{
					// the CMap may use one of the following code ranges,
					// so that we have to add the high byte to get the
					// mapped value
					if (gid == 0)
					{
						// F000 - F0FF
						gid = cmapWinSymbol.getGlyphId(code + START_RANGE_F000);
					}
					if (gid == 0)
					{
						// F100 - F1FF
						gid = cmapWinSymbol.getGlyphId(code + START_RANGE_F100);
					}
					if (gid == 0)
					{
						// F200 - F2FF
						gid = cmapWinSymbol.getGlyphId(code + START_RANGE_F200);
					}
				}
			}

			// (1, 0) - (Mac, Roman)
			if (gid == 0 && cmapMacRoman != null)
			{
				gid = cmapMacRoman.getGlyphId(code);
			}
		}

		if (gid == 0)
		{
			Log.w("PdfBoxAndroid", "Can't map code " + code + " in font " + getBaseFont());
		}

		return gid;
	}

	/**
	 * extract all useful "cmap" subtables.
	 */
	private void extractCmapTable() throws IOException
	{
		if (cmapInitialized)
		{
			return;
		}

		CmapTable cmapTable = ttf.getCmap();
		if (cmapTable != null)
		{
			// get all relevant "cmap" subtables
			CmapSubtable[] cmaps = cmapTable.getCmaps();
			for (CmapSubtable cmap : cmaps)
			{
				if (CmapTable.PLATFORM_WINDOWS == cmap.getPlatformId())
				{
					if (CmapTable.ENCODING_WIN_UNICODE_BMP == cmap.getPlatformEncodingId())
					{
						cmapWinUnicode = cmap;
					}
					else if (CmapTable.ENCODING_WIN_SYMBOL == cmap.getPlatformEncodingId())
					{
						cmapWinSymbol = cmap;
					}
				}
				else if (CmapTable.PLATFORM_MACINTOSH == cmap.getPlatformId()
						&& CmapTable.ENCODING_MAC_ROMAN == cmap.getPlatformEncodingId())
				{
					cmapMacRoman = cmap;
				}
			}
		}
		cmapInitialized = true;
	}
}
