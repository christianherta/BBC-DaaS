package de.bbcdaas.themehandler.domains;

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
public class UserData implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String username;

	/**
	 * 
	 * @return 
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id 
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return 
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 
	 * @param username 
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
