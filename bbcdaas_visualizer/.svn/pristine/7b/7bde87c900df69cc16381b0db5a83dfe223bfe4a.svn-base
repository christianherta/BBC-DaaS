package de.bbcdaas.visualizer.control.evaluation;

import de.bbcdaas.common.beans.TermToTermsGroup;
import de.bbcdaas.common.beans.TermToTermsGroups;
import de.bbcdaas.taghandler.constants.ProcessingConstants;
import de.bbcdaas.visualizer.beans.EvaluationSessionBean;
import de.bbcdaas.visualizer.beans.SessionContainer;
import de.bbcdaas.visualizer.business.EvaluationBusiness;
import de.bbcdaas.visualizer.constants.EvaluationConstants;
import de.bbcdaas.visualizer.constants.VisualizerConstants;
import de.bbcdaas.visualizer.control.BaseController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Robert Illers
 */
public class HandleEvaluationForm extends BaseController {
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@Override
	public ModelAndView handleRequestImpl(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		
		String task = request.getParameter("task");
		
		String forward = null;
		
		if (task != null) {
			
			/* add a new random terms group */
			if (task.equals("addNewRandomTermsGroup")) {
				
				/* default parameter */
				int minNumberOfTopRelatedTerms = EvaluationConstants.DEFAULT_MIN_NUMBER_OF_TOP_RELATED_TERMS;
				int maxRandomTries = EvaluationConstants.DEFAULT_MAX_RANDOM_TRIES;
				int numberOfRandomTerms = EvaluationConstants.DEFAULT_NUMBER_OF_RANDOM_TERMS;
				float minTopTermSyntag = ProcessingConstants.DEFAULT_MIN_TOP_TERM_SYNTAG;
				/* /default parameter */
				
				// get parameter from form
				String numberOfRandomTerms_String = request.getParameter("numberOfRandomTerms");
				String groupLabel = request.getParameter("groupLabel");
				String minTopTermSyntag_String = request.getParameter("minTopTermSyntag");
				
				if (groupLabel == null) {
					groupLabel = "";
				}
				
				if (numberOfRandomTerms_String != null) {
					try {
						int i = Integer.parseInt(numberOfRandomTerms_String);
						numberOfRandomTerms = i; 
					} catch(NumberFormatException e) {
						// ignore
					}
				}
				
				if (minTopTermSyntag_String != null) {
					try {
						int i = Integer.parseInt(minTopTermSyntag_String);
						minTopTermSyntag = i; 
					} catch(NumberFormatException e) {
						// ignore
					}
				}
				
				logger.debug("minTopTermSyntag: "+minTopTermSyntag);
				forward = this.task_addNewRandomTermsGroup(request, numberOfRandomTerms, minNumberOfTopRelatedTerms,
					maxRandomTries, groupLabel, minTopTermSyntag);
			} else
			/* /add a new random terms group */
				
			/* display the terms of a random terms group after selection */
			if (task.equals("selectRandomTermsGroup")) {
				
				forward = this.task_selectRandomTermsGroup(request);
			} else
			/* /display the terms of a random terms group after selection */
				
			/* show ratedTerm table after random term selection */
			if (task.equals("selectRandomTerm")) {
				
				forward = this.task_selectRandomTerm(request);
			} else
			/* /show ratedTerm table after random term selection */
		
			/* save the users input in the rated term table */
			if (task.equals("saveSample")) {
				
				forward = this.task_saveSample(request);
			} else
			/* /save the users input in the rated term table */
				
			if (task.equals("addTerm")) {
				
				forward = this.task_addAddedTerm(request);
			} else
				
			if (task.equals("removeTerm")) {
				
				forward = this.task_removeAddedTerm(request);
			} else
				
			/* remove the selected randomTermsGroup */
			if (task.equals("removeRandomTermsGroup")) {
				
				forward = this.task_removeRandomTermsGroup(request);
			} else
			/* /remove the selected randomTermsGroup */
				
			if (task.equals("removeRandomTerm")) {
				
				forward = this.task_removeRandomTerm(request);
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
	 * @param randomTermsGroupSize
	 * @param minNumberOfTopRelatedTerms
	 * @param maxRandomTries
	 * @param groupLabel
	 * @param minTopTermSyntag
	 * @return 
	 */
	private String task_addNewRandomTermsGroup(HttpServletRequest request,
		int randomTermsGroupSize, int minNumberOfTopRelatedTerms, int maxRandomTries, String groupLabel, float minTopTermSyntag) {
		
		new EvaluationBusiness().addNewRandomTermsGroup(request, randomTermsGroupSize,
			minNumberOfTopRelatedTerms, maxRandomTries, groupLabel, minTopTermSyntag);
		return EvaluationConstants.AJAX_SHOW_RANDOM_TERM_GROUPS_JSP; 
	}
	
	/**
	 * 
	 * @param request
	 * @param id_String
	 * @return 
	 */
	private String task_selectRandomTermsGroup(HttpServletRequest request) {
		
		String id_String = request.getParameter("id");
		if (id_String != null) {
			SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
			EvaluationSessionBean evaluationSessionBean = sessionContainer.getEvaluationSessionBean();
			evaluationSessionBean.setSelectedRandomTermsGroupID(Integer.parseInt(id_String));
			request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
			return EvaluationConstants.AJAX_SHOW_RANDOM_TERMS_JSP;
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param forward
	 * @param id_String
	 * @return 
	 */
	private String task_selectRandomTerm(HttpServletRequest request) {
		
		String id_String = request.getParameter("id");
		if (id_String != null) {
			SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
			EvaluationSessionBean evaluationSessionBean = sessionContainer.getEvaluationSessionBean();
			evaluationSessionBean.setSelectedRandomTermID(Integer.parseInt(id_String));
			for (TermToTermsGroups randomTermGroup : evaluationSessionBean.getRandomTermGroups()) {
				if (randomTermGroup.getGroupID() == evaluationSessionBean.getSelectedRandomTermsGroupID()) {
					for (TermToTermsGroup randomTerm : randomTermGroup.getTermToTermsGroups()) {
						if (evaluationSessionBean.getSelectedRandomTermID() == randomTerm.getTerm().getId()) {
							evaluationSessionBean.setCurrentSelectedRatingSaved(randomTerm.isSaved());
							break;
						}
					}
					break;
				}
			}
			request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
			return EvaluationConstants.AJAX_SHOW_RATED_TERMS_JSP;
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	private String task_saveSample(HttpServletRequest request) {
		
		new EvaluationBusiness().saveSample(request);
		return EvaluationConstants.AJAX_SHOW_RATED_TERMS_JSP;
	}
	
	/**
	 * add an "addedTerm" to a randomTermsGroup (user added)
	 * @param request
	 * @return 
	 */
	private String task_addAddedTerm(HttpServletRequest request) {
		
		new EvaluationBusiness().addAddedTerm(request);
		return EvaluationConstants.AJAX_SHOW_RATED_TERMS_JSP;
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	private String task_removeAddedTerm(HttpServletRequest request) {
		
		new EvaluationBusiness().removeAddedTerm(request);
		return EvaluationConstants.AJAX_SHOW_RATED_TERMS_JSP;
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	private String task_removeRandomTermsGroup(HttpServletRequest request) {
		
		new EvaluationBusiness().removeRandomTermsGroup(request);
		return EvaluationConstants.AJAX_SHOW_RANDOM_TERM_GROUPS_JSP;
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	private String task_removeRandomTerm(HttpServletRequest request) {
		
		new EvaluationBusiness().removeRandomTerm(request);
		return EvaluationConstants.AJAX_SHOW_RANDOM_TERMS_JSP;
	}
}
