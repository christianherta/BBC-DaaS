package de.bbcdaas.disambiguation.wikipedia.engine.lucene;

import de.bbcdaas.common.util.FileReader;
import de.bbcdaas.disambiguation.core.configs.AbstractDisambiguationEngineConfig;
import de.bbcdaas.disambiguation.core.connector.lucene.LuceneConnector;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.Configuration;

/**
 * Configuration for the WikiLuceneDisambiguationEngine.
 * @author Robert Illers
 */
public final class WikiDisambiguationEngineConfig extends AbstractDisambiguationEngineConfig<LuceneConnector> {

	private String uriPrefix;
	private List<Integer> candidateFinder_allQueryPatternIDs = new ArrayList<Integer>();
	private Map<Float, List<String>> candidateFinder_allRatedQueryPattern = WikiConstants.DEFAULT_PATTERN_CONFIG;
	private Integer candidateFinder_maxTermDocuments = WikiConstants.DEFAULT_TITLE_PATTERN_CANDIDATE_FINDER_MAX_TERM_DOCUMENTS;
	private Integer maxTermDocumentsPerPattern = WikiConstants.DEFAULT_TITLE_PATTERN_CANDIDATE_FINDER_MAX_TERM_DOCUMENTS_PER_PATTERN;
	private Float multimatchingSurfaceFormScore = WikiConstants.DEFAULT_MULTIMATCHING_SURFACEFORM_SCORE;
	private Float alternativeURIRating = WikiConstants.DEFAULT_TITLE_PATTERN_CANDIDATE_FINDER_ALTERNATIVE_URI_RATING;

	/**
	 * Constructor
	 * @param connector lucene connector
	 */
	public WikiDisambiguationEngineConfig(LuceneConnector connector) {
		super(connector);
	}

	/**
	 * Minimalistic constructor for using configuration without lucene, e.g. only
	 * as storage or for getting configuration parameters
	 */
	public WikiDisambiguationEngineConfig() {}

	/**
	 * Prefix for the URI of the disambiguated item.
	 * @return uriPrefix
	 */
	public String getUriPrefix() {
		return uriPrefix;
	}

	/**
	 * Prefix for the URI of the disambiguated item.
	 * @param uriPrefix
	 */
	public void setUriPrefix(String uriPrefix) {
		this.uriPrefix = uriPrefix;
	}

	/**
	 * The list of pattern with its ratings
	 * @return allRatedQueryPattern
	 */
	public Map<Float, List<String>> getAllRatedQueryPattern() {
		return candidateFinder_allRatedQueryPattern;
	}

	/**
	 * The list of pattern with its ratings
	 * @param allRatedQueryPattern
	 */
	public void setAllRatedQueryPattern(Map<Float, List<String>> allRatedQueryPattern) {
		this.candidateFinder_allRatedQueryPattern = allRatedQueryPattern;
	}

	/**
	 *
	 * @return candidateFinder_maxTermDocuments
	 */
	public Integer getMaxTermDocuments() {
		return candidateFinder_maxTermDocuments;
	}

	/**
	 *
	 * @param maxTermDocuments
	 */
	public void setMaxTermDocuments(Integer maxTermDocuments) {
		this.candidateFinder_maxTermDocuments = maxTermDocuments;
	}

	/**
	 *
	 * @return maxTermDocumentsPerPattern
	 */
	public Integer getMaxTermDocumentsPerPattern() {
		return maxTermDocumentsPerPattern;
	}

	/**
	 *
	 * @param maxTermDocumentsPerPattern
	 */
	public void setMaxTermDocumentsPerPattern(Integer maxTermDocumentsPerPattern) {
		this.maxTermDocumentsPerPattern = maxTermDocumentsPerPattern;
	}

	/**
	 *
	 * @return multimatchingSurfaceFormScore
	 */
	public Float getMultimatchingSurfaceFormScore() {
		return multimatchingSurfaceFormScore;
	}

	/**
	 *
	 * @param multimatchingSurfaceFormScore
	 */
	public void setMultimatchingSurfaceFormScore(Float multimatchingSurfaceFormScore) {
		this.multimatchingSurfaceFormScore = multimatchingSurfaceFormScore;
	}

	/**
	 *
	 * @return alternativeURIRating
	 */
	public Float getAlternativeURIRating() {
		return alternativeURIRating;
	}

	/**
	 *
	 * @param alternativeURIRating
	 */
	public void setAlternativeURIRating(Float alternativeURIRating) {
		this.alternativeURIRating = alternativeURIRating;
	}

