package de.bbcdaas.visualizer.control.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import de.bbcdaas.visualizer.constants.VisualizerConstants;
import de.bbcdaas.visualizer.control.BaseController;

/**
 *
 * @author Robert Illers
 */
public class LogoutUser extends BaseController {

	@Override
	public ModelAndView handleRequestImpl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.getSession().removeAttribute(VisualizerConstants.KEY_USER);
		
		return new ModelAndView(VisualizerConstants.FORWARD_LOGOUT_SUCCESS_JSP);
	}
	
}
