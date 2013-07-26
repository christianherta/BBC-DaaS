/** 
 * DBReader.java
 * 
 * Copyright (c) 2009, JULIE Lab. 
 * All rights reserved. This program and the accompanying materials 
 * are protected. Please contact JULIE Lab for further information. 
 *
 * Creation date: 09.11.2009 
 * 
 **/

package de.julielab.jules.reader;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

/**
 * TODO insert description
 */
public abstract class DBReader extends CollectionReader_ImplBase{

	private static final Logger LOGGER = Logger.getLogger(DBReader.class);

	public static final String PARAM_DB_DRIVER = "DataBaseDriver";
	public static final String PARAM_SERVER_URL = "DataBaseServerURL";
	public static final String PARAM_DB_USER = "DataBaseUser";
	public static final String PARAM_DB_NAME = "DataBase";
	public static final String PARAM_DB_PASSWORD = "DataBasePassword";
	public static final String PARAM_ONLY_ARTICLES = "OnlyArticles";
	public static final String PARAM_ADD_META_DATA = "AddMetaData";
	public static final String PARAM_BATCH_SIZE = "BatchSize";
	private static final String PARAM_RESTRICT_TO_CATEGORIES = "CategoryList";
	private static final String PARAM_RESTRICT_TO_PAGES = "PageList";

	private String DEFAULT_DB_DRIVER = "com.mysql.jdbc.Driver";
	private static int DEFAULT_BATCH_SIZE = 100;
	private boolean DEFAULT_ONLY_ARTICLES = true;

	private static String LIMIT = " LIMIT " + DEFAULT_BATCH_SIZE;
	private final static String SELECT_UNPROCESSED_DOCUMENT_COUNT = "SELECT COUNT(id) FROM Page WHERE is_processed = FALSE AND include = TRUE";
	private final static String SELECT_DOCUMENTS = "SELECT id,name, isDisambiguation, text FROM Page WHERE is_processed = FALSE AND is_in_process = TRUE AND include = TRUE and host_name = ? and pid = ? ";
	private static String SELECT_ID_FROM_PAGE_GIVEN_CAT_NAME = "Select id,host_name,pid from Page inner join (SELECT pages FROM category_pages WHERE id = (SELECT DISTINCT id FROM Category WHERE name = ?) ) as x on x.pages=Page.id";
	private static final String SELECT_PAGE_IDS_GIVEN_CAT_NAME_LIMIT = SELECT_ID_FROM_PAGE_GIVEN_CAT_NAME + LIMIT;
	private static final String SELECT_PAGE_IDS_GIVEN_CAT_NAME_NO_DISAMB_PAGES = SELECT_ID_FROM_PAGE_GIVEN_CAT_NAME + " and Page.isDisambiguation = ? " + LIMIT;
	private final static String SELECT_REDIRECTS_BY_ID = "SELECT redirects, id FROM page_redirects WHERE id >= ? and id <= ?";
	private static final String SELECT_CATEGORIES_BY_PAGE_ID = "SELECT DISTINCT t1.name,t2.id FROM Category t1, page_categories t2 where t1.id = t2.pages and t2.id>=? and t2.id <=?";
	private static final String SELECT_IN_LINKS_BY_PAGE_ID = " SELECT DISTINCT t1.name,t2.id FROM Page t1, page_inlinks t2 where t1.id = t2.inLinks and t2.id >= ? and t2.id <= ?";
	private static final String SELECT_OUT_LINKS_BY_PAGE_ID = "SELECT DISTINCT t1.name,t2.id FROM Page t1, page_outlinks t2 where t1.id = t2.outLinks and t2.id >= ? and t2.id <= ?";

	private static String UPDATE = "UPDATE Page SET is_in_process = TRUE, host_name = ?, pid = ? where pid is NULL";
	private static String UPDATE_DOCUMENTS_NO_DISAMB_PAGES = UPDATE + " and Page.isDisambiguation = false" + LIMIT;
	private static String UPDATE_DOCUMENTS_DONE = "UPDATE Page SET pid = ? WHERE pid = ? and host_name = ? and is_in_process=TRUE";
	private static String UPDATE_DOCUMENTS_BY_PAGE = "UPDATE Page SET is_in_process = TRUE, host_name = ?, pid = ? WHERE name=?";
	private static String UPDATE_DOCUMENTS_BY_PAGE_NO_DISAMB_PAGES = UPDATE_DOCUMENTS_BY_PAGE + " and Page.isDisambiguation = false";
	private static String UPDATE_DOCUMENTS_BY_ID = UPDATE + " and id=? ";
	private static String UPDATE_DOCUMENTS = UPDATE + LIMIT;

	
	private PreparedStatement UPDATE_DOCUMENTS_BY_PAGE_STATEMENT;
	private PreparedStatement SELECT_DOCUMENTS_STATEMENT;
	private PreparedStatement SELECT_PAGE_IDS_GIVEN_CAT_NAME_STATEMENT = null;
	private PreparedStatement SELECT_PAGE_IDS_GIVEN_CAT_NAME_STATEMENT_NO_DISAMB_PAGES = null;
	private PreparedStatement UPDATE_DOCUMENTS_BY_ID_STATEMENT;

