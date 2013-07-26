package de.bbcdaas.common.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A group of TermToTermGroup.
 * @author Robert Illers
 */
public class TermToTermsGroups implements Serializable {
	
	private int groupID = 0;
	private String groupLabel = "";
	private float minSyntag;
	private User user = null;
	private List<TermToTermsGroup> termToTermsGroups = new ArrayList<TermToTermsGroup>();

	/**
	 * 
	 * @return 
	 */
	public List<TermToTermsGroup> getTermToTermsGroups() {
		return this.termToTermsGroups;
	}

	/**
	 * 
	 * @param termToTermsGroups 
	 */
	public void setTermToTermsGroups(List<TermToTermsGroup> termToTermsGroups) {
		this.termToTermsGroups = termToTermsGroups;
	}

	/**
	 * 
	 * @return 
	 */
	public int getGroupID() {
		return groupID;
	}

	/**
	 * 
	 * @param groupID 
	 */
	public void setGroupID(int rtgID) {
		this.groupID = rtgID;
	}

	/**
	 * 
	 * @return 
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 
	 * @param user 
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 
	 * @return 
	 */
	public String getGroupLabel() {
		return groupLabel;
	}

	/**
	 * 
	 * @param groupLabel 
	 */
	public void setGroupLabel(String groupLabel) {
		this.groupLabel = groupLabel;
	}

	/**
	 * 
	 * @return 
	 */
	public float getMinSyntag() {
		return minSyntag;
	}

	/**
	 * 
	 * @param minSyntag 
	 */
	public void setMinSyntag(float minSyntag) {
		this.minSyntag = minSyntag;
	}
}
