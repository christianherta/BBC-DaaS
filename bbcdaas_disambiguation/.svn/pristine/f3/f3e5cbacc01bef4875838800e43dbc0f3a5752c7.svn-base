package de.bbcdaas.disambiguation.wikipedia.engine.lucene;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.disambiguation.core.exceptions.DisambiguationException;
import de.bbcdaas.disambiguation.core.impl.AbstractDisambiguationEngine;
import de.bbcdaas.disambiguation.core.interfaces.DisambiguationEngine;
import de.bbcdaas.disambiguation.core.interfaces.DocumentCandidateFinder;
import de.bbcdaas.disambiguation.core.interfaces.DocumentCategorizer;
import de.bbcdaas.disambiguation.core.interfaces.DocumentScorer;
import de.bbcdaas.disambiguation.core.interfaces.DocumentScoringCombiner;
import de.bbcdaas.disambiguation.wikipedia.dataimport.lucene.WikiContentHandler;
import java.util.*;
import java.util.Map.Entry;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.WildcardQuery;

/**
 * Search engine providing methods search in wikipedia lucene index created by
 * the WikiLuceneDataImporter for finding documents related to a given list of term values,
 * The found documents get a rating on how related this documents are to the context
 * described by the termValues
 * @author Robert Illers
 */
public final class WikiDisambiguationEngine extends AbstractDisambiguationEngine <WikiDisambiguationEngineConfig> implements DisambiguationEngine<WikiDisambiguationEngineConfig>  {

	public WikiDisambiguationEngine(WikiDisambiguationEngineConfig config) {
		super(config);
	}