	/**
	 * Initializes Objects defined in the properties file.
	 * @param relPath
	 * @return true if no error occured
	 */
	@Override
	public boolean initializeObjectsFromPropertiesFile(String relPath) {

		Configuration config = new FileReader().readPropertiesConfig(relPath, 
			FileReader.FILE_OPENING_TYPE.RELATIVE, FileReader.FILE_OPENING_TYPE.ABSOLUTE, true);

		if (config != null) {

			// --- candidate finder --- //

			// get categorizer first (candidate finder needs it)
			try {
				if (this.getSupportedDocumentCategorizer().containsKey(this.getDocumentCategorizerName())) {
					this.setDocumentCategorizer(this.getSupportedDocumentCategorizer().
						get(this.getDocumentCategorizerName()).getConstructor(this.getClass()).newInstance(this));
				}
			} catch(InstantiationException e) {
				logger.error(e);
			} catch(NoSuchMethodException e) {
				logger.error(e);
			} catch(IllegalAccessException e) {
				logger.error(e);
			} catch(InvocationTargetException e) {
				logger.error(e);
			}

			// get candidate finder
			if (this.getDocumentCandidateFinderNames().isEmpty()) {
				// get candidate finder names from properties file only if names not already set
				this.setDocumentCandidateFinderNames(Arrays.asList(config.
					getStringArray(WikiConstants.CONFIG_PARAM_DOCUMENT_CANDIDATE_FINDER)));
			}

			try {
				for (String candidateFinderName : this.getDocumentCandidateFinderNames()) {
					if (this.getSupportedDocumentCandidateFinder().containsKey(candidateFinderName)) {
						this.addDocumentCandidateFinder(this.getSupportedDocumentCandidateFinder().
							get(candidateFinderName).getConstructor(this.getClass()).newInstance(this));
					}
				}
			} catch(InstantiationException e) {
				logger.error(e);
			} catch(NoSuchMethodException e) {
				logger.error(e);
			} catch(IllegalAccessException e) {
				logger.error(e);
			} catch(InvocationTargetException e) {
				logger.error(e);
			}

			this.setDocumentCategorizerName(config.getString(WikiConstants.
				CONFIG_PARAM_DOCUMENT_CANDIDATE_FINDER_CATEGORIZER));

			// --- /candidate finder --- //

			// --- document scorer --- //

			this.setDocumentScorerNames(Arrays.asList(config.
				getStringArray(WikiConstants.CONFIG_PARAM_DOCUMENT_SCORER)));

			// try to create scorer objects from parameter scorer names
			try {
				for (String documentScorerName : this.getDocumentScorerNames()) {
					if (this.getSupportedDocumentScorer().containsKey(documentScorerName)) {
						this.addDocumentScorer(this.getSupportedDocumentScorer().get(documentScorerName).
							getConstructor(this.getClass()).newInstance(this));
					}
				}
			} catch(InstantiationException e) {
				logger.error(e);
			} catch(NoSuchMethodException e) {
				logger.error(e);
			} catch(IllegalAccessException e) {
				logger.error(e);
			} catch(InvocationTargetException e) {
				logger.error(e);
			}

			// --- /document scorer --- //

			// --- scoring combiner --- //

			this.setDocumentScoringCombinerNames(Arrays.asList(config.
				getStringArray(WikiConstants.CONFIG_PARAM_DOCUMENT_SCORING_COMBINER)));

			// try to create scoring combiner objects from parameter scoring combiner names
			try {
				for (String documentScoringCombinerName : this.getDocumentScoringCombinerNames()) {
					if (this.getSupportedDocumentScoringCombiner().containsKey(documentScoringCombinerName)) {
						this.addDocumentScoringCombiner(this.getSupportedDocumentScoringCombiner().get(documentScoringCombinerName).
							getConstructor(this.getClass()).newInstance(this));
					}
				}
			} catch(InstantiationException e) {
				logger.error(e);
			} catch(NoSuchMethodException e) {
				logger.error(e);
			} catch(IllegalAccessException e) {
				logger.error(e);
			} catch(InvocationTargetException e) {
				logger.error(e);
			}

			// --- /scoring combiner --- //

			return true;
		}
		return false;
	}

