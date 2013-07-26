package de.bbcdaas.taghandler.dao.constants.sql;

import de.bbcdaas.common.dao.constants.sql.SQL_Keywords;

/**
 * Contains all queries used in the taghandlerDao of the tagHandler.
 * @author Robert Illers
 */
public final class TagHandlerDB_Queries {
	
	private TagHandlerDB_Queries() {}

	public static final String GET_TOP_SYNTAGMATIC_TERMS_SYNTAG = new StringBuilder().append(SQL_Keywords.SELECT).
		append(TagHandlerDB_Scheme.FIELD_TOP_SYNTAG_TERM_COLUMN_SCORE).append(SQL_Keywords.FROM).
		append(TagHandlerDB_Scheme.TABLE_FIELD_TOP_SYNTAG_TERM).append(SQL_Keywords.WHERE).append(TagHandlerDB_Scheme.FIELD_TOP_SYNTAG_TERM_COLUMN_FIELD_ID).
		append("=?").append(SQL_Keywords.AND).append(TagHandlerDB_Scheme.FIELD_TOP_SYNTAG_TERM_COLUMN_TERM_ID).append("=?").toString();
	
	public static final String GET_COOCCURRENCE = new StringBuilder().append(SQL_Keywords.SELECT).
		append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_COOC).append(SQL_Keywords.FROM).
		append(TagHandlerDB_Scheme.TABLE_TERM_MATRIX).append(SQL_Keywords.WHERE).
		append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID1).append("=?").append(SQL_Keywords.AND).
		append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID2).append("=?").toString();
	
	public static final String GET_MAX_TERM_ID = new StringBuilder().append(SQL_Keywords.SELECT).
		append("MAX(").append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID).append(")").append(SQL_Keywords.AS).append("maxTermID").
		append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_TERM).toString();
	
	public static final String GET_MIN_TERM_ID = new StringBuilder().append(SQL_Keywords.SELECT).
		append("MIN(").append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID).append(")").append(SQL_Keywords.AS).append("minTermID").
		append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_TERM).toString();
	
	public static final String INCREASE_TERM_FREQUENCY = new StringBuilder().append(SQL_Keywords.UPDATE).
		append(TagHandlerDB_Scheme.TABLE_TERM).append(SQL_Keywords.SET).
		append(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY).append("=").
		append(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY).append("+1").append(SQL_Keywords.WHERE).
		append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID).append("=?").toString();
	
	public static final String INSERT_NEW_TERM = new StringBuilder().append(SQL_Keywords.INSERT_INTO).
		append(TagHandlerDB_Scheme.TABLE_TERM).append(" (").
        append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID).append(",").
            append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_VALUE).append(",").
		append(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY).append(")").
		append(SQL_Keywords.VALUES).append("(?, ?, 1)").toString();
	
	public static final String INSERT_NEW_TERM_MATRIX_ENTRY = new StringBuilder().append(SQL_Keywords.INSERT_INTO).
		append(TagHandlerDB_Scheme.TABLE_TERM_MATRIX).append(" (").append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID1).
		append(", ").append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID2).append(", ").
		append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_COOC).append(" )").append(SQL_Keywords.VALUES).
		append("(?, ?, 1)").toString();
	
	public static final String INCREASE_COOCCURRENCE = new StringBuilder().append(SQL_Keywords.UPDATE).
		append(TagHandlerDB_Scheme.TABLE_TERM_MATRIX).append(SQL_Keywords.SET).
		append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_COOC).append("=").
		append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_COOC).append("+1").
		append(SQL_Keywords.WHERE).append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID1).
		append("=?").append(SQL_Keywords.AND).append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID2).append("=?").toString();
	
	public static final String MATRIX_ENTRY_EXISTS = new StringBuilder().append(SQL_Keywords.SELECT).
		append("1").append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_TERM_MATRIX).
		append(SQL_Keywords.WHERE).append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID1).
		append("=?").append(SQL_Keywords.AND).append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID2).
		append("=?").toString();
	
	public static final String GET_NUMBER_OF_TERM_MATRIX_ENTRIES = new StringBuilder().append(SQL_Keywords.SELECT).
		append("COUNT(*)").append(SQL_Keywords.AS).append("numberOfTermMatrixEntries").append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_TERM_MATRIX).toString();
	
	public static final String GET_TERM_BY_ID = new StringBuilder().append(SQL_Keywords.SELECT).
		append(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY).append(",").
                append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_VALUE).
		append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_TERM).append(SQL_Keywords.WHERE).
		append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID).append("=?").toString();
	
	public static final String GET_NUMBER_OF_TERMS = new StringBuilder().append(SQL_Keywords.SELECT).
		append("COUNT(*)").append(SQL_Keywords.AS).append("numberOfTerms").append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_TERM).toString();
	
	public static final String GET_NUMBER_OF_TOP_RELATED_TERMS = new StringBuilder().
		append(SQL_Keywords.SELECT).append("COUNT(*)").append(SQL_Keywords.AS).append("numberOfTopRelatedTerms").append(SQL_Keywords.FROM).
		append(TagHandlerDB_Scheme.TABLE_TOP_RELATED_TERM).append(SQL_Keywords.WHERE).
		append(TagHandlerDB_Scheme.TOP_RELATED_TERM_COLUMN_TERM_ID).append("=?").toString();
	
	public static final String INSERT_TOP_RELATED_TERMS = new StringBuilder().append(SQL_Keywords.INSERT_INTO).
		append(TagHandlerDB_Scheme.TABLE_TOP_RELATED_TERM).append("(").append(TagHandlerDB_Scheme.TOP_RELATED_TERM_COLUMN_TERM_ID).append(",").
		append(TagHandlerDB_Scheme.TOP_RELATED_TERM_COLUMN_TOP_TERM_ID).append(",").append(TagHandlerDB_Scheme.TOP_RELATED_TERM_COLUMN_SYNTAG).
		append(")").append(SQL_Keywords.VALUES).append("(?,?,?)").toString();
	
	public static final String GET_RELATED_TERM_MATRIX_ENTRIES = new StringBuilder().append(SQL_Keywords.SELECT).append("*").append(SQL_Keywords.FROM).
		append(TagHandlerDB_Scheme.TABLE_TERM_MATRIX).append(SQL_Keywords.WHERE).append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID1).append("=?").
		append(SQL_Keywords.OR).append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID2).append("=?").
		append(SQL_Keywords.ORDER_BY).append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_SYNTAG).append(SQL_Keywords.DESC).toString();
	
	public static final String GET_ENTITY_ID = new StringBuilder().append(SQL_Keywords.SELECT).
		append(TagHandlerDB_Scheme.ENTITY_COLUMN_ENTITY_ID).append(SQL_Keywords.FROM).
		append(TagHandlerDB_Scheme.TABLE_ENTITY).append(SQL_Keywords.WHERE).
		append(TagHandlerDB_Scheme.ENTITY_COLUMN_ENTITY_NAME).append("=?").toString();
	
	public static final String ADD_TERM_TO_FIELD = new StringBuilder().append(SQL_Keywords.INSERT_INTO).append(TagHandlerDB_Scheme.TABLE_FIELD_TERM).
		append(" (").append(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_FIELD_ID).append(",").
		append(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_TERM_ID).append(")").append(SQL_Keywords.VALUES).append("(?,?)").toString();
	
	public static final String INSERT_ENTITY = new StringBuilder().append(SQL_Keywords.INSERT_INTO).
			append(TagHandlerDB_Scheme.TABLE_ENTITY).append(" (").append(TagHandlerDB_Scheme.ENTITY_COLUMN_ENTITY_NAME).append(",").
			append(TagHandlerDB_Scheme.ENTITY_COLUMN_ENTITY_ID).append(")").
			append(SQL_Keywords.VALUES).append("(?,?)").toString();
	
	public static final String INSERT_FIELD = new StringBuilder().append(SQL_Keywords.INSERT_INTO).
		append(TagHandlerDB_Scheme.TABLE_ENTITY_FIELD).append(" (").append(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_ENTITY_ID).
		append(",").append(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_FIELD_TYPE).append(",").append(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_FIELD_ID).append(")").
		append(SQL_Keywords.VALUES).append("(?,?,?)").toString();
	
	public static final String INSERT_TOP_SYNTAGMATIC_TERMS = new StringBuilder().append(SQL_Keywords.INSERT_INTO).
		append(TagHandlerDB_Scheme.TABLE_FIELD_TOP_SYNTAG_TERM).append("(").
		append(TagHandlerDB_Scheme.FIELD_TOP_SYNTAG_TERM_COLUMN_FIELD_ID).append(",").
		append(TagHandlerDB_Scheme.FIELD_TOP_SYNTAG_TERM_COLUMN_TERM_ID).append(",").
		append(TagHandlerDB_Scheme.FIELD_TOP_SYNTAG_TERM_COLUMN_SCORE).append(")").append(SQL_Keywords.VALUES).
		append("(?,?,?)").toString();
	
	public static final String GET_TERMS_BY_FIELD_ID = new StringBuilder().append(SQL_Keywords.SELECT).
		append(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_TERM_ID).append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_FIELD_TERM).
		append(SQL_Keywords.WHERE).append(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_FIELD_ID).append("=?").toString();
	
	public static final String GET_SYNTAGMATIC_TERMS_BY_FIELD_ID = new StringBuilder().append(SQL_Keywords.SELECT).
		append(TagHandlerDB_Scheme.FIELD_TOP_SYNTAG_TERM_COLUMN_TERM_ID).append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_FIELD_TOP_SYNTAG_TERM).
		append(SQL_Keywords.WHERE).append(TagHandlerDB_Scheme.FIELD_TOP_SYNTAG_TERM_COLUMN_FIELD_ID).append("=?").toString();
	
	public static final String GET_TERMS = new StringBuilder().append(SQL_Keywords.SELECT).append("*").append(SQL_Keywords.FROM).
		append(TagHandlerDB_Scheme.TABLE_TERM).toString();
	
	public static final String GET_FIELDS_BY_ENTITY_ID = new StringBuilder().append(SQL_Keywords.SELECT).append(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_FIELD_ID).
		append(",").append(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_FIELD_TYPE).append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_ENTITY_FIELD).
		append(SQL_Keywords.WHERE).append(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_ENTITY_ID).append("=?").toString();
	
	public static final String GET_FIELDS = new StringBuilder().append(SQL_Keywords.SELECT).
		append("ft.").append(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_FIELD_ID).
		append(",ft.").append(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_TERM_ID).
		append(",ef.").append(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_FIELD_ID).
		append(",ef.").append(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_FIELD_TYPE).
		append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_FIELD_TERM).append(" ft, ").append(TagHandlerDB_Scheme.TABLE_ENTITY_FIELD).append(" ef").
		append(SQL_Keywords.WHERE).
		append("ft.").append(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_TERM_ID).append("=ef.").append(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_FIELD_ID).
		append(SQL_Keywords.LIMIT).append("?").append(",").append("?").toString();
	
	public static final String GET_ENTITIES = new StringBuilder().append(SQL_Keywords.SELECT).
		append("*").append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_ENTITY).
		append(SQL_Keywords.LIMIT).append("?").append(",").append("?").toString();
	
	public static final String GET_ENTITY_BY_ID = new StringBuilder().append(SQL_Keywords.SELECT).append("*").
		append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_ENTITY).append(SQL_Keywords.WHERE).
		append(TagHandlerDB_Scheme.ENTITY_COLUMN_ENTITY_ID).append("=?").toString();
	
	public static final String GET_ENTITY_FIELDS_BY_ENTITY_ID = new StringBuilder().append(SQL_Keywords.SELECT).append("*").
		append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_ENTITY_FIELD).append(SQL_Keywords.WHERE).
		append(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_ENTITY_ID).append("=?").toString();
	
	public static final String GET_NUMBER_OF_ENTITIES = new StringBuilder().append(SQL_Keywords.SELECT).
		append("COUNT(*)").append(SQL_Keywords.AS).append("numberOfEntities").append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_ENTITY).toString();
	
	public static final String GET_NUMBER_OF_FIELDS = new StringBuilder().append(SQL_Keywords.SELECT).
		append("COUNT(*)").append(SQL_Keywords.AS).append("numberOfFields").append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_ENTITY_FIELD).toString();
	
	public static final String GET_FIELD_TERMS_BY_FIELD_ID = new StringBuilder().append(SQL_Keywords.SELECT).
		append("ft.").append(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_TERM_ID).
		append(",t.").append(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY).
		append(SQL_Keywords.FROM).
		append(TagHandlerDB_Scheme.TABLE_FIELD_TERM).append(" ft,").append(TagHandlerDB_Scheme.TABLE_TERM).append(" t").
		append(SQL_Keywords.WHERE).
		append("ft.").append(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_FIELD_ID).append("=?").append(SQL_Keywords.AND).
		append("ft.").append(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_TERM_ID).append("=t.").append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID).toString();
	
	public static final String GET_MAX_ENTITY_ID = new StringBuilder().append(SQL_Keywords.SELECT).
		append("MAX(").append(TagHandlerDB_Scheme.ENTITY_COLUMN_ENTITY_ID).append(")").append(SQL_Keywords.AS).append("maxEntityID").
		append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_ENTITY).toString();
	
	public static final String GET_MAX_FIELD_ID = new StringBuilder().append(SQL_Keywords.SELECT).
		append("MAX(").append(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_FIELD_ID).append(")").append(SQL_Keywords.AS).append("maxFieldID").
		append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_ENTITY_FIELD).toString();
	
	public static final String SET_SYNTAGMATIC_RELATION = new StringBuilder().
		append(SQL_Keywords.UPDATE).append(TagHandlerDB_Scheme.TABLE_TERM_MATRIX).append(SQL_Keywords.SET).
		append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_SYNTAG).append("=?").
		append(SQL_Keywords.WHERE).append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID1).
		append("=?").append(SQL_Keywords.AND).append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID2).append("=?").toString();
	
	public static final String CLEAR_SYNTAGMATIC_RELATIONS = new StringBuilder().append(SQL_Keywords.UPDATE).
		append(TagHandlerDB_Scheme.TABLE_TERM_MATRIX).append(SQL_Keywords.SET).append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_SYNTAG).append("=0").toString();
	
	public static final String CLEAR_SYNTAGMATIC_RELATIONS1 = new StringBuilder().append(SQL_Keywords.ALTER_TABLE).
		append(TagHandlerDB_Scheme.TABLE_TERM_MATRIX).append(SQL_Keywords.DROP_COLUMN).append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_SYNTAG).toString();
	
	public static final String CLEAR_SYNTAGMATIC_RELATIONS2 = new StringBuilder().append(SQL_Keywords.ALTER_TABLE).
		append(TagHandlerDB_Scheme.TABLE_TERM_MATRIX).append(SQL_Keywords.ADD_COLUMN).append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_SYNTAG).append(" float ").toString();
	
	public static final String CLEAR_TOP_SYNTAGMATIC_TERMS = new StringBuilder().append(SQL_Keywords.TRUNCATE_TABLE).
		append(TagHandlerDB_Scheme.TABLE_FIELD_TOP_SYNTAG_TERM).toString();
	
	public static final String GET_ALL_MATRIX_ENTRIES = new StringBuilder().
		append(SQL_Keywords.SELECT).append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID1).append(",").
		append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID2).append(",").
		append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_COOC).append(",").
		append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_SYNTAG).
		append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_TERM_MATRIX).toString();
	
	public static final String GET_ALL_MATRIX_ENTRIES_WITH_TERM_DATA = new StringBuilder().
		append(SQL_Keywords.SELECT).append("t1.").append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID).append(SQL_Keywords.AS).append("termID1,t2.").
		append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID).append(SQL_Keywords.AS).append("termID2,t1.").
		append(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY).append(SQL_Keywords.AS).append("termFrequency1,t2.").
		append(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY).append(SQL_Keywords.AS).append("termFrequency2,tm.").
		append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_COOC).append(",tm.").append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_SYNTAG).
		append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_TERM_MATRIX).append(" tm,").append(TagHandlerDB_Scheme.TABLE_TERM).
		append(" t1,").append(TagHandlerDB_Scheme.TABLE_TERM).append(" t2").append(SQL_Keywords.WHERE).
		append("t1.").append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID).append("=tm.").
		append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID1).append(SQL_Keywords.AND).
		append("t2.").append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID).
		append("=tm.").append(TagHandlerDB_Scheme.TERM_MATRIX_COLUMN_TERM_ID2).toString();
	
	public static final String GET_ENTITIES_BY_FIELD_TERMS_APPEND_WHERE = new StringBuilder().append(SQL_Keywords.SELECT).
		append(SQL_Keywords.DISTINCT).append("e.").append(TagHandlerDB_Scheme.ENTITY_COLUMN_ENTITY_ID).append(",e.").append(TagHandlerDB_Scheme.ENTITY_COLUMN_ENTITY_NAME).append(SQL_Keywords.FROM).
		append(TagHandlerDB_Scheme.TABLE_ENTITY).append(" e,").append(TagHandlerDB_Scheme.TABLE_ENTITY_FIELD).append(" ef,").
		append(TagHandlerDB_Scheme.TABLE_FIELD_TERM).append(" ft").append(SQL_Keywords.WHERE).
		append("e.").append(TagHandlerDB_Scheme.ENTITY_COLUMN_ENTITY_ID).append("=ef.").append(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_ENTITY_ID).
		append(SQL_Keywords.AND).append("ef.").append(TagHandlerDB_Scheme.ENTITY_FIELD_COLUMN_FIELD_ID).
		append("=ft.").append(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_FIELD_ID).toString();
	
	public static final String GET_TOP_RELATED_TERMS_BY_ID_WITH_MIN_SYNTAG = new StringBuilder().
		append(SQL_Keywords.SELECT).
		append("t1.").append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID).append(",t1.").
		append(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY).append(",t1.").
		append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_VALUE).append(",trt.").
		append(TagHandlerDB_Scheme.TOP_RELATED_TERM_COLUMN_SYNTAG).append(SQL_Keywords.FROM).
		append(TagHandlerDB_Scheme.TABLE_TERM).append(" t1,").
		append(TagHandlerDB_Scheme.TABLE_TOP_RELATED_TERM).append(" trt").
		append(SQL_Keywords.WHERE).
		append("t1.").append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID).append("=trt.").
		append(TagHandlerDB_Scheme.TOP_RELATED_TERM_COLUMN_TOP_TERM_ID).append(SQL_Keywords.AND).
		append("trt.").append(TagHandlerDB_Scheme.TOP_RELATED_TERM_COLUMN_TERM_ID).append("=?").append(SQL_Keywords.AND).
		append("trt.").append(TagHandlerDB_Scheme.TOP_RELATED_TERM_COLUMN_SYNTAG).append(">=?").toString();
	
	public static final String GET_TOP_RELATED_TERMS = new StringBuilder().append(SQL_Keywords.SELECT).
		append("t1.").append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID).append(SQL_Keywords.AS).append("termID1,t1.").
		append(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY).append(SQL_Keywords.AS).append("termFrequency1,t2.").
		append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID).append(SQL_Keywords.AS).append("termID2,t2.").
		append(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY).append(SQL_Keywords.AS).append("termFrequency2").
		append(SQL_Keywords.FROM).
		append(TagHandlerDB_Scheme.TABLE_TERM).append(" t1,").
		append(TagHandlerDB_Scheme.TABLE_TERM).append(" t2,").
		append(TagHandlerDB_Scheme.TABLE_TOP_RELATED_TERM).append(" trt").
		append(SQL_Keywords.WHERE).
		append("t1.").append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID).append("=trt.").
		append(TagHandlerDB_Scheme.TOP_RELATED_TERM_COLUMN_TOP_TERM_ID).append(SQL_Keywords.AND).
		append("trt.").append(TagHandlerDB_Scheme.TOP_RELATED_TERM_COLUMN_SYNTAG).append(">=?").append(SQL_Keywords.AND).	
		append("t2.").append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID).append("=trt.").append(TagHandlerDB_Scheme.TOP_RELATED_TERM_COLUMN_TERM_ID).
		append(SQL_Keywords.LIMIT).append("?,?").toString();
	
	public static final String INSERT_NEW_PROCESS_PARAMETER = new StringBuilder().append(SQL_Keywords.INSERT_INTO).
		append(TagHandlerDB_Scheme.TABLE_PROCESS).append("(").
		append(TagHandlerDB_Scheme.PROCESS_COLUMN_START_DATE).append(",").
		append(TagHandlerDB_Scheme.PROCESS_COLUMN_READ_ENTITIES).append(",").
		append(TagHandlerDB_Scheme.PROCESS_COLUMN_COMPUTE_SYNTAGMATIC_RELATIONS).append(",").
		append(TagHandlerDB_Scheme.PROCESS_COLUMN_COMPUTE_TOP_RELATED_TERMS).append(",").
		append(TagHandlerDB_Scheme.PROCESS_COLUMN_COMPUTE_TOP_SYNTAGMATIC_TERMS).append(",").
		append(TagHandlerDB_Scheme.PROCESS_COLUMN_MAX_TOP_RELATED_TERMS).append(",").
		append(TagHandlerDB_Scheme.PROCESS_COLUMN_MAX_PERCENTAGE_TOP_TERMS).append(",").
		append(TagHandlerDB_Scheme.PROCESS_COLUMN_MIN_NB_CORRELATED_TERMS).append(",").
		append(TagHandlerDB_Scheme.PROCESS_COLUMN_MIN_TERM_FREQUENCY).append(",").
		append(TagHandlerDB_Scheme.PROCESS_COLUMN_MIN_SYNTAGMATIC_VALUE).append(",").
		append(TagHandlerDB_Scheme.PROCESS_COLUMN_SYNTAGMATIC_ENTITY_TERM_FACTOR).append(",").
		append(TagHandlerDB_Scheme.PROCESS_COLUMN_A).append(",").
		append(TagHandlerDB_Scheme.PROCESS_COLUMN_B).append(")").
		append(SQL_Keywords.VALUES).append("(?,?,?,?,?,?,?,?,?,?,?,?,?)").
		toString();
	
	public static final String GET_LAST_PROCESS_PARAMETER_ID = new StringBuilder().append(SQL_Keywords.SELECT).
		append("MAX(").append(TagHandlerDB_Scheme.PROCESS_COLUMN_PROCESS_ID).append(")").append(SQL_Keywords.AS).append("maxProcessID").
		append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_PROCESS).toString();
	
	public static final String UPDATE_PROCESS_PARAMETER = new StringBuilder().append(SQL_Keywords.UPDATE).
		append(TagHandlerDB_Scheme.TABLE_PROCESS).append(SQL_Keywords.SET).
		append(TagHandlerDB_Scheme.PROCESS_COLUMN_END_DATE).append("=?").append(",").
		append(TagHandlerDB_Scheme.PROCESS_COLUMN_ERROR_CODE).append("=?").
		append(SQL_Keywords.WHERE).append(TagHandlerDB_Scheme.PROCESS_COLUMN_PROCESS_ID).append("=?").toString();
	
	public static final String GET_TERMS_WITH_MIN_FREQUENCY = new StringBuilder().append(SQL_Keywords.SELECT).
		append(TagHandlerDB_Scheme.TERM_COLUMN_TERM_ID).append(",").
		append(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY).
		append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_TERM).
		append(SQL_Keywords.WHERE).
		append(TagHandlerDB_Scheme.TERM_COLUMN_LOCAL_FREQUENCY).append(">=?").
		append(SQL_Keywords.LIMIT).append("?").append(",").append("?").toString();
	
	public static final String GET_NUMBER_OF_FIELD_TERMS = new StringBuilder().append(SQL_Keywords.SELECT).
		append("count(*)").append(SQL_Keywords.AS).append("numberOfFieldTerms").
		append(SQL_Keywords.FROM).append(TagHandlerDB_Scheme.TABLE_FIELD_TERM).
		append(SQL_Keywords.WHERE).append(TagHandlerDB_Scheme.FIELD_TERM_COLUMN_FIELD_ID).append("=?").toString();
}
