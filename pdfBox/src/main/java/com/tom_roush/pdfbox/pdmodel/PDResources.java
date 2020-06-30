package com.tom_roush.pdfbox.pdmodel;

import java.io.IOException;
import java.util.Collections;

import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.cos.COSObject;
import com.tom_roush.pdfbox.pdmodel.common.COSObjectable;
import com.tom_roush.pdfbox.pdmodel.documentinterchange.markedcontent.PDPropertyList;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.font.PDFontFactory;
import com.tom_roush.pdfbox.pdmodel.graphics.PDXObject;
import com.tom_roush.pdfbox.pdmodel.graphics.color.PDColorSpace;
import com.tom_roush.pdfbox.pdmodel.graphics.form.PDFormXObject;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.tom_roush.pdfbox.pdmodel.graphics.optionalcontent.PDOptionalContentGroup;
import com.tom_roush.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import com.tom_roush.pdfbox.pdmodel.graphics.shading.PDShading;
import com.tom_roush.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

/**
 * A set of resources available at the page/pages/stream level.
 * 
 * @author Ben Litchfield
 * @author John Hewson
 */
public final class PDResources implements COSObjectable
{
	private final COSDictionary resources;

	/**
	 * Constructor for embedding.
	 */
	public PDResources()
	{
		resources = new COSDictionary();
	}

	/**
	 * Constructor for reading.
	 * 
	 * @param resourceDictionary The cos dictionary for this resource.
	 */
	// todo: replace this constructor with a static factory which can cache PDResources at will
	//       also it should probably take a COSBase so that it is indirect-object aware.
	//       It might also want to have some context, e.g. knowing what the parent of the resources is?
	public PDResources(COSDictionary resourceDictionary)
	{
		if (resourceDictionary == null)
		{
			throw new IllegalArgumentException("resourceDictionary is null");
		}
		resources = resourceDictionary;
	}

	/**
	 * Returns the underlying dictionary.
	 */
	public COSDictionary getCOSObject()
	{
		return resources;
	}

	/**
	 * Returns the font resource with the given name, or null if none exists.
	 */
	public PDFont getFont(COSName name) throws IOException
	{
		COSDictionary dict = (COSDictionary)get(COSName.FONT, name);
		if (dict == null)
		{
			return null;
		}
		return PDFontFactory.createFont(dict);
	}

	/**
	 * Returns the color space resource with the given name, or null if none exists.
	 */
	public PDColorSpace getColorSpace(COSName name) throws IOException
	{
		// get the instance
		COSBase object = get(COSName.COLORSPACE, name);
		if (object != null)
		{
			return PDColorSpace.create(object, this);
		}
		else
		{
			return PDColorSpace.create(name, this);
		}
	}

	/**
	 * Returns true if the given color space name exists in these resources.
	 * @param name color space name
	 */
	public boolean hasColorSpace(COSName name)
	{
		return get(COSName.COLORSPACE, name) != null;
	}

	/**
	 * Returns the external graphics state resource with the given name, or null if none exists.
	 */
	public PDExtendedGraphicsState getExtGState(COSName name) throws IOException
	{
		COSDictionary dict = (COSDictionary)get(COSName.EXT_G_STATE, name);
		if (dict == null)
		{
			return null;
		}
		return new PDExtendedGraphicsState(dict);
	}

	/**
	 * Returns the shading resource with the given name, or null if none exists.
	 */
	public PDShading getShading(COSName name) throws IOException
	{
		COSDictionary dict = (COSDictionary)get(COSName.SHADING, name);
		if (dict == null)
		{
			return null;
		}
		return PDShading.create(dict);
	}

	/**
	 * Returns the pattern resource with the given name, or null if none exists.
	 */
	public PDAbstractPattern getPattern(COSName name) throws IOException
	{
		COSDictionary dict = (COSDictionary)get(COSName.PATTERN, name);
		if (dict == null)
		{
			return null;
		}
		return PDAbstractPattern.create(dict);
	}

	/**
	 * Returns the property list resource with the given name, or null if none exists.
	 */
	public PDPropertyList getProperties(COSName name) throws IOException
	{
		COSDictionary dict = (COSDictionary)get(COSName.PROPERTIES, name);
		if (dict == null)
		{
			return null;
		}
		return PDPropertyList.create(dict);
	}

