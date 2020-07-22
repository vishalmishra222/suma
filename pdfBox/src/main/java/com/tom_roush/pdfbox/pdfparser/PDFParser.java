package com.tom_roush.pdfbox.pdfparser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.cos.COSDocument;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.cos.COSNull;
import com.tom_roush.pdfbox.cos.COSObject;
import com.tom_roush.pdfbox.io.IOUtils;
import com.tom_roush.pdfbox.io.PushBackInputStream;
import com.tom_roush.pdfbox.io.RandomAccessBufferedFileInputStream;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.encryption.AccessPermission;
import com.tom_roush.pdfbox.pdmodel.encryption.DecryptionMaterial;
import com.tom_roush.pdfbox.pdmodel.encryption.PDEncryption;
import com.tom_roush.pdfbox.pdmodel.encryption.PublicKeyDecryptionMaterial;
import com.tom_roush.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;

import android.util.Log;

public class PDFParser extends COSParser
{
    private final RandomAccessBufferedFileInputStream raStream;
    private String password = "";
    private InputStream keyStoreInputStream = null;
    private String keyAlias = null;

    private AccessPermission accessPermission;

    private static final InputStream EMPTY_INPUT_STREAM = new ByteArrayInputStream(new byte[0]);

    private File tempPDFFile;

    /**
     * Constructs parser for given file using memory buffer.
     * 
     * @param filename the filename of the pdf to be parsed
     * 
     * @throws IOException If something went wrong.
     */
    public PDFParser(String filename) throws IOException
    {
        this(new File(filename), null, false);
    }

    /**
     * Constructs parser for given file using memory buffer.
     * 
     * @param filename the filename of the pdf to be parsed.
     * @param useScratchFiles use a buffer for temporary storage.
     * 
     * @throws IOException If something went wrong.
     */
    public PDFParser(String filename, boolean useScratchFiles) throws IOException
    {
        this(new File(filename), null, useScratchFiles);
    }

    /**
     * Constructs parser for given file using given buffer for temporary
     * storage.
     * 
     * @param file the pdf to be parsed
     * 
     * @throws IOException If something went wrong.
     */
    public PDFParser(File file) throws IOException
    {
        this(file, "", false);
    }

    /**
     * Constructs parser for given file using given buffer for temporary
     * storage.
     * 
     * @param file the pdf to be parsed
     * @param useScratchFiles use a buffer for temporary storage.
     * 
     * @throws IOException If something went wrong.
     */
    public PDFParser(File file, boolean useScratchFiles) throws IOException
    {
        this(file, "", useScratchFiles);
    }

    /**
     * Constructs parser for given file using given buffer for temporary storage.
     * 
     * @param file the pdf to be parsed
     * @param decryptionPassword password to be used for decryption
     * 
     * @throws IOException If something went wrong.
     */
    public PDFParser(File file, String decryptionPassword) throws IOException
    {
        this (file, decryptionPassword, false);
    }

    /**
     * Constructs parser for given file using given buffer for temporary storage.
     * 
     * @param file the pdf to be parsed.
     * @param decryptionPassword password to be used for decryption.
     * @param useScratchFiles use a buffer for temporary storage.
     * 
     * @throws IOException If something went wrong.
     */
    public PDFParser(File file, String decryptionPassword, boolean useScratchFiles)
            throws IOException
    {
        this(file, decryptionPassword, null, null, useScratchFiles);
    }

    /**
     * Constructs parser for given file using given buffer for temporary storage.
     * 
     * @param file the pdf to be parsed.
     * @param decryptionPassword password to be used for decryption.
     * @param keyStore key store to be used for decryption when using public key security 
     * @param alias alias to be used for decryption when using public key security
     * 
     * @throws IOException If something went wrong.
     */
    public PDFParser(File file, String decryptionPassword, InputStream keyStore, String alias)
            throws IOException
    {
        this(file, decryptionPassword, keyStore, alias, false);
    }

    /**
     * Constructs parser for given file using given buffer for temporary storage.
     * 
     * @param file the pdf to be parsed.
     * @param decryptionPassword password to be used for decryption.
     * @param keyStore key store to be used for decryption when using public key security 
     * @param alias alias to be used for decryption when using public key security
     * @param useScratchFiles use a buffer for temporary storage.
     * 
     * @throws IOException If something went wrong.
     */
    public PDFParser(File file, String decryptionPassword, InputStream keyStore, String alias,
            boolean useScratchFiles) throws IOException
    {
        super(EMPTY_INPUT_STREAM);
        fileLen = file.length();
        raStream = new RandomAccessBufferedFileInputStream(file);
        password = decryptionPassword;
        keyStoreInputStream = keyStore;
        keyAlias = alias;
        init(useScratchFiles);
    }

