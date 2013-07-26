package de.bbcdaas.visualizer.control.themecloud;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.ThemeCloud;
import de.bbcdaas.common.beans.User;
import de.bbcdaas.taghandler.constants.ProcessingConstants;
import de.bbcdaas.visualizer.beans.SessionContainer;
import de.bbcdaas.visualizer.beans.ThemeCloudSessionBean;
import de.bbcdaas.visualizer.business.ThemeCloudBusiness;
import de.bbcdaas.visualizer.constants.ThemeCloudConstants;
import de.bbcdaas.visualizer.constants.VisualizerConstants;
import de.bbcdaas.visualizer.control.BaseController;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Robert Illers
 */
public class HandleThemeCloudForm extends BaseController {

	@Override
	public ModelAndView handleRequestImpl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		User user = (User)request.getSession().getAttribute(VisualizerConstants.KEY_USER);
		String task = request.getParameter("task");
		logger.info("task: "+task);
		
		String forward = ThemeCloudConstants.FORWARD_THEMECLOUD_JSP;
		
		if (task != null && user != null) {
			
			if (task.equals("addNewTerm")) {
				
				forward = this.task_addNewTerm(request);
			} else
				
			if (task.equals("addSyntagCloudTerms")) {
				
				forward = this.task_addSyntagCloudTerms(request);
			} else
			
			if (task.equals("setWeighting")) {
				
				forward = this.task_setWeighting(request);
			} else
				
			if (task.equals("calcSyntagCloud")) {
				
				forward = this.task_calcSyntagCloud(request);
			} else	
				
			if (task.equals("save")) {
				
				forward = this.task_saveThemeCloud(request, user);
			} else
			
			if (task.equals("reset")) {
				
				forward = this.task_resetThemeCloud(request);
			} else
				
			if (task.equals("moveNode")) {
				
				forward = this.task_moveNode(request);
			} else
				
			if (task.equals("editThemeCloud")) {
				
				forward = this.task_editThemeCloud(request);
			} else
				
			if (task.equals("deleteThemeCloud")) {
				
				forward = this.task_deleteThemeCloud(request);
			}
		}
		
