package de.bbcdaas.uima_components.xing_tagcloud_writer;

import de.bbcdaas.common.util.FileReader;
import de.bbcdaas.uima_components.cas_types.documentmetadata.DocumentMetadata;
import de.bbcdaas.uima_components.cas_types.tagclouddescriptor.TagCloudDescriptor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;

/**
 *
 * @author herta
 * @author Robert Illers
 */
public class XingTagCloudWriter extends CasConsumer_ImplBase {

	private static Logger logger = Logger.getLogger(XingTagCloudWriter.class);
	private int count = 0;
	private boolean writeAsJson, writeAsTextFile;
	private String userNameToTagCloudSeparator;
	private String outputFolder;
	private String outputFileName;
	private String outputPath;
	private String outputPathTxt;
	private String outputPathJson;

	/**
	 *
	 * @throws ResourceInitializationException
	 */
	@Override
	public void initialize() throws ResourceInitializationException {
		
		Configuration config = new FileReader().readPropertiesConfig("properties/xingTagCloudWriter.properties",
			FileReader.FILE_OPENING_TYPE.ABSOLUTE, FileReader.FILE_OPENING_TYPE.RELATIVE, true);
		
		logger.info("-------------- Configuration: ----------------------");
		
		if (config != null) {
		
			this.writeAsTextFile = config.getBoolean("writeAsTextFile", true);
			logger.info("WriteAsTextFile: "+this.writeAsTextFile);
			
			this.writeAsJson = config.getBoolean("writeAsJson", true);
			logger.info("WriteAsJson: "+this.writeAsJson);	
			
			this.outputFolder = config.getString("outputFolder");
			logger.info("outputFolder: "+this.outputFolder);	
			
			this.outputFileName = config.getString("outputFileName");
			logger.info("outputFileName: "+this.outputFileName);
			
			this.outputPath = new StringBuilder(this.outputFolder).
				append(File.separator).append(this.outputFileName).toString();
			
			if (this.writeAsJson) {
				this.outputPathJson = new StringBuilder(this.outputPath).append(".json").toString();
			}
			
			if (this.writeAsTextFile) {
				this.outputPathTxt = new StringBuilder(this.outputPath).append(".txt").toString();
			}
			
			this.userNameToTagCloudSeparator = config.getString("userNameToTagCloudSeparator", "::");
			logger.info("UserNameToTagCloudSeparator: "+this.userNameToTagCloudSeparator);	
			
		} else {
			logger.error("xingTagCloudWriter.properties not found.");
		}
		
		logger.info("-------------- /Configuration ----------------------");
	}

	/**
	 *
	 * @param aCas
	 * @throws ResourceProcessException
	 */
	@Override
	public void processCas(CAS cas) throws ResourceProcessException {

		JCas jcas;
		try {
			jcas = cas.getJCas();
		} catch (CASException e) {
			throw new ResourceProcessException(e);
		}
		
		FSIterator tagCloudDescriptorIterator = jcas.getJFSIndexRepository().getAllIndexedFS(TagCloudDescriptor.type);
		FSIterator documentMetadataIterator = jcas.getJFSIndexRepository().getAllIndexedFS(DocumentMetadata.type);

		String documentURL = null;
		TagCloudDescriptor tagCloudDescriptor = null;
		
		if (documentMetadataIterator.hasNext()) {
			
			DocumentMetadata documentMetadata = (DocumentMetadata) documentMetadataIterator.next();
			documentURL = documentMetadata.getDocumentURL();
		} 
		
		if (tagCloudDescriptorIterator.hasNext()) {
			tagCloudDescriptor = (TagCloudDescriptor)tagCloudDescriptorIterator.next();
		}
		
		if (documentURL != null && tagCloudDescriptor != null) {

			String tags = tagCloudDescriptor.getTags();

			BufferedWriter bwTxt = null;
			BufferedWriter bwJson = null;
			count++;

			try {

				String[] splittedDocumentURL = documentURL.split("/");
				String userName = null;
				if (splittedDocumentURL.length != 0) {
					userName = splittedDocumentURL[splittedDocumentURL.length-1];
				}
				
				if (this.writeAsTextFile) {
		
					File outputFileTxt = new FileReader().readFile(this.outputPathTxt,
						FileReader.FILE_OPENING_TYPE.RELATIVE, FileReader.FILE_OPENING_TYPE.ABSOLUTE);
					
					bwTxt = new BufferedWriter(new FileWriter(outputFileTxt, true));
					bwTxt.write(new StringBuilder().append(userName).
						append(this.userNameToTagCloudSeparator).append(tags).toString());
					bwTxt.newLine();
					bwTxt.flush();
				}
				
				if (this.writeAsJson) {
					
					File outputFileJson = new FileReader().readFile(this.outputPathJson,
						FileReader.FILE_OPENING_TYPE.RELATIVE, FileReader.FILE_OPENING_TYPE.ABSOLUTE);
					
					bwJson = new BufferedWriter(new FileWriter(outputFileJson, true));
					bwJson.write(toJSON(userName, tags));
					bwJson.flush();
				}
			
			} catch (IOException ex) {
				logger.error("processCas(): ",ex);
			}
			
			finally {
				
				try {
					if (this.writeAsTextFile && bwTxt != null) {
						bwTxt.close();
					}
					if (this.writeAsJson && bwJson != null) {
						bwJson.close();
					}
				} catch (IOException ex) {
					logger.error(ex.getMessage());
				}
			}
		}
	}

	/**
	 * 
	 * @param userName
	 * @param tags
	 * @return json string
	 */
	private String toJSON(String userName, String tags) {

		StringBuilder out = new StringBuilder("{ ").
			append("\"name\": \"").append(userName).append("\", ").
			append("\"tags\": [ ").append(tags).
			append("]}");

		return out.toString();
	}

	/**
	 *
	 */
	@Override
	public void destroy() {
		
		super.destroy();
		logger.info("Number of entities written: "+this.count);
	}
}