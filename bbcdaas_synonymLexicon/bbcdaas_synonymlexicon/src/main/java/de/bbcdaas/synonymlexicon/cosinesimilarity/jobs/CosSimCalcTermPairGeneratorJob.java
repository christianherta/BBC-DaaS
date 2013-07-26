package de.bbcdaas.synonymlexicon.cosinesimilarity.jobs;

import de.bbcdaas.synonymlexicon.common.constants.SynLexConstants;
import de.bbcdaas.synonymlexicon.common.jobs.AbstractJob;
import de.bbcdaas.synonymlexicon.utils.hadoop.ConfUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.DataAccessUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.JobBuilder;
import de.bbcdaas.synonymlexicon.utils.hadoop.PathBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.log4j.Logger;

/**
 * Job 14
 * input from job 13 (ID/VectorNorm)
 * @author Robert Illers
 */
public class CosSimCalcTermPairGeneratorJob extends AbstractJob {
	
	public CosSimCalcTermPairGeneratorJob(Configuration conf) {
		super(conf);
	}
	
	@Override
	public int run(String[] args) throws Exception {
		
		Configuration conf = getConf();
		// input: job 13 output (ID/VectorNorm map)
		Job job = JobBuilder.buildJob(this, conf, args);
		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setMapperClass(CosSimCalcTermPairGeneratorMapper.class);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(LongWritable.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		job.setNumReduceTasks(0);
		
		int errorCode = job.waitForCompletion(true) ?
			org.apache.hadoop.mapred.jobcontrol.Job.SUCCESS :
			org.apache.hadoop.mapred.jobcontrol.Job.FAILED;
		
		// write the number of input lines into a file
		long countRecords = job.getCounters().
			findCounter("org.apache.hadoop.mapreduce.TaskCounter", "MAP_INPUT_RECORDS").getValue();
		
		Path countRecordsFilePath = PathBuilder.buildAsPath(ConfUtils.
			getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf), SynLexConstants.
			JOB_COUNTER_FOLDER, SynLexConstants.JOB_OUTPUT_DIR_14_TERMIDPAIRS,
			ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf));
		DataAccessUtils.addFileDataToHDFS(conf, countRecordsFilePath, String.valueOf(countRecords));
		
		return errorCode;
	}

	/**
	*
	* @author Robert Illers
	*/
   public static class CosSimCalcTermPairGeneratorMapper extends Mapper<LongWritable,
	   DoubleWritable, LongWritable, LongWritable>{

	   private static Logger logger = Logger.getLogger(CosSimCalcTermPairGeneratorMapper.class);
	   private List<Long> termIDs = new ArrayList<Long>();

	   @Override
	   protected void setup(Context context) throws IOException, InterruptedException {

		   List<MapFile.Reader> vectorNormMapfileReaders = new ArrayList<MapFile.Reader>();
		   String outputDirBaseName;
			   
		   try {
			   
			   Configuration conf = context.getConfiguration();
			   
			   outputDirBaseName = ConfUtils.getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf);
			   
			   Path inputPath = PathBuilder.buildAsPath(outputDirBaseName, 
					SynLexConstants.JOB_OUTPUT_DIR_13_TERMVECTORNORMS);
			   vectorNormMapfileReaders = DataAccessUtils.getMapFilePartReaders(inputPath, conf);

			   // get all term ids
			   LongWritable termID = new LongWritable();
			   for (MapFile.Reader reader : vectorNormMapfileReaders) {
					while(reader.next(termID, new DoubleWritable())) {
						this.termIDs.add(termID.get());
					}
			   }
			   Collections.sort(termIDs);
		   } catch(ConfigurationException ex) {
			   logger.error(ex);
		
		   } finally {
			   
			    for (MapFile.Reader reader : vectorNormMapfileReaders) {
					IOUtils.closeStream(reader);
				}
		   }
	   }

	   @Override
	   protected void map(LongWritable termID1, DoubleWritable vectorNorm, Context context) throws IOException, InterruptedException {

		   for (Long termID2 : this.termIDs) {

			   if (termID1.get() < termID2) {
				   context.write(termID1, new LongWritable(termID2));
			   } 
		   }
	   }
   }
}
