package com.tom_roush.pdfbox.contentstream.operator.text;

import java.util.List;

import com.tom_roush.pdfbox.contentstream.operator.Operator;
import com.tom_roush.pdfbox.contentstream.operator.OperatorProcessor;
import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSNumber;

import java.io.IOException;

/**
 * Tz: Set horizontal text scaling.
 *
 * @author Ben Litchfield
 */
public class SetTextHorizontalScaling extends OperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> arguments) throws IOException
    {
        COSNumber scaling = (COSNumber)arguments.get(0);
        context.getGraphicsState().getTextState().setHorizontalScaling(scaling.floatValue());
    }

    @Override
    public String getName()
    {
        return "Tz";
    }
}
