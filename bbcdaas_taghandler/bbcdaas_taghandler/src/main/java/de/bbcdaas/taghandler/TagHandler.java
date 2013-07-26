package de.bbcdaas.taghandler;

import de.bbcdaas.taghandler.cleaner.EntityCleaner;
import de.bbcdaas.taghandler.dao.TagHandlerDao;
import de.bbcdaas.taghandler.exception.ProcessException;
import de.bbcdaas.taghandler.reader.EntityReader;
import de.bbcdaas.taghandler.reducer.TermDataReducer;
import de.bbcdaas.taghandler.writer.EntityWriter;
/**
 * User-Interface to add entities into the system.  
 * Entities consist of a unique name and a set of fields that contains terms.
 * @author Christian Herta
 * @author Robert Illers
 */
public interface TagHandler extends Runnable {
	
	/**
	 * injected by spring
	 * @param taghandlerDao 
	 */
	public void setTaghandlerDao(TagHandlerDao taghandlerDao);
	
    /**
     * injected by spring
     * @param useSingleTermLexicon 
     */
    public void setUseSingleTermLexicon(boolean useSingleTermLexicon);
    
	/**
	 * injected by spring
	 * @param entityReader 
	 */
	public void setEntityReader(EntityReader entityReader);
	
	/**
	 * injected by spring
	 * @param termDataReducer 
	 */
	public void setTermDataReducer(TermDataReducer termDataReducer);
	
	/**
	 * injected by spring
	 * @param entityCleaner 
	 */
	public void setEntityCleaner(EntityCleaner entityCleaner);
	
	/**
	 * injected by spring
	 * @param entityWriter 
	 */
	public void setEntityWriter(EntityWriter entityWriter);
    
    /**
     * injected by spring
     * @param readEntities 
     */
    public void setReadEntities(boolean readEntities);
    
	/**
	 * injected by spring
	 * @param reduceTermData 
	 */
	public void setReduceTermData(boolean reduceTermData);
	
    /**
     * injected by spring
     * @param computeSyntagmaticRelations 
     */
    public void setComputeSyntagmaticRelations(boolean computeSyntagmaticRelations);
    
    /**
     * injected by spring
     * @param computeTopRelatedTerms 
     */
    public void setComputeTopRelatedTerms(boolean computeTopRelatedTerms);
    
	/**
	 * injected by spring
	 * @param computeTopSyntagmaticTerms 
	 */
	public void setComputeTopSyntagmaticTerms(boolean computeTopSyntagmaticTerms);
	
    /**
     * starts the complete handleTag process. the process can be configured in the applicationContext.xml and
     * in the parameter.properties file.
     */
    public void handleTags();
	
	/**
	 * Executes the read entities process. Entity data will be read by the
	 * reader set in the applicationContext.xml
	 */
	public void readEntities() throws ProcessException;
	
	/**
	 * Executes the computation of the syntagmatic relation of the cooccurrences
	 * in the term matrix.
	 */
	public void computeSyntagmaticRelations() throws ProcessException;
	
	/**
	 * Reduces the read data ba the given parameter set in parameter.properties.
	 * Uses the Reducer configured in the applicationContext.xml
	 * @throws ProcessException 
	 */
	public void reduceTerms() throws ProcessException;
	
	/**
	 * Executes the computation of the top related terms to each read term.
	 */
	public void computeTopRelatedTerms() throws ProcessException;
	
	/**
	 * Executes the computation of a cloud of terms that matches to the terms
	 * in the entities fields.
	 */
	public void computeTopSyntagmaticTerms() throws ProcessException;
}