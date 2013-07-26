package de.bbcdaas.visualizer.control.syntagclouds;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.taghandler.constants.ProcessingConstants;
import de.bbcdaas.visualizer.beans.SessionContainer;
import de.bbcdaas.visualizer.beans.SyntagCloudSessionBean;
import de.bbcdaas.visualizer.business.SyntagCloudsBusiness;
import de.bbcdaas.visualizer.business.VisualizerBusiness;
import de.bbcdaas.visualizer.constants.SyntagCloudsConstants;
import de.bbcdaas.visualizer.constants.VisualizerConstants;
import de.bbcdaas.visualizer.control.BaseController;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Robert Illers
 */
public class HandleSyntagCloudsForm extends BaseController {
	
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
			
			if (task.equals("getRandomEntity")) {
				
				forward = this.task_getRandomEntity(request);
			} else
			
			if (task.equals("searchForEntity")) {
				
				forward = this.task_searchForEntity(request);
			} else
				
			if (task.equals("computeSyntagClouds")) {
				
				forward = this.task_computeSyntagClouds(request);
			} else
			
			if (task.equals("selectPrevEntity")) {
				
				forward = this.task_selectPrevEntity(request);
			} else
				
			if (task.equals("selectNextEntity")) {
				
				forward = this.task_selectNextEntity(request);
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
	private String task_getRandomEntity(HttpServletRequest request) {
		
		Entity randomEntity = new SyntagCloudsBusiness().getRandomEntity();
		
		if (randomEntity != null) {
			SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
			SyntagCloudSessionBean syntagCloudSessionBean = sessionContainer.getSyntagCloudSessionBean();
			syntagCloudSessionBean.resetForGetRandomEntity();
			syntagCloudSessionBean.addEntity(randomEntity);
			request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
		}
		
		return SyntagCloudsConstants.AJAX_SHOW_RANDOM_ENTITY_JSP;
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	private String task_searchForEntity(HttpServletRequest request) {
		
		String term1Value = request.getParameter("term1");
		String term2Value = request.getParameter("term2");
		String term3Value = request.getParameter("term3");
		
		List<String> termValues = new ArrayList<String>();
		
		if (term1Value != null && !term1Value.isEmpty()) {
			termValues.add(term1Value);
		}
		if (term2Value != null && !term2Value.isEmpty()) {
			termValues.add(term2Value);
		}
		if (term3Value != null && !term3Value.isEmpty()) {
			termValues.add(term3Value);
		}
		
		List<Term> terms = new ArrayList<Term>();
		VisualizerBusiness visulalizerBusiness = new VisualizerBusiness();
		
		Term term1 = visulalizerBusiness.getTermByValue(term1Value);
		Term term2 = visulalizerBusiness.getTermByValue(term2Value);
		Term term3 = visulalizerBusiness.getTermByValue(term3Value);
		
		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
		SyntagCloudSessionBean syntagCloudSessionBean = sessionContainer.getSyntagCloudSessionBean();
		syntagCloudSessionBean.resetForSearchForEntity();
		
		if (term1 != null) {
			syntagCloudSessionBean.setTerm1Valid(terms.add(term1));
		} else {
			syntagCloudSessionBean.setTerm1Valid(false);
		}
		if (term2 != null) {
			syntagCloudSessionBean.setTerm2Valid(terms.add(term2));
		} else {
			syntagCloudSessionBean.setTerm2Valid(false);
		}
		if (term3 != null) {
			syntagCloudSessionBean.setTerm3Valid(terms.add(term3));
		} else {
			syntagCloudSessionBean.setTerm3Valid(false);
		}
		
		List<Entity> entities = new SyntagCloudsBusiness().searchForEntities(terms);
		
		syntagCloudSessionBean.setTerm1Value(term1Value);
		syntagCloudSessionBean.setTerm2Value(term2Value);
		syntagCloudSessionBean.setTerm3Value(term3Value);
		syntagCloudSessionBean.setEntities(entities);
		request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
		
		return SyntagCloudsConstants.AJAX_SHOW_RANDOM_ENTITY_JSP;
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	private String task_computeSyntagClouds(HttpServletRequest request) {
		
		String minSyntag_String = request.getParameter("minSyntag");
		String syntagmaticEntityTermFactor_String = request.getParameter("syntagmaticEntityTermFactor");
		String a_String = request.getParameter("factor_a");
		String b_String = request.getParameter("factor_b");
		
		float minSyntag = ProcessingConstants.DEFAULT_MIN_TOP_TERM_SYNTAG;
		float syntagmaticEntityTermFactor = ProcessingConstants.DEFAULT_SYNTAGMATIC_ENTITY_TERM_FACTOR;
		float a = ProcessingConstants.DEFAULT_A;
		float b = ProcessingConstants.DEFAULT_B;
		
		if (minSyntag_String != null && !minSyntag_String.isEmpty()) {
			try {
				float f = Float.parseFloat(minSyntag_String);
				minSyntag = f;
			} catch(NumberFormatException e) {
				logger.error("parameter minSyntag is no float, using default value.");
			}
		}
		
		if (syntagmaticEntityTermFactor_String != null && !syntagmaticEntityTermFactor_String.isEmpty()) {
			try {
				float f = Float.parseFloat(syntagmaticEntityTermFactor_String);
				syntagmaticEntityTermFactor = f;
			} catch(NumberFormatException e) {
				logger.error("parameter syntagmaticEntityTermFactor is no float, using default value.");
			}
		}
		
		if (a_String != null && !a_String.isEmpty()) {
			try {
				float f = Float.parseFloat(a_String);
				a = f;
			} catch(NumberFormatException e) {
				logger.error("parameter a is no float, using default value.");
			}
		}
		
		if (b_String != null && !b_String.isEmpty()) {
			try {
				float f = Float.parseFloat(b_String);
				b = f;
			} catch(NumberFormatException e) {
				logger.error("parameter b is no float, using default value.");
			}
		}
		
		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
		SyntagCloudSessionBean syntagCloudSessionBean = sessionContainer.getSyntagCloudSessionBean();
		
		// start calculation
		syntagCloudSessionBean.getEntities().get(syntagCloudSessionBean.getEntityIndex()).setFields(new SyntagCloudsBusiness().
			computeSyntagTerms(syntagCloudSessionBean.getEntities().
			get(syntagCloudSessionBean.getEntityIndex()).getFields(), minSyntag, syntagmaticEntityTermFactor, a, b));
		
		syntagCloudSessionBean.setA(a);
		syntagCloudSessionBean.setB(b);
		syntagCloudSessionBean.setMinSyntag(minSyntag);
		syntagCloudSessionBean.setSyntagmaticEntityTermFactor(syntagmaticEntityTermFactor);
		syntagCloudSessionBean.setSyntagCloudsCalculatedForCurrentRandomEntity(true);
		
		request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
		return SyntagCloudsConstants.AJAX_SHOW_ENTITY_SYNTAG_CLOUDS_JSP;
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	public String task_selectPrevEntity(HttpServletRequest request) {
		
		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
		SyntagCloudSessionBean syntagCloudSessionBean = sessionContainer.getSyntagCloudSessionBean();
		if (syntagCloudSessionBean.decreaseEntityIndex()) {
			Entity entity = syntagCloudSessionBean.getSelectedEntity();
			new SyntagCloudsBusiness().setEntityData(entity);
			syntagCloudSessionBean.updateSelectedEntity(entity);
			syntagCloudSessionBean.setSyntagCloudsCalculatedForCurrentRandomEntity(false);
		}
		request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
		return SyntagCloudsConstants.AJAX_SHOW_RANDOM_ENTITY_JSP;
	}
	
	/**
	 * 
	 * @param request
	 * @return 
	 */
	public String task_selectNextEntity(HttpServletRequest request) {
		
		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
		SyntagCloudSessionBean syntagCloudSessionBean = sessionContainer.getSyntagCloudSessionBean();
		if (syntagCloudSessionBean.increaseEntityIndex()) {
			Entity entity = syntagCloudSessionBean.getSelectedEntity();
			new SyntagCloudsBusiness().setEntityData(entity);
			syntagCloudSessionBean.updateSelectedEntity(entity);
			syntagCloudSessionBean.setSyntagCloudsCalculatedForCurrentRandomEntity(false);
		}
		request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
		return SyntagCloudsConstants.AJAX_SHOW_RANDOM_ENTITY_JSP;
	}
}
