package com.tom_roush.pdfbox.contentstream.operator.graphics;

import java.io.IOException;
import java.util.List;

import com.tom_roush.pdfbox.contentstream.operator.Operator;
import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSNumber;

import android.graphics.PointF;

/**
 * l Append straight line segment to path.
 *
 * @author Ben Litchfield
 */
public class LineTo extends GraphicsOperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> operands) throws IOException
    {
        // append straight line segment from the current point to the point
        COSNumber x = (COSNumber)operands.get(0);
        COSNumber y = (COSNumber)operands.get(1);

        PointF pos = context.transformedPoint(x.floatValue(), y.floatValue());
        context.lineTo(pos.x, pos.y);
    }

    @Override
    public String getName()
    {
        return "l";
    }
}
