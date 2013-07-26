package de.bbcdaas.disambiguation.wikipedia.engine.lucene;

import de.bbcdaas.disambiguation.core.impl.AbstractDocumentCandidateFinder;
import de.bbcdaas.disambiguation.core.impl.AbstractDocumentCategorizer;
import de.bbcdaas.disambiguation.core.impl.AbstractDocumentScorer;
import de.bbcdaas.disambiguation.core.impl.AbstractDocumentScoringCombiner;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.candidates.TitlePatternCandidateFinder;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.candidates.TitlePatternCandidateFinder2;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.categorizer.ContentFieldDocumentCategorizer;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.scoring.CandidateRatingDocumentScorer;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.scoring.CosSimilarityDocumentScorer;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.scoring.KeywordDocumentScorer;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.scoring.MultimatchingSurfaceFormDocumentScorer;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.scoring.TermVectorDocumentScorer;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.scoringcombiner.LinearScoringCombiner;
import java.util.*;

/**
 * Bean containing constants and configs for the wikipedia dump related disambiguation
 * @author Robert Illers
 */
public final class WikiConstants {

	private WikiConstants() {}

	public static final String LUCENE_INDEX_PATH = "/var/wikipedia/index";

	public static final String DATA_FOLDER_NAME = "data";
	public static final String DATA_EVALUATION_SUBFOLDER_NAME = "evaluation";

	public static final String DATA_EVALUATION_GOLDSTANDARD_FILE_NAME = "goldstandard.xml";

	// --- configuration file constants --- //

	public static final String CONFIGURATION_CONFIG_FOLDER_NAME = "properties";
	public static final String DISAMBIGUATION_CONFIGURATION_FILE_NAME = "wikiLuceneDisambiguation.properties";
	public static final String EVALUATION_CONFIGURATION_FILE_NAME = "evaluation.properties";

	// candidate finder
	public static final String CONFIG_PARAM_DOCUMENT_CANDIDATE_FINDER = "candidateFinder";
	public static final String CONFIG_PARAM_USED_PATTERN = "titlePatternCandidateFinder_pattern";
	public static final String CONFIG_PARAM_PATTERN_RATINGS = "titlePatternCandidateFinder_patternRatings";
	public static final String CONFIG_PARAM_MAX_TERM_DOCUMENTS = "titlePatternCandidateFinder__maxTermDocuments";
	public static final String CONFIG_PARAM_MAX_TERM_DOCUMENTS_PER_PATTERN = "titlePatternCandidateFinder_maxTermDocumentsPerPattern";
	public static final String CONFIG_PARAM_ALTERNATIVE_URI_RATING = "titlePatternCandidateFinder_alternativeURIRating";
	public static final String CONFIG_PARAM_DOCUMENT_CANDIDATE_FINDER_CATEGORIZER = "candidateFinder_documentCategorizer";

	public static final int DEFAULT_TITLE_PATTERN_CANDIDATE_FINDER_MAX_TERM_DOCUMENTS = 20;
	public static final int DEFAULT_TITLE_PATTERN_CANDIDATE_FINDER_MAX_TERM_DOCUMENTS_PER_PATTERN = 10;
	public static final float DEFAULT_TITLE_PATTERN_CANDIDATE_FINDER_ALTERNATIVE_URI_RATING = 0.9f;

	// document scorer
	public static final String CONFIG_PARAM_DOCUMENT_SCORER = "documentScorer";
	public static final String CONFIG_PARAM_MULTIMATCHING_SURFACEFORM_SCORE = "multimatchingSurfaceFormDocumentScorer_multimatchingSurfaceFormScore";

	public static final float DEFAULT_MULTIMATCHING_SURFACEFORM_SCORE = 0.5f;

	// scoring combiner
	public static final String CONFIG_PARAM_DOCUMENT_SCORING_COMBINER =  "documentScoringCombiner";
	public static final String CONFIG_PARAM_LINEAR_SCORING_COMBINER_SCORER_WEIGHTINGS = "linearScoringCombiner_documentScorerWeightings";

