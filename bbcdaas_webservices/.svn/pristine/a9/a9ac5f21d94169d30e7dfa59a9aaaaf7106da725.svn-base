package de.bbcdaas.webservices.services.disambiguation;

import de.bbcdaas.common.beans.category.Category;
import de.bbcdaas.common.beans.category.CategoryTree;
import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.beans.document.Field;
import de.bbcdaas.disambiguation.core.connector.lucene.LuceneConnector;
import de.bbcdaas.disambiguation.core.interfaces.DisambiguationEngine;
import de.bbcdaas.disambiguation.wikipedia.dataexport.lucene.WikiCategoryExporter;
import de.bbcdaas.disambiguation.wikipedia.dataexport.lucene.WikiCategoryExporterConfig;
import de.bbcdaas.disambiguation.wikipedia.dataimport.lucene.WikiContentHandler;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiConstants;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiDisambiguationEngine;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiDisambiguationEngineConfig;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.categorizer.ContentFieldDocumentCategorizer;
import de.bbcdaas.webservices.api.disambiguation.DisambiguationWebserviceURIs;
import de.bbcdaas.webservices.api.disambiguation.beans.CategoryContext;
import de.bbcdaas.webservices.api.disambiguation.beans.Pattern;
import de.bbcdaas.webservices.api.disambiguation.beans.ScoredDocument;
import de.bbcdaas.webservices.api.disambiguation.beans.WikipediaResult;
import de.bbcdaas.webservices.constants.Constants;
import de.bbcdaas.webservices.services.RestServices;
import java.io.File;
import java.util.*;
import java.util.Map.Entry;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.commons.configuration.Configuration;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.util.Version;

/**
 * RESTful webservices for disambiguation purposes.
 * @author Robert Illers
 */
@Path(DisambiguationWebserviceURIs.RESTSERVICE_BASE)
public final class DisambiguationRestServices extends RestServices {

	private static CategoryTree tree = null;

