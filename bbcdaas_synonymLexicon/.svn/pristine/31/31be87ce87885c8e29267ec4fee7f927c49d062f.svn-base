package de.bbcdaas.synonymlexicon;

import de.bbcdaas.synonymlexicon.common.constants.SynLexConstants;
import de.bbcdaas.synonymlexicon.cooccurrencematrix.CooccurrenceBuilder;
import de.bbcdaas.synonymlexicon.cosinesimilarity.CosineSimilarityBuilder;
import de.bbcdaas.synonymlexicon.loglikelihoodratio.LogLikelihoodRatioBuilder;
import de.bbcdaas.synonymlexicon.termlexicon.LexiconBuilder;
import de.bbcdaas.synonymlexicon.utils.hadoop.ConfUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.DataAccessUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.PathBuilder;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.jobcontrol.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;
/**
 * Hadoop implementation of the distributed tagHandler.
 * @author Christian Herta
 * @author Frithjof Schulte
 * @author Robert Illers
 */
public class SynonymLexiconBuilder extends Configured implements Tool {

	public static final int TOTAL_NUMBER_OF_JOBS = 16;
	private static Logger logger = Logger.getLogger(SynonymLexiconBuilder.class);
	private String inputPath = null;
	private String outputDirBaseName = null;
	private String outputLocalDir = null;
	private int startJob;
	private int stopJob;
	private int singleJob;
	private String tagDelimiter = null;
	private String entityToTagcloudDelimiter = null;
	private int minTermFrequency;
	private int minTermLength;
	private int maxTermLength;
	private boolean doubleTimeouts= false;

	/**
	 * Constructor
	 * @param startJob 
	 */
	public SynonymLexiconBuilder(int startJob, int stopJob, String tagDelimiter, 
		String entityToTagcloudDelimiter, int minTermFrequency, int singleJob,
		int minTermLength, int maxTermLength) {
		
		this.startJob = startJob;
		this.stopJob = stopJob;
		this.tagDelimiter = tagDelimiter;
		this.entityToTagcloudDelimiter = entityToTagcloudDelimiter;
		this.minTermFrequency = minTermFrequency;
		this.singleJob = singleJob;
		this.minTermLength = minTermLength;
		this.maxTermLength = maxTermLength;
	}
	
