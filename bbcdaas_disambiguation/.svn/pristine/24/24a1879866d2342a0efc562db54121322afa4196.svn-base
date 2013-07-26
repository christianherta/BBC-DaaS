package de.bbcdaas.disambiguation.core.interfaces;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.dao.exceptions.ApiException;
import java.util.List;
import java.util.Map;

/**
 * Interface for all classes implementing a candidate finder for documents.
 * @author Robert Illers
 */
public interface DocumentCandidateFinder {
	
	/**
	 * Sets the categorizer that should be used to categorize the found candidate documents.
	 * @param categorizer the document categorizer
	 */
	public void setDocumentCategorizer(DocumentCategorizer categorizer);
	
	/**
	 * Gets the categorizer that should be used to categorize the found candidate documents.
	 * @return the document categorizer
	 */
	public DocumentCategorizer getDocumentCategorizer();
	
	/**
	 * Searches for documents where the title matches with a list of rated pattern
	 * related to a list of surface names.
	 * @param surfaceNames
	 * @param allRatedQueryPattern
	 * @param candidateFinder_maxTermDocuments
	 * @param maxTermDocumentsPerPattern
	 * @param alternativeURIRating 
	 * @return candidate documents sorted by its surface name
	 * @throws ApiException 
	 */
	public Map<String, List<Document>> findCandidateDocuments(List<String> surfaceNames,
		Map<Float, List<String>> allRatedQueryPattern, int candidateFinder_maxTermDocuments,
		int maxTermDocumentsPerPattern, float alternativeURIRating) throws ApiException;
}
