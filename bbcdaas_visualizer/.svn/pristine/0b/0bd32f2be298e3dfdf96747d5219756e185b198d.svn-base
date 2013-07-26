package de.bbcdaas.visualizer.control.themecloud;

import de.bbcdaas.visualizer.business.ThemeCloudBusiness;
import de.bbcdaas.visualizer.constants.ThemeCloudConstants;
import de.bbcdaas.visualizer.control.BaseController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Robert Illers
 */
public class ShowThemeCloudPage extends BaseController {

	@Override
	public ModelAndView handleRequestImpl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		new ThemeCloudBusiness().initThemeCloudForm(request, false);
		return new ModelAndView(ThemeCloudConstants.FORWARD_THEMECLOUD_JSP);
	}
	
}
