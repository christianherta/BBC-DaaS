package de.bbcdaas.taghandler.writer;

import java.util.List;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.taghandler.exception.ProcessException;
/**
 * Interface for all Writer that persist entities read by an EntityReader. 
 * @author Robert Illers
 */
public interface EntityWriter {
	
    /**
     * 
     * @param useSingleTermLexicon 
     */
    public void setUseSingleTermLexicon(boolean useSingleTermLexicon);
    
	/**
	 * Sets the number of entities that should be read from the source. If set to
	 * 0, all entities will be written.
	 * @param setNbOfTotalReadEntities 
	 */
	public void setNbOfTotalReadEntities(float setNbOfTotalReadEntities);
	
	/**
	 * Starts the write process
	 * @param entities 
	 */
	public void writeEntities(List<Entity> entities) throws ProcessException;
}

