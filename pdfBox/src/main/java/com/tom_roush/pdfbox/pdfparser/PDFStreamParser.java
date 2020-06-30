package com.tom_roush.pdfbox.pdfparser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.tom_roush.pdfbox.contentstream.operator.Operator;
import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSBoolean;
import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.cos.COSNull;
import com.tom_roush.pdfbox.cos.COSNumber;
import com.tom_roush.pdfbox.cos.COSObject;
import com.tom_roush.pdfbox.cos.COSStream;
import com.tom_roush.pdfbox.pdmodel.common.PDStream;

import android.util.Log;

/**
 * This will parse a PDF byte stream and extract operands and such.
 *
 * @author Ben Litchfield
 */
public class PDFStreamParser extends BaseParser
{
	private final List<Object> streamObjects = new ArrayList<Object>( 100 );

	private static final int    MAX_BIN_CHAR_TEST_LENGTH = 10;
	private final byte[] binCharTestArr = new byte[MAX_BIN_CHAR_TEST_LENGTH];

	/**
	 * Constructor that takes a stream to parse.
	 *
	 * @param stream The stream to read data from.
	 * @throws IOException If there is an error reading from the stream.
	 */
	public PDFStreamParser(InputStream stream) throws IOException 
	{
		super(stream);
	}

	/**
	 * Constructor.
	 *
	 * @param stream The stream to parse.
	 *
	 * @throws IOException If there is an error initializing the stream.
	 */
	public PDFStreamParser( PDStream stream ) throws IOException
	{
		this( stream.createInputStream() );
	}

	/**
	 * Constructor.
	 *
	 * @param stream The stream to parse.
	 *
	 * @throws IOException If there is an error initializing the stream.
	 */
	public PDFStreamParser( COSStream stream ) throws IOException
	{
		this( stream.getUnfilteredStream() );
	}

	/**
	 * This will parse the tokens in the stream.  This will close the
	 * stream when it is finished parsing.
	 *
	 * @throws IOException If there is an error while parsing the stream.
	 */
	public void parse() throws IOException
	{
		try
		{
			Object token;
			while( (token = parseNextToken()) != null )
			{
				streamObjects.add( token );
			}
		}
		finally
		{
			pdfSource.close();
		}
	}

	/**
	 * This will get the tokens that were parsed from the stream.
	 *
	 * @return All of the tokens in the stream.
	 */
	public List<Object> getTokens()
	{
		return streamObjects;
	}

	/**
	 * This will get an iterator which can be used to parse the stream
	 * one token after the other.
	 *
	 * @return an iterator to get one token after the other
	 */
	public Iterator<Object> getTokenIterator()
	{
		return new Iterator<Object>()
				{
			private Object token;

			private void tryNext()
			{
				try
				{
					if (token == null)
					{
						token = parseNextToken();
					}
				}
				catch (IOException e)
				{
					throw new RuntimeException(e);
				}
			}

			/** {@inheritDoc} */
			@Override
			public boolean hasNext()
			{
				tryNext();
				return token != null;
			}

			/** {@inheritDoc} */
			@Override
			public Object next() 
			{
				tryNext();
				Object tmp = token;
				if (tmp == null)
				{
					throw new NoSuchElementException();
				}
				token = null;
				return tmp;
			}

			/** {@inheritDoc} */
			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
				};
	}