    /**
     * Constructor.
     * 
     * @param input input stream representing the pdf.
     * @throws IOException If something went wrong.
     */
    public PDFParser(InputStream input) throws IOException
    {
        this(input, "", false);
    }

    /**
     * Constructor.
     * 
     * @param input input stream representing the pdf.
     * @param useScratchFiles use a buffer for temporary storage.
     * 
     * @throws IOException If something went wrong.
     */
    public PDFParser(InputStream input, boolean useScratchFiles) throws IOException
    {
        this(input, "", useScratchFiles);
    }

    /**
     * Constructor.
     * 
     * @param input input stream representing the pdf.
     * @param decryptionPassword password to be used for decryption.
     * @throws IOException If something went wrong.
     */
    public PDFParser(InputStream input, String decryptionPassword) throws IOException
    {
        this(input, decryptionPassword, false);
    }

    /**
     * Constructor.
     * 
     * @param input input stream representing the pdf.
     * @param decryptionPassword password to be used for decryption.
     * @param useScratchFiles use a buffer for temporary storage.
     *
     * @throws IOException If something went wrong.
     */
    public PDFParser(InputStream input, String decryptionPassword, boolean useScratchFiles)
            throws IOException
    {
        this(input, decryptionPassword, null, null, useScratchFiles);
    }

    /**
     * Constructor.
     * 
     * @param input input stream representing the pdf.
     * @param decryptionPassword password to be used for decryption.
     * @param keyStore key store to be used for decryption when using public key security 
     * @param alias alias to be used for decryption when using public key security
     *
     * @throws IOException If something went wrong.
     */
    public PDFParser(InputStream input, String decryptionPassword, InputStream keyStore,
            String alias) throws IOException
    {
        this(input, decryptionPassword, keyStore, alias, false);
    }

    /**
     * Constructor.
     * 
     * @param input input stream representing the pdf.
     * @param decryptionPassword password to be used for decryption.
     * @param keyStore key store to be used for decryption when using public key security 
     * @param alias alias to be used for decryption when using public key security
     * @param useScratchFiles use a buffer for temporary storage.
     *
     * @throws IOException If something went wrong.
     */
    public PDFParser(InputStream input, String decryptionPassword, InputStream keyStore,
            String alias, boolean useScratchFiles) throws IOException
    {
        super(EMPTY_INPUT_STREAM);
        tempPDFFile = createTmpFile(input);
        fileLen = tempPDFFile.length();
        raStream = new RandomAccessBufferedFileInputStream(tempPDFFile);
        password = decryptionPassword;
        keyStoreInputStream = keyStore;
        keyAlias = alias;
        init(useScratchFiles);
    }

    private void init(boolean useScratchFiles) throws IOException
    {
        String eofLookupRangeStr = System.getProperty(SYSPROP_EOFLOOKUPRANGE);
        if (eofLookupRangeStr != null)
        {
            try
            {
                setEOFLookupRange(Integer.parseInt(eofLookupRangeStr));
            }
            catch (NumberFormatException nfe)
            {
            	Log.w("PdfBoxAndroid", "System property " + SYSPROP_EOFLOOKUPRANGE
                        + " does not contain an integer value, but: '" + eofLookupRangeStr + "'");
            }
        }
        document = new COSDocument(useScratchFiles);
        pdfSource = new PushBackInputStream(raStream, 4096);
    }

    /**
     * This will get the PD document that was parsed.  When you are done with
     * this document you must call close() on it to release resources.
     *
     * @return The document at the PD layer.
     *
     * @throws IOException If there is an error getting the document.
     */
    public PDDocument getPDDocument() throws IOException
    {
        return new PDDocument( getDocument(), this, accessPermission );
    }

