package com.tom_roush.pdfbox.pdmodel.interactive.measurement;

import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.pdmodel.common.COSObjectable;

/**
 * This class represents a number format dictionary.
 */
public class PDNumberFormatDictionary implements COSObjectable
{

    /**
     * The type of the dictionary.
     */
    public static final String TYPE = "NumberFormat";

    /**
     * Constant indicating that the label specified by U is a suffix to the value.
     */
    public static final String LABEL_SUFFIX_TO_VALUE = "S";
    /**
     * Constant indicating that the label specified by U is a postfix to the value.
     */
    public static final String LABEL_PREFIX_TO_VALUE = "P";

    /**
     * Constant for showing a fractional value as decimal to the precision specified by the D entry.
     */
    public static final String FRACTIONAL_DISPLAY_DECIMAL = "D";
    /**
     * Constant for showing a fractional value as a fraction with denominator specified by the D entry.
     */
    public static final String FRACTIONAL_DISPLAY_FRACTION = "F";
    /**
     * Constant for showing a fractional value without fractional part; round to the nearest whole unit.
     */
    public static final String FRACTIONAL_DISPLAY_ROUND = "R";
    /**
     * Constant for showing a fractional value without fractional part; truncate to achieve whole units.
     */
    public static final String FRACTIONAL_DISPLAY_TRUNCATE = "T";

    private COSDictionary numberFormatDictionary;

    /**
     * Constructor.
     */
    public PDNumberFormatDictionary()
    {
        this.numberFormatDictionary = new COSDictionary();
        this.numberFormatDictionary.setName(COSName.TYPE, TYPE);
    }

    /**
     * Constructor.
     * 
     * @param dictionary the corresponding dictionary
     */
    public PDNumberFormatDictionary(COSDictionary dictionary)
    {
        this.numberFormatDictionary = dictionary;
    }

    /**
     * {@inheritDoc}
     */
    public COSBase getCOSObject()
    {
        return this.numberFormatDictionary;
    }

    /**
     * This will return the dictionary.
     * 
     * @return the number format dictionary
     */
    public COSDictionary getDictionary()
    {
        return this.numberFormatDictionary;
    }

    /**
     * This will return the type of the number format dictionary.
     * It must be "NumberFormat"
     * 
     * @return the type
     */
    public String getType()
    {
        return TYPE;
    }

    /**
     * This will return the label for the units.
     * 
     * @return the label for the units
     */
    public String getUnits()
    {
        return this.getDictionary().getString("U");
    }

    /**
     * This will set the label for the units.
     * 
     * @param units the label for the units
     */
    public void setUnits(String units)
    {
        this.getDictionary().setString("U", units);
    }

    /**
     * This will return the conversion factor.
     * 
     * @return the conversion factor
     */
    public float getConversionFactor()
    {
        return this.getDictionary().getFloat("C");
    }

    /**
     * This will set the conversion factor.
     * 
     * @param conversionFactor the conversion factor
     */
    public void setConversionFactor(float conversionFactor)
    {
        this.getDictionary().setFloat("C", conversionFactor);
    }

    /** 
     * This will return the value for the manner to display a fractional value.
     *  
     * @return the manner to display a fractional value
     */
    public String getFractionalDisplay()
    {
        return this.getDictionary().getString("F", FRACTIONAL_DISPLAY_DECIMAL);
    }

    /** 
     * This will set the value for the manner to display a fractional value.
     * Allowed values are "D", "F", "R" and "T"
     * @param fractionalDisplay the manner to display a fractional value
     */
    public void setFractionalDisplay(String fractionalDisplay)
    {
        if ((fractionalDisplay == null)
            || FRACTIONAL_DISPLAY_DECIMAL.equals(fractionalDisplay)
            || FRACTIONAL_DISPLAY_FRACTION.equals(fractionalDisplay)
            || FRACTIONAL_DISPLAY_ROUND.equals(fractionalDisplay)
            || FRACTIONAL_DISPLAY_TRUNCATE.equals(fractionalDisplay))
        {
            this.getDictionary().setString("F", fractionalDisplay);
        }
        else
        {
            throw new IllegalArgumentException("Value must be \"D\", \"F\", \"R\", or \"T\", (or null).");
        }
    }

