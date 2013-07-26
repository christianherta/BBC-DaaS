package de.bbcdaas.themehandlerweb.business;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.ThemeCloud;
import de.bbcdaas.common.beans.User;
import de.bbcdaas.common.dao.api.JavaPersistenceAPI;
import de.bbcdaas.common.util.FileReader;
import de.bbcdaas.themehandlerweb.beans.SessionContainer;
import de.bbcdaas.themehandlerweb.constants.Constants;
import de.bbcdaas.themehandlerweb.dao.impl.jpa.ThemeHandlerWebDaoImpl;
import de.bbcdaas.themehandlerweb.domains.UserEntity;
import de.bbcdaas.webservices.api.taghandler.TagHandlerWebserviceAPI;
import de.bbcdaas.webservices.api.taghandler.beans.SyntagCloudResult;
import de.bbcdaas.webservices.api.taghandler.beans.TermsResult;
import de.bbcdaas.webservices.api.themehandler.ThemeHandlerWebserviceAPI;
import de.bbcdaas.webservices.api.themehandler.beans.ThemeCloudResult;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Business logic of the theme handler web application.
 * @author Robert Illers
 */
public class ThemeHandlerBusiness {

	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Return the current session.
	 * @return HttpSession
	 */
	public static HttpSession getSession() {

		ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.
			currentRequestAttributes();
		return attr.getRequest().getSession(true);
	}

	/**
	 *
	 * @param request
	 * @param user
	 */
	public void initUserSession(HttpServletRequest request, UserDetails user) {

		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(Constants.KEY_SESSION_CONTAINER);

		if (sessionContainer == null) {
			sessionContainer = new SessionContainer();
		}

		// load user list
		for (GrantedAuthority authority :user.getAuthorities()) {
			if (authority.getAuthority().equals("ROLE_ADMIN")) {
				sessionContainer.setUserList(this.getAllUser());
			}
		}

		// load configuration
		Configuration config = new FileReader().readPropertiesConfig(new StringBuilder("/properties/").
			append(Constants.CONFIGURATION_FILE_NAME).toString(),
			FileReader.FILE_OPENING_TYPE.ABSOLUTE, FileReader.FILE_OPENING_TYPE.RELATIVE, true);
		
		String restBaseURI = config.getString(Constants.CONFIG_PARAM_REST_BASE_URI);
		sessionContainer.setRestBaseURI(restBaseURI);

		request.getSession().setAttribute(Constants.KEY_SESSION_CONTAINER, sessionContainer);
	}

	/**
	 *
	 * @return
	 */
	private List<User> getAllUser() {

		List<User> users = new ArrayList<User>();
		ThemeHandlerWebDaoImpl dao = new ThemeHandlerWebDaoImpl();
		dao.setJavaPersistenceAPI(new JavaPersistenceAPI("PU"));
		List<UserEntity> userEntities = dao.getAllUser();
		for (UserEntity userEntity : userEntities) {
			User user = new User();
			user.setName(userEntity.getLoginName());
			user.setId(userEntity.getID());
			user.setRole(userEntity.getUserRole());
			users.add(user);
		}
		return users;
	}

	/**
	 *
	 * @param userName
	 * @param password
	 * @param role
	 */
	public User addUser(String userName, String password, int role) {

		ThemeHandlerWebDaoImpl dao = new ThemeHandlerWebDaoImpl();
		dao.setJavaPersistenceAPI(new JavaPersistenceAPI("PU"));
		User user = null;
		dao.insertUser(userName, password, role);
		UserEntity userEntity = dao.getUserByName(userName);
		if (userEntity != null) {
			user = new User();
			user.setId(userEntity.getID());
			user.setName(userEntity.getLoginName());
			user.setRole(userEntity.getUserRole());
		}
		return user;
	}

	/**
	 *
	 * @param userID
	 */
	public boolean deleteUser(int userID) {

		ThemeHandlerWebDaoImpl dao = new ThemeHandlerWebDaoImpl();
		dao.setJavaPersistenceAPI(new JavaPersistenceAPI("PU"));

		return dao.deleteUser(userID);
	}

	/**
	 * Returns the current user object (spring security).
	 * @return UserDetails
	 */
	public UserDetails getUserDetails() {

		SecurityContext context = SecurityContextHolder.getContext();
		Object principal = context.getAuthentication().getPrincipal();

		if (principal != null && principal instanceof UserDetails) {
			return (UserDetails)principal;
		}
		return null;
	}