	private PreparedStatement UPDATE_DOCUMENTS_STATEMENT_DONE;
	private PreparedStatement SELECT_REDIRECTS_BY_ID_STATEMENT;
	private PreparedStatement SELECT_CATEGORIES_BY_PAGE_ID_STATEMENT;
	private PreparedStatement SELECT_IN_LINKS_BY_PAGE_ID_STATEMENT;
	private PreparedStatement SELECT_OUT_LINKS_BY_PAGE_ID_STATEMENT;
	private PreparedStatement UPDATE_DOCUMENTS_STATEMENT;
	private PreparedStatement UPDATE_DOCUMENTS_BY_PAGE_STATEMENT_NO_DISAMB_PAGES;
	private PreparedStatement UPDATE_DOCUMENTS_STATEMENT_NO_DISAMB_PAGES;

	protected Connection connection;
	private Integer batchSize;
	private String hostName;
	private String pid;
	private Boolean onlyArticles;
	private boolean pageRestriction = false;
	private int totalDocumentCount;
	protected int processedDocuments;
	protected Iterator<Integer> idIterator;
	protected HashMap<Integer, Article> articles;

	private int maxId;

	private int minId;

	private boolean DEFAULT_ADD_META_DATA = false;
	protected boolean addMetaData;

	private LinkedList<String> selectedCategories;

	private LinkedList<String> selectedPages;



	@Override
	public void initialize() throws ResourceInitializationException {
		super.initialize();

		// set param only meta data
		Boolean configParameterValue = (Boolean) getConfigParameterValue(PARAM_ADD_META_DATA);
		if (configParameterValue != null) {
			addMetaData = configParameterValue;
		} else {
			addMetaData = DEFAULT_ADD_META_DATA;
		}
		LOGGER.info("Adding category, in- and outlinks and redirects to pages: " + addMetaData);
		// set param onlyArticles
		onlyArticles = (Boolean) getConfigParameterValue(PARAM_ONLY_ARTICLES);
		if (onlyArticles == null) {
			onlyArticles = DEFAULT_ONLY_ARTICLES;
			LOGGER.info("considering only articles: (default) " + onlyArticles);
		} else {
			LOGGER.info("considering only articles: " + onlyArticles);
		}

		// --------------- setting DB params ---------------//
		String dbDriverClassName = (String) getConfigParameterValue(PARAM_DB_DRIVER);
		if (dbDriverClassName == null) {
			dbDriverClassName = DEFAULT_DB_DRIVER;
		}
		LOGGER.info("DB driver class is: " + dbDriverClassName);

		String serverURL = (String) getConfigParameterValue(PARAM_SERVER_URL);
		if (serverURL == null || serverURL.isEmpty()) {
			LOGGER.error("mandatory parameter " + PARAM_SERVER_URL + " is missing");
			throw new ResourceInitializationException();
		}
		LOGGER.info("DB server URL is: " + serverURL);

		String dbName = (String) getConfigParameterValue(PARAM_DB_NAME);
		if (dbName == null || dbName.isEmpty()) {
			LOGGER.error("mandatory parameter " + PARAM_DB_NAME + " is missing");
			throw new ResourceInitializationException();
		}
		LOGGER.info("DB name is: " + dbName);

		String user = (String) getConfigParameterValue(PARAM_DB_USER);
		if (user == null || user.isEmpty()) {
			LOGGER.error("mandatory parameter " + PARAM_DB_USER + " is missing");
			throw new ResourceInitializationException();
		}
		LOGGER.info("DB user is: " + user);

		String password = (String) getConfigParameterValue(PARAM_DB_PASSWORD);
		if (password == null || password.isEmpty()) {
			// for database confugrations without a password
			password = "";
		}

		batchSize = (Integer) getConfigParameterValue(PARAM_BATCH_SIZE);
		if (batchSize == null) {
			batchSize = DEFAULT_BATCH_SIZE;
		}
		LOGGER.info("Batch size is: " + batchSize);
		// --------------- completed setting DB params ---------------//

		String dbURL = getDbURL(serverURL, dbName, dbDriverClassName);
		LOGGER.info("DB URL is: " + dbURL);

		try {
			Class.forName(dbDriverClassName);
			connection = DriverManager.getConnection(dbURL, user, password);
			this.prepareStatement(connection);
			LOGGER.debug("Established DB connection");
			totalDocumentCount = fetchUnprocessedDocumentCount();
			LOGGER.debug("Determined unprocessed document count");
		} catch (ClassNotFoundException e) {
			LOGGER.error("Exception in initialize()", e);
			throw new ResourceInitializationException(e);
		} catch (SQLException e) {
			LOGGER.error("Exception in initialize()", e);
			throw new ResourceInitializationException(e);
		}

		processedDocuments = 0;
		hostName = getHostName();
		pid = getPID();

		String[] selectedPagesArray = (String[]) getConfigParameterValue(PARAM_RESTRICT_TO_PAGES);
		if (selectedPagesArray != null && selectedPagesArray.length > 0) {
			LOGGER.info("Reading only pages in the given page list.");
			selectedPages = new LinkedList<String>();
			for (String page : selectedPagesArray) {
				selectedPages.add(page);
			}
			totalDocumentCount = selectedPages.size();
			pageRestriction = true;
		}
		// if PageList is empty and CategoryList is not, let reader only
		// consider the pages tagged with one of the categories
		else {
			String[] selectedCategoriesArray = (String[]) getConfigParameterValue(PARAM_RESTRICT_TO_CATEGORIES);
			if (selectedCategoriesArray != null && selectedCategoriesArray.length > 0) {
				LOGGER.info("Reading only pages tagged with one of the categories specified in 'CategoryList'.");
				selectedCategories = new LinkedList<String>();
				for (String cat : selectedCategoriesArray) {
					selectedCategories.add(cat);
				}
				totalDocumentCount = 0;
				pageRestriction = true;
			}
		}
	}

