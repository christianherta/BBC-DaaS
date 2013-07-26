package de.bbcdaas.synonymlexicon.utils.hadoop;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;
/**
 * Utilities for getting configuration parameters from the hadoop configuration.
 * @author Christian Herta
 * @author Frithjof Schulte
 * @author Robert Illers
 */
public class ConfUtils  {

	private static Logger logger = Logger.getLogger(ConfUtils.class);

	// 1) hdfs working directory
	public static final String KEY_OUTPUT_DIR_BASE_NAME = "outputDirBaseName";
	// 2) local result output directory
	public static final String KEY_OUTPUT_LOCAL_DIR_NAME = "outputLocalDirName";
	// 3) String separating two tags
	public static final String KEY_TAG_DELIMITER = "tagDelimiter";
	// 4) min frequency of terms in term lexiconfor being used in later computation
	public static final String KEY_MIN_TERM_FREQUENCY = "minTermFrequency";
	// 5)
	public static final String KEY_MIN_TERMS_PER_ENTITY = "minTermsPerEntity";
	// 6)
	public static final String KEY_TERM_LEXICA_NUM_TERMS_IN_MEMORY = "termLexica.numTermsInMemory";
	// 7)
	public static final String KEY_WORDCOUNT_NUM_REDUCE_TASKS = "termLexica.wordCount.numReduceTask";
	// 8)
	public static final String KEY_TERM_LEXICA_SORT_LEXICON_JOB_NUM_REDUCE_TASK = "termLexica.sortLexicon.numReduceTask";
	// 9)
	public static final String KEY_INPUT_RECORDS_COUNT_FILENAME = "inputRecords.countFileName";
	// 10)
	public static final String KEY_COSINE_SIMILARITY_KEY_DELIMITER = "cosine.similarity.keyDelimiter";
	// 11)
	public static final String KEY_ENTITY_TO_TAGCLOUD_DELIMITER = "entityToTagCloudDelimiter";
	// 12)
	public static final String KEY_TAGCLOUD_INPUT_FORMAT = "tagCloudInputFormat";
	// 13) min value for the cosine similarity, lesser result values will not be written to disk
	public static final String KEY_MIN_COSINE_SIMILARITY = "minCosineSimilarity";
	// 14)
	public static final String KEY_MAX_NUMBER_OF_TOTAL_TERMS = "maxNumberOfTotalTerms";
	// 15)
	public static final String KEY_MIN_TERM_LENGTH = "minTermLength";
	// 16)
	public static final String KEY_MAX_TERM_LENGTH = "maxTermLength";
	
	/**
	 *
	 */
	private static Configuration DEFAULT = getDefaultConf();
    private static Configuration getDefaultConf() {

        Configuration defaultConf = new Configuration();

		// No slashes please at begin and end of paths! (general reuseability rule)

		// 1)
		defaultConf.set(KEY_OUTPUT_DIR_BASE_NAME, "output");
		// 2)
		defaultConf.set(KEY_OUTPUT_LOCAL_DIR_NAME, "output");
		// 3)
		defaultConf.set(KEY_TAG_DELIMITER, ";");
		// 4)
		defaultConf.setInt(KEY_MIN_TERM_FREQUENCY, 50);
		// 5)
		defaultConf.setInt(KEY_MIN_TERMS_PER_ENTITY, 2);
		// 6)
		defaultConf.setInt(KEY_TERM_LEXICA_NUM_TERMS_IN_MEMORY, 10000);
		// 7)
		defaultConf.setInt(KEY_WORDCOUNT_NUM_REDUCE_TASKS, 4);
		// 8)
		defaultConf.setInt(KEY_TERM_LEXICA_SORT_LEXICON_JOB_NUM_REDUCE_TASK, 1);
		// 9)
		defaultConf.set(KEY_INPUT_RECORDS_COUNT_FILENAME, "countInputRecords.txt");
		// 10)
		defaultConf.set(KEY_COSINE_SIMILARITY_KEY_DELIMITER, ":");
		// 11)
		defaultConf.set(KEY_ENTITY_TO_TAGCLOUD_DELIMITER, "::");
		// 12)
		defaultConf.set(KEY_TAGCLOUD_INPUT_FORMAT, "AUTODETECT");
		// 13)
		defaultConf.setFloat(KEY_MIN_COSINE_SIMILARITY, 0.5f);
		// 14)
		defaultConf.setInt(KEY_MAX_NUMBER_OF_TOTAL_TERMS, 10000);
		// 15)
		defaultConf.setInt(KEY_MIN_TERM_LENGTH, 1);
		// 16)
		defaultConf.setInt(KEY_MAX_TERM_LENGTH, 30);
		
        return defaultConf;
    }

	/**
	 *
	 * @param confParam
	 * @param conf
	 * @return
	 * @throws ConfigurationException
	 */
	public static String getStringValue(String confKey, Configuration conf)
		throws ConfigurationException {

		String confValue = null;

		try {
			confValue = conf.get(confKey, DEFAULT.get(confKey));
		} catch(NullPointerException nex) {
			logger.error("One or more of the wanted config keys does not exist!", nex);
		}

		if (confValue == null) {
			throw new ConfigurationException(confKey + " not configured!");
		}

		return confValue;
	}

	/**
	 *
	 * @param confParam
	 * @param conf
	 * @return
	 * @throws ConfigurationException
	 */
	public static int getIntValue(String confKey, Configuration conf)
		throws ConfigurationException {

		int confValue = 0;

		try {
			confValue = conf.getInt(confKey, DEFAULT.getInt(confKey, 0));
		} catch(NullPointerException nex) {
			logger.error("One or more of the wanted config keys does not exist!", nex);
		}
		return confValue;
	}
	
	/**
	 * 
	 * @param confParam
	 * @param conf
	 * @return
	 * @throws ConfigurationException 
	 */
	public static float getFloatValue(String confKey, Configuration conf)
		throws ConfigurationException {
		
		float confValue = 0.0f;
		
		try {
			confValue = conf.getFloat(confKey, DEFAULT.getFloat(confKey, 0.0f));
		} catch(NullPointerException nex) {
			logger.error("One or more of the wanted config keys does not exist!", nex);
		}
		return confValue;
	}

	/**
	 *
	 * @param conf
	 */
	public static void printConfiguration(Configuration conf) {

		logger.info("Configuration:");
		// add config to map for sorted output
		Map<String, String> map = new TreeMap<String, String>();

		for(Entry<String, String> entry : conf) {
			map.put(entry.getKey(), entry.getValue());
		}

		for(Entry<String, String> entry : map.entrySet()) {
			logger.info(entry.getKey()+"="+entry.getValue());
		}
	}
}