	/**
	 * This will parse the next token in the stream.
	 *
	 * @return The next token in the stream or null if there are no more tokens in the stream.
	 *
	 * @throws IOException If an io error occurs while parsing the stream.
	 */
	private Object parseNextToken() throws IOException
	{
		Object retval;

		skipSpaces();
		int nextByte = pdfSource.peek();
		if( ((byte)nextByte) == -1 )
		{
			return null;
		}
		char c = (char)nextByte;
		switch(c)
		{
		case '<':
		{
			//pull off first left bracket
			int leftBracket = pdfSource.read();
			
			//check for second left bracket
			c = (char)pdfSource.peek();
			
			//put back first bracket
			pdfSource.unread(leftBracket);
			
			if(c == '<')
			{
				COSDictionary pod = parseCOSDictionary();
				skipSpaces();
				if((char)pdfSource.peek() == 's')
				{
					retval = parseCOSStream( pod );
				}
				else
				{
					retval = pod;
				}
			}
			else
			{
				retval = parseCOSString();
			}
			break;
		}
		case '[':
		{
			// array
			retval = parseCOSArray();
			break;
		}
		case '(':
			// string
			retval = parseCOSString();
			break;
		case '/':
			// name
			retval = parseCOSName();
			break;
		case 'n':
		{
			// null
			String nullString = readString();
			if( nullString.equals( "null") )
			{
				retval = COSNull.NULL;
			}
			else
			{
				retval = Operator.getOperator(nullString);
			}
			break;
		}
		case 't':
		case 'f':
		{
			String next = readString();
			if( next.equals( "true" ) )
			{
				retval = COSBoolean.TRUE;
				break;
			}
			else if( next.equals( "false" ) )
			{
				retval = COSBoolean.FALSE;
			}
			else
			{
				retval = Operator.getOperator(next);
			}
			break;
		}
		case 'R':
		{
			String line = readString();
			if( line.equals( "R" ) )
			{
				retval = new COSObject( null );
			}
			else
			{
				retval = Operator.getOperator(line);
			}
			break;
		}
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
		case '-':
		case '+':
		case '.':
		{
			/* We will be filling buf with the rest of the number.  Only
			 * allow 1 "." and "-" and "+" at start of number. */
			StringBuffer buf = new StringBuffer();
			buf.append( c );
			pdfSource.read();

			boolean dotNotRead = c != '.';
			while( Character.isDigit(c = (char)pdfSource.peek()) || dotNotRead && c == '.')
			{
				buf.append( c );
				pdfSource.read();

				if (dotNotRead && c == '.')
				{
					dotNotRead = false;
				}
			}
			retval = COSNumber.get( buf.toString() );
			break;
		}
		case 'B':
		{
			String next = readString();
			retval = Operator.getOperator(next);
			if( next.equals( "BI" ) )
			{
				Operator beginImageOP = (Operator)retval;
				COSDictionary imageParams = new COSDictionary();
				beginImageOP.setImageParameters( imageParams );
				Object nextToken = null;
				while( (nextToken = parseNextToken()) instanceof COSName )
				{
					Object value = parseNextToken();
					imageParams.setItem( (COSName)nextToken, (COSBase)value );
				}
				//final token will be the image data, maybe??
				Operator imageData = (Operator)nextToken;
				beginImageOP.setImageData( imageData.getImageData() );
			}
			break;
		}
		case 'I':
		{
			//Special case for ID operator
			String id = "" + (char)pdfSource.read() + (char)pdfSource.read();
			if( !id.equals( "ID" ) )
			{
				throw new IOException( "Error: Expected operator 'ID' actual='" + id + "'" );
			}
			ByteArrayOutputStream imageData = new ByteArrayOutputStream();
			if( isWhitespace() )
			{
				//pull off the whitespace character
				pdfSource.read();
			}
			int lastByte = pdfSource.read();
			int currentByte = pdfSource.read();
			// PDF spec is kinda unclear about this. Should a whitespace
			// always appear before EI? Not sure, so that we just read
			// until EI<whitespace>.
			// Be aware not all kind of whitespaces are allowed here. see PDFBOX-1561
			while( !(lastByte == 'E' &&
					currentByte == 'I' &&
					hasNextSpaceOrReturn() &&
					hasNoFollowingBinData( pdfSource )) &&
					!pdfSource.isEOF() )
			{
				imageData.write( lastByte );
				lastByte = currentByte;
				currentByte = pdfSource.read();
			}
			// the EI operator isn't unread, as it won't be processed anyway
			retval = Operator.getOperator("ID");
			// save the image data to the operator, so that it can be accessed later
			((Operator)retval).setImageData( imageData.toByteArray() );
			break;
		}
		case ']':
		{
			// some ']' around without its previous '['
			// this means a PDF is somewhat corrupt but we will continue to parse.
			pdfSource.read();
			
			// must be a better solution than null...
			retval = COSNull.NULL;
			break;
		}
		default:
		{
			//we must be an operator
			String operator = readOperator();
			if( operator.trim().length() == 0 )
			{
				//we have a corrupt stream, stop reading here
				retval = null;
			}
			else
			{
				retval = Operator.getOperator(operator);
			}
		}
		}
		return retval;
	}

