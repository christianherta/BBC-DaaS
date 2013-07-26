package de.bbcdaas.visualizer.dao.constants.sql;

import de.bbcdaas.common.dao.constants.sql.SQL_Keywords;

/**
 *
 * @author Robert Illers
 */
public final class ThemeHandlerDB_Queries {
	
	private ThemeHandlerDB_Queries() {}
	
	public static final String INSERT_THEME_CLOUD = new StringBuilder().append(SQL_Keywords.INSERT_INTO).
		append(ThemeHandlerDB_Scheme.TABLE_THEME_CLOUD).append(" (").
		append(ThemeHandlerDB_Scheme.THEME_CLOUD_COLUMN_THEME_CLOUD_NAME).append(", ").
		append(ThemeHandlerDB_Scheme.THEME_CLOUD_COLUMN_THEME_CLOUD_USERID).append(")").
		append(SQL_Keywords.VALUES).append("(?,?)").toString();
	
	public static final String DELETE_THEME_CLOUD = new StringBuilder().append(SQL_Keywords.DELETE_FROM).
		append(ThemeHandlerDB_Scheme.TABLE_THEME_CLOUD).append(SQL_Keywords.WHERE).append(ThemeHandlerDB_Scheme.THEME_CLOUD_COLUMN_THEME_CLOUD_ID).
		append("=?").toString();
	
	public static final String DELETE_THEME_CLOUD_TERMS = new StringBuilder().append(SQL_Keywords.DELETE_FROM).
		append(ThemeHandlerDB_Scheme.TABLE_THEME_CLOUD_TERM).append(SQL_Keywords.WHERE).append(ThemeHandlerDB_Scheme.THEME_CLOUD_TERM_COLUMN_THEME_CLOUD_ID).
		append("=?").toString();
	
	public static final String GET_THEME_CLOUD_ID_BY_NAME = new StringBuilder().append(SQL_Keywords.SELECT).
		append(ThemeHandlerDB_Scheme.THEME_CLOUD_COLUMN_THEME_CLOUD_ID).
		append(SQL_Keywords.FROM).append(ThemeHandlerDB_Scheme.TABLE_THEME_CLOUD).append(SQL_Keywords.WHERE).
		append(ThemeHandlerDB_Scheme.THEME_CLOUD_COLUMN_THEME_CLOUD_NAME).append("=?").toString();
	
	public static final String INSERT_THEME_CLOUD_TERMS = new StringBuilder().append(SQL_Keywords.INSERT_INTO).
		append(ThemeHandlerDB_Scheme.TABLE_THEME_CLOUD_TERM).append("(").append(ThemeHandlerDB_Scheme.THEME_CLOUD_TERM_COLUMN_THEME_CLOUD_ID).append(",").
		append(ThemeHandlerDB_Scheme.THEME_CLOUD_TERM_COLUMN_TERM_ID).append(",").append(ThemeHandlerDB_Scheme.THEME_CLOUD_TERM_COLUMN_RATING).append(",").
		append(ThemeHandlerDB_Scheme.THEME_CLOUD_TERM_COLUMN_WEIGHTING).append(")").
		append(SQL_Keywords.VALUES).append("(?,?,?,?)").toString();
	
	public static final String GET_THEME_CLOUD_WITH_DATA = new StringBuilder().append(SQL_Keywords.SELECT).
		append("tc.").append(ThemeHandlerDB_Scheme.THEME_CLOUD_COLUMN_THEME_CLOUD_ID).
		append(", tc.").append(ThemeHandlerDB_Scheme.THEME_CLOUD_COLUMN_THEME_CLOUD_NAME).
		append(", tc.").append(ThemeHandlerDB_Scheme.THEME_CLOUD_COLUMN_THEME_CLOUD_USERID).
		append(", tct.").append(ThemeHandlerDB_Scheme.THEME_CLOUD_TERM_COLUMN_RATING).
		append(", tct.").append(ThemeHandlerDB_Scheme.THEME_CLOUD_TERM_COLUMN_WEIGHTING).
		append(", tct.").append(ThemeHandlerDB_Scheme.THEME_CLOUD_TERM_COLUMN_TERM_ID).
		append(SQL_Keywords.FROM).
		append(ThemeHandlerDB_Scheme.TABLE_THEME_CLOUD).append(" tc,").
		append(ThemeHandlerDB_Scheme.TABLE_THEME_CLOUD_TERM).append(" tct").
		append(SQL_Keywords.WHERE).
		append("tc.").append(ThemeHandlerDB_Scheme.THEME_CLOUD_COLUMN_THEME_CLOUD_ID).append("=tct.").
		append(ThemeHandlerDB_Scheme.THEME_CLOUD_TERM_COLUMN_THEME_CLOUD_ID).
		append(SQL_Keywords.LIMIT).append("?").append(",").append("?").toString();
}
