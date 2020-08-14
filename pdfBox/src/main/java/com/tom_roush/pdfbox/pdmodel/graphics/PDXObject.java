package com.tom_roush.pdfbox.pdmodel.graphics;

import java.io.IOException;

import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.cos.COSStream;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDResources;
import com.tom_roush.pdfbox.pdmodel.common.COSObjectable;
import com.tom_roush.pdfbox.pdmodel.common.PDStream;
import com.tom_roush.pdfbox.pdmodel.graphics.form.PDFormXObject;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 * An external object, or "XObject".
 *
 * @author Ben Litchfield
 * @author John Hewson
 */
public class PDXObject implements COSObjectable
{
    private PDStream stream;

    /**
     * Creates a new XObject instance of the appropriate type for the COS stream.
     * @param base The stream which is wrapped by this XObject.
     * @return A new XObject instance.
     */
    public static PDXObject createXObject(COSBase base, String name, PDResources resources)
            throws IOException
    {
        if (base == null)
        {
            // TODO throw an exception?
            return null;
        }

        if (!(base instanceof COSStream))
        {
            throw new IOException("Unexpected object type: " + base.getClass().getName());
        }

        COSStream stream = (COSStream)base;
        String subtype = stream.getNameAsString(COSName.SUBTYPE);

        if (COSName.IMAGE.getName().equals(subtype))
        {
            return new PDImageXObject(new PDStream(stream), resources);
        }
        if (COSName.FORM.getName().equals(subtype))
        {
            return new PDFormXObject(new PDStream(stream), name);
        }
        else if (COSName.PS.getName().equals(subtype))
        {
            return new PDPostScriptXObject(new PDStream(stream));
        }
        else
        {
            throw new IOException("Invalid XObject Subtype: " + subtype);
        }
    }

    /**
     * Creates a new XObject from the given stream and subtype.
     * @param stream The stream to read.
     */
    protected PDXObject(PDStream stream, COSName subtype)
    {
        this.stream = stream;
        // could be used for writing:
        stream.getStream().setName(COSName.TYPE, COSName.XOBJECT.getName());
        stream.getStream().setName(COSName.SUBTYPE, subtype.getName());
    }

    /**
     * Creates a new XObject of the given subtype for writing.
     * @param document The document in which to create the XObject.
     * @param subtype The subtype of the new XObject.
     */
    protected PDXObject(PDDocument document, COSName subtype)
    {
        stream = new PDStream(document);
        stream.getStream().setName(COSName.TYPE, COSName.XOBJECT.getName());
        stream.getStream().setName(COSName.SUBTYPE, subtype.getName());
    }

    /**
     * Returns the stream.
     * {@inheritDoc}
     */
    public final COSBase getCOSObject()
    {
        return stream.getCOSObject();
    }

    /**
     * Returns the stream.
     * @return The stream for this object.
     */
    public final COSStream getCOSStream()
    {
        return stream.getStream();
    }

    /**
     * Returns the stream.
     * @return The stream for this object.
     */
    public final PDStream getPDStream()
    {
        return stream;
    }
}
