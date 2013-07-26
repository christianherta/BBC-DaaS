package de.bbcdaas.common.dao.base;

import de.bbcdaas.common.dao.api.JdbcAPI;
import de.bbcdaas.common.dao.exceptions.ApiException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Base Dao for all SQL based DAOs.
 * @author Robert Illers
 */
public abstract class SqlBaseDao implements BaseDao {

	private JdbcAPI jdbcApi = null;
	protected List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	protected Logger logger = Logger.getLogger(SqlBaseDao.class);

	/**
	 * 
	 * @param jdbcAPI
	 */
	public final void setJdbcAPI(JdbcAPI jdbcAPI) {
		this.jdbcApi = jdbcAPI;
	}

	/**
	 *
	 * @return
	 */
	protected final JdbcAPI getJdbc() {
		return jdbcApi;
	}

	/**
	 *
	 */
	@Override
	public final void openConnection() {

		try {
			this.jdbcApi.openConnection();
			this.jdbcApi.setConnectionOpenedExternal(true);
		} catch(ApiException e) {
			logger.error(e.getCompleteMessage());
		}
	}

	/**
	 *
	 */
	@Override
	public final void closeConnection(boolean forceClose) {
		try {
			this.jdbcApi.closeConnection(forceClose);
			this.jdbcApi.setConnectionOpenedExternal(false);
		} catch(ApiException e) {
			logger.error(e.getCompleteMessage());
		}
	}
}
