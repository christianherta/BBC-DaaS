package de.bbcdaas.common.dao.base;

import de.bbcdaas.common.dao.api.LightChouchAPI;
import de.bbcdaas.common.dao.exceptions.DaoException;
import org.apache.log4j.Logger;

/**
 * Base Dao for all chouch db based DAOs.
 * @author Robert Illers
 */
public abstract class ChouchDBBaseDao implements BaseDao {

	private LightChouchAPI lightChouchAPI = new LightChouchAPI();
	protected Logger logger = Logger.getLogger(this.getClass());

	/**
	 *
	 * @param lightChouchAPI 
	 */
	public void setLightChouchAPI(LightChouchAPI lightChouchAPI) {
		this.lightChouchAPI = lightChouchAPI;
	}

	/**
	 *
	 * @return
	 */
	protected LightChouchAPI getChouchDB() {
		return this.lightChouchAPI;
	}

        /**
         *
         */
	@Override
	public void openConnection() {
		this.lightChouchAPI.setConnectionOpenedExternal(true);
		throw new UnsupportedOperationException("Not supported yet.");
	}

        /**
         *
         * @param forceClose
         */
	@Override
	public void closeConnection(boolean forceClose) {
		this.lightChouchAPI.setConnectionOpenedExternal(false);
		throw new UnsupportedOperationException("Not supported yet.");
	}

        /**
         *
         * @throws DaoException
         */
	@Override
	public void clearAllTables() throws DaoException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
