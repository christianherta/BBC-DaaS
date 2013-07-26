package de.bbcdaas.taghandler.dao;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.TermCloud;
import de.bbcdaas.common.beans.TermMatrixEntry;
import de.bbcdaas.common.beans.TermToTermsGroup;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.common.beans.entity.TermCloudField;
import de.bbcdaas.common.dao.base.BaseDao;
import de.bbcdaas.common.dao.exceptions.DaoException;
import java.util.List;

/**
 *
 * @author Robert Illers
 */
public interface TagHandlerDao extends BaseDao {
	
	/**
	 * 
	 * @throws DaoException 
	 */
	public void clearSyntagmaticRelations() throws DaoException;
	
	/**
	 * 
	 * @throws DaoException 
	 */
	public void clearTopSyntagmaticTerms() throws DaoException;
	
	/**
	 * 
	 * @param fieldID
	 * @param topSyntagmaticTerms
	 * @return
	 * @throws DaoException 
	 */
	public List<Float> getTopSyntagmaticTermSyntags(int fieldID, List<Term> topSyntagmaticTerms) throws DaoException;
	
	/**
	 * 
	 * @param term
	 * @param numberOfRandomTerms
	 * @return
	 * @throws DaoException 
	 */
	public List<Term> getTopRelatedTerms(int termID, int numberOfRandomTerms,
		float minSyntag, boolean openNewConnection, boolean shuffle) throws DaoException;
	
	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @return
	 * @throws DaoException 
	 */ 
	public List<TermCloud> getTermClouds(int offset, int rowCount) throws DaoException;
	
	/**
	 * 
	 * @param termId1
	 * @param termId2
	 * @return 
	 */
	public int getCooccurrence(int termId1, int termId2) throws DaoException;
	
	/**
	 * 
	 * @return 
	 */
	public int getMaxTermID(boolean openNewConnection) throws DaoException;
	
	/**
	 * 
	 * @return 
	 */
	public int getMinTermID(boolean openNewConnection) throws DaoException;
	
	/**
     * 
     * @param term
     * @param openNewConnection
     * @return
     * @throws DaoException 
     */
	public Term insertTerm(Term term, boolean openNewConnection) throws DaoException;
	
	/**
	 * 
	 * @return 
	 */
	public int getNumberOfTermMatrixEntries() throws DaoException;
	
	/**
	 * 
	 * @param termID
	 * @return 
	 */
	public Term getTerm(int termID, boolean openNewConnection) throws DaoException;
	
	/**
	 * 
	 * @return
	 * @throws DaoException 
	 */
	public Term getRandomTerm(boolean openNewConnection) throws DaoException;
	
	/**
	 * 
	 * @param minNumberOfTopRelatedTerms
	 * @param maxRandomTries
	 * @param numberOfTerms
	 * @param minTopTermSyntag
	 * @return
	 * @throws DaoException 
	 */
	public Term getRandomTerm(int minNumberOfTopRelatedTerms, int maxRandomTries,
		int numberOfTerms, float minTopTermSyntag, boolean openNewConnection) throws DaoException;
	
	/**
         * 
         * @param offset
         * @param rowCount
         * @param openNewConnection
         * @return
         * @throws DaoException 
         */
	public List<Term> getTerms(int offset, int rowCount, boolean openNewConnection) throws DaoException;
	
	
	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @param minFrequency
	 * @return
	 * @throws DaoException 
	 */
	public List<Term> getTermsWithMinFrequency(int offset, int rowCount,
		int minFrequency) throws DaoException;
	/**
	 * 
	 * @param termMatrixEntries
	 * @return
	 * @throws DaoException 
	 */
	public List<TermMatrixEntry> getTerms(List<TermMatrixEntry> termMatrixEntries) throws DaoException;
	
	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @return 
	 */
	public List<Term> getTermsSortedByLocalFrequency(int offset, int rowCount) throws DaoException;
	
	/**
	 * 
	 * @return 
	 */
	public int getNumberOfTerms(boolean openNewConnection) throws DaoException;
	
	/**
	 * Returns a list of fields that contain at least minMatchingTerms terms from the given term list
	 * @param terms
	 * @param minMatchingTerms
	 * @param offset
	 * @param rowCount
	 * @return
	 * @throws DaoException 
	 */ 
	public List<TermCloudField> getFieldsContainingAtLeastXTerms(List<Term> terms, int minMatchingTerms, int offset, int rowCount) throws DaoException;
	
	/**
	 * 
	 * @param terms
	 * @return
	 * @throws DaoException 
	 */
	public List<Entity> getEntitiesByFieldTerms(List<Term> terms) throws DaoException;
	
	/**
	 * 
	 * @param termID
	 * @param minSyntag
	 * @return
	 * @throws DaoException 
	 */
	public List<Term> getTopRelatedTerms(int termID, float minSyntag, boolean openNewConnection) throws DaoException;
	
	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @param minSyntag
	 * @return
	 * @throws DaoException 
	 */
	public List<TermToTermsGroup> getTopRelatedTerms(int offset, int rowCount, float minSyntag) throws DaoException;
	
