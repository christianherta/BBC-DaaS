package de.bbcdaas.uima_components.paradigm_indexer;

import de.bbcdaas.common.util.FileReader;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.resource.ResourceProcessException;

/**
 * Reads a text file containing paradigmatic relations and puts them
 * into a lucene index.
 * @author Robert Illers
 */
public class ParadigmIndexer extends CasConsumer_ImplBase {

	private static Logger logger = Logger.getLogger(ParadigmIndexer.class);
	private ParadigmRelationParser parser;
	
	/**
	 * Constructor
	 */
	public ParadigmIndexer() {
		
		initPropertyParameter();
		initParadigmRelationParser();
	}
	
	/**
	 * 
	 */
	private void initPropertyParameter() {
		
		Configuration config = new FileReader().readPropertiesConfig("properties/paradigmIndexer.properties",
			FileReader.FILE_OPENING_TYPE.ABSOLUTE, FileReader.FILE_OPENING_TYPE.RELATIVE, true);
		
		if (config != null) {
			
			logger.info("-------------- Configuration: ----------------------");
			
			logger.info("-------------- /Configuration ----------------------");
			
		} else {
			logger.error("paradigmIndexer.properties not found.");
		}
	}
	
	/**
	 * 
	 */
	private void initParadigmRelationParser() {
		this.parser = new ParadigmRelationParser();
	}
	
	/**
	 * 
	 * @param cas
	 * @throws ResourceProcessException 
	 */
	@Override
	public void processCas(CAS cas) throws ResourceProcessException {
	
		String documentText = cas.getDocumentText();
	}
}
