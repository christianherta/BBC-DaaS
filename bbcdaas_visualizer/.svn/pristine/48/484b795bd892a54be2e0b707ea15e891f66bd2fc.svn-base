package de.bbcdaas.visualizer.control.evaluation;

import de.bbcdaas.common.beans.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import de.bbcdaas.visualizer.business.EvaluationBusiness;
import de.bbcdaas.visualizer.constants.EvaluationConstants;
import de.bbcdaas.visualizer.constants.VisualizerConstants;
import de.bbcdaas.visualizer.control.BaseController;

/**
 *
 * @author Robert Illers
 */
public class ShowEvaluationPage extends BaseController {
	
	public ModelAndView handleRequestImpl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String forward;
		User user = (User)request.getSession().getAttribute(VisualizerConstants.KEY_USER); 
		
		if (user == null) {
			forward = VisualizerConstants.FORWARD_LOGIN_JSP;
		} else {
			if (user.getRole() == VisualizerConstants.ROLE_ADMIN) {
				forward = EvaluationConstants.FORWARD_EVALUATION_ADMIN_JSP;
			} else {
				forward = EvaluationConstants.FORWARD_EVALUATION_TESTER_JSP;
			}
			new EvaluationBusiness().initEvaluationForm(request, user);	
		}
        return new ModelAndView(forward);
    }
}
