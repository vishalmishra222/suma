package com.tom_roush.pdfbox.pdmodel.interactive.form;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tom_roush.pdfbox.contentstream.operator.Operator;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.cos.COSNumber;
import com.tom_roush.pdfbox.cos.COSStream;
import com.tom_roush.pdfbox.pdfparser.PDFStreamParser;
import com.tom_roush.pdfbox.pdfwriter.ContentStreamWriter;
import com.tom_roush.pdfbox.pdmodel.PDResources;
import com.tom_roush.pdfbox.pdmodel.common.COSObjectable;
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.font.PDFontDescriptor;
import com.tom_roush.pdfbox.pdmodel.interactive.action.PDFormFieldAdditionalActions;
import com.tom_roush.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import com.tom_roush.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import com.tom_roush.pdfbox.pdmodel.interactive.annotation.PDAppearanceEntry;
import com.tom_roush.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;

import android.util.Log;

/**
 * Create the AcroForms field appearance helper.
 * <p>
 * A helper class to the {@link AppearanceGenerator} to generate update an AcroForm field appearance.'
 * </p>
 *
 * @author Stephan Gerhard
 * @author Ben Litchfield
 */
class AppearanceGeneratorHelper
{
	// scale of font glyph units
	private static final float GLYPH_TO_PDF_SCALE = 1000f;
	
	private final PDVariableText parent;

	private String value;
	private final DefaultAppearanceHandler defaultAppearanceHandler;

	private final PDAcroForm acroForm;
	private List<COSObjectable> widgets = new ArrayList<COSObjectable>();


	/**
	 * Constructs a COSAppearance from the given field.
	 *
	 * @param theAcroForm the AcroForm that this field is part of.
	 * @param field the field which you wish to control the appearance of
	 * @throws IOException If there is an error creating the appearance.
	 */
	public AppearanceGeneratorHelper(PDAcroForm theAcroForm, PDVariableText field) throws IOException
	{
		acroForm = theAcroForm;
		parent = field;

		widgets = field.getKids();
		if(widgets == null)
		{
			widgets = new ArrayList<COSObjectable>();
			widgets.add(field.getWidget());
		}
		defaultAppearanceHandler = new DefaultAppearanceHandler(getDefaultAppearance());
	}

	/**
	 * Returns the default apperance of a textbox. If the textbox does not have one,
	 * then it will be taken from the AcroForm.
	 * @return The DA element
	 */
	private String getDefaultAppearance()
	{
		return parent.getDefaultAppearance();
	}

	/**
	 * Extracts the original appearance stream into a list of tokens.
	 *
	 * @return The tokens in the original appearance stream
	 */
	private List<Object> getStreamTokens(PDAppearanceStream appearanceStream) throws IOException
	{
		List<Object> tokens = new ArrayList<Object>();
		if (appearanceStream != null)
		{
			tokens = getStreamTokens(appearanceStream.getCOSStream());
		}
		return tokens;
	}

	private List<Object> getStreamTokens(COSStream stream) throws IOException
	{
		List<Object> tokens = new ArrayList<Object>();
		if(stream != null)
		{
			PDFStreamParser parser = new PDFStreamParser(stream);
			parser.parse();
			tokens = parser.getTokens();
			parser.close();
		}
		return tokens;
	}

	/**
	 * Tests if the appearance stream already contains content.
	 * 
	 * @param streamTokens individual tokens within the appearance stream
	 *
	 * @return true if it contains any content
	 */
	private boolean containsMarkedContent(List<Object> streamTokens)
	{
		return streamTokens.contains(Operator.getOperator("BMC"));
	}

