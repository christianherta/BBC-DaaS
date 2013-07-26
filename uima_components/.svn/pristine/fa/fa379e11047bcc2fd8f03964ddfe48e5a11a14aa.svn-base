/** 
 * TestReader.java
 * 
 * Copyright (c) 2009, JULIE Lab. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0 
 *
 * Author: bernd
 * 
 * Current version: 1.0
 * Since version:   1.0
 *
 * Creation date: 12.06.2009 
 **/
package de.julielab.jules.reader.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.impl.JCasImpl;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.Progress;
import org.apache.uima.util.XMLInputSource;
import org.hsqldb.Server;
import org.hsqldb.jdbc.jdbcDataSource;
import org.jboss.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.julielab.jules.reader.DBReader;
import de.julielab.jules.reader.WikipediaReader;
import de.julielab.jules.types.Caption;
import de.julielab.jules.types.Header;
import de.julielab.jules.types.ListItem;
import de.julielab.jules.types.Paragraph;
import de.julielab.jules.types.TextObject;
import de.julielab.jules.types.wikipedia.Descriptor;
import de.julielab.jules.types.wikipedia.Link;
import de.julielab.jules.types.wikipedia.Title;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;

/**
 * Testclass for the jules-Wikipedia-Reader
 * 
 * @author Bernd Weigel
 */
public class TestReader {

	Logger logger = Logger.getLogger(TestReader.class);

	private static final String DESC_WIKI_READER = "src/test/resources/WikipediaDescriptorTestDB.xml";
	private WikipediaReader reader;
	private static Server server;
	private static String dbFileName;

	private ResultSet executeQuery;

	static {
		server = new Server();
		server.setSilent(true);
		server.setNoSystemExit(true);
		server.setErrWriter(null);
		server.setLogWriter(null);
		server.setDatabaseName(0, "eb_wikipedia");
		dbFileName = "db";
		File file = new File(dbFileName+".log");
		if(file.exists()){
			file.delete();
		}
		file = new File(dbFileName+".properties");
		if(file.exists()){
			file.delete();
		}
		file = new File(dbFileName+".script");
		if(file.exists()){
			file.delete();
		}
		server.setDatabasePath(0, dbFileName);
		server.start();
	}

	@Before
	public void setUp() throws Exception {
		Field field = DBReader.class.getDeclaredField("UPDATE_DOCUMENTS");
		field.setAccessible(true);
		field.set(null, "UPDATE Page SET is_in_process = TRUE, host_name = ?, pid = ? WHERE  pid is NULL ");
		
		field = DBReader.class.getDeclaredField("UPDATE_DOCUMENTS_NO_DISAMB_PAGES");
		field.setAccessible(true);
		field.set(null, "UPDATE Page SET is_in_process = TRUE, host_name = ?, pid = ? WHERE  pid is NULL and Page.isDisambiguation = false");
		
		field = DBReader.class.getDeclaredField("UPDATE_DOCUMENTS_BY_ID");
		field.setAccessible(true);
		field.set(null, "UPDATE Page SET is_in_process = TRUE, host_name = ?, pid = ? WHERE  pid is NULL  and id=? ");
		
		setUpDataBase();
		
		XMLInputSource source = new XMLInputSource(DESC_WIKI_READER);
		ResourceSpecifier resourceSpecifier = (ResourceSpecifier) UIMAFramework.getXMLParser().parse(source);
		reader = (WikipediaReader) UIMAFramework.produceCollectionReader(resourceSpecifier);
	}	
	
	@After
	public void tearDown() throws Exception {
		tearDownDataBase();
	}
	
	private void tearDownDataBase() throws Exception {
		Connection connection = getDBConnection();
		Statement statement = connection.createStatement();
		for (String table : drops) {
			statement.executeUpdate(table);
		}
		statement.close();
		connection.close();
	}

