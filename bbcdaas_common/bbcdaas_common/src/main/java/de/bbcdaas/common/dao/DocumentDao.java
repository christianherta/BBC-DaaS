package de.bbcdaas.common.dao;

import de.bbcdaas.common.beans.entity.Entity;

/**
 * Interface for a document based dao.
 * @author Robert Illers
 */
public interface DocumentDao {
	
	/**
	 * 
	 * @param entity 
	 */
	public String storeEntity(Entity entity);
}
