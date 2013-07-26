package de.bbcdaas.synonymlexicon.cooccurrencematrix;

import de.bbcdaas.synonymlexicon.common.constants.SynLexConstants;
import de.bbcdaas.synonymlexicon.cooccurrencematrix.jobs.TermEntriesToIdEntriesJob;
import de.bbcdaas.synonymlexicon.cooccurrencematrix.jobs.TermIdEntriesToCooccurrenceJob;
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
 * Creates a cooccurrence matrix out of an input file containing tag clouds.
 * Uses a pre-created term lexicon containing termIDs and termCounts based on the
 * tag cloud inputs. Uses the termIDs instead of the term values for further processes.
 * @author Christian Herta
 */
public class CooccurrenceBuilder extends Configured implements Tool {

	private static Logger logger = Logger.getLogger(CooccurrenceBuilder.class);

	/**
	 * Starts the cooccurrence builder.
	 * @param args input/output
	 * @param conf
	 * @return errorCode
	 * @throws ConfigurationException
	 */
	public int go(String[] args, Configuration conf, int startJob, int stopJob, int singleJob) {

		int errorCode;
		int jobNumber = 8;
		String inputPath = args[0];
		String outputDirBaseName = args[1];
		String outputLocalDir;
		String inputRecordsCountFileName;
		
		try {
			
			outputLocalDir = ConfUtils.getStringValue(ConfUtils.KEY_OUTPUT_LOCAL_DIR_NAME, conf);
			inputRecordsCountFileName = ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf);
		} catch(ConfigurationException ex) {
			
			logger.error(ex.getMessage(), ex);
			return Job.FAILED;
		}
		
		logger.info("---------------------------- CooccurrenceBuilder: ------------------------------");

		try {

			logger.info("---------------- start Job 9 Convert TermEntries to IdEntries ------------------");
			
			String outEntriesVectorPath = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_9_VECTORENTITIES);

			logger.info("inputPath: "+inputPath);
			logger.info("outputPath: "+outEntriesVectorPath);

			jobNumber++;
			if (singleJob == jobNumber || singleJob == 0 && startJob <= jobNumber && (stopJob == 0 || stopJob >= jobNumber)) {

				// reads terms from an input file (same as in job 1), compares the terms of the tag cloud with
				// the terms in the map created by job 4 and extracts the ids of the terms, if already in this map.
				// Creates for each tag cloud a Mahout RandomAccessSparseVector and sets the weighting for each 
				// term to 1.0 . Writes the created vectors under the key 1.0 into a sequence file (key/entityVector).
				// writes the counter 'org.apache.hadoop.mapreduce.TaskCounter' into a text file
				errorCode = ToolRunner.run(new TermEntriesToIdEntriesJob(conf),
					new String[] {inputPath, outEntriesVectorPath});

				// copy result to locale file system
				DataAccessUtils.copyResultToLocalFileSystem(conf, outputDirBaseName,
					outputLocalDir, SynLexConstants.JOB_OUTPUT_DIR_9_VECTORENTITIES,
					inputRecordsCountFileName);

				if(errorCode != Job.SUCCESS) {
					return errorCode;
				}
			} else {
				logger.info("Skipped job 9.");
			}

			logger.info("---------------- done Job 9 Convert TermEntries to IdEntries -------------------");

			logger.info("------------- Job 10 Convert TermIdEntries to Cooccurrence Vector --------------");

			String outCooccurrenceVectorPath = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_10_COOCVECTORENTITIES);

			logger.info("inputPath: "+outEntriesVectorPath);
			logger.info("outputPath: "+outCooccurrenceVectorPath);

			jobNumber++;
			if (singleJob == jobNumber || singleJob == 0 && startJob <= jobNumber && (stopJob == 0 || stopJob >= jobNumber)) {

				// Read the vectors from job 6, iterated through them and writes all term/id pairs into a sequence file
				// into termID1/termID2 format. The reducer reduces doubled termID pairs and counts its occurrence.
				// for each term a termID/cooccurrenceVector is written.
				errorCode = ToolRunner.run(new TermIdEntriesToCooccurrenceJob(conf),
					new String[] {outEntriesVectorPath, outCooccurrenceVectorPath});

				// copy result to locale file system
				DataAccessUtils.copyResultToLocalFileSystem(conf, outputDirBaseName,
					outputLocalDir, SynLexConstants.JOB_OUTPUT_DIR_10_COOCVECTORENTITIES, null);

				if(errorCode != Job.SUCCESS) {
					return errorCode;
				}
			} else {
				logger.info("Skipped job 10.");
			}

			logger.info("------------ done Job 10 Convert TermIdEntries to Cooccurrence Vector ----------");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Job.FAILED;
		}

		logger.info("-------------------------- CooccurrenceBuilder done. ---------------------------");

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

		logger.info("Executing CooccurrenceBuilder...");
		logger.info("ErrorCode: "+ToolRunner.run(new CooccurrenceBuilder(), args));
	}
}
