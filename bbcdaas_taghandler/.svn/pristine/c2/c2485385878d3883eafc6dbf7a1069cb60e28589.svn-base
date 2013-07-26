package de.bbcdaas.taghandler.dao.impl.sql;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.TermCloud;
import de.bbcdaas.common.beans.TermMatrixEntry;
import de.bbcdaas.common.beans.TermToTermsGroup;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.common.beans.entity.TermCloudField;
import de.bbcdaas.common.dao.base.SqlBaseDao;
import de.bbcdaas.common.dao.constants.sql.SQL_Keywords;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.taghandler.dao.TagHandlerDao;
import de.bbcdaas.taghandler.dao.constants.sql.TagHandlerDB_Queries;
import de.bbcdaas.taghandler.dao.constants.sql.TagHandlerDB_Scheme;
import java.util.*;
import java.util.Map.Entry;

/**
 * DAO that stores and retrieves data from the tagHandlers term db.
 * @author Robert Illers
 */
public class TagHandlerDaoImpl extends SqlBaseDao implements TagHandlerDao {

	/**
	 * constructor
	 */
	public TagHandlerDaoImpl() {
	}

	/**
	 * 
	 */
	@Override
	public void clearAllTables() throws DaoException {

		try {
			this.getJdbc().openConnection();
			this.getJdbc().executeUpdate(new StringBuilder().append(SQL_Keywords.TRUNCATE_TABLE).append(TagHandlerDB_Scheme.TABLE_ENTITY).toString());
			this.getJdbc().executeUpdate(new StringBuilder().append(SQL_Keywords.TRUNCATE_TABLE).append(TagHandlerDB_Scheme.TABLE_ENTITY_FIELD).toString());
			this.getJdbc().executeUpdate(new StringBuilder().append(SQL_Keywords.TRUNCATE_TABLE).append(TagHandlerDB_Scheme.TABLE_FIELD_TERM).toString());
			this.getJdbc().executeUpdate(new StringBuilder().append(SQL_Keywords.TRUNCATE_TABLE).append(TagHandlerDB_Scheme.TABLE_FIELD_TOP_SYNTAG_TERM).toString());
			this.getJdbc().executeUpdate(new StringBuilder().append(SQL_Keywords.TRUNCATE_TABLE).append(TagHandlerDB_Scheme.TABLE_TERM).toString());
			this.getJdbc().executeUpdate(new StringBuilder().append(SQL_Keywords.TRUNCATE_TABLE).append(TagHandlerDB_Scheme.TABLE_TERM_MATRIX).toString());
			this.getJdbc().executeUpdate(new StringBuilder().append(SQL_Keywords.TRUNCATE_TABLE).append(TagHandlerDB_Scheme.TABLE_TOP_RELATED_TERM).toString());
			this.getJdbc().commitAndCloseConnection();
		} catch (ApiException e) {
			throw new DaoException("clearAllTables", e.getCompleteMessage());
		}
	}

	/**
	 * 
	 * @throws DaoException 
	 */
	@Override
	public void clearTopRelatedTermTable() throws DaoException {

		try {
			this.getJdbc().openConnection();
			this.getJdbc().executeUpdate(new StringBuilder().append(SQL_Keywords.TRUNCATE_TABLE).append(TagHandlerDB_Scheme.TABLE_TOP_RELATED_TERM).toString());
			this.getJdbc().commitAndCloseConnection();
		} catch (ApiException e) {
			throw new DaoException("clearTopRelatedTermTable", e.getCompleteMessage());
		}
	}

	/**
	 * 
	 * @throws DaoException 
	 */
	@Override
	public void clearSyntagmaticRelations() throws DaoException {

		try {
			this.getJdbc().openConnection();
			// using drop column/add column to save much memory (table is very, very large)
			getJdbc().executeUpdate(TagHandlerDB_Queries.CLEAR_SYNTAGMATIC_RELATIONS1);
			getJdbc().executeUpdate(TagHandlerDB_Queries.CLEAR_SYNTAGMATIC_RELATIONS2);
			this.getJdbc().commitAndCloseConnection();
		} catch (ApiException e) {
			throw new DaoException("clearSyntagmaticRelations", e.getCompleteMessage());
		}
	}

	/**
	 * 
	 * @throws DaoException 
	 */
	@Override
	public void clearTopSyntagmaticTerms() throws DaoException {

		try {
			this.getJdbc().openConnection();
			getJdbc().executeUpdate(TagHandlerDB_Queries.CLEAR_TOP_SYNTAGMATIC_TERMS);
			this.getJdbc().commitAndCloseConnection();
		} catch (ApiException e) {
			throw new DaoException("clearTopSyntagmaticTerms", e.getCompleteMessage());
		}

	}

	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<TermCloud> getTermClouds(int offset, int rowCount) throws DaoException {

		List<TermCloud> termClouds = new ArrayList<TermCloud>();

		List<Entity> entities = this.getEntities(offset, rowCount, true);

		for (Entity entity : entities) {
			List<TermCloudField> fields = this.getFields(entity.getID(), true, true);
			for (TermCloudField field : fields) {
				TermCloud termCloud = new TermCloud();
				termCloud.setEntityID(entity.getID());
				termCloud.setEntityName(entity.getName());
				termCloud.setFieldType(field.getFieldType());
				termCloud.setFieldID(field.getID());
				termCloud.setTerms(field.getTerms());
				termCloud.setSyntagmaticTerms(field.getSyntagmaticTerms());
				termClouds.add(termCloud);
			}
		}
		return termClouds;
	}

	/**
	 * 
	 * @param topSyntagmaticTerms
	 * @return 
	 */
	@Override
	public List<Float> getTopSyntagmaticTermSyntags(int fieldID, List<Term> topSyntagmaticTerms) throws DaoException {

		List<Float> syntags = new ArrayList<Float>();
		try {
			this.getJdbc().openConnection();
			for (Term term : topSyntagmaticTerms) {
				result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_TOP_SYNTAGMATIC_TERMS_SYNTAG, fieldID, term.getId());
				for (Map<String, Object> row : result) {
					syntags.add((Float) row.get(TagHandlerDB_Scheme.FIELD_TOP_SYNTAG_TERM_COLUMN_SCORE));
				}
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getTopSyntagmaticTermSyntags", e.getCompleteMessage());
		}
		return syntags;
	}

