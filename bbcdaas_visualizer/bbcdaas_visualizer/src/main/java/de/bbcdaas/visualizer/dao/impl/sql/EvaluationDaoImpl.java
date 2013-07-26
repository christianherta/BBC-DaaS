package de.bbcdaas.visualizer.dao.impl.sql;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.TermToTermsGroup;
import de.bbcdaas.common.beans.TermToTermsGroups;
import de.bbcdaas.common.beans.User;
import de.bbcdaas.common.dao.base.SqlBaseDao;
import de.bbcdaas.common.dao.constants.sql.SQL_Keywords;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.visualizer.dao.EvaluationDao;
import de.bbcdaas.visualizer.dao.constants.sql.EvaluationDB_Queries;
import de.bbcdaas.visualizer.dao.constants.sql.EvaluationDB_Scheme;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Robert Illers
 */
public class EvaluationDaoImpl extends SqlBaseDao implements EvaluationDao {
	
	/**
	 * constructor
	 */
	public EvaluationDaoImpl() {}
	
	/**
	 * clear all tables except user
	 * @throws DaoException 
	 */
	@Override
	public void clearAllTables() throws DaoException {
		
		try {
			this.getJdbc().openConnection();
			//this.getJdbc().executeUpdate(new StringBuilder().append(SQL_Keywords.TRUNCATE_TABLE).append(EvaluationDB_Scheme.TABLE_USER).toString());
			this.getJdbc().executeUpdate(new StringBuilder().append(SQL_Keywords.TRUNCATE_TABLE).append(EvaluationDB_Scheme.TABLE_RANDOM_TERMS).toString());
			this.getJdbc().executeUpdate(new StringBuilder().append(SQL_Keywords.TRUNCATE_TABLE).append(EvaluationDB_Scheme.TABLE_SAMPLE).toString());
			this.getJdbc().commitAndCloseConnection();
		} catch(ApiException e) {
			throw new DaoException("clearAllTables",e.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 * @param randomTermIDs
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public int insertRandomTermsGroup(List<Integer> randomTermIDs, String groupLabel, float minSyntag) throws DaoException {
		
		int newRtgID = 0;
		try {
			this.getJdbc().openConnection();
			newRtgID = getMaxRandomTermsGroupID()+1;
			for (Integer randomTermID : randomTermIDs) {
				this.getJdbc().executeUpdate(EvaluationDB_Queries.INSERT_RANDOM_TERM, newRtgID, randomTermID);
			}
			this.getJdbc().executeUpdate(EvaluationDB_Queries.INSERT_RANDOM_TERMS_GROUP, newRtgID, groupLabel, minSyntag);
			this.getJdbc().commitAndCloseConnection();
		} catch(ApiException e) {
			throw new DaoException("insertRandomTermsGroup", e.getCompleteMessage());
		}
		return newRtgID;
	}
	
	/**
	 * 
	 * @param groupID
	 * @throws DaoException 
	 */
	@Override
	public void removeRandomTermsGroup(int rtgID) throws DaoException {
		
		try {
			this.getJdbc().openConnection();
			this.getJdbc().executeUpdate(EvaluationDB_Queries.REMOVE_RANDOM_TERMS_BY_RTG_ID, rtgID);
			this.getJdbc().executeUpdate(EvaluationDB_Queries.REMOVE_RANDOM_TERM_GROUP_BY_ID, rtgID);
			this.getJdbc().commitAndCloseConnection();
		} catch(ApiException e) {
			throw new DaoException("removeRandomTermsGroup", e.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 * @param rtgID
	 * @param termID
	 * @throws DaoException 
	 */
	@Override
	public void removeRandomTerm(int rtgID, int termID) throws DaoException {
		
		try {
			this.getJdbc().openConnection();
			this.getJdbc().executeUpdate(EvaluationDB_Queries.REMOVE_RANDOM_TERM_BY_RTG_ID_AND_TERM_ID, rtgID, termID);
			this.getJdbc().commitAndCloseConnection();
		} catch(ApiException e) {
			throw new DaoException("removeRandomTerm", e.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 * @param rtgID
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public TermToTermsGroups getRandomTermsGroupByID(int rtgID) throws DaoException {
	
		TermToTermsGroups randomTermsGroup = null;
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(EvaluationDB_Queries.GET_RANDOM_TERMS_GROUP_BY_ID, rtgID);
			this.getJdbc().closeConnection();
			if (!result.isEmpty()) {
				randomTermsGroup = new TermToTermsGroups();
				randomTermsGroup.setGroupID(rtgID);
			}
			for (Map<String, Object> row : result) {
				TermToTermsGroup randomTermWithTopTerms = new TermToTermsGroup();
				Term randomTerm = new Term();
				randomTerm.setId(((Long)row.get(EvaluationDB_Scheme.RANDOM_TERMS_COLUMN_RANDOM_TERM_ID)).intValue());
				randomTermWithTopTerms.setTerm(randomTerm);
				randomTermsGroup.getTermToTermsGroups().add(randomTermWithTopTerms);
			}
		} catch(ApiException e) {
			throw new DaoException("getRandomTermsGroupsByID", e.getCompleteMessage());
		}
		return randomTermsGroup;
	}
	
	/**
	 * 
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<TermToTermsGroups> getAllRandomTermGroups(boolean openNewConnection) throws DaoException {
		
		List<TermToTermsGroups> randomTermGroups = new ArrayList<TermToTermsGroups>();
		List<Integer> rtgIDs = new ArrayList<Integer>();
		
		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			result = this.getJdbc().executeQuery(EvaluationDB_Queries.GET_ALL_RANDOM_TERMS);
			for (Map<String, Object> row : result) {
				int rtgID = ((Long)row.get(EvaluationDB_Scheme.RANDOM_TERMS_COLUMN_RTG_ID)).intValue();
				if (!rtgIDs.contains(rtgID)) {
					rtgIDs.add(rtgID);
					TermToTermsGroups randomTermsGroup = new TermToTermsGroups();
					randomTermsGroup.setGroupID(rtgID);
					randomTermGroups.add(randomTermsGroup);
				}
				TermToTermsGroups randomTermsGroup = null;
				for (TermToTermsGroups group : randomTermGroups) {
					if (group.getGroupID() == rtgID) {
						randomTermsGroup = group;
						break;
					}
				}
				TermToTermsGroup randomTermWithTopTerms = new TermToTermsGroup();
				Term randomTerm = new Term();
				randomTerm.setId(((Long)row.get(EvaluationDB_Scheme.RANDOM_TERMS_COLUMN_RANDOM_TERM_ID)).intValue());
				randomTermWithTopTerms.setTerm(randomTerm);
				randomTermsGroup.getTermToTermsGroups().add(randomTermWithTopTerms);
			}
			result = this.getJdbc().executeQuery(EvaluationDB_Queries.GET_ALL_RANDOM_TERM_GROUPS);
			if (openNewConnection) {
				this.getJdbc().closeConnection();
			}
			
			for (Map<String, Object> row : result) {
				int rtgID = ((Long)row.get(EvaluationDB_Scheme.RANDOM_TERM_GROUPS_COLUMN_RTG_ID)).intValue();
				String groupLabel = (String)row.get(EvaluationDB_Scheme.RANDOM_TERM_GROUPS_COLUMN_GROUP_LABEL);
				float minSyntag = (Float)row.get(EvaluationDB_Scheme.RANDOM_TERM_GROUPS_COLUMN_MIN_SYNTAG);
				if (groupLabel != null && !groupLabel.isEmpty()) {
					TermToTermsGroups randomTermsGroup = null;
					for (TermToTermsGroups group : randomTermGroups) {
						if (group.getGroupID() == rtgID) {
							randomTermsGroup = group;
							break;
						}
					}
					randomTermsGroup.setGroupLabel(groupLabel);
					randomTermsGroup.setMinSyntag(minSyntag);
				}
			}
		} catch(ApiException e) {
			throw new DaoException("getAllRandomTermGroups", e.getCompleteMessage());
		}
		return randomTermGroups;
	}

	/**
	 * 
	 * @param rtgID
	 * @param userID
	 * @param randomTermID
	 * @param ratedTermID
	 * @param rating
	 * @throws DaoException 
	 */
	@Override
	public void insertRatedTermIntoSample(int rtgID, int userID, int randomTermID, int ratedTermID, int rating) throws DaoException {
	
		try {
			this.getJdbc().openConnection();
			this.getJdbc().executeUpdate(EvaluationDB_Queries.INSERT_SAMPLE_RATING, rtgID, userID, randomTermID, ratedTermID, rating, 0);
			this.getJdbc().commitAndCloseConnection();
		} catch(ApiException e) {
			throw new DaoException("insertRatedTermIntoSample", e.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 * @param rtgID
	 * @param userID
	 * @param randomTermID
	 * @param addedTermID
	 * @throws DaoException 
	 */
	@Override
	public void insertAddedTermIntoSample(int rtgID, int userID, int randomTermID, int addedTermID) throws DaoException {
	
		try {
			this.getJdbc().openConnection();
			this.getJdbc().executeUpdate(EvaluationDB_Queries.INSERT_SAMPLE_RATING, rtgID, userID, randomTermID, addedTermID, 0, 1);
			this.getJdbc().commitAndCloseConnection();
		} catch(ApiException e) {
			throw new DaoException("insertAddedTerm", e.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 * @param rtgID
	 * @param userID
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public TermToTermsGroup getSample(int rtgID, int randomTermID, User user, boolean openNewConnection) throws DaoException {
	
		TermToTermsGroup sample = null;
		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			result = this.getJdbc().executeQuery(EvaluationDB_Queries.GET_SAMPLE_BY_RTG_ID_AND_RANDOM_TERM_ID_AND_USER_ID, rtgID, user.getId(), randomTermID);
			if (openNewConnection) {
				this.getJdbc().closeConnection();
			}
			for (Map<String, Object> row : result) {
				if (sample == null) {
					sample = new TermToTermsGroup();
					sample.setTerm(new Term(randomTermID));
				}
				Term ratedTerm = new Term();
				ratedTerm.setId(((Long)row.get(EvaluationDB_Scheme.SAMPLE_COLUMN_RATED_TERM_ID)).intValue());
				ratedTerm.setRating(((Long)row.get(EvaluationDB_Scheme.SAMPLE_COLUMN_RATING)).intValue());
				ratedTerm.setAdded(((Long)row.get(EvaluationDB_Scheme.SAMPLE_COLUMN_ADDED)).intValue() == 1);
				sample.getRelatedTerms().add(ratedTerm);
			}
		} catch (ApiException e) {
			throw new DaoException("getSample", e.getCompleteMessage());
		}
		
		return sample;
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
	
	/*------------------------- private methods ------------------------------*/
	
	/**
	 * 
	 * @return
	 * @throws DaoException 
	 */
	private int getMaxRandomTermsGroupID() throws DaoException {
		
		int maxRandomTermsGroupID = 0;
		try {
			result = this.getJdbc().executeQuery(EvaluationDB_Queries.GET_MAX_RANDOM_TERMS_GROUP_ID);
			for (Map<String, Object> row : result) {
				maxRandomTermsGroupID = ((Long)row.get("maxRtgID")).intValue();
			}
		} catch(ApiException e) {
			throw new DaoException("getMaxRandomTermsGroupID", e.getCompleteMessage());
		}
		return maxRandomTermsGroupID;
	}
	
	/*------------------------ /private methods ------------------------------*/
}
