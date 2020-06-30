package com.tom_roush.pdfbox.pdmodel;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.tom_roush.pdfbox.cos.COSArray;
import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.cos.COSDocument;
import com.tom_roush.pdfbox.cos.COSInteger;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.cos.COSObject;
import com.tom_roush.pdfbox.cos.COSStream;
import com.tom_roush.pdfbox.cos.COSUpdateInfo;
import com.tom_roush.pdfbox.io.IOUtils;
import com.tom_roush.pdfbox.io.RandomAccessBufferedFileInputStream;
import com.tom_roush.pdfbox.pdfparser.BaseParser;
import com.tom_roush.pdfbox.pdfparser.PDFParser;
import com.tom_roush.pdfbox.pdfwriter.COSWriter;
import com.tom_roush.pdfbox.pdmodel.common.COSArrayList;
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle;
import com.tom_roush.pdfbox.pdmodel.common.PDStream;
import com.tom_roush.pdfbox.pdmodel.encryption.AccessPermission;
import com.tom_roush.pdfbox.pdmodel.encryption.PDEncryption;
import com.tom_roush.pdfbox.pdmodel.encryption.ProtectionPolicy;
import com.tom_roush.pdfbox.pdmodel.encryption.SecurityHandler;
import com.tom_roush.pdfbox.pdmodel.encryption.SecurityHandlerFactory;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import com.tom_roush.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import com.tom_roush.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import com.tom_roush.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import com.tom_roush.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface;
import com.tom_roush.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;
import com.tom_roush.pdfbox.pdmodel.interactive.form.PDAcroForm;
import com.tom_roush.pdfbox.pdmodel.interactive.form.PDFieldTreeNode;
import com.tom_roush.pdfbox.pdmodel.interactive.form.PDSignatureField;

import android.util.Log;

/**
 * This is the in-memory representation of the PDF document.
 * The #close() method must be called once the document is no longer needed.
 * 
 * @author Ben Litchfield
 */
public class PDDocument implements Closeable
{
	private final COSDocument document;

	// cached values
	private PDDocumentInformation documentInformation;
	private PDDocumentCatalog documentCatalog;

	// the encryption will be cached here. When the document is decrypted then
	// the COSDocument will not have an "Encrypt" dictionary anymore and this object must be used
	private PDEncryption encryption;

	// holds a flag which tells us if we should remove all security from this documents.
	private boolean allSecurityToBeRemoved;

	// keep tracking customized documentId for the trailer. If null, a new id will be generated
	// this ID doesn't represent the actual documentId from the trailer
	private Long documentId;

	// the PDF parser
	private final BaseParser parser;

	// the File to read incremental data from
	private File incrementalFile;

	// the access permissions of the document
	private AccessPermission accessPermission;
	
	// fonts to subset before saving
	private final Set<PDFont> fontsToSubset = new HashSet<PDFont>();
	
	// Signature interface
	private SignatureInterface signInterface;

	/**
	 * Creates an empty PDF document.
	 * You need to add at least one page for the document to be valid.
	 */
	public PDDocument()
	{
		document = new COSDocument();
		parser = null;

		// First we need a trailer
		COSDictionary trailer = new COSDictionary();
		document.setTrailer(trailer);

		// Next we need the root dictionary.
		COSDictionary rootDictionary = new COSDictionary();
		trailer.setItem(COSName.ROOT, rootDictionary);
		rootDictionary.setItem(COSName.TYPE, COSName.CATALOG);
		rootDictionary.setItem(COSName.VERSION, COSName.getPDFName("1.4"));

		// next we need the pages tree structure
		COSDictionary pages = new COSDictionary();
		rootDictionary.setItem(COSName.PAGES, pages);
		pages.setItem(COSName.TYPE, COSName.PAGES);
		COSArray kidsArray = new COSArray();
		pages.setItem(COSName.KIDS, kidsArray);
		pages.setItem(COSName.COUNT, COSInteger.ZERO);
	}

	/**
	 * This will add a page to the document. This is a convenience method, that will add the page to the root of the
	 * hierarchy and set the parent of the page to the root.
	 * 
	 * @param page The page to add to the document.
	 */
	public void addPage(PDPage page)
	{
		getPages().add(page);
	}

