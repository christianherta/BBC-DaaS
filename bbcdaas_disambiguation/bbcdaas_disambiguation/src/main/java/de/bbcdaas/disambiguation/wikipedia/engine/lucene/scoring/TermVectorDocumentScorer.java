package de.bbcdaas.disambiguation.wikipedia.engine.lucene.scoring;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.disambiguation.core.impl.AbstractDocumentScorer;
import de.bbcdaas.disambiguation.core.interfaces.DocumentScorer;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiDisambiguationEngineConfig;
import de.bbcdaas.disambiguation.wikipedia.dataimport.lucene.WikiContentHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.WildcardQuery;

/**
 * Algorithm for scoring documents based on lucene term vectors of the documents
 * content field
 *
 * @author Robert Illers
 */
public final class TermVectorDocumentScorer extends AbstractDocumentScorer <WikiDisambiguationEngineConfig> implements DocumentScorer {

	/**
	 * Constructor
	 * @param config
	 */
	public TermVectorDocumentScorer(WikiDisambiguationEngineConfig config) {
		super(config);
	}

	/**
	 * Scores documents.
	 * @param surfaceFormsDocuments
	 * @throws ApiException
	 */
	@Override
	public Map<String, List<Float>> scoreDocumentsImpl(Map<String, List<Document>> surfaceFormsDocuments) throws ApiException {

		if (this.getConfig().isEnableLogs()) {
			logger.info("---TermVectorDocumentScorer:---");
		}
		this.prepare(surfaceFormsDocuments);

		// scores as map (will be set to documents in abstract scorer)
		Map<String, List<Float>> scores = new HashMap<String, List<Float>>();
		for (String surfaceForm : surfaceFormsDocuments.keySet()) {
			scores.put(surfaceForm, new ArrayList<Float>());
		}

		// for each surfaceForm...
		for (Map.Entry<String, List<Document>> surfaceFormDocuments1 : surfaceFormsDocuments.entrySet()) {

			// .. iterate through its found documents...
			for (Document document1 : surfaceFormDocuments1.getValue()) {

				if (document1.getType() == Document.DOCUMENT_TYPE_ARTICLE) {

					BooleanQuery otherTermDocumentTitlesQuery = new BooleanQuery();

					int maxNumberOfDocumentsToCompare = 0;
					// ... and add for the documents of each other termValue...
					for (Map.Entry<String, List<Document>> termValueDocuments2 : surfaceFormsDocuments.entrySet()) {

						if (!termValueDocuments2.getKey().equals(surfaceFormDocuments1.getKey())) {

							maxNumberOfDocumentsToCompare += termValueDocuments2.getValue().size();
							for (Document document2 : termValueDocuments2.getValue()) {

								if (document2.getType() == Document.DOCUMENT_TYPE_ARTICLE) {

									// ... an title bool query to restrict result to the candidates
									otherTermDocumentTitlesQuery.add(new WildcardQuery(new Term(WikiContentHandler.FIELD_TITLE,
											document2.getFieldByName(WikiContentHandler.FIELD_TITLE).getValue())),
											BooleanClause.Occur.SHOULD);

								}
							}

						}
					}

					// build lucene query for scoring content field

					BooleanQuery currentTermContentTermsQuery = new BooleanQuery();

					// for each term value of the field "content" ...
					for (String contentTerm : document1.getFieldByName(WikiContentHandler.FIELD_CONTENT).getValues()) {

						// ... add a boolean query
						currentTermContentTermsQuery.add(new WildcardQuery(new Term(WikiContentHandler.FIELD_CONTENT,
								contentTerm)), BooleanClause.Occur.SHOULD);
					}

					BooleanQuery contentFieldScoringQuery = new BooleanQuery();
					contentFieldScoringQuery.add(otherTermDocumentTitlesQuery, BooleanClause.Occur.MUST);
					contentFieldScoringQuery.add(currentTermContentTermsQuery, BooleanClause.Occur.MUST);

					//logger.debug("Lucene Query: "+contentFieldScoringQuery.toString());

					// end build lucene query for scoring content field

					// use the build query to get the documents of all term values with lucene score
					// cosine similarity: V(query)*V(content) / |V(query)|*|V(content)|
					// Lucene: coord-factor(query,content) * query-boost(query) * V(query) * V(content) / |V(query)| * doc-len-norm(content) * doc-boost(document)

					List<ScoreDoc> hits = this.getConfig().getConnector().getLuceneAPI().
                        executeLuceneSearchQuery(contentFieldScoringQuery, maxNumberOfDocumentsToCompare);

					Float luceneScore = 0f;
					// calculate overall scores from lucene scores
					// for each document found with the linkedBooleanQuery...
					for (ScoreDoc hit : hits) {
						// for each term value...
						for (Map.Entry<String, List<Document>> surfaceFormDocuments2 : surfaceFormsDocuments.entrySet()) {
							// for each document found for the term value...
							for (Document document : surfaceFormDocuments2.getValue()) {
								// if document of the term value also found in linkedBooleanQuery...
								if (document.getID() == hit.doc) {
									// ....increase score of this document by the score of the found
									// document divied through the amount of documents found for the term value by its title
                                    // BUG?: size only for articles?
									luceneScore += hit.score / surfaceFormDocuments2.getValue().size();
								}
							}
						}
					}
					if (this.getConfig().isEnableLogs()) {
						logger.info(new StringBuilder("Added Lucene Score '").append(luceneScore).append("' to URI '").
								append(document1.getFieldByName(WikiContentHandler.FIELD_URI).getValue()).append("'.").toString());
					}
					scores.get(surfaceFormDocuments1.getKey()).add(luceneScore);
				} else {
					// not scored, but fill result map to prevent gaps
					scores.get(surfaceFormDocuments1.getKey()).add(0.0f);
				}
			}
			// end iterate through its found documents
		}
		return scores;
	}

	/**
	 * Prepares the documents fields if additional data is needed from index for this scorer.
	 * @param termValuesDocuments
	 * @throws ApiException
	 */
	@Override
	protected void prepare(Map<String, List<Document>> termValuesDocuments) throws ApiException {

		// prepare document fields
		for (Map.Entry<String, List<Document>> termValueDocuments : termValuesDocuments.entrySet()) {

			for (Document document : termValueDocuments.getValue()) {

                // get lucene termVector
				if (document.getType() == Document.DOCUMENT_TYPE_ARTICLE &&
                    (document.getFieldByName(WikiContentHandler.FIELD_CONTENT) == null ||
                     document.getFieldByName(WikiContentHandler.FIELD_CONTENT).getFieldFrequencyVector().isEmpty())) {

					// get the terms of the content field term vector for each document
					TermFreqVector termFrequencyVector = this.getConfig().getConnector().getLuceneAPI().
						getTermFrequencyVector(document.getID(), WikiContentHandler.FIELD_CONTENT);

					if (termFrequencyVector != null) {

						document.addField(WikiContentHandler.FIELD_CONTENT,
							Arrays.asList(termFrequencyVector.getTerms()), false, false, false);

						Map<String, Integer> fieldFrequencyVector = new TreeMap<String, Integer>();
						int i = 0;
						for (String term : termFrequencyVector.getTerms()) {
							fieldFrequencyVector.put(term, termFrequencyVector.getTermFrequencies()[i]);
							i++;
						}
						document.getFieldByName(WikiContentHandler.FIELD_CONTENT).
							setFieldFrequencyVector(fieldFrequencyVector);
					}
				}
			}
		}
	}
}
