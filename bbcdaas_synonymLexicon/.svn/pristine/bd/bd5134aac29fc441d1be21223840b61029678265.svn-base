package de.bbcdaas.synonymlexicon.common.jobs;

import de.bbcdaas.synonymlexicon.common.constants.SynLexConstants;
import de.bbcdaas.synonymlexicon.utils.hadoop.ConfUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.DataAccessUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.JobBuilder;
import de.bbcdaas.synonymlexicon.utils.hadoop.PathBuilder;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
/**
 * Generic Sort Job, inputFormat, outputFormat, inputKey, outputKey, reducer and
 * sortComparator can be set.
 * @author Christian Herta
 * @author Frithjof Schulte
 */
public class ConfigurableSortJob extends AbstractJob {

	private String jobName;
	private Integer numReduceTasks = 4;
	private Class outputKeyClass = null;
	private Class outputValueClass = null;
	private Class<? extends InputFormat> inputFormatClass = SequenceFileInputFormat.class;
	private Class<? extends OutputFormat> outputFormatClass = SequenceFileOutputFormat.class;
	private Class<? extends Reducer> reducerClass = null;
	private Class<? extends RawComparator> sortComparatorClass = null;
	private String inputRecordsCountFileName = null;
	private String jobOutputFolder = null;

	public ConfigurableSortJob(Configuration conf) {
		super(conf);
	}
	
	/**
	 * Name of the Job (optional)
	 * @param jobName 
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * Class for key output (required)
	 * @param outputKeyClass 
	 */
	public void setOutputKeyClass(Class outputKeyClass) {
		this.outputKeyClass = outputKeyClass;
	}

	/**
	 * Class for value output (required)
	 * @param outputValueClass 
	 */
	public void setOutputValueClass(Class outputValueClass) {
		this.outputValueClass = outputValueClass;
	}

	/**
	 * Class for output format (optional)
	 * @param outputFormatClass 
	 */
	public void setOutputFormatClass(Class<? extends OutputFormat> outputFormatClass) {
		this.outputFormatClass = outputFormatClass;
	}

	/**
	 * Class for input format (optional)
	 * @param inputFormatClass 
	 */
	public void setInputFormatClass(Class<? extends InputFormat> inputFormatClass) {
		this.inputFormatClass = inputFormatClass;
	}

	/**
	 * Class for reducing tasks (optional)
	 * @param reducerClass 
	 */
	public void setReducerClass(Class<? extends Reducer> reducerClass) {
		this.reducerClass = reducerClass;
	}

	/**
	 * Class for sort tasks (optional)
	 * @param sortComparatorClass 
	 */
	public void setSortComparatorClass(Class<? extends RawComparator> sortComparatorClass) {
		this.sortComparatorClass = sortComparatorClass;
	}

	/**
	 * Set the number of reduce tasks (default: 4)
	 * @param numReduceTasks 
	 */
	public void setNumReduceTasks(int numReduceTasks) {
		this.numReduceTasks = numReduceTasks;
	}

	public String getInputRecordsCountFileName() {
		return inputRecordsCountFileName;
	}

	public String getJobOutputFolder() {
		return jobOutputFolder;
	}

	public void setInputRecordsCountFileData(String inputRecordsCountFileName, String jobOutputFolder) {
		this.inputRecordsCountFileName = inputRecordsCountFileName;
		this.jobOutputFolder = jobOutputFolder;
	}

	/**
	 * Starts the configured job.
	 * @param args input and output path
	 * @return errorCode
	 * @throws Exception
	 */
	@Override
	public int run(String[] args) throws Exception {

		Configuration conf = getConf();
		Job job = JobBuilder.buildJob(this, conf, args);

		if (this.jobName != null && !this.jobName.isEmpty()) {
			job.setJobName(this.jobName);
		}

		if(this.reducerClass != null) {

			job.setReducerClass(this.reducerClass);
			job.setMapOutputKeyClass(this.outputValueClass);
			job.setMapOutputValueClass(this.outputKeyClass);
			job.setNumReduceTasks(this.numReduceTasks);
		}

		job.setInputFormatClass(this.inputFormatClass);
		job.setOutputFormatClass(this.outputFormatClass);
		job.setOutputKeyClass(this.outputKeyClass);
		job.setOutputValueClass(this.outputValueClass);

		if (this.sortComparatorClass != null){
			job.setSortComparatorClass(this.sortComparatorClass);
		}

		int errorCode = job.waitForCompletion(true) ?
			org.apache.hadoop.mapred.jobcontrol.Job.SUCCESS :
			org.apache.hadoop.mapred.jobcontrol.Job.FAILED;
		
		if (this.inputRecordsCountFileName != null && this.jobOutputFolder != null) {
			
			// write the number of read tag clouds into a file
			long countRecords = job.getCounters().
				findCounter("org.apache.hadoop.mapreduce.TaskCounter", "MAP_INPUT_RECORDS").getValue();

			Path countRecordsFilePath = PathBuilder.buildAsPath(ConfUtils.
				getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf), SynLexConstants.
				JOB_COUNTER_FOLDER, this.jobOutputFolder,
				ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf));
			DataAccessUtils.addFileDataToHDFS(conf, countRecordsFilePath, String.valueOf(countRecords));
		}
		
		return errorCode;
	}
}
