package de.bbcdaas.disambiguation.evaluation;

import de.bbcdaas.common.util.FileReader;
import de.bbcdaas.disambiguation.evaluation.core.AbstractEvaluator;
import de.bbcdaas.disambiguation.evaluation.core.EvaluationConstants;
import de.bbcdaas.disambiguation.evaluation.core.VarParameter;
import de.bbcdaas.disambiguation.evaluation.wikipedia.WikiDisambiguationEngineEvaluator;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

/**
 * Evaluation tool for generating combinations of parameters and results from
 * disambiguation engines.
 * @author Robert Illers
 */
public final class EvaluatorRunner {

	private static Logger logger = Logger.getLogger(EvaluatorRunner.class);
	private List<AbstractEvaluator> evaluatorList = new ArrayList<AbstractEvaluator>();

	/**
	 * Default evaluation execution
	 * @param args pathToGoldstandardXMLFile and xmlOutputPath
	 */
	public static void main(String[] args) {

		Options options = new Options();
		options.addOption("help", true, "Shows the help");
		options.addOption("goldstandardXmlPathEv", true, "path to goldstandard xml input file");
		options.addOption("xmlOutputPathEv", true, "output path for generated xml result file");
		CommandLineParser parser = new PosixParser();
		HelpFormatter helpFormater = new HelpFormatter();

		try {

			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("help")) {
				helpFormater.printHelp("java -jar [fileName]", options);
			}

			if (cmd.hasOption("goldstandardXmlPathEv")) {

				String goldstandardXmlInputPath = cmd.getOptionValue("goldstandardXmlPathEv");

				if (cmd.hasOption("xmlOutputPathEv")) {

					String xmlOutputPath = cmd.getOptionValue("xmlOutputPathEv");
					EvaluatorRunner evaluatorRunner = new EvaluatorRunner();

					WikiDisambiguationEngineEvaluator wikiEvaluator = new WikiDisambiguationEngineEvaluator(goldstandardXmlInputPath, xmlOutputPath);
					wikiEvaluator.setVarParameters(evaluatorRunner.readEvaluationProperties(WikiConstants.EVALUATION_CONFIGURATION_FILE_NAME));
					// if set to true, disambiguation will not be executed
					wikiEvaluator.setTestRun(false);
					evaluatorRunner.addEvaluator(wikiEvaluator);

					evaluatorRunner.run();
				} else {
					logger.error("Evaluation parameter xmlOutputFile missing");
				}
			} else {
				logger.error("Evaluation parameter goldstandardXmlInputPath missing");
			}
		} catch(ParseException ex) {
			logger.error(ex);
		}
	}

	/**
	 * Get the configuration for the evaluation containing the definitions of variable parameters
	 * @return List<VarParameter>
	 */
	private List<VarParameter> readEvaluationProperties(String evaluatorPropertiesFileName) {

		List<VarParameter> varParameter = new ArrayList<VarParameter>();

		String relPath = new StringBuilder(WikiConstants.CONFIGURATION_CONFIG_FOLDER_NAME).
				append(File.separator).append(evaluatorPropertiesFileName).toString();
		Configuration config = new FileReader().readPropertiesConfig(relPath,
			FileReader.FILE_OPENING_TYPE.RELATIVE, FileReader.FILE_OPENING_TYPE.ABSOLUTE, true);

		List<String> varParameterNames = Arrays.asList(config.getStringArray(EvaluationConstants.CONFIG_PARAM_VARIABLE_PARAMETER_NAMES));

		for (String varParameterName : varParameterNames) {

			boolean use = config.getBoolean(new StringBuilder(varParameterName).
				append(EvaluationConstants.CONFIG_PARAM_FRAGMENT_USE).toString());

			if (use) {

				VarParameter param = null;

				int type = config.getInt(new StringBuilder(varParameterName).
					append(EvaluationConstants.CONFIG_PARAM_FRAGMENT_PARAMETER_TYPE).toString());

				if (type == VarParameter.PARAMETER_TYPE_FLOAT ||
					type == VarParameter.PARAMETER_TYPE_INTEGER) {

					String startValue = config.getString(new StringBuilder(varParameterName).
						append(EvaluationConstants.CONFIG_PARAM_FRAGMENT_START_VALUE).toString());
					String endValue = config.getString(new StringBuilder(varParameterName).
						append(EvaluationConstants.CONFIG_PARAM_FRAGMENT_END_VALUE).toString());
					String step = config.getString(new StringBuilder(varParameterName).
						append(EvaluationConstants.CONFIG_PARAM_FRAGMENT_STEP).toString());

					param = new VarParameter(varParameterName, type, startValue, endValue, step);
				} else
				if (type == VarParameter.PARAMETER_TYPE_ENUM) {

					List<String> steps = Arrays.asList(config.getStringArray(new StringBuilder(varParameterName).
						append(EvaluationConstants.CONFIG_PARAM_FRAGMENT_STEPS).toString()));

					param = new VarParameter(varParameterName, type, steps);
				}

				if (param != null) {
					varParameter.add(param);
				} else {
					logger.error("type of '"+varParameterName+"' not supported, ignoring");
				}
			}
		}
		return varParameter;
	}

	/**
	 * Adds a new evaluator into the queue
	 * @param evaluator
	 */
	public void addEvaluator(AbstractEvaluator evaluator) {
		this.evaluatorList.add(evaluator);
	}

	/**
	 * Start the evaluation process by running all configured evaluators.
	 */
	public void run() {

		logger.info("Selected Evaluators: ");
		for (AbstractEvaluator evaluator : this.evaluatorList) {
			logger.info(evaluator.getClass().getName());
		}

		try {
			for (AbstractEvaluator evaluator : this.evaluatorList) {
					evaluator.evaluate().writeResultToXmlFile();
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

}