	/**
	 *
	 * @param args command line arguments
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		logger.info("Executing SynonymLexiconBuilder...");

		// get command line arguments
		Options options = new Options();
		options.addOption("inputPath", true, "inputPath (required)");
		options.addOption("help", false, "shows the help");
		options.addOption("startJob", true, "which job used to start pipeline (optional, default: 1)");
		options.addOption("stopJob", true, "which job should the pipeline stoped at (must be between startJob - TOTAL_NUMBER_OF_JOBS, default: TOTAL_NUMBER_OF_JOBS)");
		options.addOption("singleJob", true, "which job should the pipeline run only (if set, startJob/stopJob will be ignored, optional)");
		options.addOption(ConfUtils.KEY_TAG_DELIMITER, true, "tag delimiter (optional, default: ';' (don't forget the single quotes))");
		options.addOption(ConfUtils.KEY_ENTITY_TO_TAGCLOUD_DELIMITER, true, "entity to Tagcloud delimiter (optional, default: '::' (don't forget the single quotes))");
		options.addOption(ConfUtils.KEY_MIN_TERM_FREQUENCY, true, "min term frequency (Word Filter (Job 2))");
		options.addOption(ConfUtils.KEY_MIN_TERM_LENGTH, true, "min term length (Word Filter (Job 2))");
		options.addOption(ConfUtils.KEY_MAX_TERM_LENGTH, true, "max term length (Word Filter (Job 2))");
		CommandLineParser parser = new PosixParser();

		CommandLine cmd;
		int startJob = 1;
		int stopJob = TOTAL_NUMBER_OF_JOBS;
		int singleJob = 0;
		String tagDelimiter = null;
		String entityToTagcloudDelimiter = null;
		int minTermFrequency = 0;
		int minTermLength = 0;
		int maxTermLength = 0;
		
		try {
			cmd = parser.parse(options, args);
		} catch(ParseException ex) {
			logger.error(ex.getMessage(), ex);
			return;
		}

		// display the help
		if (cmd.hasOption("help")) {

			HelpFormatter helpFormatter = new HelpFormatter();
			helpFormatter.printHelp("hadoop jar [jarFileName] [options]", options);
		}

		// get tagcloud dir name in hdfs
		String inputPath;
		if(cmd.hasOption("inputPath")) {
			inputPath = cmd.getOptionValue("inputPath");
		} else {
			logger.error("Argument missing : inputPath");
			return;
		}
		
		if (cmd.hasOption("startJob")) {
			try {
				startJob = Integer.parseInt(cmd.getOptionValue("startJob"));
			} catch(NumberFormatException ex) {
				
				logger.error("parameter startJob: "+ex);
				return;
			}
		}
		
		if (cmd.hasOption("stopJob")) {
			try {
				stopJob = Integer.parseInt(cmd.getOptionValue("stopJob"));
			} catch(NumberFormatException ex) {
				
				logger.error("parameter stopJob: "+ex);
				return;
			}
		}
		
		if (cmd.hasOption("singleJob")) {
			
			try {
				singleJob = Integer.parseInt(cmd.getOptionValue("singleJob"));
			} catch(NumberFormatException ex) {
				
				logger.error("parameter singleJob: "+ex);
				return;
			}
		}
		
		if (cmd.hasOption(ConfUtils.KEY_TAG_DELIMITER)) {
			tagDelimiter = cmd.getOptionValue(ConfUtils.KEY_TAG_DELIMITER);
		}
		
		if (cmd.hasOption(ConfUtils.KEY_ENTITY_TO_TAGCLOUD_DELIMITER)) {
			entityToTagcloudDelimiter = cmd.getOptionValue(ConfUtils.KEY_ENTITY_TO_TAGCLOUD_DELIMITER);
		}
		
		if (cmd.hasOption(ConfUtils.KEY_MIN_TERM_FREQUENCY)) {
			try {
				minTermFrequency = Integer.parseInt(cmd.getOptionValue(ConfUtils.KEY_MIN_TERM_FREQUENCY));
			} catch(NumberFormatException ex) {
				
				logger.error("parameter minTermFrequency: "+ex);
				return;
			}
		}
		
		if (cmd.hasOption(ConfUtils.KEY_MIN_TERM_LENGTH)) {
			try {
				minTermLength = Integer.parseInt(cmd.getOptionValue(ConfUtils.KEY_MIN_TERM_LENGTH));
			} catch(NumberFormatException ex) {
				
				logger.error("parameter minTermLength: "+ex);
				return;
			}
		}

		if (cmd.hasOption(ConfUtils.KEY_MAX_TERM_LENGTH)) {
			try {
				maxTermLength = Integer.parseInt(cmd.getOptionValue(ConfUtils.KEY_MAX_TERM_LENGTH));
			} catch(NumberFormatException ex) {
				
				logger.error("parameter maxTermLength: "+ex);
				return;
			}
		}
		
		int errorCode = ToolRunner.run(new SynonymLexiconBuilder(startJob, stopJob,
			tagDelimiter, entityToTagcloudDelimiter, minTermFrequency, singleJob,
			minTermLength, maxTermLength), new String[]{inputPath, ""});

		if (errorCode == Job.SUCCESS) {
			logger.info("Done executing SynonymLexiconBuilder.");
		} else {
			logger.error("Execution of SynonymLexiconBuilder aborted, errorCode: "+errorCode);
		}
	}
	
	/**
	 * Starts the distributed taghandler.
	 * @param args command line parameters
	 * @return errorCode
	 * @throws Exception
	 */

