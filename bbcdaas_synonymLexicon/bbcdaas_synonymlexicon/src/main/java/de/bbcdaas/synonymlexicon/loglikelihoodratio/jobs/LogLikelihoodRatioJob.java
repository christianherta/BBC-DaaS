package de.bbcdaas.synonymlexicon.loglikelihoodratio.jobs;

import de.bbcdaas.synonymlexicon.common.beans.TermCountPair;
import de.bbcdaas.synonymlexicon.common.constants.SynLexConstants;
import de.bbcdaas.synonymlexicon.common.jobs.AbstractJob;
import de.bbcdaas.synonymlexicon.common.mahout.used.RandomAccessSparseVector;
import de.bbcdaas.synonymlexicon.common.mahout.used.Vector;
import de.bbcdaas.synonymlexicon.common.mahout.used.VectorWritable;
import de.bbcdaas.synonymlexicon.common.partitioner.SolidBlockPartitioner;
import de.bbcdaas.synonymlexicon.utils.common.CalculationUtils;
import de.bbcdaas.synonymlexicon.utils.common.LogLikelihoodRatioTest;
import de.bbcdaas.synonymlexicon.utils.hadoop.ConfUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.DataAccessUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.JobBuilder;
import de.bbcdaas.synonymlexicon.utils.hadoop.PathBuilder;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.MapFileOutputFormat;
import org.apache.log4j.Logger;
/**
 * Job 8
 * @author Frithjof Schulte
 */
public class LogLikelihoodRatioJob extends AbstractJob {

	public LogLikelihoodRatioJob(Configuration conf) {
		super(conf);
	}
	
