package com.tom_roush.pdfbox.pdfparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.tom_roush.pdfbox.cos.COSArray;
import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSDocument;
import com.tom_roush.pdfbox.cos.COSInteger;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.cos.COSObjectKey;
import com.tom_roush.pdfbox.cos.COSStream;

/**
 * This will parse a PDF 1.5 (or better) Xref stream and
 * extract the xref information from the stream.
 *
 *  @author Justin LeFebvre
 */
public class PDFXrefStreamParser extends BaseParser
{
    private COSStream stream;
    private XrefTrailerResolver xrefTrailerResolver;

    /**
     * Constructor.
     *
     * @param strm The stream to parse.
     * @param doc The document for the current parsing.
     * @param resolver resolver to read the xref/trailer information
     *
     * @throws IOException If there is an error initializing the stream.
     */
    public PDFXrefStreamParser(COSStream strm, COSDocument doc, XrefTrailerResolver resolver )
    	throws IOException
    {
        super(strm.getUnfilteredStream());
        document = doc;
        stream = strm;
        this.xrefTrailerResolver = resolver;
    }

    /**
     * Parses through the unfiltered stream and populates the xrefTable HashMap.
     * @throws IOException If there is an error while parsing the stream.
     */
    public void parse() throws IOException
    {
        try
        {
            COSArray xrefFormat = (COSArray)stream.getDictionaryObject(COSName.W);
            COSArray indexArray = (COSArray)stream.getDictionaryObject(COSName.INDEX);
            /*
             * If Index doesn't exist, we will use the default values.
             */
            if(indexArray == null)
            {
                indexArray = new COSArray();
                indexArray.add(COSInteger.ZERO);
                indexArray.add(stream.getDictionaryObject(COSName.SIZE));
            }

            ArrayList<Long> objNums = new ArrayList<Long>();

            /*
             * Populates objNums with all object numbers available
             */
            Iterator<COSBase> indexIter = indexArray.iterator();
            while(indexIter.hasNext())
            {
                long objID = ((COSInteger)indexIter.next()).longValue();
                int size = ((COSInteger)indexIter.next()).intValue();
                for(int i = 0; i < size; i++)
                {
                    objNums.add(objID + i);
                }
            }
            Iterator<Long> objIter = objNums.iterator();
            /*
             * Calculating the size of the line in bytes
             */
            int w0 = xrefFormat.getInt(0);
            int w1 = xrefFormat.getInt(1);
            int w2 = xrefFormat.getInt(2);
            int lineSize = w0 + w1 + w2;

            while(pdfSource.available() > 0 && objIter.hasNext())
            {
                byte[] currLine = new byte[lineSize];
                pdfSource.read(currLine);

                int type = 0;
                /*
                 * Grabs the number of bytes specified for the first column in
                 * the W array and stores it.
                 */
                for(int i = 0; i < w0; i++)
                {
                    type += (currLine[i] & 0x00ff) << ((w0 - i - 1)* 8);
                }
                //Need to remember the current objID
                Long objID = objIter.next();
                /*
                 * 3 different types of entries.
                 */
                switch(type)
                {
                    case 0:
                        /*
                         * Skipping free objects
                         */
                        break;
                    case 1:
                        int offset = 0;
                        for(int i = 0; i < w1; i++)
                        {
                            offset += (currLine[i + w0] & 0x00ff) << ((w1 - i - 1) * 8);
                        }
                        int genNum = 0;
                        for(int i = 0; i < w2; i++)
                        {
                            genNum += (currLine[i + w0 + w1] & 0x00ff) << ((w2 - i - 1) * 8);
                        }
                        COSObjectKey objKey = new COSObjectKey(objID, genNum);
                        xrefTrailerResolver.setXRef(objKey, offset);
                        break;
                    case 2:
                        /*
                         * object stored in object stream; 2nd argument is object number of object stream;
                         * 3rd argument index of object within object stream
                         * 
                         * For sequential PDFParser we do not need this information
                         * because
                         * These objects are handled by the dereferenceObjects() method
                         * since they're only pointing to object numbers
                         * 
                         * However for XRef aware parsers we have to know which objects contain
                         * object streams. We will store this information in normal xref mapping
                         * table but add object stream number with minus sign in order to
                         * distinguish from file offsets
                         */
                        int objstmObjNr = 0;
                        for(int i = 0; i < w1; i++)
                        {
                            objstmObjNr += (currLine[i + w0] & 0x00ff) << ((w1 - i - 1) * 8);
                        }    
                        objKey = new COSObjectKey( objID, 0 );
                        xrefTrailerResolver.setXRef( objKey, -objstmObjNr );
                        break;
                    default:
                        break;
                }
            }
        }
        finally
        {
            pdfSource.close();
        }
    }
}
