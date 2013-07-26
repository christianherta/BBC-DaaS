package de.bbcdaas.synonymlexicon.cosinesimilarity.jobs;

import de.bbcdaas.synonymlexicon.common.constants.SynLexConstants;
import de.bbcdaas.synonymlexicon.common.jobs.AbstractJob;
import de.bbcdaas.synonymlexicon.common.mahout.used.RandomAccessSparseVector;
import de.bbcdaas.synonymlexicon.common.mahout.used.Vector;
import de.bbcdaas.synonymlexicon.common.mahout.used.VectorWritable;
import de.bbcdaas.synonymlexicon.utils.common.CalculationUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.ConfUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.DataAccessUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.JobBuilder;
import de.bbcdaas.synonymlexicon.utils.hadoop.PathBuilder;
import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.MapFileOutputFormat;
/**
 * Job 13
 * Calculates the vector norm for each term from the llr vector.
 * @author Frithjof Schulte
 */
public class CosineSimilarityVectorNormJob extends AbstractJob {

	public CosineSimilarityVectorNormJob(Configuration conf) {
		super(conf);
	}
	
	@Override
	public int run(String[] args) throws Exception {

		Configuration conf = getConf();
		Job job = JobBuilder.buildJob(this, conf, args);
		job.setMapperClass(CosineSimilarityVectorNormMapper.class);
		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setOutputFormatClass(MapFileOutputFormat.class);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(DoubleWritable.class);
		job.setNumReduceTasks(0);

		int errorCode = job.waitForCompletion(true) ?
			org.apache.hadoop.mapred.jobcontrol.Job.SUCCESS :
			org.apache.hadoop.mapred.jobcontrol.Job.FAILED;
		
		// write the number of input lines into a file
		long countRecords = job.getCounters().
			findCounter("org.apache.hadoop.mapreduce.TaskCounter", "MAP_INPUT_RECORDS").getValue();
		
		Path countRecordsFilePath = PathBuilder.buildAsPath(ConfUtils.
			getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf), SynLexConstants.
			JOB_COUNTER_FOLDER, SynLexConstants.JOB_OUTPUT_DIR_13_TERMVECTORNORMS,
			ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf));
		DataAccessUtils.addFileDataToHDFS(conf, countRecordsFilePath, String.valueOf(countRecords));
		
		return errorCode;
	}
	
	/**
	* Calculates the vectorNorm of the terms llr vectors.
	* @author Frithjof Schulte
	*/
   public static class CosineSimilarityVectorNormMapper extends Mapper<LongWritable,
	   VectorWritable, LongWritable, DoubleWritable> {

	   /**
		*
		* @param key
		* @param value
		* @param context
		* @throws IOException
		* @throws InterruptedException
		*/
	   @Override
	   public void map(LongWritable key, VectorWritable value, Context context)
		   throws IOException, InterruptedException {

		   Double vectorNorm;
		   double tupleSum = 0.00;

		   RandomAccessSparseVector termsLlrVector = (RandomAccessSparseVector)value.get();
		   Iterator<Vector.Element> LlrVectorIterator = termsLlrVector.iterateNonZero();

		   // calculate euklid. n-dimensional vector norm of llr weightings
		   // sqrt(sum(llrEntityWeighting_x * llrEntityWeighting_x)) [x € N, 1 <= x <= n] 
		   while(LlrVectorIterator.hasNext()) {

			   Vector.Element vectorTuple = LlrVectorIterator.next();
			   tupleSum += Math.pow(vectorTuple.get(), 2);
		   }
		   vectorNorm = Math.sqrt(tupleSum);
		   vectorNorm = CalculationUtils.roundValue(vectorNorm, "#.###");

		   context.write(key, new DoubleWritable(vectorNorm));
	   }

   }
}
