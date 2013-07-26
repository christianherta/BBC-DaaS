package de.bbcdaas.disambiguation.core.configs;

import de.bbcdaas.disambiguation.core.connector.AbstractConnector;
import org.apache.log4j.Logger;

/**
 * Base class for all configurations of disambiguation components like importer,
 * exporter, scorer, candidate finder, categorizer, combiner and disambiguation engines
 * @author Robert Illers
 */
public abstract class AbstractConfiguration<T extends AbstractConnector> {

	protected Logger logger = Logger.getLogger(this.getClass());
	private T connector;
	private boolean enableLogs = true;

	/**
	 * Constructor setting the connector , used for persistence layer access
	 * @param connector wrapper class containing connection provider API and access data
	 */
	public AbstractConfiguration(T connector) {
		this.connector = connector;
	}

	/**
	 * Constructor for configurations without persistence layer access
	 */
	public AbstractConfiguration() {}

	/**
	 * The connector conatins access data and an API for accessing persistence layer
	 * @return connector
	 */
	public T getConnector() {
		return connector;
	}

	public boolean isEnableLogs() {
		return enableLogs;
	}

	public void setEnableLogs(boolean enableLogs) {
		this.enableLogs = enableLogs;
	}
}
