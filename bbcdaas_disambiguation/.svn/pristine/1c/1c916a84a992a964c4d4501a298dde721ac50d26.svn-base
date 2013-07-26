package de.bbcdaas.disambiguation.wikipedia.engine.lucene.scoring;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.beans.document.Field;
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

/**
 * Scoring documents using extracted keywords
 * @author Robert Illers
 */
public final class KeywordDocumentScorer extends AbstractDocumentScorer <WikiDisambiguationEngineConfig> implements DocumentScorer {

	/**
	 * Constructor
	 * @param luceneAPI pi wrapper for accessing lucene index
	 */
	public KeywordDocumentScorer(WikiDisambiguationEngineConfig config) {
		super(config);
	}

	/**
	 * Scores the documents.
	 * @param termValuesDocuments
	 * @throws ApiException
	 */
	@Override
	public Map<String, List<Float>> scoreDocumentsImpl(Map<String, List<Document>> surfaceFormsDocuments) throws ApiException {

		if (this.getConfig().isEnableLogs()) {
			logger.info("---KeywordDocumentScorer:---");
		}
		this.prepare(surfaceFormsDocuments);

		// scores as map (will be set to documents in abstract scorer)
		Map<String, List<Float>> scores = new HashMap<String, List<Float>>();
		for (String surfaceForm : surfaceFormsDocuments.keySet()) {
			scores.put(surfaceForm, new ArrayList<Float>());
		}

		for (Map.Entry<String, List<Document>> surfaceFormDocuments1 : surfaceFormsDocuments.entrySet()) {

			for (Document document1 : surfaceFormDocuments1.getValue()) {

				if (document1.getType() == Document.DOCUMENT_TYPE_ARTICLE) {

					// get keywords from current document
					List<String> document1Keywords = new ArrayList<String>();
					document1Keywords.addAll(document1.getFieldByName(WikiContentHandler.FIELD_KEYWORDS).getValues());

					// number of docs from other termValues
					int numberOfArticlesFromOtherTerms = 0;
					int numberOfAllMatchingKeywords = 0;

					for (Map.Entry<String, List<Document>> surfaceFormDocuments2 : surfaceFormsDocuments.entrySet()) {

						if (!surfaceFormDocuments2.getKey().equals(surfaceFormDocuments1.getKey())) {

							for (Document document2 : surfaceFormDocuments2.getValue()) {

								if (document2.getType() == Document.DOCUMENT_TYPE_ARTICLE) {

									numberOfArticlesFromOtherTerms++;

									// check how many keywords match in the documents
									List<String> document2Keywords = new ArrayList<String>();
									document2Keywords.addAll(document2.getFieldByName(WikiContentHandler.FIELD_KEYWORDS).getValues());
									document2Keywords.retainAll(document1Keywords);
									int numberOfMatchingKeywords = document2Keywords.size();

									// add the number of matching keywords for the termValue2
									if (numberOfMatchingKeywords != 0) {

										numberOfAllMatchingKeywords += numberOfMatchingKeywords;
										document1.addTermDocsKeywordMatch(surfaceFormDocuments2.getKey(), document2.getID(), numberOfMatchingKeywords);
									}
								}
							}
						}
					}
					// calculate score
					// sum(keywordCounts) / (currDocNumberOfMatchingKeywords * numberOfDocuments)
					float score = (float)((double)numberOfAllMatchingKeywords /
									((double)document1Keywords.size() * (double)numberOfArticlesFromOtherTerms));
					if (this.getConfig().isEnableLogs()) {
						logger.info(new StringBuilder("Added Keyword Score '").append(score).append("' to URI '").
							append(document1.getFieldByName(WikiContentHandler.FIELD_URI).getValue()).append("'.").toString());
					}
					scores.get(surfaceFormDocuments1.getKey()).add(score);
				} else {
					// not scored, but fill result map to prevent gaps
					scores.get(surfaceFormDocuments1.getKey()).add(0.0f);
				}
			}
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

				if (document.getType() == Document.DOCUMENT_TYPE_ARTICLE) {

					// get the keywords of the keyword field for each document
					String keywords_String = document.getFieldByName(WikiContentHandler.FIELD_KEYWORDS).getValue();
					String[] keywords_splitted = keywords_String.split(Field.FIELD_VALUE_LIST_SEPARATOR);
					document.addField(WikiContentHandler.FIELD_KEYWORDS, Arrays.asList(keywords_splitted), false, false, false);
				}
			}
		}
	}
}
