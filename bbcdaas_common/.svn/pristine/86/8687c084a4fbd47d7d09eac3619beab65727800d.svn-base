package de.bbcdaas.common.dao.api;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.beans.document.Field;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.common.util.FileWriter;
import de.bbcdaas.common.util.PerformanceUtils;
import java.io.File;
import java.io.IOException;
import java.util.*;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LimitTokenCountAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * Wrapper that covers the lucene api and allows easier usage.
 * @author Robert Illers
 */
public class LuceneAPI implements PersistenceAPI {

    private static final Logger logger = Logger.getLogger(LuceneAPI.class);
    private Version luceneVersion;
    private FileWriter fileWriter = new FileWriter();
    private Analyzer analyzer;
    private IndexWriterConfig config;
    private String indexPath;
    private Directory index;
    private boolean connectionEstablished = false;
    private IndexWriter indexWriter = null;
    private IndexReader indexReader = null;
    private IndexSearcher indexSearcher;
    private Integer limitedTokenCount = 0;
	private boolean enableLogs = true;

    /**
     * Constructor
     * @param luceneVersion
     */
    public LuceneAPI(Version luceneVersion, Analyzer analyzer) {

        this.luceneVersion = luceneVersion;
        this.analyzer = analyzer;
    }

	/**
     * Constructor
     * @param luceneVersion
     * @param indexPath
     * @param analyzer
     * @param limitedTokenCount if greater 0 a limitedTokenCount Analyzer wrapper will be used
     * @throws ApiException
     */
	public LuceneAPI(Version luceneVersion, String indexPath, Analyzer analyzer, int limitedTokenCount) throws ApiException {

            this.analyzer = analyzer;
            this.indexPath = indexPath;
            this.luceneVersion = luceneVersion;
            this.limitedTokenCount = limitedTokenCount;
			if (limitedTokenCount > 0) {
				this.config = new IndexWriterConfig(this.luceneVersion,
					new LimitTokenCountAnalyzer(this.analyzer, this.limitedTokenCount));
			} else {
				this.config = new IndexWriterConfig(this.luceneVersion, this.analyzer);
			}
	}

    /**
     *
     * @return
     */
    public Version getLuceneVersion() {
        return luceneVersion;
    }

    /**
     * deletes the complete index.
     */
    public void deleteIndex() {

        if (this.indexPath != null) {
            if (this.connectionEstablished) {
                logger.error("There is an open connection to the index, skipping delete index.");
            } else {
                logger.info("Deleting index on path '"+this.indexPath+"'...");
                this.fileWriter.deleteDirectory(new File(this.indexPath));
                logger.info("Done.");
            }
        } else {
            logger.error("No index path set, skipping delete index.");
        }
    }

    /**
     * Adds a new Document into the index
     * @param doc
     * @throws ApiException
     */
    public void addDocument(Document document) throws ApiException {

        if (this.connectionEstablished && this.index != null) {
            try {
                if (this.indexWriter == null) {
                    this.indexWriter = new IndexWriter(index, config);
                }
                org.apache.lucene.document.Document luceneDocument =
                    new org.apache.lucene.document.Document();
                for (Field field : document.getFields()) {
                    String value = field.getValues().size() == 1 ? field.getValue() :
                            field.getValuesAsSemicolonSeparatedString();
                    org.apache.lucene.document.Field luceneField = new org.apache.
                        lucene.document.Field(field.getName(), value,
                        field.shouldBeStored() ?
                            org.apache.lucene.document.Field.Store.YES :
                            org.apache.lucene.document.Field.Store.NO,
                        field.shouldBeAnalyzed() ?
                        org.apache.lucene.document.Field.Index.ANALYZED :
                        org.apache.lucene.document.Field.Index.NOT_ANALYZED,
                        field.shouldCreateTermVectors() ?
                            org.apache.lucene.document.Field.TermVector.YES :
                            org.apache.lucene.document.Field.TermVector.NO);
                    luceneDocument.add(luceneField);
                }
                this.indexWriter.addDocument(luceneDocument);
            } catch (Exception ex) {
                throw new ApiException("addDocument", ex.getMessage());
            }
        } else {
            logger.error("Index not opened, adding document skipped.");
        }
    }

