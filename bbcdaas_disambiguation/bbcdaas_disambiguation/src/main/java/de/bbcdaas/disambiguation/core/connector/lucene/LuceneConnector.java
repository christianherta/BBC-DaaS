package de.bbcdaas.disambiguation.core.connector.lucene;

import de.bbcdaas.common.dao.api.LuceneAPI;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.disambiguation.core.connector.AbstractConnector;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.util.Version;

/**
 *
 * @author Robert Illers
 */
public class LuceneConnector extends AbstractConnector {

	private String indexPath = "";
	private LuceneAPI luceneAPI;
	private int limitedTokenCount;
    private Analyzer standardAnalyzer;
	private Map<String, Analyzer> fieldAnalyzer = new HashMap<String, Analyzer>();

	/**
	 * Constructor
	 * @param luceneVersion
	 * @param indexPath
	 * @param standardAnalyzer standard analyzer used if no analyzer for field set
	 * @param fieldAnalyzers Lucene Analyzer for each field
	 */
	public LuceneConnector(Version luceneVersion, String indexPath,
		Analyzer standardAnalyzer, Map<String, Analyzer> fieldAnalyzers, int limitedTokenCount) {

		super();
		this.standardAnalyzer = standardAnalyzer;
		if (fieldAnalyzers != null) {
			this.fieldAnalyzer = fieldAnalyzers;
		}
		PerFieldAnalyzerWrapper perFieldAnalyzerWrapper = new PerFieldAnalyzerWrapper(standardAnalyzer,
			this.fieldAnalyzer);
		try {
			if (indexPath != null && !indexPath.isEmpty()) {
				this.indexPath = indexPath;
				this.luceneAPI = new LuceneAPI(luceneVersion, indexPath, perFieldAnalyzerWrapper, limitedTokenCount);
			} else {
				logger.error("indexPath not set, Lucene could not be initialized");
			}
		} catch(ApiException e) {
			logger.error(e);
			this.indexPath = "";
		}
	}

	/**
	 * URL to the lucene index.
	 * @return indexPath
	 */
	public String getIndexPath() {
		return indexPath;
	}

	/**
	 * Returns the analyzer that will be used if no analyzer is set specially
	 * for the current field.
	 * @return Analyzer
	 */
	public Analyzer getStandardAnalyzer() {
		return standardAnalyzer;
	}

	/**
	 * Returns the analyzer for a specific field.
	 * @param fieldName name of the field
	 * @return Analyzer
	 */
	public Analyzer getFieldAnalyzer(String fieldName) {
		return this.fieldAnalyzer.get(fieldName);
	}

	/**
	 * Returns the list of field analyzer.
	 * @return Map<String, Analyzer>
	 */
	public Map<String, Analyzer> getFieldAnalyzer() {
		return this.fieldAnalyzer;
	}

	/**
	 * Returns the lucene Version of the current LuceneAPI.
	 * @return Version
	 */
	public Version getLuceneVersion() {
		return this.getLuceneAPI().getLuceneVersion();
	}

	/**
	 * Returns the LuceneAPI.
	 * @return LuceneAPI
	 */
	public LuceneAPI getLuceneAPI() {
		return this.luceneAPI;
	}

	public int getLimitedTokenCount() {
		return limitedTokenCount;
	}

	@Override
	public void setEnableLogs(boolean enableLogs) {

		super.setEnableLogs(enableLogs);
		this.luceneAPI.setEnableLogs(this.isEnableLogs());
	}

	public void closeConnection() throws ApiException {
		this.luceneAPI.closeConnection();
	}
}
