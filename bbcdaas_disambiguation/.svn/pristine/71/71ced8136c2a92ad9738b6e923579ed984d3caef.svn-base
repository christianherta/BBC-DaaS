package de.bbcdaas.disambiguation.core.configs;

import de.bbcdaas.disambiguation.core.connector.AbstractConnector;
import de.bbcdaas.disambiguation.core.impl.AbstractDocumentCandidateFinder;
import de.bbcdaas.disambiguation.core.impl.AbstractDocumentCategorizer;
import de.bbcdaas.disambiguation.core.impl.AbstractDocumentScorer;
import de.bbcdaas.disambiguation.core.impl.AbstractDocumentScoringCombiner;
import de.bbcdaas.disambiguation.core.interfaces.DocumentCandidateFinder;
import de.bbcdaas.disambiguation.core.interfaces.DocumentCategorizer;
import de.bbcdaas.disambiguation.core.interfaces.DocumentScorer;
import de.bbcdaas.disambiguation.core.interfaces.DocumentScoringCombiner;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Base class for all implementations of disambiguation engine configurations.
 * @author Robert Illers
 */
public abstract class AbstractDisambiguationEngineConfig<T extends AbstractConnector> extends AbstractConfiguration<T> {

	// general candidate finder related members
	private Set<String> candidateFinderNames = new LinkedHashSet<String>();
	private Map<String, Class<? extends AbstractDocumentCandidateFinder>> supportedCandidateFinder = new HashMap<String, Class<? extends AbstractDocumentCandidateFinder>>();
	private Set<DocumentCandidateFinder> documentCandidateFinder = new LinkedHashSet<DocumentCandidateFinder>();
	private String documentCategorizerName;
	private Map<String, Class<? extends AbstractDocumentCategorizer>> supportedDocumentCategorizer = new HashMap<String, Class<? extends AbstractDocumentCategorizer>>();
	private DocumentCategorizer documentCategorizer;

	// general  document scorer related members
	private Set<String> documentScorerNames = new LinkedHashSet<String>();
	private Map<String, Class<? extends AbstractDocumentScorer>> supportedDocumentScorer = new HashMap<String, Class<? extends AbstractDocumentScorer>>();
	private Set<DocumentScorer> documentScorer = new LinkedHashSet<DocumentScorer>();

	// general scoring combiner related members
	private Set documentScoringCombinerNames = new LinkedHashSet<String>();
	private Map<String, Class<? extends AbstractDocumentScoringCombiner>> supportedDocumentScoringCombiner = new HashMap<String, Class<? extends AbstractDocumentScoringCombiner>>();
	private Set<DocumentScoringCombiner> documentScoringCombiner = new LinkedHashSet<DocumentScoringCombiner>();

	// linear scoring combiner related members
	private List<Float> linearScoringCombinerScorerWeightings = new ArrayList<Float>();

	/**
	 * Constructor
	 */
	public AbstractDisambiguationEngineConfig(T connector) {

        super(connector);
		this.supportedCandidateFinder = WikiConstants.SUPPORTED_CANDIDATE_FINDER;
		this.supportedDocumentCategorizer = WikiConstants.SUPPORTED_DOCUMENT_CATEGORIZER;
		this.supportedDocumentScorer = WikiConstants.SUPPORTED_DOCUMENT_SCORER;
		this.supportedDocumentScoringCombiner = WikiConstants.SUPPORTED__SCORING_COMBINER;
	}

	/**
	 * Minimalistic constructor for using configuration without lucene, e.g. only
	 * as storage or for getting configuration parameters
	 */
	public AbstractDisambiguationEngineConfig() {}

	/* candidate finder related */

	/**
	 *
	 * @param supportedDocumentCandidateFinder
	 */
	public void setSupportedDocumentCandidateFinder(Map<String, Class<? extends AbstractDocumentCandidateFinder>> supportedDocumentCandidateFinder) {
		this.supportedCandidateFinder = supportedDocumentCandidateFinder;
	}

	/**
	 *
	 * @return
	 */
	public Map<String, Class<? extends AbstractDocumentCandidateFinder>> getSupportedDocumentCandidateFinder() {
		return supportedCandidateFinder;
	}

	/**
	 * Get the names of the used candidate finder.
	 * @return Set<String> with the candidate finder names
	 */
	public Set<String> getDocumentCandidateFinderNames() {
		return candidateFinderNames;
	}

