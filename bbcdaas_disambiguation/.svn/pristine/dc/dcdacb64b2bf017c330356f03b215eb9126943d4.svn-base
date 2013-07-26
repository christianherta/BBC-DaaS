package de.bbcdaas.disambiguation.core.impl;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.common.util.PerformanceUtils;
import de.bbcdaas.disambiguation.core.configs.AbstractDisambiguationEngineConfig;
import de.bbcdaas.disambiguation.core.interfaces.DocumentScorer;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Base class for all implementations of document scorers.
 * @author Robert Illers
 */
public abstract class AbstractDocumentScorer <T extends AbstractDisambiguationEngineConfig> implements DocumentScorer {

	protected final Logger logger = Logger.getLogger(this.getClass());
	private boolean scorerActive = true;
    private T config;

	/**
	 * Constructor
	 * @param config the configuration that should be used
	 */
	public AbstractDocumentScorer(T config) {
        this.config = config;
	}

	/**
	 * Scores documents.
	 * @param surfaceFormsDocuments
	 * @throws ApiException
	 */
	@Override
	public final void scoreDocuments(Map<String, List<Document>> surfaceFormsDocuments) throws ApiException {

		Map<String, List<Float>> scores = null;
		PerformanceUtils pu = new PerformanceUtils();

		// do scoring
		if (this.scorerActive) {
			scores = this.scoreDocumentsImpl(surfaceFormsDocuments);
		}

		// set the scores into the documents
		for (Map.Entry<String, List<Document>> surfaceFormDocuments : surfaceFormsDocuments.entrySet()) {

			int i = 0;
			for (Document document : surfaceFormDocuments.getValue()) {

				if (scores != null && scores.get(surfaceFormDocuments.getKey()) != null &&
					scores.get(surfaceFormDocuments.getKey()).size() == surfaceFormDocuments.getValue().size()) {

					document.addScore(scores.get(surfaceFormDocuments.getKey()).get(i));
				} else {
					if (this.scorerActive) {
						logger.error("Inconsistent scores result map. Check scorer outputs.");
					}
					document.addScore(0.0f);
				}
				i++;
			}
		}
		logger.info("needed time: "+pu.getRunningTimer());
	}

	/**
	 *
	 * @param surfaceFormsDocuments
	 * @return
	 * @throws ApiException
	 */
	protected abstract Map<String, List<Float>> scoreDocumentsImpl(Map<String, List<Document>> surfaceFormsDocuments) throws ApiException;

	/**
	 * Prepares the document for the scorer.
	 * @param surfaceFormsDocuments
	 * @throws ApiException
	 */
	protected abstract void prepare(Map<String, List<Document>> surfaceFormsDocuments) throws ApiException;

	/**
	 * Set if this scorer should be executed.
	 * @param scorerActive
	 */
	@Override
	public void setScorerActive(boolean scorerActive) {
		this.scorerActive = scorerActive;
	}

    /**
	 * Gets the configuration for the disambiguation engine.
	 * @return an implementation of AbstractDisambiguationEngineConfig
	 */
	protected T getConfig() {
        return config;
    }
}