package de.bbcdaas.disambiguation.wikipedia.engine.lucene.candidates;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.beans.document.Field;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.disambiguation.core.impl.AbstractDocumentCandidateFinder;
import de.bbcdaas.disambiguation.core.interfaces.DocumentCandidateFinder;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiConstants;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiDisambiguationEngineConfig;
import de.bbcdaas.disambiguation.wikipedia.dataimport.lucene.WikiContentHandler;
import java.util.*;
import java.util.Map.Entry;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.WildcardQuery;

/**
 * Implementation of the WikiLuceneCandidateFinder interface, the candidates are
 * found by its title fields using rated title pattern
 * @author Robert Illers
 */
public final class TitlePatternCandidateFinder extends AbstractDocumentCandidateFinder <WikiDisambiguationEngineConfig>
	implements DocumentCandidateFinder {

	/**
	 * Constructor
	 * @param config
	 */
	public TitlePatternCandidateFinder(WikiDisambiguationEngineConfig config) {
		super(config);
	}

	/**
	 * Searches for documents where the title matches with a list of rated pattern related to a list of termValues
	 * @param luceneAPI api with opened connection to the index for accessing data
	 * @param surfaceForms list of term values from the users input
	 * @param allRatedQueryPattern a number of lists containing title pattern sorted by its rating
	 * @param maxTermDocuments max number of documents that should be found by using given pattern
	 * @param maxTermDocumentsPerPattern max number of docuemnts that should be found for a specific pattern
	 * @param alternativeURIRating the value that should be set to a document if its linked in an disambiguation page
	 * @return found documents sorted by termValue
	 * @throws ApiException
	 */
	@Override
	public Map<String, List<Document>> findCandidateDocumentsImpl(List<String> surfaceForms,
		Map<Float, List<String>> allRatedQueryPattern, int maxTermDocuments,
		int maxTermDocumentsPerPattern, float alternativeURIRating) throws ApiException {

		// result map
		Map<String, List<Document>> completeResult = new HashMap<String, List<Document>>();

		// get the documents depending on rated pattern
		for (String surfaceForm : surfaceForms) {

			List<Document> termResult = this.findSurfaceFormDocuments(surfaceForm,
				allRatedQueryPattern, maxTermDocuments, maxTermDocumentsPerPattern);
			completeResult.put(surfaceForm, termResult);
		}

		// categorize all found documents
		this.getDocumentCategorizer().categorizeDocuments(completeResult);

		// documents found by analyzing disambiguations and redirects
		Map<String, List<Document>> allNewFoundDocuments = new HashMap<String, List<Document>>();

		// pattern used for getting one specific term matching with exact the surfaceForm
		final List<String> singleTermPattern = new ArrayList<String>();
		singleTermPattern.add(WikiConstants.PATTERN_SINGLE_TERM);

		// look for disambiguations, collect alternative titles and set alternativeURIRating
		Map<String, Set<String>> allAlternativeTitles = this.
			handleDisambiguationDocuments(completeResult, alternativeURIRating);

		// construct rated pattern parameter for getting alternative documents
		Map<Float, List<String>> patternMap = new HashMap<Float, List<String>>();
		patternMap.put(alternativeURIRating, singleTermPattern);

		// get the alternative documents
		for (Entry<String, Set<String>> alternativeTitles : allAlternativeTitles.entrySet()) {

			for (String alternativeTitle : alternativeTitles.getValue()) {

				List<Document> documents = this.findSurfaceFormDocuments(alternativeTitle, patternMap, 1, 1);
				// add the found document to result list (if document not found link is broken)
				if (!documents.isEmpty()) {
					if (allNewFoundDocuments.get(alternativeTitles.getKey()) == null) {
						allNewFoundDocuments.put(alternativeTitles.getKey(), new ArrayList<Document>());
					}
					allNewFoundDocuments.get(alternativeTitles.getKey()).add(documents.get(0));
					if (this.getConfig().isEnableLogs()) {
						logger.info("Added alternative document with title '"+documents.get(0).
						getFieldByName(WikiContentHandler.FIELD_TITLE).getValue() +"'.");
					}
				}
			}
		}

		// look for redirects and collect redirect titles
		Map<String, Map<String, Float>> allRedirectTitles = this.handleRedirectDocuments(completeResult);

		// get the redirect documents
		for (Entry<String, Map<String, Float>> redirectTitles : allRedirectTitles.entrySet()) {

			for (Entry<String, Float> redirectTitle : redirectTitles.getValue().entrySet()) {

				// redirected document inherits rating from redirecting document
				patternMap.clear();
				patternMap.put(redirectTitle.getValue(), singleTermPattern);

				List<Document> documents = this.findSurfaceFormDocuments(redirectTitle.
                    getKey(), patternMap, 1, 1);

				// add the found document to result list (if document not found link is broken)
				if (!documents.isEmpty()) {

					if (!allNewFoundDocuments.containsKey(redirectTitles.getKey())) {
						allNewFoundDocuments.put(redirectTitles.getKey(), new ArrayList<Document>());
					}

					allNewFoundDocuments.get(redirectTitles.getKey()).add(documents.get(0));
					if (this.getConfig().isEnableLogs()) {
						logger.info("Added redirect document with title '"+documents.get(0).
							getFieldByName(WikiContentHandler.FIELD_TITLE).getValue() +"'.");
					}
				}
			}
		}

		// categorize all new found documents
		this.getDocumentCategorizer().categorizeDocuments(allNewFoundDocuments);

		// add alternative and redirect documents to result map if not already present
		for (Entry<String, List<Document>> newFoundDocuments : allNewFoundDocuments.entrySet()) {
			for (Document document : newFoundDocuments.getValue()) {
				if (!completeResult.get(newFoundDocuments.getKey()).contains(document)) {
					completeResult.get(newFoundDocuments.getKey()).add(document);
				}
			}
		}
		return completeResult;
	}

	/**
	 * Searches for documents where the title matches with a list of rated pattern related to a termValue
	 * @param luceneAPI api with opened connection to the index for accessing data
	 * @param surfaceName one of the term values the user entered
	 * @param allRatedQueryPattern a number of lists containing title pattern sorted by its rating
	 * @param maxTermDocuments max number of documents that should be found by using given pattern
	 * @param maxTermDocumentsPerPattern max number of docuemnts that should be found for a specific pattern
	 * @return found documents
	 * @throws ApiException
	 */
	private List<Document> findSurfaceFormDocuments(String surfaceForm,
		Map<Float, List<String>> allRatedQueryPattern, int maxTermDocuments,
        int maxTermDocumentsPerPattern) throws ApiException {

		// important: reverse order of the map for using pattern with highest rating first!
		Map<Float, List<Query>> allRatedQueries = new TreeMap<Float, List<Query>>(Collections.reverseOrder());
		// for each rating
		for (Map.Entry<Float, List<String>> ratedQueryPattern : allRatedQueryPattern.entrySet()) {
			// for each pattern
			List<Query> queriesForRating = new ArrayList<Query>();
			// build lucene queries out of the given pattern
			for (String patternForRating : ratedQueryPattern.getValue()) {

				String[] splittedPatternForRating = patternForRating.split(WikiConstants.PATTERN_PART_TERMVALUE);
				StringBuilder queryString = new StringBuilder();
				if (splittedPatternForRating.length == 2) {
					if (!splittedPatternForRating[0].equals(WikiConstants.PATTERN_PART_NOTHING)) {
						queryString.append(splittedPatternForRating[0]);
					}
					queryString.append(surfaceForm.toLowerCase());
					if (!splittedPatternForRating[1].equals(WikiConstants.PATTERN_PART_NOTHING)) {
						queryString.append(splittedPatternForRating[1]);
					}
					Query query = new WildcardQuery(new Term(WikiContentHandler.FIELD_TITLE,
						queryString.toString()));
					queriesForRating.add(query);
				} else {
					logger.error("Error in pattern.");
					return new ArrayList<Document>();
				}
			}
			allRatedQueries.put(ratedQueryPattern.getKey(), queriesForRating);
		}
        // done building lucene queries out of the given pattern

        // now get the candidates
		List<Document> documents = this.searchForDocuments(allRatedQueries,
			maxTermDocuments, maxTermDocumentsPerPattern);

		return documents;
	}

	/**
	 * Searches for Documents with a list of queries for each rating (more than one
	 * query can have the same rating).
	 * @param luceneAPI
	 * @param allRatedQueries
	 * @param maxTermDocuments
	 * @param maxTermDocumentsPerPattern
	 * @return found documents
	 */
	private List<Document> searchForDocuments(Map<Float,List<Query>> allRatedQueries,
		int maxTermDocuments, int maxTermDocumentsPerPattern) throws ApiException {

		List<Document> allFoundDocuments = new ArrayList<Document>();

			// for each rating
			for (Map.Entry<Float, List<Query>> ratedQueries : allRatedQueries.entrySet()) {

				if (allFoundDocuments.size() < maxTermDocuments) {

					Float candidateRating = ratedQueries.getKey();

                    // only get candidates if rating not 0
                    if (candidateRating != null && candidateRating != 0.0f) {

                        // for each query pattern
                        for (Query ratedQuery : ratedQueries.getValue()) {

                            List<Document> foundDocumentsForRating = this.getConfig().getConnector().getLuceneAPI().
                                searchForDocuments(ratedQuery, maxTermDocumentsPerPattern);

                            // add found document if not already added somewhere
                            for (Document foundDocumentForRating : foundDocumentsForRating) {

                                if (!allFoundDocuments.contains(foundDocumentForRating)) {
                                    foundDocumentForRating.addCandidateRating(this.getClass(), candidateRating);
                                    allFoundDocuments.add(foundDocumentForRating);
                                    if (allFoundDocuments.size() == maxTermDocuments) {
                                        break;
                                    }
                                }
                            }
                            if (allFoundDocuments.size() == maxTermDocuments) {
                                break;
                            }
                        }
                    }
				} else {
					break;
				}
			}
		return allFoundDocuments;
	}

	/**
	 * Gets missing redirect documents from the index
	 * @param termValuesDocuments
	 * @return map containing titles and ratings of all redirects that are not already load from the index
	 */
	private Map<String, Map<String, Float>> handleRedirectDocuments(Map<String, List<Document>> termValuesDocuments) {

		// result map (HashMap<termValue, HashMap<Title,rating>> )
		Map<String, Map<String, Float>> missingRedirects = new HashMap<String, Map<String, Float>>();
		// for each termValue...
		for (Entry<String, List<Document>> surfaceFormDocuments : termValuesDocuments.entrySet()) {

			Map<String, Float> currentRedirectSurfaceForms = new HashMap<String, Float>();
			for (Document document : surfaceFormDocuments.getValue()) {

				if (document.getType() == Document.DOCUMENT_TYPE_REDIRECT) {

					Field currRedirectSurfaceForm = document.getFieldByName(WikiContentHandler.FIELD_REDIRECT_TITLE);
					currentRedirectSurfaceForms.put(currRedirectSurfaceForm.getValue(), document.getCandidateRating(this.getClass()));
				}
			}

			for (Entry<String, Float> currentRedirectSurfaceForm : currentRedirectSurfaceForms.entrySet()) {

				boolean found = false;
				for (Document document : surfaceFormDocuments.getValue()) {

					if (document.getFieldByName(WikiContentHandler.FIELD_TITLE).
						getValue().equals(currentRedirectSurfaceForm.getKey())) {

						found = true;
						break;
					}
				}

				// document with redirect title not already in list
				if (!found) {

					if (missingRedirects.get(surfaceFormDocuments.getKey()) == null) {
						missingRedirects.put(surfaceFormDocuments.getKey(), new HashMap<String, Float>());
					}
					missingRedirects.get(surfaceFormDocuments.getKey()).
						put(currentRedirectSurfaceForm.getKey(), currentRedirectSurfaceForm.getValue());
				}
			}
		}
		return missingRedirects;
	}

	/**
	 * Sets the rating of all documents who are created from wikipedia disambiguation pages
	 * to a specific value (overrides old value if existing).
	 * @param termValuesDocuments
	 * @param alternativeURIRating
	 * @return list of document titles that are alternatives but are not already in list
	 */
	private Map<String, Set<String>> handleDisambiguationDocuments(Map<String, List<Document>> termValuesDocuments,
		float alternativeURIRating) {

		Map<String, Set<String>> missingAlternatives = new HashMap<String, Set<String>>();
		// collect the alternatives from all disambiguation sites
		Set<String> allAlternativeTitles = new HashSet<String>();
		// for each termValue...
		for (Entry<String, List<Document>> surfaceFormDocuments : termValuesDocuments.entrySet()) {

			// collect the alternatives from the disambiguation sites of the termValue
			Set<String> currentAlternativeTitles = new HashSet<String>();
			// for each found document of the termValue...
			for (Document document : surfaceFormDocuments.getValue()) {
				// if its a disambiguation page, extract alternative titles
				if (document.getType() == Document.DOCUMENT_TYPE_DISAMBIGUATION) {
					Field currAltTitles = document.getFieldByName(WikiContentHandler.FIELD_ALTERNATIVE_TITLES);
					String[] splitted = currAltTitles.getValue().split(Field.FIELD_VALUE_LIST_SEPARATOR);
					for (String altTitle : splitted) {
						altTitle = altTitle.trim();
						altTitle = altTitle.toLowerCase();
						currentAlternativeTitles.add(altTitle);
					}
				}
			}
			// collect alternative titles for later re-rating
			allAlternativeTitles.addAll(currentAlternativeTitles);
			// collect titles of missing alternative documents
			for (String currentAlternativeTitle : currentAlternativeTitles) {
				boolean found = false;
				for (Document document : surfaceFormDocuments.getValue()) {
					// if a document with the alternative title found in already
					// found documents stop searching
					if (document.getFieldByName(WikiContentHandler.FIELD_TITLE).
						getValue().equals(currentAlternativeTitle)) {

						found = true;
						break;
					}
				}
				// document with alternative title not already in list
				if (!found) {
					if (missingAlternatives.get(surfaceFormDocuments.getKey()) == null) {
						missingAlternatives.put(surfaceFormDocuments.getKey(), new HashSet<String>());
					}
					missingAlternatives.get(surfaceFormDocuments.getKey()).add(currentAlternativeTitle);
				}
			}
		}

		// set the rating for all documents that are alternatives
		for (Entry<String, List<Document>> termValueDocuments : termValuesDocuments.entrySet()) {

			for (Document document : termValueDocuments.getValue()) {

				Field title = document.getFieldByName(WikiContentHandler.FIELD_TITLE);
				if (allAlternativeTitles.contains(title.getValue())) {

					document.addCandidateRating(this.getClass(), alternativeURIRating);
					Field uri = document.getFieldByName(WikiContentHandler.FIELD_URI);
					if (this.getConfig().isEnableLogs()) {
						logger.info("Alternative URI rating set for '"+uri.getValue()+"'.");
					}
				}
			}
		}
		return missingAlternatives;
	}
}
