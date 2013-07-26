package de.bbcdaas.disambiguation.core.configs;

import de.bbcdaas.disambiguation.core.connector.AbstractConnector;
import java.util.Set;
import java.util.TreeSet;

/**
 * Configuration for a sax contentHandler for parsing a XML files.
 * @author Robert Illers
 */
public abstract class AbstractContentHandlerConfig<T extends AbstractConnector> extends AbstractConfiguration<T> {
    
	private int limitedDocsCount;
	private Set<String> startElements = new TreeSet<String>();

	/**
	 * Constructor with persistence layer connector as parameter.
	 * @param connector 
	 */
    public AbstractContentHandlerConfig(T connector) {
        super(connector);
    }

	/**
	 * Start elements (Token) used in xml file to separate data areas
	 * @return list of start elements
	 */
	public Set<String> getStartElements() {
		return startElements;
	}

	/**
	 * Start elements (Token) used in xml file to separate data areas
	 * @param startElements 
	 */
	public void setStartElements(Set<String> startElements) {
		this.startElements = startElements;
	}

	/**
	 * Max number of documents to read
	 * @return limitedDocsCount
	 */
	public int getLimitedDocsCount() {
		return limitedDocsCount;
	}

	/**
	 * Max number of documents to read
	 * @param limitedDocsCount 
	 */
	public void setLimitedDocsCount(int limitedDocsCount) {
		this.limitedDocsCount = limitedDocsCount;
	}
}
