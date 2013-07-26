package de.bbcdaas.disambiguation.tests;

import de.bbcdaas.disambiguation.core.connector.lucene.LuceneConnector;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.util.Version;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Some tests concerning the wikipedia lucene configuration for the disambiguation engine.
 * @author Robert Illers
 */
public class WikipediaConfigTests {
	
	private Version luceneVersion = null;
	private Analyzer standardAnalyzer = null;
	private Map<String, Analyzer> fieldAnalyzer = new HashMap<String, Analyzer>();
	
	/**
	 * Prepares the test cases.
	 * @throws Exception 
	 */
    @Before
    public void setUp() throws Exception {
		
		this.luceneVersion = Version.LUCENE_35;
		this.standardAnalyzer = new GermanAnalyzer(this.luceneVersion);
	}
	
	/**
	 * Tests if luceneAPI object is created on instanciating wikipedia lucene config.
	 */
	@Test
	public void testLuceneAPICreation() {
		
		LuceneConnector connector = new LuceneConnector(this.luceneVersion,
			"undefinedIndexPath", this.standardAnalyzer, this.fieldAnalyzer, 0);
		
		assertNotNull(connector.getLuceneAPI());
	}
	
	/**
	 * Clean up after testing.
	 * @throws Exception 
	 */
	@After
    public void tearDown() throws Exception {
		
		this.luceneVersion = null;
		this.standardAnalyzer = null;
	}
}
