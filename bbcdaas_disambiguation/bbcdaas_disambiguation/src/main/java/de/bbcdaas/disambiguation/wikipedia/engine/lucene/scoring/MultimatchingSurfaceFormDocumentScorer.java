package de.bbcdaas.disambiguation.wikipedia.engine.lucene.scoring;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.beans.document.Field;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.disambiguation.core.impl.AbstractDocumentScorer;
import de.bbcdaas.disambiguation.core.interfaces.DocumentScorer;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiDisambiguationEngineConfig;
import de.bbcdaas.disambiguation.wikipedia.dataimport.lucene.WikiContentHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Robert Illers
 */
public final class MultimatchingSurfaceFormDocumentScorer extends AbstractDocumentScorer <WikiDisambiguationEngineConfig> implements DocumentScorer {

	private Float multimatchingSurfaceFormScore;

	/**
	 * Constructor
	 * @param config
	 */
	public MultimatchingSurfaceFormDocumentScorer(WikiDisambiguationEngineConfig config) {
		super(config);
		this.multimatchingSurfaceFormScore = config.getMultimatchingSurfaceFormScore();
	}

	/**
	 * Nothing to prepare here at the moment
	 * @param surfaceFormsDocuments
	 * @throws ApiException
	 */
	@Override
	protected void prepare(Map<String, List<Document>> surfaceFormsDocuments) throws ApiException {}

	/**
	 *
	 * @param surfaceNamesDocuments
	 * @throws ApiException
	 */
	@Override
	public Map<String, List<Float>> scoreDocumentsImpl(Map<String, List<Document>> surfaceFormsDocuments) throws ApiException {

		if (this.getConfig().isEnableLogs()) {
			logger.info("---MultimatchingSurfaceFormDocumentScorer:---");
		}

		// scores as map (will be set to documents in abstract scorer)
		Map<String, List<Float>> scores = new HashMap<String, List<Float>>();
		for (String surfaceForm : surfaceFormsDocuments.keySet()) {
			scores.put(surfaceForm, new ArrayList<Float>());
		}
        Set<String> surfaceForms = surfaceFormsDocuments.keySet();

		if (this.multimatchingSurfaceFormScore != null && this.multimatchingSurfaceFormScore != 0.0f) {

			this.prepare(surfaceFormsDocuments);

			for (String surfaceForm1 : surfaceForms) {

				for (Document document : surfaceFormsDocuments.get(surfaceForm1)) {

					float score = 0.0f;

					if (document.getType() == Document.DOCUMENT_TYPE_ARTICLE) {

						for (String surfaceForm2 : surfaceForms) {

							String surfaceForm2_formated = surfaceForm2.toLowerCase().trim();
							Field titleField = document.getFieldByName(WikiContentHandler.FIELD_TITLE);

							if (!surfaceForm1.equals(surfaceForm2) &&
								!document.getMultimatchingSurfaceForms().contains(surfaceForm2)) {

								// increase rating if another surfaceForm is part of the current document title
								if (titleField.getValue().contains(surfaceForm2_formated)) {

									if (this.getConfig().isEnableLogs()) {
										logger.info(new StringBuilder("Matching: documents title '").append(titleField.getValue()).
											append("' of term '").append(surfaceForm1).append("' matches with term '").append(surfaceForm2).
											append("', added score ").append(this.multimatchingSurfaceFormScore).toString());
									}
									score = this.multimatchingSurfaceFormScore;
									document.addMultimatchingSurfaceForm(surfaceForm2);
								}
								// otherwise increase rating if another termValue is part of the current document keywords
								else {

									Field keywordField = document.getFieldByName(WikiContentHandler.FIELD_KEYWORDS);
									for (String keyword : keywordField.getValues()) {

										if (keyword.equals(surfaceForm2_formated)) {

											if (this.getConfig().isEnableLogs()) {
												logger.info(new StringBuilder("Matching: Keywords of document '"+titleField.getValue()+"' of term '").
													append(surfaceForm1).append("' contains term '").append(surfaceForm2).
													append("', added score ").append(this.multimatchingSurfaceFormScore).toString());
											}
											score = this.multimatchingSurfaceFormScore;
											document.addMultimatchingSurfaceForm(surfaceForm2);
											break;
										}
									}
								}
								if (this.getConfig().isEnableLogs()) {
									logger.info(new StringBuilder("Added multimatching surfaceForm Score '").append(score).append("' to URI '").
										append(document.getFieldByName(WikiContentHandler.FIELD_URI).getValue()).append("'.").toString());
								}
							}
						}
					}
                    // set score into result map
					scores.get(surfaceForm1).add(score);
				}
			}
		} else {
			logger.error("Parameter 'multimatchingSurfaceFormScore' not set, nothing scored.");
            // not scored, but fill result map to prevent gaps
            for (String surfaceForm1 : surfaceForms) {
				for (int i = 0; i<surfaceFormsDocuments.get(surfaceForm1).size();i++) {
                    scores.get(surfaceForm1).add(0.0f);
                }
            }
		}
		return scores;
	}
}