	/**
	 * Prepares the theme cloud form or resets it if parameter clear is set.
	 * @param clear if tru, form will be reseted
	 */
	public void initThemeCloudForm(boolean clear) {

		SessionContainer sessionContainer = (SessionContainer)getSession().getAttribute(Constants.KEY_SESSION_CONTAINER);

		if (sessionContainer == null) {
			sessionContainer = new SessionContainer();
		}

		if (clear) {

			sessionContainer.getThemeCloudSessionBean().setA(Constants.DEFAULT_A);
			sessionContainer.getThemeCloudSessionBean().setB(Constants.DEFAULT_B);
			sessionContainer.getThemeCloudSessionBean().setMinSyntag(Constants.DEFAULT_MIN_TOP_TERM_SYNTAG);
			sessionContainer.getThemeCloudSessionBean().setSyntagmaticEntityTermFactor(Constants.DEFAULT_SYNTAGMATIC_ENTITY_TERM_FACTOR);
			sessionContainer.getThemeCloudSessionBean().getSyntagTerms().clear();
			sessionContainer.getThemeCloudSessionBean().getThemeCloud().clear();
			sessionContainer.getThemeCloudSessionBean().getColoredThemeCloud().clear();
			sessionContainer.getThemeCloudSessionBean().setThemeCloudName("");
			sessionContainer.getThemeCloudSessionBean().getThemeClouds().clear();
		}

		if (sessionContainer.getThemeCloudSessionBean().getThemeClouds().isEmpty()) {
			sessionContainer.getThemeCloudSessionBean().setThemeClouds(this.getThemeClouds(sessionContainer.getRestBaseURI()));
		}

		getSession().setAttribute(Constants.KEY_SESSION_CONTAINER, sessionContainer);
	}

	/**
	 * Gets all ThemeClouds stored in theme handler database via rest webservice.
	 * @return List<ThemeCloud>
	 */
	public List<ThemeCloud> getThemeClouds(String restBaseURI) {

		ThemeHandlerWebserviceAPI webservices = new ThemeHandlerWebserviceAPI(restBaseURI);
		ThemeCloudResult result = webservices.getAllThemeClouds();
		if (result != null) {
			logger.debug("ThemeClouds:");
			for (ThemeCloud themeCloud : result.getThemeClouds()) {
				logger.debug("Name: "+themeCloud.getThemeCloudName()+", User: "+themeCloud.getUser().getName());
			}
			return result.getThemeClouds();
		}
		return new ArrayList<ThemeCloud>();
	}

	/**
	 * Prepares the theme cloud form bys initializing current options and values.
	 * @param request
	 * @param sessionContainer
	 */
	public void getBasicParameter(HttpServletRequest request, SessionContainer sessionContainer) {

		String themeCloudName = request.getParameter("themeCloudName");
		if (themeCloudName == null) {
			themeCloudName = "";
		}
		sessionContainer.getThemeCloudSessionBean().setThemeCloudName(themeCloudName);
		String themeCloudCreatorActive = request.getParameter("themeCloudCreatorActive");
		String themeCloudViewerActive = request.getParameter("themeCloudViewerActive");
		String syntagCloudParameterVisible = request.getParameter("syntagCloudParameterVisible");

		if (themeCloudCreatorActive != null) {
			sessionContainer.getThemeCloudSessionBean().setThemeCloudCreatorActive(Integer.parseInt(themeCloudCreatorActive));
		}

		if (themeCloudViewerActive != null) {
			sessionContainer.getThemeCloudSessionBean().setThemeCloudViewerActive(Integer.parseInt(themeCloudViewerActive));
		}

		if (syntagCloudParameterVisible != null) {
			sessionContainer.getThemeCloudSessionBean().setSyntagCloudParameterVisible(Integer.parseInt(syntagCloudParameterVisible));
		}

		String minSyntag_String = request.getParameter("minSyntag");
		String syntagmaticEntityTermFactor_String = request.getParameter("syntagmaticEntityTermFactor");
		String a_String = request.getParameter("factor_a");
		String b_String = request.getParameter("factor_b");

		float minSyntag = Constants.DEFAULT_MIN_TOP_TERM_SYNTAG;
		float syntagmaticEntityTermFactor = Constants.DEFAULT_SYNTAGMATIC_ENTITY_TERM_FACTOR;
		float a = Constants.DEFAULT_A;
		float b = Constants.DEFAULT_B;

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

		sessionContainer.getThemeCloudSessionBean().setMinSyntag(minSyntag);
		sessionContainer.getThemeCloudSessionBean().setSyntagmaticEntityTermFactor(syntagmaticEntityTermFactor);
		sessionContainer.getThemeCloudSessionBean().setA(a);
		sessionContainer.getThemeCloudSessionBean().setB(b);
	}

