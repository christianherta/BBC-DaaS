package de.bbcdaas.visualizer.control.common;

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
public final class ShowStartPage extends BaseController {
	
	@Override
    public ModelAndView handleRequestImpl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.getSession().getServletContext().setAttribute(VisualizerConstants.KEY_WEBSITE_URL, VisualizerBusiness.generateWebsiteURL(request));	
		return new ModelAndView(VisualizerConstants.FORWARD_STARTPAGE_JSP);
    }
}
