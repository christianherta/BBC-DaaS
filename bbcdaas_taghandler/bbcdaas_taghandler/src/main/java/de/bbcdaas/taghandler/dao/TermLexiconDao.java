package de.bbcdaas.taghandler.dao;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.common.dao.base.BaseDao;
import de.bbcdaas.common.dao.exceptions.DaoException;
import java.util.List;

/**
 * Dao Methods for accessing the tagHandler termLexicon.
 * @author Robert Illers
 */
public interface TermLexiconDao extends BaseDao {
	
	/**
	 * 
	 * @param termValue
	 * @return
	 * @throws DaoException 
	 */
	public Term getTerm(String termValue, boolean openNewConnection) throws DaoException;
	
	/**
	 * 
	 * @param termValue
	 * @return
	 * @throws DaoException 
	 */
	public Term getTerm(String termValue) throws DaoException;
	
	/**
	 * 
	 * @param termID
	 * @param openNewConnection
	 * @return
	 * @throws DaoException 
	 */
	public Term getTerm(int termID, boolean openNewConnection) throws DaoException;
	
	/**
	 * 
	 * @param termID
	 * @return
	 * @throws DaoException 
	 */
	public Term getTerm(int termID) throws DaoException;
	
	/**
	 * 
	 * @param termValueFragment
	 * @param maxResults
	 * @return
	 * @throws DaoException 
	 */
	public List<String> getTermValues(String termValueFragment, int maxResults) throws DaoException;
	
	/**
	 * 
	 * @param termValueFragment
	 * @param maxResults
	 * @return
	 * @throws DaoException 
	 */
	public List<Term> getTerms(String termValueFragment, int maxResults, boolean openNewConnection) throws DaoException;
	
	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @return
	 * @throws DaoException 
	 */
	public List<Term> getTermsSortedByTotalFrequency(int offset, int rowCount) throws DaoException;
	
	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @return
	 * @throws DaoException 
	 */
	public List<Term> getTermsSortedByValue(int offset, int rowCount) throws DaoException;
	
	/**
	 * 
	 * @param termValue
	 * @return
	 * @throws DaoException 
	 */
	public Term insertTerm(String termValue, boolean openNewConnection) throws DaoException;
	
	/**
	 * 
	 * @param entities
	 * @throws DaoException 
	 */
	public void insertEntityTerms(List<Entity> entities) throws DaoException;
	
	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @return
	 * @throws DaoException 
	 */
	public List<Term> getTermBlacklist(int offset, int rowCount) throws DaoException;
	
	/**
	 * 
	 * @param termID
	 * @throws DaoException 
	 */
	public void addTermToBlacklist(int termID) throws DaoException;
	
	/**
	 * 
	 * @return
	 * @throws DaoException 
	 */
	public int getMaxTermID() throws DaoException;
	
	/**
	 * 
	 * @return
	 * @throws DaoException 
	 */
	public int getMinTermID() throws DaoException;
	
	/**
	 * 
	 * @return
	 * @throws DaoException 
	 */
	public int getNumberOfTerms() throws DaoException;
}
