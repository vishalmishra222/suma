package com.tom_roush.pdfbox.pdmodel.interactive.annotation;

import com.tom_roush.pdfbox.cos.COSArray;
import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.cos.COSStream;
import com.tom_roush.pdfbox.pdmodel.common.COSObjectable;
import com.tom_roush.pdfbox.pdmodel.common.PDStream;
import com.tom_roush.pdfbox.pdmodel.graphics.color.PDColor;
import com.tom_roush.pdfbox.pdmodel.graphics.color.PDColorSpace;
import com.tom_roush.pdfbox.pdmodel.graphics.color.PDDeviceGray;
import com.tom_roush.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import com.tom_roush.pdfbox.pdmodel.graphics.form.PDFormXObject;

/**
 * This class represents an appearance characteristics dictionary.
 */
public class PDAppearanceCharacteristicsDictionary implements COSObjectable
{

    private final COSDictionary dictionary;

    /**
     * Constructor.
     * 
     * @param dict dictionary
     */
    public PDAppearanceCharacteristicsDictionary(COSDictionary dict)
    {
        this.dictionary = dict;
    }


    /**
     * returns the dictionary.
     * @return the dictionary
     */
    public COSDictionary getDictionary()
    {
        return this.dictionary;
    }

    /**
     * {@inheritDoc}
     * 
     */
    public COSBase getCOSObject()
    {
        return this.dictionary;
    }

    /**
     * This will retrieve the rotation of the annotation widget.
     * It must be a multiple of 90. Default is 0 
     * @return the rotation
     */
    public int getRotation()
    {
        return this.getDictionary().getInt(COSName.R, 0);
    }

    /**
     * This will set the rotation.
     * 
     * @param rotation the rotation as a multiple of 90
     */
    public void setRotation(int rotation)
    {
        this.getDictionary().setInt(COSName.R, rotation);
    }

    /**
     * This will retrieve the border color.
     * 
     * @return the border color.
     */
    public PDColor getBorderColour()
    {
    	return getColor(COSName.BC);
    }

    /**
     * This will set the border color.
     * 
     * @param c the border color
     */
    public void setBorderColour(PDColor c)
    {
    	this.getDictionary().setItem(COSName.BC, c.toCOSArray());
    }

    /**
     * This will retrieve the background color.
     * 
     * @return the background color.
     */
    public PDColor getBackground()
    {
    	return getColor(COSName.BG);
    }

    /**
     * This will set the background color.
     * 
     * @param c the background color
     */
    public void setBackground(PDColor c)
    {
    	this.getDictionary().setItem(COSName.BG, c.toCOSArray());
    }

    /**
     * This will retrieve the normal caption.
     * 
     * @return the normal caption.
     */
    public String getNormalCaption()
    {
        return this.getDictionary().getString("CA");
    }

    /**
     * This will set the normal caption.
     * 
     * @param caption the normal caption
     */
    public void setNormalCaption(String caption)
    {
        this.getDictionary().setString("CA", caption);
    }

    /**
     * This will retrieve the rollover caption.
     * 
     * @return the rollover caption.
     */
    public String getRolloverCaption()
    {
        return this.getDictionary().getString("RC");
    }

    /**
     * This will set the rollover caption.
     * 
     * @param caption the rollover caption
     */
    public void setRolloverCaption(String caption)
    {
        this.getDictionary().setString("RC", caption);
    }

    /**
     * This will retrieve the alternate caption.
     * 
     * @return the alternate caption.
     */
    public String getAlternateCaption()
    {
        return this.getDictionary().getString("AC");
    }

    /**
     * This will set the alternate caption.
     * 
     * @param caption the alternate caption
     */
    public void setAlternateCaption(String caption)
    {
        this.getDictionary().setString("AC", caption);
    }

    /**
     * This will retrieve the normal icon.
     * 
     * @return the normal icon.
     */
    public PDFormXObject getNormalIcon()
    {
        COSBase i = this.getDictionary().getDictionaryObject("I");
        if (i instanceof COSStream)
        {
            return new PDFormXObject(new PDStream((COSStream) i), "I");
        }
        return null;
    }

    /**
     * This will retrieve the rollover icon.
     * 
     * @return the rollover icon
     */
    public PDFormXObject getRolloverIcon()
    {
        COSBase i = this.getDictionary().getDictionaryObject("RI");
        if (i instanceof COSStream)
        {
            return new PDFormXObject(new PDStream((COSStream) i), "RI");
        }
        return null;
    }

    /**
     * This will retrieve the alternate icon.
     * 
     * @return the alternate icon.
     */
    public PDFormXObject getAlternateIcon()
    {
        COSBase i = this.getDictionary().getDictionaryObject("IX");
        if (i instanceof COSStream)
        {
            return new PDFormXObject(new PDStream((COSStream) i), "IX");
        }
        return null;
    }

    private PDColor getColor(COSName itemName)
    {
    	COSBase c = this.getDictionary().getItem(itemName);
    	if (c instanceof COSArray)
    	{
    		PDColorSpace colorSpace = null;
    		switch (((COSArray) c).size())
    		{
    		case 1:
    			colorSpace = PDDeviceGray.INSTANCE;
    			break;
    		case 3:
    			colorSpace = PDDeviceRGB.INSTANCE;
    			break;
//    		case 4:
//    			colorSpace = PDDeviceCMYK.INSTANCE;
//    			break; TODO
    		default:
    			break;
    		}
    		return new PDColor((COSArray) c, colorSpace);
    	}
    	return null;
    }
}
