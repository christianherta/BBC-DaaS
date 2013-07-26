package de.bbcdaas.visualizer.control.syntagclouds;

import de.bbcdaas.visualizer.business.SyntagCloudsBusiness;
import de.bbcdaas.visualizer.constants.SyntagCloudsConstants;
import de.bbcdaas.visualizer.control.BaseController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Robert Illers
 */
public class ShowSyntagCloudsPage extends BaseController {

	@Override
	public ModelAndView handleRequestImpl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		new SyntagCloudsBusiness().initSyntagCloudsForm(request);
		
		return new ModelAndView(SyntagCloudsConstants.FORWARD_SYNTAG_CLOUDS_JSP);
	}
	
}