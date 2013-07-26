package de.bbcdaas.uima_components.xing_tagcloud_html_extractor;

import de.bbcdaas.common.util.FileReader;
import de.bbcdaas.uima_components.cas_types.documentmetadata.DocumentMetadata;
import de.bbcdaas.uima_components.cas_types.tagclouddescriptor.TagCloudDescriptor;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/**
 * Extractor for getting tag clouds out of a html document.
 * @author herta
 * @author Robert Illers
 */
public class XingTagCloudHtmlExtractor extends JCasAnnotator_ImplBase {

	private static Logger logger = Logger.getLogger(XingTagCloudHtmlExtractor.class);
	private XMLReader xmlReader;
	private XingTagCloudHtmlContentHandler contentHandler;
	
	// parameter:
	private List<String> supportedDocumentURLs = new ArrayList<String>();
	private String tagCloudName;
	private String tagSeparator;
	private int tagLenghtMin = 2;
	private int tagAmountMin = 2;
	
	// for debugging: 
	private boolean logDocumentText = false;

	/**
	 * Constructor
	 */
	public XingTagCloudHtmlExtractor() {

		initPropertyParameter();
		initContentHandler();
		initSaxParser();
	}
	
	/**
	 * read the html extractor parameter	
	*/
	private void initPropertyParameter() {
		
		Configuration config = new FileReader().readPropertiesConfig("properties/xingTagCloudHtmlExtractor.properties",
			FileReader.FILE_OPENING_TYPE.ABSOLUTE, FileReader.FILE_OPENING_TYPE.RELATIVE, true);
		
		if (config != null) {
			
			logger.info("-------------- Configuration: ----------------------");
			
			int i = 1;
			String supportedDocumentURL;
			logger.info("Supported Document URLs:");
			while((supportedDocumentURL = config.getString("supportedDocumentURL_"+i, null)) != null) {
				this.supportedDocumentURLs.add(supportedDocumentURL);
				logger.info(supportedDocumentURL);
				i++;
			}
			
			this.tagCloudName = config.getString("tagCloudName", "Ich biete");
			logger.info("TagCloudName: "+this.tagCloudName);
			
			this.tagSeparator = config.getString("tagSeparator", ";");
			logger.info("TagSeparator: "+this.tagSeparator);
			
			this.tagLenghtMin = config.getInt("tagLenghtMin", 2);
			logger.info("TagLenghtMin: "+this.tagLenghtMin);
			
			this.tagAmountMin = config.getInt("tagAmountMin", 2);
			logger.info("TagAmountMin: "+this.tagAmountMin);
			
			this.logDocumentText = config.getBoolean("logDocumentText", false);
			logger.info("LogDocumentText: "+this.logDocumentText);
			
			logger.info("-------------- /Configuration ----------------------");
			
		} else {
			logger.error("xingTagCloudHtmlExtractor.properties not found.");
		}
	}
	
	/**
	 * 
	 */
	private void initContentHandler() {
		this.contentHandler = new XingTagCloudHtmlContentHandler(this.tagCloudName, this.tagSeparator);
	}
	
	/**
	 * init the sax parser
	 */
	private void initSaxParser() {
		
		this.xmlReader = new HtmlSaxParser();
		try {
			this.xmlReader.setFeature(HtmlSaxParser.FEATURE_AUGMENTATIONS, true);
		} catch (SAXNotRecognizedException e) {
			logger.error(e.getMessage());
		} catch (SAXNotSupportedException e) {
			logger.error(e.getMessage());
		}
		this.xmlReader.setContentHandler(this.contentHandler);
	}

	/**
	 * Executes the html extraction process from the documents text into the 
	 * document descriptor cas object.
	 * @param jcas
	 * @throws AnalysisEngineProcessException
	 */
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		String documentText = jcas.getDocumentText();
		FSIterator iterator = jcas.getJFSIndexRepository().getAllIndexedFS(DocumentMetadata.type);

		if (iterator.hasNext()) {

			DocumentMetadata docMetadata = (DocumentMetadata) iterator.next();
			String documentURL = docMetadata.getDocumentURL();
			
			// check for supported urls
			boolean supportedURL = false;
			for (String supportedDocumentURL : this.supportedDocumentURLs) {
			
				if (documentURL.contains(supportedDocumentURL)) {
					
					supportedURL = true;
					break;
				}
			}
			
			if (supportedURL) {
			
				//logger.info("Document found: "+documentURL);
				
				if (this.logDocumentText) {

					logger.info("------------------ Document text --------------------");
					logger.info(documentText);
					logger.info("----------------- /Document text --------------------");
				}
				
				// extract the relevant content from the document text
				this.extract(jcas, documentText);
			}
		}
	}

	/**
	 * Get the relevant content from the documents text by using a xml reader;
	 * @param documentText source text
	 * @param rC relevant content object
	 * @return true if relevant content found
	 */
	public void extract(JCas jcas, String documentText) {
		
		try {
				
			Reader reader = new StringReader(documentText);
			this.xmlReader.parse(new InputSource(reader));

		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (SAXException e) {
			logger.error(e.getMessage());
		}

		// get tags and reset content handler
		String tags = cleanTagCloud(this.contentHandler.getTagCloudContent(), this.tagSeparator);
		this.contentHandler.reset();
		
		if (!tags.isEmpty()) {
			
			//logger.info("Extracted Tags: "+tags);
			fillTagCloudDescriptor(jcas, this.tagCloudName, tags);
		} else {
			//logger.info("No useable tagCloud found.");
		}
	}

	/**
	 * Set the relevant content into the tag cloud descriptor Cas
	 * @param jcas
	 * @param tagCloudName 
	 * @param tags 
	 */
	public void fillTagCloudDescriptor(JCas jcas, String tagCloudName, String tags) {

		TagCloudDescriptor tcd = new TagCloudDescriptor(jcas);
		tcd.setCloudName(tagCloudName);
		tcd.setTags(tags);
		jcas.addFsToIndexes(tcd);
	}
	
	/**
	 * Cleans the tags of a tagcloud from unwanted chars and performs some condition checks
	 * @param tagCloud
	 * @param tagSeparator 
	 * @return cleaned tagCloud
	 */
	public String cleanTagCloud(String tagCloud, String tagSeparator) {

		String[] tags = tagCloud.split(tagSeparator);
		String resultTagCloud = "";
		int readTagCount = 1;
		int usableTagCount = 0;
		
		for (String tag : tags) {
			
			StringBuilder sb = new StringBuilder(tag);
			
			// delete unwanted chars
			for (int i = sb.length() - 1; i > 0; i--){
				
				if (!(sb.charAt(i) >= ' ' && sb.charAt(i) < '~' ) &&
					!(sb.charAt(i) >= 192 && sb.charAt(i) <= 255)) {
					sb.deleteCharAt(i);
				}
			}
			
			// check for min taglenght after trimming whitespaces
			if (sb.toString().trim().length() >= this.tagLenghtMin) {
				
				resultTagCloud += sb.toString().trim();
				usableTagCount++;
				if (readTagCount < tags.length) {
					resultTagCloud += tagSeparator;
				}
			}
			// end tag clean algorithm
			readTagCount++;
		}
		
		// eliminate tagCloud if it has not enough tags
		if (usableTagCount < this.tagAmountMin) {
			resultTagCloud = "";
		}
		
		return resultTagCloud;
	}
}