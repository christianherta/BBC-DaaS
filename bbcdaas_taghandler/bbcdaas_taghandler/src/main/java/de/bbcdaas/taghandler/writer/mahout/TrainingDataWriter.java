package de.bbcdaas.taghandler.writer.mahout;

/**
 * Interface for a writer that creates files that can be used for mahout import.
 * @author Robert Illers
 */
public interface TrainingDataWriter {
	
	/**
	 * Creates a file that contains a list of terms with its ids sorted by frequency
	 * @param outputPath
	 * @param numberOfTerms
	 * @return dictionaryID
	 */
	public int createTermDictionary(String outputPath, int numberOfTerms);
	
	/**
	 * Creates a traing file for mahout that contains the termIds of termClouds. Each line represents a termCloud.
	 * @param outputPath
	 * @param dictionaryID
	 * @param minMatchingTerms 
	 */
	public void createTermCloudTrainingFile(String outputPath, int dictionaryID, int minMatchingTerms);
	
}
