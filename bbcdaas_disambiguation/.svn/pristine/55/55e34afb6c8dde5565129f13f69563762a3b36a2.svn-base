package de.bbcdaas.disambiguation.evaluation.wikipedia;

import de.bbcdaas.disambiguation.evaluation.core.TagCloud;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Robert Illers
 */
public class WikiDisambiguationEvaluationStepResult {

	private Integer stepIndex;
	private Integer stepPrecision;
	private List<TagCloud> tagClouds = new ArrayList<TagCloud>();

	/**
	 *
	 * @return
	 */
	@XmlElement(name = "stepIndex")
	public Integer getStepIndex() {
		return stepIndex;
	}

	/**
	 *
	 * @param stepIndex
	 */
	public void setStepIndex(Integer stepIndex) {
		this.stepIndex = stepIndex;
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
	@XmlElement(name = "stepPrecision")
	public Integer getStepPrecision() {
		return stepPrecision;
	}

	/**
	 *
	 * @param stepPrecision
	 */
	public void setStepPrecision(Integer stepPrecision) {
		this.stepPrecision = stepPrecision;
	}
}
