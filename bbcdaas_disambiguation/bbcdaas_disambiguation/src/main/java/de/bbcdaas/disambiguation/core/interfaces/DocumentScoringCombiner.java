package de.bbcdaas.disambiguation.core.interfaces;

import de.bbcdaas.common.beans.document.Document;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for all implementations of document scoring combiner.
 * @author Robert Illers
 */
public interface DocumentScoringCombiner {

	/**
	 * Sets the active state of the scorer depending on scoring combiner parameter.
	 * @param documentScorer
	 */
	public void setDocumentScorerActivations(Set<DocumentScorer> documentScorer);

	/**
	 *
	 * @param surfaceNamesDocuments
	 * @param documentScorer
	 * @return true if no known error occured
	 */
	public boolean calulateCombinedScores(Map<String,List<Document>> surfaceNamesDocuments,
		Set<DocumentScorer> documentScorer);
}
