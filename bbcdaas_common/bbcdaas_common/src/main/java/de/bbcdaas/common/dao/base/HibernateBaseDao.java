package de.bbcdaas.common.dao.base;

import de.bbcdaas.common.dao.api.HibernateAPI;
import de.bbcdaas.common.dao.exceptions.ApiException;
import org.apache.log4j.Logger;

/**
 * Base Dao for all hibernate based DAOs.
 * @author Robert Illers
 */
public abstract class HibernateBaseDao implements BaseDao {

	private HibernateAPI hibernateApi = null;
	protected Logger logger = Logger.getLogger(HibernateBaseDao.class);

	/**
	 *
	 * @param hibernateAPI
	 */
	public final void setHibernateAPI(HibernateAPI hibernateAPI) {
		this.hibernateApi = hibernateAPI;
	}

	/**
	 *
	 * @return
	 */
	protected final HibernateAPI getHibernate() {
		return hibernateApi;
	}

	/**
	 *
	 */
	@Override
	public void openConnection() {
		try {
			this.getHibernate().openConnection();
			this.getHibernate().setConnectionOpenedExternal(true);
		} catch(ApiException e) {
			logger.error(e.getCompleteMessage());
		}
	}

	/**
	 *
	 * @param forceClose
	 */
	@Override
	public void closeConnection(boolean forceClose) {

		try {
			this.getHibernate().closeConnection();
			this.getHibernate().setConnectionOpenedExternal(false);
		} catch(ApiException e) {
			logger.error(e.getCompleteMessage());
		}
	}
}