    /**
     *
     * @param documentID
     * @param fieldName
     * @return TermFreqVector
     * @throws ApiException
     */
    public TermFreqVector getTermFrequencyVector(int documentID, String fieldName) throws ApiException {

		TermFreqVector termFreqVector = null;
        if (this.connectionEstablished && this.indexReader != null) {
            try {
                termFreqVector = this.indexReader.getTermFreqVector(documentID, fieldName);
            } catch (Exception ex) {
                throw new ApiException("getTermFrequencyVector", ex.getMessage());
            }
        } else {
			logger.error("getTermFrequencyVector(): connection to index not established.");
		}
        return termFreqVector;
    }

	/**
	 * Searches for a document in the index (Lucene searches case sensitive!)
	 * @param searchFieldName
	 * @param queryString
	 * @param maxHits
	 * @return List<Document>
	 * @throws ApiException
	 */
    public List<Document> searchForDocuments(Query query , int maxHits) throws ApiException {

        List<Document> documents = new ArrayList<Document>();

        if (this.connectionEstablished && this.indexSearcher != null) {
            PerformanceUtils pu = new PerformanceUtils();

            TopScoreDocCollector collector = TopScoreDocCollector.create(maxHits, true);
            try {

                this.indexSearcher.search(query, collector);
				if (this.isEnableLogs()) {
					logger.info("parsed query: "+query.toString());
				}

				List<ScoreDoc> scoreDocs = new ArrayList<ScoreDoc>(Arrays.asList(collector.topDocs().scoreDocs));

				for (ScoreDoc scoreDoc : scoreDocs) {

					org.apache.lucene.document.Document luceneDocument = this.indexSearcher.doc(scoreDoc.doc);
                    Document document = new Document(scoreDoc.doc);

					for (Iterator<Fieldable> it = luceneDocument.getFields().iterator(); it.hasNext();) {

						Fieldable luceneField = it.next();
                        document.addField(luceneField.name(), luceneField.stringValue(), false, false, false);
                    }
                    documents.add(document);
                }
				if (this.isEnableLogs()) {
					logger.info("Number of found documents: "+documents.size());
				}
            } catch (Exception ex) {
                throw new ApiException("searchForDocuments", ex.getMessage());
            }
			if (this.isEnableLogs()) {
				logger.info("time needed: "+pu.getRunningTimer());
			}
        } else {
            logger.error("Index Searcher not opened, document search skipped.");
        }
        return documents;
    }

	/**
	 * Executes a lucene search query.
	 * @param luceneQuery
	 * @param maxHits
	 * @return List<ScoreDoc>
	 */
	public List<ScoreDoc> executeLuceneSearchQuery(Query luceneQuery, int maxHits) {

		List<ScoreDoc> scoreDocs = new ArrayList<ScoreDoc>();
		try {
			if(this.connectionEstablished) {

                PerformanceUtils pu = new PerformanceUtils();
				TopScoreDocCollector collector = TopScoreDocCollector.create(maxHits, true);

				if (this.indexSearcher != null) {

					this.indexSearcher.search(luceneQuery, collector);
					ScoreDoc[] hits = collector.topDocs().scoreDocs;
					scoreDocs.addAll(Arrays.asList(hits));

				} else {
					logger.error("Error in executeLuceneQuery: indexSearcher not initialized.");
					return new ArrayList<ScoreDoc>();
				}
                logger.info("time needed: "+pu.getRunningTimer());
				return scoreDocs;

			} else {
				logger.error("Error in executeLuceneQuery: No connnection to index established.");
				return new ArrayList<ScoreDoc>();
			}
		} catch (IOException ex) {
			logger.error(ex.getMessage(), ex);
			return new ArrayList<ScoreDoc>();
		}
	}

    /**
     *
     * @param indexPath
     * @throws ApiException
     */
    public void openConnection(String indexPath) throws ApiException {

        this.indexPath = indexPath;
        this.openConnection();
    }

