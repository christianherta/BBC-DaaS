/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bbcdaas.disambiguation.wikipedia.engine.lucene.scoring;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.disambiguation.core.impl.AbstractDocumentScorer;
import de.bbcdaas.disambiguation.core.interfaces.DocumentScorer;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiDisambiguationEngineConfig;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Robert Illers
 */
public final class CosSimilarityDocumentScorer2 extends AbstractDocumentScorer <WikiDisambiguationEngineConfig> implements DocumentScorer {

	public CosSimilarityDocumentScorer2(WikiDisambiguationEngineConfig config) {
		super(config);
	}
	
	@Override
	protected Map<String, List<Float>> scoreDocumentsImpl(Map<String, List<Document>> surfaceFormsDocuments) throws ApiException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected void prepare(Map<String, List<Document>> surfaceFormsDocuments) throws ApiException {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	private double cosineSimBetweenTwoDocs(float doc1[], float doc2[]) {
		
		double temp;
		int doc1Len = doc1.length;
		int doc2Len = doc2.length;
		float numerator = 0;
		float temSumDoc1 = 0;
		float temSumDoc2 = 0;
		double equlideanNormOfDoc1 = 0;
		double equlideanNormOfDoc2 = 0;
		if (doc1Len > doc2Len) {
			for (int i = 0; i < doc2Len; i++) {
				numerator += doc1[i] * doc2[i];
				temSumDoc1 += doc1[i] * doc1[i];
				temSumDoc2 += doc2[i] * doc2[i];
			}
			equlideanNormOfDoc1=Math.sqrt(temSumDoc1);
			 equlideanNormOfDoc2=Math.sqrt(temSumDoc2);
		} else {
			for (int i = 0; i < doc1Len; i++) {
				numerator += doc1[i] * doc2[i];
				temSumDoc1 += doc1[i] * doc1[i];
				temSumDoc2 += doc2[i] * doc2[i];
			}
			 equlideanNormOfDoc1=Math.sqrt(temSumDoc1);
			 equlideanNormOfDoc2=Math.sqrt(temSumDoc2);
		}

		temp = numerator / (equlideanNormOfDoc1 * equlideanNormOfDoc2);
		return temp;
	}
	
}
