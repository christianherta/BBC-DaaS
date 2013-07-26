package de.bbcdaas.visualizer.dao.constants.sql;

/**
 *
 * @author Robert Illers
 */
public final class ThemeHandlerDB_Scheme {
	
	private ThemeHandlerDB_Scheme() {}
	
	// table themecloud
	public static final String TABLE_THEME_CLOUD = "themecloud";
	// columns
	public static final String THEME_CLOUD_COLUMN_THEME_CLOUD_ID = "themeCloudId";
	public static final String THEME_CLOUD_COLUMN_THEME_CLOUD_NAME = "themeCloudName";
	public static final String THEME_CLOUD_COLUMN_THEME_CLOUD_USERID = "userId";
	
	// table themecloud_term
	public static final String TABLE_THEME_CLOUD_TERM = "themecloud_term";
	//columns
	public static final String THEME_CLOUD_TERM_COLUMN_THEME_CLOUD_ID = "themeCloudId";
	public static final String THEME_CLOUD_TERM_COLUMN_TERM_ID = "termId";
	public static final String THEME_CLOUD_TERM_COLUMN_RATING = "rating";
	public static final String THEME_CLOUD_TERM_COLUMN_WEIGHTING = "weighting";
}