	/**
	 * Add a signature.
	 * 
	 * @param sigObject is the PDSignatureField model
	 * @param signatureInterface is a interface which provides signing capabilities
	 * @throws IOException if there is an error creating required fields
	 */
	public void addSignature(PDSignature sigObject, SignatureInterface signatureInterface) throws IOException
	{
		addSignature(sigObject, signatureInterface, new SignatureOptions());
	}

	/**
	 * This will add a signature to the document.
	 * 
	 * @param sigObject is the PDSignatureField model
	 * @param signatureInterface is a interface which provides signing capabilities
	 * @param options signature options
	 * @throws IOException if there is an error creating required fields
	 */
	public void addSignature(PDSignature sigObject, SignatureInterface signatureInterface,
			SignatureOptions options) throws IOException
	{
		// Reserve content
		// We need to reserve some space for the signature. Some signatures including
		// big certificate chain and we need enough space to store it.
		int preferedSignatureSize = options.getPreferedSignatureSize();
		if (preferedSignatureSize > 0)
		{
			sigObject.setContents(new byte[preferedSignatureSize]);
		}
		else
		{
			sigObject.setContents(new byte[0x2500]);
		}

		// Reserve ByteRange
		sigObject.setByteRange(new int[] { 0, 1000000000, 1000000000, 1000000000 });

		signInterface = signatureInterface;

		//
		// Create SignatureForm for signature
		// and appending it to the document
		//

		// Get the first page
		PDDocumentCatalog catalog = getDocumentCatalog();
		int pageCount = catalog.getPages().getCount();
		if (pageCount == 0)
		{
			throw new IllegalStateException("Cannot sign an empty document");
		}
		int startIndex = Math.min(Math.max(options.getPage(), 0), pageCount - 1);
		PDPage page = catalog.getPages().get(startIndex);

		// Get the AcroForm from the Root-Dictionary and append the annotation
		PDAcroForm acroForm = catalog.getAcroForm();
		catalog.getCOSObject().setNeedToBeUpdated(true);

		if (acroForm == null)
		{
			acroForm = new PDAcroForm(this);
			catalog.setAcroForm(acroForm);
		}
		else
		{
			acroForm.getDictionary().setNeedToBeUpdated(true);
		}

		// For invisible signatures, the annotation has a rectangle array with values [ 0 0 0 0 ]. This annotation is
		// usually attached to the viewed page when the signature is created. Despite not having an appearance, the
		// annotation AP and N dictionaries may be present in some versions of Acrobat. If present, N references the
		// DSBlankXObj (blank) XObject.

		// Create Annotation / Field for signature
		List<PDAnnotation> annotations = page.getAnnotations();

		List<PDFieldTreeNode> fields = acroForm.getFields();
		PDSignatureField signatureField = null;
		if(fields == null) 
		{
			fields = new ArrayList<PDFieldTreeNode>();
			acroForm.setFields(fields);
		}
		for (PDFieldTreeNode pdField : fields)
		{
			if (pdField instanceof PDSignatureField)
			{
				PDSignature signature = ((PDSignatureField) pdField).getSignature();
				if (signature != null && signature.getDictionary().equals(sigObject.getDictionary()))
				{
					signatureField = (PDSignatureField) pdField;
				}
			}
		}
		if (signatureField == null)
		{
			signatureField = new PDSignatureField(acroForm);
			
			// append the signature object
			signatureField.setSignature(sigObject);
			
			// backward linking
			signatureField.getWidget().setPage(page);
		}

		// Set the AcroForm Fields
		List<PDFieldTreeNode> acroFormFields = acroForm.getFields();
		acroForm.getDictionary().setDirect(true);
		acroForm.setSignaturesExist(true);
		acroForm.setAppendOnly(true);

		boolean checkFields = false;
		for (PDFieldTreeNode field : acroFormFields)
		{
			if (field instanceof PDSignatureField
					&& ((PDSignatureField) field).getCOSObject().equals(signatureField.getCOSObject()))
			{
				checkFields = true;
				signatureField.getDictionary().setNeedToBeUpdated(true);
				break;
			}
		}
		if (!checkFields)
		{
			acroFormFields.add(signatureField);
		}

		// Get the object from the visual signature
		COSDocument visualSignature = options.getVisualSignature();

		// Distinction of case for visual and non-visual signature
		if (visualSignature == null) // non-visual signature
		{
			// Set rectangle for non-visual signature to 0 0 0 0
			signatureField.getWidget().setRectangle(new PDRectangle()); // rectangle array [ 0 0 0 0 ]
			// Clear AcroForm / Set DefaultRessource
			acroForm.setDefaultResources(null);
			// Set empty Appearance-Dictionary
			PDAppearanceDictionary ap = new PDAppearanceDictionary();
			COSStream apsStream = getDocument().createCOSStream();
			apsStream.createUnfilteredStream();
			PDAppearanceStream aps = new PDAppearanceStream(apsStream);
			COSDictionary cosObject = (COSDictionary) aps.getCOSObject();
			cosObject.setItem(COSName.SUBTYPE, COSName.FORM);
			cosObject.setItem(COSName.BBOX, new PDRectangle());

			ap.setNormalAppearance(aps);
			ap.getCOSObject().setDirect(true);
			signatureField.getWidget().setAppearance(ap);
		}
		else
			// visual signature
		{
			// Obtain visual signature object
			List<COSObject> cosObjects = visualSignature.getObjects();

			boolean annotNotFound = true;
			boolean sigFieldNotFound = true;
			COSDictionary acroFormDict = acroForm.getDictionary();
			for (COSObject cosObject : cosObjects)
			{
				if (!annotNotFound && !sigFieldNotFound)
				{
					break;
				}

				COSBase base = cosObject.getObject();
				if (base instanceof COSDictionary)
				{
					COSBase ft = ((COSDictionary) base).getItem(COSName.FT);
					COSBase type = ((COSDictionary) base).getItem(COSName.TYPE);
					COSBase apDict = ((COSDictionary) base).getItem(COSName.AP);

					// Search for signature annotation
					if (annotNotFound && COSName.ANNOT.equals(type))
					{
						COSDictionary cosBaseDict = (COSDictionary) base;

						// Read and set the Rectangle for visual signature
						COSArray rectAry = (COSArray) cosBaseDict.getItem(COSName.RECT);
						PDRectangle rect = new PDRectangle(rectAry);
						signatureField.getWidget().setRectangle(rect);
						annotNotFound = false;
					}

					// Search for Signature-Field
					if (sigFieldNotFound && COSName.SIG.equals(ft) && apDict != null)
					{
						COSDictionary cosBaseDict = (COSDictionary) base;

						// read and set Appearance Dictionary
						PDAppearanceDictionary ap =
								new PDAppearanceDictionary((COSDictionary)cosBaseDict.getDictionaryObject(COSName.AP));
						ap.getCOSObject().setDirect(true);
						signatureField.getWidget().setAppearance(ap);

						// read and set AcroForm DefaultResource
						COSDictionary dr = (COSDictionary) cosBaseDict.getItem(COSName.DR);
						if (dr != null)
						{
							dr.setDirect(true);
							dr.setNeedToBeUpdated(true);
							acroFormDict.setItem(COSName.DR, dr);
						}
						sigFieldNotFound = false;
					}
				}
			}

			if (annotNotFound || sigFieldNotFound)
			{
				throw new IllegalArgumentException("Template is missing required objects");
			}
		}

		// Get the annotations of the page and append the signature-annotation to it
		// take care that page and acroforms do not share the same array (if so, we don't need to add it twice)
		if (!(annotations instanceof COSArrayList &&
				acroFormFields instanceof COSArrayList &&
				((COSArrayList) annotations).toList().equals(((COSArrayList) acroFormFields).toList()) &&
				checkFields))
		{
			annotations.add(signatureField.getWidget());
		}
		((COSUpdateInfo)page.getCOSObject()).setNeedToBeUpdated(true);
	}

