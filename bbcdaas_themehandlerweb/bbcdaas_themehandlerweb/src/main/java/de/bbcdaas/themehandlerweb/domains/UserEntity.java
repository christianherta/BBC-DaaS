package de.bbcdaas.themehandlerweb.domains;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Robert Illers
 */
@Entity
public class UserEntity implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String loginName;
	private String password;
	private Integer userRole;
	
	/**
	 * 
	 * @return 
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @param password 
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 
	 * @return 
	 */
	public Integer getID() {
		return id;
	}

	/**
	 * 
	 * @param userID 
	 */
	public void setID(Integer userID) {
		this.id = userID;
	}

	/**
	 * 
	 * @return 
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * 
	 * @param loginName 
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * 
	 * @return 
	 */
	public Integer getUserRole() {
		return userRole;
	}

	/**
	 * 
	 * @param userRole 
	 */
	public void setUserRole(Integer userRole) {
		this.userRole = userRole;
	}
}
