package de.bbcdaas.disambiguationweb.control;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.disambiguationweb.business.DisambiguationBusiness;
import de.bbcdaas.disambiguationweb.constants.Constants;
import de.bbcdaas.webservices.api.disambiguation.beans.CategoryContext;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller class for all disambiguation controller.
 * @author Robert Illers
 */
@Controller
public class DisambiguationController {

	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Shows the startpage.
	 * @return forward to the startpage jsp
	 */
	@RequestMapping("/"+Constants.FORWARD_STARTPAGE_CONTROLLER)
    public ModelAndView showStartPage() {
        return new ModelAndView(Constants.FORWARD_STARTPAGE_JSP);
    }

    /**
    * Shows the wikipedia disabiguation page.
    * @return forward to the wikipedia disambiguation page
    */
    @RequestMapping("/"+Constants.FORWARD_WIKIPEDIA_CONTROLLER)
    public ModelAndView showWikipediaPage(HttpServletRequest request) {

        // empty the list of terms for disambiguation
		List<String> disambiguationTermValues = new ArrayList<String>();
		DisambiguationBusiness business = new DisambiguationBusiness();
		business.getWikipediaScoringConfiguration(request);
		disambiguationTermValues.add("");
		disambiguationTermValues.add("");
		request.getSession().setAttribute("disambiguationTermValues", disambiguationTermValues);
        return new ModelAndView(Constants.FORWARD_WIKIPEDIA_JSP);
    }

	/**
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("/"+Constants.FORWARD_CATEGORY_CONTEXT_CONTROLLER)
    public ModelAndView showCategoryContextPage(HttpServletRequest request) {
		request.getSession().setAttribute(Constants.KEY_WIKIPEDIA_CATEGORY_CONTEXT, new CategoryContext());
		return new ModelAndView(Constants.FORWARD_CATEGORY_CONTEXT_JSP);
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("/"+Constants.FORWARD_WIKIPEDIA_HELP_CONTROLLER)
    public ModelAndView showWikipediaHelpPage(HttpServletRequest request) {
		return new ModelAndView(Constants.FORWARD_WIKIPEDIA_HELP_JSP);
	}

	/**
    * Adds a new input field to the disambiguation input.
    * @return forward to the disambiguation input sub page (ajax)
    */
    @RequestMapping("/"+Constants.FORWARD_ADD_DISAMBIGUATION_TERM_CONTROLLER)
    public ModelAndView addDisambiguationTerm(HttpServletRequest request) {

		try {
			List<String> disambiguationTermValues = new DisambiguationBusiness().
                getTermsParameter(request);
			disambiguationTermValues.add("");
			request.getSession().setAttribute("disambiguationTermValues", disambiguationTermValues);
		} catch (UnsupportedEncodingException ex) {
			logger.error(ex);
		}
		return new ModelAndView(Constants.AJAX_DISAMBIGUATION_INPUTS_JSP);
    }

	/**
    * Removes a input field from the disambiguation input at a given index.
    * @return forward to the disambiguation input sub page (ajax)
    */
    @RequestMapping("/"+Constants.FORWARD_REMOVE_DISAMBIGUATION_TERM_CONTROLLER)
    public ModelAndView removeDisambiguationTerm(HttpServletRequest request) {

		try {

			List<String> disambiguationTermValues = new DisambiguationBusiness().
                getTermsParameter(request);
			String indexString = request.getParameter(Constants.PARAM_INDEX);
			if (indexString != null) {
				disambiguationTermValues.remove(Integer.parseInt(indexString));
				request.getSession().setAttribute("disambiguationTermValues",
                    disambiguationTermValues);
			}
		} catch (UnsupportedEncodingException ex) {
			logger.error(ex);
		}
		return new ModelAndView(Constants.AJAX_DISAMBIGUATION_INPUTS_JSP);
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("/"+Constants.FORWARD_GET_CATEGORY_CONTEXT)
	public ModelAndView getCategoryContext(HttpServletRequest request) {

		CategoryContext categoryContext = new DisambiguationBusiness().getCategoryContext(request);

		request.getSession().setAttribute(Constants.KEY_WIKIPEDIA_CATEGORY_CONTEXT, categoryContext);
		return new ModelAndView(Constants.AJAX_WIKIPEDIA_GET_CATEGORY_CONTEXT_RESULT_JSP);
	}

    /**
     * Starts the disambiguation of the given terms using the lucene index via
     * webservice.
     * @param request
     * @return forward to the disambiguation result page (ajax)
     */
    @RequestMapping("/"+Constants.FORWARD_WIKIPEDIA_LUCENE_DISAMBIGUATION_CONTROLLER)
    public ModelAndView wikipediaLuceneDisambiguation(HttpServletRequest request) {

        new DisambiguationBusiness().wikipediaLuceneDisambiguation(request);
        return new ModelAndView(Constants.AJAX_WIKIPEDIA_DISAMBIGUATION_RESULT_JSP);
    }

    /**
     * Searches for terms in the configured tagandler termlexicon via webservice
     * who starts with letters of the given searchString.
     * @param request
     * @return forward to the suggestion result page (ajax)
     */
    @RequestMapping("/"+Constants.FORWARD_SUGGEST_TERMS_CONTROLLER)
    public ModelAndView suggestTerms(HttpServletRequest request) {

        String searchString = request.getParameter(Constants.PARAM_SEARCH_STRING);
        String selectionTarget = request.getParameter(Constants.PARAM_SELECTION_TARGET);

        if (searchString != null) {
            searchString = searchString.trim();
            try {
				searchString = URLDecoder.decode(searchString, "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				logger.error(ex.getMessage());
			}

            searchString = searchString.toLowerCase();
            DisambiguationBusiness business = new DisambiguationBusiness();
            business.cleanSession(request);
            List<Term> result = business.suggestTerms(searchString);
            request.getSession().setAttribute(Constants.KEY_SUGGESTION_LIST, result);
            request.getSession().setAttribute(Constants.KEY_SELECTION_TARGET, selectionTarget);
        }
        return new ModelAndView(Constants.AJAX_SUGGESTION_RESULT_JSP);
    }

	/**
	 * Searches for wikipedia article URIs in a lucene index of a
     * wikipedia xml dump by using a webservice that matches the searchString
     * with the title of the wikipedia articles.
     * @param request
	 * @return List of URIs
	 */
	@RequestMapping("/"+Constants.FORWARD_SEARCH_FOR_WIKIPEDIA_URIS_CONTROLLER)
    public ModelAndView searchForWikipediaURIs(HttpServletRequest request) {

		String searchString = request.getParameter(Constants.PARAM_SEARCH_STRING);

        if (searchString != null) {
            searchString = searchString.trim();
            try {
				searchString = URLDecoder.decode(searchString, "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				logger.error(ex.getMessage());
			}

            DisambiguationBusiness business = new DisambiguationBusiness();
            business.cleanSession(request);

            request.getSession().setAttribute(Constants.KEY_WIKIPEDIA_URIS,
				business.searchForWikipediaURIs(searchString));
        }
        return new ModelAndView(Constants.AJAX_WIKIPEDIA_URL_SEARCH_RESULT_JSP);
    }
}