	// --- /configuration file constants --- //

	// --- supported component classes --- //

	// candidate finder
	public static final Map<String, Class<? extends AbstractDocumentCandidateFinder>>  SUPPORTED_CANDIDATE_FINDER = getSupportedCandidateFinder();
	private static Map<String, Class<? extends AbstractDocumentCandidateFinder>> getSupportedCandidateFinder() {

		Map<String, Class<? extends AbstractDocumentCandidateFinder>> supportedCandidateFinder = new HashMap<String, Class<? extends AbstractDocumentCandidateFinder>>();
		supportedCandidateFinder.put(TitlePatternCandidateFinder.class.getSimpleName(), TitlePatternCandidateFinder.class);
        supportedCandidateFinder.put(TitlePatternCandidateFinder2.class.getSimpleName(), TitlePatternCandidateFinder2.class);
		return supportedCandidateFinder;
	}

	// document categorizer
	public static final Map<String, Class<? extends AbstractDocumentCategorizer>>  SUPPORTED_DOCUMENT_CATEGORIZER = getSupportedDocumentCategorizer();
	private static Map<String, Class<? extends AbstractDocumentCategorizer>> getSupportedDocumentCategorizer() {

		Map<String, Class<? extends AbstractDocumentCategorizer>> supportedDocumentCategorizer = new HashMap<String, Class<? extends AbstractDocumentCategorizer>>();
		supportedDocumentCategorizer.put(ContentFieldDocumentCategorizer.class.getSimpleName(), ContentFieldDocumentCategorizer.class);
		return supportedDocumentCategorizer;
	}

	// document scorer
	public static final Map<String, Class<? extends AbstractDocumentScorer>> SUPPORTED_DOCUMENT_SCORER = getSupportedDocumentScorer();
	private static Map<String, Class<? extends AbstractDocumentScorer>> getSupportedDocumentScorer() {

		Map<String, Class<? extends AbstractDocumentScorer>> supportedDocumentScorer = new HashMap<String, Class<? extends AbstractDocumentScorer>>();
		supportedDocumentScorer.put(KeywordDocumentScorer.class.getSimpleName(), KeywordDocumentScorer.class);
		supportedDocumentScorer.put(TermVectorDocumentScorer.class.getSimpleName(), TermVectorDocumentScorer.class);
		supportedDocumentScorer.put(CandidateRatingDocumentScorer.class.getSimpleName(), CandidateRatingDocumentScorer.class);
		supportedDocumentScorer.put(MultimatchingSurfaceFormDocumentScorer.class.getSimpleName(), MultimatchingSurfaceFormDocumentScorer.class);
        supportedDocumentScorer.put(CosSimilarityDocumentScorer.class.getSimpleName(), CosSimilarityDocumentScorer.class);
		return supportedDocumentScorer;
	}

	// scoring combiner
	public static final Map<String, Class<? extends AbstractDocumentScoringCombiner>> SUPPORTED__SCORING_COMBINER = getSupportedScoringCombiner();
	private static Map<String, Class<? extends AbstractDocumentScoringCombiner>> getSupportedScoringCombiner() {

		Map<String, Class<? extends AbstractDocumentScoringCombiner>> supportedScoringCombiner = new HashMap<String, Class<? extends AbstractDocumentScoringCombiner>>();
		supportedScoringCombiner.put(LinearScoringCombiner.class.getSimpleName(), LinearScoringCombiner.class);
		return supportedScoringCombiner;
	}

	// --- /supported component classes --- //

	// --- constant pattern parts --- //

	public static final String PATTERN_PART_NOTHING = "<_>";
	public static final String PATTERN_PART_TERMVALUE = "<termValue>";

	// --- /constant pattern parts --- //

	// --- possible pattern --- //

