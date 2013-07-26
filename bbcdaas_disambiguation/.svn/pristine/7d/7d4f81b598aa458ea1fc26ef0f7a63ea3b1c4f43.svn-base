package de.bbcdaas.disambiguation.core.interfaces;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.dao.exceptions.ApiException;
import java.util.List;
import java.util.Map;

/**
 * Interface for all document scorer implementations.
 * @author Robert Illers
 */
public interface DocumentScorer {
	
	/**
	 * Scores documents.
	 * @param surfaceFormsDocuments
	 * @throws ApiException 
	 */
	public void scoreDocuments(Map<String, List<Document>> surfaceFormsDocuments) throws ApiException;
	
	/**
	 * Set if this scorer should be executed.
	 * @param scorerActive 
	 */
	public void setScorerActive(boolean scorerActive);
}
