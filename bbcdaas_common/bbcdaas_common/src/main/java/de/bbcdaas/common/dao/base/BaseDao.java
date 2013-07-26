package de.bbcdaas.common.dao.base;

import de.bbcdaas.common.dao.exceptions.DaoException;

/**
 * Base interface of all DAOs, provides access to the ost common methods
 * each DAO has.
 * @author Robert Illers
 */
public interface BaseDao {
	
	/**
	 * Opens a new connection to a database.
	 */
	public void openConnection();
	
	/**
	 * Closes a connection to a database.
	 * @param forceClose 
	 */
	public void closeConnection(boolean forceClose);
	
	/**
	 * Clears all Tables of a connected database.
	 * @throws DaoException 
	 */
	public void clearAllTables() throws DaoException;	
	
	/**
	 * Commits a prepared query.
	 */
	public void commit();
}
