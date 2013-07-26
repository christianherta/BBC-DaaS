package de.bbcdaas.visualizer.control.common;

import de.bbcdaas.common.beans.User;
import de.bbcdaas.visualizer.constants.VisualizerConstants;
import de.bbcdaas.visualizer.control.BaseController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Robert Illers
 */
public class ShowUserManagement extends BaseController {

	@Override
	public ModelAndView handleRequestImpl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String forward = VisualizerConstants.FORWARD_STARTPAGE_JSP;
		User user = (User)request.getSession().getAttribute(VisualizerConstants.KEY_USER);
		
		if (user == null) {
			forward = VisualizerConstants.FORWARD_LOGIN_JSP;
		} else if (user.getRole() == VisualizerConstants.ROLE_ADMIN) {
			forward = VisualizerConstants.FORWARD_USER_MANAGEMENT_JSP;
		}
		
		return new ModelAndView(forward);
	}
	
}