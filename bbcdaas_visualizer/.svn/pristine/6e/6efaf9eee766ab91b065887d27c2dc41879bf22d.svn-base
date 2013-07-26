package de.bbcdaas.visualizer.control.common;

import de.bbcdaas.common.beans.User;
import de.bbcdaas.visualizer.beans.SessionContainer;
import de.bbcdaas.visualizer.business.VisualizerBusiness;
import de.bbcdaas.visualizer.constants.VisualizerConstants;
import de.bbcdaas.visualizer.control.BaseController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Robert Illers
 */
public class HandleUserManagementForm extends BaseController {

	@Override
	public ModelAndView handleRequestImpl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String task = request.getParameter("task");
		
		String forward = VisualizerConstants.AJAX_USER_ADD_N_DELETE_JSP;
		
		if (task != null) {
			
			/* add user */
			if (task.equals("addUser")) {
				
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				String isAdmin = request.getParameter("isAdmin");
				int role = VisualizerConstants.ROLE_TESTER;
				String passwordRepeat = request.getParameter("passwordRepeat");
				
				// validate
				if (username == null || password == null || passwordRepeat == null || !password.equals(passwordRepeat)) {
					return new ModelAndView(forward);
				}
				if (isAdmin != null) {
					role = VisualizerConstants.ROLE_ADMIN;
				}
				User user = new VisualizerBusiness().addUser(username, password, role);
				if (user != null) {
					SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
					sessionContainer.getUserList().add(user);
					request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
				}
				
			} else 
			/* /add user */
				
			/* delete user */
			if (task.equals("deleteUser")) {
				
				String userID_String = request.getParameter("userList");
				if (userID_String != null) {
					int userID = Integer.parseInt(userID_String);
					if (new VisualizerBusiness().deleteUser(userID)) {
						
						SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
						int i = 0;
						for (User user : sessionContainer.getUserList()) {
							if (user.getId() == userID) {
								break;
							}
							i++;
						}
						if (i != sessionContainer.getUserList().size()) {
							sessionContainer.getUserList().remove(i);
						}
						request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
					}
				}
			}
			/* /delete user */
		}
		
		return new ModelAndView(forward);
	}
	
}
