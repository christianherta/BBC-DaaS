package de.bbcdaas.xingpipeline;

import de.bbcdaas.uima_components.cpe.CollectionProcessingEngine;

/**
 *
 * @author Robert Illers
 */
public class App {

	public static void main(String[] args) {
		
		startUimaPipeline(null);
	}

	private static void startUimaPipeline(String descriptorPath) {

		String cpeDescriptorPath = descriptorPath;

		if (cpeDescriptorPath == null || !cpeDescriptorPath.isEmpty()) {

			cpeDescriptorPath = "cpeDescriptor/cpe-descriptor.xml";
		}

		CollectionProcessingEngine cpe = new CollectionProcessingEngine(cpeDescriptorPath);

		if (cpe.init()) {
			cpe.startProcessing();
		}
	}
}