	private void prepareStatement(Connection connection) {
		try {
			UPDATE_DOCUMENTS_STATEMENT_NO_DISAMB_PAGES =  connection.prepareStatement(UPDATE_DOCUMENTS_NO_DISAMB_PAGES);
			UPDATE_DOCUMENTS_STATEMENT_DONE = connection.prepareStatement(UPDATE_DOCUMENTS_DONE);
			UPDATE_DOCUMENTS_BY_ID_STATEMENT = connection.prepareStatement(UPDATE_DOCUMENTS_BY_ID);
			UPDATE_DOCUMENTS_BY_PAGE_STATEMENT_NO_DISAMB_PAGES = connection.prepareStatement(UPDATE_DOCUMENTS_BY_PAGE_NO_DISAMB_PAGES);
			SELECT_PAGE_IDS_GIVEN_CAT_NAME_STATEMENT = connection.prepareStatement(SELECT_PAGE_IDS_GIVEN_CAT_NAME_LIMIT);
			SELECT_DOCUMENTS_STATEMENT = connection.prepareStatement(SELECT_DOCUMENTS);
			SELECT_PAGE_IDS_GIVEN_CAT_NAME_STATEMENT_NO_DISAMB_PAGES = connection.prepareStatement(SELECT_PAGE_IDS_GIVEN_CAT_NAME_NO_DISAMB_PAGES);

			SELECT_REDIRECTS_BY_ID_STATEMENT = connection.prepareStatement(SELECT_REDIRECTS_BY_ID);
			SELECT_CATEGORIES_BY_PAGE_ID_STATEMENT = connection.prepareStatement(SELECT_CATEGORIES_BY_PAGE_ID);
			SELECT_IN_LINKS_BY_PAGE_ID_STATEMENT = connection.prepareStatement(SELECT_IN_LINKS_BY_PAGE_ID);
			SELECT_OUT_LINKS_BY_PAGE_ID_STATEMENT = connection.prepareStatement(SELECT_OUT_LINKS_BY_PAGE_ID);
			UPDATE_DOCUMENTS_STATEMENT = connection.prepareStatement(UPDATE_DOCUMENTS);
			UPDATE_DOCUMENTS_BY_PAGE_STATEMENT = connection.prepareStatement(UPDATE_DOCUMENTS_BY_PAGE);
		} catch (SQLException e) {
			LOGGER.error("error while preparing Statements ", e);
		}
	}

