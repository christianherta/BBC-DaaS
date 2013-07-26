package de.bbcdaas.disambiguation.evaluation.wikipedia;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.disambiguation.core.connector.lucene.LuceneConnector;
import de.bbcdaas.disambiguation.core.interfaces.DocumentScorer;
import de.bbcdaas.disambiguation.evaluation.core.AbstractEvaluator;
import de.bbcdaas.disambiguation.evaluation.core.GoldStandard;
import de.bbcdaas.disambiguation.evaluation.core.Tag;
import de.bbcdaas.disambiguation.evaluation.core.TagCloud;
import de.bbcdaas.disambiguation.evaluation.core.VarParameter;
import de.bbcdaas.disambiguation.wikipedia.dataimport.lucene.WikiContentHandler;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiConstants;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiDisambiguationEngine;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiDisambiguationEngineConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.util.Version;

/**
 * Evaluator for the wikipedia disambiguation using an wikipedia xml dump via lucene.
 * @author Robert Illers
 */
public class WikiDisambiguationEngineEvaluator extends AbstractEvaluator<WikiDisambiguationEngineConfig> {

	// tagClouds containg URIs that are expected by disambiguation process
	private GoldStandard goldStandard;
	// path to the xml file containing the goldstandard
	private String goldStandardInputFilePath;
	// evaluation result object that will be marshalled.
	private WikiDisambiguationEvaluationResult result;

	/**
	 * Constructor
	 * @param goldStandardInputFilePath path to the xml file containing the goldstandard
	 * @param xmlOutputPath path to xml file where the evaluation result should be written to
	 */
	public WikiDisambiguationEngineEvaluator(String goldStandardInputFilePath, String xmlOutputPath) {

		super(xmlOutputPath);
		this.goldStandardInputFilePath = goldStandardInputFilePath;
		logger.info("input file: "+this.goldStandardInputFilePath);
		this.goldStandard = this.initializeTagCloudGoldStandard();
	}

	/**
	 * Starts the evaluation of the wikipedia disambiguation engine.
	 */
	@Override
	public WikiDisambiguationEvaluationResult evaluate() {

		this.result = new WikiDisambiguationEvaluationResult(this.xmlOutputPath);
		// variable parameters has to been added before
		this.result.setVarParameters(this.getVarParameters());

		if (this.goldStandard == null) {
			logger.error("TagCloudGoldStandard could not be initialized from xml file.");
		} else {

			// initialize lucene connector
			Version version = Version.LUCENE_35;
			String indexPath = WikiConstants.LUCENE_INDEX_PATH;
			Analyzer standardAnalyzer = new GermanAnalyzer(version);
			Map<String, Analyzer> fieldAnalyzer = new HashMap<String, Analyzer>();
			LuceneConnector connector = new LuceneConnector(version, indexPath, standardAnalyzer, fieldAnalyzer, 0);
			connector.setEnableLogs(false);

			// build disambiguation engine configuration
			WikiDisambiguationEngineConfig config = new WikiDisambiguationEngineConfig(connector);
			String relPath = new StringBuilder(WikiConstants.CONFIGURATION_CONFIG_FOLDER_NAME).
				append(File.separator).append(WikiConstants.DISAMBIGUATION_CONFIGURATION_FILE_NAME).toString();
			config.setUriPrefix("http://de.wikipedia.org/wiki/");
			config.setEnableLogs(false);
			// get default configuration from serverside properties file
			config.readConfigFromPropertiesFile(relPath);
			config.initializeObjectsFromPropertiesFile(relPath);

			// initialize disambiguation engine
			WikiDisambiguationEngine engine = new WikiDisambiguationEngine(config);

			// complete result configuration with used disambiguation components
			this.result.setUsedCandidateFinder(Arrays.asList(config.getDocumentCandidateFinderNames().toArray(new String[0])));
			this.result.getUsedCategorizer().add(config.getDocumentCategorizerName());
			this.result.setUsedScorer(Arrays.asList(config.getDocumentScorerNames().toArray(new String[0])));
			this.result.setUsedScoringCombiner(Arrays.asList(config.getDocumentScoringCombinerNames().toArray(new String[0])));

			// build tag cloud output for result object
			int tagCloudIndex = 0;
			for (TagCloud tagCloud : this.goldStandard.getClouds()) {

				TagCloud resultTagCloud = this.result.addTagCloud(new TagCloud());
				resultTagCloud.setTagCloudIndex(tagCloudIndex);

				for (Tag tag : tagCloud.getTags()) {

					Tag resultTag = new Tag();
					resultTag.setName(tag.getName());
					resultTag.setExpectedUri(tag.getExpectedUri());
					resultTagCloud.addTag(resultTag);
				}
				tagCloudIndex++;
			}

			/* execute evaluation with variable parameters */
			int stepIndex = 1;
			int evalStepAmount = this.calculateEvaluationStepAmount();
			logger.info("Number of neccessary evaluation steps: "+evalStepAmount);
			do {

				logger.info("-----------------------");
				logger.info("Evaluation Step: "+stepIndex+"/"+evalStepAmount);
				logger.info("-----------------------");
				// update variable parameters
				this.setVarParameterIntoConfig(config);
				// do the disambiguation with the current parameters
				if (!this.testRun) {
					this.doEvaluationStep(engine, stepIndex);
				}
				stepIndex++;

			} while(this.applyParameterSteps());
			/* /execute evaluation with variable parameters */

			try {
				config.getConnector().closeConnection();
			} catch (ApiException ex) {
				logger.error(ex);
			}

		}
		return result;
	}