	/**
	 * Set the class names of the used candidate finder.
	 * @param candidateFinderNames names of the candidate finder
	 */
	public void setDocumentCandidateFinderNames(List<String> candidateFinderNames) {

		this.candidateFinderNames.clear();
		this.candidateFinderNames.addAll(candidateFinderNames);
	}

	/**
	 * The candidate finder for finding documents that match the criteria.
	 * @return CandidateFinder
	 */
	public Set<DocumentCandidateFinder> getDocumentCandidateFinder() {
		return documentCandidateFinder;
	}

	/**
	 * The candidate finder for finding documents that match the criteria.
	 * @param candidateFinder
	 */
	public void setDocumentCandidateFinder(Set<DocumentCandidateFinder> candidateFinder) {

		this.documentCandidateFinder.clear();
		this.candidateFinderNames.clear();
		for (DocumentCandidateFinder aCandidateFinder : candidateFinder) {
			this.addDocumentCandidateFinder(aCandidateFinder);
		}
	}

	/**
	 * Adds a candidate finder and also sets its candidate finder name.
	 * @param documentCandidateFinder List of candidate finder to add
	 */
	public void addDocumentCandidateFinder(DocumentCandidateFinder documentCandidateFinder) {

		for (Entry<String, Class<? extends AbstractDocumentCandidateFinder>> aSupportedDocumentCandidateFinder : this.supportedCandidateFinder.entrySet()) {
			if (documentCandidateFinder.getClass() == aSupportedDocumentCandidateFinder.getValue()) {
				this.documentCandidateFinder.add(documentCandidateFinder);
				this.candidateFinderNames.add(aSupportedDocumentCandidateFinder.getKey());
				break;
			}
		}
	}

	/**
	 *
	 * @param supportedDocumentCategorizer
	 */
	public void setSupportedDocumentCategorizer(Map<String, Class<? extends AbstractDocumentCategorizer>> supportedDocumentCategorizer) {
		this.supportedDocumentCategorizer = supportedDocumentCategorizer;
	}

	/**
	 *
	 * @return
	 */
	public Map<String, Class<? extends AbstractDocumentCategorizer>> getSupportedDocumentCategorizer() {
		return supportedDocumentCategorizer;
	}

	/**
	 *
	 * @return
	 */
	public String getDocumentCategorizerName() {
		return documentCategorizerName;
	}

	/**
	 *
	 * @param documentCategorizerName
	 */
	public void setDocumentCategorizerName(String documentCategorizerName) {
		this.documentCategorizerName = documentCategorizerName;
	}

	/**
	 *
	 * @return
	 */
	public DocumentCategorizer getDocumentCategorizer() {
		return documentCategorizer;
	}

	/**
	 *
	 * @param categorizer
	 */
	public void setDocumentCategorizer(DocumentCategorizer categorizer) {

		for (Entry<String, Class<? extends AbstractDocumentCategorizer>> aSupportedDocumentCategorizer : this.supportedDocumentCategorizer.entrySet()) {
			if (categorizer.getClass() == aSupportedDocumentCategorizer.getValue()) {
				this.documentCategorizer = categorizer;
				this.documentCategorizerName = aSupportedDocumentCategorizer.getKey();
				break;
			}
		}
	}

	/* /candidate finder related */

	/* document scorer related */

	/**
	 *
	 * @param classes
	 */
	public void setSupportedDocumentScorer(Map<String, Class<? extends AbstractDocumentScorer>> supportedDocumentScorer) {
		this.supportedDocumentScorer = supportedDocumentScorer;
	}

	/**
	 *
	 * @return
	 */
	public Map<String, Class<? extends AbstractDocumentScorer>> getSupportedDocumentScorer() {
		return supportedDocumentScorer;
	}

	/**
	 * Returns a Set of the document scorer set.
	 * @return Set<DocumentScorer>
	 */
	public Set<DocumentScorer> getDocumentScorer() {
		return documentScorer;
	}

	/**
	 * Sets the document scorers and also sets the scorer names.
	 * @param documentScorer list of document scorers
	 */
	public void setDocumentScorer(Set<DocumentScorer> documentScorer) {

		this.documentScorer.clear();
		this.documentScorerNames.clear();
		for (DocumentScorer aDocumentScorer : documentScorer) {
			this.addDocumentScorer(aDocumentScorer);
		}
	}

