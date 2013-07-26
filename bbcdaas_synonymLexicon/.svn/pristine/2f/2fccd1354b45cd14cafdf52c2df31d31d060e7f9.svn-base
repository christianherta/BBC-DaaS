package de.bbcdaas.synonymlexicon.termlexicon.jobs;

import de.bbcdaas.synonymlexicon.common.beans.RowNumberWritable;
import de.bbcdaas.synonymlexicon.common.beans.TermCountPair;
import de.bbcdaas.synonymlexicon.common.constants.SynLexConstants;
import de.bbcdaas.synonymlexicon.common.jobs.AbstractJob;
import de.bbcdaas.synonymlexicon.common.partitioner.RowNumberPartitioner;
import de.bbcdaas.synonymlexicon.utils.hadoop.ConfUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.DataAccessUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.PathBuilder;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MapFileOutputFormat;

/**
 * Code from: http://waredingen.nl/monotonically-increasing-row-ids-with-mapredu
 * Map reduce implementation for entry id generation.
 * Each term read by the wordCount job and cleaned by the wordFilter job gets an ID.
 * @author Friso van Vollenhoven
 * @author Robert Illers
 */
public class IdTermLexiconWriteJob extends AbstractJob {
	
	public IdTermLexiconWriteJob(Configuration conf) {
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

		Configuration conf =  getConf();
		Job job = Job.getInstance(conf);
		job.setJarByClass(IdTermLexiconWriteJob.class);
		job.setGroupingComparatorClass(IndifferentComparator.class);
		job.setPartitionerClass(RowNumberPartitioner.class);
		job.setMapperClass(RowNumberMapper.class);
		job.setMapOutputKeyClass(ByteWritable.class);
		job.setMapOutputValueClass(RowNumberWritable.class);
		job.setReducerClass(RowNumberReducer.class);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(TermCountPair.class);
		job.setOutputFormatClass(MapFileOutputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setInputFormatClass(SequenceFileInputFormat.class);

		int errorCode = job.waitForCompletion(true) ?
			org.apache.hadoop.mapred.jobcontrol.Job.SUCCESS :
			org.apache.hadoop.mapred.jobcontrol.Job.FAILED;

		// write the number of input lines into a file
		long countRecords = job.getCounters().
			findCounter("org.apache.hadoop.mapreduce.TaskCounter", "MAP_INPUT_RECORDS").getValue();
		
		Path countRecordsFilePath = PathBuilder.buildAsPath(ConfUtils.
			getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf), SynLexConstants.
			JOB_COUNTER_FOLDER, SynLexConstants.JOB_OUTPUT_DIR_4_ID2TERMLEXICON,
			ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf));
		DataAccessUtils.addFileDataToHDFS(conf, countRecordsFilePath, String.valueOf(countRecords));
		
		return errorCode;
	}

	/**
	 * 
	 */
	static class RowNumberMapper extends Mapper<LongWritable, Text, ByteWritable, RowNumberWritable> {
	
		private long[] counters;
		private int numReduceTasks;

		private RowNumberWritable outputValue = new RowNumberWritable();
		private ByteWritable outputKey = new ByteWritable();

		/**
		 * 
		 * @param context
		 * @throws IOException
		 * @throws InterruptedException 
		 */
		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			
			this.numReduceTasks = context.getNumReduceTasks();
			this.counters = new long[this.numReduceTasks];
			this.outputKey.set(RowNumberWritable.VALUE_MARKER);
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
			
			this.outputValue.setValue(new Text(new StringBuilder().
				append(term.toString()).append(":::").append(Long.toString(count.get())).toString()));
			// id term:::count
			context.write(this.outputKey, this.outputValue);
			this.counters[RowNumberPartitioner.partitionForValue(this.outputValue, this.numReduceTasks)]++;
		}

		/**
		 * 
		 * @param context
		 * @throws IOException
		 * @throws InterruptedException 
		 */
		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {
		
			this.outputKey.set(RowNumberWritable.COUNTER_MARKER);
			for(int c = 0; c < this.counters.length - 1; c++) {
				if (counters[c] > 0) {
					
					this.outputValue.setCounter(c + 1, this.counters[c]);
					context.write(this.outputKey, this.outputValue);
				}
				this.counters[c + 1] += this.counters[c];
			}
		}
	}

	/**
	 * 
	 */
	static class RowNumberReducer extends Reducer<ByteWritable, RowNumberWritable, LongWritable, TermCountPair> {

		private LongWritable outputKey = new LongWritable();

		/**
		 * 
		 * @param context
		 * @throws IOException
		 * @throws InterruptedException 
		 */
		@Override
		protected void setup(Context context) throws IOException, InterruptedException {}

		/**
		 * 
		 * @param key
		 * @param values
		 * @param context
		 * @throws IOException
		 * @throws InterruptedException 
		 */
		@Override
		protected void reduce(ByteWritable key, Iterable<RowNumberWritable> values,
			Context context) throws IOException, InterruptedException {
		
			Iterator<RowNumberWritable> itr = values.iterator();
			
			if (!itr.hasNext()) {
				return;
			}

			long offset = 1;
			RowNumberWritable value = itr.next();
			
			while (itr.hasNext() && value.getCount() > 0) {
				
				offset += value.getCount();
				value = itr.next();
			}
			
			this.outputKey.set(offset++);
			TermCountPair termCountPair = new TermCountPair();
			String termCount = value.getValue().toString();
			StringTokenizer tokenizer = new StringTokenizer(termCount, ":::");
			String term = tokenizer.nextToken();
			Long count = Long.parseLong(tokenizer.nextToken());
			termCountPair.set(new Text(term), count);
	
			context.write(this.outputKey, termCountPair);

			while(itr.hasNext()) {
			
				this.outputKey.set(offset++);
				termCount = itr.next().getValue().toString();
				tokenizer = new StringTokenizer(termCount, ":::");
				term = tokenizer.nextToken();
				count = Long.parseLong(tokenizer.nextToken());
				termCountPair.set(new Text(term), count);
				
				context.write(this.outputKey, termCountPair);
			}
		}
	}

	/**
	 * 
	 */
	public static class IndifferentComparator implements RawComparator<ByteWritable> {
		
		/**
		 * 
		 * @param left
		 * @param right
		 * @return 
		 */
		@Override
		public int compare(ByteWritable left, ByteWritable right) {
			return 0;
		}

		/**
		 * 
		 * @param b1
		 * @param s1
		 * @param l1
		 * @param b2
		 * @param s2
		 * @param l2
		 * @return 
		 */
		@Override
		public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
			return 0;
		}
	}
}