	/**
	 * 
	 * @param termID
	 * @param topRelatedTerms
	 * @throws DaoException 
	 */
	public void insertTopRelatedTerms(int termID, List<Term> topRelatedTerms,List<Float> syntags, boolean openNewConnection) throws DaoException;
	
	/**
	 * 
	 * @param termID
	 * @return 
	 */
	public List<TermMatrixEntry> getRelatedTermMatrixEntries(int termID, boolean openNewConnection) throws DaoException;
	
	/**
	 * 
	 * @param entityName
	 * @return 
	 */
	public int getEntityID(String entityName) throws DaoException;
	
	/**
	 * 
	 * @param entity 
	 */
	public void insertEntity(Entity entity) throws DaoException;
	
	/**
	 * 
	 * @param entities
	 * @throws DaoException 
	 */
	public void insertEntities(List<Entity> entities) throws DaoException;
	
	/**
	 * 
	 * @param fieldID
	 * @param topSyntagmaticTerms
	 * @param syntags
	 * @throws DaoException 
	 */
	public void insertTopSyntagmaticTerms(TermCloud termCloud) throws DaoException;
	
	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @return
	 * @throws DaoException 
	 */
	public List<TermCloudField> getFields(int offset, int rowCount) throws DaoException;
	
	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @param openNewConnection
	 * @return
	 * @throws DaoException 
	 */
	public List<Entity> getEntities(int offset, int rowCount,
		boolean openNewConnection) throws DaoException;
	
	/**
	 * 
	 * @param entityID
	 * @return
	 * @throws DaoException 
	 */
	public Entity getEntity(int entityID) throws DaoException;
	
	/**
	 * 
	 * @param entityID
	 * @return
	 * @throws DaoException 
	 */
	public List<TermCloudField> getEntityFields(int entityID) throws DaoException;
	
	/**
	 * 
	 * @param fieldID
	 * @return
	 * @throws DaoException 
	 */
	public List<Term> getFieldTerms(int fieldID) throws DaoException;
	
	/**
	 * 
	 * @return
	 * @throws DaoException 
	 */
	public int getNumberOfEntities() throws DaoException;
	
	/**
	 * 
	 * @return
	 * @throws DaoException 
	 */
	public int getNumberOfFields() throws DaoException;
	
	/**
	 * 
	 * @param fieldID
	 * @return
	 * @throws DaoException 
	 */
	public int getNumberOfFieldTerms(int fieldID) throws DaoException;
	
	/**
	 * 
	 * @return
	 * @throws DaoException 
	 */
	public int getMaxEntityID() throws DaoException;
	
	/**
	 * 
	 * @return
	 * @throws DaoException 
	 */
	public int getMaxFieldID() throws DaoException;
	
	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @param openNewConnection
	 * @return
	 * @throws DaoException 
	 */
	public List<TermMatrixEntry> getTermMatrixEntries(int offset, int rowCount,
		boolean openNewConnection) throws DaoException;
	
	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @param openNewConnection
	 * @return
	 * @throws DaoException 
	 */
	public List<TermMatrixEntry> getTermMatrixWithTermData(int offset, int rowCount,
		boolean openNewConnection) throws DaoException;
	
	/**
	 * 
	 * @param termMatrixEntries
	 * @param openNewConnection
	 * @param commitResult
	 * @throws DaoException 
	 */
	public void insertSyntagmaticRelations(List<TermMatrixEntry> termMatrixEntries,
		boolean openNewConnection, boolean commitResult) throws DaoException;
	
	/**
	 * 
	 * @param termID
	 * @return
	 * @throws DaoException 
	 */
	public int getNumberOfTopRelatedTerms(int termID) throws DaoException;
	
	/**
	 * 
	 * @throws DaoException 
	 */
	public void clearTopRelatedTermTable() throws DaoException;
	
	/**
	 * 
	 * @param readEntities
	 * @param computeSyntagmaticRelations
	 * @param acomputeTopRelatedTerms
	 * @param computeTopSyntagmaticTerms
	 * @param a
	 * @param b
	 * @return
	 * @throws DaoException 
	 */
	public int insertNewProcessParameter(Boolean readEntities, Boolean computeSyntagmaticRelations,
		Boolean computeTopRelatedTerms, Boolean computeTopSyntagmaticTerms, Integer maxTopRelatedTerms,
		Float maxPercentageTopTerms, Integer minNbCorrelatedTerms, Integer minTermFrequency,
		Float minSyntagmaticValue, Float syntagmaticEntityTermFactor, Float a, Float b) throws DaoException;
	
	/**
	 * 
	 * @param processID
	 * @param errorCode
	 * @throws DaoException 
	 */
	public void updateProcessParameter(int processID, int errorCode) throws DaoException;
}
