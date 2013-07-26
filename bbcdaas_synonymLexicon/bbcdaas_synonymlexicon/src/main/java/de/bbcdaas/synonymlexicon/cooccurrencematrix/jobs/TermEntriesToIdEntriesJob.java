package de.bbcdaas.synonymlexicon.cooccurrencematrix.jobs;

import de.bbcdaas.synonymlexicon.common.beans.IdCountPair;
import de.bbcdaas.synonymlexicon.common.constants.SynLexConstants;
import de.bbcdaas.synonymlexicon.common.jobs.AbstractJob;
import de.bbcdaas.synonymlexicon.common.mahout.used.RandomAccessSparseVector;
import de.bbcdaas.synonymlexicon.common.mahout.used.Vector;
import de.bbcdaas.synonymlexicon.common.mahout.used.VectorWritable;
import de.bbcdaas.synonymlexicon.termlexicon.jobs.WordCountJob;
import de.bbcdaas.synonymlexicon.utils.common.TagCloudTokenizerFactory;
import de.bbcdaas.synonymlexicon.utils.hadoop.ConfUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.DataAccessUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.JobBuilder;
import de.bbcdaas.synonymlexicon.utils.hadoop.PathBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;

/**
 * Job 9
 * Creates the coocurrence matrix. Reads tag clouds from the input file, sets
 * the ids from the term lexicon for each tag cloud term, and stores the
 * term occurrences.
 * @author Christian Herta
 * @author Frithjof Schulte
 */
public class TermEntriesToIdEntriesJob extends AbstractJob {

	public TermEntriesToIdEntriesJob(Configuration conf) {
		super(conf);
	}
	
	/**
	 *
	 * @param args input/output
	 * @return errorCode
	 * @throws Exception
	 */
	@Override
	public int run(String[] args) throws Exception {

		Configuration conf = getConf();
		Job job = JobBuilder.buildJob(this, conf, args);
		job.setMapperClass(TermEntriesToIdEntriesMapper.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(VectorWritable.class);

		int result = job.waitForCompletion(true) ?
			org.apache.hadoop.mapred.jobcontrol.Job.SUCCESS :
			org.apache.hadoop.mapred.jobcontrol.Job.FAILED;

		// write the number of read tag clouds into a file
		long countRecords = job.getCounters().
			findCounter("org.apache.hadoop.mapreduce.TaskCounter", "MAP_INPUT_RECORDS").getValue();

		Path countRecordsFilePath = PathBuilder.buildAsPath(ConfUtils.
			getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf), SynLexConstants.
			JOB_COUNTER_FOLDER, SynLexConstants.JOB_OUTPUT_DIR_9_VECTORENTITIES,
			ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf));
		DataAccessUtils.addFileDataToHDFS(conf, countRecordsFilePath, String.valueOf(countRecords));

		return result;
	}
	
	/**
	 * 
	 */
	public static class TermEntriesToIdEntriesMapper extends Mapper<LongWritable, Text, LongWritable, VectorWritable> {

		private TagCloudTokenizerFactory.TAGCLOUD_INPUT_FORMAT tagCloudInputFormat;
		private String tagDelimiter;
		private String entityToTagCloudDelimiter;
		private int minTagCloudSize;
		private List<MapFile.Reader> termLexiconReaders;

		/**
		 *
		 * @param context
		 */
		@Override
		protected void setup(Mapper.Context context) throws IOException {

			try {

				Configuration conf = context.getConfiguration();
				String outputDirBaseName = ConfUtils.getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf);
				Path termLexiconPath = PathBuilder.buildAsPath(outputDirBaseName, SynLexConstants.JOB_OUTPUT_DIR_5_TERM2IDLEXICON);
				
				this.termLexiconReaders = DataAccessUtils.getMapFilePartReaders(termLexiconPath, conf);
				
				this.tagDelimiter = ConfUtils.getStringValue(ConfUtils.KEY_TAG_DELIMITER, conf);
				this.entityToTagCloudDelimiter = ConfUtils.getStringValue(ConfUtils.KEY_ENTITY_TO_TAGCLOUD_DELIMITER, conf);
				this.tagCloudInputFormat = TagCloudTokenizerFactory.TAGCLOUD_INPUT_FORMAT.valueOf(ConfUtils.getStringValue(ConfUtils.KEY_TAGCLOUD_INPUT_FORMAT, conf));
				this.minTagCloudSize = ConfUtils.getIntValue(ConfUtils.KEY_MIN_TERMS_PER_ENTITY, conf);

			} catch(ConfigurationException cex) {
				throw new IOException("Some needed config keys not configured", cex);
			}
		}

	   /**
		* Creates an entity vector for each tagCloud in a text file.
		* @param tagCloudKey
		* @param tagCloud
		* @param context
		* @throws IOException
		* @throws InterruptedException
		*/
	   @Override
	   public void map(LongWritable offset, Text tagCloud, Mapper.Context context)
		   throws IOException, InterruptedException {

		   StringTokenizer tokenizer = TagCloudTokenizerFactory.getStringTokenizer(this.tagCloudInputFormat,
				   tagCloud.toString(), this.tagDelimiter, this.entityToTagCloudDelimiter);

		   Vector entityVector = new RandomAccessSparseVector(Integer.MAX_VALUE, 10);

		   List<Long> tagCloudTermIDs = new ArrayList<Long>();
		   
		   while(tokenizer.hasMoreTokens()) {

			   String term = tokenizer.nextToken();
			   IdCountPair idCountPair = new IdCountPair();

			   String cleanedTerm = WordCountJob.TermCleaner.clean(term);
			   // try to get the terms id from lexicon
			   if(cleanedTerm.length() > 0) {

				   MapFile.Reader reader = DataAccessUtils.
						getMapFilePartReaderByKey(this.termLexiconReaders,
						new Text(cleanedTerm), idCountPair, new HashPartitioner());
				   
				   // term found in lexicon
				   if (reader.get(new Text(cleanedTerm), idCountPair) != null) {
					   tagCloudTermIDs.add(idCountPair.getTermId());
				   }
			   }
		   }
		   
		   if (tagCloudTermIDs.size() >= this.minTagCloudSize) {
				
			   for (Long termID : tagCloudTermIDs) {
					entityVector.set(termID.intValue(), 1.0f);
			   }
			   context.write(new LongWritable(1), new VectorWritable(entityVector));
		   }
	   }
   }
}
