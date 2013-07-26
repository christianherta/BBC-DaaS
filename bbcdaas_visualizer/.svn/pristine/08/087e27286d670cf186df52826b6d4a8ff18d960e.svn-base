package de.bbcdaas.visualizer.control.processing;

import de.bbcdaas.taghandler.TagHandler;
import de.bbcdaas.visualizer.beans.ProcessingSessionBean;
import de.bbcdaas.visualizer.beans.SessionContainer;
import de.bbcdaas.visualizer.constants.VisualizerConstants;
import de.bbcdaas.visualizer.control.BaseController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Robert Illers
 */
public final class StartProcessing extends BaseController {

	private Thread tagHandlerThread;
	 
	@Override
    public ModelAndView handleRequestImpl(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
		ProcessingSessionBean processingSessionBean = sessionContainer.getProcessingSessionBean();
		processingSessionBean.setName(VisualizerConstants.PROCESSNAME_PROCESSING);
		
		try {
			TagHandler tagHandler = (TagHandler)new ClassPathXmlApplicationContext("applicationContext.xml").getBean("tagHandler");
			if (tagHandlerThread != null && tagHandlerThread.isAlive()) {
				logger.debug("Thread already running");
			} else {
				tagHandlerThread = new Thread(tagHandler);
				tagHandlerThread.start();
				logger.debug("Thread started");
			}
			
		} catch(Exception ex) {
			logger.error(ex.getMessage());
			processingSessionBean.setRunning(false);
			processingSessionBean.setError(true);
			request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
			return new ModelAndView(VisualizerConstants.AJAX_SHOW_PROCESS_STATE_JSP);
		}
		processingSessionBean.setRunning(true);
		request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
		
        return new ModelAndView(VisualizerConstants.AJAX_SHOW_PROCESS_STATE_JSP);
    }   
}