	/**
	 * This will add a signature field to the document.
	 * 
	 * @param sigFields are the PDSignatureFields that should be added to the document
	 * @param signatureInterface is a interface which provides signing capabilities
	 * @param options signature options
	 * @throws IOException if there is an error creating required fields
	 */
	public void addSignatureField(List<PDSignatureField> sigFields, SignatureInterface signatureInterface,
			SignatureOptions options) throws IOException
	{
		PDDocumentCatalog catalog = getDocumentCatalog();
		((COSUpdateInfo) catalog.getCOSObject()).setNeedToBeUpdated(true);

		PDAcroForm acroForm = catalog.getAcroForm();
		if (acroForm == null)
		{
			acroForm = new PDAcroForm(this);
			catalog.setAcroForm(acroForm);
		}

		COSDictionary acroFormDict = acroForm.getDictionary();
		acroFormDict.setDirect(true);
		acroFormDict.setNeedToBeUpdated(true);
		if (!acroForm.isSignaturesExist())
		{
			// 1 if at least one signature field is available
			acroForm.setSignaturesExist(true);
		}

		List<PDFieldTreeNode> field = acroForm.getFields();

		for (PDSignatureField sigField : sigFields)
		{
			sigField.getDictionary().setNeedToBeUpdated(true);
			// Check if the field already exists
			boolean checkFields = false;
			for (PDFieldTreeNode fieldNode : field)
			{
				if (fieldNode instanceof PDSignatureField
						&& fieldNode.getCOSObject().equals(sigField.getCOSObject()))
				{
					checkFields = true;
					sigField.getDictionary().setNeedToBeUpdated(true);
					break;
				}
			}

			if (!checkFields)
			{
				field.add(sigField);
			}

			// Check if we need to add a signature
			if (sigField.getSignature() != null)
			{
				sigField.getDictionary().setNeedToBeUpdated(true);
				if (options == null)
				{
					// TODO ??
				}
				addSignature(sigField.getSignature(), signatureInterface, options);
			}
		}
	}