	/**
	 * Gets this configuration from a properties file, object initialisations are not done here,
	 * use {@link WikiLuceneDisambiguationEngineConfig#initializeObjectsFromPropertiesFile(java.lang.String)  }
	 * @return true if no error occured
	 */
	@Override
	public boolean readConfigFromPropertiesFile(String relPath) {

		Configuration config = new FileReader().readPropertiesConfig(relPath,
			FileReader.FILE_OPENING_TYPE.RELATIVE, FileReader.FILE_OPENING_TYPE.ABSOLUTE, true);

		if (config != null) {

			String[] candidateFinder_usedPatternIDs = config.getStringArray(WikiConstants.CONFIG_PARAM_USED_PATTERN);
			String[] patternRatings = config.getStringArray(WikiConstants.CONFIG_PARAM_PATTERN_RATINGS);

			// read used pattern for candidate finder with ratings
			if (candidateFinder_usedPatternIDs.length != 0 && candidateFinder_usedPatternIDs.length == patternRatings.length) {

				this.candidateFinder_allRatedQueryPattern.clear();
				int patternIndex = 0;
				for (String patternIDString : candidateFinder_usedPatternIDs) {

					// add pattern id to config
					Integer patternID = Integer.parseInt(patternIDString);
					this.candidateFinder_allQueryPatternIDs.add(patternID);

					// add pattern to list of other pattern with same rating
					if (this.candidateFinder_allRatedQueryPattern.containsKey(Float.parseFloat(patternRatings[patternIndex]))) {
						this.candidateFinder_allRatedQueryPattern.get(Float.parseFloat(patternRatings[patternIndex])).
							add(WikiConstants.PATTERN.get(patternID));
					} else
					// add new list for new rating and add pattern
					{
						List<String> pattern = new ArrayList<String>();
						pattern.add(WikiConstants.PATTERN.get(patternID));
						this.candidateFinder_allRatedQueryPattern.put(Float.parseFloat(patternRatings[patternIndex]), pattern);
					}
					patternIndex++;
				}
			} else {
				System.out.println("Error while reading config file: Number of pattern is not matching to number of ratings.");
			}

			this.candidateFinder_maxTermDocuments = config.getInteger(WikiConstants.CONFIG_PARAM_MAX_TERM_DOCUMENTS,
				WikiConstants.DEFAULT_TITLE_PATTERN_CANDIDATE_FINDER_MAX_TERM_DOCUMENTS);

			this.maxTermDocumentsPerPattern = config.getInteger(WikiConstants.CONFIG_PARAM_MAX_TERM_DOCUMENTS_PER_PATTERN,
				WikiConstants.DEFAULT_TITLE_PATTERN_CANDIDATE_FINDER_MAX_TERM_DOCUMENTS_PER_PATTERN);

			this.multimatchingSurfaceFormScore = config.getFloat(WikiConstants.CONFIG_PARAM_MULTIMATCHING_SURFACEFORM_SCORE,
				WikiConstants.DEFAULT_MULTIMATCHING_SURFACEFORM_SCORE);

			this.alternativeURIRating = config.getFloat(WikiConstants.CONFIG_PARAM_ALTERNATIVE_URI_RATING,
				WikiConstants.DEFAULT_TITLE_PATTERN_CANDIDATE_FINDER_ALTERNATIVE_URI_RATING);

			// --- class names --- //

			String[] documentCandidateFinderNames = config.getStringArray(WikiConstants.CONFIG_PARAM_DOCUMENT_CANDIDATE_FINDER);
			this.setDocumentCandidateFinderNames(Arrays.asList(documentCandidateFinderNames));

			String documentCategorizerName = config.getString(WikiConstants.CONFIG_PARAM_DOCUMENT_CANDIDATE_FINDER_CATEGORIZER);
			this.setDocumentCategorizerName(documentCategorizerName);

			String[] documentScorerNames = config.getStringArray(WikiConstants.CONFIG_PARAM_DOCUMENT_SCORER);
			this.setDocumentScorerNames(Arrays.asList(documentScorerNames));

			String[] documentScoringCombinerNames = config.getStringArray(WikiConstants.CONFIG_PARAM_DOCUMENT_SCORING_COMBINER);
			this.setDocumentScoringCombinerNames(Arrays.asList(documentScoringCombinerNames));

			// --- /class names --- //

			String[] linearScoringCombinerScorerWeightings = config.
				getStringArray(WikiConstants.CONFIG_PARAM_LINEAR_SCORING_COMBINER_SCORER_WEIGHTINGS);

			if (linearScoringCombinerScorerWeightings != null && linearScoringCombinerScorerWeightings.length != 0) {

				if (linearScoringCombinerScorerWeightings.length == documentScorerNames.length) {

					try {
						this.clearLinearScoringCombinerScorerWeightings();
						for (String scorerWeighting : linearScoringCombinerScorerWeightings) {
							this.addLinearScoringCombinerScorerWeighting(Float.parseFloat(scorerWeighting));
						}
					} catch(NumberFormatException e) {
						logger.error(e);
					}
				} else {
					logger.error("Number of document scorer is not matching to number of document scorer weightings.");
				}
			}
			return true;
		}
		return false;
	}

	/**
	 *
	 * @return candidateFinder_allQueryPatternIDs
	 */
	public List<Integer> getAllQueryPatternIDs() {
		return candidateFinder_allQueryPatternIDs;
	}

	/**
	 *
	 * @param candidateFinder_queryPatternID
	 */
	public void addQueryPatternID(int candidateFinder_queryPatternID) {
		this.candidateFinder_allQueryPatternIDs.add(candidateFinder_queryPatternID);
	}
}