    /**
     * Returns all unique terms that are in the given field of all documents in
     * the index.
     * @param fieldName
     * @return Map<String, Integer>
     */
    public Map<String, Integer> getAllUniqueFieldTerms(String fieldName) {

        Map<String, Integer> allUniqueFieldTerms = new HashMap<String, Integer>();
        int pos = 0;

		try {

			logger.info("Begin caching all field terms of field '"+fieldName+"' ...");
            PerformanceUtils pu = new PerformanceUtils();
            TermEnum termEnum = this.indexReader.terms(new Term(fieldName));

			while (termEnum.next()) {
                Term term = termEnum.term();
                if (!term.field().equals(fieldName)) {
                    break;
                }
                allUniqueFieldTerms.put(term.text(), pos++);
            }

            logger.info("Done. Cache size: "+allUniqueFieldTerms.size());
            logger.info("time needed: "+pu.getRunningTimer());

        } catch (IOException ex) {
            logger.error(ex);
        }
        return allUniqueFieldTerms;
    }

    /**
     * Opens a connection to the index and initializes the index writer, index reader
     * and index searcher.
     * @throws ApiException
     */
    @Override
    public void openConnection() throws ApiException {

		if (!this.connectionEstablished) {

			try {

				// no analyzer set
				if (this.analyzer == null) {

					logger.info("No analyzer set, initializing standard analyzer...");
					this.analyzer = new StandardAnalyzer(this.luceneVersion);
					this.config = new IndexWriterConfig(this.luceneVersion, this.analyzer);
				}

				// index path set
				if (this.indexPath != null && !this.indexPath.isEmpty()) {

					this.index = FSDirectory.open(new File(this.indexPath));

					// no index found
					if (!IndexReader.indexExists(this.index)) {

						logger.info("No index found at '"+this.indexPath+"', creating new empty index.");
						this.indexWriter = new IndexWriter(index, config);
						this.connectionEstablished = true;
						logger.info("Ready for write operations.");
					}

				// no index path set
				} else {

					logger.info("No index path set, initializing RAM directory...");
					this.index = new RAMDirectory();
				}

				// no writer initialized -> init reader
				if (this.indexWriter == null) {

					this.indexReader = IndexReader.open(this.index);
					this.indexSearcher = new IndexSearcher(this.indexReader);

					this.connectionEstablished = true;
					logger.info("Ready for read operations.");
				}

			} catch (CorruptIndexException ex) {
				logger.error("openConnection()", ex);
				System.exit(1);
			} catch (IOException ex) {
				logger.error("openConnection()", ex);
				System.exit(1);
			} catch (Exception ex) {
				throw new ApiException("openConnection()", ex.getMessage());
			}
		}
    }

	/**
	  * Closes all index accessors.
	 * @throws ApiException
	 */
    @Override
    public void closeConnection() throws ApiException {

		try {

            this.connectionEstablished = false;

			// close index writer
			if (this.indexWriter != null) {

				try {
					this.indexWriter.close();
				} finally {
					if (IndexWriter.isLocked(this.index)) {
						IndexWriter.unlock(this.index);
					}
				}

                this.indexWriter = null;
            }

			// close index searcher
            if (this.indexSearcher != null) {

				this.indexSearcher.close();
                this.indexSearcher = null;
            }

			// close index reader
            if (this.indexReader != null) {

				this.indexReader.close();
                this.indexReader = null;
            }

			// close index
            if (this.index != null) {
                this.index.close();
                this.index = null;
            }

		} catch (Exception ex) {
			throw new ApiException("closeConnection", ex.getMessage());
		}
    }

    /**
     *
     * @throws ApiException
     */
    @Override
    public void commit() throws ApiException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @throws ApiException
     */
    @Override
    public void commitAndCloseConnection() throws ApiException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isConnectionOpenedExternal() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param connectionOpenedExternal
     */
    @Override
    public void setConnectionOpenedExternal(boolean connectionOpenedExternal) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	@Override
	public boolean isEnableLogs() {
		return enableLogs;
	}

	@Override
	public void setEnableLogs(boolean enableLogs) {
		this.enableLogs = enableLogs;
	}
}