	/**
	 * Gets a random selection of terms from the list of found top related terms.
	 * @param termID 
	 * @param numberOfTerms not used
	 * @param minSyntag 
	 * @param openNewConnection set to true for opening a new db connection
	 * @param shuffle not used
	 * @return List<Term>
	 * @throws DaoException 
	 */
	@Override
	public List<Term> getTopRelatedTerms(int termID, int numberOfTerms,
		float minSyntag, boolean openNewConnection, boolean shuffle) throws DaoException {

//		List<Term> randomTerms = new ArrayList<Term>();
		List<Term>	topRelatedTerms = this.getTopRelatedTerms(termID, minSyntag, openNewConnection);
		
//		if (numberOfTerms > topRelatedTerms.size()) {
//			numberOfTerms = topRelatedTerms.size();
//		}
//
//		List<Integer> termRepresentingInts = new ArrayList<Integer>();
//		for (int i = 0; i < topRelatedTerms.size(); i++) {
//			termRepresentingInts.add(i);
//		}
//
//		if (shuffle) {
//			Collections.shuffle(termRepresentingInts);
//		}
//
//		List<Integer> randomizedTermRepresentingInts = new ArrayList<Integer>();
//		for (int i = numberOfTerms; i > 0; i--) {
//			randomizedTermRepresentingInts.add(termRepresentingInts.remove((int) (Math.random() * i)));
//		}
//
//		for (int randomizedTermRepresentingInt : randomizedTermRepresentingInts) {
//			randomTerms.add(topRelatedTerms.get(randomizedTermRepresentingInt));
//		}
//		return randomTerms;
		return topRelatedTerms;
	}