	private void setUpDataBase() throws Exception {
		Connection connection = getDBConnection();
		Statement statement = connection.createStatement();
		// Just in case one of the tests failed with an exception or was stopped
		// manually
		for (String table : drops) {
			statement.executeUpdate(table);
		}
		for (String table : tables) {
			statement.executeUpdate(table);
		}
		statement.executeUpdate("insert into page "
				+ " (id, pageId, name, text, isDisambiguation, is_processed , is_in_process, include, has_errors , log,host_name, pid) values "
				+ "(7,1,'FirstTitle',  'XX', FALSE           , FALSE, 	    FALSE, 		   TRUE,FALSE,'log','', '')");
		//disambiguationPage
		statement.executeUpdate("insert into page "
				+ " (id, pageId, name, text, isDisambiguation, is_processed , is_in_process, include, has_errors , log,host_name, pid) values "
				+ "(8,8,'DisambiguationTestPage',  'XX', TRUE           , FALSE, 	    FALSE, 		   TRUE,FALSE,'log','', '')");
		statement.executeUpdate("insert into page "
				+ " (id, pageId, name, text, isDisambiguation, is_processed , is_in_process, include, has_errors , log,host_name, pid) values "
				+ "(9,10,'SecondTitle',  'XX', FALSE           , FALSE, 	    FALSE, 		   TRUE,FALSE,'log','', '')");

		statement.executeUpdate("insert into category " + "(id, pageId, name) values" + "(99,99,'ATestCategory')");
		statement.executeUpdate("insert into category_pages " + "(id, pages) values" + "(99,8)");
		statement.executeUpdate("insert into page_categories " + "(pages, id) values" + "(99,8)");

		
		String testPageText = "text of page";
		statement.executeUpdate("insert into page "
				+ " (id, pageId, name, text, isDisambiguation, is_processed , is_in_process, include, has_errors , log,host_name, pid) values "
				+ "(14149742,14149742,'TGFB1I1',  '" + testPageText + "', FALSE           , FALSE, 	    FALSE, 		   TRUE,FALSE,'log','', '')");
		statement.executeUpdate("insert into category " + "(id, pageId, name) values" + "(1,1,'Chromosome_16_gene_stubs')");
		statement.executeUpdate("insert into category " + "(id, pageId, name) values" + "(2,2,'Gene_expression')");
		statement.executeUpdate("insert into category " + "(id, pageId, name) values" + "(3,3,'Genes_on_chromosome_16')");
		statement.executeUpdate("insert into category " + "(id, pageId, name) values" + "(4,4,'Human_proteins')");
		statement.executeUpdate("insert into category " + "(id, pageId, name) values" + "(5,5,'Protein_pages_needing_a_picture')");
		statement.executeUpdate("insert into category " + "(id, pageId, name) values" + "(6,6,'Transcription_coregulators')");
		for (int i = 1; i < 7; i++) {
			statement.executeUpdate("insert into category_pages " + "(id, pages) values" + "(" + i + ",14149742)");
		}
		for (int i = 1; i < 7; i++) {
			statement.executeUpdate("insert into page_categories " + "(pages, id) values" + "(" + i + ",14149742)");
		}

		String inlinks[] = new String[] { "Retinoblastoma_protein", "Coactivator_(genetics)", "Chromatin_Structure_Remodeling_(RSC)_Complex", "SWI/SNF",
				"P300/CBP", "PELP-1", "Transcription_coregulator", "PCAF", "Nuclear_receptor_coactivator_1", "Nuclear_receptor_coactivator_2",
				"Nuclear_receptor_coactivator_3", "Nuclear_receptor_co-repressor_1", "Nuclear_receptor_co-repressor_2", "Corepressor_(genetics)", "CARM1",
				"PPARGC1A", "SIN3A", "TRIM28", "NRIP1", "NCOA4", "NCOA6", "TRIM24", "PPARGC1B", "CRTC1", "SIN3B", "HR_(gene)", "NCOA7", "RNF14", "CRTC2",
				"TRIM33", "PNRC2", "Tripartite_motif_family", "PNRC1", "NCOA5", "CRTC3" };
		

		int i = 0;
		for (String inlink : inlinks) {
			statement.executeUpdate("insert into page "
					+ " (id, pageId, name, text, isDisambiguation, is_processed , is_in_process, include, has_errors , log,host_name, pid) values "
					+ "(14149742" + i + ",14149742" + i + ",'" + inlink + "',  'page " + i
					+ "', FALSE           , FALSE, 	    FALSE, 		   FALSE,FALSE,'log','', '')");
			statement.executeUpdate("insert into page_inlinks " + " (id, inlinks) values " + "(14149742,14149742" + i + ")");
			i++;
		}

		// insert into page_inlinks id=page, inLinks=linkpage
		// vgl. outlinks
		String[] outlinks = new String[] { "CARM1", "CRTC1", "CRTC2", "CRTC3", "Chromatin_Structure_Remodeling_(RSC)_Complex", "Coactivator_(genetics)",
				"Corepressor_(genetics)", "Digital_object_identifier", "Ensembl", "Entrez", "Gene", "HR_(gene)", "HomoloGene", "Medical_Subject_Headings",
				"Mouse_Genome_Informatics", "NCOA4", "NCOA5", "NCOA6", "NCOA7", "NRIP1", "Nuclear_Receptor_Signaling_Atlas", "Nuclear_receptor_co-repressor_1",
				"Nuclear_receptor_co-repressor_2", "Nuclear_receptor_coactivator_1", "Nuclear_receptor_coactivator_2", "Nuclear_receptor_coactivator_3",
				"P300/CBP", "PCAF", "PELP-1", "PNRC1", "PNRC2", "PPARGC1A", "PPARGC1B", "Public_domain", "RCOR1", "RNF14", "Retinoblastoma_protein", "SIN3A",
				"SIN3B", "SWI/SNF", "TRIM24", "TRIM28", "TRIM33", "Transcription_coregulator", "Tripartite_motif_family",
				"United_States_National_Library_of_Medicine", "Stub" };
		for (String outlink : outlinks) {
			statement.executeUpdate("insert into page "
					+ " (id, pageId, name, text, isDisambiguation, is_processed , is_in_process, include, has_errors , log,host_name, pid) values "
					+ "(14149742" + i + ",14149742" + i + ",'" + outlink + "',  'page " + i
					+ "', FALSE           , FALSE, 	    FALSE, 		   FALSE,FALSE,'log','', '')");
			statement.executeUpdate("insert into page_outlinks " + " (id, outlinks) values " + "(14149742,14149742" + i + ")");
			i++;
		}

		statement.executeUpdate("UPDATE Page SET is_processed = FALSE, is_in_process = FALSE, has_errors = FALSE, host_name = NULL, pid = NULL");
		
		statement.close();
		connection.close();
	}

