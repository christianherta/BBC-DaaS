package de.bbcdaas.visualizer.dao;

import de.bbcdaas.common.beans.User;
import de.bbcdaas.common.dao.exceptions.DaoException;
import java.util.List;

/**
 *
 * @author Robert Illers
 */
public interface VisualizerDao {
	
	/**
	 * 
	 * @param userName
	 * @return
	 * @throws DaoException 
	 */
	public User insertUser(String userName, int role, String password) throws DaoException;
	
	/**
	 * 
	 * @param userID
	 * @return
	 * @throws DaoException 
	 */
	public void deleteUser(int userID) throws DaoException;
	
	/**
	 * 
	 * @param userID
	 * @return
	 * @throws DaoException 
	 */
	public User getUserByID(int userID) throws DaoException;
	
	/**
	 * 
	 * @param userName
	 * @return
	 * @throws DaoException 
	 */
	public User getUserByName(String userName) throws DaoException;
	
	/**
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @throws DaoException 
	 */
	public User getUserByNameAndPassword(String userName, String password) throws DaoException;
	
	/**
	 * 
	 * @return
	 * @throws DaoException 
	 */
	public List<User> getAllUser() throws DaoException;
}
