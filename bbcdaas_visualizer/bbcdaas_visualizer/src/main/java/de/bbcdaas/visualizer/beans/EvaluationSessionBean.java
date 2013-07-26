package de.bbcdaas.visualizer.beans;

import de.bbcdaas.common.beans.TermToTermsGroups;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert Illers
 */
public class EvaluationSessionBean implements Serializable {
	
	private List<TermToTermsGroups> randomTermGroups = new ArrayList<TermToTermsGroups>();
	private Integer selectedRandomTermsGroupID = null;
	private Integer selectedRandomTermID = null;
	private boolean currentSelectedRatingSaved = false;
	
	/**
	 * 
	 * @return 
	 */
	public List<TermToTermsGroups> getRandomTermGroups() {
		return randomTermGroups;
	}

	/**
	 * 
	 * @param randomTermGroups 
	 */
	public void setRandomTermGroups(List<TermToTermsGroups> randomTermGroups) {
		this.randomTermGroups = randomTermGroups;
	}

	/**
	 * 
	 * @return 
	 */
	public Integer getSelectedRandomTermsGroupID() {
		return selectedRandomTermsGroupID;
	}

	/**
	 * 
	 * @param selectedRandomTermsGroupID
	 */
	public void setSelectedRandomTermsGroupID(Integer selectedRandomTermsGroupID) {
		this.selectedRandomTermsGroupID = selectedRandomTermsGroupID;
	}
	
	/**
	 * 
	 * @return 
	 */
	public Integer getSelectedRandomTermID() {
		return this.selectedRandomTermID;
	}

	/**
	 * 
	 * @param selectedRandomTermID 
	 */
	public void setSelectedRandomTermID(Integer selectedRandomTermID) {
		this.selectedRandomTermID = selectedRandomTermID;
	}

	/**
	 * 
	 * @return 
	 */
	public Boolean getCurrentSelectedRatingSaved() {
		return currentSelectedRatingSaved;
	}

	/**
	 * 
	 * @param currentSelectedRatingSaved 
	 */
	public void setCurrentSelectedRatingSaved(Boolean currentSelectedRatingSaved) {
		this.currentSelectedRatingSaved = currentSelectedRatingSaved;
	}
}