	/**
     * This is the public method for setting the appearance stream.
     *
     * @param apValue the String value which the appearance should represent
     *
     * @throws IOException If there is an error creating the stream.
     */
    public void setAppearanceValue(String apValue) throws IOException
    {
        value = apValue;
        Iterator<COSObjectable> widgetIter = widgets.iterator();
        
        while (widgetIter.hasNext())
        {
            COSObjectable next = widgetIter.next();
            PDField field = null;
            PDAnnotationWidget widget;
            if (next instanceof PDField)
            {
                field = (PDField) next;
                widget = field.getWidget();
            }
            else
            {
                widget = (PDAnnotationWidget) next;
            }
            PDFormFieldAdditionalActions actions = null;
            if (field != null)
            {
                actions = field.getActions();
            }
            // in case all tests fail the field will be formatted by acrobat
            // when it is opened. See FreedomExpressions.pdf for an example of this.
            if (actions == null || actions.getF() == null ||
            		widget.getDictionary().getDictionaryObject(COSName.AP) != null)
            {
                PDAppearanceDictionary appearance = widget.getAppearance();
                if (appearance == null)
                {
                    appearance = new PDAppearanceDictionary();
                    widget.setAppearance(appearance);
                }

                PDAppearanceEntry normalAppearance = appearance.getNormalAppearance();
                // TODO support more than one appearance stream
                PDAppearanceStream appearanceStream = 
                        normalAppearance.isStream() ? normalAppearance.getAppearanceStream() : null;
                if (appearanceStream == null)
                {
                    COSStream cosStream = acroForm.getDocument().getDocument().createCOSStream();
                    appearanceStream = new PDAppearanceStream(cosStream);
                    appearanceStream.setBBox(widget.getRectangle()
                            .createRetranslatedRectangle());
                    appearance.setNormalAppearance(appearanceStream);
                }

                List<Object> tokens = getStreamTokens(appearanceStream);
                PDFont pdFont = getFontAndUpdateResources(appearanceStream);

                if (!containsMarkedContent(tokens))
                {
                	createAppearanceContent(tokens, widget, pdFont, appearanceStream);
                }
                else
                {
                	updateAppearanceContent(tokens, widget, pdFont, appearanceStream);
                }
            }
        }
    }
    
    /**
     * Create new content.
     */
    private void createAppearanceContent(List<Object> tokens, PDAnnotationWidget widget,
    		PDFont pdFont, PDAppearanceStream appearanceStream)
    				throws IOException
    {
    	ByteArrayOutputStream output = new ByteArrayOutputStream();
    	// BJL 9/25/2004 Must prepend existing stream
    	// because it might have operators to draw things like
    	// rectangles and such
    	ContentStreamWriter writer = new ContentStreamWriter(output);
    	writer.writeTokens(tokens);
    	output.write("/Tx BMC\n".getBytes("ISO-8859-1"));
    	PDRectangle boundingBox = resolveBoundingBox(widget, appearanceStream);
    	insertGeneratedAppearance(boundingBox, output, pdFont, tokens);
    	output.write("EMC".getBytes("ISO-8859-1"));
    	output.close();
    	writeToStream(output.toByteArray(), appearanceStream);
    }

    /**
     * Update existing content.
     */
    private void updateAppearanceContent(List<Object> tokens, PDAnnotationWidget widget,
    		PDFont pdFont, PDAppearanceStream appearanceStream)
    				throws IOException
    {
    	ByteArrayOutputStream output = new ByteArrayOutputStream();
    	ContentStreamWriter writer = new ContentStreamWriter(output);
    	PDRectangle boundingBox = resolveBoundingBox(widget, appearanceStream);

    	int bmcIndex = tokens.indexOf(Operator.getOperator("BMC"));
    	int emcIndex = tokens.indexOf(Operator.getOperator("EMC"));
    	
    	writer.writeTokens(tokens, 0, bmcIndex + 1);
    	
    	output.write("\n".getBytes("ISO-8859-1"));
    	
    	insertGeneratedAppearance(boundingBox, output, pdFont, tokens);
    	
    	if (emcIndex != -1)
    	{
    		writer.writeTokens(tokens, emcIndex, tokens.size());
    	}
    	output.close();
    	writeToStream(output.toByteArray(), appearanceStream);
    }
    
