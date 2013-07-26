package de.bbcdaas.disambiguation.core.impl;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.util.PerformanceUtils;
import de.bbcdaas.disambiguation.core.configs.AbstractDisambiguationEngineConfig;
import de.bbcdaas.disambiguation.core.interfaces.DocumentScorer;
import de.bbcdaas.disambiguation.core.interfaces.DocumentScoringCombiner;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * Base class for all document scoring combiner implementations.
 * @author Robert Illers
 */
public abstract class AbstractDocumentScoringCombiner <T extends AbstractDisambiguationEngineConfig> implements DocumentScoringCombiner {

	protected final Logger logger = Logger.getLogger(this.getClass());
    private T config;

	/**
	 * Constructor
	 * @param config the configuration that should be used
	 */
	public AbstractDocumentScoringCombiner(T config) {
        this.config = config;
	}

	/**
	 *
	 * @param surfaceNamesDocuments
	 * @param documentScorer
	 */
	@Override
	public final boolean calulateCombinedScores(Map<String, List<Document>> surfaceNamesDocuments, Set<DocumentScorer> documentScorer) {

		PerformanceUtils pu = new PerformanceUtils();
		boolean noError = this.calulateCombinedScoresImpl(surfaceNamesDocuments, documentScorer);
		logger.info("needed time: "+pu.getRunningTimer());
		return noError;
	}

	/**
	 *
	 * @param surfaceNamesDocuments
	 * @param documentScorer
	 * @return true if no known error occured
	 */
	protected abstract boolean calulateCombinedScoresImpl(Map<String, List<Document>> surfaceNamesDocuments, Set<DocumentScorer> documentScorer);

	public T getConfig() {
		return this.config;
	}
}
