package de.bbcdaas.synonymlexicon.loglikelihoodratio;

import de.bbcdaas.synonymlexicon.common.constants.SynLexConstants;
import de.bbcdaas.synonymlexicon.loglikelihoodratio.jobs.LogLikelihoodRatioJob;
import de.bbcdaas.synonymlexicon.loglikelihoodratio.jobs.LogLikelihoodTextOutputJob;
import de.bbcdaas.synonymlexicon.utils.hadoop.ConfUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.DataAccessUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.PathBuilder;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapred.jobcontrol.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;
/**
 * Calculates the llr weightings and writes a human readable output.
 * @author Frithjof Schulte
 * @author Robert Illers
 */
public class LogLikelihoodRatioBuilder extends Configured implements Tool {

	private final static Logger logger = Logger.getLogger(LogLikelihoodRatioBuilder.class);
	
	/**
	 * 
	 * @param args
	 * @param conf
	 * @return errorCode
	 * @throws ConfigurationException
	 */
	public int go(String[] args, Configuration conf, int startJob, int stopJob, int singleJob) {

		int errorCode;
		int jobNumber = 10;
		String outputDirBaseName = args[1];
		
		String outputLocalDir;
		try {
		 outputLocalDir = ConfUtils.getStringValue(ConfUtils.KEY_OUTPUT_LOCAL_DIR_NAME, conf);
		} catch(ConfigurationException ex) {
			
			logger.error(ex.getMessage(), ex);
			return Job.FAILED;
		}

		logger.info("--------------------------- LogLikelihood Ratio Builder: -----------------------");

		try {

			logger.info("----------------------- Job 11 Calculate Log Likelihood Ratio -------------------");

			// get the output folder with the vector representation of the cooccurrence matrix
			String inputVectorDir = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_10_COOCVECTORENTITIES);

			String outputLlrVectorPath = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_11_LLRVECTORENTITIES);

			logger.info("inputPath: "+inputVectorDir);
			logger.info("outputPath: "+outputLlrVectorPath);

			jobNumber++;
			if (singleJob == jobNumber || singleJob == 0 && startJob <= jobNumber && (stopJob == 0 || stopJob >= jobNumber)) {

				// reads the termID/cooccurrence map from job 10 and calculates for each term pair the loglikelihood
				// weighting. Stores the result into a termID/llrTermWeightingVector map into a sequence file.
				errorCode = ToolRunner.run(new LogLikelihoodRatioJob(conf),
					new String[] {inputVectorDir, outputLlrVectorPath});

				// copy result to locale file system
				DataAccessUtils.copyResultToLocalFileSystem(conf, outputDirBaseName,
					outputLocalDir, SynLexConstants.JOB_OUTPUT_DIR_11_LLRVECTORENTITIES, null);
				
				if (errorCode != Job.SUCCESS) {
					return errorCode;
				}
			} else {
				logger.info("Skipped job 11.");
			}

			logger.info("---------------------- done Job 11 Calculate Log Likelihood Ratio ---------------");

			logger.info("--------------------------- Job 12 Create LLR Text Output -----------------------");

			String outputLlrTextOutputPath = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_12_LOGLIKELIHOODRATIO_TEXTOUTPUT);

			logger.info("inputPath: "+outputLlrVectorPath);
			logger.info("outputPath: "+outputLlrTextOutputPath);

			jobNumber++;
			if (singleJob == jobNumber || singleJob == 0 && startJob <= jobNumber && (stopJob == 0 || stopJob >= jobNumber)) {
				
				// reads the termID/llrTermWeightingVector map from job 8 and
				// writes it as termID/llrTermWeightingVectorAsString ()
				// (Format: '{termName: llrWeighting, termName: llrWeighting, ...}')
				errorCode = ToolRunner.run(new LogLikelihoodTextOutputJob(conf),
					new String[] {outputLlrVectorPath, outputLlrTextOutputPath});

				// copy result to locale file system
				DataAccessUtils.copyResultToLocalFileSystem(conf, outputDirBaseName,
					outputLocalDir, SynLexConstants.JOB_OUTPUT_DIR_12_LOGLIKELIHOODRATIO_TEXTOUTPUT, null);
				
				if (errorCode != Job.SUCCESS) {
					return errorCode;
				}
			} else {
				logger.info("Skipped job 12.");
			}

			logger.info("------------------------ done Job 12 Create LLR Text Output ---------------------");

		} catch(Exception e) {

			logger.error(e.getMessage(), e);
			return Job.FAILED;
		}

		logger.info("----------------------- LogLikelihood Ratio Builder done. ----------------------");

		return Job.SUCCESS;
	}

	/**
	 *
	 * @param args
	 * @return
	 * @throws Exception
	 */
	@Override
	public int run(String[] args) throws Exception {

		Configuration conf = getConf();
		return go(args, conf, 0, 0, 0);
	}

	/**
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		logger.info("Executing LogLikelihoodRatioBuilder...");
		logger.info("ErrorCode: "+ToolRunner.run(new LogLikelihoodRatioBuilder(), args));
	}
}
