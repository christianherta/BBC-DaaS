package de.bbcdaas.wikipediapipeline;

import de.bbcdaas.uima_components.cpe.CollectionProcessingEngine;

/**
 *
 * @author Robil
 */
public class App {
	
	public static void main(String[] args) {

		startUimaPipeline(null);
	}

	private static void startUimaPipeline(String descriptorPath) {

		String cpeDescriptorPath = descriptorPath;

		if (cpeDescriptorPath == null || !cpeDescriptorPath.isEmpty()) {

			cpeDescriptorPath = new StringBuilder().
				append("cpeDescriptor").append("/").
				append("cpe-descriptor.xml").toString();
		}

		CollectionProcessingEngine cpe = new CollectionProcessingEngine(cpeDescriptorPath);

		if (cpe.init()) {
			cpe.startProcessing();
		}
	}
}