	@Override
	public int run(String[] args) {

		int errorCode;
		this.inputPath = args[0];
		
		// get configuration
		if (!this.getConfiguration()) {
			return Job.FAILED;
		}
		
		if (this.tagDelimiter != null) {
			this.getConf().set(ConfUtils.KEY_TAG_DELIMITER, this.tagDelimiter);
		}
		
		if (this.entityToTagcloudDelimiter != null) {
			this.getConf().set(ConfUtils.KEY_ENTITY_TO_TAGCLOUD_DELIMITER, this.entityToTagcloudDelimiter);
		}
		
		if (this.minTermFrequency != 0) {
			this.getConf().setInt(ConfUtils.KEY_MIN_TERM_FREQUENCY, this.minTermFrequency);
		}
		
		if (this.minTermLength != 0) {
			this.getConf().setInt("minTermLength", this.minTermLength);
		}
		
		if (this.maxTermLength != 0) {
			this.getConf().setInt("maxTermLength", this.maxTermLength);
		}

		// print configuration
		ConfUtils.printConfiguration(this.getConf());
		logger.info("startJob = "+this.startJob);
		logger.info("stopJob = "+this.stopJob);
		
		// remove old result data
		this.cleanupOldDataOutputs();
		
		// tagCloud dir in hdfs, working directory in hdfs
		String [] dirNames = {this.inputPath, this.outputDirBaseName};

		// Job 1-8: execute Lexicon builder
		if((errorCode = new LexiconBuilder().go(dirNames, this.getConf(), this.startJob, this.stopJob, this.singleJob)) != Job.SUCCESS) {
			return errorCode;
		}

		// Job 9-10: execute Cooccurrence builder
		if((errorCode = new CooccurrenceBuilder().go(dirNames, this.getConf(), this.startJob, this.stopJob, this.singleJob)) != Job.SUCCESS) {
			return errorCode;
		}

		// Job 11-12: execute Log likelihood ratio calculation
		if((errorCode = new LogLikelihoodRatioBuilder().go(dirNames, this.getConf(), this.startJob, this.stopJob, this.singleJob)) != Job.SUCCESS) {
			return errorCode;
		}

		// Job 13-16: execute Cosine similarity builder
		if((errorCode = new CosineSimilarityBuilder().go(dirNames, this.getConf(), this.startJob, this.stopJob, this.singleJob)) != Job.SUCCESS) {
			return errorCode;
		}
		return errorCode;
	}
	