	private Connection getDBConnection() throws SQLException {
		jdbcDataSource dataSource = new jdbcDataSource();
		dataSource.setDatabase("jdbc:hsqldb:file:" + dbFileName);
		dataSource.setUser("sa");
		dataSource.setPassword("");
		Connection connection = dataSource.getConnection();
		return connection;
	}
	
	@Test
	public void testCategoryList() throws Exception{
		reader.setConfigParameterValue("OnlyArticles", false);
		reader.setConfigParameterValue("CategoryList", new String[] { "ATestCategory" });
		reader.setConfigParameterValue("AddMetaData", true);
		reader.reconfigure();

		CAS cas = CasCreationUtils.createCas(reader.getProcessingResourceMetaData());
		executeQuery = getDBConnection().createStatement().executeQuery("Select * from Page");
		executeQuery.next();
		System.out.println(executeQuery.getInt("id") + " "+executeQuery.getString("pid"));
		reader.getNext(cas);

		Progress[] p = reader.getProgress();
		for(Progress progress:p){
			assertEquals(1,progress.getTotal());
		}
		
		JCas jcas = cas.getJCas();
//		checkHeader(jcas,"en","Wikipedia","disambiguation-page","DisambiguationTestPage","00000008",1);
		checkHeader(jcas,"en","Wikipedia","disambiguation-page","DisambiguationTestPage","8",1);
		
		cas = CasCreationUtils.createCas(reader.getProcessingResourceMetaData());
		reader.getNext(cas);
		jcas = cas.getJCas();
		checkHeader(jcas,"","","","","",0);
		
	}
	
	@Test
	public void testOnlyArticle() throws Exception {
		reader.setConfigParameterValue("OnlyArticles", false);
		reader.setConfigParameterValue("AddMetaData", true);
		reader.reconfigure();
		
		Progress[] p = reader.getProgress();
		for(Progress progress:p){
			assertEquals(4,progress.getTotal());
		}
		// if only Articles is false, there is one more article before the
		// checked article
		CAS cas = CasCreationUtils.createCas(reader.getProcessingResourceMetaData());
		reader.getNext(cas);

		cas = CasCreationUtils.createCas(reader.getProcessingResourceMetaData());
		reader.getNext(cas);

		JCas jcas = cas.getJCas();
//		checkHeader(jcas,"en","Wikipedia","disambiguation-page","DisambiguationTestPage","00000008",1);
		checkHeader(jcas,"en","Wikipedia","disambiguation-page","DisambiguationTestPage","8",1);
		
		cas = CasCreationUtils.createCas(reader.getProcessingResourceMetaData());
		reader.getNext(cas);

		cas = CasCreationUtils.createCas(reader.getProcessingResourceMetaData());
		reader.getNext(cas);
		
		jcas = cas.getJCas();
		checkHeader(jcas,"en","Wikipedia","article","TGFB1I1","14149742",1);
	}

