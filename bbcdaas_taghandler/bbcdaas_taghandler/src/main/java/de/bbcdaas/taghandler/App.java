package de.bbcdaas.taghandler;

import de.bbcdaas.taghandler.writer.statistics.StatisticWriter;
import java.io.File;
import java.util.Iterator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * This applications starts a handleTag Process. The tagHandler can be configured
 * in the applicationContext.xml, jdb.properties and the parameter.properties file.
 * @author Robert Illers
 */
public class App {

    private static Logger logger = Logger.getLogger(App.class);

	/**
	 * Main methods that starts the tagHandler process or statistics writer.
	 * @param args
	 */
	public static void main(String[] args) {

			String configFilePath = null;
			Options options = new Options();
			options.addOption("configFilePath", true, "Path to configuration file");
			options.addOption("writestatistics", false, "switch for activating statistics writer");
			CommandLineParser parser = new PosixParser();
			try {

				CommandLine cmd = parser.parse(options, args);
				
				// get configuration file
				if (cmd.hasOption("configFilePath")) {
					configFilePath = cmd.getOptionValue("configFilePath");
				}
				
				// statistics writer
				if (cmd.hasOption("writestatistics")) {	
					writeStatistics();
				}
				
				// tag handler
				if (!cmd.hasOption("writestatistics")) {
					try {
						handleTags(configFilePath);
					} catch (ConfigurationException ex) {
						logger.error(ex.getMessage());
						System.exit(1);
					}
				}
				
			} catch(ParseException ex) {
				logger.error(ex);
			}
        }

        /**
         * Starts the taghandler process with given parameters
		 * @param configFilepath path to configuration file
         */
        private static void handleTags(String configFilepath) throws ConfigurationException {

			logger.info("Starting taghandler...");
            TagHandler tagHandler = ((TagHandler)new ClassPathXmlApplicationContext("applicationContext.xml").
			getBean("tagHandler"));

            if (configFilepath != null) {

                File configFile = new File(configFilepath);
				
                if (configFile.exists() && configFile.isFile()) {
                    
					logger.info("Using parameter from external file...");
                    Configuration config = new PropertiesConfiguration(configFile);
                    Iterator<String> keys = config.getKeys();
                    
					// print config
					logger.info("Configuration:");
					while (keys.hasNext()) {
                        logger.info(keys.next());
                    }
                    // TODO
                    logger.debug("TODO: use these parameters");
                } else {
                    logger.error("Configuration file '"+configFilepath+"' can not be opened.");
                    System.exit(1);
                }

            } else {
                logger.info("Using default parameter...");
            }
            tagHandler.handleTags();
        }

        /**
         * Starts the writing of statistics out of the taghandler database
         */
        private static void writeStatistics() {

			logger.info("Starting statistics writer");
            ((StatisticWriter)new ClassPathXmlApplicationContext("applicationContext.xml").
			getBean("statisticsWriter")).writeStatistics();
        }
}