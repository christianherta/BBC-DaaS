package de.bbcdaas.visualizer.business;

import de.bbcdaas.common.beans.RankListEntry;
import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.common.beans.entity.TermCloudField;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.taghandler.compute.relation.syntagmatic.SyntagmaticRelationCompute;
import de.bbcdaas.taghandler.dao.TagHandlerDao;
import de.bbcdaas.taghandler.dao.TermLexiconDao;
import de.bbcdaas.taghandler.exception.ProcessException;
import de.bbcdaas.visualizer.beans.SessionContainer;
import de.bbcdaas.visualizer.constants.VisualizerConstants;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Robert Illers
 */
public final class SyntagCloudsBusiness {

	private final Logger logger = Logger.getLogger(EvaluationBusiness.class);
	private TagHandlerDao taghandlerDao;
	private TermLexiconDao taghandler_termLexiconDao;
	private SyntagmaticRelationCompute syntagmaticRelationCompute;

	/**
	 *
	 */
	public SyntagCloudsBusiness() {
		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		this.taghandlerDao = (TagHandlerDao)classPathXmlApplicationContext.getBean("taghandlerDao");
		this.taghandler_termLexiconDao = (TermLexiconDao)classPathXmlApplicationContext.getBean("taghandler_termlexiconDao");
		this.syntagmaticRelationCompute = (SyntagmaticRelationCompute)classPathXmlApplicationContext.getBean("syntagmaticRelationCompute");
	}

	/**
	 *
	 * @param request
	 */
	public void initSyntagCloudsForm(HttpServletRequest request) {

		SessionContainer sessionContainer = (SessionContainer)request.getSession().getAttribute(VisualizerConstants.KEY_SESSION_CONTAINER);

			if (sessionContainer == null) {

				sessionContainer = new SessionContainer();
				request.getSession().setAttribute(VisualizerConstants.KEY_SESSION_CONTAINER, sessionContainer);
			}
	}

	/**
	 *
	 * @return
	 */
	public Entity getRandomEntity() {

		Entity randomEntity = null;

		try {
			int numberOfEntities = taghandlerDao.getNumberOfEntities();
			int rndEntityID = (int)(Math.random() *numberOfEntities);
			randomEntity = taghandlerDao.getEntity(rndEntityID);
			randomEntity.setFields(taghandlerDao.getEntityFields(rndEntityID));
			taghandler_termLexiconDao.openConnection();
			for (TermCloudField field : randomEntity.getFields()) {
				field.setTerms(taghandlerDao.getFieldTerms(field.getID()));
				for (Term term : field.getTerms()) {
					Term termLexiconTerm = taghandler_termLexiconDao.getTerm(term.getId(), true);
					if (termLexiconTerm == null) {
						logger.error("term with id='"+term.getId()+"' is not in taghandler termLexicon.");
					} else {
						term.setValue(termLexiconTerm.getValue());
					}
				}
			}
			taghandler_termLexiconDao.closeConnection(false);
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
		}

		return randomEntity;
	}

	/**
	 *
	 * @param termCloudFields
	 * @param minSyntag
	 * @param syntagmaticEntityTermFactor
	 * @param a
	 * @param b
	 * @return
	 */
	public List<TermCloudField> computeSyntagTerms(List<TermCloudField> termCloudFields, float minSyntag, float syntagmaticEntityTermFactor, float a, float b) {

		for (TermCloudField field : termCloudFields) {

			try {

				List<RankListEntry> topSyntagmaticTerms = this.syntagmaticRelationCompute.computeFieldTopSyntagmaticTerms(field.getTerms(),
					minSyntag, syntagmaticEntityTermFactor, a, b);
				field.clearSyntagmaticTerms();
				field.getScores().clear();
				for (RankListEntry entry : topSyntagmaticTerms) {
					field.addSyntagmaticTerm(entry.getTerm());
					field.addScore(entry.getScore());
				}
			} catch(ProcessException e) {
				logger.error(e);
			}
		}
		return termCloudFields;
	}

	/**
	 *
	 * @param terms
	 * @return
	 */
	public List<Entity> searchForEntities(List<Term> terms) {

		List<Entity> entities = new ArrayList<Entity>();

		try {
			// dont search without restrictive terms (too many results)
			if (terms != null && !terms.isEmpty()) {
				entities = taghandlerDao.getEntitiesByFieldTerms(terms);
			}
		} catch(DaoException e) {
			logger.error(e.getMessage());
		}

		if (!entities.isEmpty()) {
			this.setEntityData(entities.get(0));
		}
		return entities;
	}

	/**
	 *
	 * @param entity
	 */
	public void setEntityData(Entity entity) {

		try {
			entity.setFields(taghandlerDao.getEntityFields(entity.getID()));
			for (TermCloudField field : entity.getFields()) {
				field.setTerms(taghandlerDao.getFieldTerms(field.getID()));
				taghandler_termLexiconDao.openConnection();
				for (Term term : field.getTerms()) {
					Term termLexiconTerm = taghandler_termLexiconDao.getTerm(term.getId(), true);
					if (termLexiconTerm == null) {
						logger.error("term with id='"+term.getId()+"' is not in taghandler termLexicon.");
					} else {
						term.setValue(termLexiconTerm.getValue());
					}
				}
				taghandler_termLexiconDao.closeConnection(false);
			}
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
		}
	}
}
