package de.bbcdaas.common.util;

import static de.bbcdaas.common.util.LineRemover.logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

/**
 *
 * @author Robert Illers
 */
public class LineRemover {
	
	public static final Logger logger = Logger.getLogger(LineRemover.class);
	
	private static void performRemove(FileReader fileReader, FileWriter fileWriter, Params params) {
		
		String line;
		int lineNumber = 1;
		int linesRemoved = 0;
		int linesKept = 0;
		int currentLinesRemoved = 0;
		int currentLinesKept = 0;
		boolean keep;
		
		while ((line = fileReader.readln()) != null) {
			
			keep = true;
			
			/*--- rules ---*/ 
			
			// reached line at end position
			if (params.endPosition != null && lineNumber > params.endPosition) {
				break;
			}
			
			// keep distance reached
			if (currentLinesKept == params.keepDistance) {
				
				keep = false;
				currentLinesKept = 0;
			}
			
			// remove distance reached
			if (currentLinesRemoved == params.removeDistance) {
				
				keep = true;
				currentLinesRemoved = 0;
			}
			
			// remove limit reached
			if (params.removeLimit != null && params.removeLimit <= linesRemoved) {
				keep = true;
			}
			
			// start position not reached yet
			if (params.startPosition != null && lineNumber < params.startPosition) {
				keep = false;
			}
			
			/*--- /rules ---*/
			
			if (keep) {
				
				fileWriter.println(line);
				linesKept++;
				currentLinesKept++;
			} else {
				
				linesRemoved++;
				currentLinesRemoved++;
			}
			lineNumber++;
		}
		logger.info("Lines removed: "+linesRemoved);
		logger.info("Lines written: "+linesKept);
	}
	
	private static void printParams(Params params) {
		
		logger.info("Parameters:");
		logger.info("inputFile: "+params.inputFile);
		logger.info("outputFile: "+params.outputFile);
		logger.info("keepDistance: "+params.keepDistance);
		logger.info("removeDistance: "+params.removeDistance);
		logger.info("startPosition: "+params.startPosition);
		logger.info("endPosition: "+params.endPosition);
		logger.info("removeLimit: "+params.removeLimit);
	}
	
	public static void main(String[] args) {
		
		FileReader fileReader = null;
		FileWriter fileWriter = null;
		boolean ready = false;
		Params params = readParams(args);
		
		if (params != null) {
		
			printParams(params);
			fileReader = new FileReader();
			
			if (fileReader.openFile(params.inputFile)) {
				
				fileWriter = new FileWriter();
				ready = fileWriter.openFile(params.outputFile, false);
			}
		}
		
		if (ready) {	
			performRemove(fileReader, fileWriter, params);
		}
		
		if (fileReader != null) {
			fileReader.closeFile();
		}
		if (fileWriter != null) {
			fileWriter.closeFile();
		}
	}
	
	private static Params readParams(String[] args) {
		
		Params params = new Params();
		Options options = new Options();
		options.addOption(new Option("help", false, "Shows the help"));
		options.addOption(new Option("inputFile", true, "Input file with the lines (required)"));
		options.addOption(new Option("outputFile", true, "Output file where the result should be written to, file should not already exist (required)"));
		options.addOption(new Option("keepDistance", true, "How many lines should be kept between to remove operations (required)"));
		options.addOption(new Option("removeDistance", true, "How many lines should be removed (required)"));
		options.addOption(new Option("startPosition", true, "Start position, the line where the removing will begin (optional)"));
		options.addOption(new Option("endPosition", true, "End position, the line where the removing will stop (optional)"));
		options.addOption(new Option("removeLimit", true, "How often should the remove be done to more than (optional)"));
		CommandLineParser commandLineParser = new PosixParser();
		HelpFormatter helpFormatter = new HelpFormatter();
		CommandLine commandLine = null;
		
		try {
			commandLine = commandLineParser.parse(options, args);
		} catch(ParseException ex) {
			logger.error(ex);
		}
		
		if (commandLine != null) {
			
			if (commandLine.hasOption("help")) {
				
				helpFormatter.printHelp("java -jar [jarfileName] [parameters]", options);
				return null;
			} 
			
			// required params:

			if (commandLine.hasOption("inputFile")) {

				params.inputFile = commandLine.getOptionValue("inputFile");
			} else {
				logger.error("Parameter missing: inputFile (use -help for more info)");
				return null;
			}

			if (commandLine.hasOption("outputFile")) {

				params.outputFile = commandLine.getOptionValue("outputFile");
			} else {
				logger.error("Parameter missing: outputFile (use -help for more info)");
				return null;
			}

			if (commandLine.hasOption("keepDistance")) {

				try {
					params.keepDistance = Integer.parseInt(commandLine.getOptionValue("keepDistance"));
				} catch(NumberFormatException ex) {

					logger.error("keepDistance Error: ", ex);
					return null;
				}
			} else {
				logger.error("Parameter missing: keepDistance (use -help for more info)");
				return null;
			}

			if (commandLine.hasOption("removeDistance")) {

				try {
					params.removeDistance = Integer.parseInt(commandLine.getOptionValue("removeDistance"));
				} catch(NumberFormatException ex) {

					logger.error("removeDistance Error: ", ex);
					return null;
				}
			} else {
				logger.error("Parameter missing: removeDistance (use -help for more info)");
				return null;
			}

			// optional params:

			if (commandLine.hasOption("startPosition")) {

				try {
					params.startPosition = Integer.parseInt(commandLine.getOptionValue("startPosition"));
				} catch(NumberFormatException ex) {

					logger.error("startPosition Error: ", ex);
					return null;
				}
			}

			if (commandLine.hasOption("endPosition")) {

				try {
					params.endPosition = Integer.parseInt(commandLine.getOptionValue("endPosition"));
				} catch(NumberFormatException ex) {

					logger.error("endPosition Error: ", ex);
					return null;
				}
			}

			if (commandLine.hasOption("removeLimit")) {

				try {
					params.removeLimit = Integer.parseInt(commandLine.getOptionValue("removeLimit"));
				} catch(NumberFormatException ex) {

					logger.error("removeLimit Error: ", ex);
					return null;
				}
			}
		}
		return params;
	}
	
	private static class Params {
		
		public String inputFile = null;
		public String outputFile = null;
		public Integer keepDistance = null;
		public Integer removeDistance = null;
		public Integer startPosition = null;
		public Integer endPosition = null;
		public Integer removeLimit = null;
	}
}