	private String getDbURL(String serverURL, String dbName, String dbDriverClassName) {
		if (dbDriverClassName.equals("org.hsqldb.jdbcDriver"))
			return "jdbc:hsqldb:hsql://" + serverURL + "/" + dbName;
		return "jdbc:mysql://" + serverURL + ":3306/" + dbName;
	}

	private Set<String> getArrayFromHashMap(HashMap<Integer, Set<String>> map, int id) {
		Set<String> set = map.get(id);
		if (set == null) {
			return new HashSet<String>();
		}
		return set;
	}

	private HashMap<Integer, Set<String>> getResultStrings(PreparedStatement statement, int minArticleId, int maxArticleId) throws SQLException {
		HashMap<Integer, Set<String>> resultStrings = new HashMap<Integer, Set<String>>(batchSize, 1.0f);
		statement.setInt(1, minArticleId);
		statement.setInt(2, maxArticleId);
		ResultSet resultSet = statement.executeQuery();
		while (resultSet.next()) {
			String resultString = resultSet.getString(1);
			int resultId = resultSet.getInt(2);
			Set<String> set = resultStrings.get(resultId);
			if (set == null) {
				set = new HashSet<String>();
				resultStrings.put(resultId, set);
			}
			set.add(resultString);
		}
		resultSet.close();
		return resultStrings;
	}

	protected int fetchUnprocessedDocumentCount() throws SQLException, ResourceInitializationException {
		ResultSet resultSet = connection.createStatement().executeQuery(SELECT_UNPROCESSED_DOCUMENT_COUNT);
		int count = 0;
		// use resultSet.next() instead of resultSet.first(), because not every
		// DBMS supports resultSet.first()
		if (resultSet.next()) {
			count = resultSet.getInt(1);
		} else {
			LOGGER.error("No result counting unprocessed documents.");
			throw new ResourceInitializationException();
		}
		resultSet.close();
		return count;
	}

	/*
	 * If you overwrite this method you have to call super.hasNext().
	 * 
	 * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#hasNext()
	 */
	public boolean hasNext() throws IOException, CollectionException {
		if (idIterator == null || !idIterator.hasNext()) {
			articles = new HashMap<Integer, Article>(batchSize, 1.0f);
			try {
				fetchDocuments();
				idIterator = articles.keySet().iterator();
				totalDocumentCount = articles.size();
			} catch (SQLException e) {
				LOGGER.error("Exception in hasNext()", e);
				throw new CollectionException(e);
			}
		}
		return idIterator.hasNext();
	}

	protected void fetchDocuments() throws SQLException, CollectionException {
		ResultSet resultSet = updateAndSelectArticles();

		minId = Integer.MAX_VALUE;
		maxId = 0;
		while (resultSet != null && resultSet.next()) {
			// id,name, isDisambiguation, text,id
			Integer id = resultSet.getInt(1);
			Article article = new Article();
			article.setDisambiguation(resultSet.getBoolean(3));
			article.setId(id);
			article.setText(resultSet.getString(4));
			article.setName(resultSet.getString(2));
			minId = Math.min(minId, id);
			maxId = Math.max(maxId, id);

			articles.put(id, article);
		}
		if (addMetaData) {
			HashMap<Integer, Set<String>> redirects = getResultStrings(SELECT_REDIRECTS_BY_ID_STATEMENT, minId, maxId);
			// get categories
			HashMap<Integer, Set<String>> categories = getResultStrings(SELECT_CATEGORIES_BY_PAGE_ID_STATEMENT, minId, maxId);
			// get inLinks
			HashMap<Integer, Set<String>> inLinks = getResultStrings(SELECT_IN_LINKS_BY_PAGE_ID_STATEMENT, minId, maxId);
			// get outLinks
			HashMap<Integer, Set<String>> outLinks = getResultStrings(SELECT_OUT_LINKS_BY_PAGE_ID_STATEMENT, minId, maxId);

			for (Article article : articles.values()) {
				int id = article.getId();
				if (article.getName() != null && !article.getName().isEmpty() && article.getText() != null && !article.getText().isEmpty()) {
					article.setRedirects(getArrayFromHashMap(redirects, id));
					article.setCategories(getArrayFromHashMap(categories, id));
					article.setInLinks(getArrayFromHashMap(inLinks, id));
					article.setOutLinks(getArrayFromHashMap(outLinks, id));
				} else {
					LOGGER.error("Exception in getNextArticle(): no title or text was found for article with id: " + id);
					throw new CollectionException();
				}

			}
		}
	}

