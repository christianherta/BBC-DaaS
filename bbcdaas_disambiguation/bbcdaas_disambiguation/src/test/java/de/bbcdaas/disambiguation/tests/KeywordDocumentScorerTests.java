package de.bbcdaas.disambiguation.tests;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.disambiguation.core.connector.lucene.LuceneConnector;
import de.bbcdaas.disambiguation.core.interfaces.DocumentScorer;
import de.bbcdaas.disambiguation.wikipedia.dataimport.lucene.WikiContentHandler;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiDisambiguationEngineConfig;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.scoring.KeywordDocumentScorer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.util.Version;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Robert Illers
 */
public class KeywordDocumentScorerTests {
	
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
	}
	
	/**
	 * 
	 */
	@Test
	public void testScoring() {
		
		Map<String,List<Document>> surfaceFormsDocuments = new HashMap<String, List<Document>>();
		String surfaceForm1 = "Java";
		String surfaceForm2 = "Programmiersprache";
		surfaceFormsDocuments.put(surfaceForm1, new ArrayList<Document>());
		surfaceFormsDocuments.put(surfaceForm2, new ArrayList<Document>());
		
		Float expectedDocument1Score = 0.6f;
		Document document1 = new Document(1);
		document1.setSurfaceForm(surfaceForm1);
		document1.setType(Document.DOCUMENT_TYPE_ARTICLE);
		document1.setUri("http://de.wikipedia.org/wiki/Java_(Programmiersprache)");
		document1.addField(WikiContentHandler.FIELD_URI, document1.getUri(), false, false, false);
		document1.addField(WikiContentHandler.FIELD_KEYWORDS,
			"Methode;Objekt;Konstruktor;Klasse;Garbage Collector", false, false, false);
		surfaceFormsDocuments.get(surfaceForm1).add(document1);
		
		Float expectedDocument2Score = 0.5f;
		Document document2 = new Document(2);
		document2.setSurfaceForm(surfaceForm2);
		document2.setType(Document.DOCUMENT_TYPE_ARTICLE);
		document2.setUri("http://de.wikipedia.org/wiki/Programmiersprache");
		document2.addField(WikiContentHandler.FIELD_URI, document2.getUri(), false, false, false);
		document2.addField(WikiContentHandler.FIELD_KEYWORDS,
			"Klasse;Funktion;Schleife;Methode;Objekt;aspektorientiert", false, false, false);
		surfaceFormsDocuments.get(surfaceForm2).add(document2);
		
		DocumentScorer keywordDocumentScorer = new KeywordDocumentScorer(this.config);
		try {
			keywordDocumentScorer.scoreDocuments(surfaceFormsDocuments);
		} catch(ApiException e) {
			fail("APIException thrown: "+e.getMessage());
		}
		
		List<Float> document1Scores = surfaceFormsDocuments.get(surfaceForm1).get(0).getScores();
		List<Float> document2Scores = surfaceFormsDocuments.get(surfaceForm2).get(0).getScores();
		
		assertEquals(expectedDocument1Score, document1Scores.get(0));
		assertEquals(expectedDocument2Score, document2Scores.get(0));
	}
	
	/**
	 * Clean up after testing.
	 * @throws Exception 
	 */
	@After
    public void tearDown() throws Exception {}
}
