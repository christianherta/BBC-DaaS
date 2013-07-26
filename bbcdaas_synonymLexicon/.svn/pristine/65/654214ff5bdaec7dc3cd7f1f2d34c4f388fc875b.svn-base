package de.bbcdaas.synonymlexicon.loglikelihoodratio.jobs;

import de.bbcdaas.synonymlexicon.common.beans.LlrWeightingTermPair;
import de.bbcdaas.synonymlexicon.common.beans.TermCountPair;
import de.bbcdaas.synonymlexicon.common.constants.SynLexConstants;
import de.bbcdaas.synonymlexicon.common.jobs.AbstractJob;
import de.bbcdaas.synonymlexicon.common.mahout.used.RandomAccessSparseVector;
import de.bbcdaas.synonymlexicon.common.mahout.used.Vector;
import de.bbcdaas.synonymlexicon.common.mahout.used.VectorWritable;
import de.bbcdaas.synonymlexicon.common.partitioner.SolidBlockPartitioner;
import de.bbcdaas.synonymlexicon.utils.hadoop.ConfUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.DataAccessUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.JobBuilder;
import de.bbcdaas.synonymlexicon.utils.hadoop.PathBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.log4j.Logger;
/**
 * Job 9
 * Writes the llr weighting clouds as human readable text.
 * @author Frithjof Schulte
 */
public class LogLikelihoodTextOutputJob extends AbstractJob {

	public LogLikelihoodTextOutputJob(Configuration conf) {
		super(conf);
	}
	
	/**
	 * Executes the job.
	 * @param args input/output
	 * @return errorCode
	 * @throws Exception
	 */
	@Override
	public int run(String[] args) throws Exception {

		Configuration conf = getConf();
		Job job = JobBuilder.buildJob(this, conf, args);
		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setMapperClass(LogLikelihoodTextOutputMapper.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		int errorCode = job.waitForCompletion(true) ?
			org.apache.hadoop.mapred.jobcontrol.Job.SUCCESS :
			org.apache.hadoop.mapred.jobcontrol.Job.FAILED;
		
		// write the number of input lines into a file
		long countRecords = job.getCounters().
			findCounter("org.apache.hadoop.mapreduce.TaskCounter", "MAP_INPUT_RECORDS").getValue();
		
		Path countRecordsFilePath = PathBuilder.buildAsPath(ConfUtils.
			getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf), SynLexConstants.
			JOB_COUNTER_FOLDER, SynLexConstants.JOB_OUTPUT_DIR_12_LOGLIKELIHOODRATIO_TEXTOUTPUT,
			ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf));
		DataAccessUtils.addFileDataToHDFS(conf, countRecordsFilePath, String.valueOf(countRecords));
		
		return errorCode;
	}
	
	/**
	* Writes the llrWeighting term clouds as text output 
	* "term {term:llrWeighting,term:llrWeighting, ...}"
	* @author Frithjof Schulte
	* @author Robert Illers
	*/
   public static class LogLikelihoodTextOutputMapper extends Mapper<LongWritable,
	   VectorWritable, Text, Text> {

	   private static Logger logger = Logger.getLogger(LogLikelihoodTextOutputMapper.class);
	   private List<MapFile.Reader> id2TermMapfileReaders;
	   private long numberOfTerms;

	   /**
		* Initialize term lexicon reader and parameters.
		* @param context
		* @throws IOException
		*/
	   @Override
	   public void setup(Mapper.Context context) throws IOException {

		   Configuration conf = context.getConfiguration();

		   try {

			   String outputDirBaseName = ConfUtils.getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf);
			   Path id2TermPath = PathBuilder.buildAsPath(outputDirBaseName, SynLexConstants.JOB_OUTPUT_DIR_4_ID2TERMLEXICON);

			   this.id2TermMapfileReaders = DataAccessUtils.getMapFilePartReaders(id2TermPath, conf);
			   
			   // get the number of terms from the hdfs
			   String nbOfTerms = DataAccessUtils.getFileDataFromHDFS(conf,
				   PathBuilder.buildAsPath(outputDirBaseName, SynLexConstants.JOB_COUNTER_FOLDER,
				   SynLexConstants.JOB_OUTPUT_DIR_3_COUNTSORT, 
				   ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf)));
			   this.numberOfTerms = Long.parseLong(nbOfTerms);

		   } catch(ConfigurationException cex) {
			   logger.error("Some needed configKey not configured!", cex);
		   }
	   }

	   /**
		* Close opened reader to term lexicon.
		* @param context
		* @throws IOException
		* @throws InterruptedException
		*/
	   @Override
	   public void cleanup(Mapper.Context context) throws IOException, InterruptedException {
		   
		   for (MapFile.Reader reader : this.id2TermMapfileReaders) {
				IOUtils.closeStream(reader);
		   }
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
	   public void map(LongWritable key, VectorWritable value, Mapper.Context context)
		   throws IOException, InterruptedException {

		   TermCountPair termCountPair = new TermCountPair();
		   StringBuilder llrVectorRepresentation = new StringBuilder("{");

		   RandomAccessSparseVector llrVector = (RandomAccessSparseVector)value.get();
		   Iterator<Vector.Element> llrVectorIterator = llrVector.iterateNonZero();

		   // for each vector term write term:llrWeighting pair
		   List<LlrWeightingTermPair> llrWeightingTermPairs = new ArrayList<LlrWeightingTermPair>();
		   while (llrVectorIterator.hasNext()) {

			   Vector.Element vectorTuple = llrVectorIterator.next();

			   // get vector term value by its id
			   MapFile.Reader reader = DataAccessUtils.getMapFilePartReaderByKey(this.id2TermMapfileReaders,
					new LongWritable(vectorTuple.index()), termCountPair, new SolidBlockPartitioner(this.numberOfTerms));
			   reader.get(new LongWritable(vectorTuple.index()), termCountPair);

			   LlrWeightingTermPair llrWeightingTermPair = new LlrWeightingTermPair(vectorTuple.get(), termCountPair.getTerm().toString());
			   llrWeightingTermPairs.add(llrWeightingTermPair);
		   }
		   // sort by llrWeighting in descending order
		   Collections.sort(llrWeightingTermPairs, Collections.reverseOrder());

		   // {termName: llrWeighting, termName: llrWeighting, ...}
		   for (LlrWeightingTermPair pair :  llrWeightingTermPairs) {

			   llrVectorRepresentation.append(pair.getTerm()).append(":").
				   append(pair.getLlrWeighting()).append(",");
		   }

		   // remove last "," separator (or first "{" if empty, will have no effect)
		   llrVectorRepresentation.delete(llrVectorRepresentation.length()-1,
			   llrVectorRepresentation.length());

		   llrVectorRepresentation.append("}");

		   // vector not empty -> write ("", "{", "{}", "{,}", "{X,}", "{,X}" == empty)
		   if (llrVectorRepresentation.length() > 4) {

			   // get current term value from term lexicon
			   MapFile.Reader reader = DataAccessUtils.getMapFilePartReaderByKey(this.id2TermMapfileReaders,
					new LongWritable(key.get()), termCountPair, new SolidBlockPartitioner(this.numberOfTerms));
			   reader.get(new LongWritable(key.get()), termCountPair);
			   Text keyTerm = new Text(termCountPair.getTerm());

			   context.write(keyTerm, new Text(llrVectorRepresentation.toString()));
		   }
	   }
   }
}