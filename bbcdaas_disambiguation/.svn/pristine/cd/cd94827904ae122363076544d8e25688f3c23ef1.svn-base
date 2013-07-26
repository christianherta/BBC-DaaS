package de.bbcdaas.disambiguation.wikipedia.engine.lucene.scoring;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.disambiguation.core.impl.AbstractDocumentScorer;
import de.bbcdaas.disambiguation.core.interfaces.DocumentScorer;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiDisambiguationEngineConfig;
import de.bbcdaas.disambiguation.wikipedia.dataimport.lucene.WikiContentHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calculates a score from the candidateRatings of the document
 * @author Robert Illers
 */
public final class CandidateRatingDocumentScorer extends AbstractDocumentScorer <WikiDisambiguationEngineConfig> implements DocumentScorer {

	/**
	 * Constructor
	 * @param config
	 */
	public CandidateRatingDocumentScorer(WikiDisambiguationEngineConfig config) {
		super(config);
	}

	/**
	 *
	 * @param surfaceFormsDocuments
	 * @throws ApiException
	 */
	@Override
	public Map<String, List<Float>> scoreDocumentsImpl(Map<String, List<Document>> surfaceFormsDocuments) throws ApiException {

		if (this.getConfig().isEnableLogs()) {
			logger.info("---CandidateRatingDocumentScorer:---");
		}
		this.prepare(surfaceFormsDocuments);

		// scores as map (will be set to documents in abstract scorer)
		Map<String, List<Float>> scores = new HashMap<String, List<Float>>();
		for (String surfaceForm : surfaceFormsDocuments.keySet()) {
			scores.put(surfaceForm, new ArrayList<Float>());
		}

		for (Map.Entry<String, List<Document>> surfaceFormDocuments : surfaceFormsDocuments.entrySet()) {

			for (Document document : surfaceFormDocuments.getValue()) {

				// the score is calculated from the average value of the candidateRatings
				float combinedCandidateRating = 0.0f;
				for (float candidateRating : document.getCandidateRatings().values()) {
					combinedCandidateRating += candidateRating;
				}

				float score = combinedCandidateRating / (float)document.getCandidateRatings().size();
				if (this.getConfig().isEnableLogs()) {
					logger.info(new StringBuilder("Added Candidate rating Score '").append(score).append("' to URI '").
						append(document.getFieldByName(WikiContentHandler.FIELD_URI).getValue()).append("'.").toString());
				}
				scores.get(surfaceFormDocuments.getKey()).add(score);
			}
		}
		return scores;
	}

	/**
	 *
	 * @param surfaceFormsDocuments
	 * @throws ApiException
	 */
	@Override
	protected void prepare(Map<String, List<Document>> surfaceFormsDocuments) throws ApiException {}

}
