package de.bbcdaas.visualizer.control.fileoutput;

import de.bbcdaas.visualizer.business.FileOutputBusiness;
import de.bbcdaas.visualizer.constants.FileOutputConstants;
import de.bbcdaas.visualizer.control.BaseController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Robert Illers
 */
public class HandleFileOutputForm extends BaseController {
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@Override
	public ModelAndView handleRequestImpl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String task = request.getParameter("task");
		
		String forward = null;
		
		if (task != null) {
			
			if (task.equals("createTermDictionary")) {
				
				forward = this.task_createTermDictionary(request);
			} else
				
			if (task.equals("createTrainingDataOutput")) {
				
				forward = this.task_createTrainingDataOutput(request);
			}
		}
		
		if (forward == null) {
			return null;
		} else {
			return new ModelAndView(forward);
		}
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	private String task_createTermDictionary(HttpServletRequest request) {
		
		String maxNumberOfTerms_String = request.getParameter("maxNumberOfTerms");
		int maxNumberOfTerms = 10000;
		
		if (maxNumberOfTerms_String != null && !maxNumberOfTerms_String.isEmpty()) {
			try {
				maxNumberOfTerms = Integer.parseInt(maxNumberOfTerms_String);
			} catch(NumberFormatException e) {
				logger.error(e);
			}
		}
		
		new FileOutputBusiness(request).createTermDictionary(maxNumberOfTerms);
		return FileOutputConstants.AJAX_SHOW_TERM_DICTIONARY_JSP;
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	private String task_createTrainingDataOutput(HttpServletRequest request) {
		
		String minMatchingTerms_String = request.getParameter("minMatchingTerms");
		int minMatchingTerms = FileOutputConstants.DEFAULT_MIN_MATCHING_TERMS;
		if (minMatchingTerms_String != null && !minMatchingTerms_String.isEmpty()) {
			try {
				minMatchingTerms = Integer.parseInt(minMatchingTerms_String);
			} catch(NumberFormatException e) {
				logger.error(e);
			}
		}
		
		new FileOutputBusiness(request).createTrainingDataOutput(0, minMatchingTerms);
		return FileOutputConstants.AJAX_SHOW_TRAINING_DATA_JSP;
	}
}