	/**
	 * Returns the XObject resource with the given name, or null if none exists.
	 */
	public PDXObject getXObject(COSName name) throws IOException
	{
		COSBase value = get(COSName.XOBJECT, name);
		if (value == null)
		{
			return null;
		}
		else if (value instanceof COSObject)
		{
			COSObject object = (COSObject)value;
			// add the object number to create an unique identifier
			String id = name.getName();
			id += "#" + object.getObjectNumber();
			return PDXObject.createXObject(object.getObject(), id, this);
		}
		else
		{
			return PDXObject.createXObject(value, name.getName(), this);
		}
	}

	/**
	 * Returns the resource with the given name and kind, or null.
	 */
	private COSBase get(COSName kind, COSName name)
	{
		COSDictionary dict = (COSDictionary)resources.getDictionaryObject(kind);
		if (dict == null)
		{
			return null;
		}
		return dict.getDictionaryObject(name);
	}

	/**
	 * Returns the names of the color space resources, if any.
	 */
	public Iterable<COSName> getColorSpaceNames()
	{
		return getNames(COSName.COLORSPACE);
	}

	/**
	 * Returns the names of the XObject resources, if any.
	 */
	public Iterable<COSName> getXObjectNames()
	{
		return getNames(COSName.XOBJECT);
	}

	/**
	 * Returns the names of the font resources, if any.
	 */
	public Iterable<COSName> getFontNames()
	{
		return getNames(COSName.FONT);
	}

	/**
	 * Returns the names of the property list resources, if any.
	 */
	public Iterable<COSName> getPropertiesNames()
	{
		return getNames(COSName.PROPERTIES);
	}

	/**
	 * Returns the names of the shading resources, if any.
	 */
	public Iterable<COSName> getShadingNames()
	{
		return getNames(COSName.SHADING);
	}

	/**
	 * Returns the names of the pattern resources, if any.
	 */
	public Iterable<COSName> getPatternNames()
	{
		return getNames(COSName.PATTERN);
	}

	/**
	 * Returns the names of the external graphics state resources, if any.
	 */
	public Iterable<COSName> getExtGStateNames()
	{
		return getNames(COSName.EXT_G_STATE);
	}

	/**
	 * Returns the resource names of the given kind.
	 */
	private Iterable<COSName> getNames(COSName kind)
	{
		COSDictionary dict = (COSDictionary)resources.getDictionaryObject(kind);
		if (dict == null)
		{
			return Collections.emptySet();
		}
		return dict.keySet();
	}

	/**
	 * Adds the given font to the resources of the current page and returns the name for the
	 * new resources. Returns the existing resource name if the given item already exists.
	 *
	 * @param font the font to add
	 * @return the name of the resource in the resources dictionary
	 */
	public COSName add(PDFont font) throws IOException
	{
		return add(COSName.FONT, "F", font);
	}

	/**
	 * Adds the given color space to the resources of the current page and returns the name for the
	 * new resources. Returns the existing resource name if the given item already exists.
	 *
	 * @param colorSpace the color space to add
	 * @return the name of the resource in the resources dictionary
	 */
	public COSName add(PDColorSpace colorSpace)
	{
		return add(COSName.COLORSPACE, "cs", colorSpace);
	}

	/**
	 * Adds the given external graphics state to the resources of the current page and returns the
	 * name for the new resources. Returns the existing resource name if the given item already exists.
	 *
	 * @param extGState the external graphics stae to add
	 * @return the name of the resource in the resources dictionary
	 */
	public COSName add(PDExtendedGraphicsState extGState)
	{
		return add(COSName.EXT_G_STATE, "gs", extGState);
	}

	/**
	 * Adds the given shading to the resources of the current page and returns the name for the
	 * new resources. Returns the existing resource name if the given item already exists.
	 *
	 * @param shading the shading to add
	 * @return the name of the resource in the resources dictionary
	 */
	public COSName add(PDShading shading)
	{
		return add(COSName.SHADING, "sh", shading);
	}

	/**
	 * Adds the given pattern to the resources of the current page and returns the name for the
	 * new resources. Returns the existing resource name if the given item already exists.
	 *
	 * @param pattern the pattern to add
	 * @return the name of the resource in the resources dictionary
	 */
	public COSName add(PDAbstractPattern pattern)
	{
		return add(COSName.PATTERN, "p", pattern);
	}

	/**
	 * Adds the given property list to the resources of the current page and returns the name for
	 * the new resources. Returns the existing resource name if the given item already exists.
	 *
	 * @param properties the property list to add
	 * @return the name of the resource in the resources dictionary
	 */
	public COSName add(PDPropertyList properties)
	{
		if (properties instanceof PDOptionalContentGroup)
		{
			return add(COSName.PROPERTIES, "oc", properties);
		}
		else
		{
			return add(COSName.PROPERTIES, "Prop", properties);
		}
	}

