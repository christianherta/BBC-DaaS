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
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * Count/term text output
 * @author Robert Illers
 */
public class CountTermTextOutputJob extends AbstractJob {

	public CountTermTextOutputJob(Configuration conf) {
		super(conf);
	}
	
	@Override
	public int run(String[] args) throws Exception {
		
		Configuration conf =  getConf();
		Job job = JobBuilder.buildJob(this, conf, args);
		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setMapperClass(CountTermTextOutputMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setNumReduceTasks(0);
		
		int errorCode = job.waitForCompletion(true) ?
			org.apache.hadoop.mapred.jobcontrol.Job.SUCCESS :
			org.apache.hadoop.mapred.jobcontrol.Job.FAILED;	
		
		// write the number of input lines into a file
		long countRecords = job.getCounters().
			findCounter("org.apache.hadoop.mapreduce.TaskCounter", "MAP_INPUT_RECORDS").getValue();
		
		Path countRecordsFilePath = PathBuilder.buildAsPath(ConfUtils.
			getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf), SynLexConstants.
			JOB_COUNTER_FOLDER, SynLexConstants.JOB_OUTPUT_DIR_8_COUNTTERM_TEXTOUTPUT,
			ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf));
		DataAccessUtils.addFileDataToHDFS(conf, countRecordsFilePath, String.valueOf(countRecords));
		
		return errorCode;
	}

	public static class CountTermTextOutputMapper extends Mapper<LongWritable, Text, Text, Text> {
	
		/**
		 * Reads the term/IDCountPair map from the output files from job 5 and writes
		 * it as human readable text output (count/term).
		 * @param key term
		 * @param value idCountPair
		 * @param context output file
		 * @throws IOException
		 * @throws InterruptedException 
		 */
		@Override
		protected void map(LongWritable count, Text term, Context context) throws IOException, InterruptedException {

			// output format : "count term"
			context.write(new Text(count.toString()), term);
		}
	}
	
}
