package de.bbcdaas.themehandlerweb.control;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.ThemeCloud;
import de.bbcdaas.themehandlerweb.beans.SessionContainer;
import de.bbcdaas.themehandlerweb.beans.ThemeCloudSessionBean;
import de.bbcdaas.themehandlerweb.business.ThemeHandlerBusiness;
import de.bbcdaas.themehandlerweb.constants.Constants;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Robert Illers
 */
@Controller
public class ThemeHandlerController {

	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 *
	 * @return
	 */
	@RequestMapping("/startpage.do")
    public ModelAndView showStartPage() {
        return new ModelAndView("startpage");
    }

	/**
	 *
	 * @return
	 */
	@RequestMapping("/themeclouds.do")
	@Secured({"ROLE_ADMIN","ROLE_USER"})
    public ModelAndView showThemeClouds() {
        new ThemeHandlerBusiness().initThemeCloudForm(false);
		return new ModelAndView("themeclouds");
    }

	/**
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("/handlethemeclouds.do")
	@Secured({"ROLE_ADMIN","ROLE_USER"})
    public ModelAndView handleThemeClouds(HttpServletRequest request) {

		String task = request.getParameter("task");
		logger.info("task: "+task);
		String forward = Constants.FORWARD_THEMECLOUD_JSP;

		if (task != null) {

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

				forward = this.task_saveThemeCloud(request);
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
	 * @return
	 */
	public String task_moveNode(HttpServletRequest request) {

		String node1_String = request.getParameter("dnd_node1");
		String node2_String = request.getParameter("dnd_node2");
		String target1_String = request.getParameter("dnd_target1");
		Integer node1ID = 0, node2ID = 0;

		if (node1_String != null) {
			node1ID = Integer.parseInt(node1_String.substring(1));
		}

		if (node2_String != null) {
			node2ID = Integer.parseInt(node2_String.substring(1));
		}

		if (node1ID != 0) {

			SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(Constants.KEY_SESSION_CONTAINER);
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
			new ThemeHandlerBusiness().calcSyntagCloud(sessionContainer,
					sessionContainer.getThemeCloudSessionBean().getMinSyntag(),
					sessionContainer.getThemeCloudSessionBean().getSyntagmaticEntityTermFactor(),
					sessionContainer.getThemeCloudSessionBean().getA(),
					sessionContainer.getThemeCloudSessionBean().getB());
			request.getSession().setAttribute(Constants.KEY_SESSION_CONTAINER, sessionContainer);
		}

		return Constants.FORWARD_THEMECLOUD_JSP;
	}

