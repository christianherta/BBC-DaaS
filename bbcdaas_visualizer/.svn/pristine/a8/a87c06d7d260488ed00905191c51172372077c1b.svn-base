package de.bbcdaas.visualizer.control.common;

import de.bbcdaas.common.beans.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import de.bbcdaas.visualizer.business.VisualizerBusiness;
import de.bbcdaas.visualizer.constants.VisualizerConstants;
import de.bbcdaas.visualizer.control.BaseController;

/**
 *
 * @author Robert Illers
 */
public class HandleLoginForm extends BaseController {
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@Override
	public ModelAndView handleRequestImpl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String forward = VisualizerConstants.FORWARD_LOGIN_FAILURE_JSP;
		boolean loggedIn = false;
		String username = request.getParameter("username");
		String password = request.getParameter("password");	

		User user = (User)request.getSession().getAttribute(VisualizerConstants.KEY_USER);
		if (user == null) {
			user = new VisualizerBusiness().login(request, username, password);
			loggedIn = true;
		}

		if (user != null) {

			if (loggedIn) {
				request.getSession().setAttribute(VisualizerConstants.KEY_USER, user);
			}
			forward = VisualizerConstants.FORWARD_STARTPAGE_JSP;
		}
		return new ModelAndView(forward);
	}
}
