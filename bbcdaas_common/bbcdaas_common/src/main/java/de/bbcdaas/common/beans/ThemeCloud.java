package de.bbcdaas.common.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A ThemeCloud is a group of terms related to a theme that is discribed by
 * the theme cloud name.
 * @author Robert Illers
 */
public class ThemeCloud implements Serializable {
	
	private Integer id;
	private String themeCloudName;
	private List<Term> terms = new ArrayList<Term>();
	private User user;

	/**
	 * 
	 * @return id
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
	 * Name of the theme cloud discribing its purpose.
	 * @return themeCloudName
	 */
	public String getThemeCloudName() {
		return themeCloudName;
	}

	/**
	 * Name of the theme cloud discribing its purpose.
	 * @param themeCloudName 
	 */
	public void setThemeCloudName(String themeCloudName) {
		this.themeCloudName = themeCloudName;
	}
	
	/**
	 * 
	 * @return 
	 */
	public List<Term> getTerms() {
		return terms;
	}

	/**
	 * 
	 * @param terms 
	 */
	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}

	/**
	 * The User who created the theme cloud.
	 * @return user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * The User who created the theme cloud.
	 * @param user 
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
