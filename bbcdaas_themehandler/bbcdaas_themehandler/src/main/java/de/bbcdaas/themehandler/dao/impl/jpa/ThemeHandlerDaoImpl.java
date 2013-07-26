package de.bbcdaas.themehandler.dao.impl.jpa;

import de.bbcdaas.common.dao.api.JavaPersistenceAPI;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.common.dao.base.JPABaseDao;
import de.bbcdaas.themehandler.dao.ThemeHandlerDao;
import de.bbcdaas.themehandler.domains.Term;
import de.bbcdaas.themehandler.domains.ThemeCloud;
import de.bbcdaas.themehandler.domains.UserData;
import java.util.List;

/**
 *
 * @author Robert Illers
 */
public class ThemeHandlerDaoImpl extends JPABaseDao implements ThemeHandlerDao {

	/**
	 * constructor
	 */
	public ThemeHandlerDaoImpl() {
		this.setJavaPersistenceAPI(new JavaPersistenceAPI("PU"));
	}
	
	/**
	 * 
	 * @param persistenceUnitName 
	 */
	public ThemeHandlerDaoImpl(String persistenceUnitName) {
		this.setJavaPersistenceAPI(new JavaPersistenceAPI(persistenceUnitName));
	}
	
	/**
	 * 
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<ThemeCloud> getThemeClouds() throws DaoException {
		
		try {
			this.getJPA().openConnection();
			List<ThemeCloud> themeClouds = this.getJPA().findAll(ThemeCloud.class);
			this.getJPA().closeConnection();
			return themeClouds;
		} catch(ApiException e) {
			throw new DaoException("getThemeClouds()", e.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 * @param themeCloudName
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public ThemeCloud getThemeCloudByName(String themeCloudName) throws DaoException {
		
		try {
			this.getJPA().openConnection();
			List<ThemeCloud> themeClouds = this.getJPA().findAllByConditions(ThemeCloud.class, "name='"+themeCloudName+"'");
			this.getJPA().closeConnection();
			if (themeClouds.isEmpty()) {
				return null;
			} else {
				return themeClouds.get(0);
			}
		} catch(ApiException e) {
			throw new DaoException("getThemeCloudByName()", e.getCompleteMessage());
		}
	}

	/**
	 * 
	 * @throws DaoException 
	 */
	@Override
	public void clearAllTables() throws DaoException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * 
	 * @param themeCloudID
	 * @throws DaoException 
	 */
	@Override
	public void deleteThemeCloud(int themeCloudID) throws DaoException {
		
		try {
			this.getJPA().openConnection();
			ThemeCloud themeCloud = this.getJPA().find(ThemeCloud.class, themeCloudID);
			if (themeCloud != null) {
				this.getJPA().remove(themeCloud);
				this.getJPA().commitAndCloseConnection();
			} else {
				this.getJPA().closeConnection();
				logger.error("ThemeCloud with ID = '"+themeCloudID+"' not found and could not be deleted.");
			}
		} catch(ApiException e) {
			throw new DaoException("deleteThemeCloud()", e.getCompleteMessage());
		}
	}

	/**
	 * 
	 * @param themeCloud
	 * @throws DaoException 
	 */
	@Override
	public void insertThemeCloud(ThemeCloud themeCloud) throws DaoException {
		
		try {
			this.getJPA().openConnection();
			this.getJPA().persist(themeCloud);
			this.getJPA().commitAndCloseConnection();
		} catch(ApiException e) {
			throw new DaoException("insertThemeCloud()", e.getCompleteMessage());
		}
	}

	/**
	 * 
	 * @param themeCloud
	 * @throws DaoException 
	 */
	@Override
	public void updateThemeCloud(ThemeCloud themeCloud) throws DaoException {
		
		try {
			this.getJPA().openConnection();
			this.getJPA().refresh(themeCloud);
			this.getJPA().commitAndCloseConnection();
		} catch(ApiException e) {
			throw new DaoException("updateThemeCloud()", e.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 * @param termID
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public Term getTerm(int termID) throws DaoException {
		
		try {
			this.getJPA().openConnection();
			Term term = this.getJPA().find(Term.class, termID);
			this.getJPA().closeConnection();
			return term;
		} catch(ApiException e) {
			throw new DaoException("getTerm()", e.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 * @param term
	 * @throws DaoException 
	 */
	@Override
	public void updateTerm(Term term) throws DaoException {
		
		try {
			this.getJPA().openConnection();
			this.getJPA().refresh(term);
			this.getJPA().commitAndCloseConnection();
		} catch(ApiException e) {
			throw new DaoException("updateTerm()", e.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 * @param term
	 * @throws DaoException 
	 */
	@Override
	public void insertTerm(Term term) throws DaoException {
		
		try {
			this.getJPA().openConnection();
			this.getJPA().persist(term);
			this.getJPA().commitAndCloseConnection();
		} catch(ApiException e) {
			throw new DaoException("insertTerm()", e.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 * @param term
	 * @throws DaoException 
	 */
	@Override
	public void deleteTerm(Term term) throws DaoException {
		
		try {
			this.getJPA().openConnection();
			this.getJPA().remove(term);
			this.getJPA().commitAndCloseConnection();
		} catch(ApiException e) {
			throw new DaoException("deleteTerm()", e.getCompleteMessage());
		}
	}
	
	
	@Override
	public Term getTermByValue(String value) throws DaoException {
		
		// TODO: more efficient
		try {
			this.getJPA().openConnection();
			List<Term> terms = this.getJPA().findAll(Term.class);
			for (Term term : terms) {
				if (term.getTermValue().equalsIgnoreCase(value)) {
					return term;
				}	
			}
			this.getJPA().closeConnection();
		} catch(ApiException e) {
			throw new DaoException("getTermByValue()", e.getCompleteMessage());
		}
		return null;
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public UserData getUserByName(String username) throws DaoException {
		
		try {
			this.getJPA().openConnection();
			List<UserData> users = this.getJPA().findAll(UserData.class);
			for (UserData user : users) {
				if (user.getUsername().equals(username)) {
					return user;
				}	
			}
			this.getJPA().closeConnection();
		} catch(ApiException e) {
			throw new DaoException("getUserByName()", e.getCompleteMessage());
		}
		return null;
	}
	
	/**
	 * 
	 * @param userID
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public UserData getUserByID(int userID) throws DaoException {
		
		try {
			this.getJPA().openConnection();
			List<UserData> users = this.getJPA().findAll(UserData.class);
			for (UserData user : users) {
				if (user.getId() == userID) {
					return user;
				}	
			}
			this.getJPA().closeConnection();
		} catch(ApiException e) {
			throw new DaoException("getUserByID()", e.getCompleteMessage());
		}
		return null;
	}
	
	/**
	 * 
	 * @param user
	 * @throws DaoException 
	 */
	@Override
	public void insertUser(UserData user) throws DaoException {
		
		try {
			this.getJPA().openConnection();
			this.getJPA().persist(user);
			this.getJPA().commitAndCloseConnection();
		} catch(ApiException e) {
			throw new DaoException("insertUser()", e.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 * @param user
	 * @throws DaoException 
	 */
	@Override
	public void updateUser(UserData user) throws DaoException {
		
		try {
			this.getJPA().openConnection();
			this.getJPA().refresh(user);
			this.getJPA().commitAndCloseConnection();
		} catch(ApiException e) {
			throw new DaoException("updateUser()", e.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 */ 
	@Override
	public void commit() {
		
		try {
			this.getJPA().commit();
		} catch(ApiException e) {
			logger.error(e.getCompleteMessage());
		}
	}
}