	private void checkHeader(JCas jcas, String language, String source, String docType, String title, String docId, int expectedCount) {
		AnnotationIndex annotationIndex = jcas.getAnnotationIndex(Header.type);
		FSIterator iterator = annotationIndex.iterator();
		int count = 0;
		Header header = null;
		while (iterator.hasNext()) {
			count++;
			header = (Header) iterator.next();
		}

		assertEquals(expectedCount, count);
		if(count==0)
			return;
		assertEquals(title, header.getTitle());
		assertEquals(docId, header.getDocId());
		assertEquals(language, header.getLanguage());
		assertEquals(source, header.getSource());
		assertEquals(docType, header.getDocType());
	}
	@Test
	public void testReader() {
		try {
			
			reader.setConfigParameterValue("PageList", new String[] { "TGFB1I1" });
			reader.setConfigParameterValue("AddMetaData", true);
			reader.reconfigure();
			
			Progress[] p = reader.getProgress();
			for(Progress progress:p){
				assertEquals(1,progress.getTotal());
			}
			
			CAS cas = CasCreationUtils.createCas(reader.getProcessingResourceMetaData());
			reader.getNext(cas);
			JCas jcas = cas.getJCas();

			AnnotationIndex annotationIndex = jcas.getAnnotationIndex(Header.type);
			FSIterator iterator = annotationIndex.iterator();
			int count = 0;
			Header header = null;
			while (iterator.hasNext()) {
				count++;
				header = (Header) iterator.next();
			}

			assertEquals(1, count);
			assertEquals("en", header.getLanguage());
			assertEquals("Wikipedia", header.getSource());
			assertEquals("article", header.getDocType());
			assertEquals("TGFB1I1", header.getTitle());
			assertEquals("14149742", header.getDocId());

			// check descriptor
			annotationIndex = jcas.getAnnotationIndex(Descriptor.type);
			iterator = annotationIndex.iterator();
			count = 0;
			Descriptor descriptor = null;
			while (iterator.hasNext()) {
				count++;
				descriptor = (Descriptor) iterator.next();
			}
			assertEquals(1, count);

			// check redirects
			Set<String> expectedRedirects = arrayToSet(new String[] {}); // no
			// redirects
			// for
			// test
			// page
			// available
			// :-(
			Set<String> actualRedirects = stringArrayToSet(descriptor.getRedirects());
			assertEquals(expectedRedirects, actualRedirects);

			// check categories
			Set<String> expectedCategories = arrayToSet(new String[] { "Chromosome_16_gene_stubs", "Gene_expression", "Genes_on_chromosome_16",
					"Human_proteins", "Protein_pages_needing_a_picture", "Transcription_coregulators" });
			Set<String> actualCategories = fsArrayToSet(descriptor.getCategories());
			assertEquals(expectedCategories, actualCategories);

			// check inlinks
			Set<String> expectedInLinks = arrayToSet(new String[] { "Retinoblastoma_protein", "Coactivator_(genetics)",
					"Chromatin_Structure_Remodeling_(RSC)_Complex", "SWI/SNF", "P300/CBP", "PELP-1", "Transcription_coregulator", "PCAF",
					"Nuclear_receptor_coactivator_1", "Nuclear_receptor_coactivator_2", "Nuclear_receptor_coactivator_3", "Nuclear_receptor_co-repressor_1",
					"Nuclear_receptor_co-repressor_2", "Corepressor_(genetics)", "CARM1", "PPARGC1A", "SIN3A", "TRIM28", "NRIP1", "NCOA4", "NCOA6", "TRIM24",
					"PPARGC1B", "CRTC1", "SIN3B", "HR_(gene)", "NCOA7", "RNF14", "CRTC2", "TRIM33", "PNRC2", "Tripartite_motif_family", "PNRC1", "NCOA5",
					"CRTC3" });
			Set<String> actualInLinks = fsArrayToSet(descriptor.getIncomingLinks());
			assertEquals(expectedInLinks, actualInLinks);

			// check outlinks
			Set<String> exprectedOutLinks = arrayToSet(new String[] { "CARM1", "CRTC1", "CRTC2", "CRTC3", "Chromatin_Structure_Remodeling_(RSC)_Complex",
					"Coactivator_(genetics)", "Corepressor_(genetics)", "Digital_object_identifier", "Ensembl", "Entrez", "Gene", "HR_(gene)", "HomoloGene",
					"Medical_Subject_Headings", "Mouse_Genome_Informatics", "NCOA4", "NCOA5", "NCOA6", "NCOA7", "NRIP1", "Nuclear_Receptor_Signaling_Atlas",
					"Nuclear_receptor_co-repressor_1", "Nuclear_receptor_co-repressor_2", "Nuclear_receptor_coactivator_1", "Nuclear_receptor_coactivator_2",
					"Nuclear_receptor_coactivator_3", "P300/CBP", "PCAF", "PELP-1", "PNRC1", "PNRC2", "PPARGC1A", "PPARGC1B", "Public_domain", "RCOR1",
					"RNF14", "Retinoblastoma_protein", "SIN3A", "SIN3B", "SWI/SNF", "TRIM24", "TRIM28", "TRIM33", "Transcription_coregulator",
					"Tripartite_motif_family", "United_States_National_Library_of_Medicine", "Stub" });
			Set<String> actualOutLinks = fsArrayToSet(descriptor.getOutgoingLinks());
			assertEquals(exprectedOutLinks, actualOutLinks);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Database connection or accessing Wikipedia data failed.");
		}
	}
	
