package de.bbcdaas.taghandler.dao.impl.sql;

import de.bbcdaas.common.dao.base.SqlBaseDao;
import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.common.beans.entity.TermCloudField;
import de.bbcdaas.taghandler.dao.TermLexiconDao;
import de.bbcdaas.common.dao.constants.sql.SQL_Keywords;
import de.bbcdaas.taghandler.dao.constants.sql.TermLexicon_Queries;
import de.bbcdaas.taghandler.dao.constants.sql.TermLexicon_Scheme;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.common.dao.exceptions.DaoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Robert Illers
 */
public class TermLexiconDaoImpl extends SqlBaseDao implements TermLexiconDao {

	/**
	 * 
	 * @throws DaoException 
	 */
	@Override
	public void clearAllTables() throws DaoException {
		try {
			this.getJdbc().openConnection();
			this.getJdbc().executeUpdate(TermLexicon_Queries.CLEAR_TERMS);
			this.getJdbc().executeUpdate(TermLexicon_Queries.CLEAR_TERM_BLACKLIST);
			this.getJdbc().commitAndCloseConnection();
		} catch (ApiException e) {
			throw new DaoException("clearAllTables", e.getCompleteMessage());
		}
	}
	
	/**
	 * Gets a term by its value out of the term lexicon.
	 * @param termValue
	 * @return Term 
	 */
	@Override
	public Term getTerm(String termValue, boolean openNewConnection) throws DaoException {

		Term term = null;
		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			result = getJdbc().executeQuery(TermLexicon_Queries.GET_TERM_BY_VALUE, termValue);
			for (Map<String, Object> row : result) {
				term = new Term(((Long) row.get(TermLexicon_Scheme.TERM_COLUMN_TERM_ID)).intValue());
				term.setValue(termValue);
				term.setTotalFrequency(((Long) row.get(TermLexicon_Scheme.TERM_COLUMN_TERM_FREQUENCY)).intValue());
			}
			if (openNewConnection) {
				this.getJdbc().closeConnection();
			}
		} catch (ApiException e) {
			throw new DaoException("getTerm", e.getCompleteMessage());
		}
		return term;
	}
	
	/**
	 * 
	 * @param termValue
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public Term getTerm(String termValue) throws DaoException {
		return this.getTerm(termValue, true);
	}
	
	/**
	 * 
	 * @param termValueFragment
	 * @param maxResults
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<String> getTermValues(String termValueFragment, int maxResults) throws DaoException {
		
		String query = new StringBuilder().append(TermLexicon_Queries.GET_TERM_BY_VALUE_FRAGMENT).append(SQL_Keywords.LIMIT).append(maxResults).toString();
		List<String> termValues = new ArrayList<String>();
		try {
			this.getJdbc().openConnection();
			result = getJdbc().executeQuery(query, termValueFragment + "%");
			getJdbc().closeConnection();
			for (Map<String, Object> row : result) {
				termValues.add((String) row.get(TermLexicon_Scheme.TERM_COLUMN_VALUE));
			}
		} catch (ApiException e) {
			throw new DaoException("getTermValues", e.getCompleteMessage());
		}
		return termValues;
	}
	
	/**
	 * 
	 * @param termValueFragment
	 * @param maxResults
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<Term> getTerms(String termValueFragment, int maxResults, boolean openNewConnection) throws DaoException {
		
		String query = new StringBuilder().append(TermLexicon_Queries.GET_TERM_BY_VALUE_FRAGMENT).append(SQL_Keywords.LIMIT).append(maxResults).toString();
		List<Term> terms = new ArrayList<Term>();
		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			result = getJdbc().executeQuery(query, termValueFragment + "%");
			if (openNewConnection) {
				getJdbc().closeConnection();
			}
			for (Map<String, Object> row : result) {
				Term term = new Term((String) row.get(TermLexicon_Scheme.TERM_COLUMN_VALUE),
					((Long) row.get(TermLexicon_Scheme.TERM_COLUMN_TERM_ID)).intValue());
				terms.add(term);
			}
		} catch (ApiException e) {
			throw new DaoException("getTerms", e.getCompleteMessage());
		}
		return terms;
	}
	
	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @return 
	 */
	@Override
	public List<Term> getTermsSortedByTotalFrequency(int offset, int rowCount) throws DaoException {

		return getSortedTerms(offset, rowCount, TermLexicon_Scheme.TERM_COLUMN_TERM_FREQUENCY);
	}
	
	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @return 
	 */
	@Override
	public List<Term> getTermsSortedByValue(int offset, int rowCount) throws DaoException {

		return getSortedTerms(offset, rowCount, TermLexicon_Scheme.TERM_COLUMN_VALUE);
	}
	
	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @param sortByColumn
	 * @return 
	 */
	private List<Term> getSortedTerms(int offset, int rowCount, String sortByColumn) throws DaoException {

		String query = new StringBuilder().append(TermLexicon_Queries.GET_TERMS).
				append(SQL_Keywords.ORDER_BY).append(sortByColumn).append(SQL_Keywords.DESC).
				append(SQL_Keywords.LIMIT).append(offset).append(",").append(rowCount).toString();
		List<Term> terms = new ArrayList<Term>();
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(query);
			for (Map<String, Object> row : result) {
				Term term = new Term();
				term.setId(((Long) row.get(TermLexicon_Scheme.TERM_COLUMN_TERM_ID)).intValue());
				term.setValue((String) row.get(TermLexicon_Scheme.TERM_COLUMN_VALUE));
				term.setTotalFrequency(((Long) row.get(TermLexicon_Scheme.TERM_COLUMN_TERM_FREQUENCY)).intValue());
				terms.add(term);
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getSortedTerms", e.getCompleteMessage());
		}
		return terms;
	}
	
	/**
	 * 
	 * @param termID
	 * @return 
	 */
	@Override
	public Term getTerm(int termID, boolean openNewConnection) throws DaoException {

		Term term = null;
		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			result = getJdbc().executeQuery(TermLexicon_Queries.GET_TERM_BY_ID, termID);
			for (Map<String, Object> row : result) {
				term = new Term(termID);
				term.setValue((String) row.get(TermLexicon_Scheme.TERM_COLUMN_VALUE));
				term.setLocalFrequency(((Long) row.get(TermLexicon_Scheme.TERM_COLUMN_TERM_FREQUENCY)).intValue());
			}
			if (openNewConnection) {
				this.getJdbc().closeConnection();
			}
		} catch (ApiException e) {
			throw new DaoException("getTerm", e.getCompleteMessage());
		}
		return term;
	}
	
	/**
	 * 
	 * @param termID
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public Term getTerm(int termID) throws DaoException {
		return this.getTerm(termID, true);
	}
	
	/**
	 * 
	 * @param term 
	 */
	private void increaseTotalTermFrequency(int termID, boolean openNewConnection) throws DaoException {

		try {
            if (openNewConnection) {
                this.getJdbc().openConnection();
            }
			this.getJdbc().executeUpdate(TermLexicon_Queries.INCREASE_TERM_FREQUENCY, termID);
			if (openNewConnection) {
                this.getJdbc().commitAndCloseConnection();
            }
		} catch (ApiException e) {
			throw new DaoException("increaseTermFrequency", e.getCompleteMessage());
		}
	}

	/**
	 * 
	 * @param term 
	 */
	private Term insertNewTerm(String termValue, boolean openNewConnection) throws DaoException {

		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			this.getJdbc().executeUpdate(TermLexicon_Queries.INSERT_NEW_TERM, termValue);
			if (openNewConnection) {
				this.getJdbc().commitAndCloseConnection();
			} else {
				this.getJdbc().commit();
			}
		} catch (ApiException e) {
			throw new DaoException("insertNewTerm", e.getCompleteMessage());
		}
		Term term = getTerm(termValue, false);
		if (term == null) {
			String msg = "Error while generating ID for new term '" + termValue + "'";
			throw new DaoException("insertNewTerm", msg);
		}
		return term;
	}

	/**
	 * 
	 * @param termValue
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public Term insertTerm(String termValue, boolean openNewConnection) throws DaoException {

		Term term = getTerm(termValue, openNewConnection);
		if (term != null) {
			increaseTotalTermFrequency(term.getId(), openNewConnection);
			term.setTotalFrequency(term.getTotalFrequency() + 1);
		} else {
			term = insertNewTerm(termValue, openNewConnection);
		}
		return term;
	}
	
	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<Term> getTermBlacklist(int offset, int rowCount) throws DaoException {
		
		List<Term> termBlackList = new ArrayList<Term>();
		try {
			this.getJdbc().openConnection();
			result = getJdbc().executeQuery(TermLexicon_Queries.GET_TERM_BLACKLIST, offset, rowCount);
			for (Map<String, Object> row : result) {
				int termID = ((Long) row.get(TermLexicon_Scheme.TERM_BLACKLIST_COLUMN_TERM_ID)).intValue();
				Term term = this.getTerm(termID, false);
				termBlackList.add(term);
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getTermBlacklist", e.getCompleteMessage());
		}
		return termBlackList;
	}
	
	/**
	 * 
	 * @param termID
	 * @throws DaoException 
	 */
	@Override
	public void addTermToBlacklist(int termID) throws DaoException {
		
		try {
			this.getJdbc().openConnection();
			getJdbc().executeUpdate(TermLexicon_Queries.ADD_TERM_INTO_BLACKLIST, termID);
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("addTermToBlacklist", e.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 * @param entities
	 * @throws DaoException 
	 */
	@Override
	public void insertEntityTerms(List<Entity> entities) throws DaoException {
		
		try {
			this.getJdbc().openConnection();
			for (Entity entity : entities) {
				for (TermCloudField field : entity.getFields()) {
					for (Term term : field.getTerms()) {
						Term insertedTerm = this.insertTerm(term.getValue(), false);
						term.setId(insertedTerm.getId());
						term.setTotalFrequency(insertedTerm.getTotalFrequency());
					}
				}
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("insertEntityTerms", e.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 * @return 
	 */
	@Override
	public int getMaxTermID() throws DaoException {

		int ret = 0;
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TermLexicon_Queries.GET_MAX_TERM_ID);
			for (Map<String, Object> row : result) {
				ret = ((Long) row.get("maxTermID")).intValue();
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getMaxTermID", e.getCompleteMessage());
		}
		return ret;
	}

	/**
	 * 
	 * @return 
	 */
	@Override
	public int getMinTermID() throws DaoException {

		int ret = 0;
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TermLexicon_Queries.GET_MIN_TERM_ID);
			for (Map<String, Object> row : result) {
				ret = ((Long) row.get("minTermID")).intValue();
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getMinTermID", e.getCompleteMessage());
		}
		return ret;
	}
	
	/**
	 * 
	 * @return 
	 */
	@Override
	public int getNumberOfTerms() throws DaoException {

		int number = 0;
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TermLexicon_Queries.GET_NUMBER_OF_TERMS);
			for (Map<String, Object> row : result) {
				number = ((Long) row.get("numberOfTerms")).intValue();
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getNumberOfTerms", e.getCompleteMessage());
		}
		return number;
	}
	
	/**
	 * 
	 */ 
	@Override
	public void commit() {
		
		try {
			this.getJdbc().commit();
		} catch(ApiException e) {
			logger.error(e.getCompleteMessage());
		}
	}
}
