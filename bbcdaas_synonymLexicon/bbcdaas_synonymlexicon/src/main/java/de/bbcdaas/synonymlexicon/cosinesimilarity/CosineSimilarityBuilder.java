package de.bbcdaas.synonymlexicon.cosinesimilarity;

import de.bbcdaas.synonymlexicon.common.beans.TermCountPair;
import de.bbcdaas.synonymlexicon.common.jobs.ConfigurableSortJob;
import de.bbcdaas.synonymlexicon.common.constants.SynLexConstants;
import de.bbcdaas.synonymlexicon.common.partitioner.SolidBlockPartitioner;
import de.bbcdaas.synonymlexicon.cosinesimilarity.jobs.CosSimCalcProcessingJob;
import de.bbcdaas.synonymlexicon.cosinesimilarity.jobs.CosSimCalcTermPairGeneratorJob;
import de.bbcdaas.synonymlexicon.cosinesimilarity.jobs.CosineSimilarityVectorNormJob;
import de.bbcdaas.synonymlexicon.utils.hadoop.ConfUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.DataAccessUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.PathBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.jobcontrol.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;
/**
 * Calculates the cosine similarity between each term of the inputs tag clouds.
 * @author Frithjof Schulte
 * @author Robert Illers
 */
public class CosineSimilarityBuilder extends Configured implements Tool {

	private static Logger logger = Logger.getLogger(CosineSimilarityBuilder.class);

	/**
	 *
	 * @param args
	 * @param conf
	 * @return
	 * @throws ConfigurationException
	 */
	public int go(String[] args, Configuration conf, int startJob, int stopJob, int singleJob) {

		int errorCode;
		String outputDirBaseName = args[1];
		int jobNumber = 12;
		String outputLocalDir;
		String inputRecordsCountFileName;
		
		try {
			
			outputLocalDir = ConfUtils.getStringValue(ConfUtils.KEY_OUTPUT_LOCAL_DIR_NAME, conf);
			inputRecordsCountFileName = ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf);
		} catch(ConfigurationException ex) {
			
			logger.error(ex.getMessage(), ex);
			return Job.FAILED;
		}

		logger.info("---------------------------- Cosine Similarity Builder: ------------------------");

		try {

			// generate vector norms
			logger.info("-------------------------- Job 13 Generate Vector Norm --------------------------");

			String llrVectorPath = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_11_LLRVECTORENTITIES);

