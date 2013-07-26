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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.WildcardQuery;

/**
 * Slower implementation of the titlePatternCandidateFinder (tried to find faster
 * implementation by sending fewer but bigger lucene queries, but was slower)
 * @deprecated not efficient.
 * @author Robert Illers
 */
public final class TitlePatternCandidateFinder2 extends AbstractDocumentCandidateFinder <WikiDisambiguationEngineConfig>
	implements DocumentCandidateFinder {

	/**
	 * Constructor
	 * @param luceneAPI
	 */
	public TitlePatternCandidateFinder2(WikiDisambiguationEngineConfig config) {
		super(config);
	}

    /**
     *
     * @param surfaceNames
     * @param allRatedQueryPattern
     * @param candidateFinder_maxTermDocuments
     * @param maxTermDocumentsPerPattern
     * @param alternativeURIRating
     * @return
     * @throws ApiException
     */
    @Override
    protected Map<String, List<Document>> findCandidateDocumentsImpl(List<String> surfaceNames,
        Map<Float, List<String>> allRatedQueryPattern, int candidateFinder_maxTermDocuments,
        int maxTermDocumentsPerPattern, float alternativeURIRating) throws ApiException {

        // result map
		Map<String, List<Document>> completeResult = this.findSurfaceFormDocuments(
            surfaceNames, allRatedQueryPattern, maxTermDocumentsPerPattern, maxTermDocumentsPerPattern);

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

        // look for redirects and collect redirect titles
		Map<String, Map<String, Float>> allRedirectTitles = this.handleRedirectDocuments(completeResult);

        return completeResult;
    }

    /**
     *
     * @param luceneAPI
     * @param surfaceForms
     * @param allRatedQueryPattern
     * @param maxTermDocuments
     * @param maxTermDocumentsPerPattern
     * @return
     * @throws ApiException
     */
    private Map<String, List<Document>> findSurfaceFormDocuments(List<String> surfaceForms,
		Map<Float, List<String>> allRatedQueryPattern, int maxTermDocuments,
        int maxTermDocumentsPerPattern) throws ApiException {

        List<String> allTermPattern = new ArrayList<String>();
        for (List<String> pattern : allRatedQueryPattern.values()) {
            allTermPattern.addAll(pattern);
        }

        BooleanQuery bQuery = new BooleanQuery();
        for (String surfaceForm : surfaceForms) {
            for (String pattern : allTermPattern) {

                String[] splittedPatternForRating = pattern.split(WikiConstants.PATTERN_PART_TERMVALUE);
                StringBuilder queryString = new StringBuilder();
                if (splittedPatternForRating.length == 2) {

                    if (!splittedPatternForRating[0].equals(WikiConstants.PATTERN_PART_NOTHING)) {
                            queryString.append(splittedPatternForRating[0]);
                    }
                    queryString.append(surfaceForm.toLowerCase());
                    if (!splittedPatternForRating[1].equals(WikiConstants.PATTERN_PART_NOTHING)) {
                        queryString.append(splittedPatternForRating[1]);
                    }
                     Query wQuery = new WildcardQuery(new Term(WikiContentHandler.FIELD_TITLE,
                        queryString.toString()));
                     bQuery.add(wQuery, BooleanClause.Occur.SHOULD);
                } else {
                    logger.error("Error in pattern.");
                    return new HashMap<String, List<Document>>();
                }
            }
        }
        // now get the candidates
		return this.searchForDocuments(bQuery, surfaceForms,
			maxTermDocuments, maxTermDocumentsPerPattern, allRatedQueryPattern);

    }

    /**
     *
     * @param luceneAPI
     * @param query
     * @param surfaceForms
     * @param maxTermDocuments
     * @param maxTermDocumentsPerPattern
     * @param allRatedQueryPattern
     * @return
     * @throws ApiException
     */
	private Map<String, List<Document>> searchForDocuments(Query query, List<String> surfaceForms,
		int maxTermDocuments, int maxTermDocumentsPerPattern, Map<Float, List<String>> allRatedQueryPattern) throws ApiException {

        List<Document> documents = this.getConfig().getConnector().getLuceneAPI().searchForDocuments(query, maxTermDocuments);
        return this.repatchPatternAndSurfaceFormsOnResult(documents, maxTermDocumentsPerPattern, allRatedQueryPattern, surfaceForms);
    }

    /**
     *
     * @param documents
     * @param maxTermDocumentsPerPattern
     * @param allRatedQueryPattern
     * @param surfaceForms
     * @return
     */
    private Map<String, List<Document>> repatchPatternAndSurfaceFormsOnResult(List<Document> documents,
        int maxTermDocumentsPerPattern, Map<Float, List<String>> allRatedQueryPattern, List<String> surfaceForms) {

        Map<String, List<Document>> result = new HashMap<String, List<Document>>();
        for (String surfaceForm : surfaceForms) {

            result.put(surfaceForm, new ArrayList<Document>());
            for (Document document : documents) {

                String documentTitle = document.getFieldByName(WikiContentHandler.FIELD_TITLE).getValue();
                if (documentTitle.contains(surfaceForm)) {

                    boolean foundCandidate = false;
                    for (Entry<Float, List<String>> ratedQueryPattern : allRatedQueryPattern.entrySet()) {

                        for (String patternString : ratedQueryPattern.getValue()) {

                            String[] splittedPatternString = patternString.split(WikiConstants.PATTERN_PART_TERMVALUE);
                            StringBuilder aRegex = new StringBuilder();
                            if (splittedPatternString.length != 2) {

                                logger.error("Error in pattern.");
                                return new HashMap<String, List<Document>>();
                            }
                            if (!splittedPatternString[0].equals(WikiConstants.PATTERN_PART_NOTHING)) {
                                String temp = splittedPatternString[0];
								temp = temp.replace("*", ".*");
                                temp = temp.replace("(", "/(");
                                temp = temp.replace(")", "/)");
                                aRegex.append(temp);
                            }
                            aRegex.append(surfaceForm);
                            if (!splittedPatternString[1].equals(WikiConstants.PATTERN_PART_NOTHING)) {
                                String temp = splittedPatternString[1];
                                temp = temp.replace("*", ".*");
                                temp = temp.replace("(", "/(");
                                temp = temp.replace(")", "/)");
                                aRegex.append(temp);
                            }
                            logger.debug("aRegex: "+aRegex.toString());
                            Pattern pattern = Pattern.compile(aRegex.toString(), Pattern.CASE_INSENSITIVE);
                            Matcher matcher = pattern.matcher(documentTitle);
                            if (matcher.find()) {
                                foundCandidate = true;
                                document.addCandidateRating(this.getClass(), ratedQueryPattern.getKey());
                                result.get(surfaceForm).add(document);
                                break;
                            }
                        }
                        if (foundCandidate) {
                            break;
                        }
                    }

                }
            }
        }
        return result;
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
		for (Map.Entry<String, List<Document>> surfaceFormDocuments : termValuesDocuments.entrySet()) {

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
		for (Map.Entry<String, List<Document>> termValueDocuments : termValuesDocuments.entrySet()) {

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

    /**
	 * Gets missing redirect documents from the index
	 * @param termValuesDocuments
	 * @return map containing titles and ratings of all redirects that are not already load from the index
	 */
	private Map<String, Map<String, Float>> handleRedirectDocuments(Map<String, List<Document>> termValuesDocuments) {

		// result map (HashMap<termValue, HashMap<Title,rating>> )
		Map<String, Map<String, Float>> missingRedirects = new HashMap<String, Map<String, Float>>();
		// for each termValue...
		for (Map.Entry<String, List<Document>> surfaceFormDocuments : termValuesDocuments.entrySet()) {

			Map<String, Float> currentRedirectSurfaceForms = new HashMap<String, Float>();
			for (Document document : surfaceFormDocuments.getValue()) {

				if (document.getType() == Document.DOCUMENT_TYPE_REDIRECT) {

					Field currRedirectSurfaceForm = document.getFieldByName(WikiContentHandler.FIELD_REDIRECT_TITLE);
					currentRedirectSurfaceForms.put(currRedirectSurfaceForm.getValue(), document.getCandidateRating(this.getClass()));
				}
			}

			for (Map.Entry<String, Float> currentRedirectSurfaceForm : currentRedirectSurfaceForms.entrySet()) {

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

}