     /*
     * Generate and insert text content and clipping around it.
     */
     private void insertGeneratedAppearance(PDRectangle boundingBox, OutputStream output,
     PDFont font, List<Object> tokens)
     throws IOException
	{
		AppearancePrimitivesComposer composer = new AppearancePrimitivesComposer(output);
		float fontSize = 0.0f;
		
		// Acrobat calculates the left and right padding dependent on the offset of the border edge
		// This calculation works for forms having been generated by Acrobat.
		// The minimum distance is always 1f even if there is no rectangle being drawn around.
		float lineWidth = getLineWidth(tokens);
		PDRectangle paddingEdge = applyPadding(boundingBox,Math.max(1f, lineWidth));
		PDRectangle contentEdge = applyPadding(paddingEdge,Math.max(1f, lineWidth));

		// add a clipping path to avoid overlapping with the border
		composer.addRect(paddingEdge);
		composer.clip();

		// start the text output
		composer.beginText();
		
		// calculate the fontSize
		fontSize = calculateFontSize(font, contentEdge);
		
		if (!defaultAppearanceHandler.getTokens().isEmpty())
		{
			defaultAppearanceHandler.setFontSize(fontSize);
			ContentStreamWriter daWriter = new ContentStreamWriter(output);
			daWriter.writeTokens(defaultAppearanceHandler.getTokens());
		}
		
		// calculation of the vertical offset from where the text will be printed
		float verticalOffset = calculateVerticalOffset(paddingEdge, contentEdge, font, fontSize);

		// calculation of the horizontal offset from where the text will be printed
		float leftOffset = 0f;

		// show the text
		if (!isMultiLine())
		{
			// calculation of the horizontal offset from where the text will be printed
			leftOffset = calculateHorizontalOffset(contentEdge, font, fontSize);
			composer.newLineAtOffset(leftOffset, verticalOffset);
			composer.showText(value, font);
		}
		else
		{
			leftOffset = contentEdge.getLowerLeftX();
			PlainText textContent = new PlainText(value);
			AppearanceStyle appearanceStyle = new AppearanceStyle();
			appearanceStyle.setFont(font);
			appearanceStyle.setFontSize(fontSize);
			
			//Adobe Acrobat uses the font's bounding box for the leading between the lines
			appearanceStyle.setLeading(font.getBoundingBox().getHeight() / GLYPH_TO_PDF_SCALE * fontSize);
			
			PlainTextFormatter formatter = new PlainTextFormatter
					.Builder(composer)
			.style(appearanceStyle)
			.text(textContent)
			.width(contentEdge.getWidth())
			.wrapLines(true)
			.initialOffset(leftOffset, verticalOffset)
			.textAlign(parent.getQ())
			.build();
			formatter.format();
		}        
		composer.endText();
	}

	/*
     * To update an existing appearance stream first copy any needed resources from the
     * document's DR dictionary into the stream's Resources dictionary.
     * If the DR and Resources dictionaries contain resources with the same name,
     * the one already in the Resources dictionary shall be left intact,
     * not replaced with the corresponding value from the DR dictionary. 
     */
    private PDFont getFontAndUpdateResources(PDAppearanceStream appearanceStream) throws IOException
    {
        PDFont font = null;
        PDResources streamResources = appearanceStream.getResources();
        PDResources formResources = acroForm.getDefaultResources();
        
        if (streamResources == null && formResources == null)
        {
            throw new IOException("Unable to generate field appearance - missing required resources");
        }
        
        COSName cosFontName = defaultAppearanceHandler.getFontName();
        
        if (streamResources != null)
        {
            font = streamResources.getFont(cosFontName);
            if (font != null)
            {
                return font;
            }
        }
        else
        {
            streamResources = new PDResources();
            appearanceStream.setResources(streamResources);
        }
        
        if (formResources != null)
        {
            font = formResources.getFont(cosFontName);
            if (font != null)
            {
                streamResources.put(cosFontName, font);
                return font;
            }
        }        
        
        // if we get here the font might be there but under a different name
        // which is incorrect but try to treat the resource name as the font name
        font = resolveFont(streamResources, formResources, cosFontName);
            
        if (font != null)
        {
            streamResources.put(cosFontName, font);
            return font;
        }
        else
        {
            throw new IOException("Unable to generate field appearance - missing required font resources: "
            		+ cosFontName);
        }
    }
    