	/**
	 * 
	 * @param termId1
	 * @param termId2
	 * @return 
	 */
	@Override
	public int getCooccurrence(int termId1, int termId2) throws DaoException {

		int ret = 0;
		int firstTermId, secondTermId;
		if (termId1 < termId2) {
			firstTermId = termId1;
			secondTermId = termId2;
		} else {
			firstTermId = termId2;
			secondTermId = termId1;
		}
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_COOCCURRENCE, firstTermId, secondTermId);
			for (Map<String, Object> row : result) {
				ret = ((Long) row.get(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_COOC)).intValue();
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getCooccurrence", e.getCompleteMessage());
		}
		return ret;
	}

	/**
	 * 
	 * @return 
	 */
	@Override
	public int getMaxTermID(boolean openNewConnection) throws DaoException {

		int ret = 0;
		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_MAX_TERM_ID);
			for (Map<String, Object> row : result) {
				ret = ((Long) row.get("maxTermID")).intValue();
			}
			if (openNewConnection)  {
				this.getJdbc().closeConnection();
			}
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
	public int getMinTermID(boolean openNewConnection) throws DaoException {

		int ret = 0;
		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_MIN_TERM_ID);
			for (Map<String, Object> row : result) {
				ret = ((Long) row.get("minTermID")).intValue();
			}
			if (openNewConnection) {
				this.getJdbc().closeConnection();
			}
		} catch (ApiException e) {
			throw new DaoException("getMinTermID", e.getCompleteMessage());
		}
		return ret;
	}

	/**
	 * 
	 * @param term 
	 */
	private void increaseLocalTermFrequency(int termID, boolean openNewConnection) throws DaoException {

		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			this.getJdbc().executeUpdate(TagHandlerDB_Queries.INCREASE_TERM_FREQUENCY, termID);
			if (openNewConnection) {
				this.getJdbc().commitAndCloseConnection();
			} 
		} catch (ApiException e) {
			throw new DaoException("increaseTermFrequency", e.getCompleteMessage());
		}
	}

	/**
	 * 
	 * @param termID
	 * @param openNewConnection
	 * @return
	 * @throws DaoException 
	 */
	private Term insertNewTerm(int termID, String termValue, boolean openNewConnection) throws DaoException {

		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			this.getJdbc().executeUpdate(TagHandlerDB_Queries.INSERT_NEW_TERM, termID, termValue);
			if (openNewConnection) {
				this.getJdbc().commitAndCloseConnection();
			} 
		} catch (ApiException e) {
			throw new DaoException("nsertNewTerm", e.getCompleteMessage());
		}
		Term term = new Term(termID);
		term.setLocalFrequency(1);
		return term;
	}

	/**
     * 
     * @param term
     * @param openNewConnection
     * @return
     * @throws DaoException 
     */
	@Override
	public Term insertTerm(Term term, boolean openNewConnection) throws DaoException {

		Term verifiedTerm = getTerm(term.getId(), openNewConnection);
		if (verifiedTerm != null) {
			increaseLocalTermFrequency(verifiedTerm.getId(), openNewConnection);
			term.setLocalFrequency(verifiedTerm.getLocalFrequency() + 1);
		} else {
			verifiedTerm = insertNewTerm(term.getId(), term.getValue(), openNewConnection);
		}
		return verifiedTerm;
	}

	/**
	 * 
	 * @param termValue1
	 * @param termValue2
	 * @return 
	 */
	private void insertCooccurrence(Term term1, Term term2, boolean openNewConnection) throws DaoException {

		// sort terms by id
		int firstTermID, secondTermID;
		if (term1.getId() < term2.getId()) {
			firstTermID = term1.getId();
			secondTermID = term2.getId();
		} else {
			firstTermID = term2.getId();
			secondTermID = term1.getId();
		}

		try {
			if (openNewConnection) {
					this.getJdbc().openConnection();
				}
			if (matrixEntryExist(firstTermID, secondTermID)) {
				this.getJdbc().executeUpdate(TagHandlerDB_Queries.INCREASE_COOCCURRENCE, firstTermID, secondTermID);
			} else {
				this.getJdbc().executeUpdate(TagHandlerDB_Queries.INSERT_NEW_TERM_MATRIX_ENTRY, firstTermID, secondTermID);
			}
			if (openNewConnection) {
				this.getJdbc().commitAndCloseConnection();
			}
		} catch (ApiException e) {
			throw new DaoException("insertCooccurrence", e.getCompleteMessage());
		}
	}

	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<TermMatrixEntry> getTermMatrixEntries(int offset, int rowCount,
		boolean openNewConnection) throws DaoException {

		String query = new StringBuilder().append(TagHandlerDB_Queries.GET_ALL_MATRIX_ENTRIES).
				append(SQL_Keywords.LIMIT).append(offset).append(",").append(rowCount).toString();
		List<TermMatrixEntry> ret = new ArrayList<TermMatrixEntry>();
		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			result = getJdbc().executeQuery(query);
			if (openNewConnection) {
				this.getJdbc().closeConnection();
			}
			for (Map<String, Object> row : result) {
				TermMatrixEntry entry = new TermMatrixEntry();
				Term term1 = new Term();
				term1.setId(((Long) row.get(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID1)).intValue());
				Term term2 = new Term();
				term2.setId(((Long) row.get(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID2)).intValue());
				entry.setTerm1(term1);
				entry.setTerm2(term2);
				entry.setCoocurrence(((Long) row.get(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_COOC)).intValue());
				entry.setSyntag((Float) row.get(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_SYNTAG));
				ret.add(entry);
			}
		} catch (ApiException e) {
			throw new DaoException("getTermMatrixEntries", e.getCompleteMessage());
		}
		return ret;
	}

	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @param openNewConnection
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<TermMatrixEntry> getTermMatrixWithTermData(int offset, int rowCount,
		boolean openNewConnection) throws DaoException {

		String query = new StringBuilder().append(TagHandlerDB_Queries.GET_ALL_MATRIX_ENTRIES_WITH_TERM_DATA).
				append(SQL_Keywords.LIMIT).append(offset).append(",").append(rowCount).toString();
		List<TermMatrixEntry> ret = new ArrayList<TermMatrixEntry>();
		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			result = getJdbc().executeQuery(query);
			if (openNewConnection) {
				this.getJdbc().closeConnection();
			}
			for (Map<String, Object> row : result) {
				TermMatrixEntry entry = new TermMatrixEntry();
				Term term1 = new Term();
				term1.setId(((Long) row.get("termID1")).intValue());
				term1.setLocalFrequency(((Long) row.get("termFrequency1")).intValue());
				Term term2 = new Term();
				term2.setId(((Long) row.get("termID2")).intValue());
				term2.setLocalFrequency(((Long) row.get("termFrequency2")).intValue());
				entry.setTerm1(term1);
				entry.setTerm2(term2);
				entry.setCoocurrence(((Long) row.get(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_COOC)).intValue());
				entry.setSyntag((Float) row.get(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_SYNTAG));
				ret.add(entry);
			}
		} catch (ApiException e) {
			throw new DaoException("getTermMatrixWithTermData", e.getCompleteMessage());
		}
		return ret;
	}

	/**
	 * 
	 * @param termMatrixEntries
	 * @param openNewConnection
	 * @throws DaoException 
	 */
	@Override
	public void insertSyntagmaticRelations(List<TermMatrixEntry> termMatrixEntries,
		boolean openNewConnection, boolean commitResult) throws DaoException {

		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			for (TermMatrixEntry termMatrixEntry : termMatrixEntries) {

				this.insertSyntagmaticRelation(termMatrixEntry.getTermId1(), 
					termMatrixEntry.getTermId2(), termMatrixEntry.getSyntag(), false);
			}
			if (openNewConnection) {
				this.getJdbc().commitAndCloseConnection();
			} else if (commitResult) {
				this.getJdbc().commit();
			}
		} catch (ApiException e) {
			throw new DaoException("insertSyntagmaticRelations", e.getCompleteMessage());
		}
	}

	/**
	 * 
	 * @param termID1
	 * @param termID2
	 * @param syntag
	 * @throws DaoException 
	 */
	private void insertSyntagmaticRelation(int termID1, int termID2, float syntag,
		boolean openNewConnection) throws DaoException {

		// sort terms by id
		int firstTermID, secondTermID;
		if (termID1 < termID2) {
			firstTermID = termID1;
			secondTermID = termID2;
		} else {
			firstTermID = termID2;
			secondTermID = termID1;
		}

		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			getJdbc().executeUpdate(TagHandlerDB_Queries.SET_SYNTAGMATIC_RELATION, syntag, firstTermID, secondTermID);
			if (openNewConnection) {
				this.getJdbc().commitAndCloseConnection();
			}
		} catch (ApiException e) {
			throw new DaoException("insertSyntagmaticRelation", e.getCompleteMessage());
		}
	}

	/**
	 * 
	 * @param termID1
	 * @param termID2
	 * @return 
	 */
	private boolean matrixEntryExist(int termID1, int termID2) throws DaoException {

		boolean matrixEntryExists = true;
		try {
			this.getJdbc().openConnection();
			result = getJdbc().executeQuery(TagHandlerDB_Queries.MATRIX_ENTRY_EXISTS, termID1, termID2);
			if (result.isEmpty()) {
				matrixEntryExists = false;
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("matrixEntryExist", e.getCompleteMessage());
		}
		return matrixEntryExists;
	}

	/**
	 * 
	 * @return 
	 */
	@Override
	public int getNumberOfTermMatrixEntries() throws DaoException {

		int ret = 0;
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_NUMBER_OF_TERM_MATRIX_ENTRIES);
			for (Map<String, Object> row : result) {
				ret = ((Long) row.get("numberOfTermMatrixEntries")).intValue();
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getNumberOfTermMatrixEntries", e.getCompleteMessage());
		}
		return ret;
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
			result = getJdbc().executeQuery(TagHandlerDB_Queries.GET_TERM_BY_ID, termID);
			for (Map<String, Object> row : result) {
				term = new Term(termID);
                                term.setValue((String) row.get(TagHandlerDB_Scheme.TERM_COLUMN_TERM_VALUE));
				term.setLocalFrequency(((Long) row.get(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY)).intValue());
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
	 * Returns a random term.
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public Term getRandomTerm(boolean openNewConnection) throws DaoException {

		Term randomTerm = null;

		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			randomTerm = this.getTerm((int)(Math.random()*this.getNumberOfTerms(false)), false);
			if (openNewConnection) {
				this.getJdbc().closeConnection();
			}
		} catch(ApiException e) {
			throw new DaoException("getRandomTerm", e.getCompleteMessage());
		}
		
		return randomTerm;
	}

	/**
	 * Gets a random term that has at least a minNumberOfTopRelatedTerms number of topRelatedTerms. There
	 * will be maxRandomTries attempts to get this term.
	 * @param numberOfTopRelatedTerms
	 * @param maxRandomTries
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public Term getRandomTerm(int minNumberOfTopRelatedTerms, int maxRandomTries,
		int numberOfTerms, float minTopTermSyntag, boolean openNewConnection) throws DaoException {

		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			for (int i = 0; i < maxRandomTries; i++) {
				int rndTermID = (int) (Math.random() * numberOfTerms);
				List<Term> topRelatedTerms = this.getTopRelatedTerms(rndTermID, minTopTermSyntag, false);
				if (topRelatedTerms.size() >= minNumberOfTopRelatedTerms) {
					return this.getTerm(rndTermID, false);
				}
			}
			if (openNewConnection) {
				this.getJdbc().closeConnection();
			}
		} catch(ApiException e) {
			throw new DaoException("getRandomTerm", e.getCompleteMessage());
		}
		return null;
	}

	/**
         * 
         * @param offset
         * @param rowCount
         * @param openNewConnection
         * @return
         * @throws DaoException 
         */
	@Override
	public List<Term> getTerms(int offset, int rowCount, boolean openNewConnection) throws DaoException {

		String query = new StringBuilder().append(TagHandlerDB_Queries.GET_TERMS).append(SQL_Keywords.LIMIT).
				append(offset).append(",").append(rowCount).toString();
		List<Term> terms = new ArrayList<Term>();
		try {
                        if (openNewConnection) {
                            this.getJdbc().openConnection();
                        }
			result = this.getJdbc().executeQuery(query);
			for (Map<String, Object> row : result) {
				Term term = new Term();
				term.setId(((Long) row.get(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID)).intValue());
                                term.setValue((String) row.get(TagHandlerDB_Scheme.TERM_COLUMN_TERM_VALUE));
				term.setLocalFrequency(((Long) row.get(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY)).intValue());
				terms.add(term);
			}
                        if (openNewConnection) {
                            this.getJdbc().closeConnection();
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
	 * @param minFrequency
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<Term> getTermsWithMinFrequency(int offset, int rowCount,
		int minFrequency) throws DaoException {

		List<Term> terms = new ArrayList<Term>();
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_TERMS_WITH_MIN_FREQUENCY,
				minFrequency, offset, rowCount);
			for (Map<String, Object> row : result) {
				Term term = new Term();
				term.setId(((Long) row.get(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID)).intValue());
				term.setLocalFrequency(((Long) row.get(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY)).intValue());
				terms.add(term);
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getTermsWithMinFrequency", e.getCompleteMessage());
		}
		return terms;
	}

	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @param sortByColumn
	 * @return 
	 */
	private List<Term> getSortedTerms(int offset, int rowCount, String sortByColumn) throws DaoException {

		String query = new StringBuilder().append(TagHandlerDB_Queries.GET_TERMS).
				append(SQL_Keywords.ORDER_BY).append(sortByColumn).append(SQL_Keywords.DESC).
				append(SQL_Keywords.LIMIT).append(offset).append(",").append(rowCount).toString();
		List<Term> terms = new ArrayList<Term>();
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(query);
			for (Map<String, Object> row : result) {
				Term term = new Term();
				term.setId(((Long) row.get(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID)).intValue());
				term.setLocalFrequency(((Long) row.get(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY)).intValue());
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
	 * @param offset
	 * @param rowCount
	 * @return 
	 */
	@Override
	public List<Term> getTermsSortedByLocalFrequency(int offset, int rowCount) throws DaoException {

		return getSortedTerms(offset, rowCount, TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY);
	}

	/**
	 * 
	 * @return 
	 */
	@Override
	public int getNumberOfTerms(boolean openNewConnection) throws DaoException {

		int number = 0;
		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_NUMBER_OF_TERMS);
			for (Map<String, Object> row : result) {
				number = ((Long) row.get("numberOfTerms")).intValue();
			}
			if (openNewConnection) {
				this.getJdbc().closeConnection();
			}
		} catch (ApiException e) {
			throw new DaoException("getNumberOfTerms", e.getCompleteMessage());
		}
		return number;
	}

	/**
	 * Returns a list of fields that contain at least minMatchingTerms terms from the given term list
	 * @param terms
	 * @param minMatchingTerms number of terms that should match the field terms
	 * @return 
	 */
	@Override
	public List<TermCloudField> getFieldsContainingAtLeastXTerms(List<Term> dictionaryTerms, int minMatchingTerms, int offset, int rowCount) throws DaoException {

		List<TermCloudField> fields = new ArrayList<TermCloudField>();
		try {
			StringBuilder query = new StringBuilder().append(SQL_Keywords.SELECT).append(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_FIELD_ID).
					append(",").append(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_TERM_ID).append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_FIELD_TERM).
					append(SQL_Keywords.WHERE).append(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_TERM_ID).append(SQL_Keywords.IN).append("(");
			for (Term dictionaryTerm : dictionaryTerms) {
				query.append(dictionaryTerm.getId()).append(",");
			}
			query.deleteCharAt(query.length() - 1);
			query.append(")").append(SQL_Keywords.LIMIT).append(offset).append(",").append(rowCount);
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(query.toString());
			this.getJdbc().closeConnection();

			// get all field-term relations from db that contain given terms
			Map<Integer, List<Integer>> allField_terms = new HashMap<Integer, List<Integer>>();
			for (Map<String, Object> row : result) {
				int fieldId = ((Long) row.get(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_FIELD_ID)).intValue();
				int termId = ((Long) row.get(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_TERM_ID)).intValue();
				if (allField_terms.containsKey(fieldId)) {
					allField_terms.get(fieldId).add(termId);
				} else {
					List<Integer> termIDs = new ArrayList<Integer>();
					termIDs.add(termId);
					allField_terms.put(fieldId, termIDs);
				}
			}

			// collect the fields that have at least minMatchingTerms terms
			List<Integer> useableFieldIds = new ArrayList<Integer>();
			for (Entry<Integer, List<Integer>> field_terms : allField_terms.entrySet()) {
				if (field_terms.getValue().size() >= minMatchingTerms) {
					useableFieldIds.add(field_terms.getKey());
				}
			}
			// get the fields demanded
			for (Integer useableFieldID : useableFieldIds) {
				TermCloudField termCloudField = new TermCloudField();
				termCloudField.setID(useableFieldID);
				termCloudField.setTerms(this.getFieldTerms(useableFieldID));
				for (Term term : termCloudField.getTerms()) {
					for (Term dictionaryTerm : dictionaryTerms) {
						if (term.getId() == dictionaryTerm.getId()) {
							term.setSecondId(dictionaryTerm.getSecondId());
							break;
						}
					}
				}
				fields.add(termCloudField);
			}
		} catch (ApiException e) {
			throw new DaoException("getFieldsContainingAtLeastXTerms", e.getCompleteMessage());
		}
		return fields;
	}

	/**
	 * Returns the entities that ALL contains all of the terms in the list in one of their fields
	 * @param terms
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<Entity> getEntitiesByFieldTerms(List<Term> terms) throws DaoException {

		List<Entity> entities = new ArrayList<Entity>();
		//todo: "constantize" queries
		try {

			this.getJdbc().openConnection();
			List<List<Integer>> allFieldIds = new ArrayList<List<Integer>>();
			for (Term term : terms) {
				String x = "SELECT ft.fieldId FROM field_term ft WHERE ft.termId=" + term.getId();
				List<Integer> fieldIDs = new ArrayList<Integer>();
				result = getJdbc().executeQuery(x);
				for (Map<String, Object> row : result) {
					fieldIDs.add(((Long) row.get("fieldId")).intValue());
				}
				allFieldIds.add(fieldIDs);
			}

			List<Integer> intersectedFieldIds = new ArrayList<Integer>();
			int i = 0;
			for (List<Integer> fieldIds : allFieldIds) {
				if (i == 0) {
					intersectedFieldIds.addAll(fieldIds);
				} else {
					intersectedFieldIds.retainAll(fieldIds);
				}
				i++;
			}
			for (Integer fieldId : intersectedFieldIds) {
				String y = "SELECT e.entityId, e.entityName FROM entity e, entity_field ef WHERE e.entityId=ef.entityId AND ef.fieldId=" + fieldId;
				result = getJdbc().executeQuery(y);
				for (Map<String, Object> row : result) {
					Entity entity = new Entity();
					entity.setID(((Long) row.get(TagHandlerDB_Scheme.ENTITY_COLUMN_ENTITY_ID)).intValue());
					entity.setName((String) row.get(TagHandlerDB_Scheme.ENTITY_COLUMN_ENTITY_NAME));
					entities.add(entity);
				}
			}
			getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getEntitiesByFieldTerms", e.getCompleteMessage());
		}
		return entities;
	}

	/**
	 * 
	 * @param termID
	 * @return 
	 */
	@Override
	public int getNumberOfTopRelatedTerms(int termID) throws DaoException {

		int ret = 0;
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_NUMBER_OF_TOP_RELATED_TERMS, termID);
			for (Map<String, Object> row : result) {
				ret = ((Long) row.get("numberOfTopRelatedTerms")).intValue();
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getNumberOfTopRelatedTerms", e.getCompleteMessage());
		}
		return ret;
	}

	/**
	 * 
	 * @param termID
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<Term> getTopRelatedTerms(int termID, float minSyntag, boolean openNewConnection) throws DaoException {

		List<Term> topRelatedTerms = new ArrayList<Term>();
		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			result = getJdbc().executeQuery(TagHandlerDB_Queries.GET_TOP_RELATED_TERMS_BY_ID_WITH_MIN_SYNTAG, termID, minSyntag);
			for (Map<String, Object> row : result) {
				Term term = new Term();
				term.setId(((Long) row.get(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID)).intValue());
                                term.setValue((String) row.get(TagHandlerDB_Scheme.TERM_COLUMN_TERM_VALUE));
				term.setLocalFrequency(((Long) row.get(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY)).intValue());
				term.setSyntag((Float) row.get(TagHandlerDB_Scheme.TOP_RELATED_TERM_COLUMN_SYNTAG));
				topRelatedTerms.add(term);
			}
			if (openNewConnection) {
				getJdbc().closeConnection();
			}
		} catch (ApiException e) {
			throw new DaoException("getTopRelatedTerms", e.getCompleteMessage());
		}
		return topRelatedTerms;
	}

	/**
	 * 
	 * @param offset
	 * @param rowCount
	 * @return 
	 */
	@Override
	public List<TermToTermsGroup> getTopRelatedTerms(int offset, int rowCount, float minSyntag) throws DaoException {

		List<TermToTermsGroup> topRelatedTermEntries = new ArrayList<TermToTermsGroup>();
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_TOP_RELATED_TERMS, offset, rowCount);
			int termID;
			Term term = null;
			TermToTermsGroup entry = null;
			for (Map<String, Object> row : result) {

				termID = ((Long) row.get("termID2")).intValue();
				if (term == null || term.getId() != termID) {
					if (entry != null) {
						topRelatedTermEntries.add(entry);
					}
					entry = new TermToTermsGroup();
					term = new Term();
					term.setId(termID);
					term.setLocalFrequency(((Long) row.get("termFrequency2")).intValue());
					entry.setTerm(term);
				}
				Term topTerm = new Term();
				topTerm.setId(((Long) row.get("termID1")).intValue());
				topTerm.setLocalFrequency(((Long) row.get("termFrequency1")).intValue());
				entry.getRelatedTerms().add(topTerm);
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getTopRelatedTerms", e.getCompleteMessage());
		}
		return topRelatedTermEntries;
	}

	/**
	 * 
	 * @param termID
	 * @param topRelatedTerms
	 * @throws DaoException 
	 */
	@Override
	public void insertTopRelatedTerms(int termID, List<Term> topRelatedTerms, List<Float> syntags, boolean openNewConnection) throws DaoException {

		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			int i = 0;
			for (Term term : topRelatedTerms) {
				this.getJdbc().executeUpdate(TagHandlerDB_Queries.INSERT_TOP_RELATED_TERMS, termID, term.getId(), syntags.get(i));
				i++;
			}
			if (openNewConnection) {
				this.getJdbc().commitAndCloseConnection();
			} else {
				this.getJdbc().commit();
			}
		} catch (ApiException e) {
			throw new DaoException("insertTopRelatedTerms", e.getCompleteMessage());
		}
	}

	/**
	 * 
	 * @param termID
	 * @return 
	 */
	@Override
	public List<TermMatrixEntry> getRelatedTermMatrixEntries(int termID, boolean openNewConnection) throws DaoException {

		List<TermMatrixEntry> termMatrixEntries = new ArrayList<TermMatrixEntry>();
		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_RELATED_TERM_MATRIX_ENTRIES, termID, termID);
			for (Map<String, Object> row : result) {
				TermMatrixEntry entry = new TermMatrixEntry();
				Term term1 = new Term(((Long) row.get(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID1)).intValue());
				Term term2 = new Term(((Long) row.get(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID2)).intValue());
				entry.setTerm1(term1);
				entry.setTerm2(term2);
				entry.setCoocurrence(((Long) row.get(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_COOC)).intValue());
				entry.setSyntag((Float) row.get(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_SYNTAG));
				termMatrixEntries.add(entry);
			}
			if (openNewConnection) {
				this.getJdbc().closeConnection();
			}
		} catch (ApiException e) {
			throw new DaoException("getRelatedTermMatrixEntries", e.getCompleteMessage());
		}
		return termMatrixEntries;
	}

	/**
	 * 
	 * @param entityName
	 * @return 
	 */
	@Override
	public int getEntityID(String entityName) throws DaoException {

		int ret = 0;
		try {
			this.getJdbc().openConnection();
			result = getJdbc().executeQuery(TagHandlerDB_Queries.GET_ENTITY_ID, entityName);
			for (Map<String, Object> row : result) {
				ret = ((Long) row.get(TagHandlerDB_Scheme.ENTITY_COLUMN_ENTITY_ID)).intValue();
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getEntityID", e.getCompleteMessage());
		}
		return ret;
	}

	/**
	 * 
	 * @param fieldID
	 * @param terms
	 * @throws DaoException 
	 */
	private void addTermsToField(int fieldID, List<Term> terms, boolean openNewConnection) throws DaoException {

		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			for (Term term : terms) {
				this.getJdbc().executeUpdate(TagHandlerDB_Queries.ADD_TERM_TO_FIELD, fieldID, term.getId());
			}
			if (openNewConnection) {
				this.getJdbc().commitAndCloseConnection();
			}
		} catch (ApiException e) {
			throw new DaoException("addTermsToField", e.getCompleteMessage());
		}
	}

	/**
	 * 
	 * @param entity 
	 */
	@Override
	public void insertEntity(Entity entity) throws DaoException {

		// add entity
		entity.setID(this.getMaxEntityID() + 1);
		try {
			this.getJdbc().openConnection();
			this.getJdbc().executeUpdate(TagHandlerDB_Queries.INSERT_ENTITY, entity.getName(), entity.getID());
			// add fields of entity
			int maxFieldID = this.getMaxFieldID();
			for (TermCloudField field : entity.getFields()) {
				maxFieldID++;
				field.setID(maxFieldID);
				this.getJdbc().executeUpdate(TagHandlerDB_Queries.INSERT_FIELD, entity.getID(), field.getFieldType(), field.getID());
			}
			
			// add terms of fields
			for (TermCloudField field : entity.getFields()) {
				for (Term term : field.getTerms()) {
					term = insertTerm(term, false);
				}
				this.addTermsToField(field.getID(), field.getTerms(), false);
			}

			// create term matrix
			for (TermCloudField field : entity.getFields()) {
				for (int i = 0; i < field.getTerms().size(); i++) {
					for (int j = i + 1; j < field.getTerms().size(); j++) {
						this.insertCooccurrence(field.getTerms().get(i), field.getTerms().get(j), false);
					}
				}
			}
			this.getJdbc().commitAndCloseConnection();
		} catch (ApiException e) {
			throw new DaoException("insertEntity", e.getCompleteMessage());
		}
	}

	/**
	 * 
	 * @param entities 
	 */
	@Override
	public void insertEntities(List<Entity> entities) throws DaoException {

		int entityID = this.getMaxEntityID();
		int fieldID = this.getMaxFieldID();
		
		try {

			this.getJdbc().openConnection();
			// add entities
			for (Entity entity : entities) {
				entityID++;
				entity.setID(entityID);
				this.getJdbc().executeUpdate(TagHandlerDB_Queries.INSERT_ENTITY, entity.getName(), entity.getID());

			}
			// add fields
			for (Entity entity : entities) {
				for (TermCloudField field : entity.getFields()) {
					fieldID++;
					field.setID(fieldID);
					this.getJdbc().executeUpdate(TagHandlerDB_Queries.INSERT_FIELD, entity.getID(), field.getFieldType(), field.getID());
				}
			}

			// add terms to fields
			for (Entity entity : entities) {
				for (TermCloudField field : entity.getFields()) {
					// insert term
                    for (Term term : field.getTerms()) {
						Term newTerm = insertTerm(term, false);
						term.setLocalFrequency(newTerm.getLocalFrequency());
						term.setId(newTerm.getId());
					}
					this.addTermsToField(field.getID(), field.getTerms(), false);
				}
			}

			// create term matrix
			for (Entity entity : entities) {
				for (TermCloudField field : entity.getFields()) {
					for (int i = 0; i < field.getTerms().size(); i++) {
						for (int j = i + 1; j < field.getTerms().size(); j++) {
							this.insertCooccurrence(field.getTerms().get(i), field.getTerms().get(j), false);
						}
					}
				}
			}
			this.getJdbc().commitAndCloseConnection();
		} catch (ApiException e) {
			throw new DaoException("insertEntities", e.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 * @param fieldID
	 * @param topSyntagmaticTerms 
	 */
	@Override
	public void insertTopSyntagmaticTerms(TermCloud termCloud) throws DaoException {

		try {
			this.getJdbc().openConnection();
			int i = 0;
			for (Term term : termCloud.getSyntagmaticTerms()) {
				getJdbc().executeUpdate(TagHandlerDB_Queries.INSERT_TOP_SYNTAGMATIC_TERMS, termCloud.getFieldID(), term.getId(), termCloud.getScores().get(i));
				i++;
			}
			this.getJdbc().commitAndCloseConnection();
		} catch (ApiException e) {
			throw new DaoException("insertTopSyntagmaticTerms", e.getCompleteMessage());
		}
	}

	/**
	 * 
	 * @param termMatrixEntries
	 * @return 
	 */
	@Override
	public List<TermMatrixEntry> getTerms(List<TermMatrixEntry> termMatrixEntries) throws DaoException {

		try {
			this.getJdbc().openConnection();
			for (TermMatrixEntry entry : termMatrixEntries) {
				entry.setTerm1(this.getTerm(entry.getTermId1(), false));
				entry.setTerm2(this.getTerm(entry.getTermId2(), false));
				
			}
			this.getJdbc().closeConnection();
		} catch(ApiException e) {
			throw new DaoException("getTerms", e.getCompleteMessage());
		}
		return termMatrixEntries;
	}

	/**
	 * 
	 * @param fieldID
	 * @return 
	 */
	private List<Term> getTerms(int fieldID) throws DaoException {

		List<Term> terms = new ArrayList<Term>();
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_TERMS_BY_FIELD_ID, fieldID);
			for (Map<String, Object> row : result) {
				Term term = new Term();
				term.setId(((Long) row.get(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_TERM_ID)).intValue());
				terms.add(term);
			}
			for (Term term : terms) {
				Term t = this.getTerm(term.getId(), false);
				term.setValue(t.getValue());
				term.setLocalFrequency(t.getLocalFrequency());
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getTerms", e.getCompleteMessage());
		}
		return terms;
	}

	/**
	 * 
	 * @param fieldID
	 * @return
	 * @throws DaoException 
	 */
	private List<Term> getSyntagmaticTerms(int fieldID) throws DaoException {

		List<Term> syntagmaticTerms = new ArrayList<Term>();
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_SYNTAGMATIC_TERMS_BY_FIELD_ID, fieldID);
			for (Map<String, Object> row : result) {
				Term term = new Term();
				term.setId(((Long) row.get(TagHandlerDB_Scheme.FIELD_TOP_SYNTAG_TERM_COLUMN_TERM_ID)).intValue());
				syntagmaticTerms.add(term);
			}
			for (Term term : syntagmaticTerms) {
				Term t = this.getTerm(term.getId(), false);
				term.setValue(t.getValue());
				term.setLocalFrequency(t.getLocalFrequency());
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getSyntagmaticTerms", e.getCompleteMessage());
		}

		
		return syntagmaticTerms;
	}

	/**
	 * Gets the fields of an entity.
	 * @param entityID
	 * @param withTerms 
	 * @param withSyntagmaticTerms 
	 * @return List of fields
	 * @throws DaoException 
	 */
	private List<TermCloudField> getFields(int entityID, boolean withTerms, boolean withSyntagmaticTerms) throws DaoException {

		List<TermCloudField> fields = new ArrayList<TermCloudField>();
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_FIELDS_BY_ENTITY_ID, entityID);
			for (Map<String, Object> row : result) {
				TermCloudField field = new TermCloudField();
				field.setID(((Long) row.get(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_FIELD_ID)).intValue());
				field.setFieldType(((Long) row.get(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_FIELD_TYPE)).intValue());
				fields.add(field);
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getFields", e.getCompleteMessage());
		}

		if (withTerms) {
			for (TermCloudField field : fields) {
				field.setTerms(this.getTerms(field.getID()));
			}
		}

		if (withSyntagmaticTerms) {
			for (TermCloudField field : fields) {
				field.setSyntagmaticTerms(this.getSyntagmaticTerms(field.getID()));
			}
		}

		return fields;
	}

	/**
	 * Gets a list of fields stored in the database containing its id, fieldType
	 * and terms.
	 * @param offset
	 * @param rowCount
	 * @return List of fields
	 * @throws DaoException 
	 */
	@Override
	public List<TermCloudField> getFields(int offset, int rowCount) throws DaoException {

		List<TermCloudField> fields = new ArrayList<TermCloudField>();
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_FIELDS, offset, rowCount);
			for (Map<String, Object> row : result) {
				int fieldID = ((Long)row.get(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_FIELD_ID)).intValue();
				int termID = ((Long)row.get(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_TERM_ID)).intValue();
				int fieldType = ((Long)row.get(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_FIELD_TYPE)).intValue();
				Term term = this.getTerm(termID, false);
				boolean fieldFound = false;
				for (TermCloudField field : fields) {
					if (field.getID() == fieldID) {
						field.addTerm(term);
						fieldFound = true;
						break;
					}
				}
				if (!fieldFound) {
					TermCloudField field = new TermCloudField();
					field.setID(fieldID);
					field.setFieldType(fieldType);
					field.addTerm(term);
					fields.add(field);
				}
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getFields", e.getCompleteMessage());
		}
		return fields;
	}

	/**
	 * Gets a list of entities with its field data.
	 * @param offset
	 * @param rowCount
	 * @return List of entities
	 * @throws DaoException 
	 */
	@Override
	public List<Entity> getEntities(int offset, int rowCount,
		boolean openNewConnection) throws DaoException {
		
		List<Entity> entities = new ArrayList<Entity>();
		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_ENTITIES,
					offset, rowCount);
			if (openNewConnection) {
				this.getJdbc().closeConnection();
			}
			for (Map<String, Object> row : result) {
				Entity entity = new Entity();
				entity.setID(((Long) row.get(TagHandlerDB_Scheme.ENTITY_COLUMN_ENTITY_ID)).intValue());
				entity.setName((String) row.get(TagHandlerDB_Scheme.ENTITY_COLUMN_ENTITY_NAME));
				entities.add(entity);
			}
		} catch (ApiException e) {
			throw new DaoException("getEntities", e.getCompleteMessage());
		}

		for (Entity entity : entities) {
			entity.setFields(this.getFields(entity.getID(), true, true));
		}
		return entities;
	}

	/**
	 * Gets an entity object with only its ID and entityName.
	 * @param entityID
	 * @return Entity
	 * @throws DaoException 
	 */
	@Override
	public Entity getEntity(int entityID) throws DaoException {

		Entity entity = new Entity();
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_ENTITY_BY_ID, entityID);
			this.getJdbc().closeConnection();
			for (Map<String, Object> row : result) {
				entity.setID(((Long) row.get(TagHandlerDB_Scheme.ENTITY_COLUMN_ENTITY_ID)).intValue());
				entity.setName((String) row.get(TagHandlerDB_Scheme.ENTITY_COLUMN_ENTITY_NAME));
			}
		} catch (ApiException e) {
			throw new DaoException("getEntity", e.getCompleteMessage());
		}
		return entity;
	}

	/**
	 * Gets the fields of an entity with the given entityID. This fields only 
	 * contain its ID and field type.
	 * @param entityID
	 * @return List of fields with its IDs and field types.
	 * @throws DaoException 
	 */
	@Override
	public List<TermCloudField> getEntityFields(int entityID) throws DaoException {

		List<TermCloudField> termCloudFields = new ArrayList<TermCloudField>();
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_ENTITY_FIELDS_BY_ENTITY_ID, entityID);
			this.getJdbc().closeConnection();
			for (Map<String, Object> row : result) {
				TermCloudField termCloudField = new TermCloudField();
				termCloudField.setID(((Long) row.get(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_FIELD_ID)).intValue());
				termCloudField.setFieldType(((Long) row.get(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_FIELD_TYPE)).intValue());
				termCloudFields.add(termCloudField);
			}
		} catch (ApiException e) {
			throw new DaoException("getEntityFields", e.getCompleteMessage());
		}
		return termCloudFields;
	}

	/**
	 * Gets the terms of the field with the given fieldId.
	 * @param fieldID
	 * @return List of field terms
	 * @throws DaoException 
	 */
	@Override
	public List<Term> getFieldTerms(int fieldID) throws DaoException {

		List<Term> terms = new ArrayList<Term>();
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_FIELD_TERMS_BY_FIELD_ID, fieldID);
			this.getJdbc().closeConnection();
			for (Map<String, Object> row : result) {
				Term term = new Term();
				term.setId(((Long) row.get(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_TERM_ID)).intValue());
				term.setLocalFrequency(((Long) row.get(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY)).intValue());
				terms.add(term);
			}
		} catch (ApiException e) {
			throw new DaoException("getFieldTerms", e.getCompleteMessage());
		}
		return terms;
	}

	/**
	 * Gets the number of Entities stored in the database.
	 * @return number of entities
	 * @throws DaoException 
	 */
	@Override
	public int getNumberOfEntities() throws DaoException {

		int ret = 0;
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_NUMBER_OF_ENTITIES);
			this.getJdbc().closeConnection();
			for (Map<String, Object> row : result) {
				ret = ((Long) row.get("numberOfEntities")).intValue();
			}
		} catch (ApiException e) {
			throw new DaoException("getNumberOfEntities", e.getCompleteMessage());
		}
		return ret;
	}

	/**
	 * Gets the number of fields stored in the database.
	 * @return number of fields
	 * @throws DaoException 
	 */
	@Override
	public int getNumberOfFields() throws DaoException {

		int ret = 0;
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_NUMBER_OF_FIELDS);
			for (Map<String, Object> row : result) {
				ret = ((Long) row.get("numberOfFields")).intValue();
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getNumberOfFields", e.getCompleteMessage());
		}
		return ret;
	}

	/**
	 * Gets the highest entity ID.
	 * @return ID of the entity with the highest ID.
	 * @throws DaoException 
	 */
	@Override
	public int getMaxEntityID() throws DaoException {

		int ret = 0;
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_MAX_ENTITY_ID);
			for (Map<String, Object> column : result) {
				ret = ((Long) column.get("maxEntityID")).intValue();
			}
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("getMaxEntityID", e.getCompleteMessage());
		}
		return ret;
	}

	/**
	 * Gets the highest ID a field of an entity has.
	 * @return ID of the field with the highest ID.
	 * @throws DaoException 
	 */
	@Override
	public int getMaxFieldID() throws DaoException {

		int ret = 0;
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_MAX_FIELD_ID);
			this.getJdbc().closeConnection();
			for (Map<String, Object> column : result) {
				ret = ((Long) column.get("maxFieldID")).intValue();
			}
		} catch (ApiException e) {
			throw new DaoException("getMaxFieldID", e.getCompleteMessage());
		}
		return ret;
	}
	
	/**
	 * 
	 * @param readEntities
	 * @param computeSyntagmaticRelations
	 * @param acomputeTopRelatedTerms
	 * @param computeTopSyntagmaticTerms
	 * @param a
	 * @param b
	 * @return 
	 */
	@Override
	public int insertNewProcessParameter(Boolean readEntities, Boolean computeSyntagmaticRelations,
		Boolean computeTopRelatedTerms, Boolean computeTopSyntagmaticTerms, Integer maxTopRelatedTerms,
		Float maxPercentageTopTerms, Integer minNbCorrelatedTerms, Integer minTermFrequency,
		Float minSyntagmaticValue, Float syntagmaticEntityTermFactor, Float a, Float b) throws DaoException {
		
		int processID = 0;
		try {
			Date date = new Date();
			String startDate = date.toString();
			this.getJdbc().openConnection();
			this.getJdbc().executeUpdate(TagHandlerDB_Queries.INSERT_NEW_PROCESS_PARAMETER, startDate, readEntities, 
				computeSyntagmaticRelations, computeTopRelatedTerms, computeTopSyntagmaticTerms, maxTopRelatedTerms,
				maxPercentageTopTerms, minNbCorrelatedTerms, minTermFrequency, minSyntagmaticValue, syntagmaticEntityTermFactor, a, b);
			this.getJdbc().commit();
			processID = this.getLastProcessParameterID(false);
			this.getJdbc().closeConnection();
		} catch (ApiException e) {
			throw new DaoException("insertNewProcessParameter", e.getCompleteMessage());
		}
		return processID;
	}
	
	/**
	 * 
	 * @return
	 * @throws DaoException 
	 */
	private int getLastProcessParameterID(boolean openNewConnection) throws DaoException {
		
		int processID = 0;
		try {
			if (openNewConnection) {
				this.getJdbc().openConnection();
			}
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_LAST_PROCESS_PARAMETER_ID);
			if (openNewConnection) {
				this.getJdbc().closeConnection();
			}
			for (Map<String, Object> row : result) {
				processID = ((Long)row.get("maxProcessID")).intValue();
				break;
			}
		} catch(ApiException e) {
			throw new DaoException("getLastProcessParameterID", e.getCompleteMessage());
		}
		return processID;
	}
	
	/**
	 * 
	 * @param processID
	 * @param errorCode 
	 */
	@Override
	public void updateProcessParameter(int processID, int errorCode) throws DaoException {
		
		Date date = new Date();
		String endDate = date.toString();
		try {
			this.getJdbc().openConnection();
			this.getJdbc().executeUpdate(TagHandlerDB_Queries.UPDATE_PROCESS_PARAMETER, endDate, errorCode, processID);
			this.getJdbc().commitAndCloseConnection();
		} catch(ApiException e) {
			throw new DaoException("updateProcessParameter", e.getCompleteMessage());
		}
	}
	
	/**
	 * 
	 * @param fieldID
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public int getNumberOfFieldTerms(int fieldID) throws DaoException {
		
		int numberOfFieldTerms = 0;
		try {
			this.getJdbc().openConnection();
			result = this.getJdbc().executeQuery(TagHandlerDB_Queries.GET_NUMBER_OF_FIELD_TERMS, fieldID);
			this.getJdbc().closeConnection();
			for (Map<String, Object> row : result) {
				numberOfFieldTerms = ((Long)row.get("numberOfFieldTerms")).intValue();
				break;
			}
			return numberOfFieldTerms;
		} catch(ApiException e) {
			throw new DaoException("getNumberOfFieldTerms", e.getCompleteMessage());
		}
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