	public String task_addNewTerm(HttpServletRequest request) {

		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(Constants.KEY_SESSION_CONTAINER);
		new ThemeHandlerBusiness().getBasicParameter(request, sessionContainer);

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

		new ThemeHandlerBusiness().addTermToTermCloud(sessionContainer, termsToAdd,
				sessionContainer.getThemeCloudSessionBean().getMinSyntag(),
				sessionContainer.getThemeCloudSessionBean().getSyntagmaticEntityTermFactor(),
				sessionContainer.getThemeCloudSessionBean().getA(),
				sessionContainer.getThemeCloudSessionBean().getB());
		request.getSession().setAttribute(Constants.KEY_SESSION_CONTAINER, sessionContainer);

		return Constants.FORWARD_THEMECLOUD_JSP;
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	private String task_addSyntagCloudTerms(HttpServletRequest request) {

		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(Constants.KEY_SESSION_CONTAINER);
		new ThemeHandlerBusiness().getBasicParameter(request, sessionContainer);

		List<Term> termsToAdd = new ArrayList<Term>();
		// terms from syntag cloud
		String rating_String;
		float weighting = 1.0f;
		int rating;
		int index = 0;

		while(request.getParameter("rating_"+index) != null) {

			rating_String = request.getParameter("rating_"+index);
			rating = Integer.parseInt(rating_String);
			sessionContainer.getThemeCloudSessionBean().getSyntagTerms().get(index).setRating(rating);
			sessionContainer.getThemeCloudSessionBean().getSyntagTerms().get(index).setWeighting(weighting);
			termsToAdd.add(sessionContainer.getThemeCloudSessionBean().getSyntagTerms().get(index));
			index++;
		}

		new ThemeHandlerBusiness().addTermToTermCloud(sessionContainer, termsToAdd,
				sessionContainer.getThemeCloudSessionBean().getMinSyntag(),
				sessionContainer.getThemeCloudSessionBean().getSyntagmaticEntityTermFactor(),
				sessionContainer.getThemeCloudSessionBean().getA(),
				sessionContainer.getThemeCloudSessionBean().getB());
		request.getSession().setAttribute(Constants.KEY_SESSION_CONTAINER, sessionContainer);

		return Constants.FORWARD_THEMECLOUD_JSP;
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	private String task_setWeighting(HttpServletRequest request) {

		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(Constants.KEY_SESSION_CONTAINER);
		new ThemeHandlerBusiness().getBasicParameter(request, sessionContainer);

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

		request.getSession().setAttribute(Constants.KEY_SESSION_CONTAINER, sessionContainer);

		return Constants.FORWARD_THEMECLOUD_JSP;
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	private String task_calcSyntagCloud(HttpServletRequest request) {

		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(Constants.KEY_SESSION_CONTAINER);
		new ThemeHandlerBusiness().getBasicParameter(request, sessionContainer);
		new ThemeHandlerBusiness().calcSyntagCloud(sessionContainer,
				sessionContainer.getThemeCloudSessionBean().getMinSyntag(),
				sessionContainer.getThemeCloudSessionBean().getSyntagmaticEntityTermFactor(),
				sessionContainer.getThemeCloudSessionBean().getA(),
				sessionContainer.getThemeCloudSessionBean().getB());
		request.getSession().setAttribute(Constants.KEY_SESSION_CONTAINER, sessionContainer);

		return Constants.FORWARD_THEMECLOUD_JSP;
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	private String task_saveThemeCloud(HttpServletRequest request) {

		SessionContainer sessionContainer = (SessionContainer)request.getSession().
			getAttribute(Constants.KEY_SESSION_CONTAINER);
		String themeCloudName = request.getParameter("themeCloudName");
		sessionContainer.getThemeCloudSessionBean().setThemeCloudName(themeCloudName);
		sessionContainer.getThemeCloudSessionBean().setThemeCloudCreatorActive(1);
		sessionContainer.getThemeCloudSessionBean().setThemeCloudViewerActive(0);

		List<Term> themeCloudTerms = sessionContainer.getThemeCloudSessionBean().getThemeCloud();

		if (themeCloudName != null && !themeCloudName.isEmpty() && !themeCloudTerms.isEmpty()) {

			ThemeHandlerBusiness business = new ThemeHandlerBusiness();
			UserDetails user = business.getUserDetails();
			business.saveThemeCloud(themeCloudName, themeCloudTerms, user.getUsername(), sessionContainer.getRestBaseURI());
			business.initThemeCloudForm(true);
		} else {
			logger.error("ThemeCloud not saved, no name or no terms set.");
		}

		return Constants.FORWARD_THEMECLOUD_JSP;
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	private String task_resetThemeCloud(HttpServletRequest request) {

		new ThemeHandlerBusiness().initThemeCloudForm(true);
		return Constants.FORWARD_THEMECLOUD_JSP;
	}

	/**
	 * Get selected themecloud and load it into edit form.
	 * @param request HttpServletRequest object
	 * @return forward
	 */
	private String task_editThemeCloud(HttpServletRequest request) {

		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(Constants.KEY_SESSION_CONTAINER);
		new ThemeHandlerBusiness().getBasicParameter(request, sessionContainer);
		ThemeCloudSessionBean themeCloudSessionBean = sessionContainer.getThemeCloudSessionBean();
		String selectedThemeCloudName = request.getParameter("selectedThemeCloudName");
		logger.info("selectedThemeCloudName: "+selectedThemeCloudName);
		if (selectedThemeCloudName != null) {
			ThemeCloud themeCloud = null;
			for (ThemeCloud tc : themeCloudSessionBean.getThemeClouds()) {
				if (tc.getThemeCloudName().equals(selectedThemeCloudName)) {
					themeCloud = tc;
					break;
				}
			}
			if (themeCloud != null) {
				themeCloudSessionBean.setThemeCloud(themeCloud.getTerms());
				themeCloudSessionBean.setThemeCloudName(themeCloud.getThemeCloudName());
				themeCloudSessionBean.setThemeCloudCreatorActive(1);
				themeCloudSessionBean.setThemeCloudViewerActive(0);
			} else {
				logger.error("Internal Error: can not find themeCloud in session bean.");
			}
		}
		new ThemeHandlerBusiness().calcSyntagCloud(sessionContainer,
			themeCloudSessionBean.getMinSyntag(),
			themeCloudSessionBean.getSyntagmaticEntityTermFactor(),
			themeCloudSessionBean.getA(),
			themeCloudSessionBean.getB());
		request.getSession().setAttribute(Constants.KEY_SESSION_CONTAINER, sessionContainer);
		return Constants.FORWARD_THEMECLOUD_JSP;
	}

	/**
	 * Delete a ThemeCloud.
	 * @param request HttpServletRequest object
	 * @return forward
	 */
	private String task_deleteThemeCloud(HttpServletRequest request) {

		SessionContainer sessionContainer = (SessionContainer)request.getSession().
			getAttribute(Constants.KEY_SESSION_CONTAINER);
		ThemeCloudSessionBean themeCloudSessionBean = sessionContainer.getThemeCloudSessionBean();
		String selectedThemeCloudName = request.getParameter("selectedThemeCloudName");

		// themecloud has a name
		if (selectedThemeCloudName != null) {

			ThemeCloud themeCloud = null;

			// get themeCloud to delete from session bean
			for (ThemeCloud tc : themeCloudSessionBean.getThemeClouds()) {

				if (tc.getThemeCloudName().equals(selectedThemeCloudName)) {
					themeCloud = tc;
					break;
				}
			}

			if (themeCloud != null) {

				if (new ThemeHandlerBusiness().deleteThemeCloud(themeCloud.getThemeCloudName(), sessionContainer.getRestBaseURI())) {
					themeCloudSessionBean.getThemeClouds().remove(themeCloud);
					new ThemeHandlerBusiness().initThemeCloudForm(true);
				}
				themeCloudSessionBean.setThemeCloudCreatorActive(0);
				themeCloudSessionBean.setThemeCloudViewerActive(1);
			}
		}
		request.getSession().setAttribute(Constants.KEY_SESSION_CONTAINER, sessionContainer);
		return Constants.FORWARD_THEMECLOUD_JSP;
	}
}