    /**
     * Get the font from the resources.
     * @return the retrieved font
     * @throws IOException 
     */
    private PDFont resolveFont(PDResources streamResources, PDResources formResources, COSName cosFontName)
            throws IOException
    {
        // if the font couldn't be retrieved it might be because the font name
        // in the DA string didn't point to the font resources dictionary entry but
        // is the name of the font itself. So try to resolve that.
        
        PDFont font = null;
        if (streamResources != null)
        {
            for (COSName fontName : streamResources.getFontNames()) 
            {
                font = streamResources.getFont(fontName);
                if (font.getName().equals(cosFontName.getName()))
                {
                    return font;
                }
            }
        }

        if (formResources != null)
        {
            for (COSName fontName : formResources.getFontNames()) 
            {
                font = formResources.getFont(fontName);
                if (font.getName().equals(cosFontName.getName()))
                {
                    return font;
                }
            }
        }
        return null;
    }

	private boolean isMultiLine()
	{
		return parent instanceof PDTextField && ((PDTextField) parent).isMultiline();
	}

	/**
	 * Writes the stream to the actual stream in the COSStream.
	 *
	 * @throws IOException If there is an error writing to the stream
	 */
	private void writeToStream(byte[] data, PDAppearanceStream appearanceStream) throws IOException
	{
		OutputStream out = appearanceStream.getCOSStream().createUnfilteredStream();
		out.write(data);
		out.flush();
	}


	/**
	 * w in an appearance stream represents the lineWidth.
	 * 
	 * @return the linewidth
	 */
	private float getLineWidth( List<Object> tokens )
	{
		float retval = 0f;
		if(tokens != null)
		{
			int btIndex = tokens.indexOf(Operator.getOperator("BT"));
			int wIndex = tokens.indexOf(Operator.getOperator("w"));
			//the w should only be used if it is before the first BT.
			if((wIndex > 0) && (wIndex < btIndex || btIndex == -1))
			{
				retval = ((COSNumber)tokens.get(wIndex - 1)).floatValue();
			}
		}
		return retval;
	}

	/**
	 * My "not so great" method for calculating the fontsize. It does not work superb, but it
	 * handles ok.
	 * @return the calculated font-size
	 *
	 * @throws IOException If there is an error getting the font information.
	 */
	private float calculateFontSize(PDFont pdFont, PDRectangle contentEdge) throws IOException
	{
		// default font size is 12 in Acrobat
		float fontSize = 12f;
		
		if (!defaultAppearanceHandler.getTokens().isEmpty())
		{
			fontSize = defaultAppearanceHandler.getFontSize();
		}

		// if the font size is 0 the size depends on the content
		if (fontSize == 0 && !isMultiLine())
		{
			float widthAtFontSize1 = pdFont.getStringWidth(value) / GLYPH_TO_PDF_SCALE;
			float widthBasedFontSize = contentEdge.getWidth() / widthAtFontSize1;
			float height = pdFont.getFontDescriptor().getFontBoundingBox().getHeight() / GLYPH_TO_PDF_SCALE;
			fontSize = Math.min(contentEdge.getHeight() / height, widthBasedFontSize);
		}
		
		// restore to default size for multiline text
		if(fontSize == 0)
		{
			fontSize = 12f;
		}
		
		return fontSize;
	}


