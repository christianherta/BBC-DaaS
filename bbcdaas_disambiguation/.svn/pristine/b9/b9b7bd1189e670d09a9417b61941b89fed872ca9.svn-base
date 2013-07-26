package de.bbcdaas.disambiguation.core.impl;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.util.PerformanceUtils;
import de.bbcdaas.disambiguation.core.configs.AbstractDisambiguationEngineConfig;
import de.bbcdaas.disambiguation.core.interfaces.DocumentCategorizer;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Robert Illers
 */
public abstract class AbstractDocumentCategorizer <T extends AbstractDisambiguationEngineConfig> implements DocumentCategorizer {

	protected final Logger logger = Logger.getLogger(this.getClass());
    private T config;

	/**
	 * Constructor
	 * @param config
	 */
	public AbstractDocumentCategorizer(T config) {
        this.config = config;
	}

	/**
	 * Categorizes a list of documents, the type will be set into the document
	 * objects.
	 * @param surfaceNamesDocuments documents sorted by its surface name
	 */
	@Override
	public final void categorizeDocuments(Map<String, List<Document>> surfaceNamesDocuments) {

		PerformanceUtils pu = new PerformanceUtils();
		this.categorizeDocumentsImpl(surfaceNamesDocuments);
		logger.info("needed time: "+pu.getRunningTimer());
	}

	/**
	 *
	 * @param surfaceNamesDocuments
	 * @return
	 */
	protected abstract void categorizeDocumentsImpl(Map<String, List<Document>> surfaceNamesDocuments);

    /**
	 * Gets the configuration for the disambiguation engine.
	 * @return an implementation of AbstractDisambiguationEngineConfig
	 */
	protected T getConfig() {
        return config;
    }
}