	/**
	 * Looks up an amount of bytes if they contain only ASCII characters (no
	 * control sequences etc.), and that these ASCII characters begin with a
	 * sequence of 1-3 non-blank characters between blanks
	 *
	 * @return <code>true</code> if next bytes are probably printable ASCII
	 * characters starting with a PDF operator, otherwise <code>false</code>
	 */
	private boolean hasNoFollowingBinData(final PushbackInputStream pdfSource) 
			throws IOException
	{
		// as suggested in PDFBOX-1164
		final int readBytes = pdfSource.read(binCharTestArr, 0, MAX_BIN_CHAR_TEST_LENGTH);
		boolean noBinData = true;
		int startOpIdx = -1;
		int endOpIdx = -1;

		if (readBytes > 0)
		{
			for (int bIdx = 0; bIdx < readBytes; bIdx++)
			{
				final byte b = binCharTestArr[bIdx];
				if ((b < 0x09) || ((b > 0x0a) && (b < 0x20) && (b != 0x0d)))
				{
					// control character or > 0x7f -> we have binary data
					noBinData = false;
					break;
				}
				// find the start of a PDF operator
				if (startOpIdx == -1 && !(b == 9 || b == 0x20 || b == 0x0a || b == 0x0d))
				{
					startOpIdx = bIdx;
				}
				else if (startOpIdx != -1 && endOpIdx == -1 && (b == 9 || b == 0x20 || b == 0x0a || b == 0x0d))
				{
					endOpIdx = bIdx;
				}
			}
			
			// only if not close to eof
			if (readBytes == MAX_BIN_CHAR_TEST_LENGTH)
			{
				// a PDF operator is 1-3 bytes long
				if (startOpIdx != -1 && endOpIdx == -1)
				{
					endOpIdx = MAX_BIN_CHAR_TEST_LENGTH;
				}
				if (endOpIdx != -1 && startOpIdx != -1 && endOpIdx - startOpIdx > 3)
				{
					noBinData = false;
				}
			}
			pdfSource.unread(binCharTestArr, 0, readBytes);
		}

		if (!noBinData)
		{
			Log.w("PdfBoxAndroid", "ignoring 'EI' assumed to be in the middle of inline image");
		}

		return noBinData;
	}

	/**
	 * Check whether the output stream ends with 70 ASCII85 data bytes
	 * (33..117). This method is to be called when "EI" and then space/LF/CR
	 * are detected.
	 *
	 * @param imageData output data stream without the "EI"
	 * @return true if this is an ASCII85 line so the "EI" is to be considered
	 * part of the data stream, false if not
	 */
	private boolean hasPrecedingAscii85Data(ByteArrayOutputStream imageData)
	{
		if (imageData.size() < 70)
		{
			return false;
		}
		byte[] tab = imageData.toByteArray();
		for (int i = tab.length - 1; i >= tab.length - 70; --i)
		{
			if (tab[i] < 33 || tab[i] > 117)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * This will read an operator from the stream.
	 *
	 * @return The operator that was read from the stream.
	 *
	 * @throws IOException If there is an error reading from the stream.
	 */
	protected String readOperator() throws IOException
	{
		skipSpaces();

		//average string size is around 2 and the normal string buffer size is
		//about 16 so lets save some space.
		StringBuffer buffer = new StringBuffer(4);
		int nextChar = pdfSource.peek();
		while(
				nextChar != -1 && // EOF
				!isWhitespace(nextChar) &&
				!isClosing(nextChar) &&
				nextChar != '[' &&
				nextChar != '<' &&
				nextChar != '(' &&
				nextChar != '/' &&
				(nextChar < '0' ||
						nextChar > '9' ) )
		{
			char currentChar = (char)pdfSource.read();
			nextChar = pdfSource.peek();
			buffer.append( currentChar );
			// Type3 Glyph description has operators with a number in the name
			if (currentChar == 'd' && (nextChar == '0' || nextChar == '1') ) 
			{
				buffer.append( (char)pdfSource.read() );
				nextChar = pdfSource.peek();
			}
		}
		return buffer.toString();
	}


	private boolean isSpaceOrReturn( int c )
	{
		return c == 10 || c == 13 || c == 32;
	}

	/**
	 * Checks if the next char is a space or a return.
	 * 
	 * @return true if the next char is a space or a return
	 * @throws IOException if something went wrong
	 */
	private boolean hasNextSpaceOrReturn() throws IOException
	{
		return isSpaceOrReturn( pdfSource.peek() );
	}
}
