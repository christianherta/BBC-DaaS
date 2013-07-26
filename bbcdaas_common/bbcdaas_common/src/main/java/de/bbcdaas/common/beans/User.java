package de.bbcdaas.common.beans;

import java.io.Serializable;

/**
 * A User Object represents a user by its name and role.
 * @author Robert Illers
 */
public class User implements Serializable {
	
	private int id;
	private String name;
	private int role;

	/**
	 * 
	 * @return 
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @param id 
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return 
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name 
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return 
	 */
	public int getRole() {
		return role;
	}

	/**
	 * 
	 * @param role 
	 */
	public void setRole(int role) {
		this.role = role;
	}
}
