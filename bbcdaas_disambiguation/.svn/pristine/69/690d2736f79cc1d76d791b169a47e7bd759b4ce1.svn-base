package de.bbcdaas.disambiguation.core.interfaces;

import de.bbcdaas.common.beans.document.Document;
import java.util.List;
import java.util.Map;

/**
 * Interface for all document categorizer.
 * @author Robert Illers
 */
public interface DocumentCategorizer {
	
	/**
	 * Categorizes a document.
	 * @param document wikipedia document
	 * @return documentType
	 */
	public int categorizeDocument(Document document);
	
	/**
	 * Categorizes a list of documents, the type will be set into the document 
	 * objects.
	 * @param surfaceNamesDocuments documents sorted by its surface name
	 */
	public void categorizeDocuments(Map<String, List<Document>> surfaceNamesDocuments);
	
	/**
	 * Returns the number of documents that have a specific type assuming they
	 * have already a type.
	 * @param documents
	 * @param documentType
	 * @return number of documents with a specific type
	 */
	public int getNumberOfTypedDocuments(List<Document> documents, int documentType);
}