    /**
     * This will return the precision or denominator of a fractional amount.
     * 
     * @return the precision or denominator
     */
    public int getDenominator()
    {
        return this.getDictionary().getInt("D");
    }

    /**
     * This will set the precision or denominator of a fractional amount.
     * 
     * @param denominator the precision or denominator
     */
    public void setDenominator(int denominator)
    {
        this.getDictionary().setInt("D", denominator);
    }

    /**
     * This will return the value indication if the denominator of the fractional value is reduced/truncated .
     * 
     * @return fd
     */
    public boolean isFD()
    {
        return this.getDictionary().getBoolean("FD", false);
    }

    /**
     * This will set the value indication if the denominator of the fractional value is reduced/truncated .
     * The denominator may not be reduced/truncated if true
     * @param fd fd
     */
    public void setFD(boolean fd)
    {
        this.getDictionary().setBoolean("FD", fd);
    }

    /**
     * This will return the text to be used between orders of thousands in display of numerical values.
     * 
     * @return thousands separator
     */
    public String getThousandsSeparator()
    {
        return this.getDictionary().getString("RT", ",");
    }

    /**
     * This will set the text to be used between orders of thousands in display of numerical values.
     * 
     * @param thousandsSeparator thousands separator
     */
    public void setThousandsSeparator(String thousandsSeparator)
    {
        this.getDictionary().setString("RT", thousandsSeparator);
    }

    /**
     * This will return the text to be used as the decimal point in displaying numerical values.
     * 
     * @return decimal separator
     */
    public String getDecimalSeparator()
    {
        return this.getDictionary().getString("RD", ".");
    }

    /**
     * This will set the text to be used as the decimal point in displaying numerical values.
     * 
     * @param decimalSeparator decimal separator
     */
    public void setDecimalSeparator(String decimalSeparator)
    {
        this.getDictionary().setString("RD", decimalSeparator);
    }

    /**
     * This will return the text to be concatenated to the left of the label specified by U.
     * @return label prefix
     */
    public String getLabelPrefixString()
    {
        return this.getDictionary().getString("PS", " ");
    }

    /**
     * This will set the text to be concatenated to the left of the label specified by U.
     * @param labelPrefixString label prefix
     */
    public void setLabelPrefixString(String labelPrefixString)
    {
        this.getDictionary().setString("PS", labelPrefixString);
    }

    /**
     * This will return the text to be concatenated after the label specified by U.
     * 
     * @return label suffix
     */
    public String getLabelSuffixString()
    {
        return this.getDictionary().getString("SS", " ");
    }

    /**
     * This will set the text to be concatenated after the label specified by U.
     * 
     * @param labelSuffixString label suffix
     */
    public void setLabelSuffixString(String labelSuffixString)
    {
        this.getDictionary().setString("SS", labelSuffixString);
    }

    /**
     * This will return a value indicating the ordering of the label specified by U to the calculated unit value.
     * 
     * @return label position 
     */
    public String getLabelPositionToValue()
    {
        return this.getDictionary().getString("O", LABEL_SUFFIX_TO_VALUE);
    }

    /**
     * This will set the value indicating the ordering of the label specified by U to the calculated unit value.
     * Possible values are "S" and "P"
     * 
     * @param labelPositionToValue label position 
     */
    public void setLabelPositionToValue(String labelPositionToValue)
    {
        if ((labelPositionToValue == null)
            || LABEL_PREFIX_TO_VALUE.equals(labelPositionToValue)
            || LABEL_SUFFIX_TO_VALUE.equals(labelPositionToValue))
        {
            this.getDictionary().setString("O", labelPositionToValue);
        }
        else
        {
            throw new IllegalArgumentException("Value must be \"S\", or \"P\" (or null).");
        }
    }

}