	/**
	 * Here the supported variable parameters are updated into the configuration
	 * for each step. Only parameters supported here can be made variable.
	 * @param config configuration containing the parameters
	 */
	@Override
	protected void setVarParameterIntoConfig(WikiDisambiguationEngineConfig config) {

		/* ---------- pattern ratings ---------- */
		Map<Float, List<String>> candidateFinder_allRatedQueryPattern = config.getAllRatedQueryPattern();
		Map<Float, List<String>> newAllRatedQueryPattern = new TreeMap<Float, List<String>>(Collections.reverseOrder());

		for (Integer patternID : config.getAllQueryPatternIDs()) {

			String pattern = WikiConstants.PATTERN.get(patternID);
			Float patternRating = 0f;

			VarParameter param_patternRating = this.getVarParameterByName(
				new StringBuilder(WikiConstants.CONFIG_PARAM_PATTERN_RATINGS).append("_").append(patternID).toString()) ;

			// variable pattern rating found
			if (param_patternRating != null) {
				patternRating = Float.parseFloat(param_patternRating.getValue());
			} else {

				// get old pattern rating from config
				for (Map.Entry<Float, List<String>> entry : candidateFinder_allRatedQueryPattern.entrySet()) {
					if (entry.getValue().contains(pattern)) {
						patternRating = entry.getKey();
						break;
					}
				}
			}

			// rebuild allRatedQueryPattern
			if (!newAllRatedQueryPattern.containsKey(patternRating)) {
				newAllRatedQueryPattern.put(patternRating, new ArrayList<String>());
			}
			newAllRatedQueryPattern.get(patternRating).add(pattern);

		}
		config.setAllRatedQueryPattern(newAllRatedQueryPattern);
		/* ---------- /pattern ratings ---------- */

		/* ---------- document scorer weightings ---------- */
		List<Float> scorerWeightings = new ArrayList<Float>(config.getLinearScoringCombinerScorerWeightings());
		config.clearLinearScoringCombinerScorerWeightings();

		for (int scorerIndex = 1; scorerIndex <= config.getDocumentScorer().size(); scorerIndex++) {

			VarParameter param_linearScoringCombiner_scorerWeighting = this.getVarParameterByName(
				new StringBuilder(WikiConstants.CONFIG_PARAM_LINEAR_SCORING_COMBINER_SCORER_WEIGHTINGS).append("_").append(scorerIndex).toString()) ;

			if (param_linearScoringCombiner_scorerWeighting != null) {
				config.addLinearScoringCombinerScorerWeighting(Float.parseFloat(param_linearScoringCombiner_scorerWeighting.getValue()));
			} else {
				config.addLinearScoringCombinerScorerWeighting(scorerWeightings.get(scorerIndex-1));
			}
		}
		/* ---------- /document scorer weightings ---------- */

		// multimatching surface form score
		VarParameter param_multiMatchingSurfaceForm = this.getVarParameterByName(WikiConstants.
				CONFIG_PARAM_MULTIMATCHING_SURFACEFORM_SCORE) ;
		if (param_multiMatchingSurfaceForm != null) {
			config.setMultimatchingSurfaceFormScore(Float.parseFloat(param_multiMatchingSurfaceForm.getValue()));
		}

		// alternative URI rating
		VarParameter param_alternativeUriRating = this.getVarParameterByName(WikiConstants.
				CONFIG_PARAM_ALTERNATIVE_URI_RATING) ;
		if (param_alternativeUriRating != null) {
			config.setAlternativeURIRating(Float.parseFloat(param_alternativeUriRating.getValue()));
		}

		// max term documents
		VarParameter param_maxTermDocuments = this.getVarParameterByName(WikiConstants.CONFIG_PARAM_MAX_TERM_DOCUMENTS);
		if (param_maxTermDocuments != null) {
			config.setMaxTermDocuments(Integer.parseInt(param_maxTermDocuments.getValue()));
		}

		// max term documents per pattern
		VarParameter param_maxTermDocumentsPerPattern = this.getVarParameterByName(WikiConstants.CONFIG_PARAM_MAX_TERM_DOCUMENTS_PER_PATTERN);
		if (param_maxTermDocumentsPerPattern != null) {
			config.setMaxTermDocumentsPerPattern(Integer.parseInt(param_maxTermDocumentsPerPattern.getValue()));
		}
	}

