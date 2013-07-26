package de.bbcdaas.themehandlerweb.dao;

import de.bbcdaas.themehandlerweb.domains.UserEntity;
import java.util.List;

/**
 *
 * @author Robert Illers
 */
public interface ThemeHandlerWebDao {
	
	/**
	 * 
	 * @return 
	 */
	public List<UserEntity> getAllUser();
	
	/**
	 * 
	 * @param UserId
	 * @return 
	 */
	public boolean deleteUser(int userId);
	
	/**
	 * 
	 * @param userName
	 * @return 
	 */
	public UserEntity getUserByName(String userName);
	
	/**
	 * 
	 * @param userName
	 * @param password
	 * @param userRole 
	 */
	public void insertUser(String userName , String password, int userRole);
}