	/**
	 * Sends a rest request to the taghandler service for a syntagmatic term cloud
	 * that matches to the current theme cloud the user created.
	 * @param sessionContainer
	 * @param minSyntag
	 * @param syntagmaticEntityTermFactor
	 * @param a
	 * @param b
	 */
	public void calcSyntagCloud(SessionContainer sessionContainer, Float minSyntag,
		Float syntagmaticEntityTermFactor, Float a, Float b) {

		TagHandlerWebserviceAPI webservices = new TagHandlerWebserviceAPI(sessionContainer.getRestBaseURI());
		StringBuilder themeCloudTermValues = new StringBuilder();
		int i = 0;
		// build term values parameter
		for (Term term : sessionContainer.getThemeCloudSessionBean().getWhiteThemeCloud()) {
			if (i != 0) {
				themeCloudTermValues.append(",");
			}
			themeCloudTermValues.append(term.getValue());
			i++;
		}
		sessionContainer.getThemeCloudSessionBean().getSyntagTerms().clear();
		// call the webservice
		SyntagCloudResult result = webservices.getSyntagTermCloud(themeCloudTermValues.toString(),
			minSyntag.toString(), syntagmaticEntityTermFactor.toString(), a.toString(), b.toString());
		if (result != null) {
			// add received terms to syntag cloud if term is not already in theme cloud
			for (Term syntagTerm : result.getSyntagCloudTerms()) {
				if (!sessionContainer.getThemeCloudSessionBean().getThemeCloud().contains(syntagTerm)) {
					sessionContainer.getThemeCloudSessionBean().getSyntagTerms().add(syntagTerm);
				}
			}
		}
	}

	/**
	 * Adds terms from the syntag cloud or the term input field to the theme cloud
	 * if term is in term lexicon.
	 * @param sessionContainer
	 * @param termsToAdd
	 * @param minSyntag
	 * @param syntagmaticEntityTermFactor
	 * @param a
	 * @param b
	 */
	public void addTermToTermCloud(SessionContainer sessionContainer, List<Term> termsToAdd,
		Float minSyntag, Float syntagmaticEntityTermFactor, Float a, Float b) {

		TagHandlerWebserviceAPI webservices = new TagHandlerWebserviceAPI(sessionContainer.getRestBaseURI());

		// verify terms
		for (Term term : termsToAdd) {
			if (!term.isValid()) {
				TermsResult result = webservices.getTerm(term.getValue());
				if (result != null && !result.getTerms().isEmpty()) {
					term.setId(result.getTerms().get(0).getId());
				}
			}
			// add only existing terms that are not already in theme cloud
			if (term.isValid() && !sessionContainer.getThemeCloudSessionBean().getThemeCloud().contains(term)) {
				sessionContainer.getThemeCloudSessionBean().getThemeCloud().add(term);
				sessionContainer.getThemeCloudSessionBean().rebuildColoredThemeCloud();
				logger.info("Term with value '"+term.getValue()+"' added to theme cloud.");
			} else {
				logger.info("No term found for input value '"+term.getValue()+"'.");
			}
		}

		// compute new syntag cloud
		if (!termsToAdd.isEmpty()) {
			this.calcSyntagCloud(sessionContainer, minSyntag, syntagmaticEntityTermFactor, a, b);
		}
	}

	/**
	 *
	 * @param request
	 * @param themeCloudName
	 * @param themeCloudTerms
	 * @param userID
	 */
	public void saveThemeCloud(String themeCloudName, List<Term> themeCloudTerms, String userName, String restBaseURI) {

		ThemeHandlerWebserviceAPI services = new ThemeHandlerWebserviceAPI(restBaseURI);

		User user = new User();
		user.setName(userName);

		ThemeCloud themeCloud = new ThemeCloud();
		themeCloud.setThemeCloudName(themeCloudName);
		themeCloud.setTerms(themeCloudTerms);
		themeCloud.setUser(user);

		services.saveNewThemeCloud(themeCloud);
	}

	/**
	 *
	 * @param request
	 * @param themeCloudID
	 * @return
	 */
	public boolean deleteThemeCloud(String themeCloudName, String restBaseURI) {

		ThemeHandlerWebserviceAPI webservices = new ThemeHandlerWebserviceAPI(restBaseURI);
		webservices.deleteThemeCloud(themeCloudName);
		// TODO: use response from webservice to check if delete succeeded
		return true;
	}

}
