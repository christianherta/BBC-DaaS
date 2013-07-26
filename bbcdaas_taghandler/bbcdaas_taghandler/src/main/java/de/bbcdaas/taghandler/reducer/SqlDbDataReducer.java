package de.bbcdaas.taghandler.reducer;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.common.beans.entity.TermCloudField;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.taghandler.dao.TagHandlerDao;
import de.bbcdaas.taghandler.dao.TermLexiconDao;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Implementation of a Term data reducer for a mySQL Database.
 * @author Robert Illers
 */
public class SqlDbDataReducer implements TermDataReducer {

	private TagHandlerDao taghandlerDao;
	private TermLexiconDao termLexiconDao;
	private TagHandlerDao taghandlerDaoReduced;
	private TermLexiconDao termLexiconDaoReduced;
	private Logger logger = Logger.getLogger(this.getClass());
	private Integer minTermFrequency = 10;
	private Integer minTermCount = 2;

	/**
	 * injected by Spring. This is the DAO of the source database.
	 * @param taghandlerDao
	 */
	public void setTaghandlerDao(TagHandlerDao taghandlerDao) {
		this.taghandlerDao = taghandlerDao;
	}

	/**
	 * injected by Spring. This is the DAO of the term lexicon database.
	 * @param termLexiconDao
	 */
	public void setTermLexiconDao(TermLexiconDao termLexiconDao) {
		this.termLexiconDao = termLexiconDao;
	}

	/**
	 * injected by Spring. This is the DAO of the target taghandler database.
	 * @param taghandlerDaoReduced
	 */
	public void setTaghandlerDaoReduced(TagHandlerDao taghandlerDaoReduced) {
		this.taghandlerDaoReduced = taghandlerDaoReduced;
	}

	/**
	 * injected by Spring. This is the DAO of the target term lexicon database.
	 * @param termLexiconDaoReduced
	 */
	public void setTermLexiconDaoReduced(TermLexiconDao termLexiconDaoReduced) {
		this.termLexiconDaoReduced = termLexiconDaoReduced;
	}

	/**
	 * Gets the term frequency that is used to filter all terms in the term lexicon
	 * who have a lesser frequency than the setted one.
	 * @return
	 */
	@Override
	public Integer getMinTermFrequency() {
		return minTermFrequency;
	}

	/**
	 * injected by Spring. Gets the term frequency that is used to filter all
	 * terms in the term lexicon who have a lesser frequency than the setted one.
	 * @param minTermFrequency
	 */
	public void setMinTermFrequency(Integer minTermFrequency) {
		this.minTermFrequency = minTermFrequency;
	}

	/**
	 * Gets the number of terms a field should contain at last. If the field has
	 * a less number of terms the field will be filtered.
	 * @return minTermCount
	 */
	@Override
	public Integer getMinTermCount() {
		return minTermCount;
	}

	/**
	 * injected by Spring. Gets the number of terms a field should contain at
	 * last. If the field has a less number of terms the field will be filtered.
	 * @param minTermCount
	 */
	public void setMinTermCount(Integer minTermCount) {
		this.minTermCount = minTermCount;
	}

	/**
	 * Starts the reduce process.
	 */
	@Override
	public void reduceTermData() {

		try {
			int numberOfEntities = this.taghandlerDao.getNumberOfEntities();
			int offset = 0;
			int rowCount = 100;
			List<Entity> entities;
			List<Entity> reducedEntities;
			List<TermCloudField> reducedFields;
			List<Term> reducedFieldTerms;
			this.taghandlerDaoReduced.clearAllTables();
			this.termLexiconDaoReduced.clearAllTables();
			// read a bunch of entities and reduce them until all entities read
			do {
				entities = taghandlerDao.getEntities(offset, rowCount, true);
				reducedEntities = new ArrayList<Entity>();
				// reduce terms/fields/entities und set ids to 0 so they can get
				// a new id in reduced database
				for (Entity entity : entities) {
					entity.setID(0);
					reducedFields = new ArrayList<TermCloudField>();
					for (TermCloudField field : entity.getFields()) {
						field.setID(0);
						reducedFieldTerms = new ArrayList<Term>();
						for (Term term : field.getTerms()) {

							// all terms with a lesser frequency than minTermFrequency will be removed
							if (term.getLocalFrequency() >= this.minTermFrequency) {
								// get value from lexicon
								Term lexiconTerm = this.termLexiconDao.getTerm(term.getId());
								term.setValue(lexiconTerm.getValue());
								term.setId(0);
								reducedFieldTerms.add(term);
							}
						}
						// remove all fields that have less than minTermCount terms
						if (reducedFieldTerms.size() >= this.minTermCount) {
							field.setTerms(reducedFieldTerms);
							reducedFields.add(field);
						}
					}
					if (!reducedFields.isEmpty()) {
						entity.setFields(reducedFields);
						reducedEntities.add(entity);
					}
				}
				// write the reduced entities to the target database
				if (!reducedEntities.isEmpty()) {
					termLexiconDaoReduced.insertEntityTerms(reducedEntities);
					taghandlerDaoReduced.insertEntities(reducedEntities);
				}
				offset += rowCount;
				System.out.print(".");
			} while(offset < numberOfEntities);
		} catch(DaoException ex) {
			logger.error(ex.getCompleteMessage());
		}
	}
}
