package de.bbcdaas.disambiguation.wikipedia.engine.lucene.scoringcombiner;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.disambiguation.core.impl.AbstractDocumentScoringCombiner;
import de.bbcdaas.disambiguation.core.interfaces.DocumentScorer;
import de.bbcdaas.disambiguation.core.interfaces.DocumentScoringCombiner;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiDisambiguationEngineConfig;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Assembles the combined score from the calculated scores of the documents.
 * @author Robert Illers
 */
public final class LinearScoringCombiner extends AbstractDocumentScoringCombiner <WikiDisambiguationEngineConfig> implements DocumentScoringCombiner {

	private List<Float> documentScorerWeightings;

	/**
	 * Constructor
	 * @param config
	 */
	public LinearScoringCombiner(WikiDisambiguationEngineConfig config) {
		super(config);
		this.documentScorerWeightings = config.getLinearScoringCombinerScorerWeightings();
	}

	/**
	 *
	 * @param termValuesDocuments
	 * @param documentScorer
	 */
	@Override
	protected boolean calulateCombinedScoresImpl(Map<String,List<Document>> surfaceFormsDocuments, Set<DocumentScorer> documentScorer) {

		if (documentScorer != null && !documentScorer.isEmpty()) {

			if (documentScorer.size() == this.documentScorerWeightings.size()) {
				StringBuilder combinedScoreString = new StringBuilder();
				for (String surfaceForm : surfaceFormsDocuments.keySet()) {
					for (Document document : surfaceFormsDocuments.get(surfaceForm)) {

						if (document.getType() == Document.DOCUMENT_TYPE_ARTICLE) {

							float combinedScore = 0.0f;
							List<Float> scores = document.getScores();
							if (this.getConfig().isEnableLogs()) {
								logger.info(new StringBuilder().append(document.getUri()).append(" scores: ").append(scores).toString());
							}

							combinedScoreString.setLength(0);
							combinedScoreString.append("combined Score: ");
							for (int scorerIndex = 0;scorerIndex < documentScorer.size();scorerIndex++) {

								float scoreWeighting = this.documentScorerWeightings.get(scorerIndex);
								if (scoreWeighting != 0) {

									float score = scores.get(scorerIndex);
									combinedScore += score * scoreWeighting;
									if (scorerIndex != 0) {
										combinedScoreString.append("+");
									}
									combinedScoreString.append(score).append("*").append(scoreWeighting);
								}
							}
							document.addCombinedScore(combinedScore);
							combinedScoreString.append("=").append(combinedScore);
							if (this.getConfig().isEnableLogs()) {
								logger.info(combinedScoreString.toString());
							}
						}
					}
				}
			} else {
				logger.error("Number of document scorer does not match the number of scorer weightings.");
				logger.error(this.documentScorerWeightings);
				return false;
			}
		} else {
			logger.error("Document Scorer not set properly.");
			return false;
		}
		return true;
	}

	/**
	 * Sets the active state of the scorer depending on the documentScorerWeightings parameter.
	 * @param documentScorer
	 */
	@Override
	public void setDocumentScorerActivations(Set<DocumentScorer> documentScorer) {

		if (documentScorer.size() == this.documentScorerWeightings.size()) {

			int i = 0;
			for (DocumentScorer aDocumentScorer : documentScorer) {

				if (this.documentScorerWeightings.get(i) == 0) {
					aDocumentScorer.setScorerActive(false);
				}
				i++;
			}
		} else {
			logger.error("Number of document scorer does not match the number of scorer weightings.");
			logger.error(this.documentScorerWeightings);
		}
	}
}
