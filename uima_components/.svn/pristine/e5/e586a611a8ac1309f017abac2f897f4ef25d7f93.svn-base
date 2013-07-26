package de.bbcdaas.uima_components.cpe;

import de.bbcdaas.common.util.FileReader;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.metadata.CpeCasProcessor;
import org.apache.uima.collection.metadata.CpeCasProcessors;
import org.apache.uima.collection.metadata.CpeConfiguration;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.apache.uima.collection.metadata.CpeResourceManagerConfiguration;
import org.apache.uima.resource.Resource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;

/**
 * Wrapper implementation of the UIMA CollectionProcessingEngine (CPE) for
 * starting UIMA processes from java code.
 * @author Robert Illers
 */
public class CollectionProcessingEngine {

	private static Logger logger = Logger.getLogger(CollectionProcessingEngine.class);
	private org.apache.uima.collection.CollectionProcessingEngine uimaCPE = null;
	private String cpeDescriptorFilePath = "";
	private boolean running = false;

	/**
	 * constructor
	 * @param cpeDescriptorFilePath path to the cpe-descriptor.xml
	 */
	public CollectionProcessingEngine(String cpeDescriptorFilePath) {
		this.cpeDescriptorFilePath = cpeDescriptorFilePath;
	}

	/**
	 *
	 * @param path
	 * @return
	 */
	private XMLInputSource getXMLInputSource(String path) {

		FileReader fileReader = new FileReader(); 
		XMLInputSource xmlInputSource = null;
		// try getting configuration as file
		File file = fileReader.readFile(path, FileReader.FILE_OPENING_TYPE.ABSOLUTE,
			FileReader.FILE_OPENING_TYPE.RELATIVE);
		// if it fails try getting url of configuration from jar (resources are no java.io.files in java, sadly)
		URL url = null;
		if (file == null || !file.isFile() || !file.canRead()) {
			url = fileReader.getUrlByRelativePath(path, true);
			logger.info("Using default CPE descriptor...");
		}

		// create xml input source from file
		if (file != null && file.isFile() && file.canRead()) {

			try {
				xmlInputSource = new XMLInputSource(file);
				logger.info("Using external CPE descriptor...");
			} catch(Exception ex) {
				logger.error("XMLInputSource can not be created.", ex);
				return null;
			}
		} else
		
		// otherwhise create xml input source from resource url
		if (url != null) {
			
			try {
				xmlInputSource = new XMLInputSource(url);
			} catch(Exception ex) {
				logger.error("XMLInputSource can not be created.", ex);
				return null;
			}
		}
		
		if (xmlInputSource == null) {
			logger.error("XMLInputSource can not be created. File not found (path == "+path+")");
		}
		
		return xmlInputSource;
	}

	/**
	 * initialized the CPE engine using the current descriptor.
	 */
	public boolean init() {

		XMLInputSource cpeDescriptorInputSource = this.getXMLInputSource(this.cpeDescriptorFilePath);

		if (cpeDescriptorInputSource != null) {

			try {

				logger.info("CPE descriptor found at "+cpeDescriptorInputSource.getURL().toString());
				//parse CPE descriptor in file
				CpeDescription cpeDesc = UIMAFramework.getXMLParser().parseCpeDescription(cpeDescriptorInputSource);
				this.printCpeDesc(cpeDesc);
				Properties newProps = UIMAFramework.getDefaultPerformanceTuningProperties();
				Map params = new HashMap();
				// cpe parameter can be set here
				params.put(Resource.PARAM_PERFORMANCE_TUNING_SETTINGS, newProps);
				this.uimaCPE = UIMAFramework.produceCollectionProcessingEngine(cpeDesc, params);
				//Create and register a Status Callback Listener
				this.uimaCPE.addStatusCallbackListener(new StatusCallbackListenerImpl());

			} catch (InvalidXMLException ex) {

				logger.error("Parsing CPE descriptor failed.", ex);
				return false;
			} catch (ResourceInitializationException ex) {

				logger.error("Error creating CPE.", ex);
				return false;
			}
		} else {
			logger.error("CPE descriptor file can not be opened. Path: '"+this.cpeDescriptorFilePath+"'.");
			return false;
		}
		return true;
	}

	/**
	 * Prints out the current CPE Description.
	 * @param cpeDesc
	 */
	private void printCpeDesc(CpeDescription cpeDesc) {

		try {

			if (cpeDesc != null) {

				CpeCasProcessors cpeCasProcessors = cpeDesc.getCpeCasProcessors();
				CpeConfiguration cpeConfiguration = cpeDesc.getCpeConfiguration();
				CpeResourceManagerConfiguration resourceManagerConfiguration = cpeDesc.getResourceManagerConfiguration();

				logger.debug("CPE Description settings:");
				logger.debug("-----------------------------------------------");
				if (cpeCasProcessors != null) {
					List<CpeCasProcessor> cpeCasProcessorList = Arrays.asList(cpeCasProcessors.getAllCpeCasProcessors());
					logger.debug("SourceUrlString: "+cpeCasProcessors.getSourceUrlString());
					logger.debug("CasPoolSize: "+cpeCasProcessors.getCasPoolSize());
					logger.debug("Number of CpeCasProcessors: "+cpeCasProcessorList.size());
					int i = 1;
					for (CpeCasProcessor cpeCasProcessor : cpeCasProcessorList) {
						logger.debug("CpeCasProcessor "+i+":");
						logger.debug("cpeCasProcessor.getName(): "+cpeCasProcessor.getName());
						logger.debug("cpeCasProcessor.getBatchSize(): "+cpeCasProcessor.getBatchSize());
						logger.debug("cpeCasProcessor.getDeployment(): "+cpeCasProcessor.getDeployment());
						logger.debug("cpeCasProcessor.getIsParallelizable(): "+cpeCasProcessor.getIsParallelizable());
						i++;
					}
				}
				if (resourceManagerConfiguration != null) {
					logger.debug("resourceManagerConfiguration.getSourceUrl(): "+resourceManagerConfiguration.getSourceUrl());
				}
				if (cpeConfiguration != null) {
					logger.debug("CPE Configuration:");
					logger.debug("SourceURL: "+cpeConfiguration.getSourceUrlString());
				}
			} else {
				logger.error("ERROR: CpeDescription is null.");
			}


		} catch(CpeDescriptorException ex) {
			logger.error("printCpeDesc: ", ex);
		}
	}

	/**
	 * Start the CPE processing thread.
	 */
	public void startProcessing() {

		if (this.uimaCPE != null) {

			if (!this.running) {
				try {
					this.running = true;
					this.uimaCPE.process();
				} catch (ResourceInitializationException ex) {
					logger.error("startProcessing(): ",ex);
				}
			} else {
				logger.error("CPE already running.");
			}
		} else {
			logger.error("CPE not initialized, processing not started.");
		}
	}

	/**
	 * Gets the current state of the CPE process.
	 */
	public int getCpeState() {

		if (this.uimaCPE != null) {

			if (this.uimaCPE.isProcessing()) {
				logger.info("CPE running.");
				return 1;
			} else {
				logger.info("CPE initialized and ready.");
				this.running = false;
				return 2;
			}
		} else {
			logger.info("CPE not initialized.");
			return 0;
		}
	}
}
