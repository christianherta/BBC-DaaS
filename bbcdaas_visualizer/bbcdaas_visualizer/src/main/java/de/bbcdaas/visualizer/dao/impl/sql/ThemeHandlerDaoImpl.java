package de.bbcdaas.visualizer.dao.impl.sql;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.ThemeCloud;
import de.bbcdaas.common.beans.User;
import de.bbcdaas.common.dao.constants.sql.SQL_Keywords;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.common.dao.base.SqlBaseDao;
import de.bbcdaas.visualizer.dao.ThemeHandlerDao;
import de.bbcdaas.visualizer.dao.constants.sql.ThemeHandlerDB_Queries;
import de.bbcdaas.visualizer.dao.constants.sql.ThemeHandlerDB_Scheme;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Robert Illers
 */
public class ThemeHandlerDaoImpl extends SqlBaseDao implements ThemeHandlerDao {

	/**
	 * 
	 * @throws DaoException 
	 */
	@Override
	public void clearAllTables() throws DaoException {
		
		try {
			this.getJdbc().openConnection();
			this.getJdbc().executeUpdate(new StringBuilder().append(SQL_Keywords.TRUNCATE_TABLE).append(ThemeHandlerDB_Scheme.TABLE_THEME_CLOUD).toString());
			this.getJdbc().executeUpdate(new StringBuilder().append(SQL_Keywords.TRUNCATE_TABLE).append(ThemeHandlerDB_Scheme.TABLE_THEME_CLOUD_TERM).toString());
			this.getJdbc().commitAndCloseConnection();
		} catch (ApiException e) {
			throw new DaoException("clearAllTables", e.getCompleteMessage());
		}
	}
	
	/**
	 * Inserts a new ThemeCloud into the database.
	 * @param themeCloudName
	 * @param themeCloudTerms
	 * @param userID
	 * @return ID of the created themeCloud
	 * @throws DaoException 
	 */
	@Override
	public int insertThemeCloud(String themeCloudName, List<Term> themeCloudTerms, int userID) throws DaoException {

		int themeCloudID = 0;
		try {
			this.getJdbc().openConnection();
			this.getJdbc().executeUpdate(ThemeHandlerDB_Queries.INSERT_THEME_CLOUD, themeCloudName, userID);
			this.getJdbc().commitAndCloseConnection();
			themeCloudID = this.getThemeCloudID(themeCloudName);
			this.insertThemeCloudTerms(themeCloudID, themeCloudTerms);

		} catch (ApiException e) {
			throw new DaoException("saveThemeCloud", e.getCompleteMessage());
		}
		return themeCloudID;
	}
	
	/**
	 * Replaces an existing themeCloud with the given themeCloudName or creates a new one.
	 * The ThemeCloud replaced or created gets a new ID.
	 * @param themeCloudID
	 * @param themeCloudName
	 * @param themeCloudTerms
	 * @return ID of the created themeCloud
	 * @throws DaoException 
	 */
	@Override
	public int updateThemeCloud(int themeCloudID, String themeCloudName, List<Term> themeCloudTerms, int userID) throws DaoException {
		
		this.deleteThemeCloud(themeCloudID);
		return this.insertThemeCloud(themeCloudName, themeCloudTerms, userID);	
	}
	
	/**
	 * removes a themeCloud from the database.
	 */
	@Override
	public void deleteThemeCloud(int themeCloudID) throws DaoException {
		
		try {
			this.getJdbc().openConnection();
			this.getJdbc().executeUpdate(ThemeHandlerDB_Queries.DELETE_THEME_CLOUD, themeCloudID);
			this.getJdbc().executeUpdate(ThemeHandlerDB_Queries.DELETE_THEME_CLOUD_TERMS, themeCloudID);
			this.getJdbc().commitAndCloseConnection();
		} catch (ApiException e) {
			throw new DaoException("deleteThemeCloud", e.getCompleteMessage());
		}
	}
	