	private ResultSet updateAndSelectArticles() throws SQLException {
		UPDATE_DOCUMENTS_STATEMENT_DONE.setString(1, pid);
		UPDATE_DOCUMENTS_STATEMENT_DONE.setString(2, pid + "iP");
		UPDATE_DOCUMENTS_STATEMENT_DONE.setString(3, hostName);
		UPDATE_DOCUMENTS_STATEMENT_DONE.executeUpdate();

		PreparedStatement statement = null;
		if (pageRestriction) {
			if (selectedCategories != null && selectedCategories.size() > 0) {
				String name = selectedCategories.poll();
				markDocumentsCategory(name);
			} else if (selectedPages != null && selectedPages.size() > 0) {
				String name = selectedPages.poll();
				markDocumentByPage(name);
			} else {
				return null;
			}
		} else {
			markDocuments(hostName, pid);
		}
		statement = SELECT_DOCUMENTS_STATEMENT;
		statement.setString(1, hostName);
		statement.setString(2, pid + "iP");

		ResultSet resultSet = statement.executeQuery();
		return resultSet;
	}

	private void markDocumentsCategory(String name) throws SQLException {
		PreparedStatement st;
		if(onlyArticles){
			st = SELECT_PAGE_IDS_GIVEN_CAT_NAME_STATEMENT_NO_DISAMB_PAGES;
		}else
			st = SELECT_PAGE_IDS_GIVEN_CAT_NAME_STATEMENT;

		st.setString(1, name);

		ResultSet resultSet = SELECT_PAGE_IDS_GIVEN_CAT_NAME_STATEMENT.executeQuery();
		while (resultSet.next()) {
			int id = resultSet.getInt(1);
			UPDATE_DOCUMENTS_BY_ID_STATEMENT.setString(1, hostName);
			UPDATE_DOCUMENTS_BY_ID_STATEMENT.setString(2, pid + "iP");
			UPDATE_DOCUMENTS_BY_ID_STATEMENT.setInt(3, id);
			int affectedRows = UPDATE_DOCUMENTS_BY_ID_STATEMENT.executeUpdate();
			if(affectedRows>0)
				totalDocumentCount++;
		}
	}

	private void markDocumentByPage(String name) throws SQLException {
		PreparedStatement st;
		if(onlyArticles){
			st = UPDATE_DOCUMENTS_BY_PAGE_STATEMENT_NO_DISAMB_PAGES;
		}else
			st = UPDATE_DOCUMENTS_BY_PAGE_STATEMENT;

		
		st.setString(1, hostName);
		st.setString(2, pid + "iP");
		st.setString(3, name);
		st.executeUpdate();
	}

	protected void markDocuments(String hostName, String pid) throws SQLException {
		PreparedStatement st;
		if(onlyArticles){
			st = UPDATE_DOCUMENTS_STATEMENT_NO_DISAMB_PAGES;
		}else
			st = UPDATE_DOCUMENTS_STATEMENT;

		st.setString(1, hostName);
		st.setString(2, pid + "iP");
		st.executeUpdate();
	}

	public void close() throws IOException {
		try {
			SELECT_DOCUMENTS_STATEMENT = connection.prepareStatement(SELECT_DOCUMENTS);
			SELECT_REDIRECTS_BY_ID_STATEMENT.close();
			SELECT_CATEGORIES_BY_PAGE_ID_STATEMENT.close();
			SELECT_IN_LINKS_BY_PAGE_ID_STATEMENT.close();
			SELECT_OUT_LINKS_BY_PAGE_ID_STATEMENT.close();
			UPDATE_DOCUMENTS_STATEMENT.close();
			SELECT_PAGE_IDS_GIVEN_CAT_NAME_STATEMENT.close();
			connection.close();
		} catch (SQLException e) {
			LOGGER.error("Exception in close()", e);
			throw new IOException(e);
		}
	}

	public Progress[] getProgress() {
		return new Progress[] { new ProgressImpl(processedDocuments, totalDocumentCount, Progress.ENTITIES, true) };
	}

	public String getPID() {
		String id = ManagementFactory.getRuntimeMXBean().getName();
		return id.substring(0, id.indexOf("@"));
	}

	public String getHostName() {
		InetAddress address;
		String hostName;
		try {
			address = InetAddress.getLocalHost();
			hostName = address.getHostName();
		} catch (UnknownHostException e) {
			throw new IllegalStateException(e);
		}
		return hostName;
	}

}
