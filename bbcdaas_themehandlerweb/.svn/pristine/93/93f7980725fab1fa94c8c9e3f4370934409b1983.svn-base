package de.bbcdaas.themehandlerweb.control;

import de.bbcdaas.themehandlerweb.business.ThemeHandlerBusiness;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Robert Illers
 */
@Controller
public class LoginController {

	/**
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/login.do", method = RequestMethod.GET)
	public String login(ModelMap model) {

		return "login";

	}

	/**
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/loginfailed.do", method = RequestMethod.GET)
	public String loginFailed(ModelMap model) {

		model.addAttribute("error", "true");
		return "login";

	}

	/**
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/logout.do", method = RequestMethod.GET)
	public String logout(ModelMap model) {

		return "login";

	}

	/**
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/loginsuccess.do", method = RequestMethod.GET)
	public String loginSuccess(HttpServletRequest request) {

		ThemeHandlerBusiness business = new ThemeHandlerBusiness();
		business.initUserSession(request, business.getUserDetails());
		return "startpage";
	}
}