	/**
	 * Remove the page from the document.
	 * 
	 * @param page The page to remove from the document.
	 */
	public void removePage(PDPage page)
	{
		getPages().remove(page);
	}

	/**
	 * Remove the page from the document.
	 * 
	 * @param pageNumber 0 based index to page number.
	 */
	public void removePage(int pageNumber)
	{
		getPages().remove(pageNumber);
	}

	/**
	 * This will import and copy the contents from another location. Currently the content stream is stored in a scratch
	 * file. The scratch file is associated with the document. If you are adding a page to this document from another
	 * document and want to copy the contents to this document's scratch file then use this method otherwise just use
	 * the addPage method.
	 * 
	 * @param page The page to import.
	 * @return The page that was imported.
	 * 
	 * @throws IOException If there is an error copying the page.
	 */
	public PDPage importPage(PDPage page) throws IOException
	{
		PDPage importedPage = new PDPage(new COSDictionary(page.getCOSObject()));
		InputStream is = null;
		OutputStream os = null;
		try
		{
			PDStream src = page.getStream();
			if (src != null)
			{
				PDStream dest = new PDStream(document.createCOSStream());
				dest.addCompression();
				importedPage.setContents(dest);
				is = src.createInputStream();
				os = dest.createOutputStream();
				IOUtils.copy(is, os);
			}
			addPage(importedPage);
		}
		finally
		{
			if (is != null)
			{
				is.close();
			}
			if (os != null)
			{
				os.close();
			}
		}
		return importedPage;
	}

	/**
	 * Constructor that uses an existing document. The COSDocument that is passed in must be valid.
	 * 
	 * @param doc The COSDocument that this document wraps.
	 */
	public PDDocument(COSDocument doc)
	{
		this(doc, null);
	}

	/**
	 * Constructor that uses an existing document. The COSDocument that is passed in must be valid.
	 * 
	 * @param doc The COSDocument that this document wraps.
	 * @param usedParser the parser which is used to read the pdf
	 */
	public PDDocument(COSDocument doc, BaseParser usedParser)
	{
		this(doc, usedParser, null);
	}

	/**
	 * Constructor that uses an existing document. The COSDocument that is passed in must be valid.
	 * 
	 * @param doc The COSDocument that this document wraps.
	 * @param usedParser the parser which is used to read the pdf
	 * @param permission he access permissions of the pdf
	 */
	public PDDocument(COSDocument doc, BaseParser usedParser, AccessPermission permission)
	{
		document = doc;
		parser = usedParser;
		accessPermission = permission;
	}

	/**
	 * This will get the low level document.
	 * 
	 * @return The document that this layer sits on top of.
	 */
	public COSDocument getDocument()
	{
		return document;
	}

