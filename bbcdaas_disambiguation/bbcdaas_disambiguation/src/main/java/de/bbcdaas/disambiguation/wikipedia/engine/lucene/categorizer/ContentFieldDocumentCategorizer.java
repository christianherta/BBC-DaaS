package de.bbcdaas.disambiguation.wikipedia.engine.lucene.categorizer;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.beans.document.Field;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.disambiguation.core.impl.AbstractDocumentCategorizer;
import de.bbcdaas.disambiguation.core.interfaces.DocumentCategorizer;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiDisambiguationEngineConfig;
import de.bbcdaas.disambiguation.wikipedia.dataimport.lucene.WikiContentHandler;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.lucene.index.TermFreqVector;
/**
 * Document Categorizer for documents of a lucene index created from a wikipedia dump. 
 * @author Robert Illers
 */
public final class ContentFieldDocumentCategorizer extends AbstractDocumentCategorizer <WikiDisambiguationEngineConfig> implements DocumentCategorizer {
    
	/**
	 * Constructor
	 * @param config 
	 */
	public ContentFieldDocumentCategorizer(WikiDisambiguationEngineConfig config) {
		super(config);
	}
	
	/**
	 * Categorizes a document.
	 * @param document
	 * @return documentType
	 */
	@Override
	public int categorizeDocument(Document document) {
		
		// 1. verify if document describes a redirect page
        Field redirectTitle = document.getFieldByName(WikiContentHandler.FIELD_REDIRECT_TITLE);
		if (redirectTitle != null && !redirectTitle.getValue().isEmpty()) {
			return Document.DOCUMENT_TYPE_REDIRECT;
		} 
        // 2. verify if document describes a disambiguation page
        Field altTitles = document.getFieldByName(WikiContentHandler.FIELD_ALTERNATIVE_TITLES);
        if (altTitles != null && !altTitles.getValue().isEmpty()) {
            return Document.DOCUMENT_TYPE_DISAMBIGUATION;
        }
        // 3. verify if document is an article
        try {
            TermFreqVector contentTermVector = this.getConfig().getConnector().getLuceneAPI().
                getTermFrequencyVector(document.getID(), WikiContentHandler.FIELD_CONTENT);
            if (contentTermVector != null && contentTermVector.getTerms().length != 0) {
                return Document.DOCUMENT_TYPE_ARTICLE;
            }
        } catch(ApiException e) {
            logger.error(e);
        }
		return Document.DOCUMENT_TYPE_UNDEFINED;
	}

	/**
	 * Categorizes a list of documents, the type will be set into the document 
	 * objects.
	 * @param termValuesDocuments 
	 */
	@Override
	protected void categorizeDocumentsImpl(Map<String, List<Document>> surfaceFormsDocuments) {
		
		for (Entry<String, List<Document>> surfaceFormDocuments : surfaceFormsDocuments.entrySet()) {
			for (Document document : surfaceFormDocuments.getValue()) {
				document.setType(this.categorizeDocument(document));
			}
		}
	}	

	/**
	 * Returns the number of documents that have a specific type assuming they
	 * have already a type.
	 * @param documents
	 * @param documentType
	 * @return number of documents with a specific type
	 */
	@Override
	public int getNumberOfTypedDocuments(List<Document> documents, int documentType) {
		
		int numberOfTypedDocuments = 0;
		for (Document document : documents) {
			if (document.getType() == documentType) {
				numberOfTypedDocuments++;
			}
		}
		
		return numberOfTypedDocuments;
	}
}
