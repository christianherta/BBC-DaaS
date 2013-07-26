package de.bbcdaas.disambiguation.core.impl;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.beans.document.Field;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.common.util.PerformanceUtils;
import de.bbcdaas.disambiguation.core.configs.AbstractDisambiguationEngineConfig;
import de.bbcdaas.disambiguation.core.interfaces.DocumentCandidateFinder;
import de.bbcdaas.disambiguation.core.interfaces.DocumentCategorizer;
import de.bbcdaas.disambiguation.wikipedia.dataimport.lucene.WikiContentHandler;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;

/**
 *
 * @author Robert Illers
 */
public abstract class AbstractDocumentCandidateFinder <T extends AbstractDisambiguationEngineConfig> implements DocumentCandidateFinder {

	protected final Logger logger = Logger.getLogger(this.getClass());
	private DocumentCategorizer categorizer;
    private T config;

	/**
	 * Constructor
	 * @param config the configuration that should be used
	 */
	public AbstractDocumentCandidateFinder(T config) {

        this.config = config;
		this.categorizer = config.getDocumentCategorizer();
	}

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
	@Override
	public final Map<String, List<Document>> findCandidateDocuments(List<String> surfaceNames,
		Map<Float, List<String>> allRatedQueryPattern, int candidateFinder_maxTermDocuments,
		int maxTermDocumentsPerPattern, float alternativeURIRating) throws ApiException {

		PerformanceUtils pu = new PerformanceUtils();
		Map<String, List<Document>> candidates = this.findCandidateDocumentsImpl(surfaceNames, allRatedQueryPattern,
			candidateFinder_maxTermDocuments, maxTermDocumentsPerPattern, alternativeURIRating);

        // set the data of some special fields into the document objects
        for (Entry<String, List<Document>> entry : candidates.entrySet()) {

            for (Document document : entry.getValue()) {

                // set surfaceForm the user has set in search field into document
                document.setSurfaceForm(entry.getKey().toLowerCase());

                // set uri into documents uri attribute
                Field uriField = document.getFieldByName(WikiContentHandler.FIELD_URI);
                if (uriField != null) {
                    document.setUri(uriField.getValue());
                }
            }
        }

		logger.info("needed time: "+pu.getRunningTimer());
		return candidates;
	}

	/**
	 *
	 * @param surfaceNames
	 * @param allRatedQueryPattern
	 * @param candidateFinder_maxTermDocuments
	 * @param maxTermDocumentsPerPattern
	 * @param alternativeURIRating
	 * @return
	 * @throws ApiException
	 */
	protected abstract Map<String, List<Document>> findCandidateDocumentsImpl(List<String> surfaceNames,
		Map<Float, List<String>> allRatedQueryPattern, int candidateFinder_maxTermDocuments,
		int maxTermDocumentsPerPattern, float alternativeURIRating) throws ApiException;

	/**
	 * The categorizer that sets the document type
	 * @param categorizer
	 */
	@Override
	public void setDocumentCategorizer(DocumentCategorizer categorizer) {
		this.categorizer = categorizer;
	}

	/**
	 * The categorizer that sets the document type
	 * @return categorizer
	 */
	@Override
	public DocumentCategorizer getDocumentCategorizer() {
		return this.categorizer;
	}

   /**
	 * Gets the configuration for the disambiguation engine.
	 * @return an implementation of AbstractDisambiguationEngineConfig
	 */
	protected T getConfig() {
        return config;
    }
}
