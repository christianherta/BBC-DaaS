package de.bbcdaas.visualizer.business;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.TermToTermsGroup;
import de.bbcdaas.common.beans.TermToTermsGroups;
import de.bbcdaas.common.beans.User;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.taghandler.dao.TagHandlerDao;
import de.bbcdaas.taghandler.dao.TermLexiconDao;
import de.bbcdaas.visualizer.beans.EvaluationSessionBean;
import de.bbcdaas.visualizer.beans.SessionContainer;
import de.bbcdaas.visualizer.constants.VisualizerConstants;
import de.bbcdaas.visualizer.dao.EvaluationDao;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Robert Illers
 */
public final class EvaluationBusiness {

	private EvaluationDao evaluationDao;
	private TagHandlerDao taghandlerDao;
	private TermLexiconDao taghandler_termLexiconDao;
	private final Logger logger = Logger.getLogger(EvaluationBusiness.class);

	public EvaluationBusiness() {
		this.evaluationDao = (EvaluationDao)new ClassPathXmlApplicationContext("applicationContext.xml").getBean("evaluationDao");
		this.taghandlerDao = (TagHandlerDao)new ClassPathXmlApplicationContext("applicationContext.xml").getBean("taghandlerDao");
		this.taghandler_termLexiconDao = (TermLexiconDao)new ClassPathXmlApplicationContext("applicationContext.xml").getBean("taghandler_termlexiconDao");
	}