	@Test
	public void testGetProgress() throws Exception{
		reader.setConfigParameterValue("OnlyArticles", true);
		reader.setConfigParameterValue("AddMetaData", true);
		//reader.setConfigParameterValue("CategoryList", new String[] { "ATestCategory" });
		reader.reconfigure();
		CAS cas = CasCreationUtils.createCas(reader.getProcessingResourceMetaData());
		reader.getNext(cas);
		Progress[] p = reader.getProgress();
		for(Progress progress:p){
			assertEquals(1,progress.getCompleted());
			assertEquals(3,progress.getTotal());
		}

		cas = CasCreationUtils.createCas(reader.getProcessingResourceMetaData());
		reader.getNext(cas);
		
		p = reader.getProgress();
		for(Progress progress:p){
			assertEquals(2,progress.getCompleted());
			assertEquals(3,progress.getTotal());
		}
	}


	
	
	@Test
	public void testAdaptTextSegment() throws Exception {
		WikipediaReader w = new WikipediaReader();
		// add empty article text
		StringBuilder articleText = new StringBuilder();
		this.setPrivateField(w, "articleText", articleText);
		assertEquals("A(), B , C", this.invokePrivateMethod(w, "adaptTextSegment", "A(), B () , C (   )").toString());
		assertEquals("A[], B , C", this.invokePrivateMethod(w, "adaptTextSegment", "A[], B [] , C [   ]").toString());
		assertEquals("A{}, B , C", this.invokePrivateMethod(w, "adaptTextSegment", "A{}, B {} , C {   }").toString());
		assertEquals("A\"\", B , C", this.invokePrivateMethod(w, "adaptTextSegment", "A\"\", B \"\" , C \"   \"").toString());
		assertEquals("A'', B , C", this.invokePrivateMethod(w, "adaptTextSegment", "A'', B '' , C '   '").toString());
		assertEquals("A B C", this.invokePrivateMethod(w, "adaptTextSegment", " A       B C ").toString());
	}

	@Test
	public void testAddEnding() throws Exception {
		WikipediaReader w = new WikipediaReader();

		this.setPrivateField(w, "articleText", new StringBuilder());
		// w.addEnding(false); //true/false spielt keine Rolle
		this.invokePrivateMethod(w, "addEnding");
		assertEquals("", ((StringBuilder) this.getPrivateFieldValue(w, "articleText")).toString());

		this.setPrivateField(w, "articleText", new StringBuilder("."));
		// w.addEnding(false); //true/false spielt keine Rolle
		this.invokePrivateMethod(w, "addEnding");
		assertEquals(". ", ((StringBuilder) this.getPrivateFieldValue(w, "articleText")).toString());

		this.setPrivateField(w, "articleText", new StringBuilder(")"));
		// w.addEnding(false); //true/false spielt keine Rolle
		this.invokePrivateMethod(w, "addEnding");
		assertEquals("). ", ((StringBuilder) this.getPrivateFieldValue(w, "articleText")).toString());

		this.setPrivateField(w, "articleText", new StringBuilder("a"));
		// w.addEnding(false);
		this.invokePrivateMethod(w, "addEnding");
		// assertEquals("a, ", w.getArticleText().toString());
		assertEquals("a. ", ((StringBuilder) this.getPrivateFieldValue(w, "articleText")).toString());

		this.setPrivateField(w, "articleText", new StringBuilder("a"));
		// w.addEnding(false);
		this.invokePrivateMethod(w, "addEnding");
		assertEquals("a. ", ((StringBuilder) this.getPrivateFieldValue(w, "articleText")).toString());

	}

	@Test
	public void testCapitalize() throws Exception {
		WikipediaReader w = new WikipediaReader();
		this.setPrivateField(w, "articleText", new StringBuilder());
		assertTrue((Boolean) this.invokePrivateMethod(w, "capitalize"));

		this.setPrivateField(w, "articleText", new StringBuilder("a"));
		assertFalse((Boolean) this.invokePrivateMethod(w, "capitalize"));

		this.setPrivateField(w, "articleText", new StringBuilder("a "));
		assertFalse((Boolean) this.invokePrivateMethod(w, "capitalize"));

		this.setPrivateField(w, "articleText", new StringBuilder(". "));
		assertTrue((Boolean) this.invokePrivateMethod(w, "capitalize"));

		this.setPrivateField(w, "articleText", new StringBuilder("."));
		assertFalse((Boolean) this.invokePrivateMethod(w, "capitalize"));
	}

