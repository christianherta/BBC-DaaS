package de.bbcdaas.disambiguation.core.interfaces;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.disambiguation.core.configs.AbstractDisambiguationEngineConfig;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Robert Illers
 */
public interface DisambiguationEngine<T extends AbstractDisambiguationEngineConfig> {

	/**
	 * Scores the documents found for each surfaceForm.
	 * @param surfaceForms Strings that could match a term in the index
	 * @return documents grouped by the surfaceForm they belong to
	 */
	public Map<String, List<Document>> scoreDocuments(List<String> surfaceForms, boolean keepConnection);

	/**
	 *
	 * @param surfaceForm
	 * @param maxHits
	 * @return
	 */
	public List<Document> getDocumentsByTitle(String surfaceForm, int maxHits);

	public T getConfig();
}
