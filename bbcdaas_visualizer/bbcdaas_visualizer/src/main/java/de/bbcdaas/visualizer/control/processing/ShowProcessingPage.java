package de.bbcdaas.visualizer.control.processing;

import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import de.bbcdaas.visualizer.constants.VisualizerConstants;
import de.bbcdaas.visualizer.control.BaseController;

/**
 *
 * @author Robert Illers
 */
public final class ShowProcessingPage extends BaseController {
	
    public ModelAndView handleRequestImpl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
        return new ModelAndView(VisualizerConstants.FORWARD_PROCESSING_JSP);
    }
    
}