			String outVectorNormPath = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_13_TERMVECTORNORMS);

			logger.info("inputPath: "+llrVectorPath);
			logger.info("outputPath: "+outVectorNormPath);
			
			jobNumber++;
			if (singleJob == jobNumber || singleJob == 0 && startJob <= jobNumber && (stopJob == 0 || stopJob >= jobNumber)) {
				
				// Calculates the euklidic n-dimensional vector norm of the llr weightings
				// and writes them as termID/vectorNorm map into a sequence file
				errorCode = ToolRunner.run(new CosineSimilarityVectorNormJob(conf),
					new String[] {llrVectorPath, outVectorNormPath});

				// copy result to locale file system
				DataAccessUtils.copyResultToLocalFileSystem(conf, outputDirBaseName,
					outputLocalDir, SynLexConstants.JOB_OUTPUT_DIR_13_TERMVECTORNORMS,
					inputRecordsCountFileName);
				
				if(errorCode != Job.SUCCESS) {
					return errorCode;
				}
			} else {
				logger.info("Skipped job 13.");
			}

			logger.info("---------------------- done Job 13 Generate Vector Norm -------------------------");
			
			logger.info("----------------------- Job 14 Generate termID pairs ----------------------------");
			
			String termIDPairsPath = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_14_TERMIDPAIRS);
			
			logger.info("inputPath: "+outVectorNormPath);
			logger.info("outputPath: "+termIDPairsPath);
			
			jobNumber++;
			if (singleJob == jobNumber || singleJob == 0 && startJob <= jobNumber && (stopJob == 0 || stopJob >= jobNumber)) {
				
				errorCode = ToolRunner.run(new CosSimCalcTermPairGeneratorJob(conf),
					new String[] {outVectorNormPath, termIDPairsPath});

				// copy result to locale file system
				DataAccessUtils.copyResultToLocalFileSystem(conf, outputDirBaseName,
					outputLocalDir, SynLexConstants.JOB_OUTPUT_DIR_14_TERMIDPAIRS,
					inputRecordsCountFileName);
				
				if(errorCode != Job.SUCCESS) {
					return errorCode;
				}
			} else {
				logger.info("Skipped job 14.");
			}
			
			logger.info("-------------------- done Job 14 Generate termID pairs --------------------------");

			logger.info("----------------------- Job 15 Generate Cosine Similarity -----------------------");

			String outCosSimilarityPath = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_15_COSSIMILARITY);

			logger.info("inputPath: "+termIDPairsPath);
			logger.info("outputPath: "+outCosSimilarityPath);

			jobNumber++;
			if (singleJob == jobNumber || singleJob == 0 && startJob <= jobNumber && (stopJob == 0 || stopJob >= jobNumber)) {
				
				errorCode = ToolRunner.run(new CosSimCalcProcessingJob(conf),
					new String[] {termIDPairsPath, outCosSimilarityPath});

				// copy result to locale file system
				DataAccessUtils.copyResultToLocalFileSystem(conf, outputDirBaseName,
					outputLocalDir, SynLexConstants.JOB_OUTPUT_DIR_15_COSSIMILARITY,
					inputRecordsCountFileName);
				
				if(errorCode != Job.SUCCESS) {
					return errorCode;
				}
			} else {
				logger.info("Skipped job 15.");
			}

			logger.info("------------------ done Job 15 Generate Cosine Similarity -----------------------");

			logger.info("------------------------- Job 16 Sort Cosine Similarity ------------------------");

			String outSortedSimilarityPath = PathBuilder.buildAsString(outputDirBaseName,
				SynLexConstants.JOB_OUTPUT_DIR_16_COSINESIMILARITY_TEXTOUTPUT);

			logger.info("inputPath: "+outCosSimilarityPath);
			logger.info("outputPath: "+outSortedSimilarityPath);

			ConfigurableSortJob sorterJob =  new ConfigurableSortJob(conf);
			sorterJob.setOutputKeyClass(Text.class);
			sorterJob.setOutputValueClass(DoubleWritable.class);
			sorterJob.setOutputFormatClass(TextOutputFormat.class);
			sorterJob.setReducerClass(CosineSimilaritySortOutputReducer.class);
			sorterJob.setNumReduceTasks(ConfUtils.getIntValue(ConfUtils.
				KEY_TERM_LEXICA_SORT_LEXICON_JOB_NUM_REDUCE_TASK, conf));
			sorterJob.setSortComparatorClass(LongWritable.DecreasingComparator.class);

			jobNumber++;
			if (singleJob == jobNumber || singleJob == 0 && startJob <= jobNumber && (stopJob == 0 || stopJob >= jobNumber)) {
				
				// sort cosine similarity from high to low
				errorCode = ToolRunner.run(sorterJob, new String[] {outCosSimilarityPath,
					outSortedSimilarityPath});

				// copy result to locale file system
				DataAccessUtils.copyResultToLocalFileSystem(conf, outputDirBaseName,
					outputLocalDir, SynLexConstants.JOB_OUTPUT_DIR_16_COSINESIMILARITY_TEXTOUTPUT,
					inputRecordsCountFileName);
				
				if(errorCode != Job.SUCCESS) {
					return errorCode;
				}
			} else {
				logger.info("Skipped job 16.");
			}

			logger.info("--------------------- done Job 16 Sort Cosine Similarity ------------------------");

		} catch(Exception e) {

			logger.error(e.getMessage(), e);
			return Job.FAILED;
		}

		logger.info("-------------------------- Cosine Similarity Builder done. ---------------------");

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

	public static void main(String[] args) throws Exception {

		logger.info("Executing CosineSimilarityBuilder...");
		logger.info("ErrorCode: "+ToolRunner.run(new CosineSimilarityBuilder(), args));
	}
	
	/**
	* Input format: <similarity, List(termIdCombi1, termIdCombi2, termIdCombi3, termIdCombi4, ...)>
	* Output format: <termIdCombi1, similarity>, <termIdCombi2, similarity>, <termIdCombi3, similarity>, ...
	* @author Frithjof Schulte
	*/
   public static class CosineSimilaritySortOutputReducer extends Reducer<DoubleWritable,
	   Text, Text, DoubleWritable> {

	   private static Logger logger = Logger.getLogger(CosineSimilaritySortOutputReducer.class);
	   private String termIdDelimiter;
	   private long numberOfTerms;
	   private List<MapFile.Reader> id2TermMapfileReaders = new ArrayList<MapFile.Reader>();

	   /**
		*
		* @param context
		*/
	   @Override
	   public void setup(Context context) throws IOException {

		   Configuration conf = context.getConfiguration();

		   try {

			   this.termIdDelimiter = ConfUtils.getStringValue(ConfUtils.
				   KEY_COSINE_SIMILARITY_KEY_DELIMITER, conf);

			   String outputDirBaseName = ConfUtils.getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf);
			   
			   this.id2TermMapfileReaders = DataAccessUtils.getMapFilePartReaders(PathBuilder.buildAsPath(
					ConfUtils.getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf), 
					SynLexConstants.JOB_OUTPUT_DIR_4_ID2TERMLEXICON), conf);

			    // get the number of terms from the hdfs
			   String nbOfTerms = DataAccessUtils.getFileDataFromHDFS(conf,
				   PathBuilder.buildAsPath(outputDirBaseName, SynLexConstants.JOB_COUNTER_FOLDER,
				   SynLexConstants.JOB_OUTPUT_DIR_3_COUNTSORT, 
				   ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf)));
			   this.numberOfTerms = Long.parseLong(nbOfTerms);
			   
		   } catch(ConfigurationException cex) {
			   logger.error("Some needed configKey not configured!", cex);
		   }
	   }

	   /**
		*
		* @param context
		* @throws IOException
		* @throws InterruptedException
		*/
	   @Override
	   public void cleanup(Context context) throws IOException, InterruptedException {
		   
		   for (MapFile.Reader reader : this.id2TermMapfileReaders) {
				IOUtils.closeStream(reader);
		   }
	   }

	   /**
		* 
		* @param similarity
		* @param termCombiKeys
		* @param context
		* @throws IOException
		* @throws InterruptedException
		*/
	   @Override
	   public void reduce(DoubleWritable similarity, Iterable<Text> termCombiKeys,
		   Context context) throws IOException, InterruptedException {

		   for(Text termCombiKey : termCombiKeys) {

			   String[] keys = termCombiKey.toString().split(this.termIdDelimiter);
			   
			   LongWritable termID1 = new LongWritable(Long.parseLong(keys[0]));
			   LongWritable termID2 = new LongWritable(Long.parseLong(keys[1]));
			   
			   TermCountPair termCountPair = new TermCountPair();
			   
			   MapFile.Reader reader = DataAccessUtils.getMapFilePartReaderByKey(this.
					id2TermMapfileReaders, termID1, termCountPair, new SolidBlockPartitioner(this.numberOfTerms));
			   reader.get(termID1, termCountPair);
			   
			   String term1 = termCountPair.getTerm().toString();
			   
			   reader = DataAccessUtils.getMapFilePartReaderByKey(this.
					id2TermMapfileReaders, termID2, termCountPair, new SolidBlockPartitioner(this.numberOfTerms));
			   reader.get(termID2, termCountPair);
			   
			   String term2 = termCountPair.getTerm().toString();

			   Text keyTerms = new Text(new StringBuilder().append(term1).
					append(this.termIdDelimiter).append(term2).toString());
			   
			   context.write(keyTerms, similarity);
		   }
	   }
   }
}
