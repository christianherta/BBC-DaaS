package de.bbcdaas.visualizer.dao.impl.sql;

import de.bbcdaas.visualizer.dao.VisualizerDao;
import de.bbcdaas.common.beans.User;
import de.bbcdaas.visualizer.dao.constants.sql.VisualizerDB_Queries;
import de.bbcdaas.visualizer.dao.constants.sql.VisualizerDB_Scheme;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.common.dao.base.SqlBaseDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Robert Illers
 */
public class VisualizerDaoImpl extends SqlBaseDao implements VisualizerDao {

	@Override
	public void clearAllTables() throws DaoException {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public User insertUser(String userName, int role, String password) throws DaoException {
		
		User user = null;
		try {
			
			user = this.getUserByName(userName);
			if (user == null) {
				this.getJdbc().openConnection();
				this.getJdbc().executeUpdate(VisualizerDB_Queries.INSERT_USER, userName, role, password);
				this.getJdbc().commitAndCloseConnection();
				user = this.getUserByName(userName);
			} 
		} catch(ApiException e) {
			throw new DaoException("insertUser", e.getCompleteMessage());
		}
		return user;
	} 
	
	/**
	 * 
	 * @param userID
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public User getUserByID(int userID) throws DaoException {
		
		User user = null;
		try {
			
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(VisualizerDB_Queries.GET_USER_BY_ID, userID);
			this.getJdbc().closeConnection();
			for (Map<String, Object> row : result) {
				
				user = new User();
				user.setName((String)row.get(VisualizerDB_Scheme.USER_COLUMN_USER_NAME));
				user.setId(userID);
				user.setRole(((Long)row.get(VisualizerDB_Scheme.USER_COLUMN_ROLE)).intValue());
				break;
			}
		} catch (ApiException e) {
			throw new DaoException("getUserByID", e.getCompleteMessage());
		}
		return user;
	}
	
	/**
	 * 
	 * @param userName
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public User getUserByName(String userName) throws DaoException {
		
		User user = null;
		try {
			
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(VisualizerDB_Queries.GET_USER_BY_NAME, userName);
			this.getJdbc().closeConnection();
			for (Map<String, Object> row : result) {
				
				user = new User();
				user.setName(userName);
				user.setId(((Long)row.get(VisualizerDB_Scheme.USER_COLUMN_USER_ID)).intValue());
				user.setRole(((Long)row.get(VisualizerDB_Scheme.USER_COLUMN_ROLE)).intValue());
				break;
			}
		} catch (ApiException e) {
			throw new DaoException("getUserByUserName", e.getCompleteMessage());
		}
		return user;
	}
	
	/**
	 * 
	 * @param userID
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public void deleteUser(int userID) throws DaoException {
		
		try {
			this.getJdbc().openConnection();
			this.getJdbc().executeUpdate(VisualizerDB_Queries.DELETE_USER_BY_ID, userID);
			this.getJdbc().commitAndCloseConnection();
		} catch (ApiException e) {
			throw new DaoException("deleteUser", e.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws DaoException 
	 */ 
	@Override
	public List<User> getAllUser() throws DaoException {
		
		List<User> users = new ArrayList<User>();
		try {
			
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(VisualizerDB_Queries.GET_ALL_USER);
			this.getJdbc().closeConnection();
			for (Map<String, Object> row : result) {
				
				User user = new User();
				user.setName((String)row.get(VisualizerDB_Scheme.USER_COLUMN_USER_NAME));
				user.setId(((Long)row.get(VisualizerDB_Scheme.USER_COLUMN_USER_ID)).intValue());
				user.setRole(((Long)row.get(VisualizerDB_Scheme.USER_COLUMN_ROLE)).intValue());
				users.add(user);
			}
		} catch (ApiException e) {
			throw new DaoException("getAllUser", e.getCompleteMessage());
		}
		return users;
	}
	
	/**
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public User getUserByNameAndPassword(String userName, String password) throws DaoException {
		
		User user = null;
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(VisualizerDB_Queries.GET_USER_BY_NAME_AND_PASSWORD, userName, password);
			this.getJdbc().closeConnection();
			for (Map<String, Object> row : result) {
				
				user = new User();
				user.setName(userName);
				user.setId(((Long)row.get(VisualizerDB_Scheme.USER_COLUMN_USER_ID)).intValue());
				user.setRole(((Long)row.get(VisualizerDB_Scheme.USER_COLUMN_ROLE)).intValue());
				break;
			}
		} catch(ApiException e) {
			throw new DaoException("getUserByNameAndPassword", e.getCompleteMessage());
		}
		return user;
	}
	
	/**
	 * 
	 */ 
	@Override
	public void commit() {
		
		try {
			this.getJdbc().commit();
		} catch(ApiException e) {
			logger.error(e.getCompleteMessage());
		}
	}
}