    /**
     *
     * @param title
     * @return
     */
    @GET
    @Path(DisambiguationWebserviceURIs.RESTSERVICE_SEARCH_FOR_WIKIPEDIA_URIS)
    @Produces(MediaType.APPLICATION_JSON)
    public WikipediaResult searchForWikipediaURIs(
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_SEARCH_FOR_WIKIPEDIA_URIS_PARAM_LABEL) String title,
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_SEARCH_FOR_WIKIPEDIA_URIS_PARAM_MAX_HITS) String maxHits_String) {

		Configuration webservicesConfiguration = this.getWebservicesConfiguration();
		List<String> disabledWebservices = Arrays.asList(webservicesConfiguration.getStringArray(Constants.CONFIG_PARAM_DISABLED_WEBSERVICES_DISAMBIGUATION));

		if (disabledWebservices.isEmpty() ||
			disabledWebservices.contains(Constants.CONFIG_KEYWORD_NONE) ||
			!disabledWebservices.contains(DisambiguationWebserviceURIs.RESTSERVICE_SEARCH_FOR_WIKIPEDIA_URIS)) {

			WikipediaResult result = new WikipediaResult();

			LuceneConnector connector = new LuceneConnector(Version.LUCENE_35,
				WikiConstants.LUCENE_INDEX_PATH, new GermanAnalyzer(Version.LUCENE_35), null, 0);

			WikiDisambiguationEngineConfig config = new WikiDisambiguationEngineConfig(connector);
			config.setUriPrefix("http://de.wikipedia.org/wiki/");
			config.setDocumentCategorizer(new ContentFieldDocumentCategorizer(config));

			DisambiguationEngine disambiguationEngine = new WikiDisambiguationEngine(config);

			try {

				int maxHits = WikiConstants.DEFAULT_URL_SEARCH_MAX_HITS;
				if (maxHits_String != null && !maxHits_String.isEmpty()) {
					try {
						maxHits = Integer.parseInt(maxHits_String);
					} catch(NumberFormatException ex) {
						logger.error(ex.getMessage(), ex);
					}
				}

				List<Document> documents = disambiguationEngine.getDocumentsByTitle(title, maxHits);

				// extract required fields
				for (Document document : documents) {

					Field uriField = document.getFieldByName(WikiContentHandler.FIELD_URI);
					ArrayList<String> keywords = new ArrayList<String>();
					Field keywordsField = document.getFieldByName(WikiContentHandler.FIELD_KEYWORDS);

					if (keywordsField != null) {

						String[] splitted = keywordsField.getValue().split(Field.FIELD_VALUE_LIST_SEPARATOR);

						for (String keyword : splitted) {
							keywords.add(keyword.trim().toLowerCase());
						}
					}

					ScoredDocument scoredDocument = new ScoredDocument();
					scoredDocument.setUri(uriField.getValue());
					scoredDocument.setKeywords(keywords);
					scoredDocument.setDocumentType(document.getType());
					result.getScoredDocuments().add(scoredDocument);
				}

			} catch(Exception e) {
				logger.error(e.getMessage(), e);
			}
			return result;
		}
		logger.info("Disabled Webservice "+DisambiguationWebserviceURIs.RESTSERVICE_SEARCH_FOR_WIKIPEDIA_URIS+" called.");
		return null;
    }

	/**
	 * Gets the configuration for WikipediaDoc scoring.
	 * @return WikipediaResult
	 */
	@GET
	@Path(DisambiguationWebserviceURIs.RESTSERVICE_GET_SCORING_CONFIGURATION)
	@Produces(MediaType.APPLICATION_JSON)
	public WikipediaResult getScoringConfiguration() {

		Configuration webservicesConfiguration = this.getWebservicesConfiguration();
		List<String> disabledWebservices = Arrays.asList(webservicesConfiguration.getStringArray(Constants.CONFIG_PARAM_DISABLED_WEBSERVICES_DISAMBIGUATION));

		if (disabledWebservices.isEmpty() ||
			disabledWebservices.contains(Constants.CONFIG_KEYWORD_NONE) ||
			!disabledWebservices.contains(DisambiguationWebserviceURIs.RESTSERVICE_GET_SCORING_CONFIGURATION)) {

			WikipediaResult result = new WikipediaResult();
			WikiDisambiguationEngineConfig config = new WikiDisambiguationEngineConfig();
			String relPath = new StringBuilder(WikiConstants.CONFIGURATION_CONFIG_FOLDER_NAME).
				append(File.separator).append(WikiConstants.DISAMBIGUATION_CONFIGURATION_FILE_NAME).toString();

			config.readConfigFromPropertiesFile(relPath);

			this.setScoreConfigIntoResult(config, result);
			return result;
		}
		logger.info("Disabled Webservice "+DisambiguationWebserviceURIs.RESTSERVICE_GET_SCORING_CONFIGURATION+" called.");
		return null;
	}

    /**
     * Gets rated URLs of wikipedia articles from a lucene index who are related
	 * to the context a given list of termValue describes.
     * @param terms
     * @return WikipediaResult
     */
    @GET
    @Path(DisambiguationWebserviceURIs.RESTSERVICE_SCORE_WIKIPEDIA_DOCS)
    @Produces(MediaType.APPLICATION_JSON)
    public WikipediaResult scoreWikipediaDocs(
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_SCORE_WIKIPEDIA_DOCS_PARAM_TERMS) String terms,
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_SCORE_WIKIPEDIA_DOCS_PARAM_MAX_TERM_DOCUMENTS) String maxTermDocuments_String,
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_SCORE_WIKIPEDIA_DOCS_PARAM_MAX_TERM_DOCUMENTS_PER_PATTERN) String maxTermDocumentsPerPattern_String,
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_SCORE_WIKIPEDIA_DOCS_PARAM_MULTIMATCHING_DOCUMENTS_RATING_ADDEND) String multimatchingDocumentsRatingAddend_String,
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_SCORE_WIKIPEDIA_DOCS_PARAM_PATTERN_RATINGS) String patternRatings_String,
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_SCORE_WIKIPEDIA_DOCS_PARAM_ALTERNATIVE_URI_RATING) String alternativeURIRating_String,
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_SCORE_WIKIPEDIA_DOCS_PARAM_SCORER_WEIGHTINGS) String linearScoringCombinerScorerWeightings_String,
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_SCORE_WIKIPEDIA_DOCS_PARAM_CANDIDATE_FINDER) String candidateFinder_String) {

		Configuration webservicesConfiguration = this.getWebservicesConfiguration();
		List<String> disabledWebservices = Arrays.asList(webservicesConfiguration.getStringArray(Constants.CONFIG_PARAM_DISABLED_WEBSERVICES_DISAMBIGUATION));

		if (disabledWebservices.isEmpty() ||
			disabledWebservices.contains(Constants.CONFIG_KEYWORD_NONE) ||
			!disabledWebservices.contains(DisambiguationWebserviceURIs.RESTSERVICE_SCORE_WIKIPEDIA_DOCS)) {

			return this.scoreWikipediaDocsImpl(terms, maxTermDocuments_String,
				maxTermDocumentsPerPattern_String, multimatchingDocumentsRatingAddend_String,
				patternRatings_String, alternativeURIRating_String, linearScoringCombinerScorerWeightings_String,
				candidateFinder_String);
		}
		logger.info("Disabled Webservice "+DisambiguationWebserviceURIs.RESTSERVICE_SCORE_WIKIPEDIA_DOCS+" called.");
		return null;
    }

	/**
	 * Gets the wikipedia URI that matches best to the given context described by
	 * a term cloud.
	 * @param terms
	 * @param maxTermDocuments_String
	 * @param maxTermDocumentsPerPattern_String
	 * @param multimatchingDocumentsRatingAddend_String
	 * @param patternRatings_String
	 * @param alternativeURIRating_String
	 * @param scorerWeightings_String
	 * @return URI
	 */
	@GET
	@Path(DisambiguationWebserviceURIs.RESTSERVICE_DISAMBIGUATE_CONTEXT)
	@Produces(MediaType.TEXT_PLAIN)
	public String disambiguateContext(
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_DISAMBIGUATE_CONTEXT_PARAM_TERMS) String terms,
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_DISAMBIGUATE_CONTEXT_PARAM_MAX_TERM_DOCUMENTS) String maxTermDocuments_String,
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_DISAMBIGUATE_CONTEXT_PARAM_MAX_TERM_DOCUMENTS_PER_PATTERN) String maxTermDocumentsPerPattern_String,
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_DISAMBIGUATE_CONTEXT_PARAM_MULTIMATCHING_DOCUMENTS_RATING_ADDEND) String multimatchingDocumentsRatingAddend_String,
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_DISAMBIGUATE_CONTEXT_PARAM_PATTERN_RATINGS) String patternRatings_String,
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_DISAMBIGUATE_CONTEXT_PARAM_ALTERNATIVE_URI_RATING) String alternativeURIRating_String,
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_DISAMBIGUATE_CONTEXT_PARAM_SCORER_WEIGHTINGS) String linearScoringCombinerScorerWeightings_String,
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_SCORE_WIKIPEDIA_DOCS_PARAM_CANDIDATE_FINDER) String candidateFinder_String) {

		Configuration webservicesConfiguration = this.getWebservicesConfiguration();
		List<String> disabledWebservices = Arrays.asList(webservicesConfiguration.getStringArray(Constants.CONFIG_PARAM_DISABLED_WEBSERVICES_DISAMBIGUATION));

		if (disabledWebservices.isEmpty() ||
			disabledWebservices.contains(Constants.CONFIG_KEYWORD_NONE) ||
			!disabledWebservices.contains(DisambiguationWebserviceURIs.RESTSERVICE_DISAMBIGUATE_CONTEXT)) {

			WikipediaResult result = this.scoreWikipediaDocsImpl(terms, maxTermDocuments_String,
				maxTermDocumentsPerPattern_String, multimatchingDocumentsRatingAddend_String,
				patternRatings_String, alternativeURIRating_String, linearScoringCombinerScorerWeightings_String,
				candidateFinder_String);

			String disambiguationURI = "";
			if (result.getScoredDocuments() != null && !result.getScoredDocuments().isEmpty()) {
				List<ScoredDocument> scoredDocuments = new ArrayList<ScoredDocument>();
				for (ScoredDocument scoredDocument : result.getScoredDocuments()) {
					if (!scoredDocuments.contains(scoredDocument)) {
						scoredDocuments.add(scoredDocument);
					}
				}
				Collections.sort(scoredDocuments);
				disambiguationURI = scoredDocuments.get(0).getUri();
			}
			return disambiguationURI;
		}
		logger.info("Disabled Webservice "+DisambiguationWebserviceURIs.RESTSERVICE_DISAMBIGUATE_CONTEXT+" called.");
		return null;
	}

	/**
	 *
	 * @param categoryName
	 * @return WikipediaResult object containing a list of category contexts
	 */
	@GET
	@Path(DisambiguationWebserviceURIs.RESTSERVICE_GET_CATEGORY_CONTEXT)
	@Produces(MediaType.APPLICATION_JSON)
	public WikipediaResult getWikipediaCategoryContext(
		@QueryParam(DisambiguationWebserviceURIs.RESTSERVICE_GET_CATEGORY_CONTEXT_PARAM_CATEGORY_NAME) String categoryName) {

		Configuration webservicesConfiguration = this.getWebservicesConfiguration();
		List<String> disabledWebservices = Arrays.asList(webservicesConfiguration.getStringArray(Constants.CONFIG_PARAM_DISABLED_WEBSERVICES_DISAMBIGUATION));
		if (disabledWebservices.isEmpty() ||
			disabledWebservices.contains(Constants.CONFIG_KEYWORD_NONE) ||
			!disabledWebservices.contains(DisambiguationWebserviceURIs.RESTSERVICE_GET_CATEGORY_CONTEXT)) {

			// result object
			WikipediaResult result = new WikipediaResult();

			if (categoryName != null && !categoryName.isEmpty()) {

				ArrayList<CategoryContext> catContextList = new ArrayList<CategoryContext>();

				if (tree == null) {

					logger.info("Caching category tree...");
					Version luceneVersion = Version.LUCENE_35;
					String indexPath = WikiConstants.LUCENE_INDEX_PATH;
					Analyzer standardAnalyzer = new GermanAnalyzer(luceneVersion);
					String categoryTitleQueryString = "kategorie:*";
					int maxHits = 200000;

					LuceneConnector connector = new LuceneConnector(luceneVersion,
						indexPath, standardAnalyzer, null, 0);

					WikiCategoryExporterConfig config = new WikiCategoryExporterConfig(connector);
					config.setCategoryTitleQueryString(categoryTitleQueryString);
					config.addRootDirectoryName("sachsystematik");
					config.addRootDirectoryName("räumliche systematik");
					config.addRootDirectoryName("zeitliche systematik");
					config.addRootDirectoryName("abkürzung");
					config.addRootDirectoryName("begriffsklärung");
					config.addRootDirectoryName("liste");
					config.addRootDirectoryName("!Wikipedia");
					config.addCategoryTitleExclude("wikipedia:");

					WikiCategoryExporter categoryExporter = new WikiCategoryExporter(config);

					tree = categoryExporter.export(maxHits);
					logger.info("Done caching category tree.");
				}

				logger.info("Incoming Category name: '"+categoryName+"'");
				CategoryContext categoryContext = new CategoryContext();
				categoryContext.setCategoryName(categoryName);

				Set<String> parentNames = new HashSet<String>();
				Set<String> childNames = new HashSet<String>();

				List<Category> categoryObjects = tree.findCategory(categoryName);
				for (Category categoryObject : categoryObjects) {
					parentNames.add(categoryObject.getParentName());
					for (Category subCategory : categoryObject.getSubCategories()) {
						childNames.add(subCategory.getName());
					}
				}
				if (categoryObjects.isEmpty()) {
					logger.info("Category not found in category tree.");
				} else {
					logger.info("Found parent categories: "+parentNames.toString());
					logger.info("Found direct child categories: "+childNames.toString());
				}
				for (String parentName : parentNames) {
					categoryContext.addCategoryParentName(parentName);
				}
				for (String childName : childNames) {
					categoryContext.addCategoryDirectChildName(childName);
				}
				catContextList.add(categoryContext);
				result.setCategoryContext(catContextList);
			}
			return result;
		}
		logger.info("Disabled Webservice "+DisambiguationWebserviceURIs.RESTSERVICE_GET_CATEGORY_CONTEXT+" called.");
		return null;
	}

	// ----------------------------- private methods ---------------------------

	private WikipediaResult scoreWikipediaDocsImpl(String surfaceForms, String maxTermDocuments_String,
		String maxTermDocumentsPerPattern_String, String multimatchingDocumentsRatingAddend_String,
		String patternRatings_String, String alternativeURIRating_String,
		String linearScoringCombinerScorerWeightings_String, String candidateFinder_String) {

		// result object
		WikipediaResult result = new WikipediaResult();

		if (surfaceForms != null && !surfaceForms.isEmpty()) {

			// clean the surface forms
			List<String> surfaceForms_splitted = Arrays.asList(surfaceForms.split(","));
			Set<String> surfaceForms_cleaned = new LinkedHashSet<String>();
			for (String surfaceForm : surfaceForms_splitted) {
				surfaceForm = surfaceForm.trim();
				if (!surfaceForm.isEmpty()) {
					surfaceForms_cleaned.add(surfaceForm);
				}
			}

			// begin disambiguation only if more than 1 surfaceForm has been received
			if (surfaceForms_cleaned.size() > 1) {

				LuceneConnector connector = new LuceneConnector(Version.LUCENE_35,
					WikiConstants.LUCENE_INDEX_PATH, new GermanAnalyzer(Version.LUCENE_35), null, 0);

				WikiDisambiguationEngineConfig config = new WikiDisambiguationEngineConfig(connector);
				config.setUriPrefix("http://de.wikipedia.org/wiki/");
				String relPath = new StringBuilder(WikiConstants.CONFIGURATION_CONFIG_FOLDER_NAME).
				append(File.separator).append(WikiConstants.DISAMBIGUATION_CONFIGURATION_FILE_NAME).toString();

				// get default configuration from serverside properties file
				config.readConfigFromPropertiesFile(relPath);

				/* overwrite default configuration with parameters from client */

				if (maxTermDocuments_String != null && !maxTermDocuments_String.isEmpty()) {
					try {
						config.setMaxTermDocuments(Integer.parseInt(maxTermDocuments_String));
					} catch(NumberFormatException e) {
						logger.error(e.getMessage());
					}
				}
				if (maxTermDocumentsPerPattern_String != null && !maxTermDocumentsPerPattern_String.isEmpty()) {
					try {
						config.setMaxTermDocumentsPerPattern(Integer.parseInt(maxTermDocumentsPerPattern_String));
					} catch(NumberFormatException e) {
						logger.error(e.getMessage());
					}
				}
				if (multimatchingDocumentsRatingAddend_String != null && !multimatchingDocumentsRatingAddend_String.isEmpty()) {
					try {
						config.setMultimatchingSurfaceFormScore(Float.parseFloat(multimatchingDocumentsRatingAddend_String));
					} catch(NumberFormatException e) {
						logger.error(e.getMessage());
					}
				}
				if (alternativeURIRating_String != null && !alternativeURIRating_String.isEmpty()) {
					try {
						config.setAlternativeURIRating(Float.parseFloat(alternativeURIRating_String));
					} catch(NumberFormatException e) {
						logger.error(e.getMessage());
					}
				}
				if (linearScoringCombinerScorerWeightings_String != null && !linearScoringCombinerScorerWeightings_String.isEmpty()) {
					try {
						config.clearLinearScoringCombinerScorerWeightings();
						for (String scorerWeighting_String : linearScoringCombinerScorerWeightings_String.split(",")) {
							config.addLinearScoringCombinerScorerWeighting(Float.parseFloat(scorerWeighting_String));
						}
					} catch(NumberFormatException e) {
						logger.error(e.getMessage());
					}
				}
				if (candidateFinder_String != null && !candidateFinder_String.isEmpty()) {
					List<String> candidateFinderNames = new ArrayList<String>();
					candidateFinderNames.addAll(Arrays.asList(candidateFinder_String.split(",")));
					config.setDocumentCandidateFinderNames(candidateFinderNames);
				}

				/* /overwrite default configuration with parameters from client */

				config.initializeObjectsFromPropertiesFile(relPath);

				if (patternRatings_String != null && !patternRatings_String.isEmpty()) {
					String[] patternRatings_splitted = patternRatings_String.split(",");
					int i = 1;
					// important: reverse order of the map for using pattern with highest rating first!
					Map<Float, List<String>> allRatedQueryPattern = new TreeMap<Float, List<String>>(Collections.reverseOrder());
					for (String patternRating : patternRatings_splitted) {
						String pattern = WikiConstants.PATTERN.get(i);
						if (pattern != null) {
							if (allRatedQueryPattern.get(Float.parseFloat(patternRating)) == null) {
								List<String> ratedPattern = new ArrayList<String>();
								ratedPattern.add(pattern);
								allRatedQueryPattern.put(Float.parseFloat(patternRating), ratedPattern);
							} else {
								allRatedQueryPattern.get(Float.parseFloat(patternRating)).add(pattern);
							}
						} else {
							logger.error("malformed param 'patternRatings': "+patternRatings_String);
							break;
						}
						i++;
					}
					config.setAllRatedQueryPattern(allRatedQueryPattern);
				}
				ArrayList<ScoredDocument> scoredDocuments = new ArrayList<ScoredDocument>();

				DisambiguationEngine disambiguationEngine = new WikiDisambiguationEngine(config);

				this.setScoreConfigIntoResult(config, result);

				try {

					logger.info("Incoming terms:"+surfaceForms_cleaned.toString());

					Map<String, List<Document>> termsWithDocuments = disambiguationEngine.
						scoreDocuments(Arrays.asList(surfaceForms_cleaned.toArray(new String[0])), false);

					for (Entry<String, List<Document>> termWithDocuments : termsWithDocuments.entrySet()) {
						for (Document document : termWithDocuments.getValue()) {

							if (document.getCombinedScore() != 0.0f) {

								ScoredDocument scoredDocument = new ScoredDocument();
								scoredDocument.setScore(document.getCombinedScore());
								scoredDocument.setTermValue(termWithDocuments.getKey());
								scoredDocument.setUri(document.getFieldByName(WikiContentHandler.FIELD_URI).getValue());
								logger.info("term='"+scoredDocument.getTermValue()+
									"', URI='"+scoredDocument.getUri()+"', Score='"+scoredDocument.getScore()+"'.");
								scoredDocuments.add(scoredDocument);
							}
						}
					}
				} catch(Exception e) {
					logger.error(e.getMessage(), e);
				}
				result.setScoredDocuments(scoredDocuments);
			}
		}
        return result;
	}

	/**
	 *
	 * @param result
	 */
	private void setScoreConfigIntoResult(WikiDisambiguationEngineConfig config, WikipediaResult result) {

		result.setMaxTermDocuments(config.getMaxTermDocuments());
		result.setMaxTermDocumentsPerPattern(config.getMaxTermDocumentsPerPattern());
		result.setMultimatchingDocumentsRatingAddend(config.getMultimatchingSurfaceFormScore());
		result.setAlternativeURIRating(config.getAlternativeURIRating());
        result.setCandidateFinderNames((ArrayList)Arrays.asList(config.getDocumentCandidateFinderNames().toArray(new String[0])));
		result.setDocumentScorerNames((ArrayList)Arrays.asList(config.getDocumentScorerNames().toArray(new String[0])));
		result.setDocumentScorerWeightings((ArrayList)Arrays.asList(config.getLinearScoringCombinerScorerWeightings().toArray(new Float[0])));
		ArrayList<Pattern> pattern = new ArrayList<Pattern>();
		for (Entry<Float, List<String>> ratedQueryPattern : config.getAllRatedQueryPattern().entrySet()) {
			Float rating = ratedQueryPattern.getKey();
			for (String pattern_String : ratedQueryPattern.getValue()) {
				for (Entry<Integer, String> patternWithID : WikiConstants.PATTERN.entrySet()) {
					if (patternWithID.getValue().equals(pattern_String)) {
						Pattern aPattern = new Pattern();
						aPattern.setId(patternWithID.getKey());
						aPattern.setPattern(pattern_String);
						aPattern.setRating(rating);
						pattern.add(aPattern);
						break;
					}
				}
			}
		}
		Collections.sort(pattern);
		result.setPattern(pattern);
	}
}