	/**
	 *
	 * @param request
	 */
	public void initEvaluationForm(HttpServletRequest request, User user) {

		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
		EvaluationSessionBean evaluationSessionBean = sessionContainer.getEvaluationSessionBean();

		/* enums for all users */
		List<TermToTermsGroups> randomTermsGroups = this.getAllRandomTermGroups(user);
		evaluationSessionBean.setRandomTermGroups(randomTermsGroups);
		if (!randomTermsGroups.isEmpty()) {
			evaluationSessionBean.setSelectedRandomTermsGroupID(randomTermsGroups.get(0).getGroupID());
		}
		/* /enums for all users */

		request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);

	}

	/**
	 * creates a new randomTermsGroup and refreshes the list of randomTermGroups in session
	 * @param request
	 * @param randomTermsGroupSize
	 * @param minNumberOfTopRelatedTerms
	 * @param maxRandomTries
	 * @param groupLabel
	 */
	public void addNewRandomTermsGroup(HttpServletRequest request, int randomTermsGroupSize,
		int minNumberOfTopRelatedTerms, int maxRandomTries, String groupLabel, float minTopTermSyntag) {

		try {
			User user = (User)request.getSession().getAttribute(VisualizerConstants.KEY_USER);
			List<Integer> randomTermIDs = new ArrayList<Integer>();
			int i = 0;
			taghandlerDao.openConnection();
			int numberOfTerms = taghandlerDao.getNumberOfTerms(false);
			while (i < randomTermsGroupSize) {
				int termID = 0;
				Term randomTerm = this.taghandlerDao.getRandomTerm(minNumberOfTopRelatedTerms,
					maxRandomTries, numberOfTerms, minTopTermSyntag, false);
				if (randomTerm == null) {
					logger.info("No randomTerm found with at least "+
						minNumberOfTopRelatedTerms+" topRelatedTerms with a minSyntag of "+
						minTopTermSyntag+". "+maxRandomTries+" tries executed. Skipping...");
					i++;
				} else {
					termID = randomTerm.getId();
				}
				if (termID != 0 && !randomTermIDs.contains(termID)) {
					randomTermIDs.add(termID);
					i++;
				}
			}
			taghandlerDao.closeConnection(false);
			this.evaluationDao.insertRandomTermsGroup(randomTermIDs, groupLabel, minTopTermSyntag);
			SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
			EvaluationSessionBean evaluationSessionBean = sessionContainer.getEvaluationSessionBean();
			evaluationSessionBean.setRandomTermGroups(this.getAllRandomTermGroups(user));
			request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
		}
	}

	/**
	 *
	 * @param evaluationSessionBean
	 * @return
	 */
	private TermToTermsGroups getActualRandomTermsGroup(EvaluationSessionBean evaluationSessionBean) {

		for (TermToTermsGroups randomTermsGroup : evaluationSessionBean.getRandomTermGroups()) {
			if (randomTermsGroup.getGroupID() == evaluationSessionBean.getSelectedRandomTermsGroupID()) {
				return randomTermsGroup;
			}
		}
		return null;
	}

	/**
	 *
	 * @param sessionContainer
	 * @return
	 */
	private TermToTermsGroup getActualRandomTermRatedTermsGroup(EvaluationSessionBean evaluationSessionBean) {

		for (TermToTermsGroups randomTermsGroup : evaluationSessionBean.getRandomTermGroups()) {
			if (randomTermsGroup.getGroupID() == evaluationSessionBean.getSelectedRandomTermsGroupID()) {
				for (TermToTermsGroup randomTermGroup : randomTermsGroup.getTermToTermsGroups()) {
					if (randomTermGroup.getTerm().getId() == evaluationSessionBean.getSelectedRandomTermID()) {
						return randomTermGroup;
					}
				}
			}
		}
		return null;
	}

	/**
	 *
	 * @param sessionContainer
	 * @param actualRandomTermsGroup
	 */
	private void updateActualRandomTermsGroup(EvaluationSessionBean evaluationSessionBean, TermToTermsGroup actualRandomTermsGroup) {

		for (TermToTermsGroups randomTermsGroup : evaluationSessionBean.getRandomTermGroups()) {
			if (randomTermsGroup.getGroupID() == evaluationSessionBean.getSelectedRandomTermsGroupID()) {
				for (TermToTermsGroup randomTermGroup : randomTermsGroup.getTermToTermsGroups()) {
					if (randomTermGroup.getTerm().getId() == evaluationSessionBean.getSelectedRandomTermID()) {
						randomTermGroup.setRelatedTerms(actualRandomTermsGroup.getRelatedTerms());
						randomTermGroup.setSaved(actualRandomTermsGroup.isSaved());
						break;
					}
				}
				break;
			}
		}
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	public boolean saveSample(HttpServletRequest request) {

		try {

			SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
			EvaluationSessionBean evaluationSessionBean = sessionContainer.getEvaluationSessionBean();
			TermToTermsGroup randomTermRatedTermsGroup = this.getActualRandomTermRatedTermsGroup(evaluationSessionBean);
			boolean ok = true;

			if (randomTermRatedTermsGroup.isSaved()) {
				logger.error("Already saved.");
				ok = false;
			}

			User user = (User)request.getSession().getAttribute(VisualizerConstants.KEY_USER);

			int numberOfAlreadyRatedTerms = 0;
			int numberOfRatedTerms = 0;
			for (Term relatedTerm : randomTermRatedTermsGroup.getRelatedTerms()) {
				if (!relatedTerm.isAdded()) {
					numberOfRatedTerms++;
					String rating_String = request.getParameter(new StringBuilder().append("rating_").append(relatedTerm.getId()).toString());
					if (rating_String != null) {
						numberOfAlreadyRatedTerms++;
						relatedTerm.setRating(Integer.parseInt(rating_String));
					}
				}
			}

			if (numberOfAlreadyRatedTerms != numberOfRatedTerms) {
				logger.error("Not all terms rated.");
				ok = false;
			}

			if (ok) {
				for (Term relatedTerm : randomTermRatedTermsGroup.getRelatedTerms()) {
					if (relatedTerm.isAdded()) {
						this.evaluationDao.insertAddedTermIntoSample(evaluationSessionBean.getSelectedRandomTermsGroupID(),
							user.getId(), evaluationSessionBean.getSelectedRandomTermID(), relatedTerm.getId());
					} else {
						this.evaluationDao.insertRatedTermIntoSample(evaluationSessionBean.getSelectedRandomTermsGroupID(),
							user.getId(), evaluationSessionBean.getSelectedRandomTermID(), relatedTerm.getId(), relatedTerm.getRating());
					}
				}

				// mark group as saved to prevent saving it twice
				randomTermRatedTermsGroup.setSaved(true);
				evaluationSessionBean.setCurrentSelectedRatingSaved(true);
			}
			this.updateActualRandomTermsGroup(evaluationSessionBean, randomTermRatedTermsGroup);
			request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);

		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
			return false;
		}
		return true;
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	public boolean addAddedTerm(HttpServletRequest request) {

		String termValue = request.getParameter("value");
		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
		EvaluationSessionBean evaluationSessionBean = sessionContainer.getEvaluationSessionBean();
		TermToTermsGroup randomTermRatedTermsGroup = this.getActualRandomTermRatedTermsGroup(evaluationSessionBean);
		boolean ok = false;

		if (termValue != null) {
			try {
				ok = true;
				termValue = termValue.trim();
				termValue = termValue.toLowerCase();
				termValue = URLDecoder.decode(termValue, "UTF-8");

				for (Term relatedTerm : randomTermRatedTermsGroup.getRelatedTerms()) {
					if (relatedTerm.getValue().equals(termValue)) {
						ok = false;
						logger.error("term already in related terms.");
						break;
					}
				}
			} catch (UnsupportedEncodingException ex) {
				logger.error(ex);
			}
		}

		for (Term relatedTerm : randomTermRatedTermsGroup.getRelatedTerms()) {
			if (!relatedTerm.isAdded()) {
				String rating_String = request.getParameter(new StringBuilder().append("rating_").append(relatedTerm.getId()).toString());
				if (rating_String != null) {
					logger.info("added: "+Integer.parseInt(rating_String));
					relatedTerm.setRating(Integer.parseInt(rating_String));
				}
			}
		}

		if (ok) {

			try {
				Term addedTerm = taghandler_termLexiconDao.getTerm(termValue);
				if (addedTerm != null) {
					addedTerm.setAdded(true);
					randomTermRatedTermsGroup.getRelatedTerms().add(addedTerm);
				} else {
					logger.error("Term is not in term db.");
				}
			} catch(DaoException e) {
				logger.error(e.getCompleteMessage());
				return false;
			}
		}
		this.updateActualRandomTermsGroup(evaluationSessionBean, randomTermRatedTermsGroup);
		request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
		return true;
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	public boolean removeAddedTerm(HttpServletRequest request) {

		String termToRemoveID_String = request.getParameter("termToRemoveID");
		if (termToRemoveID_String != null) {
			int termToRemoveID = Integer.parseInt(termToRemoveID_String);
			logger.debug(termToRemoveID);
			SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
			EvaluationSessionBean evaluationSessionBean = sessionContainer.getEvaluationSessionBean();
			TermToTermsGroup randomTermRatedTermsGroup = this.getActualRandomTermRatedTermsGroup(evaluationSessionBean);
			int i = 0;
			for (Term relatedTerm : randomTermRatedTermsGroup.getRelatedTerms()) {
				if (relatedTerm.getId() == termToRemoveID) {
					break;
				}
				i++;
			}
			if (i != randomTermRatedTermsGroup.getRelatedTerms().size()) {
				randomTermRatedTermsGroup.getRelatedTerms().remove(i);
			}
			this.updateActualRandomTermsGroup(evaluationSessionBean, randomTermRatedTermsGroup);
			request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
			return true;
		}
		return false;
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	public boolean removeRandomTermsGroup(HttpServletRequest request) {

		String groupID_String = request.getParameter("groupID");
		Integer groupID = null;
		if (groupID_String != null) {
			groupID = Integer.parseInt(groupID_String);
		}
		if (groupID != null) {

			try {
				this.evaluationDao.removeRandomTermsGroup(groupID);
			} catch(DaoException e) {
				logger.error(e.getCompleteMessage());
				return false;
			}

			SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
			EvaluationSessionBean evaluationSessionBean = sessionContainer.getEvaluationSessionBean();

			int i = 0;
			for (TermToTermsGroups termToTermsGroups : evaluationSessionBean.getRandomTermGroups()) {
				if (termToTermsGroups.getGroupID() == groupID) {
					break;
				}
				i++;
			}

			if (i != evaluationSessionBean.getRandomTermGroups().size()) {
				evaluationSessionBean.getRandomTermGroups().remove(i);
			}
			request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
			return true;
		}
		return false;
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	public boolean removeRandomTerm(HttpServletRequest request) {

		String termID_String = request.getParameter("id");
		Integer termID = null;
		if (termID_String != null) {
			termID = Integer.parseInt(termID_String);
		}
		String groupID_String = request.getParameter("groupID");
		Integer groupID = null;
		if (groupID_String != null) {
			groupID = Integer.parseInt(groupID_String);
		}

		try {
			this.evaluationDao.removeRandomTerm(groupID, termID);
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
			return false;
		}

		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);
		EvaluationSessionBean evaluationSessionBean = sessionContainer.getEvaluationSessionBean();
		evaluationSessionBean.setSelectedRandomTermID(termID);
		TermToTermsGroups randomTermsGroup = this.getActualRandomTermsGroup(evaluationSessionBean);

		// iterate through random terms
		int i = 0;
		for (TermToTermsGroup randomTermWithRatedTerms : randomTermsGroup.getTermToTermsGroups()) {

			if (randomTermWithRatedTerms.getTerm().getId() == termID) {
				break;
			}
			i++;
		}
		if (i != randomTermsGroup.getTermToTermsGroups().size()) {
			randomTermsGroup.getTermToTermsGroups().remove(i);
		}

		request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
		return true;
	}

	/**
	 *
	 * @return
	 */
	private List<TermToTermsGroups> getAllRandomTermGroups(User user) {

		List<TermToTermsGroups> randomTermGroups = new ArrayList<TermToTermsGroups>();

		try {
			evaluationDao.openConnection();
			taghandler_termLexiconDao.openConnection();
			taghandlerDao.openConnection();
			// get ids of random terms
			randomTermGroups = evaluationDao.getAllRandomTermGroups(false);

			for (TermToTermsGroups randomTermsGroup : randomTermGroups) {

				for (TermToTermsGroup randomTermWithRatedTerms : randomTermsGroup.getTermToTermsGroups()) {

					// get value of random term
					Term term = this.taghandler_termLexiconDao.getTerm(randomTermWithRatedTerms.getTerm().getId(), false);
					if (term == null) {
						logger.error("Could not find Term with id='"+randomTermWithRatedTerms.getTerm().getId()+
							"' from randomTermsGroup in taghandler termLexicon, skipping.");
						break;
					}
					randomTermWithRatedTerms.getTerm().setValue(term.getValue());
					// try to get sample for random term of current user
					TermToTermsGroup sample = null;
					if (user.getRole() != VisualizerConstants.ROLE_ADMIN) {
						sample = evaluationDao.getSample(randomTermsGroup.getGroupID(),
							randomTermWithRatedTerms.getTerm().getId(), user, false);
					}
					if (sample != null) {
						randomTermWithRatedTerms.setSaved(true);
						for (Term relatedTerm : sample.getRelatedTerms()) {
							term = this.taghandler_termLexiconDao.getTerm(relatedTerm.getId(), false);
							if (term == null) {
								logger.error("Could not find Term with id='"+relatedTerm.getId()+
									"' from randomTermsGroup in taghandler termLexicon, skipping.");
							} else {
								relatedTerm.setValue(term.getValue());
							}
							randomTermWithRatedTerms.getRelatedTerms().add(relatedTerm);
						}
					} else {
						// no sample found, take topRelatedTerms
						randomTermWithRatedTerms.setRelatedTerms(taghandlerDao.
							getTopRelatedTerms(randomTermWithRatedTerms.getTerm().getId(), randomTermsGroup.getMinSyntag(), false));
						for (Term relatedTerm : randomTermWithRatedTerms.getRelatedTerms()) {
							term = this.taghandler_termLexiconDao.getTerm(relatedTerm.getId(), false);
							if (term == null) {
								logger.error("Could not find Term with id='"+relatedTerm.getId()+
									"' from randomTermsGroup in taghandler termLexicon, skipping.");
							} else {
								relatedTerm.setValue(term.getValue());
							}
						}
					}
				}
			}
			taghandlerDao.closeConnection(false);
			evaluationDao.closeConnection(false);
			taghandler_termLexiconDao.closeConnection(false);
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
		}
		return randomTermGroups;
	}
}
