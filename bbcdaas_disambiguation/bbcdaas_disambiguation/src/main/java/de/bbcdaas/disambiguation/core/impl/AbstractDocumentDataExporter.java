package de.bbcdaas.disambiguation.core.impl;

import de.bbcdaas.disambiguation.core.configs.AbstractDataExporterConfig;
import de.bbcdaas.disambiguation.core.interfaces.DocumentDataExporter;
import org.apache.log4j.Logger;

/**
 *
 * @author Robert Illers
 */
public abstract class AbstractDocumentDataExporter <T extends AbstractDataExporterConfig> implements DocumentDataExporter {

    protected final Logger logger = Logger.getLogger(this.getClass());
    private T config;

    public AbstractDocumentDataExporter(T config) {
        this.config = config;
    }

    /**
	 * Gets the configuration for the disambiguation engine.
	 * @return an implementation of AbstractDisambiguationEngineConfig
	 */
	protected T getConfig() {
        return config;
    }
}
