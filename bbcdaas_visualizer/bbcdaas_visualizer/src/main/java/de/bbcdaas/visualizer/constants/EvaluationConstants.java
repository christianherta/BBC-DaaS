package de.bbcdaas.visualizer.constants;

/**
 *
 * @author Robert Illers
 */
public final class EvaluationConstants {
	
	private EvaluationConstants() {}
	
	/* default evaluation parameter */
	
	public static final int DEFAULT_MIN_NUMBER_OF_TOP_RELATED_TERMS = 5;
	public static final int DEFAULT_MAX_RANDOM_TRIES = 300;
	public static final int DEFAULT_NUMBER_OF_RANDOM_TERMS = 5;
	
	/* /default evaluation parameter */
	
	/* jsp forwards */
	
	public final static String FORWARD_EVALUATION_ADMIN_JSP = "page.evaluation_admin";
	public final static String FORWARD_EVALUATION_TESTER_JSP = "page.evaluation_tester";
	public final static String AJAX_SHOW_RANDOM_TERM_GROUPS_JSP = "page.randomTermGroups";
	public final static String AJAX_SHOW_RANDOM_TERMS_JSP = "page.randomTerms";
	public final static String AJAX_SHOW_RATED_TERMS_JSP = "page.ratedTerms";
	
	/* /jsp forwards */
	
	/* control forwards */
	
	public final static String FORWARD_SHOW_EVALUATION_PAGE_CONTROL = "evaluation.do";
	public final static String FORWARD_HANDLE_EVALUATION_FORM_CONTROL = "handleEvaluationForm.do";
	
	/* /control forwards */
}
