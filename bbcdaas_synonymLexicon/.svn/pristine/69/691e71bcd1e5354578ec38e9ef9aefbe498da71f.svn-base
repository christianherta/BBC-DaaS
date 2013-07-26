package de.bbcdaas.synonymlexicon.termlexicon.jobs;

import de.bbcdaas.synonymlexicon.common.beans.IdCountPair;
import de.bbcdaas.synonymlexicon.common.constants.SynLexConstants;
import de.bbcdaas.synonymlexicon.common.jobs.AbstractJob;
import de.bbcdaas.synonymlexicon.utils.hadoop.ConfUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.DataAccessUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.JobBuilder;
import de.bbcdaas.synonymlexicon.utils.hadoop.PathBuilder;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * Writes the termLexicon as human readable text into a textfile
 * @author Robert Illers
 */
public class TermCountTextOutputJob extends AbstractJob {
	
	public TermCountTextOutputJob(Configuration conf) {
		super(conf);
	}
	
	@Override
	public int run(String[] args) throws Exception {
		
		Configuration conf =  getConf();
		Job job = JobBuilder.buildJob(this, conf, args);
		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setMapperClass(TermCountTextOutputMapper.class);
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
			JOB_COUNTER_FOLDER, SynLexConstants.JOB_OUTPUT_DIR_7_TERMCOUNT_TEXTOUTPUT,
			ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf));
		DataAccessUtils.addFileDataToHDFS(conf, countRecordsFilePath, String.valueOf(countRecords));
		
		return errorCode;
	}
	
	/**
	* Writes the termLexicon as human readable text into a textfile
	* @author Robert Illers
	*/
   public static  class TermCountTextOutputMapper extends Mapper<Text, IdCountPair, Text, Text> {

	   /**
		* Reads the term/IDCountPair map from the output files from job 5 and writes
		* it as human readable text output (term/count).
		* @param key term
		* @param value idCountPair
		* @param context output file
		* @throws IOException
		* @throws InterruptedException 
		*/
	   @Override
	   protected void map(Text key, IdCountPair value, Context context) throws IOException, InterruptedException {

		   String term = key.toString();
		   Long count = value.getCount();
		   Long termID = value.getTermId();
		   // output format : "term count:ID"
		   context.write(new Text(term), new Text(new StringBuilder().
				append(count.toString()).append(":").append(termID.toString()).toString()));
	   }
   } 
}