	@Test
	public void testParsedPage() throws Exception {
		String articleRawText = getTestPageText();
		articleRawText = (String) this.invokePrivateMethod(reader, "removeTimelinesAndReferences", articleRawText);
		// NOTE: must be done before parsing! otherwise parser removes <ref/>
		// tags without the text in between
		MediaWikiParser parser = (MediaWikiParser) this.getPrivateFieldValue(reader, "parser");
		ParsedPage parsedPage = parser.parse(articleRawText);
		CAS cas = CasCreationUtils.createCas(reader.getProcessingResourceMetaData());
		JCas jcas = cas.getJCas();
		this.setPrivateField(reader, "articleText", new StringBuilder());
		this.setPrivateField(reader, "offset", 0);
		this.invokePrivateMethod(reader, "handleParsedPage", jcas, parsedPage);

		// Class[] parameterTypes = new Class[2];
		//		
		// parameterTypes[0] = JCas.class;
		// parameterTypes[1] = ParsedPage.class;
		// Method method = reader.getClass().getDeclaredMethod(,
		// parameterTypes);
		// method.setAccessible(true);
		// method.invoke(reader, jcas, parsedPage);

		jcas.setDocumentText(((StringBuilder) this.getPrivateFieldValue(reader, "articleText")).toString());

		AnnotationIndex index = jcas.getAnnotationIndex(ListItem.type);
		FSIterator iterator = index.iterator();
		ListItem li = (de.julielab.jules.types.ListItem) iterator.next();
		assertEquals("Definition of first item.", li.getCoveredText());
		li = (de.julielab.jules.types.ListItem) iterator.next();
		assertEquals("Definition of second item", li.getCoveredText());
		li = (de.julielab.jules.types.ListItem) iterator.next();
		assertEquals("Definition of third item", li.getCoveredText());
		li = (de.julielab.jules.types.ListItem) iterator.next();
		assertEquals("A", li.getCoveredText());
		assertEquals(1, li.getLevel());
		li = (de.julielab.jules.types.ListItem) iterator.next();
		assertEquals("B.", li.getCoveredText());
		assertEquals(1, li.getLevel());
		li = (de.julielab.jules.types.ListItem) iterator.next();
		assertEquals("X", li.getCoveredText());
		assertEquals(2, li.getLevel());
		li = (de.julielab.jules.types.ListItem) iterator.next();
		assertEquals("Y", li.getCoveredText());
		assertEquals(2, li.getLevel());
		li = (de.julielab.jules.types.ListItem) iterator.next();
		assertEquals("K.", li.getCoveredText());
		assertEquals(3, li.getLevel());
		li = (de.julielab.jules.types.ListItem) iterator.next();
		assertEquals("L", li.getCoveredText());
		assertEquals(3, li.getLevel());
		li = (de.julielab.jules.types.ListItem) iterator.next();
		assertEquals("M", li.getCoveredText());
		assertEquals(3, li.getLevel());
		li = (de.julielab.jules.types.ListItem) iterator.next();
		assertEquals("Z", li.getCoveredText());
		assertEquals(2, li.getLevel());
		li = (de.julielab.jules.types.ListItem) iterator.next();
		assertEquals("C.", li.getCoveredText());
		assertEquals(1, li.getLevel());
		li = (de.julielab.jules.types.ListItem) iterator.next();
		assertEquals("D.", li.getCoveredText());
		assertEquals(1, li.getLevel());
		assertFalse(iterator.hasNext());
		index = jcas.getAnnotationIndex(de.julielab.jules.types.List.type);
		iterator = index.iterator();
		de.julielab.jules.types.List list = (de.julielab.jules.types.List) iterator.next();
		assertEquals("Definition lists", list.getCoveredText());
		list = (de.julielab.jules.types.List) iterator.next();
		assertEquals("First item", list.getCoveredText());
		list = (de.julielab.jules.types.List) iterator.next();
		assertEquals("Second item (zweites)", list.getCoveredText());
		list = (de.julielab.jules.types.List) iterator.next();
		assertEquals("Third item", list.getCoveredText());
		list = (de.julielab.jules.types.List) iterator.next();
		assertEquals("A. B. X. Y. K. L. M. Z. C. D.", list.getCoveredText());
		assertFalse(iterator.hasNext());
		AnnotationIndex secIndex = jcas.getAnnotationIndex(de.julielab.jules.types.Section.type);
		iterator = secIndex.iterator();
		assertEquals(
				"This is a simple article. It was created for Testing purpose and should contain all relevant elements of the wikipedia"
						+ " api. The first reference is followed by the second one. The article contains links without caption and link. Subheadline. A subsection Text. Second Subheadline. "
						+ "Another Section with some text. Third Subheadline. A Section with some stupid Text and some elements. And some line breaks. "
						+ "Yes. Some line breaks. Test Tabeble with numbers. A Subsection. Example image caption Some Text. Definition lists. First item. "
						+ "Definition of first item. Second item (zweites). Definition of second item. Third item. Definition of third item. Another SubSection. A "
						+ "List. A. B. X. Y. K. L. M. Z. C. D.", jcas.getDocumentText());
		de.julielab.jules.types.Section sec = ((de.julielab.jules.types.Section) iterator.next());
		assertEquals("This is a simple article. It was created for Testing purpose and should contain all relevant elements of the wikipedia api."
				+ " The first reference is followed by the second one. The article contains links without caption and link.", sec.getCoveredText());
		sec = ((de.julielab.jules.types.Section) iterator.next());
		assertEquals("Subheadline. A subsection Text.", sec.getCoveredText());
		assertEquals("Subheadline", sec.getSectionHeading().getCoveredText());
		sec = ((de.julielab.jules.types.Section) iterator.next());
		assertEquals("Second Subheadline. Another Section with some text.", sec.getCoveredText());
		assertEquals(null, sec.getTextObjects());
		sec = ((de.julielab.jules.types.Section) iterator.next());
		assertEquals("Third Subheadline. A Section with some stupid Text and some elements. And some line breaks. Yes. Some line breaks. "
				+ "Test Tabeble with numbers.", sec.getCoveredText());
		assertEquals(1, sec.getTextObjects().size());
		assertEquals(sec.getTextObjects(0).getCoveredText(), "Test Tabeble with numbers");
		assertEquals(1, sec.getTextObjects().size());
		sec = ((de.julielab.jules.types.Section) iterator.next());
		assertEquals("Example image caption", sec.getTextObjects(0).getCoveredText());
		assertEquals("A Subsection. Example image caption Some Text. Definition lists. First item. "
				+ "Definition of first item. Second item (zweites). Definition of second item. Third item. Definition of third item.", sec.getCoveredText());
		assertEquals("A Subsection", sec.getSectionHeading().getCoveredText());
		sec = ((de.julielab.jules.types.Section) iterator.next());
		assertEquals("Another SubSection. A List. A. B. X. Y. K. L. M. Z. C. D.", sec.getCoveredText());
		assertFalse(iterator.hasNext());
		AnnotationIndex paragraphs = jcas.getAnnotationIndex(Paragraph.type);
		iterator = paragraphs.iterator();
		Paragraph p = (Paragraph) iterator.next();
		assertEquals("This is a simple article. It was created for Testing purpose and should contain all relevant elements of the wikipedia api."
				+ " The first reference is followed by the second one.", p.getCoveredText());
		p = (Paragraph) iterator.next();
		assertEquals("The article contains links without caption and link", p.getCoveredText());
		p = (Paragraph) iterator.next();
		assertEquals("A subsection Text.", p.getCoveredText());
		p = (Paragraph) iterator.next();
		assertEquals("Another Section with some text.", p.getCoveredText());
		p = (Paragraph) iterator.next();
		assertEquals("A Section with some stupid Text and some elements. And some line breaks. Yes. Some line breaks.", p.getCoveredText());
		p = (Paragraph) iterator.next();
		assertEquals("Example image caption Some Text", p.getCoveredText());
		p = (Paragraph) iterator.next();
		assertEquals("A List.", p.getCoveredText());
		assertFalse(iterator.hasNext());
		index = jcas.getAnnotationIndex(Link.type);
		iterator = index.iterator();
		Link l = (Link) iterator.next();
		assertEquals("links_without_caption", l.getTarget());
		assertEquals("links without caption", l.getCoveredText());
		l = (Link) iterator.next();
		assertEquals("links_with_caption", l.getTarget());
		assertEquals("link", l.getCoveredText());
		l = (Link) iterator.next();
		assertEquals("zweites", l.getTarget());
		assertEquals("zweites", l.getCoveredText());
		assertFalse(iterator.hasNext());
		index = jcas.getAnnotationIndex(Caption.type);
		iterator = index.iterator();
		Caption c = (Caption) iterator.next();
		assertEquals("Test Tabeble with numbers", c.getCoveredText());
		c = (Caption) iterator.next();
		assertEquals("Example image caption", c.getCoveredText());
		assertFalse(iterator.hasNext());
		index = jcas.getAnnotationIndex(TextObject.type);
		iterator = index.iterator();
		TextObject t = (TextObject) iterator.next();
		assertEquals("Test Tabeble with numbers", t.getCoveredText());
		t = (TextObject) iterator.next();
		assertEquals("Example image caption", t.getCoveredText());
		assertFalse(iterator.hasNext());
	}