	/**
	 * This will get the document info dictionary. This is guaranteed to not return null.
	 * 
	 * @return The documents /Info dictionary
	 */
	public PDDocumentInformation getDocumentInformation()
	{
		if (documentInformation == null)
		{
			COSDictionary trailer = document.getTrailer();
			COSDictionary infoDic = (COSDictionary) trailer.getDictionaryObject(COSName.INFO);
			if (infoDic == null)
			{
				infoDic = new COSDictionary();
				trailer.setItem(COSName.INFO, infoDic);
			}
			documentInformation = new PDDocumentInformation(infoDic);
		}
		return documentInformation;
	}

	/**
	 * This will set the document information for this document.
	 * 
	 * @param info The updated document information.
	 */
	public void setDocumentInformation(PDDocumentInformation info)
	{
		documentInformation = info;
		document.getTrailer().setItem(COSName.INFO, info.getDictionary());
	}

	/**
	 * This will get the document CATALOG. This is guaranteed to not return null.
	 * 
	 * @return The documents /Root dictionary
	 */
	public PDDocumentCatalog getDocumentCatalog()
	{
		if (documentCatalog == null)
		{
			COSDictionary trailer = document.getTrailer();
			COSBase dictionary = trailer.getDictionaryObject(COSName.ROOT);
			if (dictionary instanceof COSDictionary)
			{
				documentCatalog = new PDDocumentCatalog(this, (COSDictionary) dictionary);
			}
			else
			{
				documentCatalog = new PDDocumentCatalog(this);
			}
		}
		return documentCatalog;
	}

	/**
	 * This will tell if this document is encrypted or not.
	 * 
	 * @return true If this document is encrypted.
	 */
	public boolean isEncrypted()
	{
		return document.isEncrypted();
	}

	/**
	 * This will get the encryption dictionary for this document. This will still return the parameters if the document
	 * was decrypted. As the encryption architecture in PDF documents is plugable this returns an abstract class,
	 * but the only supported subclass at this time is a
	 * PDStandardEncryption object.
	 *
	 * @return The encryption dictionary(most likely a PDStandardEncryption object)
	 */
	public PDEncryption getEncryption()
	{
		if (encryption == null && isEncrypted())
		{
			encryption = new PDEncryption(document.getEncryptionDictionary());
		}
		return encryption;
	}

	/**
	 * This will set the encryption dictionary for this document.
	 * 
	 * @param encryption The encryption dictionary(most likely a PDStandardEncryption object)
	 * 
	 * @throws IOException If there is an error determining which security handler to use.
	 */
	public void setEncryptionDictionary(PDEncryption encryption) throws IOException
	{
		this.encryption = encryption;
	}

	/**
	 * This will return the last signature.
	 * 
	 * @return the last signature as <code>PDSignatureField</code>.
	 * @throws IOException if no document catalog can be found.
	 */
	public PDSignature getLastSignatureDictionary() throws IOException
	{
		List<PDSignature> signatureDictionaries = getSignatureDictionaries();
		int size = signatureDictionaries.size();
		if (size > 0)
		{
			return signatureDictionaries.get(size - 1);
		}
		return null;
	}

	/**
	 * Retrieve all signature fields from the document.
	 * 
	 * @return a <code>List</code> of <code>PDSignatureField</code>s
	 * @throws IOException if no document catalog can be found.
	 */
	public List<PDSignatureField> getSignatureFields() throws IOException
	{
		List<PDSignatureField> fields = new LinkedList<PDSignatureField>();
		PDAcroForm acroForm = getDocumentCatalog().getAcroForm();
		if (acroForm != null)
		{
			List<COSDictionary> signatureDictionary = document.getSignatureFields(false);
			for (COSDictionary dict : signatureDictionary)
			{
				fields.add(new PDSignatureField(acroForm, dict, null));
			}
		}
		return fields;
	}

	/**
	 * Retrieve all signature dictionaries from the document.
	 * 
	 * @return a <code>List</code> of <code>PDSignatureField</code>s
	 * @throws IOException if no document catalog can be found.
	 */
	public List<PDSignature> getSignatureDictionaries() throws IOException
	{
		List<COSDictionary> signatureDictionary = document.getSignatureDictionaries();
		List<PDSignature> signatures = new LinkedList<PDSignature>();
		for (COSDictionary dict : signatureDictionary)
		{
			signatures.add(new PDSignature(dict));
		}
		return signatures;
	}
	
