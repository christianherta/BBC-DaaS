package de.bbcdaas.visualizer.business;

import de.bbcdaas.common.beans.RankListEntry;
import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.ThemeCloud;
import de.bbcdaas.common.beans.User;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.taghandler.compute.relation.syntagmatic.SyntagmaticRelationCompute;
import de.bbcdaas.taghandler.constants.ProcessingConstants;
import de.bbcdaas.taghandler.dao.TagHandlerDao;
import de.bbcdaas.taghandler.dao.TermLexiconDao;
import de.bbcdaas.taghandler.exception.ProcessException;
import de.bbcdaas.visualizer.beans.SessionContainer;
import de.bbcdaas.visualizer.constants.VisualizerConstants;
import de.bbcdaas.visualizer.dao.ThemeHandlerDao;
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
public final class ThemeCloudBusiness {

	private final Logger logger = Logger.getLogger(ThemeCloudBusiness.class);
	private SyntagmaticRelationCompute syntagmaticRelationCompute;
	private TagHandlerDao taghandlerDao;
	private VisualizerDao visualizerDao;
	private ThemeHandlerDao themehandlerDao;
	private TermLexiconDao taghandler_termLexiconDao;

	/**
	 * Constructor
	 */
	public ThemeCloudBusiness() {

		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		taghandlerDao = (TagHandlerDao)classPathXmlApplicationContext.getBean("taghandlerDao");
		visualizerDao = (VisualizerDao)classPathXmlApplicationContext.getBean("visualizerDao");
		themehandlerDao = (ThemeHandlerDao)classPathXmlApplicationContext.getBean("themehandlerDao");
		taghandler_termLexiconDao = (TermLexiconDao)classPathXmlApplicationContext.getBean("taghandler_termlexiconDao");
		this.syntagmaticRelationCompute = (SyntagmaticRelationCompute)classPathXmlApplicationContext.getBean("syntagmaticRelationCompute");
	}

	/**
	 *
	 * @param sessionContainer
	 * @param termValue
	 */
	public void addTermToTermCloud(SessionContainer sessionContainer, List<Term> termsToAdd,
		Float minSyntag, Float syntagmaticEntityTermFactor, Float a, Float b) {

		// verify terms
		try {
			for (Term termToAdd : termsToAdd) {
				if (!termToAdd.isValid()) {
					Term newTerm = taghandler_termLexiconDao.getTerm(termToAdd.getValue());
					if (newTerm != null) {
						termToAdd.setId(newTerm.getId());
					}
				}
				// add only existing terms that are not already in theme cloud
				if (termToAdd.isValid() && !sessionContainer.getThemeCloudSessionBean().getThemeCloud().contains(termToAdd)) {
					sessionContainer.getThemeCloudSessionBean().getThemeCloud().add(termToAdd);
					sessionContainer.getThemeCloudSessionBean().rebuildColoredThemeCloud();
					logger.info("Term with value '"+termToAdd.getValue()+"' added to theme cloud.");
				} else {
					logger.info("No term found for input value '"+termToAdd.getValue()+"'.");
				}
			}
		} catch(DaoException ex) {
			logger.error("error in addTermToTermCloud():",ex);
		}

		// compute new syntag cloud
		if (!termsToAdd.isEmpty()) {
			this.calcSyntagCloud(sessionContainer, minSyntag, syntagmaticEntityTermFactor, a, b);
		}
	}

	/**
	 *
	 * @param sessionContainer
	 * @param minSyntag
	 * @param syntagmaticEntityTermFactor
	 * @param a
	 * @param b
	 */
	public void calcSyntagCloud(SessionContainer sessionContainer, float minSyntag,
		float syntagmaticEntityTermFactor, float a, float b) {

		try {
			List<RankListEntry> topSyntagmaticTerms = this.syntagmaticRelationCompute.
				computeFieldTopSyntagmaticTerms(sessionContainer.getThemeCloudSessionBean().getWhiteThemeCloud(),
				minSyntag, syntagmaticEntityTermFactor, a, b);

			sessionContainer.getThemeCloudSessionBean().getSyntagTerms().clear();
			for (RankListEntry entry : topSyntagmaticTerms) {
				// filter theme cloud terms from syntag Cloud
				if (!sessionContainer.getThemeCloudSessionBean().getThemeCloud().contains(entry.getTerm())) {
					sessionContainer.getThemeCloudSessionBean().getSyntagTerms().add(entry.getTerm());
				}
			}
		} catch(ProcessException e) {
			logger.error("Error in calcSyntagCloud(): ",e);
		}
	}

