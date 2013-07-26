package de.bbcdaas.visualizer.constants;
/**
 *
 * @author Robert Illers
 */
public final class VisualizerConstants {
	
	private VisualizerConstants() {};
	
	// scope keys
	public static final String KEY_MENU_CONFIGURATION ="menuConfiguration";
	public static final String KEY_SEARCH_TYPE = "searchType";
	public static final String KEY_SEARCH_STRING = "searchString";
	public static final String KEY_USER = "user";
	public static final String KEY_SESSION_CONTAINER = "sessionContainer";
	public static final String KEY_WEBSITE_URL = "websiteURL";
	
	/* control forwards */
	
	// common
	public final static String FORWARD_SHOW_STARTPAGE_CONTROL = "start.do";
        public final static String FORWARD_SHOW_LOGIN_FORM_CONTROL = "showLoginForm.do";
	public final static String FORWARD_HANDLE_LOGIN_FORM_CONTROL = "handleLoginForm.do";
	public final static String FORWARD_LOGOUT_USER_CONTROL = "logoutUser.do";
	public final static String FORWARD_USER_MANAGEMENT_CONTROL = "userManagement.do";
	public final static String FORWARD_HANDLE_USER_MANAGEMENT_FORM_CONTROL = "handleUserManagementForm.do";
	
	// processing
	public final static String FORWARD_SHOW_PROCESSING_PAGE_CONTROL = "processing.do";
	public final static String FORWARD_START_PROCESSING_CONTROL = "startProcessing.do";
	
	// search
	public final static String FORWARD_SHOW_SEARCH_PAGE_CONTROL = "search.do";
	public final static String FORWARD_SHOW_HELP_PAGE_CONTROL = "help.do";
	public final static String FORWARD_PERFORM_SEARCH_CONTROL = "performSearch.do";
	
	/* /control forwards */
	
	/* jsp forwards */
	
	// common
	public final static String FORWARD_STARTPAGE_JSP = "page.startpage";
	public final static String FORWARD_USER_MANAGEMENT_JSP = "page.userManagement";
	public final static String AJAX_USER_ADD_N_DELETE_JSP = "page.userAddNDelete";
	public final static String FORWARD_LOGIN_JSP = "page.login";
	public final static String FORWARD_LOGIN_FAILURE_JSP = "page.loginFailure";
	public final static String FORWARD_LOGOUT_SUCCESS_JSP = "page.logoutSuccess";
	public final static String AJAX_SHOW_PROCESS_STATE_JSP = "page.processState";
	public final static String FORWARD_SHOW_HELP_JSP = "page.help";
	
	// processing
	public final static String FORWARD_PROCESSING_JSP = "page.processing";
	
	// search
	public final static String FORWARD_SEARCH_JSP = "page.search";
	public final static String AJAX_SHOW_SEARCH_RESULT_JSP = "page.searchResult";
	
	/* /jsp forwards */
	
	/* parameter */
	public static final String PARAM_ENABLE_PROCESSING = "enableProcessing";
	public static final String PARAM_ENABLE_STATISTICS = "enableStatistics";
	public static final String PARAM_ENABLE_SEARCH = "enableSearch";
	public static final String PARAM_ENABLE_EVALUATION = "enableEvaluation";
	public static final String PARAM_ENABLE_SYNTAG_CLOUDS = "enableSyntagClouds";
	public static final String PARAM_ENABLE_THEME_CLOUDS = "enableThemeClouds";
	public static final String PARAM_ENABLE_FILE_OUTPUT = "enableFileOutput";
	/* /parameter */
	
	/* process names */
	
	public static final String PROCESSNAME_PROCESSING = "processing";
	public static final String PROCESSNAME_STATISTICS_WRITER = "statisticsWriter";
	
	/* /process names */
	
	/* search types */
	
	public static final String SEARCH_TYPE_TERM_SUGGESTION = "termSuggestion";
	public static final String SEARCH_TYPE_TOP_RELATED_TERMS = "topRelatedTerms";
	
	/* /search types */
	
	// max font size for tagCloud
	public static final int TAGCLOUD_MAX_FONTSIZE = 3;
	
	/* roles for users */
	
	public static final int ROLE_TESTER = 0;
	public static final int ROLE_ADMIN = 1;
	
	/* roles for users */
}