	/**
	 * Gets the ID of a ThemeCloud by the themeClouds name.
	 * @param themeCloudName
	 * @return ID of the themeCloud
	 */
	@Override
	public int getThemeCloudID(String themeCloudName) throws DaoException {

		int ret = 0;
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(ThemeHandlerDB_Queries.GET_THEME_CLOUD_ID_BY_NAME, themeCloudName);
			this.getJdbc().closeConnection();
			for (Map<String, Object> row : result) {
				ret = ((Long) row.get(ThemeHandlerDB_Scheme.THEME_CLOUD_COLUMN_THEME_CLOUD_ID)).intValue();
			}
		} catch (ApiException e) {
			throw new DaoException("getThemeCloudID", e.getCompleteMessage());
		}
		return ret;
	}
	
	/**
	 * Stores the term IDs of the themeCloud's terms in the table themeCloud_term 
	 * @param termCloudID
	 * @param themeCloudTerms
	 * @throws DaoException 
	 */
	private void insertThemeCloudTerms(int termCloudID, List<Term> themeCloudTerms) throws DaoException {

		try {
			this.getJdbc().openConnection();
			for (Term term : themeCloudTerms) {
				this.getJdbc().executeUpdate(ThemeHandlerDB_Queries.INSERT_THEME_CLOUD_TERMS, termCloudID, term.getId(), term.getRating(), term.getWeighting());
			}
			this.getJdbc().commitAndCloseConnection();
		} catch (ApiException e) {
			throw new DaoException("saveThemeCloudTerms", e.getCompleteMessage());
		}
	}
	
	/**
	 * Returns a list of themeClouds with its terms.
	 * @param offset
	 * @param rowCount
	 * @return List of themeClouds
	 * @throws DaoException 
	 */
	@Override
	public List<ThemeCloud> getThemeClouds(int offset, int rowCount) throws DaoException {
		
		List<ThemeCloud> themeClouds = new ArrayList<ThemeCloud>();
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(ThemeHandlerDB_Queries.GET_THEME_CLOUD_WITH_DATA, offset, rowCount);
			this.getJdbc().closeConnection();
			ThemeCloud themeCloud;
			for (Map<String, Object> row : result) {
				
				themeCloud = null;
				int newThemeCloudID = ((Long) row.get(ThemeHandlerDB_Scheme.THEME_CLOUD_COLUMN_THEME_CLOUD_ID)).intValue();
				String themeCloudName = (String) row.get(ThemeHandlerDB_Scheme.THEME_CLOUD_COLUMN_THEME_CLOUD_NAME);
				int userID = ((Long) row.get(ThemeHandlerDB_Scheme.THEME_CLOUD_COLUMN_THEME_CLOUD_USERID)).intValue();
				int termID = ((Long) row.get(ThemeHandlerDB_Scheme.THEME_CLOUD_TERM_COLUMN_TERM_ID)).intValue();
				int rating = ((Long) row.get(ThemeHandlerDB_Scheme.THEME_CLOUD_TERM_COLUMN_RATING)).intValue();
				Float weighting = (Float) row.get(ThemeHandlerDB_Scheme.THEME_CLOUD_TERM_COLUMN_WEIGHTING);
				Term term = new Term(termID);
				term.setRating(rating);
				term.setWeighting(weighting);
				for (ThemeCloud tc : themeClouds) {
					if (tc.getId() == newThemeCloudID) {
						themeCloud = tc;
						themeCloud.getTerms().add(term);
						break;
					}
				}
				
				if (themeCloud == null) {
					themeCloud = new ThemeCloud();
					themeCloud.setId(newThemeCloudID);
					themeCloud.setThemeCloudName(themeCloudName);
					User user = new User();
					user.setId(userID);
					themeCloud.setUser(user);
					themeCloud.getTerms().add(term);
					themeClouds.add(themeCloud);
				}
				
				
				
			}
		} catch (ApiException e) {
			throw new DaoException("getThemeClouds", e.getCompleteMessage());
		}
		
		return themeClouds;
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
