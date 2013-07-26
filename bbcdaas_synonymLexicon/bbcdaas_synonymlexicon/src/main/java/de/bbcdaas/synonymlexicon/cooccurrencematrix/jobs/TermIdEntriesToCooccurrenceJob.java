package de.bbcdaas.synonymlexicon.cooccurrencematrix.jobs;

import de.bbcdaas.synonymlexicon.common.jobs.AbstractJob;
import de.bbcdaas.synonymlexicon.common.mahout.used.RandomAccessSparseVector;
import de.bbcdaas.synonymlexicon.common.mahout.used.Vector;
import de.bbcdaas.synonymlexicon.common.mahout.used.VectorWritable;
import de.bbcdaas.synonymlexicon.utils.hadoop.JobBuilder;
import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
/**
 * Job 10
 * @author Frithjof Schulte
 */
public class TermIdEntriesToCooccurrenceJob extends AbstractJob {

	public TermIdEntriesToCooccurrenceJob(Configuration conf) {
		super(conf);
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
		Job job = JobBuilder.buildJob(this, conf, args);
		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setMapperClass(TermIdEntriesToCooccurrenceMapper.class);
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(LongWritable.class);
		job.setReducerClass(TermIdEntriesToCooccurrenceReducer.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(VectorWritable.class);

		return job.waitForCompletion(true) ?
			org.apache.hadoop.mapred.jobcontrol.Job.SUCCESS :
			org.apache.hadoop.mapred.jobcontrol.Job.FAILED;
	}
	
	/**
	* Input format: < , {termId1:1.0, termId2:1.0, termId3:1.0}>
	* Output format: <termId1, termId2>, <termId1, termId3>, <termId2, termId1>, ...
	*
	* @author Frithjof Schulte
	*/
   public static class TermIdEntriesToCooccurrenceMapper extends Mapper<LongWritable,
	   VectorWritable, LongWritable, LongWritable> {

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

		   Iterator<Vector.Element> iterOut = value.get().iterateNonZero();

		   // build term pairs
		   while(iterOut.hasNext()) {

			   Long termID1 = (long)iterOut.next().index();

			   Iterator<Vector.Element> iterIn = value.get().iterateNonZero();

			   while(iterIn.hasNext()) {

				   Long termID2 = (long)iterIn.next().index();

				   if(termID1 != termID2) {

					   context.write(new LongWritable(termID1),
									 new LongWritable(termID2));
				   }
			   }
		   }
	   }
   }
   
   /**
	* Input format: <termId1, List(termId2, termId2, termId3, termID2, ...)>
	* Output format: < termId1, {termId2:"cf", termId3:"cf", termId4:"cf", ...}>
	* (cf = co-occurred frequence)
	* @author Frithjof Schulte
	*/
   public static class TermIdEntriesToCooccurrenceReducer extends Reducer<LongWritable,
	   LongWritable, LongWritable, VectorWritable> {

	   /**
		*
		* @param termIdKey
		* @param termIds
		* @param context
		* @throws IOException
		* @throws InterruptedException
		*/
	   @Override
	   public void reduce(LongWritable termIdKey, Iterable<LongWritable> termIds,
		   Context context) throws IOException, InterruptedException {

		   Double termId2Count;
		   Vector cooccurrenceVector = new RandomAccessSparseVector(Integer.MAX_VALUE, 100);

		   for(LongWritable termIdsEntry : termIds) {

			   if(termIdKey.get() != termIdsEntry.get()) {

				   Integer termId2 = (int) termIdsEntry.get();
				   termId2Count = cooccurrenceVector.get(termId2) + 1.0;
				   cooccurrenceVector.set(termId2, termId2Count);
			   }
		   }
		   context.write(termIdKey, new VectorWritable(cooccurrenceVector));
	   }
   }
}
