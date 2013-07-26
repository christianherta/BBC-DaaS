package de.bbcdaas.synonymlexicon.termlexicon;

import de.bbcdaas.synonymlexicon.common.beans.IdCountPair;
import de.bbcdaas.synonymlexicon.common.jobs.ConfigurableSortJob;
import de.bbcdaas.synonymlexicon.common.constants.SynLexConstants;
import de.bbcdaas.synonymlexicon.termlexicon.jobs.WordCountJob;
import de.bbcdaas.synonymlexicon.termlexicon.jobs.IdTermLexiconWriteJob;
import de.bbcdaas.synonymlexicon.termlexicon.jobs.CountTermTextOutputJob;
import de.bbcdaas.synonymlexicon.termlexicon.jobs.IdTermTextOutputJob;
import de.bbcdaas.synonymlexicon.termlexicon.jobs.TermCountTextOutputJob;
import de.bbcdaas.synonymlexicon.termlexicon.jobs.TermIdLexiconWriteJob;
import de.bbcdaas.synonymlexicon.termlexicon.jobs.WordFilterJob;
import de.bbcdaas.synonymlexicon.utils.hadoop.ConfUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.DataAccessUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.PathBuilder;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.jobcontrol.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;
/**
 * Reads tag clouds from input files and stores its terms into a term lexicon.
 * Creates a human readable output.
 * @author Christian Herta
 * @author Robert Illers
 */
public class LexiconBuilder extends Configured implements Tool {

	private static Logger logger = Logger.getLogger(LexiconBuilder.class);

	/**
	 *
	 * @param args
	 * @param conf
	 * @return
	 * @throws ConfigurationException
	 */
	public int go(String[] args, Configuration conf, int startJob, int stopJob, int singleJob) {

		int errorCode;
		int jobNumber = 0;
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

		try {
			logger.info("------------------------------ Lexicon Builder: ------------------------------");

			logger.info("------------------------ start Job 1 Word Count ------------------------------");

			String wordCountOutputPath = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_1_WORDCOUNT);

			logger.info("InputPath: "+inputPath);
			logger.info("OutputPath: "+wordCountOutputPath);

			jobNumber++;
			if (singleJob == jobNumber || singleJob == 0 && startJob <= jobNumber && (stopJob == 0 || stopJob >= jobNumber)) {

				// mapper reads terms from an input file (CSV) and counts how often terms occur
				// reducer creates from the mappers term/count maps a single count/term map 
				errorCode = ToolRunner.run(new WordCountJob(conf),
					new String[]{inputPath, wordCountOutputPath});

				// copy result to locale file system
				DataAccessUtils.copyResultToLocalFileSystem(conf, outputDirBaseName,
					outputLocalDir, SynLexConstants.JOB_OUTPUT_DIR_1_WORDCOUNT,
					inputRecordsCountFileName);

				if(errorCode != Job.SUCCESS) {
					return errorCode;
				}
			} else {
				logger.info("Skipped job 1.");
			}

			logger.info("------------------------- done Job 1 Word Count ------------------------------");

			logger.info("----------------------- start Job 2 Word Filter ------------------------------");
			
			String wordFilterOutputPath = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_2_WORDFILTER);
			
			logger.info("InputPath: "+wordCountOutputPath);
			logger.info("OutputPath: "+wordFilterOutputPath);
			
			jobNumber++;
			if (singleJob == jobNumber || singleJob == 0 && startJob <= jobNumber && (stopJob == 0 || stopJob >= jobNumber)) {
			
				// filters words from the term lexicon
				errorCode = ToolRunner.run(new WordFilterJob(conf),
					new String[]{wordCountOutputPath, wordFilterOutputPath});
				
				// copy result to locale file system
				DataAccessUtils.copyResultToLocalFileSystem(conf, outputDirBaseName,
					outputLocalDir, SynLexConstants.JOB_OUTPUT_DIR_2_WORDFILTER,
					inputRecordsCountFileName);
				
				if(errorCode != Job.SUCCESS) {
					return errorCode;
				}
				
			} else {
				logger.info("Skipped job 2.");
			}
			
			logger.info("------------------------ done Job 2 Word Filter ------------------------------");
			
			logger.info("------------------------ start Job 3 Count Sort ------------------------------");

