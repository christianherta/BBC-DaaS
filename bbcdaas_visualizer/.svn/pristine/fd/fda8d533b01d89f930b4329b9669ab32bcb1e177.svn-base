package de.bbcdaas.visualizer.dao.constants.sql;

import de.bbcdaas.common.dao.constants.sql.SQL_Keywords;

/**
 *
 * @author Robert Illers
 */
public final class VisualizerDB_Queries {
	
	private VisualizerDB_Queries() {}
	
	public static final String INSERT_USER = new StringBuilder().append(SQL_Keywords.INSERT_INTO).append(VisualizerDB_Scheme.TABLE_USER).
		append(" (").append(VisualizerDB_Scheme.USER_COLUMN_USER_NAME).append(",").
		append(VisualizerDB_Scheme.USER_COLUMN_ROLE).append(",").
		append(VisualizerDB_Scheme.USER_COLUMN_PASSWORD).append(")").
		append(SQL_Keywords.VALUES).append("(?,?,?)").toString();
	
	public static final String DELETE_USER_BY_ID = new StringBuilder().append(SQL_Keywords.DELETE_FROM).append(VisualizerDB_Scheme.TABLE_USER).
		append(SQL_Keywords.WHERE).append(VisualizerDB_Scheme.USER_COLUMN_USER_ID).append("=?").toString();
	
	public static final String GET_USER_BY_ID = new StringBuilder().append(SQL_Keywords.SELECT).append(VisualizerDB_Scheme.USER_COLUMN_USER_NAME).
		append(",").append(VisualizerDB_Scheme.USER_COLUMN_ROLE).append(SQL_Keywords.FROM).append(VisualizerDB_Scheme.TABLE_USER).
		append(SQL_Keywords.WHERE).append(VisualizerDB_Scheme.USER_COLUMN_USER_ID).append("=?").toString();
	
	public static final String GET_USER_BY_NAME = new StringBuilder().append(SQL_Keywords.SELECT).
		append(VisualizerDB_Scheme.USER_COLUMN_USER_ID).append(",").
		append(VisualizerDB_Scheme.USER_COLUMN_ROLE).append(SQL_Keywords.FROM).append(VisualizerDB_Scheme.TABLE_USER).
		append(SQL_Keywords.WHERE).append(VisualizerDB_Scheme.USER_COLUMN_USER_NAME).append("=?").toString();
	
	public static final String GET_ALL_USER = new StringBuilder().append(SQL_Keywords.SELECT).
		append(VisualizerDB_Scheme.USER_COLUMN_USER_ID).append(",").
		append(VisualizerDB_Scheme.USER_COLUMN_USER_NAME).append(",").
		append(VisualizerDB_Scheme.USER_COLUMN_ROLE).append(SQL_Keywords.FROM).append(VisualizerDB_Scheme.TABLE_USER).toString();
	
	public static final String GET_USER_BY_NAME_AND_PASSWORD = new StringBuilder().append(SQL_Keywords.SELECT).append(VisualizerDB_Scheme.USER_COLUMN_USER_ID).
		append(",").append(VisualizerDB_Scheme.USER_COLUMN_ROLE).append(SQL_Keywords.FROM).append(VisualizerDB_Scheme.TABLE_USER).
		append(SQL_Keywords.WHERE).append(VisualizerDB_Scheme.USER_COLUMN_USER_NAME).append("=?").append(SQL_Keywords.AND).append(VisualizerDB_Scheme.USER_COLUMN_PASSWORD).
		append("=?").toString();
}
