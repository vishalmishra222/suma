package com.tom_roush.pdfbox.pdmodel.fdf;

import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.pdmodel.common.COSObjectable;

/**
 * This represents an FDF page info that is part of the FDF page.
 *
 * @author Ben Litchfield
 */
public class FDFPageInfo implements COSObjectable
{
    private COSDictionary pageInfo;

    /**
     * Default constructor.
     */
    public FDFPageInfo()
    {
        pageInfo = new COSDictionary();
    }

    /**
     * Constructor.
     *
     * @param p The FDF page.
     */
    public FDFPageInfo( COSDictionary p )
    {
        pageInfo = p;
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    public COSBase getCOSObject()
    {
        return pageInfo;
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    public COSDictionary getCOSDictionary()
    {
        return pageInfo;
    }
}