	/**
	 * Executes the disambiguation with the current parameters
	 * @param engine disambiguation engine
	 * @param stepIndex index of the evaluation step
	 */
	private void doEvaluationStep(WikiDisambiguationEngine engine, int stepIndex) {

		WikiDisambiguationEvaluationStepResult stepResult = new WikiDisambiguationEvaluationStepResult();
		stepResult.setStepIndex(stepIndex);

		int tagCloudIndex = 0;
		int numberOfAllExpectedUriMatches = 0;
		int numberOfAllTags = 0;

		// for each tag cloud of the goldstandard
		for (TagCloud tagCloud : this.goldStandard.getClouds()) {

			TagCloud resultTagCloud = stepResult.addTagCloud(new TagCloud());
			resultTagCloud.setTagCloudIndex(tagCloudIndex);

			List<String> tagNames = new ArrayList<String>();

			for (Tag tag : tagCloud.getTags()) {

				tagNames.add(tag.getName());
				Tag resultTag = new Tag();
				resultTag.setName(tag.getName());
				resultTagCloud.addTag(resultTag);
			}

			logger.info("======================");
			logger.info("Input tags: "+tagNames.toString());

			// do the disambiguation
			Map<String, List<Document>> engineResult = engine.scoreDocuments(tagNames, true);

			// Map<TagName, Map<Score, List<DocumentUri>>
			Map<String, Map<Float, List<String>>> documentUrisSortedByScore = new HashMap<String, Map<Float, List<String>>>();
			int numberOfExpectedUrisMatches = 0;
			int numberOfNearByUriMatches = 0;

			// for each tag of the tag cloud
			for (String tagName : engineResult.keySet()) {

				if (!documentUrisSortedByScore.containsKey(tagName)) {
					documentUrisSortedByScore.put(tagName, new TreeMap<Float, List<String>>(Collections.reverseOrder()));
				}
				List<Document> documents = engineResult.get(tagName);
				Collections.sort(documents);

				// for each document found for the current tag (candidates)
				for (Document document : documents) {

					if (!documentUrisSortedByScore.get(tagName).containsKey(document.getCombinedScore())) {
						documentUrisSortedByScore.get(tagName).put(document.getCombinedScore(), new ArrayList<String>());
					}

					// get document title and build documents uri from it
					String title = document.getFieldByName(WikiContentHandler.FIELD_TITLE).getValue();
					documentUrisSortedByScore.get(tagName).get(document.getCombinedScore()).
						add(new StringBuilder(engine.getConfig().getUriPrefix()).append(title).toString());
				}
				// end for each document found for the current tag (candidates)

				if (!documentUrisSortedByScore.get(tagName).isEmpty()) {

					// first score of sorted map is the best score
					Float bestScore = documentUrisSortedByScore.get(tagName).keySet().toArray(new Float[0])[0];
					List<String> bestResultUris = documentUrisSortedByScore.get(tagName).get(bestScore);

					Tag resultTag = resultTagCloud.getTag(tagName);
					logger.info("Best results for tag '"+tagName+"' with score '"+bestScore+"':");

					for (String resultUri : bestResultUris) {

						logger.info(resultUri);
						resultTag.addBestResultUri(resultUri);
					}

					// compare result with expected results
					for (Tag tag : tagCloud.getTags()) {

						if (tagName.equals(tag.getName())) {

							/* --- check expected uri --- */
							String expectedUri = tag.getExpectedUri().toLowerCase();
							boolean expectedUriMatches = documentUrisSortedByScore.get(tagName).get(bestScore).contains(expectedUri);
							if (expectedUriMatches) {
								numberOfExpectedUrisMatches++;
							}
							logger.info("Expected Result: '"+expectedUri+"', Match: "+expectedUriMatches);
							resultTag.setExpectedUriMatches(expectedUriMatches);
							/* --- /check expected uri --- */

							/* --- check nearBy uris --- */
							if (!expectedUriMatches) {
								List<String> nearByUris = new ArrayList<String>();
								for (String nearByUri : tag.getNearByUris()) {
									nearByUris.add(nearByUri.toLowerCase());
								}
								boolean nearByUriMatches = false;
								for (String nearByUri : nearByUris) {

									// TODO: check for all scores?
									if (documentUrisSortedByScore.get(tagName).get(bestScore).contains(nearByUri)) {

										nearByUriMatches = true;
										numberOfNearByUriMatches++;
										logger.info("nearByUri match: '"+nearByUri+"'");
										break;
									}
								}
								resultTag.setNearByUriMatches(nearByUriMatches);
							}
							/* --- /check nearBy uris --- */

							break;
						}
					}
				}
			}
			// end for each tag of the tag cloud
			numberOfAllExpectedUriMatches += numberOfExpectedUrisMatches;
			int tagCloudSize = engineResult.keySet().size();
			numberOfAllTags += tagCloudSize;
			Integer cloudPrecision = (int)((double)((float)numberOfExpectedUrisMatches / (float)tagCloudSize)*100);
			resultTagCloud.setCloudPrecision(cloudPrecision);
			resultTagCloud.setNearByUriHits(numberOfNearByUriMatches);
			logger.info("Cloud precision: "+cloudPrecision+"%");
			tagCloudIndex++;
		}
		// end for each tag cloud of the goldstandard
		Integer stepPrecision = (int)((double)((float)numberOfAllExpectedUriMatches / (float)numberOfAllTags)*100);
		stepResult.setStepPrecision(stepPrecision);
		logger.info("Step precision: "+stepPrecision+"%");
		this.result.addStepResult(stepResult);
	}

	/**
	 * Transform goldstandard xml file into an object.
	 * @return GoldStandard
	 */
	private GoldStandard initializeTagCloudGoldStandard() {

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(GoldStandard.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			File inputFile = new File(this.goldStandardInputFilePath);
			//InputStream input = getClass().getClassLoader().getResourceAsStream(this.goldStandardInputFilePath);
			InputStream input = new FileInputStream(inputFile);
			return (GoldStandard)unmarshaller.unmarshal(input);
		} catch (JAXBException ex) {
			logger.error(ex);
		} catch(FileNotFoundException ex) {
			logger.error(ex);
		}
		return null;
	}
}