	/**
	 * Adds a document scoer and also sets its document scorer name.
	 * @param documentScorer List of document scorer to add
	 */
	public void addDocumentScorer(DocumentScorer documentScorer) {

		for (Entry<String, Class<? extends AbstractDocumentScorer>> aSupportedDocumentScorer : this.supportedDocumentScorer.entrySet()) {
			if (documentScorer.getClass() == aSupportedDocumentScorer.getValue()) {
				this.documentScorer.add(documentScorer);
				this.documentScorerNames.add(aSupportedDocumentScorer.getKey());
				break;
			}
		}
	}

	/**
	 * Get the names of the uesed scorers.
	 * @return Set<String> with the scorer names
	 */
	public Set<String> getDocumentScorerNames() {
		return documentScorerNames;
	}

	/**
	 * Set the class names of the used scorers.
	 * @param documentScorerNames names of the used scorer
	 */
	public void setDocumentScorerNames(List<String> documentScorerNames) {

		this.documentScorerNames.clear();
		this.documentScorerNames.addAll(documentScorerNames);
	}

	/* /document scorer related */

	/* scoring combiner related */

	/**
	 *
	 * @param classes
	 */
	public void setSupportedDocumentScoringCombiner(Map<String, Class<? extends AbstractDocumentScoringCombiner>> supportedDocumentScoringCombiner) {
		this.supportedDocumentScoringCombiner = supportedDocumentScoringCombiner;
	}

	/**
	 *
	 * @return
	 */
	public Map<String, Class<? extends AbstractDocumentScoringCombiner>> getSupportedDocumentScoringCombiner() {
		return supportedDocumentScoringCombiner;
	}

	/**
	 *
	 * @return
	 */
	public Set<DocumentScoringCombiner> getDocumentScoringCombiner() {
		return documentScoringCombiner;
	}

	/**
	 *
	 * @param scoringCombiner
	 */
	public void setDocumentScoringCombiner(Set<DocumentScoringCombiner> scoringCombiner) {

		this.documentScoringCombiner.clear();
		this.documentScoringCombinerNames.clear();
		for (DocumentScoringCombiner aScoringCombiner : scoringCombiner) {
			this.addDocumentScoringCombiner(aScoringCombiner);
		}
	}

	/**
	 *
	 * @param scoringCombiner
	 */
	public void addDocumentScoringCombiner(DocumentScoringCombiner scoringCombiner) {

		for (Entry<String, Class<? extends AbstractDocumentScoringCombiner>> aSupportedDocumentScoringCombiner : this.supportedDocumentScoringCombiner.entrySet()) {
			if (scoringCombiner.getClass() == aSupportedDocumentScoringCombiner.getValue()) {
				this.documentScoringCombiner.add(scoringCombiner);
				this.documentScoringCombinerNames.add(aSupportedDocumentScoringCombiner.getKey());
				break;
			}
		}
	}

	/**
	 *
	 * @return
	 */
	public List<Float> getLinearScoringCombinerScorerWeightings() {
		return linearScoringCombinerScorerWeightings;
	}

	/**
	 *
	 */
	public void clearLinearScoringCombinerScorerWeightings() {
		this.linearScoringCombinerScorerWeightings.clear();
	}

	/**
	 *
	 * @param linearScoringCombinerScorerWeighting
	 */
	public void addLinearScoringCombinerScorerWeighting(float linearScoringCombinerScorerWeighting) {
		this.linearScoringCombinerScorerWeightings.add(linearScoringCombinerScorerWeighting);
	}

	/**
	 *
	 * @return
	 */
	public Set<String> getDocumentScoringCombinerNames() {
		return documentScoringCombinerNames;
	}

	/**
	 *
	 * @param scoringCombinerNames
	 */
	public void setDocumentScoringCombinerNames(List<String> scoringCombinerNames) {
		this.documentScoringCombinerNames.clear();
		this.documentScoringCombinerNames.addAll(scoringCombinerNames);
	}

	/* /scoring combiner related */

	/**
	 * Gets this configuration from a properties file, object initialisations are not done here
	 * @return true if no error occured
	 */
	public abstract boolean readConfigFromPropertiesFile(String relPath);

	/**
	 * Gets a part of the configuration from the properties file containing
	 * informations about the used scorer etc.
	 * @return true if no error occured
	 */
	public abstract boolean initializeObjectsFromPropertiesFile(String relPath);
}