    /**
     * The initial parse will first parse only the trailer, the xrefstart and all xref tables to have a pointer (offset)
     * to all the pdf's objects. It can handle linearized pdfs, which will have an xref at the end pointing to an xref
     * at the beginning of the file. Last the root object is parsed.
     * 
     * @throws IOException If something went wrong.
     */
    protected void initialParse() throws IOException
    {
        COSDictionary trailer = null;
        // parse startxref
        long startXRefOffset = getStartxrefOffset();
        if (startXRefOffset > -1)
        {
            trailer = parseXref(startXRefOffset);
        }
        else if (isLenient())
        {
            trailer = rebuildTrailer();
        }
        // prepare decryption if necessary
        prepareDecryption();
    
        // PDFBOX-1557 - ensure that all COSObject are loaded in the trailer
        // PDFBOX-1606 - after securityHandler has been instantiated
        for (COSBase trailerEntry : trailer.getValues())
        {
            if (trailerEntry instanceof COSObject)
            {
                COSObject tmpObj = (COSObject) trailerEntry;
                parseObjectDynamically(tmpObj, false);
            }
        }
        // parse catalog or root object
        COSObject root = (COSObject) trailer.getItem(COSName.ROOT);
    
        if (root == null)
        {
            throw new IOException("Missing root object specification in trailer.");
        }
    
        parseObjectDynamically(root, false);
    
        COSObject catalogObj = document.getCatalog();
        if (catalogObj != null && catalogObj.getObject() instanceof COSDictionary)
        {
            parseDictObjects((COSDictionary) catalogObj.getObject(), (COSName[]) null);
            document.setDecrypted();
        }
        initialParseDone = true;
    }

    /**
     * This will parse the stream and populate the COSDocument object.  This will close
     * the stream when it is done parsing.
     *
     * @throws IOException If there is an error reading from the stream or corrupt data
     * is found.
     */
    public void parse() throws IOException
    {
         // set to false if all is processed
         boolean exceptionOccurred = true; 
         try
         {
            // PDFBOX-1922 read the version header and rewind
            if (!parsePDFHeader() && !parseFDFHeader())
            {
                throw new IOException( "Error: Header doesn't contain versioninfo" );
            }
    
            if (!initialParseDone)
            {
                initialParse();
            }
            exceptionOccurred = false;
        }
        finally
        {
            IOUtils.closeQuietly(pdfSource);
            IOUtils.closeQuietly(keyStoreInputStream);
    
            deleteTempFile();
    
            if (exceptionOccurred && document != null)
            {
                try
                {
                    document.close();
                    document = null;
                }
                catch (IOException ioe)
                {
                }
            }
        }
    }

    /**
     * Remove the temporary file. A temporary file is created if this class is instantiated with an InputStream
     */
    private void deleteTempFile()
    {
        if (tempPDFFile != null)
        {
            try
            {
                if (!tempPDFFile.delete())
                {
                	Log.w("PdfBoxAndroid", "Temporary file '" + tempPDFFile.getName() + "' can't be deleted");
                }
            }
            catch (SecurityException e)
            {
            	Log.w("PdfBoxAndroid", "Temporary file '" + tempPDFFile.getName() + "' can't be deleted", e);
            }
        }
    }

    /**
     * Prepare for decryption.
     * 
     * @throws IOException if something went wrong
     */
    private void prepareDecryption() throws IOException
    {
        COSBase trailerEncryptItem = document.getTrailer().getItem(COSName.ENCRYPT);
        if (trailerEncryptItem != null && !(trailerEncryptItem instanceof COSNull))
        {
            if (trailerEncryptItem instanceof COSObject)
            {
                COSObject trailerEncryptObj = (COSObject) trailerEncryptItem;
                parseDictionaryRecursive(trailerEncryptObj);
            }
            try
            {
                PDEncryption encryption = new PDEncryption(document.getEncryptionDictionary());
    
                DecryptionMaterial decryptionMaterial;
                if (keyStoreInputStream != null)
                {
                    KeyStore ks = KeyStore.getInstance("PKCS12");
                    ks.load(keyStoreInputStream, password.toCharArray());
    
                    decryptionMaterial = new PublicKeyDecryptionMaterial(ks, keyAlias, password);
                }
                else
                {
                    decryptionMaterial = new StandardDecryptionMaterial(password);
                }
    
                securityHandler = encryption.getSecurityHandler();
                securityHandler.prepareForDecryption(encryption, document.getDocumentID(),
                        decryptionMaterial);
                accessPermission = securityHandler.getCurrentAccessPermission();
            }
            catch (IOException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new IOException("Error (" + e.getClass().getSimpleName()
                        + ") while creating security handler for decryption", e);
            }
        }
    }

    /**
     * Resolves all not already parsed objects of a dictionary recursively.
     * 
     * @param dictionaryObject dictionary to be parsed
     * @throws IOException if something went wrong
     * 
     */
    private void parseDictionaryRecursive(COSObject dictionaryObject) throws IOException
    {
        parseObjectDynamically(dictionaryObject, true);
        COSDictionary dictionary = (COSDictionary)dictionaryObject.getObject();
        for(COSBase value : dictionary.getValues())
        {
            if (value instanceof COSObject)
            {
                COSObject object = (COSObject)value;
                if (object.getObject() == null)
                {
                    parseDictionaryRecursive(object);
                }
            }
        }
    }

}
