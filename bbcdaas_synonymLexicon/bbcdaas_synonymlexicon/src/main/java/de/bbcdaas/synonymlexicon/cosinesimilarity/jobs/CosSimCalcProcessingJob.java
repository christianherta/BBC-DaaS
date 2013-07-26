package de.bbcdaas.synonymlexicon.cosinesimilarity.jobs;

import de.bbcdaas.synonymlexicon.common.constants.SynLexConstants;
import de.bbcdaas.synonymlexicon.common.jobs.AbstractJob;
import de.bbcdaas.synonymlexicon.common.mahout.used.RandomAccessSparseVector;
import de.bbcdaas.synonymlexicon.common.mahout.used.Vector;
import de.bbcdaas.synonymlexicon.common.mahout.used.VectorWritable;
import de.bbcdaas.synonymlexicon.utils.hadoop.ConfUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.DataAccessUtils;
import de.bbcdaas.synonymlexicon.utils.hadoop.JobBuilder;
import de.bbcdaas.synonymlexicon.utils.hadoop.PathBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;
import org.apache.log4j.Logger;

/**
 * Job 15
 * Calculates the cosine similarity of term pairs.
 * @author Robert Illers
 */
public class CosSimCalcProcessingJob extends AbstractJob {
	
	private static Logger log = Logger.getLogger(CosSimCalcProcessingJob.class);
	
	/**
	 * Constructor
	 * @param conf 
	 */
	public CosSimCalcProcessingJob(Configuration conf) {
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
		
		String outputDirBaseName;
		Configuration conf = getConf();
		
		try {
			outputDirBaseName = ConfUtils.getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, getConf());
		} catch(ConfigurationException ex) {
			log.error(ex.getMessage(), ex);
			return org.apache.hadoop.mapred.jobcontrol.Job.FAILED;
		}	
		
		// copy llrVectorMap to distributed cache
		DataAccessUtils.addFilePathToDistributedCache(PathBuilder.buildAsPath(outputDirBaseName,
			SynLexConstants.JOB_OUTPUT_DIR_11_LLRVECTORENTITIES), conf, "part");
		
		// input: job 14 output (ID1/ID2, all term combination (symetrically))
		Job job = JobBuilder.buildJob(this, conf, args);
		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setMapperClass(CosSimCalcProcessingMapper.class);
		job.setOutputKeyClass(DoubleWritable.class);
		job.setOutputValueClass(Text.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		
		int errorCode =  job.waitForCompletion(true) ?
			org.apache.hadoop.mapred.jobcontrol.Job.SUCCESS :
			org.apache.hadoop.mapred.jobcontrol.Job.FAILED;
		
		// write the number of input lines into a file
		long countRecords = job.getCounters().
			findCounter("org.apache.hadoop.mapreduce.TaskCounter", "MAP_INPUT_RECORDS").getValue();
		
		Path countRecordsFilePath = PathBuilder.buildAsPath(ConfUtils.
			getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf), SynLexConstants.
			JOB_COUNTER_FOLDER, SynLexConstants.JOB_OUTPUT_DIR_15_COSSIMILARITY,
			ConfUtils.getStringValue(ConfUtils.KEY_INPUT_RECORDS_COUNT_FILENAME, conf));
		DataAccessUtils.addFileDataToHDFS(conf, countRecordsFilePath, String.valueOf(countRecords));
		
