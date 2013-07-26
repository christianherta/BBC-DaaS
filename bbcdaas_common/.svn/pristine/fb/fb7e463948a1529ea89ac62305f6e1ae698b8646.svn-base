package de.bbcdaas.common.dao.base;

import de.bbcdaas.common.dao.api.JavaPersistenceAPI;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.common.dao.exceptions.DaoException;
import org.apache.log4j.Logger;

/**
 * Base Dao for JPA based DAOs.
 * @author Robert Illers
 */
public abstract class JPABaseDao implements BaseDao {

	private JavaPersistenceAPI jpa = null;
	protected Logger logger = Logger.getLogger(JPABaseDao.class);

	/**
	 *
	 * @param jpa
	 */
	public final void setJavaPersistenceAPI(JavaPersistenceAPI jpa) {
		this.jpa = jpa;
	}

	/**
	 *
	 * @return
	 */
	protected final JavaPersistenceAPI getJPA() {
		return this.jpa;
	}

	/**
	 *
	 */
	@Override
	public void openConnection() {

		try {
			this.jpa.openConnection();
			this.jpa.setConnectionOpenedExternal(true);
		} catch (ApiException ex) {
			logger.error(ex.getCompleteMessage());
		}

	}

	/**
	 *
	 * @param forceClose
	 */
	@Override
	public void closeConnection(boolean forceClose) {

		try {
			this.jpa.closeConnection(forceClose);
		} catch (ApiException ex) {
			logger.error(ex.getCompleteMessage());
		}
	}

	@Override
	public void clearAllTables() throws DaoException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