	/**
	 * Reads the termID/cooccurrence map from job 7 and calculates for each term pair the loglikelihood
	 * weighting. Stores the result into a termID/llrTermWeightingVector map into a sequence file.
	 * @param args input/output path
	 * @return errorCode
	 * @throws Exception
	 */
	@Override
	public int run(String[] args) throws Exception {

		Configuration conf = getConf();
		Job job = JobBuilder.buildJob(this, conf, args);
		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setMapperClass(LogLikelihoodCalculationMapper.class);
		job.setOutputFormatClass(MapFileOutputFormat.class);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(VectorWritable.class);

		int errorCode =  job.waitForCompletion(true) ?
			org.apache.hadoop.mapred.jobcontrol.Job.SUCCESS :
			org.apache.hadoop.mapred.jobcontrol.Job.FAILED;
		
		// write the number of input lines into a file
		long countRecords = job.getCounters().
			findCounter("org.apache.hadoop.mapreduce.TaskCounter", "MAP_INPUT_RECORDS").getValue();
		
		Path countRecordsFilePath = PathBuilder.buildAsPath(ConfUtils.
			getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf), SynLexConstants.
			JOB_COUNTER_FOLDER, SynLexConstants.JOB_OUTPUT_DIR_11_LLRVECTORENTITIES,
			ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf));
		DataAccessUtils.addFileDataToHDFS(conf, countRecordsFilePath, String.valueOf(countRecords));
		
		return errorCode;
	}

	/**
	* Reads the termID/cooccurrence map from job 7 and calculates for each term pair the loglikelihood
	* weighting. Stores the result into a termID/llrTermWeightingVector map into a sequence file.
	* @author Frithjof Schulte
	* @author Robert Illers
	*/
   public static class LogLikelihoodCalculationMapper extends Mapper<LongWritable,
	   VectorWritable, LongWritable, VectorWritable> {

	   private static Logger logger = Logger.getLogger(LogLikelihoodCalculationMapper.class);
	   private List<MapFile.Reader> id2TermMapfileReaders;
	   private long numberOfTagClouds = 0;
	   private long numberOfTerms = 0;

	   /**
		* Set the id2Term mapfile reader and the input records count.
		* @param context
		*/
	   @Override
	   public void setup(Mapper.Context context) {

		   Configuration conf = context.getConfiguration();

		   String outputDirBaseName;

		   try {

			   outputDirBaseName = ConfUtils.getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf);

			   // get the id2term map from the hdfs
			   this.id2TermMapfileReaders = DataAccessUtils.getMapFilePartReaders(PathBuilder.
					buildAsPath(outputDirBaseName, SynLexConstants.JOB_OUTPUT_DIR_4_ID2TERMLEXICON), conf);

			   // get the number of tagclouds from the hdfs
			   String nbOfTagClouds = DataAccessUtils.getFileDataFromHDFS(conf,
				   PathBuilder.buildAsPath(outputDirBaseName, SynLexConstants.JOB_COUNTER_FOLDER,
				   SynLexConstants.JOB_OUTPUT_DIR_9_VECTORENTITIES, 
				   ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf)));
			   this.numberOfTagClouds = Long.parseLong(nbOfTagClouds);
			   
			   // get the number of terms from the hdfs
			   String nbOfTerms = DataAccessUtils.getFileDataFromHDFS(conf,
				   PathBuilder.buildAsPath(outputDirBaseName, SynLexConstants.JOB_COUNTER_FOLDER,
				   SynLexConstants.JOB_OUTPUT_DIR_3_COUNTSORT, 
				   ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf)));
			   this.numberOfTerms = Long.parseLong(nbOfTerms);

		   } catch(Exception ex) {
			   logger.error(ex);
		   } 
	   }

	   /**
		* Close mapfile reader.
		* @param context
		* @throws IOException
		*/
	   @Override
	   public void cleanup(Mapper.Context context) throws IOException {
		   
		   for (MapFile.Reader reader : id2TermMapfileReaders) {
				IOUtils.closeStream(reader);
		   }
	   }

	   /**
		* Calculates the llr weightings for a term and its cooccurring terms.
		* @param termID
		* @param coocVector
		* @param context
		* @throws IOException
		* @throws InterruptedException
		*/
	   @Override
	   public void map(LongWritable termID, VectorWritable coocVector, Mapper.Context context)
		   throws IOException, InterruptedException {

		   long termCount;
		   long corTermId;
		   long corTermCount;
		   double occuredTogether;
		   TermCountPair termCountPair = new TermCountPair();
		   Vector llrEntityVector = new RandomAccessSparseVector(Integer.MAX_VALUE, 10);

		   // get the term frequency of current term (count)
		   MapFile.Reader reader = DataAccessUtils.getMapFilePartReaderByKey(this.
				id2TermMapfileReaders, termID, termCountPair, new SolidBlockPartitioner(this.numberOfTerms));
		   reader.get(termID, termCountPair);
		   termCount = termCountPair.getCount();

		   RandomAccessSparseVector correspondTermsVector = (RandomAccessSparseVector)coocVector.get();
		   Iterator<Vector.Element> correspondTermsIterator = correspondTermsVector.iterateNonZero();

		   // iterate through the cooc terms
		   while (correspondTermsIterator.hasNext()) {

			   // get coresponding term id and cooccurrence count
			   Vector.Element correspondTerm = correspondTermsIterator.next();
			   corTermId = correspondTerm.index();
			   occuredTogether = correspondTerm.get();

			   // get the number of times the coresponding term is in termLexicon (term quality)
			   reader = DataAccessUtils.getMapFilePartReaderByKey(this.id2TermMapfileReaders,
					new LongWritable(corTermId), termCountPair, new SolidBlockPartitioner(this.numberOfTerms));
			   reader.get(new LongWritable(corTermId), termCountPair);
			   corTermCount = termCountPair.getCount();

			   //calculate llr weighting
			   Double llrResult = LogLikelihoodRatioTest.computeSyntagmaticRelation(termCount, corTermCount,
				   occuredTogether, this.numberOfTagClouds);

			   // only positive llr weighting useable (quality of result ok)
			   if (llrResult > 0) { 
				   llrEntityVector.set((int)corTermId, CalculationUtils.roundValue(llrResult, "#.###"));
			   }
		   }

		   // add only filled llr entity vectors
		   if (llrEntityVector.size() != 0) {
			   context.write(termID, new VectorWritable(llrEntityVector));
		   }
	   }
	}
}