		return errorCode;
	}
	
	/**
	 * Does a part of the cosine similarity calculation.
	 */
	public static class CosSimCalcProcessingMapper extends Mapper<LongWritable,
	   LongWritable, DoubleWritable, Text> {

		private List<MapFile.Reader> llrVectorEntityMapfileReaders = new ArrayList<MapFile.Reader>();
		private Map<Long, Double> termIDVectorNorms = new HashMap<Long, Double>();
		private float minCosSim;
		private String termIdDelimiter;
		
		/**
		 * Open file reader to llr vectors, cache vector norms and get configuration
		 * @param context mapper context
		 * @throws IOException
		 * @throws InterruptedException 
		 */
		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			
			List<MapFile.Reader> vectorNormMapfileReaders = new ArrayList<MapFile.Reader>();
			
			try {
				
				Configuration conf = context.getConfiguration();
				
				String outputDirBaseName = ConfUtils.getStringValue(ConfUtils.KEY_OUTPUT_DIR_BASE_NAME, conf);
				this.minCosSim = ConfUtils.getFloatValue(ConfUtils.KEY_MIN_COSINE_SIMILARITY, conf);
				this.termIdDelimiter = ConfUtils.getStringValue(ConfUtils.KEY_COSINE_SIMILARITY_KEY_DELIMITER, conf);
				
				Path llrVectorEntityMapfilePath = PathBuilder.buildAsPath(outputDirBaseName,
					SynLexConstants.JOB_OUTPUT_DIR_11_LLRVECTORENTITIES);
				
				List<Path> distCachePartPaths = DataAccessUtils.getFilePathsFromDistributedCache(context,
					llrVectorEntityMapfilePath.toString(), "part");
				
				if (!distCachePartPaths.isEmpty()) {
			
					// get from distributed cache
					for (Path distCachePartPath : distCachePartPaths) {
						this.llrVectorEntityMapfileReaders.addAll(DataAccessUtils.
							getMapFilePartReaders(distCachePartPath, conf));
					}
				} else {
			
					log.info("Files not found in distributed cache, getting from hdfs now (slower)");
					// get from hdfs if not in distributed cache
					this.llrVectorEntityMapfileReaders = DataAccessUtils.
						getMapFilePartReaders(llrVectorEntityMapfilePath, conf);
				}
				
				Path vectorNormMapfilePath = PathBuilder.buildAsPath(outputDirBaseName,
					SynLexConstants.JOB_OUTPUT_DIR_13_TERMVECTORNORMS);
				
				vectorNormMapfileReaders = DataAccessUtils.
					getMapFilePartReaders(vectorNormMapfilePath, conf);
			
				LongWritable termID = new LongWritable();
				DoubleWritable vectorNorm = new DoubleWritable();
				
				// cache vector norms
				for (MapFile.Reader reader : vectorNormMapfileReaders) {
					while(reader.next(termID, vectorNorm)) {
						this.termIDVectorNorms.put(termID.get(), vectorNorm.get());
					}
				}
				
			} catch(ConfigurationException cex) {
				throw new IOException(cex);
			} finally {
				for (MapFile.Reader reader : vectorNormMapfileReaders) {
					IOUtils.closeStream(reader);
				}
			}
		}
		
		/**
		 * Does a part of the cosine similarity calculation
		 * @param termID1 smaller id of termID - pair
		 * @param termID2 bigger id of termID - pair
		 * @param context Mapper context
		 * @throws IOException
		 * @throws InterruptedException 
		 */
		@Override
		protected void map(LongWritable termID1, LongWritable termID2, Context context) throws IOException, InterruptedException {
			
			VectorWritable t1Vector = new VectorWritable();
			VectorWritable t2Vector = new VectorWritable();
			
			// get llr vector for term 1
			MapFile.Reader reader = DataAccessUtils.getMapFilePartReaderByKey(this.
				llrVectorEntityMapfileReaders, termID1, t1Vector, new HashPartitioner());
			reader.get(termID1, t1Vector);
			
			// get llr vector for term 2
			reader = DataAccessUtils.getMapFilePartReaderByKey(this.
				llrVectorEntityMapfileReaders, termID2, t2Vector, new HashPartitioner());
			reader.get(termID2, t2Vector);
			
			RandomAccessSparseVector term1Vector = (RandomAccessSparseVector)t1Vector.get();
			RandomAccessSparseVector term2Vector = (RandomAccessSparseVector)t2Vector.get();
			
			Double scalarProd = 0.0;
			
			Iterator<Vector.Element> term1VectorIterator = term1Vector.iterateNonZero();
			
			// calculate scalar product
			while(term1VectorIterator.hasNext()) {

				Vector.Element vecPair = term1VectorIterator.next();
				Double llr1 = vecPair.get();
				Double llr2 = term2Vector.getQuick(vecPair.index());
				scalarProd += (llr1 * llr2);
			}
			
			// calculate consine similarity
			Double similarity = scalarProd / (this.termIDVectorNorms.
				get(termID1.get()) * this.termIDVectorNorms.get(termID2.get()));
			
			// write the result (minCosSim could be set to a value higher than 0)
			if (similarity >= this.minCosSim) {
				
				Text cosSimTermPairIDs = new Text(new StringBuilder().append(termID1.get()).
					append(this.termIdDelimiter).append(termID2.get()).toString());
				
				context.write(new DoubleWritable(similarity), cosSimTermPairIDs);
			}
		}

		/**
		 * Close llr vector norm reader
		 * @param context Mapper context
		 * @throws IOException
		 * @throws InterruptedException 
		 */
		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {
			
			for (MapFile.Reader reader : this.llrVectorEntityMapfileReaders) {
				IOUtils.closeStream(reader);
			}
		}
	}
}