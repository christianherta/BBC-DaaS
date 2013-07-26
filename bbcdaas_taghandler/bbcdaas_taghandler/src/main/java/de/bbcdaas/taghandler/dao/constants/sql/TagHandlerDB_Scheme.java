package de.bbcdaas.taghandler.dao.constants.sql;

/**
 * Contains the names of the tales and columns of the term db.
 * @author Robert Illers
 */
public final class TagHandlerDB_Scheme {
	
	private TagHandlerDB_Scheme() {}
	
	// table entity
	public static final String TABLE_ENTITY = "entity";
	// columns
	public static final String ENTITY_COLUMN_ENTITY_ID = "entityId";
	public static final String ENTITY_COLUMN_ENTITY_NAME = "entityName";
	
	// table entity_field
	public static final String TABLE_ENTITY_FIELD = "entity_field";
	// columns
	public static final String ENTITY_FIELD_COLUMN_FIELD_ID = "fieldId";
	public static final String ENTITY_FIELD_COLUMN_FIELD_TYPE = "fieldType";
	public static final String ENTITY_FIELD_COLUMN_ENTITY_ID = "entityId";
	
	// table field_term
	public static final String TABLE_FIELD_TERM = "field_term";
	// columns
	public static final String FIELD_TERM_COLUMN_FIELD_ID = "fieldId";
	public static final String FIELD_TERM_COLUMN_TERM_ID = "termId";
	
	// table field_top_syntag_term
	public static final String TABLE_FIELD_TOP_SYNTAG_TERM = "field_top_syntag_term";
	// columns
	public static final String FIELD_TOP_SYNTAG_TERM_COLUMN_FIELD_ID = "fieldId";
	public static final String FIELD_TOP_SYNTAG_TERM_COLUMN_TERM_ID = "termId";
	public static final String FIELD_TOP_SYNTAG_TERM_COLUMN_SCORE = "score";
	
	// table term
	public static final String TABLE_TERM = "term";
	//columns
	public static final String TERM_COLUMN_TERM_ID = "termId";
    public static final String TERM_COLUMN_TERM_VALUE = "termValue";
	public static final String TERM_COLUMN_LOCAL_FREQUENCY = "termFrequency";

	// table term_matrix
	public static final String TABLE_TERM_MATRIX = "term_matrix";
	// columns
	public static final String TERM_MATRIX_COLUMN_TERM_ID1 = "termId1";
	public static final String TERM_MATRIX_COLUMN_TERM_ID2 = "termId2";
	public static final String TERM_MATRIX_COLUMN_COOC = "cooc";
	public static final String TERM_MATRIX_COLUMN_SYNTAG = "syntag";
	
	// table top_related_term
	public static final String TABLE_TOP_RELATED_TERM = "top_related_term";
	// columns
	public static final String TOP_RELATED_TERM_COLUMN_TERM_ID = "termId";
	public static final String TOP_RELATED_TERM_COLUMN_TOP_TERM_ID = "topTermId";
	public static final String TOP_RELATED_TERM_COLUMN_SYNTAG = "syntag";
	
	// table process
	public static final String TABLE_PROCESS = "process";
	// columns
	public static final String PROCESS_COLUMN_PROCESS_ID = "processId";
	public static final String PROCESS_COLUMN_START_DATE = "startDate";
	public static final String PROCESS_COLUMN_END_DATE = "endDate";
	public static final String PROCESS_COLUMN_ERROR_CODE = "errorCode";
	public static final String PROCESS_COLUMN_READ_ENTITIES = "readEntities";
	public static final String PROCESS_COLUMN_COMPUTE_SYNTAGMATIC_RELATIONS = "computeSyntagmaticRelations";
	public static final String PROCESS_COLUMN_COMPUTE_TOP_RELATED_TERMS = "computeTopRelatedTerms";
	public static final String PROCESS_COLUMN_COMPUTE_TOP_SYNTAGMATIC_TERMS = "computeTopSyntagmaticTerms";
	public static final String PROCESS_COLUMN_MAX_TOP_RELATED_TERMS = "maxTopRelatedTerms";
	public static final String PROCESS_COLUMN_MAX_PERCENTAGE_TOP_TERMS = "maxPercentageTopTerms";
	public static final String PROCESS_COLUMN_MIN_NB_CORRELATED_TERMS = "minNbCorrelatedTerms";
	public static final String PROCESS_COLUMN_MIN_TERM_FREQUENCY = "minTermFrequency";
	public static final String PROCESS_COLUMN_MIN_SYNTAGMATIC_VALUE = "minSyntagmaticValue";
	public static final String PROCESS_COLUMN_SYNTAGMATIC_ENTITY_TERM_FACTOR = "syntagmaticEntityTermFactor";
	public static final String PROCESS_COLUMN_A = "a";
	public static final String PROCESS_COLUMN_B = "b";
}
