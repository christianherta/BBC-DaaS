package de.bbcdaas.taghandler.cleaner;

import java.util.List;
import de.bbcdaas.common.beans.entity.Entity;
/**
 * Interface for the cleansing module of the taghandler. 
 * @author Robert Illers
 */
public interface EntityCleaner {
	
	/**
	 * The Number of terms that should be at last in a field.
	 * @param minTermCount 
	 */
	public void setMinTermCount(int minTermCount);
	
    /**
     * The Number of terms that should be at last in a field.
     * @return minTermClount
     */
    public int getMinTermCount();
    
	/**
	 * The number of characters a term should be have at last.
	 * @param minTermLength 
	 */
	public void setMinTermLength(int minTermLength);
	
    /**
     * The number of characters a term should be have at last.
     * @return minTermLenght
     */
    public int getMinTermLength();
    
	/**
	 * The number of words a term can have. A word is a number of characters
	 * who are not divided by whitespaces.
	 * @param maxWordsInTerm 
	 */
	public void setMaxWordsInTerm(int maxWordsInTerm);
	
    /**
     * The number of words a term can have. A word is a number of characters
	 * who are not divided by whitespaces.
     * @return maxWordsInTerm 
     */
    public int getMaxWordsInTerm();
    
	/**
	 * The number of characters a word in a term can have at last.
	 * @param maxWordLength 
	 */
	public void setMaxWordLength(int maxWordLength);
	
    /**
     * The number of characters a word in a term can have at last.
     * @return maxWordLength
     */
    public int getMaxWordLength();
    
	/**
	 * A List of Strings that should not appear in the term value, if it does,
	 * the term will be ignored.
	 * @param forbiddenStrings 
	 */
	public void setForbiddenStrings(String forbiddenStrings);
	
    /**
     * A List of Strings that should not appear in the term value, if it does,
	 * the term will be ignored.
     * @return forbiddenStrings 
     */
    public List<String> getForbiddenStringsAsList();
    
	/**
	 * Starts the cleaning process.
	 * @param rawEntities
	 * @return List<Entity>
	 */
	public List<Entity> clean(List<Entity> rawEntities);
}
