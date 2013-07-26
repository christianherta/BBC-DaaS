package de.bbcdaas.disambiguationweb.dao;

import de.bbcdaas.disambiguationweb.domains.UserEntity;
import java.util.List;

/**
 *
 * @author Robert Illers
 */
public interface DisambiguationWebDao {
	
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
	public boolean deleteUser(int UserId);
	
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