	/**
	 * Adds the given image to the resources of the current page and returns the name for the
	 * new resources. Returns the existing resource name if the given item already exists.
	 *
	 * @param image the image to add
	 * @return the name of the resource in the resources dictionary
	 */
	public COSName add(PDImageXObject image)
	{
		return add(COSName.XOBJECT, "Im", image);
	}

	/**
	 * Adds the given form to the resources of the current page and returns the name for the
	 * new resources. Returns the existing resource name if the given item already exists.
	 *
	 * @param form the form to add
	 * @return the name of the resource in the resources dictionary
	 */
	public COSName add(PDFormXObject form)
	{
		return add(COSName.XOBJECT, "Form", form);
	}

	/**
	 * Adds the given XObject to the resources of the current page and returns the name for the
	 * new resources. Returns the existing resource name if the given item already exists.
	 *
	 * @param xobject the XObject to add
	 * @param prefix the prefix to be used when creating the resource name
	 * @return the name of the resource in the resources dictionary
	 */
	public COSName add(PDXObject xobject, String prefix)
	{
		return add(COSName.XOBJECT, prefix, xobject);
	}

	/**
	 * Adds the given resource if it does not already exist.
	 */
	private COSName add(COSName kind, String prefix, COSObjectable object)
	{
		// return the existing key if the item exists already
		COSDictionary dict = (COSDictionary)resources.getDictionaryObject(kind);
		if (dict != null && dict.containsValue(object.getCOSObject()))
		{
			return dict.getKeyForValue(object.getCOSObject());
		}

		// add the item with a new key
		COSName name = createKey(kind, prefix);
		put(kind, name, object);
		return name;
	}

	/**
	 * Returns a unique key for a new resource.
	 */
	private COSName createKey(COSName kind, String prefix)
	{
		COSDictionary dict = (COSDictionary)resources.getDictionaryObject(kind);
		if (dict == null)
		{
			return COSName.getPDFName(prefix + 1);
		}

		// find a unique key
		String key;
		int iter = dict.keySet().size() + 1;
		do
		{
			key = prefix + iter++;
		}
		while (dict.containsKey(key));
		return COSName.getPDFName(key);
	}

	/**
	 * Sets the value of a given named resource.
	 */
	private void put(COSName kind, COSName name, COSObjectable object)
	{
		COSDictionary dict = (COSDictionary)resources.getDictionaryObject(kind);
		if (dict == null)
		{
			dict = new COSDictionary();
			resources.setItem(kind, dict);
		}
		dict.setItem(name, object);
	}

	/**
	 * Sets the font resource with the given name.
	 *
	 * @param name the name of the resource
	 * @param font the font to be added
	 */
	public void put(COSName name, PDFont font) throws IOException
	{
		put(COSName.FONT, name, font);
	}

	/**
	 * Sets the color space resource with the given name.
	 *
	 * @param name the name of the resource
	 * @param colorSpace the color space to be added
	 */
	public void put(COSName name, PDColorSpace colorSpace) throws IOException
	{
		put(COSName.COLORSPACE, name, colorSpace);
	}

	/**
	 * Sets the external graphics state resource with the given name.
	 *
	 * @param name the name of the resource
	 * @param extGState the external graphics state to be added
	 */
	public void put(COSName name, PDExtendedGraphicsState extGState) throws IOException
	{
		put(COSName.EXT_G_STATE, name, extGState);
	}

	/**
	 * Sets the shading resource with the given name.
	 *
	 * @param name the name of the resource
	 * @param shading the shading to be added
	 */
	public void put(COSName name, PDShading shading) throws IOException
	{
		put(COSName.SHADING, name, shading);
	}

	/**
	 * Sets the pattern resource with the given name.
	 *
	 * @param name the name of the resource
	 * @param pattern the pattern to be added
	 */
	public void put(COSName name, PDAbstractPattern pattern) throws IOException
	{
		put(COSName.PATTERN, name, pattern);
	}

	/**
	 * Sets the property list resource with the given name.
	 *
	 * @param name the name of the resource
	 * @param properties the property list to be added
	 */
	public void put(COSName name, PDPropertyList properties) throws IOException
	{
		put(COSName.PROPERTIES, name, properties);
	}

	/**
	 * Sets the XObject resource with the given name.
	 *
	 * @param name the name of the resource
	 * @param xobject the XObject to be added
	 */
	public void put(COSName name, PDXObject xobject) throws IOException
	{
		put(COSName.XOBJECT, name, xobject);
	}
}
