package de.bbcdaas.synonymlexicon.termlexicon.jobs;

import de.bbcdaas.synonymlexicon.common.constants.SynLexConstants;
import de.bbcdaas.synonymlexicon.common.jobs.AbstractJob;
import de.bbcdaas.synonymlexicon.utils.hadoop.ConfUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.DataAccessUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.JobBuilder;
import de.bbcdaas.synonymlexicon.utils.hadoop.PathBuilder;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

/**
 *
 * @author Robert Illers
 */
public class WordFilterJob extends AbstractJob {

	public final static int DEFAULT_MAX_TERM_LENGHT = 30;
	public final static int DEFAULT_MIN_TERM_LENGHT = 1;
	public final static int DEFAULT_MIN_TERM_FREQUENCY = 100;
	
	public WordFilterJob(Configuration conf) {
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
		job.setMapperClass(WordFilterMapper.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(Text.class);
		
		int errorCode = job.waitForCompletion(true) ?
			org.apache.hadoop.mapred.jobcontrol.Job.SUCCESS :
			org.apache.hadoop.mapred.jobcontrol.Job.FAILED;
		
		// write the number of input lines into a file
		long countRecords = job.getCounters().
			findCounter("org.apache.hadoop.mapreduce.TaskCounter", "MAP_INPUT_RECORDS").getValue();
		
		Path countRecordsFilePath = PathBuilder.buildAsPath(ConfUtils.
			getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf), SynLexConstants.
			JOB_COUNTER_FOLDER, SynLexConstants.JOB_OUTPUT_DIR_2_WORDFILTER,
			ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf));
		DataAccessUtils.addFileDataToHDFS(conf, countRecordsFilePath, String.valueOf(countRecords));
		
		return errorCode;
	}
	
	/**
	 * 
	 */
	public static class WordFilterMapper extends Mapper<LongWritable, Text, LongWritable, Text> {
		
		private int maxTermLength;
		private int minTermLength;
		private int minTermFrequency;

		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
		
			Configuration conf = context.getConfiguration();
		
			this.maxTermLength = conf.getInt("maxTermLength", WordFilterJob.DEFAULT_MAX_TERM_LENGHT);
			this.minTermLength = conf.getInt("minTermLength", WordFilterJob.DEFAULT_MIN_TERM_LENGHT);
			this.minTermFrequency = conf.getInt("minTermFrequency", WordFilterJob.DEFAULT_MIN_TERM_FREQUENCY);
		}
		
		/**
		 * 
		 * @param key
		 * @param value
		 * @param context
		 * @throws IOException
		 * @throws InterruptedException 
		 */
		@Override
		protected void map(LongWritable count, Text term, Context context) throws IOException, InterruptedException {
			
			if (WordFilter.filter(count.get(), term.toString(), this.minTermLength,
				this.maxTermLength, this.minTermFrequency)) {
				
				context.write(count, term);
			} 
		}
	}
	
	/**
	 * 
	 */
	public static class WordFilter {
		
		/**
		 * 
		 * @param count
		 * @param term
		 * @param minTermLength
		 * @param maxTermLength
		 * @param minTermFrequency
		 * @return 
		 */
		public static boolean filter(long count, String term,
			int minTermLength, int maxTermLength, long minTermFrequency) {

			// Rule 1: no empty terms allowed
			if (term == null || term.isEmpty()) {
				return false;
			}

			// Rule 2: terms should not be to short or too long
			if (term.length() > maxTermLength || term.length() < minTermLength) {
				return false;
			}

			// Rule 3: term frequency should not be to small
			if (count < minTermFrequency) {
				return false;
			}
			return true;
		}
	}
}