	/**
	 *
	 */
	public void saveThemeCloud(HttpServletRequest request, String themeCloudName, List<Term> themeCloudTerms, int userID) {

		try {
			int themeCloudID = themehandlerDao.getThemeCloudID(themeCloudName);
			if (themeCloudID == 0) {
				themehandlerDao.insertThemeCloud(themeCloudName, themeCloudTerms, userID);
			} else {
				themehandlerDao.updateThemeCloud(themeCloudID, themeCloudName, themeCloudTerms, userID);
			}
			this.initThemeCloudForm(request, true);
		} catch(DaoException ex) {
			logger.error("Error in saveThemeCloud(): ",ex);
		}
	}

	/**
	 *
	 * @param themeCloud
	 * @return
	 */
	public boolean deleteThemeCloud(HttpServletRequest request, int themeCloudID) {

		try {
			themehandlerDao.deleteThemeCloud(themeCloudID);
			this.initThemeCloudForm(request, true);
		} catch(DaoException ex) {
			logger.error("Error in deleteThemeCloud: ",ex);
		}
		return true;
	}

	/**
	 *
	 * @return
	 */
	public List<ThemeCloud> getThemeClouds(User currentUser) {

		int rowCount = 500;
		int offset = 0;
		List<ThemeCloud> themeClouds = new ArrayList<ThemeCloud>();
		List<ThemeCloud> themeCloudsPart;
		try {
			do {
				themeCloudsPart = themehandlerDao.getThemeClouds(offset, rowCount);
				offset += rowCount;
				themeClouds.addAll(themeCloudsPart);
			} while(themeCloudsPart.size() == rowCount);
			List<User> users = this.visualizerDao.getAllUser();
			for (ThemeCloud themeCloud : themeClouds) {
				for (Term term : themeCloud.getTerms()) {
					Term termLexiconTerm  = taghandler_termLexiconDao.getTerm(term.getId(), true);
					if (termLexiconTerm != null) {
						term.setValue(termLexiconTerm.getValue());
					} else {
						logger.error("term with id ='"+term.getId()+"' not in taghandler termLexicon.");
					}
				}
				for (User user : users) {
					if (themeCloud.getUser().getId() == user.getId()) {
						themeCloud.setUser(user);
						break;
					}
				}
			}
		} catch(DaoException ex) {
			logger.error("Error in getThemeClouds: ",ex);
		}
		return themeClouds;
	}

	/**
	 *
	 */
	public void initThemeCloudForm(HttpServletRequest request, boolean clear) {

		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
		User currentUser = (User)request.getSession().getAttribute(VisualizerConstants.KEY_USER);

		if (sessionContainer == null) {
			sessionContainer = new SessionContainer();
		}

		if (clear) {

			sessionContainer.getThemeCloudSessionBean().setA(ProcessingConstants.DEFAULT_A);
			sessionContainer.getThemeCloudSessionBean().setB(ProcessingConstants.DEFAULT_B);
			sessionContainer.getThemeCloudSessionBean().setMinSyntag(ProcessingConstants.DEFAULT_MIN_TOP_TERM_SYNTAG);
			sessionContainer.getThemeCloudSessionBean().setSyntagmaticEntityTermFactor(ProcessingConstants.DEFAULT_SYNTAGMATIC_ENTITY_TERM_FACTOR);
			sessionContainer.getThemeCloudSessionBean().getSyntagTerms().clear();
			sessionContainer.getThemeCloudSessionBean().getThemeCloud().clear();
			sessionContainer.getThemeCloudSessionBean().getColoredThemeCloud().clear();
			sessionContainer.getThemeCloudSessionBean().setThemeCloudName("");
			sessionContainer.getThemeCloudSessionBean().getThemeClouds().clear();
		}

		if (sessionContainer.getThemeCloudSessionBean().getThemeClouds().isEmpty()) {
			sessionContainer.getThemeCloudSessionBean().setThemeClouds(this.getThemeClouds(currentUser));
		}

		request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
	}
}