    /**
	* Searches for documents matching the title field value.
	* @param surfaceForm
	* @param maxHits
	* @return List<Document>
	*/
	@Override
    public List<Document> getDocumentsByTitleImpl(String surfaceForm, int maxHits) {

		Map<String, List<Document>> documentMap = new HashMap<String, List<Document>>();
		List<Document> documents = new ArrayList<Document>();

		if (surfaceForm != null) {

			String surfaceForm_formatted = surfaceForm.toLowerCase().trim();
			try {

				// open connection to lucene index
				this.getConfig().getConnector().getLuceneAPI().openConnection(this.getConfig().getConnector().getIndexPath());

				// get matching documents from index
				Query query = new WildcardQuery(new Term(WikiContentHandler.FIELD_TITLE, surfaceForm_formatted));
				documents.addAll(this.getConfig().getConnector().getLuceneAPI().searchForDocuments(query, maxHits));

				// categorize the found documents
				documentMap.put(surfaceForm_formatted, documents);
				this.getConfig().getDocumentCategorizer().categorizeDocuments(documentMap);
				documents = documentMap.get(surfaceForm_formatted);

				// close connection to lucene index
				this.getConfig().getConnector().getLuceneAPI().closeConnection();
			} catch (ApiException ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
        return documents;
    }

	/**
	 * Scores the documents found for each surfaceForm.
	 * @param surfaceForms Strings that could match a term in the index
	 * @param keepConnection
	 * @return documents grouped by the surfaceForm they belong to
	 */
	@Override
	public Map<String, List<Document>> scoreDocumentsImpl(List<String> surfaceForms, boolean keepConnection) {

		// get the configuration
		Set<DocumentCandidateFinder> candidateFinder = this.getConfig().getDocumentCandidateFinder();
		DocumentCategorizer documentCategorizer = this.getConfig().getDocumentCategorizer();
		Set<DocumentScorer> documentScorer = this.getConfig().getDocumentScorer();
		Set<DocumentScoringCombiner> scoringCombiner = this.getConfig().getDocumentScoringCombiner();

		Map<Float, List<String>> allRatedQueryPattern = this.getConfig().getAllRatedQueryPattern();
		int candidateFinder_maxTermDocuments = this.getConfig().getMaxTermDocuments();
		int maxTermDocumentsPerPattern = this.getConfig().getMaxTermDocumentsPerPattern();
		float alternativeURIRating = this.getConfig().getAlternativeURIRating();
		float multimatchingSurfaceFormScore = this.getConfig().getMultimatchingSurfaceFormScore();

		// result map
		Map<String, List<Document>> surfaceFormsDocuments = new HashMap<String, List<Document>>();
        for (String surfaceForm : surfaceForms) {
            surfaceFormsDocuments.put(surfaceForm, new ArrayList<Document>());
        }

		// print the configuration
		if (this.getConfig().isEnableLogs()) {

			logger.info("---------Configuration:-----------");
			logger.info("allRatedQueryPattern:");
			for(Entry<Float, List<String>> entry : allRatedQueryPattern.entrySet()) {
				logger.info(new StringBuilder("Rating: ").append(entry.getKey()).append(", Pattern: ").append(entry.getValue().toString()).toString());
			}
			logger.info(new StringBuilder("candidateFinder_maxTermDocuments: ").append(candidateFinder_maxTermDocuments).toString());
			logger.info(new StringBuilder("maxTermDocumentsPerPattern: ").append(maxTermDocumentsPerPattern).toString());
			logger.info(new StringBuilder("multimatchingSurfaceFormScore: ").append(multimatchingSurfaceFormScore).toString());
			logger.info(new StringBuilder("alternativeURIRating: ").append(alternativeURIRating).toString());

			// candidate finder
			if (!candidateFinder.isEmpty()) {
				logger.info("candidate finder: ");
				for (DocumentCandidateFinder aCandidateFinder : candidateFinder) {
					logger.info(aCandidateFinder.getClass().getName());
				}
			} else {
				logger.error("candidate finder: NONE. Scoring aborted.");
				return surfaceFormsDocuments;
			}

			// document categorizer
			if (documentCategorizer != null) {
				logger.info("categorizer: ");
				logger.info(documentCategorizer.getClass().getName());
			} else {
				logger.error("categorizer: NONE. Scoring aborted.");
				return surfaceFormsDocuments;
			}

			// document scorer
			if (!documentScorer.isEmpty()) {
				logger.info("document scorer: ");
				for (DocumentScorer aDocumentScorer : documentScorer) {
					logger.info(aDocumentScorer.getClass().getName());
				}
			} else {
				logger.error("document scorer: NONE. Scoring aborted.");
				return surfaceFormsDocuments;
			}

			// scoring combiner
			if (!scoringCombiner.isEmpty()) {
				logger.info("document scoring combiner: ");
				for (DocumentScoringCombiner aDocumentScoringCombiner : scoringCombiner) {
					logger.info(aDocumentScoringCombiner.getClass().getName());
				}
			} else {
				logger.error("document scoring combiner: NONE. Scoring aborted.");
				return surfaceFormsDocuments;
			}

			logger.info("----------------------------------");
		}
		// done printing the configuration

        // --------------------------- DISAMBIGUATION ------------------------//

		// begin disambiguation
		try {

            // open connection to lucene index
            this.getConfig().getConnector().getLuceneAPI().openConnection(this.getConfig().getConnector().getIndexPath());

            // ----------------------- STEP 1 : CANDIDATE FINDING ------------//

			// 1. find documents for the termValues based on pattern applied on
			// the document title and categorize them
			for (DocumentCandidateFinder aCandidateFinder : candidateFinder) {

                // execute current candidate finder
				Map<String, List<Document>> candidateFinderResult = aCandidateFinder.findCandidateDocuments(surfaceForms,
					allRatedQueryPattern, candidateFinder_maxTermDocuments, maxTermDocumentsPerPattern,
					alternativeURIRating);

                // merge candidates into result map
				for (Entry<String, List<Document>> entry : candidateFinderResult.entrySet()) {
                    for (Document newDocument : entry.getValue()) {
                        // a) document already found by other candidate finder
                        if (surfaceFormsDocuments.get(entry.getKey()).contains(newDocument)) {
                            for (Document oldDocument : surfaceFormsDocuments.get(entry.getKey())) {
                                if (newDocument.equals(oldDocument)) {
                                    oldDocument.addCandidateRatings(newDocument.getCandidateRatings());
                                    break;
                                }
                            }
                        // b) new found document
                        } else {
                            surfaceFormsDocuments.get(entry.getKey()).add(newDocument);
                        }
                    }
                }
				surfaceFormsDocuments.putAll(candidateFinderResult);
			}

            // ---------------------- /STEP 1 : CANDIDATE FINDING ------------//

            // ----------------------- STEP 2 : DOCUMENT SCORING -------------//

			// 2. score the documents

			// 2.1 deactivate scorer by combiner parameter
			for (DocumentScoringCombiner aDocumentScoringCombiner : scoringCombiner) {
				aDocumentScoringCombiner.setDocumentScorerActivations(documentScorer);
			}

			// 2.2 begin scoring
			for (DocumentScorer aDocumentScorer : documentScorer) {
				aDocumentScorer.scoreDocuments(surfaceFormsDocuments);
			}

            // ---------------------- /STEP 2 : DOCUMENT SCORING -------------//

            // ----------------------- STEP 3 : SCORE COMBINING --------------//

			// 3. combine the calculated scores
			for (DocumentScoringCombiner aDocumentScoringCombiner : scoringCombiner) {
				if (!aDocumentScoringCombiner.calulateCombinedScores(surfaceFormsDocuments, documentScorer)) {
					throw new DisambiguationException("scoreDocuments()", "Error in scoring combiner");
				}
			}

            // ---------------------- /STEP 3 : SCORE COMBINING --------------//

            // ----------------------- STEP 4 : SORTING ----------------------//

			// 4. sort the documents by its combined scores
			for (Map.Entry<String, List<Document>> termValueDocuments : surfaceFormsDocuments.entrySet()) {
				Collections.sort(termValueDocuments.getValue());
			}

            // ---------------------- /STEP 4 : SORTING ----------------------//

			// return the result
			return surfaceFormsDocuments;

        } catch (ApiException ex) {
            logger.error(ex.getCompleteMessage());
        } catch (DisambiguationException ex) {
            logger.error(ex.getCompleteMessage());
        } finally {
            if (!keepConnection) {
				try {
					this.getConfig().getConnector().getLuceneAPI().closeConnection();
				} catch (ApiException ex) {
					logger.error(ex.getCompleteMessage());
				}
			}
        }
		// done with disambiguation

        // -------------------------- /DISAMBIGUATION ------------------------//

		return new HashMap<String, List<Document>>();
	}
}
