package de.bbcdaas.visualizer.beans;

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
	
	private EvaluationSessionBean evaluationSessionBean = new EvaluationSessionBean();
	private ThemeCloudSessionBean themeCloudSessionBean = new ThemeCloudSessionBean();
	private SyntagCloudSessionBean syntagCloudSessionBean = new SyntagCloudSessionBean();
	private ProcessingSessionBean processingSessionBean = new ProcessingSessionBean();
	private StatisticsSessionBean statisticsSessionBean = new StatisticsSessionBean();
	
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
	public EvaluationSessionBean getEvaluationSessionBean() {
		return evaluationSessionBean;
	}
	
	/**
	 * 
	 * @return 
	 */
	public SyntagCloudSessionBean getSyntagCloudSessionBean() {
		return syntagCloudSessionBean;
	}
	
	/**
	 * 
	 * @return 
	 */
	public ProcessingSessionBean getProcessingSessionBean() {
		return processingSessionBean;
	}
	
	/**
	 * 
	 * @return 
	 */
	public StatisticsSessionBean getStatisticsSessionBean() {
		return statisticsSessionBean;
	}
}
