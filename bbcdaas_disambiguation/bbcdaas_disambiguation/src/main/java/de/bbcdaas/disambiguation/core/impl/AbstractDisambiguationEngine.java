package de.bbcdaas.disambiguation.core.impl;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.util.PerformanceUtils;
import de.bbcdaas.disambiguation.core.configs.AbstractDisambiguationEngineConfig;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Base class for all disambiguation engine implementations.
 * @author Robert Illers
 */
public abstract class AbstractDisambiguationEngine <T extends AbstractDisambiguationEngineConfig>  {

	protected final Logger logger = Logger.getLogger(this.getClass());
	private T config;

	/**
	* Constructor
	* @param config the configuration that should be used
	*/
    public AbstractDisambiguationEngine(T config) {
        this.config = config;
    }

	/**
	 *
	 * @param surfaceForms
	 * @return
	 */
	public final Map<String, List<Document>> scoreDocuments(List<String> surfaceForms, boolean keepConnection) {

		PerformanceUtils pu = new PerformanceUtils();
		Map<String, List<Document>> scoredDocuments = this.scoreDocumentsImpl(surfaceForms, keepConnection);
		logger.info("needed time: "+pu.getRunningTimer());
		return scoredDocuments;
	}

	/**
	 *
	 * @param surfaceForms
	 * @return
	 */
	protected abstract Map<String, List<Document>> scoreDocumentsImpl(List<String> surfaceForms, boolean keepConnection);

	/**
	 *
	 * @param surfaceForm
	 * @param maxHits
	 * @return
	 */
	public final List<Document> getDocumentsByTitle(String surfaceForm, int maxHits) {

		PerformanceUtils pu = new PerformanceUtils();
		List<Document> documents = this.getDocumentsByTitleImpl(surfaceForm, maxHits);
		logger.info("needed time: "+pu.getRunningTimer());
		return documents;
	}

	/**
	 *
	 * @param surfaceForm
	 * @param maxHits
	 * @return
	 */
	protected abstract List<Document> getDocumentsByTitleImpl(String surfaceForm, int maxHits);

	/**
	 * Gets the configuration for the disambiguation engine.
	 * @return an implementation of AbstractDisambiguationEngineConfig
	 */
	public T getConfig() {
        return config;
    }
}