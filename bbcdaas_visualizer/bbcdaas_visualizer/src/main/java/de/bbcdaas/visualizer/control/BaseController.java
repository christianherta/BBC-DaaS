package de.bbcdaas.visualizer.control;

import de.bbcdaas.visualizer.constants.VisualizerConstants;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author Robert Illers
 */
public abstract class BaseController implements Controller {

	protected final Logger logger = Logger.getLogger(this.getClass());

	private boolean menuOptionsUpdated = false;
	private boolean enableProcessing = false;
	private boolean enableStatistics = false;
	private boolean enableSearch = false;
	private boolean enableEvaluation = false;
	private boolean enableSyntagClouds = false;
	private boolean enableThemeClouds = false;
	private boolean enableFileOutput = false;

	public abstract ModelAndView handleRequestImpl(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public void setEnableProcessing(boolean enableProcessing) {
		this.enableProcessing = enableProcessing;
		menuOptionsUpdated = true;
	}

	public void setEnableStatistics(boolean enableStatistics) {
		this.enableStatistics = enableStatistics;
		menuOptionsUpdated = true;
	}

	public void setEnableSearch(boolean enableSearch) {
		this.enableSearch = enableSearch;
		menuOptionsUpdated = true;
	}

	public void setEnableEvaluation(boolean enableEvaluation) {
		this.enableEvaluation = enableEvaluation;
		menuOptionsUpdated = true;
	}

	public void setEnableSyntagClouds(boolean enableSyntagClouds) {
		this.enableSyntagClouds = enableSyntagClouds;
		menuOptionsUpdated = true;
	}

	public void setEnableThemeClouds(boolean enableThemeClouds) {
		this.enableThemeClouds = enableThemeClouds;
		menuOptionsUpdated = true;
	}

	public void setEnableFileOutput(boolean enableFileOutput) {
		this.enableFileOutput = enableFileOutput;
		menuOptionsUpdated = true;
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Boolean> menuConfig = (Map<String, Boolean>)request.getSession().getAttribute(VisualizerConstants.KEY_MENU_CONFIGURATION);
		if (menuConfig == null) {
			menuConfig = new HashMap<String, Boolean>();
		}
		if (menuOptionsUpdated) {
			menuConfig.put(VisualizerConstants.PARAM_ENABLE_PROCESSING, enableProcessing);
			menuConfig.put(VisualizerConstants.PARAM_ENABLE_STATISTICS, enableStatistics);
			menuConfig.put(VisualizerConstants.PARAM_ENABLE_SEARCH, enableSearch);
			menuConfig.put(VisualizerConstants.PARAM_ENABLE_EVALUATION, enableEvaluation);
			menuConfig.put(VisualizerConstants.PARAM_ENABLE_SYNTAG_CLOUDS, enableSyntagClouds);
			menuConfig.put(VisualizerConstants.PARAM_ENABLE_THEME_CLOUDS, enableThemeClouds);
			menuConfig.put(VisualizerConstants.PARAM_ENABLE_FILE_OUTPUT, enableFileOutput);
			request.getSession().getServletContext().setAttribute(VisualizerConstants.KEY_MENU_CONFIGURATION, menuConfig);
		}
		return handleRequestImpl(request, response);
	}

}
