package de.bbcdaas.disambiguation.tests;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.disambiguation.core.connector.lucene.LuceneConnector;
import de.bbcdaas.disambiguation.core.interfaces.DocumentCategorizer;
import de.bbcdaas.disambiguation.wikipedia.dataimport.lucene.WikiContentHandler;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiDisambiguationEngineConfig;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.categorizer.ContentFieldDocumentCategorizer;
import java.util.Arrays;
import org.apache.lucene.util.Version;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Robert Illers
 */
public class ContentFieldDocumentCategorizerTests {
	
	private Version luceneVersion;
	private WikiDisambiguationEngineConfig config;
	
	/**
	 * Prepares the test cases.
	 * @throws Exception 
	 */
    @Before
    public void setUp() throws Exception {
	
		this.luceneVersion = Version.LUCENE_35;
		
		LuceneConnector connector = new LuceneConnector(this.luceneVersion, "undefinedIndexPath", null, null, 0);
		
		this.config = new WikiDisambiguationEngineConfig(connector);
		this.config.setDocumentCategorizer(new ContentFieldDocumentCategorizer(this.config));
	}
	
	/**
	 * Tests if categorizer gives the right type to a wikipedia disambiguation page.
	 */
	@Test
	public void testDisambiguationCategorizing() {
		
		Document javaDisambiguationPage = new Document();
		javaDisambiguationPage.addField(WikiContentHandler.FIELD_ALTERNATIVE_TITLES,
			Arrays.asList(new String[] {"Java (Insel)", "Java (Programmiersprache)"}), false, false, false);
		
		DocumentCategorizer categorizer = this.config.getDocumentCategorizer();
		javaDisambiguationPage.setType(categorizer.categorizeDocument(javaDisambiguationPage));
		assertEquals(Document.DOCUMENT_TYPE_DISAMBIGUATION, javaDisambiguationPage.getType());
	}
	
	/**
	 * Tests if categorizer gives the right type to a wikipedia redirect page.
	 */
	@Test
	public void testRedirectCategorizing() {
	
		Document javaRedirectPage = new Document();
		javaRedirectPage.addField(WikiContentHandler.FIELD_REDIRECT_TITLE, "Java (Insel)", false, false, false);
		
		DocumentCategorizer categorizer = this.config.getDocumentCategorizer();
		javaRedirectPage.setType(categorizer.categorizeDocument(javaRedirectPage));
		assertEquals(Document.DOCUMENT_TYPE_REDIRECT, javaRedirectPage.getType());
	}
	
	/**
	 * Clean up after testing.
	 * @throws Exception 
	 */
	@After
    public void tearDown() throws Exception {}
}
