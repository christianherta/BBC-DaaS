package de.bbcdaas.visualizer.business;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.User;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.taghandler.dao.TermLexiconDao;
import de.bbcdaas.visualizer.beans.SessionContainer;
import de.bbcdaas.visualizer.constants.VisualizerConstants;
import de.bbcdaas.visualizer.dao.VisualizerDao;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Robert Illers
 */
public final class VisualizerBusiness {

	private final Logger logger = Logger.getLogger(VisualizerBusiness.class);
	private TermLexiconDao taghandler_termLexiconDao;
	private VisualizerDao visualizerDao;

	/**
	 *
	 */
	public VisualizerBusiness() {
		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		this.taghandler_termLexiconDao = (TermLexiconDao)classPathXmlApplicationContext.getBean("taghandler_termlexiconDao");
		this.visualizerDao = (VisualizerDao)classPathXmlApplicationContext.getBean("visualizerDao");
	}

	/**
	 *
	 * @param userName
	 * @param password
	 * @return
	 */
	public User login(HttpServletRequest request, String userName, String password) {

		User user = null;

		try {
			user = visualizerDao.getUserByNameAndPassword(userName, password);
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
		}

		if (user == null) {
			logger.info("Login with username '"+userName+"' failed.");
		} else {

			this.initUserSession(request, user);
		}

		return user;
	}

	/**
	 *
	 * @param user
	 */
	private void initUserSession(HttpServletRequest request, User user) {

		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);

		if (sessionContainer == null) {
			sessionContainer = new SessionContainer();
		}

		if (user.getRole() == VisualizerConstants.ROLE_ADMIN) {
			sessionContainer.setUserList(new VisualizerBusiness().getAllUser());
		}
		request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
	}

	/**
	 *
	 * @param name
	 * @param password
	 * @param role
	 * @return
	 */
	public User addUser(String name, String password, int role) {

		User user = null;
		try {
			user = visualizerDao.insertUser(name, role, password);
		} catch (DaoException e) {
			logger.error(e.getCompleteMessage());
		}
		return user;
	}

	/**
	 *
	 * @param userID
	 * @return
	 */
	public boolean deleteUser(int userID) {

		try {
			visualizerDao.deleteUser(userID);
		} catch (DaoException e) {
			logger.error(e.getCompleteMessage());
			return false;
		}
		return true;
	}

	/**
	 *
	 * @return
	 */
	public List<User> getAllUser() {

		List<User> userList = new ArrayList<User>();
		try {
			userList = visualizerDao.getAllUser();
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
		}
		return userList;
	}

	/**
	 *
	 * @param value
	 * @return
	 */
	public Term getTermByValue(String value) {

		try {
			return taghandler_termLexiconDao.getTerm(value);
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
			return null;
		}
	}

	/**
	 *
	 * @return
	 */
	public static String generateWebsiteURL(HttpServletRequest request) {

		List<String> localhostURLs = new ArrayList<String>();
		localhostURLs.add("0.0.0.0"); // for remote servers
		localhostURLs.add("0:0:0:0:0:0:0:1"); // ip v6
		localhostURLs.add("127.0.0.1"); // local loop

		StringBuilder websiteURL = new StringBuilder();
		websiteURL.append("http://");
		if (localhostURLs.contains(request.getLocalName())) {
			websiteURL.append("localhost");
		} else {
			websiteURL.append(request.getLocalName());
		}
		websiteURL.append(":").append(request.getLocalPort());
		websiteURL.append(request.getContextPath());

		return websiteURL.toString();
	}
}
