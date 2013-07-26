package de.bbcdaas.uima_components.xing_tagcloud_html_extractor;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Html content handler for the tag cloud html extractor
 * @author herta
 * @author Robert Illers
 */
public final class XingTagCloudHtmlContentHandler implements ContentHandler {
	
	// parameter:
	private final String tagCloudName;
	private final String tagSeparator;
	
	// parse states:
	private boolean inSomeTagCloud = false;
	private boolean checkTagCloudName = false;
	private boolean inSpecifiedTagCloud = false;
	private boolean inTagCloudTranslateItem = false;
	private boolean inTagLink = false;
	
	// result
	private String tagCloudContent = "";
	
	/**
	 * Constructor
	 * @param tagCloudName Name of the tagCloud to be extracted
	 * @param tagSeparator a string separating tags of a tagCloud, default is ";"
	 */
	public XingTagCloudHtmlContentHandler(String tagCloudName, String tagSeparator) {
		
		this.tagCloudName = tagCloudName;
		this.tagSeparator = tagSeparator;
		this.reset();
	}

	/**
	 * The extracted tagCloud content
	 * @return tagCloudContent
	 */
	public String getTagCloudContent() {
		return tagCloudContent;
	}
	
	/**
	 * Resets the members to start values
	 */
	public void reset() {
		
		this.tagCloudContent = "";
		this.inSomeTagCloud = false;
		this.checkTagCloudName = false;
		this.inSpecifiedTagCloud = false;
		this.inTagCloudTranslateItem = false;
		this.inTagLink = false;		
	}
	
	/**
	 * Perform character analyzation of a character string in a documents line
	 * @param ch characters
	 * @param start offset
	 * @param length string lenght
	 * @throws SAXException 
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		
		String content = new String(ch, start, length);
		
		// possible tagCloud name field reached
		if (this.inSomeTagCloud && this.checkTagCloudName) {
			
			// name field contains tag cloud name?
			if (content.contains(this.tagCloudName)) {
				this.inSpecifiedTagCloud = true;
			}
			this.checkTagCloudName = false;
			return;
		}
		
		// get tag of current tagCloud
		if (this.inTagLink) {
			
			this.tagCloudContent += content;
			this.tagCloudContent += this.tagSeparator;
		}
	}

	/**
	 * Perform opening tag analyzation
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param attributes
	 * @throws SAXException 
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		// entered tag cloud
		if (localName.contentEquals("DL")) {
			
			inSomeTagCloud = true;
			return;
		}
		
		// found tagCloud name field
		if (this.inSomeTagCloud && localName.contentEquals("DT")) {
			
			this.checkTagCloudName = true;
			return;
		}
		
		// entered tag cloud translate item field (wrapping tag links of tag cloud)
		if (inSpecifiedTagCloud && localName.contentEquals("DD")) {
			
			for (int i = 0; i < attributes.getLength(); i++) {
				
				if (attributes.getQName(i).equals("class") &&			
					attributes.getValue(i).equals("translate-item")) {
					
					this.inTagCloudTranslateItem = true;
					return;
				}
			}	
		}
		
		// found tag link in translate item of tagCloud
		if (inTagCloudTranslateItem && localName.contentEquals("A")) {
			
			// ignore 'edit' link
			boolean isEditLink = false;
			for (int i = 0; i < attributes.getLength(); i++) {
				
				
				if (attributes.getQName(i).equals("class") &&			
					attributes.getValue(i).equals("text-button-right")) {
					
					isEditLink = true;
					break;
				} 
			}
			
			if (!isEditLink) {
				this.inTagLink = true;
			}
		}
	}
	
	/**
	 * Perform closing tag analyzation
	 * @param uri
	 * @param localName
	 * @param qName
	 * @throws SAXException 
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		// tag cloud left
		if (inSomeTagCloud && localName.contentEquals("DL")) {
			
			this.inSomeTagCloud = false;
			this.inSpecifiedTagCloud = false;
			return;
		}
		
		// tag cloud translate item left
		if (inTagCloudTranslateItem && localName.contentEquals("DD")) {
			
			this.inTagCloudTranslateItem = false;
			return;
		}
		
		// tag link left
		if (inTagCloudTranslateItem && localName.contentEquals("A")) {
			this.inTagLink = false;
		}
	}	

	/* --------------------- not implemented methods ------------------------ */
	
	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {}	
	
	@Override
	public void endPrefixMapping(String prefix) throws SAXException {}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {}

	@Override
	public void setDocumentLocator(Locator locator) {}

	@Override
	public void skippedEntity(String name) throws SAXException {}

	@Override
	public void startDocument() throws SAXException {}
	
	@Override
	public void endDocument() throws SAXException {}
	
	/* -------------------- /not implemented methods ------------------------ */
}