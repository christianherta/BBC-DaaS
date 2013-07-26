package de.bbcdaas.themehandlerweb.control;

import de.bbcdaas.common.beans.User;
import de.bbcdaas.themehandlerweb.beans.SessionContainer;
import de.bbcdaas.themehandlerweb.business.ThemeHandlerBusiness;
import de.bbcdaas.themehandlerweb.constants.Constants;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Robert Illers
 */
@Controller
public class UserManagementController {

	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 *
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/usermanagement.do")
	public String showUserManagementForm() {
		return Constants.FORWARD_USERMANAGEMENT_JSP;
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/handleusermanagementform.do")
	public String handleUserManagementForm(HttpServletRequest request) {

		String task = request.getParameter("task");
		logger.info("task: "+task);

		if (task != null) {

			if (task.equals("addUser")) {
				this.task_addUser(request);
			} else
			if (task.equals("deleteUser")) {
				this.task_deleteUser(request);
			}
		}

		return Constants.AJAX_USER_ADD_N_DELETE_JSP;
	}

	/**
	 *
	 * @param request
	 */
	private void task_addUser(HttpServletRequest request) {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String isAdmin = request.getParameter("isAdmin");
		int role = Constants.ROLE_TESTER;
		String passwordRepeat = request.getParameter("passwordRepeat");

		// validate
		if (username != null && password != null && passwordRepeat != null &&
			password.equals(passwordRepeat)) {

			if (isAdmin != null) {
				role = Constants.ROLE_ADMIN;
			}

			ThemeHandlerBusiness business = new ThemeHandlerBusiness();
			User user = business.addUser(username, password, role);
			if (user != null) {
				SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(Constants.KEY_SESSION_CONTAINER);
				sessionContainer.getUserList().add(user);
				request.getSession().setAttribute(Constants.KEY_SESSION_CONTAINER, sessionContainer);
			}
		}
	}

	/**
	 *
	 * @param request
	 */
	private void task_deleteUser(HttpServletRequest request) {

		String userID_String = request.getParameter("userList");

		if (userID_String != null) {

			int userID = Integer.parseInt(userID_String);
			if (new ThemeHandlerBusiness().deleteUser(userID)) {
				SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(Constants.KEY_SESSION_CONTAINER);
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
				request.getSession().setAttribute(Constants.KEY_SESSION_CONTAINER, sessionContainer);
			}
		}
	}
}
