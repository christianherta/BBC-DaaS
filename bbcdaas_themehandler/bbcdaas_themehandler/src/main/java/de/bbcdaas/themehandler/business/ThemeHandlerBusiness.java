package de.bbcdaas.themehandler.business;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.ThemeCloud;
import de.bbcdaas.common.beans.User;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.themehandler.dao.impl.jpa.ThemeHandlerDaoImpl;
import de.bbcdaas.themehandler.domains.ThemeCloudTerm;
import de.bbcdaas.themehandler.domains.UserData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * ThemeHandler Business logic.
 * @author Robert Illers
 */
public final class ThemeHandlerBusiness {

	private ThemeHandlerDaoImpl dao;
	private Logger logger = Logger.getLogger(this.getClass());

	public ThemeHandlerBusiness(ThemeHandlerDaoImpl dao) {
		this.dao = dao;
	}

	/**
	 * Gets all theme clouds out of the database.
	 * @return List<ThemeCloud>
	 */
	public List<ThemeCloud> getThemeClouds() {

		List<ThemeCloud> themeClouds = new ArrayList<ThemeCloud>();

		try {

			dao.openConnection();
			List<de.bbcdaas.themehandler.domains.ThemeCloud> themeCloudDomains = dao.getThemeClouds();
			for (de.bbcdaas.themehandler.domains.ThemeCloud themeCloudDomain : themeCloudDomains) {

				ThemeCloud themeCloud = new ThemeCloud();
				themeCloud.setThemeCloudName(themeCloudDomain.getName());

				for (ThemeCloudTerm themeCloudTermDomain : themeCloudDomain.getTerms()) {

					de.bbcdaas.themehandler.domains.Term termDomain = dao.getTerm(themeCloudTermDomain.getId().getThemeCloudTermId());
					Term term = new Term(termDomain.getTermValue());
					term.setRating(themeCloudTermDomain.getType());
					term.setWeighting(themeCloudTermDomain.getWeighting());
					term.setId(themeCloudTermDomain.getId().getThemeCloudTermId());
					themeCloud.getTerms().add(term);
				}

				de.bbcdaas.themehandler.domains.UserData userDomain = dao.getUserByID(themeCloudDomain.getUserId());
				User user = new User();
				user.setName(userDomain.getUsername());
				themeCloud.setUser(user);

				themeClouds.add(themeCloud);
			}
			dao.closeConnection(true);
		} catch(DaoException e) {
			logger.error("getThemeClouds()", e);
		}
		return themeClouds;
	}

	/**
	 *
	 * @param themeCloudName
	 */
	public void deleteThemeCloudByName(String themeCloudName) {

		try {
			dao.openConnection();
			de.bbcdaas.themehandler.domains.ThemeCloud themeCloudDomain = dao.getThemeCloudByName(themeCloudName);
			if (themeCloudDomain != null) {
				dao.deleteThemeCloud(themeCloudDomain.getId());
			} else {
				logger.error("No ThemeCloud with name='"+themeCloudName+"' found. Deleting skipped.");
			}
			dao.closeConnection(true);
		} catch(DaoException e) {
			logger.error("deleteThemeCloudByName()", e);
		}
	}

	/**
	 * Inserta a new ThemeCloud into the database.
	 * @param themeCloud
	 */
	public void saveNewThemeCloud(ThemeCloud themeCloud) {

		try {

			dao.openConnection();
			if (dao.getThemeCloudByName(themeCloud.getThemeCloudName()) != null) {
				logger.error("ThemeCloud with name '"+themeCloud.getThemeCloudName()+"' already stored in database.");
			} else {
				// create a new themeCloud domain for local database
				de.bbcdaas.themehandler.domains.ThemeCloud themeCloudDomain =
					new de.bbcdaas.themehandler.domains.ThemeCloud();
				themeCloudDomain.setName(themeCloud.getThemeCloudName());

				// check if user is already in local database
				UserData userDomain = dao.getUserByName(themeCloud.getUser().getName());
				if (userDomain == null) {
					// if not -> create new one
					userDomain = new UserData();
					userDomain.setUsername(themeCloud.getUser().getName());
					dao.insertUser(userDomain);
				}

				// set local userID into themeCloudDomain
				themeCloudDomain.setUserId(userDomain.getId());

				Set<ThemeCloudTerm> themeCloudTermDomains = new HashSet<ThemeCloudTerm>();

				// create a term domain and domain meta informations for each input term
				for (Term term : themeCloud.getTerms()) {

					// check if term is already in local database
					de.bbcdaas.themehandler.domains.Term termDomain = dao.getTermByValue(term.getValue());
					if (termDomain == null) {
						// if not -> create new one
						termDomain = new de.bbcdaas.themehandler.domains.Term();
						termDomain.setTermValue(term.getValue());
						dao.insertTerm(termDomain);
					}

					// create a domain for term meta informations related to this themecloud
					ThemeCloudTerm themeCloudTermDomain = new ThemeCloudTerm(themeCloudDomain,
						termDomain.getId(), term.getRating(), term.getWeighting());

					// add the domain to the list of term meta information domains
					themeCloudTermDomains.add(themeCloudTermDomain);
				}

				// add the list of term meta informations to the themeCloudDomain
				themeCloudDomain.setTerms(themeCloudTermDomains);

				// save the themeCloud
				dao.insertThemeCloud(themeCloudDomain);
			}
			dao.closeConnection(true);

		} catch(DaoException e) {
			logger.error("saveNewThemeCloud", e);
		}
	}

}