	/**
	 * Returns the list of fonts which will be subset before the document is saved.
	 */
	Set<PDFont> getFontsToSubset()
	{
		return fontsToSubset;
	}
	
	/**
	 * Parses PDF with non sequential parser.
	 * 
	 * @param file file to be loaded
	 * 
	 * @return loaded document
	 * 
	 * @throws IOException in case of a file reading or parsing error
	 */
	public static PDDocument load(File file) throws IOException
	{
		return load(file, "", false);
	}

	/**
	 * Parses PDF with non sequential parser.
	 * 
	 * @param file file to be loaded
	 * @param useScratchFiles enables the usage of a scratch file if set to true
	 * 
	 * @return loaded document
	 * 
	 * @throws IOException in case of a file reading or parsing error
	 */
	public static PDDocument load(File file, boolean useScratchFiles) throws IOException
	{
		return load(file, "", null, null, useScratchFiles);
	}

	/**
	 * Parses PDF with non sequential parser.
	 * 
	 * @param file file to be loaded
	 * @param password password to be used for decryption
	 * 
	 * @return loaded document
	 * 
	 * @throws IOException in case of a file reading or parsing error
	 */
	public static PDDocument load(File file, String password) throws IOException
	{
		return load(file, password, null, null, false);
	}

	/**
	 * Parses PDF with non sequential parser.
	 * 
	 * @param file file to be loaded
	 * @param password password to be used for decryption
	 * @param useScratchFiles enables the usage of a scratch file if set to true
	 * 
	 * @return loaded document
	 * 
	 * @throws IOException in case of a file reading or parsing error
	 */
	public static PDDocument load(File file, String password, boolean useScratchFiles) throws IOException
	{
		return load(file, password, null, null, useScratchFiles);
    }

    /**
     * Parses PDF with non sequential parser.
     * 
     * @param file file to be loaded
     * @param password password to be used for decryption
     * @param keyStore key store to be used for decryption when using public key security 
     * @param alias alias to be used for decryption when using public key security
     * 
     * @return loaded document
     * 
     * @throws IOException in case of a file reading or parsing error
     */
    public static PDDocument load(File file, String password, InputStream keyStore, String alias)
            throws IOException
    {
        return load(file, password, keyStore, alias, false);
    }

    /**
     * Parses PDF with non sequential parser.
     * 
     * @param file file to be loaded
     * @param password password to be used for decryption
     * @param keyStore key store to be used for decryption when using public key security 
     * @param alias alias to be used for decryption when using public key security
     * @param useScratchFiles enables the usage of a scratch file if set to true
     * 
     * @return loaded document
     * 
     * @throws IOException in case of a file reading or parsing error
     */
    public static PDDocument load(File file, String password, InputStream keyStore, String alias,
            boolean useScratchFiles) throws IOException
    {
    	PDFParser parser = new PDFParser(file, password, keyStore, alias, useScratchFiles);
    	parser.parse();
    	PDDocument doc = parser.getPDDocument();
    	doc.incrementalFile = file;
    	return doc;
	}

	/**
	 * Parses PDF with non sequential parser.
	 * 
	 * @param input stream that contains the document.
	 * 
	 * @return loaded document
	 * 
	 * @throws IOException in case of a file reading or parsing error
	 */
	public static PDDocument load(InputStream input) throws IOException
	{
		return load(input, "", null, null, false);
	}

	/**
	 * Parses PDF with non sequential parser.
	 * 
	 * @param input stream that contains the document.
	 * @param useScratchFiles enables the usage of a scratch file if set to true
	 * 
	 * @return loaded document
	 * 
	 * @throws IOException in case of a file reading or parsing error
	 */
	public static PDDocument load(InputStream input, boolean useScratchFiles) throws IOException
	{
		return load(input, "", null, null, useScratchFiles);
	}

	/**
	 * Parses PDF with non sequential parser.
	 * 
	 * @param input stream that contains the document.
	 * @param password password to be used for decryption
	 * 
	 * @return loaded document
	 * 
	 * @throws IOException in case of a file reading or parsing error
	 */
	public static PDDocument load(InputStream input, String password)
			throws IOException
	{
		return load(input, password, false);
	}

