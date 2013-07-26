package de.bbcdaas.common.util;

import java.io.FileReader;
import java.io.IOException;
import org.jboss.logging.Logger;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
/**
 *
 * @author Robert Illers
 */
public class SaxParser {

	private Logger logger = Logger.getLogger(this.getClass());
    private XMLReader xmlReader;
    private InputSource inputSource;
    private String pathToXmlFile;

    /**
     *
     * @param pathToXmlFile
     */
    public SaxParser(String pathToXmlFile, ContentHandler contentHandler) {

        try {
            this.xmlReader = XMLReaderFactory.createXMLReader();
            this.xmlReader.setContentHandler(contentHandler);
        } catch (SAXException ex) {
            logger.error("SaxParser()", ex);
        }

        try {
            this.pathToXmlFile = pathToXmlFile;
            this.inputSource = new InputSource(new FileReader(this.pathToXmlFile));
        } catch (IOException ex) {
            logger.error("SaxParser()", ex);
        }
    }

    /**
     *
     */
    public void parse() {

        try {
            logger.info(new StringBuilder().append("Begin parsing '").append(pathToXmlFile).append("'...").toString());
            xmlReader.parse(inputSource);
            logger.info("Done.");
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        } catch (SAXException ex) {
            logger.error(ex.getMessage());
        }
    }
}
