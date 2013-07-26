package de.bbcdaas.taghandler.tests;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.TermMatrixEntry;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.taghandler.TagHandler;
import de.bbcdaas.taghandler.cleaner.EntityCleaner;
import de.bbcdaas.taghandler.dao.TagHandlerDao;
import de.bbcdaas.taghandler.dao.TermLexiconDao;
import de.bbcdaas.taghandler.exception.ProcessException;
import de.bbcdaas.taghandler.reader.XingEntityFileReader;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * Unit test for tagHandler.
 *
 * @author Christian Herta
 * @author Robert Illers
 */
public class AppTest {

	private Logger logger = Logger.getLogger(AppTest.class);
	private TagHandler tagHandler;
	private XingEntityFileReader entityReader;
	private EntityCleaner entityCleaner;
	private TagHandlerDao taghandlerDao;
	private TermLexiconDao termLexiconDao;

        /**
         *
         */
        @Before
        public void setUp() {

            ApplicationContext context = new ClassPathXmlApplicationContext("test-applicationContext.xml");
            tagHandler = (TagHandler)context.getBean("tagHandler");
            entityCleaner = (EntityCleaner)context.getBean("entityCleaner");
            taghandlerDao = (TagHandlerDao)context.getBean("taghandlerDao");
            termLexiconDao = (TermLexiconDao)context.getBean("termlexiconDao");
            entityReader = new XingEntityFileReader();

            try {
                    termLexiconDao.clearAllTables();
                    tagHandler.readEntities();
                    tagHandler.computeSyntagmaticRelations();
            } catch(ProcessException e) {
                    logger.error(e.getMessage());
            } catch(DaoException e) {
                    logger.error(e.getCompleteMessage());
            }
        }

        @After
        public void tearDown() {
            printLexiconTerms(termLexiconDao);
        }

        /**
         * Check if term matrix valid
         */
        @Test
        public void checkSyntagmaticRelations() {

            logger.info("Test: checkSyntagmaticRelations()");
            try {
                taghandlerDao.openConnection();
                List<Term> terms = taghandlerDao.getTerms(0, taghandlerDao.getNumberOfTerms(false), false);
                for (Term term : terms) {
                    List<TermMatrixEntry> termMatrixEntries = taghandlerDao.getRelatedTermMatrixEntries(term.getId(), false);
                    for (TermMatrixEntry termMatrixEntry : termMatrixEntries) {
                        // check id integrity
                        assertTrue(termMatrixEntry.getTermId1() != termMatrixEntry.getTermId2());
                        assertTrue(term.getId() == termMatrixEntry.getTermId1() ||
                            term.getId() == termMatrixEntry.getTermId2());
                    }
                }
                taghandlerDao.closeConnection(false);
            } catch(DaoException e) {
                    logger.error(e.getCompleteMessage());
            }
        }

	/**
	 *
	 */
        @Test
	public void compareCoocurrenceEntries() {

		logger.info("Test: compareCoocurrenceEntries()");
		entityReader.setInputFileName("testCooccurrences.txt");
		try {

			List<Entity> entities = entityReader.readEntities();
			entities = entityCleaner.clean(entities);

			for (Entity entity : entities) {

				logger.debug(entity.getFields().get(0).getTerms().get(0).getValue());
				entity.getFields().get(0).getTerms().get(0).setId(termLexiconDao.getTerm(entity.getFields().get(0).getTerms().get(0).getValue()).getId());
				logger.debug(entity.getFields().get(0).getTerms().get(0).getId());
				logger.debug(entity.getFields().get(0).getTerms().get(1).getValue());
				entity.getFields().get(0).getTerms().get(1).setId(termLexiconDao.getTerm(entity.getFields().get(0).getTerms().get(1).getValue()).getId());
				logger.debug(entity.getFields().get(0).getTerms().get(1).getId());
				int cooc = taghandlerDao.getCooccurrence(entity.getFields().get(0).getTerms().get(0).getId(), entity.getFields().get(0).getTerms().get(1).getId());
				assertTrue(cooc != -1);
				assertEquals(Integer.parseInt(entity.getFields().get(0).getTerms().get(2).getValue()), cooc);
			}
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
			assertTrue(false);
		} catch(ProcessException e) {
			logger.error(e);
			assertTrue(false);
		}
	}

	/**
	 *
	 */
        @Test
	public void compareFrequencyCounts() {

		logger.info("Test: compareFrequencyCounts()");
		entityReader.setInputFileName("testFrequencies.txt");
		try {

			List<Entity> entities = entityReader.readEntities();
			entities = entityCleaner.clean(entities);

			for (Entity entity : entities) {
				int currentLocalFrequency = termLexiconDao.getTerm(entity.getFields().get(0).getTerms().get(0).getValue()).getLocalFrequency();
				assertTrue(currentLocalFrequency != -1);
				int targetLocalFrequency = Integer.parseInt(entity.getFields().get(0).getTerms().get(1).getValue());
				assertEquals(currentLocalFrequency, targetLocalFrequency);
			}
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
			assertTrue(false);
		} catch(ProcessException e) {
			logger.error(e);
			assertTrue(false);
		}
	}

	/**
	 * Logs all Terms in the term lexicon sorted by its ID, beginning
         * with the lowest ID.
	 * @param taghandlerDao
	 */
	private boolean printLexiconTerms(TermLexiconDao termLexiconDao) {

		try {
			int minTermID = termLexiconDao.getMinTermID();
			int maxTermID = termLexiconDao.getMaxTermID();
			long nbTerms = termLexiconDao.getNumberOfTerms();
			logger.info("Lexicon nbTerms: " + nbTerms);

			termLexiconDao.openConnection();
			for (int i = minTermID; i <= maxTermID; i++) {
				Term term = termLexiconDao.getTerm(i, false);
				if (term != null) {
					logger.info("Term with id = '" + i + "' is " + term.getValue());
				} else {
					logger.info("Term with id = '" + i + "' does not exist");
				}
			}
			termLexiconDao.closeConnection(false);
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
			return false;
		}
		return true;
	}

	/**
	 * Logs all Terms in the local taghandler db sorted by its ID, beginning
	 * with the lowest ID.
	 * @param tagHandlerDao
	 * @return
	 */
	private boolean printLocalTerms(TagHandlerDao tagHandlerDao) {

		try {
						tagHandlerDao.openConnection();
			int minTermID = tagHandlerDao.getMinTermID(false);
			int maxTermID = tagHandlerDao.getMaxTermID(false);
			long nbTerms = tagHandlerDao.getNumberOfTerms(false);
			logger.info("Local nbTerms: " + nbTerms);

			for (int i = minTermID; i <= maxTermID; i++) {
				Term term = tagHandlerDao.getTerm(i, false);
				if (term != null) {
					logger.info("Term with id = '" + i + "' is " + term.getValue());
				} else {
					logger.info("Term with id = '" + i + "' does not exist");
				}
			}
			tagHandlerDao.closeConnection(false);
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
			return false;
		}
		return true;
	}
}