package de.bbcdaas.taghandler.dao.constants.sql;

import de.bbcdaas.common.dao.constants.sql.SQL_Keywords;

/**
 *
 * @author Robert Illers
 */
public class TermLexicon_Queries {
	
	public static final String CLEAR_TERMS = new StringBuilder().append(SQL_Keywords.TRUNCATE_TABLE).
		append(TermLexicon_Scheme.TABLE_TERM).toString();
	
	public static final String CLEAR_TERM_BLACKLIST = new StringBuilder().append(SQL_Keywords.TRUNCATE_TABLE).
		append(TermLexicon_Scheme.TABLE_TERM_BLACKLIST).toString();
	
	public static final String INSERT_NEW_TERM = new StringBuilder().append(SQL_Keywords.INSERT_INTO).
		append(TermLexicon_Scheme.TABLE_TERM).append(" (").
		append(TermLexicon_Scheme.TERM_COLUMN_VALUE).append(", ").
		append(TermLexicon_Scheme.TERM_COLUMN_TERM_FREQUENCY).append(")").
		append(SQL_Keywords.VALUES).append("(?, 1)").toString();
	
	public static final String INCREASE_TERM_FREQUENCY = new StringBuilder().append(SQL_Keywords.UPDATE).
		append(TermLexicon_Scheme.TABLE_TERM).append(SQL_Keywords.SET).
		append(TermLexicon_Scheme.TERM_COLUMN_TERM_FREQUENCY).append("=").
		append(TermLexicon_Scheme.TERM_COLUMN_TERM_FREQUENCY).append("+1").append(SQL_Keywords.WHERE).
		append(TermLexicon_Scheme.TERM_COLUMN_TERM_ID).append("=?").toString();
	
	public static final String GET_TERM_BY_VALUE = new StringBuilder().append(SQL_Keywords.SELECT).
		append(TermLexicon_Scheme.TERM_COLUMN_TERM_ID).append(",").append(TermLexicon_Scheme.TERM_COLUMN_TERM_FREQUENCY).
		append(SQL_Keywords.FROM).append(TermLexicon_Scheme.TABLE_TERM).append(SQL_Keywords.WHERE).
		append(TermLexicon_Scheme.TERM_COLUMN_VALUE).append("=?").toString();
	
	public static final String GET_TERM_BY_ID = new StringBuilder().append(SQL_Keywords.SELECT).
		append(TermLexicon_Scheme.TERM_COLUMN_VALUE).append(",").append(TermLexicon_Scheme.TERM_COLUMN_TERM_FREQUENCY).
		append(SQL_Keywords.FROM).append(TermLexicon_Scheme.TABLE_TERM).append(SQL_Keywords.WHERE).
		append(TermLexicon_Scheme.TERM_COLUMN_TERM_ID).append("=?").toString();
	
	public static final String GET_TERMS = new StringBuilder().append(SQL_Keywords.SELECT).append("*").append(SQL_Keywords.FROM).
		append(TermLexicon_Scheme.TABLE_TERM).toString();
	
	public static final String GET_TERM_BY_VALUE_FRAGMENT = new StringBuilder().append(SQL_Keywords.SELECT).
		append(TermLexicon_Scheme.TERM_COLUMN_VALUE).append(",").append(TermLexicon_Scheme.TERM_COLUMN_TERM_ID).append(SQL_Keywords.FROM).
		append(TermLexicon_Scheme.TABLE_TERM).append(SQL_Keywords.WHERE).
		append(TermLexicon_Scheme.TERM_COLUMN_VALUE).append(SQL_Keywords.LIKE).append("?").
		append(SQL_Keywords.GROUP_BY).
		append(TermLexicon_Scheme.TERM_COLUMN_TERM_ID).toString();
	
	public static final String REMOVE_TERM_FROM_BLACKLIST = new StringBuilder().append(SQL_Keywords.DELETE_FROM).
		append(TermLexicon_Scheme.TABLE_TERM_BLACKLIST).append(SQL_Keywords.WHERE).append(TermLexicon_Scheme.TERM_BLACKLIST_COLUMN_TERM_ID).
		append("=?").toString();
	
	public static final String ADD_TERM_INTO_BLACKLIST = new StringBuilder().append(SQL_Keywords.INSERT_INTO).
		append(TermLexicon_Scheme.TABLE_TERM_BLACKLIST).append(" (").
		append(TermLexicon_Scheme.TERM_BLACKLIST_COLUMN_TERM_ID).append(")").
		append(SQL_Keywords.VALUES).append("(?)").toString();
	
	public static final String GET_TERM_BLACKLIST = new StringBuilder().append(SQL_Keywords.SELECT).
		append(TermLexicon_Scheme.TERM_BLACKLIST_COLUMN_TERM_ID).append(SQL_Keywords.FROM).
		append(TermLexicon_Scheme.TABLE_TERM_BLACKLIST).append(SQL_Keywords.LIMIT).append("?").append(",").append("?").toString();
	
	public static final String GET_MAX_TERM_ID = new StringBuilder().append(SQL_Keywords.SELECT).
		append("MAX(").append(TermLexicon_Scheme.TERM_COLUMN_TERM_ID).append(")").append(SQL_Keywords.AS).append("maxTermID").
		append(SQL_Keywords.FROM).append(TermLexicon_Scheme.TABLE_TERM).toString();
	
	public static final String GET_MIN_TERM_ID = new StringBuilder().append(SQL_Keywords.SELECT).
		append("MIN(").append(TermLexicon_Scheme.TERM_COLUMN_TERM_ID).append(")").append(SQL_Keywords.AS).append("minTermID").
		append(SQL_Keywords.FROM).append(TermLexicon_Scheme.TABLE_TERM).toString();
	
	public static final String GET_NUMBER_OF_TERMS = new StringBuilder().append(SQL_Keywords.SELECT).
		append("COUNT(*)").append(SQL_Keywords.AS).append("numberOfTerms").append(SQL_Keywords.FROM).append(TermLexicon_Scheme.TABLE_TERM).toString();
}
