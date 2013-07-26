package de.bbcdaas.visualizer.dao.constants.sql;

import de.bbcdaas.common.dao.constants.sql.SQL_Keywords;

/**
 *
 * @author Robert Illers
 */
public final class EvaluationDB_Queries {
	
	private EvaluationDB_Queries() {}
	
	public static final String GET_MAX_RANDOM_TERMS_GROUP_ID = new StringBuilder().append(SQL_Keywords.SELECT).
		append("MAX(").append(EvaluationDB_Scheme.RANDOM_TERMS_COLUMN_RTG_ID).append(")").append(SQL_Keywords.AS).append("maxRtgID").
		append(SQL_Keywords.FROM).append(EvaluationDB_Scheme.TABLE_RANDOM_TERMS).toString();
	
	public static final String INSERT_RANDOM_TERM = new StringBuilder().append(SQL_Keywords.INSERT_INTO).
		append(EvaluationDB_Scheme.TABLE_RANDOM_TERMS).append(" (").append(EvaluationDB_Scheme.RANDOM_TERMS_COLUMN_RTG_ID).append(",").
		append(EvaluationDB_Scheme.RANDOM_TERMS_COLUMN_RANDOM_TERM_ID).append(")").
		append(SQL_Keywords.VALUES).append("(?,?)").toString();
	
	public static final String INSERT_RANDOM_TERMS_GROUP = new StringBuilder().append(SQL_Keywords.INSERT_INTO).
		append(EvaluationDB_Scheme.TABLE_RANDOM_TERM_GROUPS).append(" (").
		append(EvaluationDB_Scheme.RANDOM_TERM_GROUPS_COLUMN_RTG_ID).append(",").
		append(EvaluationDB_Scheme.RANDOM_TERM_GROUPS_COLUMN_GROUP_LABEL).append(",").
		append(EvaluationDB_Scheme.RANDOM_TERM_GROUPS_COLUMN_MIN_SYNTAG).append(")").
		append(SQL_Keywords.VALUES).append("(?,?,?)").toString();
	
	public static final String GET_RANDOM_TERMS_GROUP_BY_ID = new StringBuilder().append(SQL_Keywords.SELECT).
		append(EvaluationDB_Scheme.RANDOM_TERMS_COLUMN_RANDOM_TERM_ID).
		append(SQL_Keywords.FROM).append(EvaluationDB_Scheme.TABLE_RANDOM_TERMS).append(SQL_Keywords.WHERE).
		append(EvaluationDB_Scheme.RANDOM_TERMS_COLUMN_RTG_ID).append("=?").toString();
	
	public static final String GET_ALL_RANDOM_TERMS = new StringBuilder().append(SQL_Keywords.SELECT).
		append(EvaluationDB_Scheme.RANDOM_TERMS_COLUMN_RTG_ID).append(",").
		append(EvaluationDB_Scheme.RANDOM_TERMS_COLUMN_RANDOM_TERM_ID).append(SQL_Keywords.FROM).
		append(EvaluationDB_Scheme.TABLE_RANDOM_TERMS).toString();
	
	public static final String GET_ALL_RANDOM_TERM_GROUPS = new StringBuilder().append(SQL_Keywords.SELECT).
		append(EvaluationDB_Scheme.RANDOM_TERM_GROUPS_COLUMN_RTG_ID).append(",").
		append(EvaluationDB_Scheme.RANDOM_TERM_GROUPS_COLUMN_GROUP_LABEL).append(",").
		append(EvaluationDB_Scheme.RANDOM_TERM_GROUPS_COLUMN_MIN_SYNTAG).append(SQL_Keywords.FROM).
		append(EvaluationDB_Scheme.TABLE_RANDOM_TERM_GROUPS).toString();
	
	public static final String INSERT_SAMPLE_RATING = new StringBuilder().append(SQL_Keywords.INSERT_INTO).
		append(EvaluationDB_Scheme.TABLE_SAMPLE).append(" (").
		append(EvaluationDB_Scheme.SAMPLE_COLUMN_RTG_ID).append(",").
		append(EvaluationDB_Scheme.SAMPLE_COLUMN_USER_ID).append(",").
		append(EvaluationDB_Scheme.SAMPLE_COLUMN_RANDOM_TERM_ID).append(",").
		append(EvaluationDB_Scheme.SAMPLE_COLUMN_RATED_TERM_ID).append(",").
		append(EvaluationDB_Scheme.SAMPLE_COLUMN_RATING).append(",").
		append(EvaluationDB_Scheme.SAMPLE_COLUMN_ADDED).append(")").
		append(SQL_Keywords.VALUES).append("(?,?,?,?,?,?)").toString();
	
	public static final String GET_SAMPLE_BY_RTG_ID_AND_RANDOM_TERM_ID_AND_USER_ID = new StringBuilder().append(SQL_Keywords.SELECT).
		append(EvaluationDB_Scheme.SAMPLE_COLUMN_RTG_ID).append(",").
		append(EvaluationDB_Scheme.SAMPLE_COLUMN_USER_ID).append(",").
		append(EvaluationDB_Scheme.SAMPLE_COLUMN_RANDOM_TERM_ID).append(",").
		append(EvaluationDB_Scheme.SAMPLE_COLUMN_RATED_TERM_ID).append(",").
		append(EvaluationDB_Scheme.SAMPLE_COLUMN_RATING).append(",").
		append(EvaluationDB_Scheme.SAMPLE_COLUMN_ADDED).append(SQL_Keywords.FROM).
		append(EvaluationDB_Scheme.TABLE_SAMPLE).append(SQL_Keywords.WHERE).
		append(EvaluationDB_Scheme.SAMPLE_COLUMN_RTG_ID).append("=?").append(SQL_Keywords.AND).append(EvaluationDB_Scheme.SAMPLE_COLUMN_USER_ID).
		append("=?").append(SQL_Keywords.AND).append(EvaluationDB_Scheme.SAMPLE_COLUMN_RANDOM_TERM_ID).append("=?").toString();
	
	public static final String REMOVE_RANDOM_TERMS_BY_RTG_ID = new StringBuilder().append(SQL_Keywords.DELETE_FROM).
		append(EvaluationDB_Scheme.TABLE_RANDOM_TERMS).append(SQL_Keywords.WHERE).
		append(EvaluationDB_Scheme.RANDOM_TERMS_COLUMN_RTG_ID).append("=?").toString();
	
	public static final String REMOVE_RANDOM_TERM_GROUP_BY_ID = new StringBuilder().append(SQL_Keywords.DELETE_FROM).
		append(EvaluationDB_Scheme.TABLE_RANDOM_TERM_GROUPS).append(SQL_Keywords.WHERE).
		append(EvaluationDB_Scheme.RANDOM_TERM_GROUPS_COLUMN_RTG_ID).append("=?").toString();
		
	public static final String REMOVE_RANDOM_TERM_BY_RTG_ID_AND_TERM_ID = new StringBuilder().append(SQL_Keywords.DELETE_FROM).
		append(EvaluationDB_Scheme.TABLE_RANDOM_TERMS).append(SQL_Keywords.WHERE).
		append(EvaluationDB_Scheme.RANDOM_TERMS_COLUMN_RTG_ID).append("=?").append(SQL_Keywords.AND).
		append(EvaluationDB_Scheme.RANDOM_TERMS_COLUMN_RANDOM_TERM_ID).append("=?").toString();
}