		return new ModelAndView(forward);
	}
	
	/**
	 * 
	 * @param request
	 * @param sessionContainer 
	 */
	private void getBasicParameter(HttpServletRequest request, SessionContainer sessionContainer) {
		
		String themeCloudName = request.getParameter("themeCloudName");
		if (themeCloudName == null) {
			themeCloudName = "";
		}
		sessionContainer.getThemeCloudSessionBean().setThemeCloudName(themeCloudName);
		String themeCloudCreatorActive = request.getParameter("themeCloudCreatorActive");
		String themeCloudViewerActive = request.getParameter("themeCloudViewerActive");
		String syntagCloudParameterVisible = request.getParameter("syntagCloudParameterVisible");
		
		if (themeCloudCreatorActive != null) {
			sessionContainer.getThemeCloudSessionBean().setThemeCloudCreatorActive(Integer.parseInt(themeCloudCreatorActive));
		}
		
		if (themeCloudViewerActive != null) {
			sessionContainer.getThemeCloudSessionBean().setThemeCloudViewerActive(Integer.parseInt(themeCloudViewerActive));
		}
		
		if (syntagCloudParameterVisible != null) {
			sessionContainer.getThemeCloudSessionBean().setSyntagCloudParameterVisible(Integer.parseInt(syntagCloudParameterVisible));
		}
		
		String minSyntag_String = request.getParameter("minSyntag");
		String syntagmaticEntityTermFactor_String = request.getParameter("syntagmaticEntityTermFactor");
		String a_String = request.getParameter("factor_a");
		String b_String = request.getParameter("factor_b");
		
		float minSyntag = ProcessingConstants.DEFAULT_MIN_TOP_TERM_SYNTAG;
		float syntagmaticEntityTermFactor = ProcessingConstants.DEFAULT_SYNTAGMATIC_ENTITY_TERM_FACTOR;
		float a = ProcessingConstants.DEFAULT_A;
		float b = ProcessingConstants.DEFAULT_B;
		
		if (minSyntag_String != null && !minSyntag_String.isEmpty()) {
			try {
				float f = Float.parseFloat(minSyntag_String);
				minSyntag = f;
			} catch(NumberFormatException e) {
				logger.error("parameter minSyntag is no float, using default value.");
			}
		}
		
		if (syntagmaticEntityTermFactor_String != null && !syntagmaticEntityTermFactor_String.isEmpty()) {
			try {
				float f = Float.parseFloat(syntagmaticEntityTermFactor_String);
				syntagmaticEntityTermFactor = f;
			} catch(NumberFormatException e) {
				logger.error("parameter syntagmaticEntityTermFactor is no float, using default value.");
			}
		}
		
		if (a_String != null && !a_String.isEmpty()) {
			try {
				float f = Float.parseFloat(a_String);
				a = f;
			} catch(NumberFormatException e) {
				logger.error("parameter a is no float, using default value.");
			}
		}
		
		if (b_String != null && !b_String.isEmpty()) {
			try {
				float f = Float.parseFloat(b_String);
				b = f;
			} catch(NumberFormatException e) {
				logger.error("parameter b is no float, using default value.");
			}
		}
		
		sessionContainer.getThemeCloudSessionBean().setMinSyntag(minSyntag);
		sessionContainer.getThemeCloudSessionBean().setSyntagmaticEntityTermFactor(syntagmaticEntityTermFactor);
		sessionContainer.getThemeCloudSessionBean().setA(a);
		sessionContainer.getThemeCloudSessionBean().setB(b);
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	public String task_moveNode(HttpServletRequest request) {
		
		String node1_String = request.getParameter("dnd_node1");
		String node2_String = request.getParameter("dnd_node2");
		String target1_String = request.getParameter("dnd_target1");
		Integer node1ID = 0, node2ID = 0;
		
		if (node1_String != null) {
			node1ID = Integer.parseInt(node1_String);
		}
		
		if (node2_String != null) {
			node2ID = Integer.parseInt(node2_String);
		}
		
		if (node1ID != 0) {
			
			SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
			ThemeCloudSessionBean themeCloudSessionBean = sessionContainer.getThemeCloudSessionBean();
			// 1. move node on node
			if (node2ID != 0) {
				// nothing to do now
			} else
			// 2. move node on target
			if (target1_String != null && !target1_String.isEmpty()) {
				for (Term cloudTerm : themeCloudSessionBean.getThemeCloud()) {
					if (cloudTerm.getId() == node1ID) {
						if (target1_String.equals("dnd_target_white")) {
							cloudTerm.setRating(0);
						} else
						if (target1_String.equals("dnd_target_gray")) {
							cloudTerm.setRating(1);
						} else
						if (target1_String.equals("dnd_target_red")) {
							cloudTerm.setRating(2);
						}
						break;
					} 
				}
			// 3. move node into nirvana
			} else {
				Term term = null;
				for (Term cloudTerm : themeCloudSessionBean.getThemeCloud()) {
					if (cloudTerm.getId() == node1ID) {
						term = cloudTerm;
					}
				}
				if (term != null) {
					themeCloudSessionBean.getThemeCloud().remove(term);
				}
			}
			new ThemeCloudBusiness().calcSyntagCloud(sessionContainer, 
					sessionContainer.getThemeCloudSessionBean().getMinSyntag(), 
					sessionContainer.getThemeCloudSessionBean().getSyntagmaticEntityTermFactor(),
					sessionContainer.getThemeCloudSessionBean().getA(),
					sessionContainer.getThemeCloudSessionBean().getB());
			request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
		}
		
		return ThemeCloudConstants.FORWARD_THEMECLOUD_JSP;
	}
	
	public String task_addNewTerm(HttpServletRequest request) {
		
		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
		this.getBasicParameter(request, sessionContainer);
		
		List<Term> termsToAdd = new ArrayList<Term>();
		
		// new term from user input
		String inputTerm = request.getParameter("inputTerm");
		if (inputTerm != null && !inputTerm.isEmpty()) {
			inputTerm = inputTerm.trim();
			inputTerm = inputTerm.toLowerCase();
			Term term = new Term(inputTerm);
			term.setRating(0);
			term.setWeighting(1.0f);
			termsToAdd.add(term);
		}
		
		new ThemeCloudBusiness().addTermToTermCloud(sessionContainer, termsToAdd, 
				sessionContainer.getThemeCloudSessionBean().getMinSyntag(), 
				sessionContainer.getThemeCloudSessionBean().getSyntagmaticEntityTermFactor(),
				sessionContainer.getThemeCloudSessionBean().getA(),
				sessionContainer.getThemeCloudSessionBean().getB());
		request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
		
		return ThemeCloudConstants.FORWARD_THEMECLOUD_JSP;
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	private String task_addSyntagCloudTerms(HttpServletRequest request) {
		
		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
		this.getBasicParameter(request, sessionContainer);
		
		List<Term> termsToAdd = new ArrayList<Term>();
		
		
		// terms from syntag cloud	
		String rating_String;
		//String weighting_String = null;
		float weighting = 1.0f;
		int rating;
		int index = 0;
		while(request.getParameter("rating_"+index) != null) {
			rating_String = request.getParameter("rating_"+index);
			rating = Integer.parseInt(rating_String);
			/*if (rating == 0) {
				weighting_String = request.getParameter("weightingInput_"+index);
				if (weighting_String != null) {
					try {
						weighting = Float.parseFloat(weighting_String);
					} catch(NumberFormatException ex) {
						logger.error(ex);
					}
				}
			}*/
			sessionContainer.getThemeCloudSessionBean().getSyntagTerms().get(index).setRating(rating);
			sessionContainer.getThemeCloudSessionBean().getSyntagTerms().get(index).setWeighting(weighting);
			termsToAdd.add(sessionContainer.getThemeCloudSessionBean().getSyntagTerms().get(index));
			index++;
		}
		
		new ThemeCloudBusiness().addTermToTermCloud(sessionContainer, termsToAdd, 
				sessionContainer.getThemeCloudSessionBean().getMinSyntag(), 
				sessionContainer.getThemeCloudSessionBean().getSyntagmaticEntityTermFactor(),
				sessionContainer.getThemeCloudSessionBean().getA(),
				sessionContainer.getThemeCloudSessionBean().getB());
		request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
		
		return ThemeCloudConstants.FORWARD_THEMECLOUD_JSP;
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	private String task_setWeighting(HttpServletRequest request) {
		
		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
		this.getBasicParameter(request, sessionContainer);
		
		String weighting_String;
		for(Term themeCloudTerm : sessionContainer.getThemeCloudSessionBean().getThemeCloud()) {
			weighting_String = request.getParameter("weightingInput_"+themeCloudTerm.getId());
			if (weighting_String != null) {
				try {
						themeCloudTerm.setWeighting(Float.parseFloat(weighting_String));
					} catch(NumberFormatException ex) {
						logger.error(ex);
					}
			}
		}
		
		request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
		
		return ThemeCloudConstants.FORWARD_THEMECLOUD_JSP;
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	private String task_calcSyntagCloud(HttpServletRequest request) {
		
		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
		this.getBasicParameter(request, sessionContainer);
		new ThemeCloudBusiness().calcSyntagCloud(sessionContainer, 
				sessionContainer.getThemeCloudSessionBean().getMinSyntag(), 
				sessionContainer.getThemeCloudSessionBean().getSyntagmaticEntityTermFactor(),
				sessionContainer.getThemeCloudSessionBean().getA(),
				sessionContainer.getThemeCloudSessionBean().getB());
		request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
		
		return ThemeCloudConstants.FORWARD_THEMECLOUD_JSP;
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	private String task_saveThemeCloud(HttpServletRequest request, User user) {
		
		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
		String themeCloudName = request.getParameter("themeCloudName");
		sessionContainer.getThemeCloudSessionBean().setThemeCloudName(themeCloudName);
		sessionContainer.getThemeCloudSessionBean().setThemeCloudCreatorActive(1);
		sessionContainer.getThemeCloudSessionBean().setThemeCloudViewerActive(0);
		
		List<Term> themeCloudTerms = sessionContainer.getThemeCloudSessionBean().getThemeCloud();
		
		if (themeCloudName != null && !themeCloudName.isEmpty() && !themeCloudTerms.isEmpty()) {
			new ThemeCloudBusiness().saveThemeCloud(request, themeCloudName, themeCloudTerms, user.getId());	
		} else {
			logger.error("ThemeCloud not saved, no name or no terms set.");
		}
		
		return ThemeCloudConstants.FORWARD_THEMECLOUD_JSP;
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	private String task_resetThemeCloud(HttpServletRequest request) {
		
		new ThemeCloudBusiness().initThemeCloudForm(request, true);
		return ThemeCloudConstants.FORWARD_THEMECLOUD_JSP;
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	private String task_editThemeCloud(HttpServletRequest request) {
		
		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
		this.getBasicParameter(request, sessionContainer);
		ThemeCloudSessionBean themeCloudSessionBean = sessionContainer.getThemeCloudSessionBean();
		String selectedThemeCloudID_String = request.getParameter("selectedThemeCloudID");
		int selectedThemeCloudID = 0;
		if (selectedThemeCloudID_String != null && !selectedThemeCloudID_String.isEmpty()) {
			selectedThemeCloudID = Integer.parseInt(selectedThemeCloudID_String);
		}
		if (selectedThemeCloudID != 0) {
			ThemeCloud themeCloud = null;
			for (ThemeCloud tc : themeCloudSessionBean.getThemeClouds()) {
				if (tc.getId() == selectedThemeCloudID) {
					themeCloud = tc;
					break;
				}
			}
			if (themeCloud != null) {
				themeCloudSessionBean.setThemeCloud(themeCloud.getTerms());
				themeCloudSessionBean.setThemeCloudName(themeCloud.getThemeCloudName());
				themeCloudSessionBean.setThemeCloudCreatorActive(1);
				themeCloudSessionBean.setThemeCloudViewerActive(0);
			}
		}
		new ThemeCloudBusiness().calcSyntagCloud(sessionContainer, 
			themeCloudSessionBean.getMinSyntag(), 
			themeCloudSessionBean.getSyntagmaticEntityTermFactor(),
			themeCloudSessionBean.getA(),
			themeCloudSessionBean.getB());
		request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
		return ThemeCloudConstants.FORWARD_THEMECLOUD_JSP;
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	private String task_deleteThemeCloud(HttpServletRequest request) {
		
		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
		ThemeCloudSessionBean themeCloudSessionBean = sessionContainer.getThemeCloudSessionBean();
		String selectedThemeCloudID_String = request.getParameter("selectedThemeCloudID");
		int selectedThemeCloudID = 0;
		if (selectedThemeCloudID_String != null && !selectedThemeCloudID_String.isEmpty()) {
			selectedThemeCloudID = Integer.parseInt(selectedThemeCloudID_String);
		}
		if (selectedThemeCloudID != 0) {
			ThemeCloud themeCloud = null;
			for (ThemeCloud tc : themeCloudSessionBean.getThemeClouds()) {
				if (tc.getId() == selectedThemeCloudID) {
					themeCloud = tc;
					break;
				}
			}
			if (themeCloud != null) {
				if (new ThemeCloudBusiness().deleteThemeCloud(request, themeCloud.getId())) {
					themeCloudSessionBean.getThemeClouds().remove(themeCloud);
				}
				themeCloudSessionBean.setThemeCloudCreatorActive(0);
				themeCloudSessionBean.setThemeCloudViewerActive(1);
			}
		}
		request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
		return ThemeCloudConstants.FORWARD_THEMECLOUD_JSP;
	}
}
