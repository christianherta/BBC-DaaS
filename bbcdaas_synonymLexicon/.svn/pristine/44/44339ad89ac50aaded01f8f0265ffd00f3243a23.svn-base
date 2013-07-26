package de.bbcdaas.synonymlexicon.termlexicon.jobs;

import de.bbcdaas.synonymlexicon.common.beans.IdCountPair;
import de.bbcdaas.synonymlexicon.common.beans.TermCountPair;
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
import org.apache.hadoop.mapreduce.lib.output.MapFileOutputFormat;

/**
 *
 * @author Robert Illers
 */
public class TermIdLexiconWriteJob extends AbstractJob {

	public TermIdLexiconWriteJob(Configuration conf) {
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
		job.setMapperClass(TermIdLexiconWriteMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IdCountPair.class);
		job.setOutputFormatClass(MapFileOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IdCountPair.class);
		
		int errorCode = job.waitForCompletion(true) ?
			org.apache.hadoop.mapred.jobcontrol.Job.SUCCESS :
			org.apache.hadoop.mapred.jobcontrol.Job.FAILED;
		
		// write the number of input lines into a file
		long countRecords = job.getCounters().
			findCounter("org.apache.hadoop.mapreduce.TaskCounter", "MAP_INPUT_RECORDS").getValue();
		
		Path countRecordsFilePath = PathBuilder.buildAsPath(ConfUtils.
			getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf), SynLexConstants.
			JOB_COUNTER_FOLDER, SynLexConstants.JOB_OUTPUT_DIR_5_TERM2IDLEXICON,
			ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf));
		DataAccessUtils.addFileDataToHDFS(conf, countRecordsFilePath, String.valueOf(countRecords));
		
		return errorCode;
	}
	
	/**
	 * 
	 */
	public static class TermIdLexiconWriteMapper extends Mapper<LongWritable, TermCountPair, Text, IdCountPair> {

		/**
		 * 
		 * @param id
		 * @param termCountPair
		 * @param context
		 * @throws IOException
		 * @throws InterruptedException 
		 */
		@Override
		protected void map(LongWritable id, TermCountPair termCountPair, Context context) throws IOException, InterruptedException {
			
			IdCountPair idCountPair = new IdCountPair();
			idCountPair.set(id.get(), termCountPair.getCount());

			context.write(termCountPair.getTerm(), idCountPair);
		}
	}
}