	public static final int PATTERNID_SINGLE_TERM = 1;
	public static final int PATTERNID_TERM_WITH_BRACKETS = 2;
	public static final int PATTERNID_TERM_AS_FIRST_WORD = 3;
	public static final int PATTERNID_TERM_AS_FIRST_WORD_WITH_DASH = 4;
	public static final int PATTERNID_TERM_AS_LAST_WORD = 5;
	public static final int PATTERNID_TERM_AS_LAST_WORD_WITH_DASH = 6;
	public static final int PATTERNID_TERM_AS_LAST_PART_OF_A_WORD = 7;
	public static final int PATTERNID_TERM_AS_SINGLE_WORD_BETWEEN_WORDS = 8;
	public static final int PATTERNID_TERM_AS_SINGLE_WORD_BETWEEN_WORDS_WITH_DASH = 9;
	public static final int PATTERNID_TERM_IN_THE_MIDDLE_OF_A_WORD = 10;

	// "<_><termValue><_>"
    public static final String PATTERN_SINGLE_TERM = new StringBuilder().
		append(PATTERN_PART_NOTHING).append(PATTERN_PART_TERMVALUE).append(PATTERN_PART_NOTHING).toString();

	// "<_><TermValue> (*)"
    public static final String PATTERN_TERM_WITH_BRACKETS = new StringBuilder().
		append(PATTERN_PART_NOTHING).append(PATTERN_PART_TERMVALUE).append(" (*)").toString();

	// "<_><termValue> *"
    public static final String PATTERN_TERM_AS_FIRST_WORD = new StringBuilder().
		append(PATTERN_PART_NOTHING).append(PATTERN_PART_TERMVALUE).append(" *").toString();

	// "<_><termValue>-*"
    public static final String PATTERN_TERM_AS_FIRST_WORD_WITH_DASH = new StringBuilder().
		append(PATTERN_PART_NOTHING).append(PATTERN_PART_TERMVALUE).append("-*").toString();

	// "* <termValue><_>"
    public static final String PATTERN_TERM_AS_LAST_WORD = new StringBuilder().
		append("* ").append(PATTERN_PART_TERMVALUE).append(PATTERN_PART_NOTHING).toString();

	// "*-<termValue><_>"
    public static final String PATTERN_TERM_AS_LAST_WORD_WITH_DASH = new StringBuilder().
		append("*-").append(PATTERN_PART_TERMVALUE).append(PATTERN_PART_NOTHING).toString();

	// "*<termValue><_>"
    public static final String PATTERN_TERM_AS_LAST_PART_OF_A_WORD = new StringBuilder().
		append("*").append(PATTERN_PART_TERMVALUE).append(PATTERN_PART_NOTHING).toString();

	// "* <termValue> *"
    public static final String PATTERN_TERM_AS_SINGLE_WORD_BETWEEN_WORDS = new StringBuilder().
		append("* ").append(PATTERN_PART_TERMVALUE).append(" *").toString();

	// "*-<TermValue>-*"
    public static final String PATTERN_TERM_AS_SINGLE_WORD_BETWEEN_WORDS_WITH_DASH = new StringBuilder().
		append("*-").append(PATTERN_PART_TERMVALUE).append("-*").toString();

	// "*<termValue>*"
    public static final String PATTERN_TERM_IN_THE_MIDDLE_OF_A_WORD = new StringBuilder().
		append("*").append(PATTERN_PART_TERMVALUE).append("*").toString();

