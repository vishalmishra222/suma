package com.tom_roush.pdfbox.pdmodel.interactive.form;

import java.io.IOException;

import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.cos.COSNumber;
import com.tom_roush.pdfbox.cos.COSString;
import com.tom_roush.pdfbox.pdmodel.common.PDTextStream;

/**
 * Base class for fields which use "Variable Text".
 * These fields construct an appearance stream dynamically at viewing time.
 *
 * @author Ben Litchfield
 */
public abstract class PDVariableText extends PDField
{
	/**
	 * A Q value.
	 */
	public static final int QUADDING_LEFT = 0;

	/**
	 * A Q value.
	 */
	public static final int QUADDING_CENTERED = 1;

	/**
	 * A Q value.
	 */
	public static final int QUADDING_RIGHT = 2;

	/**
	 * @see PDField#PDField(PDAcroForm,COSDictionary)
	 *
	 * @param theAcroForm The acroform.
	 */
	PDVariableText(PDAcroForm theAcroForm)
	{
		super( theAcroForm );
	}

	/**
	 * Constructor.
	 * 
	 * @param theAcroForm The form that this field is part of.
	 * @param field the PDF object to represent as a field.
	 * @param parentNode the parent node of the node to be created
	 */
	protected PDVariableText(PDAcroForm theAcroForm, COSDictionary field, PDFieldTreeNode parentNode)
	{
		super( theAcroForm, field, parentNode);
	}

	/**
	 * Get the default appearance.
	 * 
	 * This is an inheritable attribute.
	 * 
	 * The default appearance contains a set of default graphics and text operators
	 * to define the field's text size and color.
	 * 
	 * @return the DA element of the dictionary object
	 */
	public String getDefaultAppearance()
	{
		COSString defaultAppearance = (COSString) getInheritableAttribute(COSName.DA);
		return defaultAppearance.getString();
	}

	/**
	 * Set the default appearance.
	 * 
	 * This will set the local default appearance for the variable text field only not 
	 * affecting a default appearance in the parent hierarchy.
	 * 
	 * Providing null as the value will remove the local default appearance.
	 * 
	 * @param daValue a string describing the default appearance
	 */
	public void setDefaultAppearance(String daValue)
	{
		if (daValue != null)
		{
			setInheritableAttribute(COSName.DA, new COSString(daValue));
		}
		else
		{
			removeInheritableAttribute(COSName.DA);
		}
	}

	/**
	 * Get the default style string.
	 *
	 * The default style string defines the default style for
	 * rich text fields.
	 *
	 * @return the DS element of the dictionary object
	 */
	public String getDefaultStyleString()
	{
		COSString defaultStyleString = (COSString)getDictionary().getDictionaryObject(COSName.DS);
		return defaultStyleString.getString();
	}

	/**
	 * Set the default style string.
	 *
	 * Providing null as the value will remove the default style string.
	 *
	 * @param defaultStyleString a string describing the default style.
	 */
	public void setDefaultStyleString(String defaultStyleString)
	{
		if (defaultStyleString != null)
		{
			getDictionary().setItem(COSName.DS, new COSString(defaultStyleString));
		}
		else
		{
			getDictionary().removeItem(COSName.DS);
		}
	}

	/**
	 * This will get the 'quadding' or justification of the text to be displayed.
	 * 
	 * This is an inheritable attribute.
	 * 
	 * 0 - Left(default)<br/>
	 * 1 - Centered<br />
	 * 2 - Right<br />
	 * Please see the QUADDING_CONSTANTS.
	 *
	 * @return The justification of the text strings.
	 */
	public int getQ()
	{
		int retval = 0;

		COSNumber number = (COSNumber)getInheritableAttribute(COSName.Q );
		
		if( number != null )
		{
			retval = number.intValue();
		}
		return retval;
	}

	/**
	 * This will set the quadding/justification of the text.  See QUADDING constants.
	 *
	 * @param q The new text justification.
	 */
	public void setQ( int q )
	{
		getDictionary().setInt( COSName.Q, q );
	}

	/**
	 * Get the fields rich text value.
	 *
	 * @return the rich text value string
	 */
	public String getRichTextValue() throws IOException
	{
		PDTextStream textStream = getAsTextStream(getInheritableAttribute(COSName.RV));

		if (textStream != null)
		{
			return textStream.getAsString();
		}
		
		return "";
	}
	
	/**
	 * Get the fields rich text value.
	 *
	 * The value is stored in the field dictionaries "V" entry.
	 *
	 * @return The value of this entry.
	 * @throws IOException if the field dictionary entry is not a text type
	 */
	public PDTextStream getRichTextValueAsStream() throws IOException
	{
		return getAsTextStream(getInheritableAttribute(COSName.RV));
	}

	/**
	 * Set the fields rich text value.
	 *
	 * <p>
	 * Setting the rich text value will not generate the appearance
	 * for the field.
	 *
	 * <br/>
	 * You can set {@link PDAcroForm#setNeedAppearances(Boolean)} to
	 * signal a conforming reader to generate the appearance stream.
	 *
	 * </p>
	 * <p>
	 * For long text it's more efficient to provide the text content as a
	 * text stream {@link #setRichTextValue(PDTextStream)}
	 * </p>
	 * 
	 * Providing null as the value will remove the default style string.
	 *
	 * @param richTextValue a rich text string
	 */
	public void setRichTextValue(String richTextValue)
	{
		// TODO stream instead of string
		if (richTextValue != null)
		{
			getDictionary().setItem(COSName.RV, new COSString(richTextValue));
		}
		else
		{
			getDictionary().removeItem(COSName.RV);
		}
	}
	
	/**
	 * Set the fields rich text value.
	 *
	 * Setting the rich text value will not generate the appearance
	 * for the field.
	 *
	 * You can set {@link PDAcroForm#setNeedAppearances(Boolean)} to
	 * signal a conforming reader to generate the appearance stream.
	 *
	 * Providing null as the value will remove the default style string.
	 *
	 * @param richTextValue a rich text string
	 */
	public void setRichTextValue(PDTextStream richTextValue)
	{
		if (richTextValue != null)
		{
			getDictionary().setItem(COSName.RV, richTextValue.getCOSObject());
		}
		else
		{
			getDictionary().removeItem(COSName.RV);
		}
	}
}