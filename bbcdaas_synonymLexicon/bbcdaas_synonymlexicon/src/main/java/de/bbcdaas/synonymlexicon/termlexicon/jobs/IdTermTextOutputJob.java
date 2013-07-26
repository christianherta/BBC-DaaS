package de.bbcdaas.synonymlexicon.termlexicon.jobs;

import de.bbcdaas.synonymlexicon.common.beans.TermCountPair;
import de.bbcdaas.synonymlexicon.common.jobs.AbstractJob;
import de.bbcdaas.synonymlexicon.utils.hadoop.JobBuilder;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 *
 * @author Robert Illers
 */
public class IdTermTextOutputJob extends AbstractJob {
	
	public IdTermTextOutputJob(Configuration conf) {
		super(conf);
	}
	
	@Override
	public int run(String[] args) throws Exception {
		
		Configuration conf =  getConf();
		Job job = JobBuilder.buildJob(this, conf, args);
		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setMapperClass(IdTermTextOutputMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setNumReduceTasks(0);
		
		int errorCode = job.waitForCompletion(true) ?
			org.apache.hadoop.mapred.jobcontrol.Job.SUCCESS :
			org.apache.hadoop.mapred.jobcontrol.Job.FAILED;	
		
		return errorCode;
	}
	
	public static class IdTermTextOutputMapper extends Mapper<LongWritable, TermCountPair, Text, Text> {
	
		/**
		 * Reads the ID/termCountPair map from the output files from job 4 and writes
		 * it as human readable text output (id/term).
		 * @param key id
		 * @param value termCountPair
		 * @param context output file
		 * @throws IOException
		 * @throws InterruptedException 
		 */
		@Override
		protected void map(LongWritable id, TermCountPair termCountPair, Mapper.Context context) throws IOException, InterruptedException {

			// output format : "id term"
			context.write(new Text(id.toString()), new Text(termCountPair.getTerm().toString()));
		}
	}
}
