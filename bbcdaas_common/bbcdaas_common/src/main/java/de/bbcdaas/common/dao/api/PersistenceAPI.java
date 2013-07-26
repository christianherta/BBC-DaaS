package de.bbcdaas.common.dao.api;

import de.bbcdaas.common.dao.exceptions.ApiException;

/**
 * Interface for all APIs wrapping a kind of data access objects implementation
 * for standarized access in bbcdaas modules.
 * @author Robert Illers
 */
public interface PersistenceAPI {

	/**
	 *
	 * @throws ApiException
	 */
	public void openConnection() throws ApiException;

	/**
	 *
	 * @throws ApiException
	 */
	public void commit() throws ApiException;

	/**
	 *
	 * @throws ApiException
	 */
	public void closeConnection() throws ApiException;

	/**
	 *
	 * @throws ApiException
	 */
	public void commitAndCloseConnection() throws ApiException;

	/**
	 *
	 * @return
	 */
	public boolean isConnectionOpenedExternal();

	/**
	 *
	 * @param connectionOpenedExternal
	 */
	public void setConnectionOpenedExternal(boolean connectionOpenedExternal);

	public boolean isEnableLogs();

	public void setEnableLogs(boolean enableLogs);
}
