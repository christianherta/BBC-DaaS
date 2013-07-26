package de.bbcdaas.disambiguation.evaluation.wikipedia;

import de.bbcdaas.disambiguation.evaluation.core.AbstractEvaluationResult;
import de.bbcdaas.disambiguation.evaluation.core.TagCloud;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Result of the evaluation process of the wikipedia disambiguation.
 * @author Robert Illers
 */
@XmlRootElement(name = "WikiDisambiguationEvaluationResult")
public class WikiDisambiguationEvaluationResult extends AbstractEvaluationResult {

	// configuration
	private List<TagCloud> tagClouds = new ArrayList<TagCloud>();
	private List<String> usedCandidateFinder = new ArrayList<String>();
	private List<String> usedScorer = new ArrayList<String>();
	private List<String> usedCategorizer = new ArrayList<String>();
	private List<String> usedScoringCombiner = new ArrayList<String>();

	// step results
	private List<WikiDisambiguationEvaluationStepResult> stepResults = new ArrayList<WikiDisambiguationEvaluationStepResult>();

	/**
	 * Constructor used for unmarshalling
	 */
	public WikiDisambiguationEvaluationResult() {
		super(null);
	}

	/**
	 * Constructor used to create the objectfor marshalling
	 * @param xmlOutputPath
	 */
	public WikiDisambiguationEvaluationResult(String xmlOutputPath) {
		super(xmlOutputPath);
	}

	/**
	 *
	 * @param stepResult
	 */
	public void addStepResult(WikiDisambiguationEvaluationStepResult stepResult) {
		this.stepResults.add(stepResult);
	}

	/**
	 *
	 * @return
	 */
	@XmlElement(name = "stepResult")
	public List<WikiDisambiguationEvaluationStepResult> getStepResults() {
		return stepResults;
	}

	/**
	 * Marshals this object into an xml file.
	 * @throws JAXBException
	 * @throws IOException
	 */
	@Override
	public void writeResultToXmlFile() throws JAXBException, IOException {

		JAXBContext jaxbContext = JAXBContext.newInstance(WikiDisambiguationEvaluationResult.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty("jaxb.formatted.output", true);
		File outputFile = new File(this.xmlOutputPath);
		OutputStream output = new FileOutputStream(outputFile);
		marshaller.marshal( this, output);
		output.close();
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	public TagCloud getTagCloud(int index) {

		if (this.tagClouds.size() > index) {
			return this.tagClouds.get(index);
		}
		return null;
	}

	/**
	 *
	 * @return
	 */
	@XmlElement(name = "tagCloud")
	public List<TagCloud> getTagClouds() {
		return tagClouds;
	}

	/**
	 *
	 * @param tagCloud
	 * @return
	 */
	public TagCloud addTagCloud(TagCloud tagCloud) {

		this.tagClouds.add(tagCloud);
		return this.tagClouds.get(this.tagClouds.size()-1);
	}

	/**
	 *
	 * @return
	 */
	@XmlElement(name = "usedCandidateFinder")
	public List<String> getUsedCandidateFinder() {
		return usedCandidateFinder;
	}

	/**
	 *
	 * @param usedCandidateFinder
	 */
	public void setUsedCandidateFinder(List<String> usedCandidateFinder) {
		this.usedCandidateFinder = usedCandidateFinder;
	}

	/**
	 *
	 * @return
	 */
	@XmlElement(name = "usedScorer")
	public List<String> getUsedScorer() {
		return usedScorer;
	}

	/**
	 *
	 * @param usedScorer
	 */
	public void setUsedScorer(List<String> usedScorer) {
		this.usedScorer = usedScorer;
	}

	/**
	 *
	 * @return
	 */
	@XmlElement(name = "usedCategorizer")
	public List<String> getUsedCategorizer() {
		return usedCategorizer;
	}

	/**
	 *
	 * @param usedCategorizer
	 */
	public void setUsedCategorizer(List<String> usedCategorizer) {
		this.usedCategorizer = usedCategorizer;
	}

	/**
	 *
	 * @return
	 */
	@XmlElement(name = "usedScoringCombiner")
	public List<String> getUsedScoringCombiner() {
		return usedScoringCombiner;
	}

	/**
	 *
	 * @param usedScoringCombiner
	 */
	public void setUsedScoringCombiner(List<String> usedScoringCombiner) {
		this.usedScoringCombiner = usedScoringCombiner;
	}
}