	public static final Map<Integer, String> PATTERN = getPattern();
	private static Map<Integer, String> getPattern() {

		Map<Integer, String> patternMap = new HashMap<Integer, String>();
		patternMap.put(PATTERNID_SINGLE_TERM, PATTERN_SINGLE_TERM);
		patternMap.put(PATTERNID_TERM_WITH_BRACKETS, PATTERN_TERM_WITH_BRACKETS);
		patternMap.put(PATTERNID_TERM_AS_FIRST_WORD, PATTERN_TERM_AS_FIRST_WORD);
		patternMap.put(PATTERNID_TERM_AS_FIRST_WORD_WITH_DASH, PATTERN_TERM_AS_FIRST_WORD_WITH_DASH);
		patternMap.put(PATTERNID_TERM_AS_LAST_WORD, PATTERN_TERM_AS_LAST_WORD);
		patternMap.put(PATTERNID_TERM_AS_LAST_WORD_WITH_DASH, PATTERN_TERM_AS_LAST_WORD_WITH_DASH);
		patternMap.put(PATTERNID_TERM_AS_LAST_PART_OF_A_WORD, PATTERN_TERM_AS_LAST_PART_OF_A_WORD);
		patternMap.put(PATTERNID_TERM_AS_SINGLE_WORD_BETWEEN_WORDS, PATTERN_TERM_AS_SINGLE_WORD_BETWEEN_WORDS);
		patternMap.put(PATTERNID_TERM_AS_SINGLE_WORD_BETWEEN_WORDS_WITH_DASH, PATTERN_TERM_AS_SINGLE_WORD_BETWEEN_WORDS_WITH_DASH);
		patternMap.put(PATTERNID_TERM_IN_THE_MIDDLE_OF_A_WORD, PATTERN_TERM_IN_THE_MIDDLE_OF_A_WORD);
		return patternMap;
	}

	// --- /possible pattern --- //

	// --- default pattern config --- //

	public static final Map<Float, List<String>> DEFAULT_PATTERN_CONFIG = getDefaultPatternConfig();
	private static Map<Float, List<String>> getDefaultPatternConfig() {

		// important: reverse order of the map for using pattern with highest rating first!
		Map<Float, List<String>> allRatedQueryPattern = new TreeMap<Float, List<String>>(Collections.reverseOrder());

		// rating1_0 pattern
		List<String> rating1_0_pattern = new ArrayList<String>();
		rating1_0_pattern.add(PATTERN_SINGLE_TERM);
		rating1_0_pattern.add(PATTERN_TERM_WITH_BRACKETS);
		allRatedQueryPattern.put(1.0f, rating1_0_pattern);

		// rating0_9 pattern
		List<String> rating0_9_pattern = new ArrayList<String>();
		allRatedQueryPattern.put(0.9f, rating0_9_pattern);

		// rating0_6 pattern
		List<String> rating0_6_pattern = new ArrayList<String>();
		rating0_6_pattern.add(PATTERN_TERM_AS_FIRST_WORD);
		rating0_6_pattern.add(PATTERN_TERM_AS_FIRST_WORD_WITH_DASH);
		allRatedQueryPattern.put(0.6f, rating0_6_pattern);

		// rating0_5 pattern
		List<String> rating0_5_pattern = new ArrayList<String>();
		rating0_5_pattern.add(PATTERN_TERM_AS_SINGLE_WORD_BETWEEN_WORDS);
		rating0_5_pattern.add(PATTERN_TERM_AS_LAST_WORD);
		rating0_5_pattern.add(PATTERN_TERM_AS_LAST_WORD_WITH_DASH);
		allRatedQueryPattern.put(0.5f, rating0_5_pattern);

		// rating0_4 pattern
		List<String> rating0_4_pattern = new ArrayList<String>();
		rating0_4_pattern.add(PATTERN_TERM_AS_SINGLE_WORD_BETWEEN_WORDS_WITH_DASH);
		allRatedQueryPattern.put(0.4f, rating0_4_pattern);

		// rating0_1 pattern
		List<String> rating0_1_pattern = new ArrayList<String>();
		rating0_4_pattern.add(PATTERN_TERM_AS_LAST_PART_OF_A_WORD);
		rating0_1_pattern.add(PATTERN_TERM_IN_THE_MIDDLE_OF_A_WORD);
		allRatedQueryPattern.put(0.1f, rating0_1_pattern);

		return allRatedQueryPattern;
	}

	// --- /default pattern config --- //

	public static final int DEFAULT_URL_SEARCH_MAX_HITS = 10;
}
