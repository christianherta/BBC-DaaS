package de.bbcdaas.themehandler.dao;

import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.themehandler.domains.Term;
import de.bbcdaas.themehandler.domains.ThemeCloud;
import de.bbcdaas.themehandler.domains.UserData;
import java.util.List;

/**
 *
 * @author Robert Illers
 */
public interface ThemeHandlerDao {
	
	/**
	 * 
	 * @return
	 * @throws DaoException 
	 */
	public List<ThemeCloud> getThemeClouds() throws DaoException;
	
	/**
	 * 
	 * @param themeCloudName
	 * @return
	 * @throws DaoException 
	 */
	public ThemeCloud getThemeCloudByName(String themeCloudName) throws DaoException;
	
	/**
	 * 
	 * @param themeCloudID
	 * @throws DaoException 
	 */
	public void deleteThemeCloud(int themeCloudID) throws DaoException;
	
	/**
	 * 
	 * @param themeCloud
	 * @throws DaoException 
	 */
	public void insertThemeCloud(ThemeCloud themeCloud) throws DaoException;
	
	/**
	 * 
	 * @param themeCloud
	 * @throws DaoException 
	 */
	public void updateThemeCloud(ThemeCloud themeCloud) throws DaoException;
	
	/**
	 * 
	 * @param termID
	 * @return
	 * @throws DaoException 
	 */
	public Term getTerm(int termID) throws DaoException;
	
	/**
	 * 
	 * @param value
	 * @return
	 * @throws DaoException 
	 */
	public Term getTermByValue(String value) throws DaoException;
	
	/**
	 * 
	 * @param term
	 * @throws DaoException 
	 */
	public void updateTerm(Term term) throws DaoException;
	
	/**
	 * 
	 * @param term
	 * @throws DaoException 
	 */
	public void insertTerm(Term term) throws DaoException;
	
	/**
	 * 
	 * @param term
	 * @throws DaoException 
	 */
	public void deleteTerm(Term term) throws DaoException;
	
	/**
	 * 
	 * @param username
	 * @return
	 * @throws DaoException 
	 */
	public UserData getUserByName(String username) throws DaoException;
	
	/**
	 * 
	 * @param userID
	 * @return
	 * @throws DaoException 
	 */
	public UserData getUserByID(int userID) throws DaoException;
	
	/**
	 * 
	 * @param user
	 * @throws DaoException 
	 */
	public void insertUser(UserData user) throws DaoException;
	
	/**
	 * 
	 * @param user
	 * @throws DaoException 
	 */
	public void updateUser(UserData user) throws DaoException;
}
