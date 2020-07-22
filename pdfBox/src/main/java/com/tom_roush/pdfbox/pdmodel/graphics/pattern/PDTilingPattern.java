package com.tom_roush.pdfbox.pdmodel.graphics.pattern;

import com.tom_roush.pdfbox.contentstream.PDContentStream;
import com.tom_roush.pdfbox.cos.COSArray;
import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.cos.COSStream;
import com.tom_roush.pdfbox.pdmodel.PDResources;
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle;

/**
 * A tiling pattern dictionary.
 */
public class PDTilingPattern extends PDAbstractPattern implements PDContentStream
{
	/** paint type 1 = colored tiling pattern. */
	public static final int PAINT_COLORED = 1;

	/** paint type 2 = uncolored tiling pattern. */
	public static final int PAINT_UNCOLORED = 2;

	/** tiling type 1 = constant spacing.*/
	public static final int TILING_CONSTANT_SPACING = 1;

	/**  tiling type 2 = no distortion. */
	public static final int TILING_NO_DISTORTION = 2;

	/** tiling type 3 = constant spacing and faster tiling. */
	public static final int TILING_CONSTANT_SPACING_FASTER_TILING = 3;

	/**
	 * Creates a new tiling pattern.
	 */
	public PDTilingPattern()
	{
		super();
		getCOSDictionary().setInt(COSName.PATTERN_TYPE, PDAbstractPattern.TYPE_TILING_PATTERN);
	}

	/**
	 * Creates a new tiling pattern from the given COS dictionary.
	 * @param resourceDictionary The COSDictionary for this pattern resource.
	 */
	public PDTilingPattern(COSDictionary resourceDictionary)
	{
		super(resourceDictionary);
	}

	@Override
	public int getPatternType()
	{
		return PDAbstractPattern.TYPE_TILING_PATTERN;
	}

	/**
	 * This will set the length of the content stream.
	 * @param length The new stream length.
	 */
	@Override
	public void setLength(int length)
	{
		getCOSDictionary().setInt(COSName.LENGTH, length);
	}

	/**
	 * This will return the length of the content stream.
	 * @return The length of the content stream
	 */
	@Override
	public int getLength()
	{
		return getCOSDictionary().getInt( COSName.LENGTH, 0 );
	}

	/**
	 * This will set the paint type.
	 * @param paintType The new paint type.
	 */
	@Override
	public void setPaintType(int paintType)
	{
		getCOSDictionary().setInt(COSName.PAINT_TYPE, paintType);
	}

	/**
	 * This will return the paint type.
	 * @return The paint type
	 */
	public int getPaintType()
	{
		return getCOSDictionary().getInt( COSName.PAINT_TYPE, 0 );
	}

	/**
	 * This will set the tiling type.
	 * @param tilingType The new tiling type.
	 */
	public void setTilingType(int tilingType)
	{
		getCOSDictionary().setInt(COSName.TILING_TYPE, tilingType);
	}

	/**
	 * This will return the tiling type.
	 * @return The tiling type
	 */
	public int getTilingType()
	{
		return getCOSDictionary().getInt( COSName.TILING_TYPE, 0 );
	}

	/**
	 * This will set the XStep value.
	 * @param xStep The new XStep value.
	 */
	public void setXStep(float xStep)
	{
		getCOSDictionary().setFloat(COSName.X_STEP, xStep);
	}

	/**
	 * This will return the XStep value.
	 * @return The XStep value
	 */
	public float getXStep()
	{
		// ignores invalid values, see PDFBOX-1094-065514-XStep32767.pdf
		float xStep = getCOSDictionary().getFloat( COSName.X_STEP, 0 );
		return xStep == Short.MAX_VALUE ? 0 : xStep;
	}

	/**
	 * This will set the YStep value.
	 * @param yStep The new YStep value.
	 */
	public void setYStep(float yStep)
	{
		getCOSDictionary().setFloat(COSName.Y_STEP, yStep);
	}

	/**
	 * This will return the YStep value.
	 * @return The YStep value
	 */
	public float getYStep()
	{
		// ignores invalid values, see PDFBOX-1094-065514-XStep32767.pdf
		float yStep = getCOSDictionary().getFloat( COSName.Y_STEP, 0 );
		return yStep == Short.MAX_VALUE ? 0 : yStep;
	}

	@Override
	public COSStream getContentStream() {
		return (COSStream)getCOSObject();
	}

	/**
	 * This will get the resources for this pattern.
	 * This will return null if no resources are available at this level.
	 * @return The resources for this pattern.
	 */
	@Override
	public PDResources getResources()
	{
		PDResources retval = null;
		COSDictionary resources = (COSDictionary)getCOSDictionary()
				.getDictionaryObject( COSName.RESOURCES );
		if( resources != null )
		{
			retval = new PDResources( resources );
		}
		return retval;
	}

	/**
	 * This will set the resources for this pattern.
	 * @param resources The new resources for this pattern.
	 */
	public void setResources( PDResources resources )
	{
		if (resources != null)
		{
			getCOSDictionary().setItem( COSName.RESOURCES, resources );
		}
		else
		{
			getCOSDictionary().removeItem( COSName.RESOURCES );
		}
	}

	/**
	 * An array of four numbers in the form coordinate system (see
	 * below), giving the coordinates of the left, bottom, right, and top edges,
	 * respectively, of the pattern's bounding box.
	 *
	 * @return The BBox of the pattern.
	 */
	@Override
	public PDRectangle getBBox()
	{
		PDRectangle retval = null;
		COSArray array = (COSArray)getCOSDictionary().getDictionaryObject( COSName.BBOX );
		if( array != null )
		{
			retval = new PDRectangle( array );
		}
		return retval;
	}

	/**
	 * This will set the BBox (bounding box) for this Pattern.
	 * @param bbox The new BBox for this Pattern.
	 */
	public void setBBox(PDRectangle bbox)
	{
		if( bbox == null )
		{
			getCOSDictionary().removeItem( COSName.BBOX );
		}
		else
		{
			getCOSDictionary().setItem( COSName.BBOX, bbox.getCOSArray() );
		}
	}
}