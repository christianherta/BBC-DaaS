package de.bbcdaas.synonymlexicon.utils.hadoop;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.log4j.Logger;

/**
 * Factory for Job instances.
 * @author Robert Illers
 */
public final class JobBuilder {

	private static Logger logger = Logger.getLogger(JobBuilder.class);
	
	/**
	 * Creates a job instance and sets the tool class, the input and the output
	 * path int the job.
	 * @param tool
	 * @param conf
	 * @param args
	 * @return Job
	 * @throws IOException
	 */
	public static Job buildJob(Tool tool, Configuration conf,
		String[] args) throws IOException {

		if (args.length < 2) {
		  printUsage(tool, "<input> <output> <optional parameter>");
		  return null;
		}
		Job job = Job.getInstance(conf);
		job.setJarByClass(tool.getClass());
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		return job;
	}

	/**
	 *
	 * @param tool
	 * @param extraArgsUsage
	 */
	private static void printUsage(Tool tool, String extraArgsUsage) {

		logger.info("Usage: "+tool.getClass().getSimpleName()+" [genericOptions] "+extraArgsUsage);
		GenericOptionsParser.printGenericCommandUsage(System.err);
	}
}
