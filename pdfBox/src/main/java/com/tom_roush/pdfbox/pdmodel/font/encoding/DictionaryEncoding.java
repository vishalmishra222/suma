package com.tom_roush.pdfbox.pdmodel.font.encoding;

import java.util.HashMap;
import java.util.Map;

import com.tom_roush.pdfbox.cos.COSArray;
import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.cos.COSNumber;

import android.util.Log;

/**
 * This will perform the encoding from a dictionary.
 *
 * @author Ben Litchfield
 */
public class DictionaryEncoding extends Encoding
{
	private final COSDictionary encoding;
	private final Encoding baseEncoding;
	private final Map<Integer, String> differences = new HashMap<Integer, String>();

	/**
	 * Creates a new DictionaryEncoding for embedding.
	 */
	public DictionaryEncoding(COSName baseEncoding, COSArray differences)
	{
		encoding = new COSDictionary();
		encoding.setItem(COSName.NAME, COSName.ENCODING);
		encoding.setItem(COSName.DIFFERENCES, differences);
		if (baseEncoding != COSName.STANDARD_ENCODING)
		{
			encoding.setItem(COSName.BASE_ENCODING, baseEncoding);
			this.baseEncoding = Encoding.getInstance(baseEncoding);
		}
		else
		{
			this.baseEncoding = Encoding.getInstance(baseEncoding);
		}

		if (this.baseEncoding == null)
		{
			throw new IllegalArgumentException("Invalid encoding: " + baseEncoding);
		}
	}

	/**
	 * Creates a new DictionaryEncoding from a PDF.
	 *
	 * @param fontEncoding The encoding dictionary.
	 */
	public DictionaryEncoding(COSDictionary fontEncoding, boolean isNonSymbolic, Encoding builtIn)
	{
		encoding = fontEncoding;

		Encoding base = null;
		if (encoding.containsKey(COSName.BASE_ENCODING))
		{
			COSName name = encoding.getCOSName(COSName.BASE_ENCODING);
			base = Encoding.getInstance(name); // may be null
		}

		if (base == null)
		{
			if (isNonSymbolic)
			{
				// Otherwise, for a nonsymbolic font, it is StandardEncoding
				base = StandardEncoding.INSTANCE;
			}
			else
			{
				// and for a symbolic font, it is the font's built-in encoding.
				if (builtIn != null)
				{
					base = builtIn;
				}
				else
				{
					base = StandardEncoding.INSTANCE;
					Log.w("PdfBoxAndroid", "Built-in encoding required for symbolic font, using standard encoding");
					//FIXME, see PDFBOX-2299, happens with Type3 fonts of the isartor test suite
					// throw new IllegalArgumentException("Built-in Encoding required for symbolic font");throw new IllegalArgumentException("Built-in Encoding required for symbolic font");
				}
			}
		}
		baseEncoding = base;

		codeToName.putAll( baseEncoding.codeToName );
		names.addAll( baseEncoding.names );

		// now replace with the differences
		COSArray differences = (COSArray)encoding.getDictionaryObject( COSName.DIFFERENCES );
		int currentIndex = -1;
		for( int i=0; differences != null && i<differences.size(); i++ )
		{
			COSBase next = differences.getObject( i );
			if( next instanceof COSNumber )
			{
				currentIndex = ((COSNumber)next).intValue();
			}
			else if( next instanceof COSName )
			{
				COSName name = (COSName)next;
				add(currentIndex, name.getName());
				this.differences.put(currentIndex, name.getName());
				currentIndex++;
			}
		}
	}

	/**
	 * Returns the base encoding.
	 */
	public Encoding getBaseEncoding()
	{
		return baseEncoding;
	}

	/**
	 * Returns the Differences array.
	 */
	public Map<Integer, String> getDifferences()
	{
		return differences;
	}

	/**
	 * Convert this standard java object to a COS object.
	 *
	 * @return The cos object that matches this Java object.
	 */
	public COSBase getCOSObject()
	{
		return encoding;
	}
}