	/**
	 * Calculate the vertical start position for the text.
	 * 
	 * @param paddingEdge the content edge
	 * @param contentEdge the content edge
	 * @param pdFont the font to use for formatting
	 * @param fontSite the font size to use for formating
	 *
	 * @return the vertical start position of the text
	 *
	 * @throws IOException If there is an error calculating the text position.
	 */
	private float calculateVerticalOffset(PDRectangle paddingEdge,
			PDRectangle contentEdge, PDFont pdFont, float fontSize) throws IOException
	{
		float verticalOffset = 0.0f;
		float capHeight = getCapHeight(pdFont, fontSize);
		float fontHeight = pdFont.getBoundingBox().getHeight() / GLYPH_TO_PDF_SCALE * fontSize;
		
		if(parent instanceof PDTextField && ((PDTextField) parent).isMultiline())
		{
			verticalOffset = contentEdge.getUpperRightY() + fontHeight;
		}
		else
		{
			// Acrobat shifts the value so it aligns to the bottom if
			// the font's caps are larger than the height of the paddingEdge
			if (capHeight > paddingEdge.getHeight())
			{
				verticalOffset = paddingEdge.getLowerLeftX()
						- pdFont.getFontDescriptor().getDescent() / GLYPH_TO_PDF_SCALE * fontSize;
			}
			else
			{
				verticalOffset = (paddingEdge.getHeight() - capHeight) / 2f + paddingEdge.getLowerLeftX();
			}
		}
		return verticalOffset;
	}
	
	/**
	 * Calculate the horizontal start position for the text.
	 *
	 * @param contentEdge the content edge
	 * @param pdFont the font to use for formatting
	 * @param fontSite the font size to use for formating
	 *
	 * @return the horizontal start position of the text
	 *
	 * @throws IOException If there is an error calculating the text position.
	 */
	private float calculateHorizontalOffset(PDRectangle contentEdge, PDFont pdFont, float fontSize) throws IOException
	{
		// Acrobat aligns left regardless of the quadding if the text is wider than the remaining width
		float stringWidth = (pdFont.getStringWidth(value) / GLYPH_TO_PDF_SCALE) * fontSize;
		float leftOffset = 0f;

		int q = parent.getQ();

		if (q == PDTextField.QUADDING_LEFT
				|| stringWidth > contentEdge.getWidth())
		{
			leftOffset = contentEdge.getLowerLeftX();
		}
		else if (q == PDTextField.QUADDING_CENTERED)
		{
			leftOffset = contentEdge.getLowerLeftX() + (contentEdge.getWidth() - stringWidth) / 2;
		}
		else if (q == PDTextField.QUADDING_RIGHT)
		{
			leftOffset = contentEdge.getLowerLeftX() + contentEdge.getWidth() - stringWidth;
		}
		else
		{
			// Unknown quadding value - default to left
			leftOffset = contentEdge.getLowerLeftX();
			Log.d("PdfBoxAndroid", "Unknown justification value, defaulting to left: " + q);
		}
	
	 return leftOffset;
	 }
	
	/**
	 * Get the capHeight for a font.
	 * @throws IOException in case the font information can not be retrieved.
	 */
	private float getCapHeight(PDFont pdFont, float fontSize) throws IOException
	{
		final PDFontDescriptor fontDescriptor = pdFont.getFontDescriptor();

		// as the font descriptor might be null or the cap height might be 0
		// alternate calculation for the cap height
		if (fontDescriptor == null || fontDescriptor.getCapHeight() == 0)
		{
			// TODO: refine the calculation if needed
			return pdFont.getBoundingBox().getHeight() / GLYPH_TO_PDF_SCALE * fontSize * 0.7f;
		}
		else
		{
			return pdFont.getFontDescriptor().getCapHeight() / GLYPH_TO_PDF_SCALE * fontSize;
		}
	}
	
	/**
	 * Resolve the bounding box.
	 *
	 * @param fieldWidget the annotation widget.
	 * @param appearanceStream the annotations appearance stream.
	 * @return the resolved boundingBox.
	 */
	private PDRectangle resolveBoundingBox(PDAnnotationWidget fieldWidget, PDAppearanceStream appearanceStream)
	{
		PDRectangle boundingBox = appearanceStream.getBBox();
		if (boundingBox == null)
		{
			boundingBox = fieldWidget.getRectangle().createRetranslatedRectangle();
		}
		return boundingBox;
	}
	
	/**
	 * Apply padding to a box.
	 *
	 * @param original box
	 * @return the padded box.
	 */
	private PDRectangle applyPadding(PDRectangle box, float padding)
	{
		return new PDRectangle(
				box.getLowerLeftX() +padding,
				box.getLowerLeftY() +padding,
				box.getWidth()-2*padding, box.getHeight()-2*padding
				);
	}
}
