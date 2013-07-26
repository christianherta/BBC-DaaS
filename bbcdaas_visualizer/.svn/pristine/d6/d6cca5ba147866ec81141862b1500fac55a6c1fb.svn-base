package de.bbcdaas.visualizer.dao;

import de.bbcdaas.common.beans.TermToTermsGroup;
import de.bbcdaas.common.beans.TermToTermsGroups;
import de.bbcdaas.common.beans.User;
import de.bbcdaas.common.dao.base.BaseDao;
import de.bbcdaas.common.dao.exceptions.DaoException;
import java.util.List;

/**
 *
 * @author Robert Illers
 */
public interface EvaluationDao extends BaseDao {
	
	/**
	 * 
	 * @param randomTermIDs
	 * @param groupLabel
	 * @return
	 * @throws DaoException 
	 */
	public int insertRandomTermsGroup(List<Integer> randomTermIDs, String groupLabel, float minSyntag) throws DaoException;
	
	/**
	 * 
	 * @param rtgID
	 * @return
	 * @throws DaoException 
	 */
	public TermToTermsGroups getRandomTermsGroupByID(int rtgID) throws DaoException;
	
	/**
	 * 
	 * @return
	 * @throws DaoException 
	 */
	public List<TermToTermsGroups> getAllRandomTermGroups(boolean openNewConnection) throws DaoException;
	
	/**
	 * 
	 * @param rtgID
	 * @param userID
	 * @param randomTermID
	 * @param ratedTermID
	 * @param rating
	 * @throws DaoException 
	 */
	public void insertRatedTermIntoSample(int rtgID, int userID, int randomTermID, int ratedTermID, int rating) throws DaoException;
	
	/**
	 * 
	 * @param rtgID
	 * @param userID
	 * @param randomTermID
	 * @param addedTermID
	 * @throws DaoException 
	 */
	public void insertAddedTermIntoSample(int rtgID, int userID, int randomTermID, int addedTermID) throws DaoException;
	
	/**
	 * 
	 * @param rtgID
	 * @param randomTermID
	 * @param userID
	 * @return
	 * @throws DaoException 
	 */
	public TermToTermsGroup getSample(int rtgID, int randomTermID, User user, boolean openNewConnection) throws DaoException;
	
	/**
	 * 
	 * @param rtgID
	 * @throws DaoException 
	 */
	public void removeRandomTermsGroup(int rtgID) throws DaoException;
	
	/**
	 * 
	 * @param rtgID
	 * @param termID
	 * @throws DaoException 
	 */
	public void removeRandomTerm(int rtgID, int termID) throws DaoException;
}
