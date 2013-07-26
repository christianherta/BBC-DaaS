package de.bbcdaas.visualizer.control.statistics;

import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import de.bbcdaas.taghandler.writer.statistics.StatisticWriter;
import de.bbcdaas.visualizer.beans.SessionContainer;
import de.bbcdaas.visualizer.beans.StatisticsSessionBean;
import de.bbcdaas.visualizer.constants.VisualizerConstants;
import de.bbcdaas.visualizer.control.BaseController;

/**
 *
 * @author Robert Illers
 */
public final class StartStatisticsWriter extends BaseController {

	private Thread statisticsThread;
	
    public ModelAndView handleRequestImpl(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
		StatisticsSessionBean statisticsSessionBean = sessionContainer.getStatisticsSessionBean();
		statisticsSessionBean.setName(VisualizerConstants.PROCESSNAME_STATISTICS_WRITER);
		
		try {
			StatisticWriter writer = (StatisticWriter)new ClassPathXmlApplicationContext("../applicationContext.xml").getBean("statisticWriter");
			if (statisticsThread != null && statisticsThread.isAlive()) {
				logger.debug("Thread already running");
			} else {
				statisticsThread = new Thread(writer);
				statisticsThread.start();
				logger.debug("Thread started");
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage());
			statisticsSessionBean.setRunning(false);
			statisticsSessionBean.setError(true);
			request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
			return new ModelAndView(VisualizerConstants.AJAX_SHOW_PROCESS_STATE_JSP);
		}
		statisticsSessionBean.setRunning(true);
		request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
		
        return new ModelAndView(VisualizerConstants.AJAX_SHOW_PROCESS_STATE_JSP);
    }   
}