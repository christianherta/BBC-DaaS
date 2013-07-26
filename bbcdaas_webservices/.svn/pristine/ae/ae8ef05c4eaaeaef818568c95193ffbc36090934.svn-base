package de.bbcdaas.webservices.services;

import de.bbcdaas.common.util.FileReader;
import de.bbcdaas.webservices.constants.Constants;
import java.io.File;
import java.net.URL;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * Base class for all webservices
 * @author Robert Illers
 */
public abstract class RestServices {

	protected final Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Gets the webservices configuration.
	 * @return Configuration
	 */
	protected Configuration getWebservicesConfiguration() {

		Configuration config = null;
		FileReader fileReader = new FileReader();
		URL url = this.getClass().getResource(new StringBuilder("/properties/").
			append(Constants.CONFIGURATION_FILE_NAME).toString());
		if (url != null) {
			
			String path = url.toString();
			config = fileReader.readPropertiesConfig(path,
				FileReader.FILE_OPENING_TYPE.ABSOLUTE, FileReader.FILE_OPENING_TYPE.RELATIVE, true);
		}
		if (config == null) {
			logger.error("TODO: catch exception here, configuration not found");
			config = new PropertiesConfiguration();
		}
		return config;
	}
}
