package de.bbcdaas.taghandler.cleaner;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.common.beans.entity.TermCloudField;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
/**
 * Simple implementation of en EntityCleaner.
 * @author Christian Herta
 * @author Robert Illers
 */
public final class SimpleEntityCleaner implements EntityCleaner {

	private int MIN_TERM_COUNT = 2;
	private int MIN_TERM_LENGTH = 1;
	private int MAX_WORDS_IN_TERM = 2;
	private int MAX_WORD_LENGTH = 30;
	private List<String> FORBIDDEN_STRINGS = new ArrayList<String>();
	
	/**
	 * The Number of terms that should be at last in a field.
	 * @param minTermCount 
	 */
	@Override
	public void setMinTermCount(int minTermCount) {
		MIN_TERM_COUNT = minTermCount;
	}
	
    /**
     * The Number of terms that should be at last in a field.
     * @return minTermCount
     */
	@Override
    public int getMinTermCount() {
        return this.MIN_TERM_COUNT;
    }
    
	/**
	 * The number of characters a term should be have at last.
	 * @param minTermLength 
	 */
	@Override
	public void setMinTermLength(int minTermLength) {
		MIN_TERM_LENGTH = minTermLength;
	}
	
    /**
     * The number of characters a term should be have at last.
     * @return minTermLenght
     */
	@Override
    public int getMinTermLength() {
        return this.MIN_TERM_LENGTH;
    }
    
	/**
	 * The number of words a term can have. A word is a number of characters
	 * who are not divided by whitespaces.
	 * @param maxWordsInTerm 
	 */
	@Override
	public void setMaxWordsInTerm(int maxWordsInTerm) {
		MAX_WORDS_IN_TERM = maxWordsInTerm;
	}
	
    /**
     * The number of words a term can have. A word is a number of characters
	 * who are not divided by whitespaces.
     * @return maxWordsInTerm 
     */
	@Override
    public int getMaxWordsInTerm() {
        return this.MAX_WORDS_IN_TERM;
    }
    
	/**
	 * The number of characters a word in a term can have at last.
	 * @param maxWordLength 
	 */
	@Override
	public void setMaxWordLength(int maxWordLength) {
		MAX_WORD_LENGTH = maxWordLength;
	}
	
    /**
     * The number of characters a word in a term can have at last.
     * @return maxWordLength
     */
	@Override
    public int getMaxWordLength() {
        return this.MAX_WORD_LENGTH;
    }
    
	/**
	 * A List of Strings that should not appear in the term value, if it does,
	 * the term will be ignored.
	 * @param forbiddenStrings 
	 */
	@Override
	public void setForbiddenStrings(String forbiddenStrings) {
		String[] splitted = forbiddenStrings.split(";;");
        for (String forbiddenString : splitted) {
            FORBIDDEN_STRINGS.add(forbiddenString);
        }

	}
	
    /**
     * A List of Strings that should not appear in the term value, if it does,
	 * the term will be ignored.
     * @return forbiddenStrings 
     */
	@Override
    public List<String> getForbiddenStringsAsList() {
        return this.FORBIDDEN_STRINGS;
    }
    
	/**
	 * Starts the simple cleaning process.
	 * @param rawEntities
	 * @return List<Entity>
	 */
	@Override
	public List<Entity> clean(List<Entity> rawEntities) {
		
		List<Entity> cleanedEntities = new ArrayList<Entity>();
		
        // begin interate through entities
        for (Entity rawEntity : rawEntities) {
			
            Entity cleanedEntity = new Entity();
            List<TermCloudField> cleanedFields = new ArrayList<TermCloudField>();
            
            cleanedEntity.setID(rawEntity.getID());
            
			String name = rawEntity.getName();
			// clean entity if it has a name
			if (name != null && !name.isEmpty()) {
				
				// remove leading and trailing whitespaces from entity name
				name = name.trim();
                cleanedEntity.setName(name);
                
                // begin iterate through fields
                for (TermCloudField field : rawEntity.getFields()) {
                   
                    TermCloudField cleanedField = new TermCloudField();
					cleanedField.setID(field.getID());
                    List<Term> cleanedTerms = new ArrayList<Term>();
                    StringBuilder regex = new StringBuilder();
                    
                    // begin iterate through terms
                    for (Term term : field.getTerms()) {

                        // remove leading and trailing whitespaces from terms
                        term.setValue(term.getValue().trim());
                                      
                        // convert tag to lowercase
                        term.setValue(term.getValue().toLowerCase(java.util.Locale.GERMAN));

                        /* remove tag if there are more than MAX_WORDS_IN_TERM words and
                           words with more than MAX_WORD_LENGTH characters */
                        regex.append("[^ ]{1,").append(MAX_WORD_LENGTH).append("}");
                        if (MAX_WORDS_IN_TERM > 1) {
                            regex.append("([ ][^ ]{1,").append(MAX_WORD_LENGTH).
                                append("}){0,").append(MAX_WORDS_IN_TERM-1).append("}");
                        }
                        if (!term.getValue().matches(regex.toString())) {
                            term = null;
                        }
                        regex.setLength(0);
                        /* /remove tag if there are more than MAX_WORDS_IN_TERM words and
                           words with more than MAX_WORD_LENGTH characters */

                        /* remove tag if it contains one or more entries of the forbidden string list */
                        if (term != null && !FORBIDDEN_STRINGS.isEmpty()) {
                            regex.append("[");
                            for (String forbiddenString : FORBIDDEN_STRINGS) {
                                regex.append("^(").append(forbiddenString).append(")");
                            }
                            regex.append("]*");
                            if (!term.getValue().matches(regex.toString())) {
                                term = null;
                            }
                            regex.setLength(0);
                        }
                        /*  */

                        // ignore tags with a length less than MIN_TERM_LENGTH
                        if (term != null && term.getValue().length() >= MIN_TERM_LENGTH) {
                            cleanedTerms.add(term);
                        }
                    }
                    // done iterate through terms
				
                    // remove all duplicate terms
                    Set<Term> set = new LinkedHashSet<Term>(cleanedTerms);
                    cleanedTerms.clear();
                    cleanedTerms.addAll(set);
                    // ignore fields with a number of terms less than MIN_TERM_COUNT
                    if (cleanedTerms.size() >= MIN_TERM_COUNT) {
                        cleanedField.setTerms(cleanedTerms);
                        cleanedFields.add(cleanedField);
                    } 
                }
                // done iterate through fields
            }
            
            // add the cleaned entity to result if it has fields left    
            if (!cleanedFields.isEmpty()) {
                cleanedEntity.setFields(cleanedFields);
                cleanedEntities.add(cleanedEntity);
            }
		}
        // done interate through entities
    
		return cleanedEntities;
	}
}