	/**
	 * Parses PDF with non sequential parser.
	 * 
	 * @param input stream that contains the document.
	 * @param password password to be used for decryption
	 * @param useScratchFiles enables the usage of a scratch file if set to true
	 * 
	 * @return loaded document
	 * 
	 * @throws IOException in case of a file reading or parsing error
	 */
	public static PDDocument load(InputStream input, String password, boolean useScratchFiles)
			throws IOException
	{
		return load(input, password, null, null, false);
    }

    /**
     * Parses PDF with non sequential parser.
     * 
     * @param input stream that contains the document.
     * @param password password to be used for decryption
     * @param keyStore key store to be used for decryption when using public key security 
     * @param alias alias to be used for decryption when using public key security
     * 
     * @return loaded document
     * 
     * @throws IOException in case of a file reading or parsing error
     */
    public static PDDocument load(InputStream input, String password, InputStream keyStore, String alias)
            throws IOException
    {
        return load(input, password, keyStore, alias, false);
    }

    /**
     * Parses PDF with non sequential parser.
     * 
     * @param input stream that contains the document.
     * @param password password to be used for decryption
     * @param keyStore key store to be used for decryption when using public key security 
     * @param alias alias to be used for decryption when using public key security
     * @param useScratchFiles enables the usage of a scratch file if set to true
     * 
     * @return loaded document
     * 
     * @throws IOException in case of a file reading or parsing error
     */
    public static PDDocument load(InputStream input, String password, InputStream keyStore,
    		String alias, boolean useScratchFiles) throws IOException
    {
        PDFParser parser = new PDFParser(input, password, keyStore, alias, useScratchFiles);
		parser.parse();
		return parser.getPDDocument();
	}

	/**
	 * Save the document to a file.
	 * 
	 * @param fileName The file to save as.
	 *
	 * @throws IOException if the output could not be written
	 */
	public void save(String fileName) throws IOException
	{
		save(new File(fileName));
	}

	/**
	 * Save the document to a file.
	 * 
	 * @param file The file to save as.
	 *
	 * @throws IOException if the output could not be written
	 */
	public void save(File file) throws IOException
	{
		save(new FileOutputStream(file));
	}

	/**
	 * This will save the document to an output stream.
	 * 
	 * @param output The stream to write to.
	 *
	 * @throws IOException if the output could not be written
	 */
	public void save(OutputStream output) throws IOException
	{
		if (document.isClosed())
		{
			throw new IOException("Cannot save a document which has been closed");
		}
		// subset designated fonts
		for (PDFont font : fontsToSubset)
		{
			font.subset();
		}
		fontsToSubset.clear();

		// save PDF
		COSWriter writer = new COSWriter(output);
		try
		{
			writer.write(this);
			writer.close();
		}
		finally
		{
			writer.close();
		}
	}

	/**
	 * Save the PDF as an incremental update.
	 *
	 * Use of this method is discouraged, use {@link #saveIncremental(OutputStream)} instead.
	 *
	 * @param output stream to write
	 * @throws IOException if the output could not be written
	 */
	public void saveIncremental(OutputStream output) throws IOException
	{
		InputStream input = new RandomAccessBufferedFileInputStream(incrementalFile);
		COSWriter writer = null;
		try
		{
			writer = new COSWriter(output, input);
			writer.write(this, signInterface);
			writer.close();
		}
		finally
		{
			if (writer != null)
			{
				writer.close();
			}
		}
	}

	/**
	 * Returns the page at the given index.
	 * 
	 * @param pageIndex the page index
	 * @return the page at the given index.
	 */
	public PDPage getPage(int pageIndex) // todo: REPLACE most calls to this method with BELOW method
	{
		return getDocumentCatalog().getPages().get(pageIndex);
	}

	// TODO new
	public PDPageTree getPages() {
		return getDocumentCatalog().getPages();
	}

	/**
	 * This will return the total page count of the PDF document.
	 * 
	 * @return The total number of pages in the PDF document.
	 */
	public int getNumberOfPages()
	{
		return getDocumentCatalog().getPages().getCount();
	}

	/**
	 * This will close the underlying COSDocument object.
	 * 
	 * @throws IOException If there is an error releasing resources.
	 */
	@Override
	public void close() throws IOException
	{
		if (!document.isClosed())
		{
			// close all intermediate I/O streams
			document.close();
			
			// close the source PDF stream, if we read from one
			if (parser != null)
			{
				parser.close();
			}
		}
	}

