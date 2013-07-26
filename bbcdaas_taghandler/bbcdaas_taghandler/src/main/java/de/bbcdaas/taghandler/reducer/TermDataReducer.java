package de.bbcdaas.taghandler.reducer;

/**
 * Interface for a term data reducer. After term data is read from a source by 
 * the entity reader, this tool can reduce the amount of data that is used
 * for further calculations. Each Reducer has its own set of parameters that can
 * be set in parameter.properties.
 * @author Robert Illers
 */
public interface TermDataReducer {
	
	/**
	 * Starts the reduce process.
	 */
	public void reduceTermData();
	
	/**
	 * 
	 * @return 
	 */
	public Integer getMinTermFrequency();
	
	/**
	 * 
	 * @return 
	 */
	public Integer getMinTermCount();
}