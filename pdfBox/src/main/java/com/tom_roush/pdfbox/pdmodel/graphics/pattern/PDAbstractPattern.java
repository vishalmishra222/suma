package com.tom_roush.pdfbox.pdmodel.graphics.pattern;

import java.io.IOException;

import com.tom_roush.pdfbox.cos.COSArray;
import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.cos.COSFloat;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.pdmodel.common.COSObjectable;
import com.tom_roush.pdfbox.util.Matrix;
import com.tom_roush.pdfbox.util.awt.AffineTransform;

/**
 * A Pattern dictionary from a page's resources.
 */
public abstract class PDAbstractPattern implements COSObjectable
{
    /** Tiling pattern type. */
    public static final int TYPE_TILING_PATTERN = 1;

    /** Shading pattern type. */
    public static final int TYPE_SHADING_PATTERN = 2;

    /**
     * Create the correct PD Model pattern based on the COS base pattern.
     * @param resourceDictionary the COS pattern dictionary
     * @return the newly created pattern resources object
     * @throws IOException If we are unable to create the PDPattern object.
     */
    public static PDAbstractPattern create(COSDictionary resourceDictionary) throws IOException
    {
        PDAbstractPattern pattern;
        int patternType = resourceDictionary.getInt(COSName.PATTERN_TYPE, 0);
        switch (patternType)
        {
            case TYPE_TILING_PATTERN:
                pattern = new PDTilingPattern(resourceDictionary);
                break;
            case TYPE_SHADING_PATTERN:
                pattern = new PDShadingPattern(resourceDictionary);
                break;
            default:
                throw new IOException("Error: Unknown pattern type " + patternType);
        }
        return pattern;
    }

    private final COSDictionary patternDictionary;

    /**
     * Creates a new Pattern dictionary.
     */
    public PDAbstractPattern()
    {
        patternDictionary = new COSDictionary();
        patternDictionary.setName(COSName.TYPE, COSName.PATTERN.getName());
    }

    /**
     * Creates a new Pattern dictionary from the given COS dictionary.
     * @param resourceDictionary The COSDictionary for this pattern resource.
     */
    public PDAbstractPattern(COSDictionary resourceDictionary)
    {
        patternDictionary = resourceDictionary;
    }

    /**
     * This will get the underlying dictionary.
     * @return The dictionary for these pattern resources.
     */
    public COSDictionary getCOSDictionary()
    {
        return patternDictionary;
    }

    /**
     * Convert this standard java object to a COS object.
     * @return The cos object that matches this Java object.
     */
    @Override
    public COSBase getCOSObject()
    {
        return patternDictionary;
    }

    /**
     * Sets the filter entry of the encryption dictionary.
     * @param filter The filter name.
     */
    public void setFilter(String filter)
    {
        patternDictionary.setItem(COSName.FILTER, COSName.getPDFName(filter));
    }

    /**
     * Get the name of the filter.
     * @return The filter name contained in this encryption dictionary.
     */
    public String getFilter()
    {
        return patternDictionary.getNameAsString(COSName.FILTER);
    }

    /**
     * This will set the length of the content stream.
     * @param length The new stream length.
     */
    public void setLength(int length)
    {
        patternDictionary.setInt(COSName.LENGTH, length);
    }

    /**
     * This will return the length of the content stream.
     * @return The length of the content stream
     */
    public int getLength()
    {
        return patternDictionary.getInt(COSName.LENGTH, 0);
    }

    /**
     * This will set the paint type.
     * @param paintType The new paint type.
     */
    public void setPaintType(int paintType)
    {
        patternDictionary.setInt(COSName.PAINT_TYPE, paintType);
    }

    /**
     * This will return the paint type.
     * @return The type of object that this is.
     */
    public String getType()
    {
        return COSName.PATTERN.getName();
    }

    /**
     * This will set the pattern type.
     * @param patternType The new pattern type.
     */
    public void setPatternType(int patternType)
    {
        patternDictionary.setInt(COSName.PATTERN_TYPE, patternType);
    }

    /**
     * This will return the pattern type.
     * @return The pattern type
     */
    public abstract int getPatternType();
    
    /**
     * Returns the pattern matrix, or the identity matrix is none is available.
     */
    public Matrix getMatrix()
    {
    	COSArray array = (COSArray)getCOSDictionary().getDictionaryObject(COSName.MATRIX);
    	if (array != null)
    	{
    		return new Matrix(array);
    	}
    	else
    	{
    		// default value is the identity matrix
    		return new Matrix();
    	}
    }

    /**
     * Sets the optional Matrix entry for the Pattern.
     * @param transform the transformation matrix
     */
    public void setMatrix(AffineTransform transform)
    {
    	COSArray matrix = new COSArray();
    	double[] values = new double[6];
    	transform.getMatrix(values);
    	for (double v : values)
    	{
    		matrix.add(new COSFloat((float)v));
    	}
    	getCOSDictionary().setItem(COSName.MATRIX, matrix);
    }
}
