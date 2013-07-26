package de.bbcdaas.disambiguation.wikipedia.engine.lucene.scoring;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.disambiguation.core.impl.AbstractDocumentScorer;
import de.bbcdaas.disambiguation.core.interfaces.DocumentScorer;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiDisambiguationEngineConfig;
import de.bbcdaas.disambiguation.wikipedia.dataimport.lucene.WikiContentHandler;
import java.util.*;
import org.apache.commons.math.linear.OpenMapRealVector;
import org.apache.commons.math.linear.RealVectorFormat;
import org.apache.commons.math.linear.SparseRealVector;
import org.apache.lucene.index.TermFreqVector;

/**
 * Scorer using consineSimilarity for text comparison of the documents content field,
 * need to increase heap memory of webserver
 * @author Robert Illers
 */
public final class CosSimilarityDocumentScorer extends AbstractDocumentScorer <WikiDisambiguationEngineConfig> implements DocumentScorer {

    private static Map<String, Integer> allUniqueFieldTerms = new HashMap<String, Integer>();

    /**
	 * Constructor
	 * @param config
	 */
	public CosSimilarityDocumentScorer(WikiDisambiguationEngineConfig config) {
		super(config);
	}

    /**
	 * Prepares the documents fields if additional data is needed from index for this scorer.
	 * @param surfaceFormsDocuments
	 * @throws ApiException
	 */
	@Override
	protected void prepare(Map<String, List<Document>> surfaceFormsDocuments) throws ApiException {

		// important: at the first call of the prepare method the term list will be
		// created in memory. all following request will be much faster after data
		// is cached.
		if (allUniqueFieldTerms.isEmpty()) {
			allUniqueFieldTerms = this.getConfig().getConnector().getLuceneAPI().
				getAllUniqueFieldTerms(WikiContentHandler.FIELD_CONTENT);
		}

		// prepare document fields
		for (Map.Entry<String, List<Document>> surfaceFormDocuments : surfaceFormsDocuments.entrySet()) {

			for (Document document : surfaceFormDocuments.getValue()) {

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

    /**
     *
     * @param surfaceFormsDocuments
     * @throws ApiException
     */
    @Override
    public Map<String, List<Float>> scoreDocumentsImpl(Map<String, List<Document>> surfaceFormsDocuments) throws ApiException {

		if (this.getConfig().isEnableLogs()) {
			logger.info("---CosSimilarityDocumentScorer:---");
		}
		this.prepare(surfaceFormsDocuments);
        int uniqueFieldTermsSize = allUniqueFieldTerms.size();

		// scores as map (will be set to documents in abstract scorer)
		Map<String, List<Float>> scores = new HashMap<String, List<Float>>();
		for (String surfaceForm : surfaceFormsDocuments.keySet()) {
			scores.put(surfaceForm, new ArrayList<Float>());
		}

		for (Map.Entry<String, List<Document>> surfaceFormDocuments1 : surfaceFormsDocuments.entrySet()) {

            for (Document document1 : surfaceFormDocuments1.getValue()) {

                if (document1.getType() == Document.DOCUMENT_TYPE_ARTICLE) {

                    FieldVector fieldVector1 = new FieldVector(uniqueFieldTermsSize, document1.getType());
                    Map<String, Integer> fieldFrequencyVector1 = document1.
                        getFieldByName(WikiContentHandler.FIELD_CONTENT).getFieldFrequencyVector();

                    for (String term : fieldFrequencyVector1.keySet()) {
                        fieldVector1.setEntry(allUniqueFieldTerms, term, fieldFrequencyVector1.get(term));
                        //logger.debug("term: "+term+", frequency: "+fieldFrequencyVector1.get(term));
                    }
                    float score = 0.0f;

                    for (Map.Entry<String, List<Document>> surfaceFormDocuments2 : surfaceFormsDocuments.entrySet()) {

                        if (!surfaceFormDocuments2.getKey().equals(surfaceFormDocuments1.getKey())) {

                            for (Document document2 : surfaceFormDocuments2.getValue()) {

                                if (document2.getType() == Document.DOCUMENT_TYPE_ARTICLE) {

                                    FieldVector fieldVector2 = new FieldVector(uniqueFieldTermsSize, document2.getType());
                                    Map<String, Integer> fieldFrequencyVector2 = document2.
                                        getFieldByName(WikiContentHandler.FIELD_CONTENT).getFieldFrequencyVector();

									for (String term : fieldFrequencyVector2.keySet()) {
                                        fieldVector2.setEntry(allUniqueFieldTerms, term, fieldFrequencyVector2.get(term));
                                    }

                                    // BUG?: size only for articles?
                                    score += (float)this.getCosineSimilarity(fieldVector1, fieldVector2) /
										surfaceFormDocuments2.getValue().size();
                                }
                            }
                        }
                    }
					if (this.getConfig().isEnableLogs()) {
						logger.info(new StringBuilder("Added cosSimilarity Score '").append(score).append("' to URI '").
								append(document1.getFieldByName(WikiContentHandler.FIELD_URI).getValue()).append("'.").toString());
					}
                    scores.get(surfaceFormDocuments1.getKey()).add(score);
                    //System.gc();
                }else {
					// not scored, but fill result map to prevent gaps
					scores.get(surfaceFormDocuments1.getKey()).add(0.0f);
				}
            }
        }
		return scores;
    }

    /**
     *
     * @param fieldVector1
     * @param fieldVector2
     * @return
     */
    private double getCosineSimilarity(FieldVector fieldVector1, FieldVector fieldVector2) {
        return (fieldVector1.getVector().dotProduct(fieldVector2.getVector())) /
            (fieldVector1.getVector().getNorm() * fieldVector2.getVector().getNorm());
    }

    /**
     *
     * @author Robert Illers
     */
    private class FieldVector {

        private SparseRealVector vector;
        private Integer documentType = Document.DOCUMENT_TYPE_UNDEFINED;

        /**
         *
         * @return
         */
        public SparseRealVector getVector() {
            return this.vector;
        }

        /**
         *
         * @return
         */
        public Integer getDocumentType() {
            return documentType;
        }

        /**
         * Constructor
         * @param terms
         */
        public FieldVector(int allUniqueFieldTermsSize, int documentType) {

            this.vector = new OpenMapRealVector(allUniqueFieldTermsSize);
            this.documentType = documentType;
        }

        /**
         *
         * @param term
         * @param freq
         */
        public void setEntry(Map<String, Integer> allUniqueFieldTerms, String term, int freq) {

            if (allUniqueFieldTerms.containsKey(term)) {

                int pos = allUniqueFieldTerms.get(term);
                this.vector.setEntry(pos, freq);
            }
        }

        /**
         *
         */
        public void normalize() {

            double sum = this.vector.getL1Norm();
			logger.debug("L1Norm: "+sum);
            this.vector = (SparseRealVector) this.vector.mapDivide(sum);
        }

        /**
         *
         * @return
         */
        @Override
        public String toString() {

            RealVectorFormat formatter = new RealVectorFormat();
            return formatter.format(this.vector);
        }
    }
}