	/**
	 * Protects the document with the protection policy pp. The document content will be really encrypted when it will
	 * be saved. This method only marks the document for encryption.
	 *
	 * @see com.tom_roush.pdfbox.pdmodel.encryption.StandardProtectionPolicy
	 * @see com.tom_roush.pdfbox.pdmodel.encryption.PublicKeyProtectionPolicy
	 * 
	 * @param policy The protection policy.
	 * 
	 * @throws IOException if there isn't any suitable security handler.
	 */
	public void protect(ProtectionPolicy policy) throws IOException
	{
		if (!isEncrypted())
		{
			encryption = new PDEncryption();
		}

		SecurityHandler securityHandler = SecurityHandlerFactory.INSTANCE.newSecurityHandlerForPolicy(policy);
		if (securityHandler == null)
		{
			throw new IOException("No security handler for policy " + policy);
		}

		getEncryption().setSecurityHandler(securityHandler);
	}

	/**
	 * Returns the access permissions granted when the document was decrypted. If the document was not decrypted this
	 * method returns the access permission for a document owner (ie can do everything). The returned object is in read
	 * only mode so that permissions cannot be changed. Methods providing access to content should rely on this object
	 * to verify if the current user is allowed to proceed.
	 * 
	 * @return the access permissions for the current user on the document.
	 */
	public AccessPermission getCurrentAccessPermission()
	{
		if(accessPermission == null) {
			accessPermission = AccessPermission.getOwnerAccessPermission();
		}
		return accessPermission;
	}

	/**
	 * Indicates if all security is removed or not when writing the pdf.
	 * 
	 * @return returns true if all security shall be removed otherwise false
	 */
	public boolean isAllSecurityToBeRemoved()
	{
		return allSecurityToBeRemoved;
	}

	/**
	 * Activates/Deactivates the removal of all security when writing the pdf.
	 * 
	 * @param removeAllSecurity remove all security if set to true
	 */
	public void setAllSecurityToBeRemoved(boolean removeAllSecurity)
	{
		allSecurityToBeRemoved = removeAllSecurity;
	}

	/**
	 * Provides the document ID.
	 * 
	 * @return the dcoument ID
	 */
	public Long getDocumentId()
	{
		return documentId;
	}

	/**
	 * Sets the document ID to the given value.
	 * 
	 * @param docId the new document ID
	 */
	public void setDocumentId(Long docId)
	{
		documentId = docId;
	}
	
	/**
	 * Returns the PDF specification version this document conforms to.
	 *
	 * @return the PDF version (e.g. 1.4f)
	 */
	public float getVersion()
	{
		float headerVersionFloat = getDocument().getVersion();
		// there may be a second version information in the document catalog starting with 1.4
		if (headerVersionFloat >= 1.4f)
		{
			String catalogVersion = getDocumentCatalog().getVersion();
			float catalogVersionFloat = -1;
			if (catalogVersion != null)
			{
				try
				{
					catalogVersionFloat = Float.parseFloat(catalogVersion);
				}
				catch(NumberFormatException exception)
				{
					Log.e("PdfBoxAndroid", "Can't extract the version number of the document catalog.", exception);
				}
			}
			// the most recent version is the correct one
			return Math.max(catalogVersionFloat, headerVersionFloat);
		}
		else
		{
			return headerVersionFloat;
		}
	}

	/**
	 * Sets the PDF specification version for this document.
	 *
	 * @param newVersion the new PDF version (e.g. 1.4f)
	 *
	 */
	public void setVersion(float newVersion)
	{
		float currentVersion = getVersion();
		// nothing to do?
		if (newVersion == currentVersion)
		{
			return;
		}
		// the version can't be downgraded
		if (newVersion < currentVersion)
		{
			Log.e("PdfBoxAndroid", "It's not allowed to downgrade the version of a pdf.");
			return;
		}
		// update the catalog version if the document version is >= 1.4
		if (getDocument().getVersion() >= 1.4f)
		{
			getDocumentCatalog().setVersion(Float.toString(newVersion));
		}
		else
		{
			// versions < 1.4f have a version header only
			getDocument().setVersion(newVersion);
		}
	}
}
