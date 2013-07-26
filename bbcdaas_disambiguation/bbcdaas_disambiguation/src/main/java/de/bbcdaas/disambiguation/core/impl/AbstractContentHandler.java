package de.bbcdaas.disambiguation.core.impl;

import de.bbcdaas.disambiguation.core.configs.AbstractContentHandlerConfig;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Implementation of a SAX content handler for parsing a XML input file for
 * writing data into a lucene index.
 * @author Robert Illers
 */
public abstract class AbstractContentHandler <T extends AbstractContentHandlerConfig> implements ContentHandler {

    protected final Logger logger = Logger.getLogger(this.getClass());
	private T config;
	protected StringBuffer buffer = null;
	protected String currentValue;
	protected int indexCount = 0;

	/**
	 * Constructor
	 * @param config
	 */
    public AbstractContentHandler(T config) {
        this.config = config;
    }

	/**
	 * Gets the configuration for the disambiguation engine.
	 * @return an implementation of AbstractDisambiguationEngineConfig
	 */
	protected T getConfig() {
        return config;
    }

	/**
	 *
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param atts
	 * @throws SAXException
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

		if (config.getStartElements().contains(localName)) {
			this.buffer = new StringBuffer();
		}
	}

	/**
	 *
	 * @param uri
	 * @param localName
	 * @param qName
	 * @throws SAXException
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (this.config.getStartElements().contains(localName)) {
			this.currentValue = this.buffer.toString();
			this.buffer = null;
		}
	}

	/**
	 * Reads a line from the document that is being parsed.
	 * @param ch
	 * @param start
	 * @param length
	 * @throws SAXException
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {

		if (this.buffer != null) {
			this.buffer.append(ch, start, length);
		}
	}

	/**
	 *
	 * @param name
	 * @throws SAXException
	 */
	@Override
	public void skippedEntity(String name) throws SAXException {}

	/**
	 *
	 * @param target
	 * @param data
	 * @throws SAXException
	 */
	@Override
	public void processingInstruction(String target, String data) throws SAXException {}

	/**
	 *
	 * @param ch
	 * @param start
	 * @param length
	 * @throws SAXException
	 */
	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}

	/**
	 *
	 * @param prefix
	 * @param uri
	 * @throws SAXException
	 */
	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {}

	/**
	 *
	 * @param prefix
	 * @throws SAXException
	 */
	@Override
	public void endPrefixMapping(String prefix) throws SAXException {}

	/**
	 *
	 * @param locator
	 */
	@Override
	public void setDocumentLocator(Locator locator) {}

	/**
	 *
	 * @throws SAXException
	 */
	@Override
	public void startDocument() throws SAXException {}
}