	/**
	 * Gets the parameter from the arguments and configurations.
	 * @param args arguments
	 */
	private boolean getConfiguration() {
		
		this.getConf().set("mapred.used.genericoptionsparser", "true");
		// double default timeouts to give slow htw servers a chance...
		if (this.doubleTimeouts) {
			this.getConf().setInt("dfs.ha.fencing.ssh.connect-timeout", 60000); // default: 30000
			this.getConf().setInt("ha.failover-controller.cli-check.rpc-timeout.ms", 40000); // default: 20000
			this.getConf().setInt("ha.failover-controller.graceful-fence.rpc-timeout.ms", 10000); // default: 5000
			this.getConf().setInt("ha.failover-controller.new-active.rpc-timeout.ms", 120000); //default: 60000
			this.getConf().setInt(" ha.health-monitor.rpc-timeout.ms", 90000); // default: 45000
			this.getConf().setInt(" ha.zookeeper.session-timeout.ms", 10000); // default: 5000
			this.getConf().setInt("ipc.client.connect.max.retries.on.timeouts", 90); // default: 45
		}
		this.getConf().addResource(SynLexConstants.DISTRIBUTED_TAGHANDLER_CONFIG_FILENAME);
		
		try {
			
			this.outputDirBaseName = ConfUtils.getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, this.getConf());
			this.outputLocalDir = ConfUtils.getStringValue(ConfUtils.KEY_OUTPUT_LOCAL_DIR_NAME, this.getConf());
		} catch(ConfigurationException ex) {
			logger.error(ex.getMessage(), ex);
			return false;
		}		
		return true;
	}
	
	/**
	 * Remove old result data (job 1 - TOTAL_NUMBER_OF_JOBS)
	 */
	private void cleanupOldDataOutputs() {
	
		if (this.singleJob == 0 && (this.startJob == 0 || this.startJob == 1)) {
			
			DataAccessUtils.deletePathFromLocal(this.getConf(), new Path(outputLocalDir));
			logger.info("Deleted folder '"+outputLocalDir+"' from local file system.");
			DataAccessUtils.deletePathFromHDFS(this.getConf(), new Path(outputDirBaseName));
			logger.info("Deleted folder '"+this.outputDirBaseName+"' from hdfs file system.");
		} else {
			
			if (this.singleJob == 1) {
				this.deleteJobFolder(SynLexConstants.JOB_OUTPUT_DIR_1_WORDCOUNT);
			}
			
			if (this.singleJob == 0 && this.startJob <= 16 || this.singleJob == 16) {
				this.deleteJobFolder(SynLexConstants.JOB_OUTPUT_DIR_16_COSINESIMILARITY_TEXTOUTPUT);
			}
			
			if (this.singleJob == 0 && this.startJob <= 15 || this.singleJob == 15) {
				this.deleteJobFolder(SynLexConstants.JOB_OUTPUT_DIR_15_COSSIMILARITY);
			}
			
			if (this.singleJob == 0 && this.startJob <= 14 || this.singleJob == 14) {
				this.deleteJobFolder(SynLexConstants.JOB_OUTPUT_DIR_14_TERMIDPAIRS);
			}
			
			if (this.singleJob == 0 && this.startJob <= 13 || this.singleJob == 13) {
				this.deleteJobFolder(SynLexConstants.JOB_OUTPUT_DIR_13_TERMVECTORNORMS);
			}
			
			if (this.singleJob == 0 && this.startJob <= 12 || this.singleJob == 12) {
				this.deleteJobFolder(SynLexConstants.JOB_OUTPUT_DIR_12_LOGLIKELIHOODRATIO_TEXTOUTPUT);
			}
			
			if (this.singleJob == 0 && this.startJob <= 11 || this.singleJob == 11) {
				this.deleteJobFolder(SynLexConstants.JOB_OUTPUT_DIR_11_LLRVECTORENTITIES);
			}
			
			if (this.singleJob == 0 && this.startJob <= 10 || this.singleJob == 10) {
				this.deleteJobFolder(SynLexConstants.JOB_OUTPUT_DIR_10_COOCVECTORENTITIES);
			}
			
			if (this.singleJob == 0 && this.startJob <= 9 || this.singleJob == 9) {
				this.deleteJobFolder(SynLexConstants.JOB_OUTPUT_DIR_9_VECTORENTITIES);
			}
			
			if (this.singleJob == 0 && this.startJob <= 8 || this.singleJob == 8) {
				
				this.deleteJobFolder(SynLexConstants.JOB_OUTPUT_DIR_8_COUNTTERM_TEXTOUTPUT);
				this.deleteJobFolder(SynLexConstants.JOB_OUTPUT_DIR_8_1_IDTERM_TEXTOUTPUT);
			}
			
			if (this.singleJob == 0 && this.startJob <= 7 || this.singleJob == 7) {
				this.deleteJobFolder(SynLexConstants.JOB_OUTPUT_DIR_7_TERMCOUNT_TEXTOUTPUT);
			}
			
			if (this.singleJob == 0 && this.startJob <= 6 || this.singleJob == 6) {
				this.deleteJobFolder(SynLexConstants.JOB_OUTPUT_DIR_6_TERMSORT);
			}
			
			if (this.singleJob == 0 && this.startJob <= 5 || this.singleJob == 5) {
				this.deleteJobFolder(SynLexConstants.JOB_OUTPUT_DIR_5_TERM2IDLEXICON);
			}
			
			if (this.singleJob == 0 && this.startJob <= 4 || this.singleJob == 4) {
				this.deleteJobFolder(SynLexConstants.JOB_OUTPUT_DIR_4_ID2TERMLEXICON);
			}
			
			if (this.singleJob == 0 && this.startJob <= 3 || this.singleJob == 3) {
				this.deleteJobFolder(SynLexConstants.JOB_OUTPUT_DIR_3_COUNTSORT);
			}
			
			if (this.singleJob == 0 && this.startJob <= 2 || this.singleJob == 2) {
				this.deleteJobFolder(SynLexConstants.JOB_OUTPUT_DIR_2_WORDFILTER);
			}
		}
	}
	
	/**
	 * 
	 * @param jobOutputDir 
	 */
	private void deleteJobFolder(String jobOutputDir) {

		DataAccessUtils.deletePathFromLocal(this.getConf(), PathBuilder.
			buildAsPath(outputLocalDir, jobOutputDir));
		logger.info("Deleted folder '"+outputLocalDir+"/"+jobOutputDir+"' from local file system.");

		DataAccessUtils.deletePathFromHDFS(this.getConf(), PathBuilder.
			buildAsPath(outputDirBaseName, jobOutputDir));
		logger.info("Deleted folder '"+outputDirBaseName+"/"+jobOutputDir+"' from hdfs file system.");
	}
}