	private Set<String> stringArrayToSet(StringArray stringArray) {
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < stringArray.size(); i++) {
			set.add(stringArray.get(i));
		}
		return set;
	}

	private Set<String> fsArrayToSet(FSArray fsArray) {
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < fsArray.size(); i++) {
			Title title = (Title) fsArray.get(i);
			set.add(title.getFullname());
		}
		return set;
	}

	@SuppressWarnings(value = { "unchecked" })
	private Object invokePrivateMethod(Object reader, String methodName, Object... parameter) throws Exception {
		Class[] parameterTypes = new Class[parameter.length];
		for (int i = 0; i < parameter.length; i++) {
			if (parameter[i].getClass().equals(JCasImpl.class)) {
				parameterTypes[i] = JCas.class;
			} else {
				parameterTypes[i] = parameter[i].getClass();
			}
		}
		Method method = reader.getClass().getDeclaredMethod(methodName, parameterTypes);
		method.setAccessible(true);
		return method.invoke(reader, parameter);
	}

	private void setPrivateField(WikipediaReader reader, String fieldName, Object parameter) throws Exception {
		Field field = reader.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(reader, parameter);
	}

	private Object getPrivateFieldValue(WikipediaReader reader, String fieldName) throws Exception {
		Field field = reader.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(reader);
	}

	private Set<String> arrayToSet(String[] strings) {
		Set<String> set = new HashSet<String>();
		for (String string : strings) {
			set.add(string.replaceAll("_", " "));
		}
		return set;
	}

	private String getTestPageText() {
		FileReader reader;
		String readData = "";
		try {
			reader = new FileReader("src/test/resources/testFile.txt");
			int numRead = 0;
			while ((numRead = reader.read()) != -1) {
				if (numRead == 10) {
					readData += '\n';
				} else
					readData += (char) numRead;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return readData;
	}

	String tables[] = new String[] {
			"CREATE TABLE Category (id bigint NOT NULL, pageId int, name varchar)",
			"CREATE TABLE MetaData (id bigint NOT NULL, language varchar, disambiguationCategory varchar, mainCategory varchar, nrofPages bigint, nrofRedirects bigint, nrofDisambiguationPages bigint, nrofCategories bigint, version varchar)",
			"CREATE TABLE Page (id bigint NOT NULL, pageId int, name varchar, text varchar,isDisambiguation bit, is_processed bit NOT NULL, is_in_process bit NOT NULL, include bit NOT NULL, has_errors bit NOT NULL, log varchar,host_name varchar, pid varchar)",
			"CREATE TABLE PageMapLine (id bigint NOT NULL, name varchar, pageID int, stem varchar, lemma varchar)",
			"CREATE TABLE RelatednessCacheLine (id bigint NOT NULL, page1 int, page2 int, PathLengthAverage double, PathLengthBest double, PathLengthSelectivity double, LeacockChodorowAverage double, LeacockChodorowBest double, LeacockChodorowSelectivity double, ResnikAverage double, ResnikBest double, ResnikSelectivityLinear double, ResnikSelectivityLog double, LinAverage double, LinBest double, JiangConrathAverage double, JiangConrathBest double, WuPalmerAverage double, WuPalmerBest double, LeskFirst double, LeskFull double)",
			"CREATE TABLE category_inlinks (id bigint NOT NULL,inLinks int )", "CREATE TABLE category_outlinks (id bigint NOT NULL,outLinks int )",
			"CREATE TABLE category_pages (id bigint NOT NULL,pages int )",
			// pages = id der page, id = id der category

			"CREATE TABLE page_categories (id bigint NOT NULL,pages int )",
			// pages = id der category, id = id der page

			"CREATE TABLE page_inlinks (id bigint NOT NULL,inLinks int )", "CREATE TABLE page_outlinks (id bigint NOT NULL,outLinks int )",
			"CREATE TABLE page_redirects (id bigint NOT NULL,redirects varchar )" };
	String drops[] = new String[] { "DROP TABLE IF EXISTS Category ", "DROP TABLE IF EXISTS MetaData ", "DROP TABLE IF EXISTS Page ",
			"DROP TABLE IF EXISTS PageMapLine ", "DROP TABLE IF EXISTS RelatednessCacheLine ", "DROP TABLE IF EXISTS category_inlinks ",
			"DROP TABLE IF EXISTS category_outlinks ", "DROP TABLE IF EXISTS category_pages ", "DROP TABLE IF EXISTS page_categories ",
			"DROP TABLE IF EXISTS page_inlinks ", "DROP TABLE IF EXISTS page_outlinks ", "DROP TABLE IF EXISTS page_redirects " };
}
