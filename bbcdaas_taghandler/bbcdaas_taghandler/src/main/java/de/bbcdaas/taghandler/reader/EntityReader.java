package de.bbcdaas.taghandler.reader;

import java.util.List;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.taghandler.exception.ProcessException;
/**
 * The Entity Reader reads data from a given source that contains data sets who
 * represent an entity. An entity has a name and a list of at least one field.
 * The fields contain tags who will be read an converted into terms (they get an ID).
 * @author Christian Herta
 * @author Robert Illers
 */
public interface EntityReader {
    
	/**
     * Sets how many entities should be read from source in a reading step
     * @param entityReadStep 
     */
	public void setEntityReadStep(int entityReadStep);

	/**
	 * returns the number of entities that are read in a single read step
	 * @return number of entities to read
	 */
	public int getEntityReadStep();
	
	/**
	 * Closes the connection to the input data.
	 */
	public void closeReader();
	
	/**
     * Starts the reading process.
     * @return a list of read entity objects
     */
	public List<Entity> readEntities() throws ProcessException;
}