			String countSortOutputPath = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_3_COUNTSORT);

			logger.info("InputPath: "+wordFilterOutputPath);
			logger.info("OutputPath: "+countSortOutputPath);

			jobNumber++;
			if (singleJob == jobNumber || singleJob == 0 && startJob <= jobNumber && (stopJob == 0 || stopJob >= jobNumber)) {
				
				// sorts the count/term map by count in descending order		
				ConfigurableSortJob sorterJob =  new ConfigurableSortJob(conf);
				sorterJob.setOutputKeyClass(LongWritable.class);
				sorterJob.setOutputValueClass(Text.class);
				sorterJob.setSortComparatorClass(LongWritable.DecreasingComparator.class);
				sorterJob.setInputRecordsCountFileData(inputRecordsCountFileName, 
					SynLexConstants.JOB_OUTPUT_DIR_3_COUNTSORT);
		
				errorCode = ToolRunner.run(sorterJob, new String[] {wordFilterOutputPath, countSortOutputPath});

				// copy result to locale file system
				DataAccessUtils.copyResultToLocalFileSystem(conf, outputDirBaseName,
					outputLocalDir, SynLexConstants.JOB_OUTPUT_DIR_3_COUNTSORT, 
					inputRecordsCountFileName);

				if(errorCode != Job.SUCCESS) {
					return errorCode;
				}
			} else {
				logger.info("Skipped job 3.");
			}

			logger.info("------------------------- done Job 3 Count Sort ------------------------------");

			logger.info("---------------------- start Job 4 Write ID Term Lexicon ---------------------");

			String id2TermLexiconOutputPath =  PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_4_ID2TERMLEXICON);
			
			logger.info("InputPath: "+countSortOutputPath);
			logger.info("OutputPath: "+id2TermLexiconOutputPath);
 
			jobNumber++;
			if (singleJob == jobNumber || singleJob == 0 && startJob <= jobNumber && (stopJob == 0 || stopJob >= jobNumber)) {

				// converts the sorted count/term map from sequence file to map file
				// uses the parameter 'minTermFrequency' for ignoring terms with count < minTermFrequency
				// creates for each term an ID (from 0 ascending) and stores them in a id/TermCountPair map
				errorCode = ToolRunner.run(new IdTermLexiconWriteJob(conf),
					new String[]{countSortOutputPath, id2TermLexiconOutputPath});

				// copy result to locale file system
				DataAccessUtils.copyResultToLocalFileSystem(conf, outputDirBaseName,
					outputLocalDir, SynLexConstants.JOB_OUTPUT_DIR_4_ID2TERMLEXICON,
					inputRecordsCountFileName);

				if(errorCode != Job.SUCCESS) {
					return errorCode;
				}
			} else {
				logger.info("Skipped job 4.");
			}

			logger.info("----------------------- done Job 4 Write ID Term Lexicon ---------------------");

			logger.info("-------------------- start Job 5 Write Term to ID Lexicon --------------------");

			String term2IdLexiconOutputPath = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_5_TERM2IDLEXICON);

			logger.info("InputPath: "+id2TermLexiconOutputPath);
			logger.info("OutputPath: "+term2IdLexiconOutputPath);

			jobNumber++;
			if (singleJob == jobNumber || singleJob == 0 && startJob <= jobNumber && (stopJob == 0 || stopJob >= jobNumber)) {
				
				// read the map from job 4 and creates a term/IdCountPair map in a sequence file
				errorCode = ToolRunner.run(new TermIdLexiconWriteJob(conf),
					new String[]{id2TermLexiconOutputPath, term2IdLexiconOutputPath});

				// copy result to locale file system
				DataAccessUtils.copyResultToLocalFileSystem(conf, outputDirBaseName,
					outputLocalDir, SynLexConstants.JOB_OUTPUT_DIR_5_TERM2IDLEXICON,
					inputRecordsCountFileName);

				if(errorCode != Job.SUCCESS) {
					return errorCode;
				}
			} else {
				logger.info("Skipped job 4.");
			}

			logger.info("-------------------- done Job 5 Write Term to ID Lexicon ----------------------");

			logger.info("------------------------- start Job 6 term sort -------------------------------");

			String termSortOutputPath = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_6_TERMSORT);

			logger.info("InputPath: "+term2IdLexiconOutputPath);
			logger.info("OutputPath: "+termSortOutputPath);

			jobNumber++;
			if (singleJob == jobNumber || singleJob == 0 && startJob <= jobNumber && (stopJob == 0 || stopJob >= jobNumber)) {
			
				// sorts the term lexicon by the term names
				ConfigurableSortJob sorterJob =  new ConfigurableSortJob(conf);
				sorterJob.setOutputKeyClass(Text.class);
				sorterJob.setOutputValueClass(IdCountPair.class);
				sorterJob.setInputRecordsCountFileData(inputRecordsCountFileName, 
					SynLexConstants.JOB_OUTPUT_DIR_6_TERMSORT);
			
				errorCode = ToolRunner.run(sorterJob,
					new String[]{term2IdLexiconOutputPath, termSortOutputPath});

				// copy result to locale file system
				DataAccessUtils.copyResultToLocalFileSystem(conf, outputDirBaseName,
					outputLocalDir, SynLexConstants.JOB_OUTPUT_DIR_6_TERMSORT, 
					inputRecordsCountFileName);

				if(errorCode != Job.SUCCESS) {
					return errorCode;
				}
			} else {
				logger.info("Skipped job 6.");
			}

			logger.info("------------------------- done Job 6 term sort ---------------------------------");

			logger.info("------------------- start Job 7 term count text output -------------------------");
			
			String termCountTextOutputPath = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_7_TERMCOUNT_TEXTOUTPUT);
			
			logger.info("InputPath: "+termSortOutputPath);
			logger.info("OutputPath: "+termCountTextOutputPath);
			
			jobNumber++;
			if (singleJob == jobNumber || singleJob == 0 && startJob <= jobNumber && (stopJob == 0 || stopJob >= jobNumber)) {
				
				// write a human readable term lexicon text output into a file
				// format: term/count (sorted by name)
				errorCode = ToolRunner.run(new TermCountTextOutputJob(conf),
					new String[]{termSortOutputPath, termCountTextOutputPath});
			
				// copy result to locale file system
				DataAccessUtils.copyResultToLocalFileSystem(conf, outputDirBaseName,
					outputLocalDir, SynLexConstants.JOB_OUTPUT_DIR_7_TERMCOUNT_TEXTOUTPUT, 
					inputRecordsCountFileName);
				
			if(errorCode != Job.SUCCESS) {
					return errorCode;
				}
			} else {
				logger.info("Skipped job 7.");
			}
				
			logger.info("--------------------- done Job 7 term count text output ------------------------");
			
			logger.info("------------------- start Job 8 count term text output -------------------------");
			
			String countTermTextOutputPath = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_8_COUNTTERM_TEXTOUTPUT);
			
			logger.info("InputPath: "+countSortOutputPath);
			logger.info("OutputPath: "+countTermTextOutputPath);
			
			jobNumber++;
			if (singleJob == jobNumber || singleJob == 0 && startJob <= jobNumber && (stopJob == 0 || stopJob >= jobNumber)) {
				
				// write a human readable term lexicon text output into a file
				// format: count/term (sorted by count)
				errorCode = ToolRunner.run(new CountTermTextOutputJob(conf),
					new String[]{countSortOutputPath, countTermTextOutputPath});
			
				// copy result to locale file system
				DataAccessUtils.copyResultToLocalFileSystem(conf, outputDirBaseName,
					outputLocalDir, SynLexConstants.JOB_OUTPUT_DIR_8_COUNTTERM_TEXTOUTPUT, 
					inputRecordsCountFileName);
				
				if (errorCode != Job.SUCCESS) {
						return errorCode;
				}
				
			} else {
				logger.info("Skipped job 8.");
			}
				
			logger.info("-------------------- done Job 8 count term text output ------------------------");
			
			logger.info("---------------------- Job 8.1 id term text output ----------------------------");
			
			String idTermTextOutputPath = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_8_1_IDTERM_TEXTOUTPUT);
			
			logger.info("InputPath: "+id2TermLexiconOutputPath);
			logger.info("OutputPath: "+idTermTextOutputPath);
			
			if (singleJob == jobNumber || singleJob == 0 && startJob <= jobNumber && (stopJob == 0 || stopJob >= jobNumber)) {
				
				// for debugging
				errorCode = ToolRunner.run(new IdTermTextOutputJob(conf),
					new String[]{id2TermLexiconOutputPath, idTermTextOutputPath});
				
				// copy result to locale file system
				DataAccessUtils.copyResultToLocalFileSystem(conf, outputDirBaseName,
					outputLocalDir, SynLexConstants.JOB_OUTPUT_DIR_8_1_IDTERM_TEXTOUTPUT, 
					null);
				
				if (errorCode != Job.SUCCESS) {
						return errorCode;
				}
			} else {
				logger.info("Skipped job 8.1 .");
			}
			
			logger.info("-------------------- done Job 8.1 id term text output -------------------------");
			
			logger.info("--------------------------- Lexicon Builder done. -----------------------------");
		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			return Job.FAILED;
		}
		return Job.SUCCESS;
	}

	/**
	 *
	 * @param args
	 * @return errorCode
	 * @throws Exception
	 */
	@Override
	public int run(String[] args) throws Exception {
		return go(args, getConf(), 0, 0, 0);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		logger.info("Executing LexiconBuilder...");
		logger.info("ErrorCode: "+ToolRunner.run(new LexiconBuilder(), args));
	}
}
