package de.bbcdaas.synonymlexicon.termlexicon.jobs;

import de.bbcdaas.synonymlexicon.common.constants.SynLexConstants;
import de.bbcdaas.synonymlexicon.common.jobs.AbstractJob;
import de.bbcdaas.synonymlexicon.utils.common.TagCloudTokenizerFactory;
import de.bbcdaas.synonymlexicon.utils.hadoop.ConfUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.DataAccessUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.JobBuilder;
import de.bbcdaas.synonymlexicon.utils.hadoop.PathBuilder;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
/**
 * Job 1
 * mapper reads terms from an input file (CSV) and counts how often terms occur
 * reducer creates from the mappers term/count maps a single count/term map 
 * @author Christian Herta
 * @author Robert Illers
 */
public class WordCountJob extends AbstractJob {
	
	public WordCountJob(Configuration conf) {
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
		job.setMapperClass(InMapperCombiningWordCountMapper.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);
		job.setCombinerClass(WordCountReducer.class);
		job.setReducerClass(WordCountInverseReducer.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);
		job.setNumReduceTasks(ConfUtils.getIntValue(ConfUtils.
			KEY_WORDCOUNT_NUM_REDUCE_TASKS, conf));
		
		int errorCode = job.waitForCompletion(true) ?
			org.apache.hadoop.mapred.jobcontrol.Job.SUCCESS :
			org.apache.hadoop.mapred.jobcontrol.Job.FAILED;
		
		// write the number of input lines into a file
		long countRecords = job.getCounters().
			findCounter("org.apache.hadoop.mapreduce.TaskCounter", "MAP_INPUT_RECORDS").getValue();
		
		Path countRecordsFilePath = PathBuilder.buildAsPath(ConfUtils.
			getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf), SynLexConstants.
			JOB_COUNTER_FOLDER, SynLexConstants.JOB_OUTPUT_DIR_1_WORDCOUNT,
			ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf));
		DataAccessUtils.addFileDataToHDFS(conf, countRecordsFilePath, String.valueOf(countRecords));
		
		return errorCode;
	}
	
	/**
	* Reads tag clouds from a csv file and stores them with its occurrence count into
	* the mapper context.
	* @author Christian Herta
	*/
   public static class InMapperCombiningWordCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

	   private TagCloudTokenizerFactory.TAGCLOUD_INPUT_FORMAT tagCloudInputFormat;
	   private String tagDelimiter;
	   private String entityToTagCloudDelimiter;
	   private Map<String, Long> localLexica = new HashMap<String, Long>();
	   private int writeStepSize = 10000;

	   /**
		* Reads the configuration for the term delimiter in the incoming csv file.
		* @param context
		* @throws IOException
		* @throws InterruptedException
		*/
	   @Override
	   public void setup(Context context) throws IOException, InterruptedException {

		   Configuration conf = context.getConfiguration();
		   try {

			   this.tagCloudInputFormat = TagCloudTokenizerFactory.TAGCLOUD_INPUT_FORMAT.valueOf(ConfUtils.
				   getStringValue(ConfUtils.KEY_TAGCLOUD_INPUT_FORMAT, conf));

			   this.tagDelimiter = ConfUtils.
				   getStringValue(ConfUtils.KEY_TAG_DELIMITER, conf);

			   this.entityToTagCloudDelimiter = ConfUtils.
				   getStringValue(ConfUtils.KEY_ENTITY_TO_TAGCLOUD_DELIMITER, conf);

		   } catch(ConfigurationException cex) {
			   throw new IOException("Error while getting mapper configuration", cex);
		   }

	   }

	   /**
		* Reads a tagcloud (one line) from a csv file and stores the term with its
		* occurrences in a map.
		* @param key not used
		* @param value csv line
		* @param context mapper context
		* @throws IOException
		* @throws InterruptedException
		*/
	   @Override
	   public void map(LongWritable offset, Text tagCloud, Context context) throws
		   IOException, InterruptedException {

		   StringTokenizer tokenizer = TagCloudTokenizerFactory.getStringTokenizer(this.tagCloudInputFormat,
			   tagCloud.toString(), this.tagDelimiter, this.entityToTagCloudDelimiter);

		   if (tokenizer != null) {
				
				while (tokenizer.hasMoreTokens()) {

					String term = tokenizer.nextToken();

					String cleanedTerm = TermCleaner.clean(term);
					
					if (!cleanedTerm.isEmpty()) {
					
						// add term or raise count
						this.localLexica.put(cleanedTerm, this.localLexica.containsKey(cleanedTerm) ?
							this.localLexica.get(cleanedTerm) + 1 : 1);
					}
				}
		   } else {
			   throw new IOException("could not get tokenizer");
		   }

		   if (this.localLexica.size() > this.writeStepSize) {
			   this.writeContext(context);
		   }
	   }
	   
	   /**
		* Stores the remaining term counts into the mappers context. 
		* @param context mapper context
		* @throws IOException
		* @throws InterruptedException
		*/
	   @Override
	   public void cleanup(Context context) throws IOException, InterruptedException {
		   this.writeContext(context);
	   }
	   
	   /**
		* Stores the term and its count from the localLexica map into the mapper context.
		* @param context mapper context
		* @throws IOException
		* @throws InterruptedException
		*/
	   private void writeContext(Context context) throws IOException, InterruptedException {

			   for (Map.Entry<String, Long> entry : this.localLexica.entrySet()) {
				   context.write(new Text(entry.getKey()), new LongWritable(entry.getValue()));
			   }
			   this.localLexica.clear();
	   }
   }
   
   /**
	* Takes all Term/Count maps and reduces them to a single count/term map.
	* @author Christian Herta
	*/
   public static class WordCountInverseReducer extends Reducer<Text, LongWritable, LongWritable, Text> {

	   /**
		* Takes all term/count maps and reduces them to a single count/term map.
		* @param key term
		* @param values all term counts
		* @param context reducer context
		* @throws IOException
		* @throws InterruptedException 
		*/
	   @Override
	   public void reduce(Text term, Iterable<LongWritable> counts, Context context)
		   throws IOException, InterruptedException {

		   int countSum = 0;
		   for (LongWritable count : counts) {
			   countSum += count.get();
		   }
		   context.write(new LongWritable(countSum), term);
	   }
   }
   
   /**
	* use the new API
	* could be used as a Combiner
	* @author Christian Herta
	*/
   public static class WordCountReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

	   /**
		* 
		* @param term
		* @param counts
		* @param context
		* @throws IOException
		* @throws InterruptedException 
		*/
	   @Override
	   public void reduce(Text term, Iterable<LongWritable> counts, Context context)
		   throws IOException, InterruptedException {

		   int countSum = 0;
		   for (LongWritable count : counts) {
			   countSum += count.get();
		   }
		   context.write(term, new LongWritable(countSum));
	   }
   }
   
   /**
	 * 
	 */
	public static class TermCleaner {
		
		private final static String[] umlautList = {"ä", "ö", "ü", "ß"};
		private final static String[] replaceList = {"ae", "oe", "ue", "ss"};
		
		/**
		 * 
		 * @param term
		 * @return 
		 */
		public static String clean(String term) {
			
			String cleanedTerm = null;
			
			if (term != null) {
				
				// Step 1: transform to lowercase
				cleanedTerm = term.toLowerCase();
				
				// Step 2: remove all more than 3 times repeating characters
				cleanedTerm = cleanedTerm.replaceAll("[.]{4,}", "");

				// Step 3: remove some forbidden strings
				cleanedTerm = cleanedTerm.replaceAll("[\"?:!'|=()]", "");
				cleanedTerm = cleanedTerm.replaceAll("[\\.]{2,}", "");
				
				// Step 4: trim whitespaces
				cleanedTerm = cleanedTerm.trim();
				
				// Step 5: replace umlauts
				cleanedTerm = StringUtils.replaceEachRepeatedly(cleanedTerm,
					umlautList, replaceList);
			}
			return cleanedTerm;
		}
	}
}
