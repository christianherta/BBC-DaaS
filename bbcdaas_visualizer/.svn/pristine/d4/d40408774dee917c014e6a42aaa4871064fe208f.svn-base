package de.bbcdaas.visualizer.dao;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.ThemeCloud;
import de.bbcdaas.common.dao.exceptions.DaoException;
import java.util.List;

/**
 *
 * @author Robert Illers
 */
public interface ThemeHandlerDao {
	
	/**
	 * 
	 * @param themeCloudName
	 * @param themeCloudTerms
	 * @return
	 * @throws DaoException 
	 */
	public int insertThemeCloud(String themeCloudName, List<Term> themeCloudTerms, int userID) throws DaoException;
	
	/**
	 * 
	 * @param themeCloudID
	 * @param themeCloudName
	 * @param themeCloudTerms
	 * @return
	 * @throws DaoException 
	 */
	public int updateThemeCloud(int themeCloudID, String themeCloudName, List<Term> themeCloudTerms, int UserID) throws DaoException;
	
	/**
	 * 
	 * @param themeCloudName
	 * @return
	 * @throws DaoException 
	 */
	public int getThemeCloudID(String themeCloudName) throws DaoException;
	
	/**
	 * 
	 * @param themeCloudID
	 * @throws DaoException 
	 */
	public void deleteThemeCloud(int themeCloudID) throws DaoException;
	
	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @return
	 * @throws DaoException 
	 */ 
	public List<ThemeCloud> getThemeClouds(int offset, int rowCount) throws DaoException;
}
