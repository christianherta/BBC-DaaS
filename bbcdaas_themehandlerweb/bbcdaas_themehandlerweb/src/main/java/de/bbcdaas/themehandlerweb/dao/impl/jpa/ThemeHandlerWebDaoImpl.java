package de.bbcdaas.themehandlerweb.dao.impl.jpa;

import de.bbcdaas.common.dao.base.JPABaseDao;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.themehandlerweb.dao.ThemeHandlerWebDao;
import de.bbcdaas.themehandlerweb.domains.UserEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 *
 * @author Robert Illers
 */
public class ThemeHandlerWebDaoImpl extends JPABaseDao implements ThemeHandlerWebDao {

	/**
	 * 
	 * @return 
	 */
	@Override
	public List<UserEntity> getAllUser() {
		
		List<UserEntity> users = new ArrayList<UserEntity>();
		try {
			this.getJPA().openConnection();
			users = this.getJPA().findAll(UserEntity.class);
			this.getJPA().closeConnection();
		} catch (ApiException ex) {
			logger.error(ex.getCompleteMessage());
		}
		return users;
	}
	
	/**
	 * 
	 * @param userName
	 * @return 
	 */
	@Override
	public UserEntity getUserByName(String userName) {
		
		try {
			this.getJPA().openConnection();
			List<UserEntity> userEntities = this.getJPA().findAll(UserEntity.class);
			this.getJPA().closeConnection();
			for (UserEntity userEntity : userEntities) {
				if (userEntity.getLoginName().equals(userName)) {
					return userEntity;
				}	
			}
		} catch (ApiException ex) {
			logger.error(ex.getCompleteMessage());
		}
		return null;
		
	}

	/**
	 * 
	 * @param userName
	 * @param password
	 * @param userRole 
	 */
	@Override
	public void insertUser(String userName , String password, int userRole) {
		
		UserEntity userEntity = new UserEntity();
		userEntity.setLoginName(userName);
		userEntity.setPassword(new Md5PasswordEncoder().encodePassword(password, null));
		userEntity.setUserRole(userRole);
		
		try {
			this.getJPA().openConnection();
			this.getJPA().persist(userEntity);
			this.getJPA().commitAndCloseConnection();
		} catch (ApiException ex) {
			logger.error(ex.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 * @param UserId 
	 */
	@Override
	public boolean deleteUser(int userId) {
		
		try {
			this.getJPA().openConnection();
			UserEntity userEntity = this.getJPA().find(UserEntity.class, userId);
			if (userEntity == null) {
				return false;
			}
			this.getJPA().remove(userEntity);
			this.getJPA().commitAndCloseConnection();
		} catch (ApiException ex) {
			logger.error(ex.getCompleteMessage());
		}
		return true;
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
