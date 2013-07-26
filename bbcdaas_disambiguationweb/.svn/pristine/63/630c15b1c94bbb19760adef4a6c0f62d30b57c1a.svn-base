package de.bbcdaas.disambiguationweb.business;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.util.FileReader;
import de.bbcdaas.disambiguationweb.beans.membership.WikipediaScoringParams;
import de.bbcdaas.disambiguationweb.constants.Constants;
import de.bbcdaas.webservices.api.disambiguation.DisambiguationWebserviceAPI;
import de.bbcdaas.webservices.api.disambiguation.beans.CategoryContext;
import de.bbcdaas.webservices.api.disambiguation.beans.ScoredDocument;
import de.bbcdaas.webservices.api.disambiguation.beans.WikipediaResult;
import de.bbcdaas.webservices.api.taghandler.TagHandlerWebserviceAPI;
import de.bbcdaas.webservices.api.taghandler.beans.TermsResult;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

/**
 * Business logic for the DisambiguationWeb application.
 * @author Robert Illers
 */
public final class DisambiguationBusiness {

    private Logger logger = Logger.getLogger(this.getClass());

    /**
        * Gets a term suggestion via webservice for the string the user inserts
        * in a term input field.
        * @param searchString
        * @return a list of suggested term
        */
    public List<Term> suggestTerms(String searchString) {

        TagHandlerWebserviceAPI webservices = new TagHandlerWebserviceAPI("http://bbcdaas.f4.htw-berlin.de:8080/webservices/services/");
        TermsResult result = webservices.searchForTerms(searchString);
        if (result != null) {
            return  result.getTerms();
        }
        return new ArrayList<Term>();
    }

    /**
        * Searches for wikipedia article URIs in a lucene index of a
        * wikipedia xml dump by using a webservice that matches the searchString
        * with the title of the wikipedia articles.
        * @param searchString
        * @return List of URIs
        */
    public List<ScoredDocument> searchForWikipediaURIs(String searchString) {

		DisambiguationWebserviceAPI webservices = new DisambiguationWebserviceAPI("http://bbcdaas.f4.htw-berlin.de:8080/webservices/services/");
        WikipediaResult result = webservices.searchForWikipediaURIs(searchString);
		if (result != null) {
			logger.debug("result.getScoredDocuments().size(): "+result.getScoredDocuments().size());
			return result.getScoredDocuments();
		}
		logger.debug("result empty");
        return new ArrayList<ScoredDocument>();
    }

	/**
	 *
	 * @param categoryName
	 * @return
	 */
	public CategoryContext getCategoryContext(HttpServletRequest request) {

		String categoryName = request.getParameter("categoryName");
		DisambiguationWebserviceAPI webservices = new DisambiguationWebserviceAPI("http://bbcdaas.f4.htw-berlin.de:8080/webservices/services/");
		CategoryContext categoryContext = webservices.getCategoryContext(categoryName);
		if (categoryContext == null) {
			logger.error("No result from webservice.");
		} else {
			logger.info("categoryContext.getCategoryName(): "+categoryContext.getCategoryName());
			logger.info("categoryContext.getCategoryParentNames(): "+categoryContext.getCategoryParentNames().toString());
			logger.info("categoryContext.getCategoryDirectChildNames(): "+categoryContext.getCategoryDirectChildNames().toString());
		}
		return categoryContext;
	}

