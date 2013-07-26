package de.bbcdaas.disambiguation;

import de.bbcdaas.disambiguation.evaluation.EvaluatorRunner;
import de.bbcdaas.disambiguation.wikipedia.dataexport.lucene.WikiCategoryExporter;
import de.bbcdaas.disambiguation.wikipedia.tools.GoldStandardEnhancer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

/**
 * Main command line execution entry point of disambiguation application.
 * @author Robert Illers
 */
public class App {

	public static final Logger logger = Logger.getLogger(App.class);

	public static void main(String[] args) {

		logger.info("-= BBC-DAAS Disambiguation =-");
		logger.info("use parameter '-help' for usage informations");

		Options allOptions = new Options();

		// common options
		OptionGroup commonOptions = new OptionGroup();
		commonOptions.addOption(new Option("help", false, "Shows the help"));
		commonOptions.addOption(new Option("task", true, "luceneimport | export | evaluate | enhance"));
		allOptions.addOptionGroup(commonOptions);

		// evaluation options
		OptionGroup evaluationOptions = new OptionGroup();
		evaluationOptions.addOption(new Option("goldstandardXmlPathEv", true, "evaluate: path to goldstandard xml input file"));
		evaluationOptions.addOption(new Option("xmlOutputPathEv", true, "evaluate: output path for generated xml result file"));
		allOptions.addOptionGroup(evaluationOptions);

		// enhance options
		OptionGroup enhancerOptions = new OptionGroup();
		enhancerOptions.addOption(new Option("xmlOutputPathEn", true, "enhance: path to enhanced goldstandard xml output file"));
		allOptions.addOptionGroup(enhancerOptions);

		// luceneimport options
		OptionGroup luceneImportOptions = new OptionGroup();
		luceneImportOptions.addOption(new Option("propertiesFilePathLi", true, "luceneimport: path to lucene import properties file"));
		allOptions.addOptionGroup(luceneImportOptions);

		CommandLineParser parser = new PosixParser();
		HelpFormatter helpFormatter = new HelpFormatter();

		try {

			CommandLine cmd = parser.parse(allOptions, args);

			if (cmd.hasOption("help")) {
				helpFormatter.printHelp("java -jar [fileName] -task [task] [taskparameter]", allOptions);
			} else 

			// parameter: luceneimport|uimaimport|export|evaluate|enhance|installPear
			if (cmd.hasOption("task")) {

				if (cmd.getOptionValue("task").equals("luceneimport")) {
					logger.info("WikiLuceneDataImporter started.");
					// parameter: propertiesFilePathLi
					de.bbcdaas.disambiguation.wikipedia.dataimport.lucene.WikiDataImporter.main(args);
				} else

				if (cmd.getOptionValue("task").equals("export")) {
					logger.info("CategoryExporter started.");
					// parameter : none
					WikiCategoryExporter.main(args);
				} else

				if (cmd.getOptionValue("task").equals("evaluate")) {
					logger.info("Evaluator started.");
					// parameter: goldstandardXmlPathEv, xmlOutputPathEv
					EvaluatorRunner.main(args);
				} else

				// enhances a goldstandard xml file containing tag clouds by additional tags
				if (cmd.getOptionValue("task").equals("enhance")) {
					logger.info("GoldStandard enhancer started.");
					// parameter: xmlOutputPathEn
					GoldStandardEnhancer.main(args);
				} 
			}

		} catch(ParseException ex) {
			logger.error(ex);
		}
	}
}
