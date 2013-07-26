package de.bbcdaas.themehandlerweb.beans;

import de.bbcdaas.common.beans.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert Illers
 */
public class SessionContainer implements Serializable {
	
	private List<User> userList = new ArrayList<User>();
	private ThemeCloudSessionBean themeCloudSessionBean = new ThemeCloudSessionBean();
	private String restBaseURI;
	
	/**
	 * 
	 * @return 
	 */
	public ThemeCloudSessionBean getThemeCloudSessionBean() {
		return themeCloudSessionBean;
	}
	
	/**
	 * 
	 * @return 
	 */
	public List<User> getUserList() {
		return userList;
	}

	/**
	 * 
	 * @param userList 
	 */
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public String getRestBaseURI() {
		return restBaseURI;
	}

	public void setRestBaseURI(String restBaseURI) {
		this.restBaseURI = restBaseURI;
	}
}