    /**
    * Starts the disambiguation of the given terms using the lucene index via
    * webservice.
    * @param request
    */
    public void wikipediaLuceneDisambiguation(HttpServletRequest request) {

        try {
            List<String> searchStrings = this.getTermsParameter(request);
            DisambiguationWebserviceAPI webservices = new DisambiguationWebserviceAPI("http://bbcdaas.f4.htw-berlin.de:8080/webservices/services/");
			Integer maxTermDocuments = null;
			String maxTermDocuments_String = request.getParameter("maxTermDocuments");
			Integer maxTermDocumentsPerPattern = null;
			String maxTermDocumentsPerPattern_String = request.getParameter("maxTermDocumentsPerPattern");
			Float multimatchingDocumentsRatingAddend = null;
			String multimatchingDocumentsRatingAddend_String = request.getParameter("multimatchingDocumentsRatingAddend");
			Float alternativeURIRating = null;
			String alternativeURIRating_String = request.getParameter("alternativeURIRating");
			String patternRatings = request.getParameter("patternRatings");
			String scorerWeightings = request.getParameter("scorerWeightings");
			String candidateFinder = request.getParameter("candidateFinder");
			if (maxTermDocuments_String != null && !maxTermDocuments_String.trim().isEmpty()) {
				maxTermDocuments = Integer.parseInt(maxTermDocuments_String.trim());
			}
			if (maxTermDocumentsPerPattern_String != null && !maxTermDocumentsPerPattern_String.trim().isEmpty()) {
				maxTermDocumentsPerPattern = Integer.parseInt(maxTermDocumentsPerPattern_String.trim());
			}
			if (multimatchingDocumentsRatingAddend_String != null && !multimatchingDocumentsRatingAddend_String.trim().isEmpty()) {
				multimatchingDocumentsRatingAddend = Float.parseFloat(multimatchingDocumentsRatingAddend_String.trim());
			}
			if (alternativeURIRating_String != null && !alternativeURIRating_String.trim().isEmpty()) {
				alternativeURIRating = Float.parseFloat(alternativeURIRating_String.trim());
			}
            WikipediaResult result = webservices.scoreWikipediaDocs(searchStrings,
				maxTermDocuments, maxTermDocumentsPerPattern,
				multimatchingDocumentsRatingAddend, patternRatings,
				alternativeURIRating, scorerWeightings, candidateFinder);
			request.getSession().removeAttribute(Constants.KEY_SCORED_WIKIPEDIA_URIS_SORTED_BY_TERMVALUE);
			request.getSession().removeAttribute(Constants.KEY_SCORED_WIKIPEDIA_URIS_SORTED_BY_SCORE);
			request.getSession().removeAttribute(Constants.KEY_WIKIPEDIA_SCORING_PATTERN);
			request.getSession().removeAttribute(Constants.KEY_WIKIPEDIA_SCORING_PARAMS);

			if (result != null) {

				request.getSession().setAttribute(Constants.KEY_SCORED_WIKIPEDIA_URIS_SORTED_BY_TERMVALUE, result.getScoredDocuments());
				List<ScoredDocument> scoredDocuments = new ArrayList<ScoredDocument>();
			    for (ScoredDocument scoredDocument : result.getScoredDocuments()) {
					if (!scoredDocuments.contains(scoredDocument)) {
						scoredDocuments.add(scoredDocument);
					}
				}
				Collections.sort(scoredDocuments);
				request.getSession().setAttribute(Constants.KEY_SCORED_WIKIPEDIA_URIS_SORTED_BY_SCORE, scoredDocuments);
				request.getSession().setAttribute(Constants.KEY_WIKIPEDIA_SCORING_PATTERN, result.getPattern());
				WikipediaScoringParams params = new WikipediaScoringParams();
				params.setMaxTermDocuments(result.getMaxTermDocuments());
				params.setMaxTermDocumentsPerPattern(result.getMaxTermDocumentsPerPattern());
				params.setMultimatchingDocumentsRatingAddend(result.getMultimatchingDocumentsRatingAddend());
				params.setAlternativeURIRating(result.getAlternativeURIRating());
				params.setDocumentScorerNames(result.getDocumentScorerNames());
				params.setDocumentScorerWeightings(result.getDocumentScorerWeightings());
				request.getSession().setAttribute(Constants.KEY_WIKIPEDIA_SCORING_PARAMS, params);
			} else {
				logger.info("wikipediaLuceneDisambiguation(): No result.");
			}
        } catch (UnsupportedEncodingException ex) {
			logger.error(ex);
		}
    }

	/**
	 *
	 * @param request
	 */
	public void getWikipediaScoringConfiguration(HttpServletRequest request) {

		DisambiguationWebserviceAPI webservices = new DisambiguationWebserviceAPI("http://bbcdaas.f4.htw-berlin.de:8080/webservices/services/");
		WikipediaResult result;
		result = webservices.getScoringConfiguration();
		WikipediaScoringParams params = new WikipediaScoringParams();

		request.getSession().removeAttribute(Constants.KEY_WIKIPEDIA_SCORING_PARAMS);
		request.getSession().removeAttribute(Constants.KEY_WIKIPEDIA_SCORING_PATTERN);

		if (result != null) {

			params.setMaxTermDocuments(result.getMaxTermDocuments());
			params.setMaxTermDocumentsPerPattern(result.getMaxTermDocumentsPerPattern());
			params.setMultimatchingDocumentsRatingAddend(result.getMultimatchingDocumentsRatingAddend());
			params.setAlternativeURIRating(result.getAlternativeURIRating());
            params.setCandidateFinderNames(result.getCandidateFinderNames());
			params.setDocumentScorerNames(result.getDocumentScorerNames());
			params.setDocumentScorerWeightings(result.getDocumentScorerWeightings());
			request.getSession().setAttribute(Constants.KEY_WIKIPEDIA_SCORING_PARAMS, params);
			request.getSession().setAttribute(Constants.KEY_WIKIPEDIA_SCORING_PATTERN, result.getPattern());
		}
	}

    /**
	 * Helper Method to get terms from the request.
	 * @param request
	 * @return List of terms
	 */
	public List<String> getTermsParameter(HttpServletRequest request) throws UnsupportedEncodingException {

		String terms = URLDecoder.decode(request.getParameter("terms"), "UTF-8");
		List<String> disambiguationTermValues = new ArrayList<String>();
		String[] splittedTerms = terms.split(",");
		for (String splittedTerm : splittedTerms) {
			splittedTerm = splittedTerm.trim();
			splittedTerm = splittedTerm.toLowerCase();
			disambiguationTermValues.add(splittedTerm);
		}
		return disambiguationTermValues;
	}

    /**
     * Cleans the session from some attributes.
     * @param request
     */
    public void cleanSession(HttpServletRequest request) {

        request.getSession().removeAttribute("wikipediaURIs");
        request.getSession().removeAttribute("suggestionList");
        request.getSession().removeAttribute("selectionTarget");
    }

	/**
	 *
	 * @return
	 */
	private Configuration getConfiguration() {

		Configuration config = null;
		FileReader fileReader = new FileReader();
		URL url = this.getClass().getResource(new StringBuilder("/properties").
			append(File.separator).append(Constants.CONFIGURATION_FILE_NAME).toString());
		if (url != null) {
			String path = url.toString();
			if (path.startsWith("file:")) {
				path = path.substring(5);
			}
			config = fileReader.readPropertiesConfig(path, 
				FileReader.FILE_OPENING_TYPE.ABSOLUTE, FileReader.FILE_OPENING_TYPE.RELATIVE, true);
		}
		return config;
	}
}
