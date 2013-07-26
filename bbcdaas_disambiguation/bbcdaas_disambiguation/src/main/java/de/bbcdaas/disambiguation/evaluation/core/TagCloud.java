package de.bbcdaas.disambiguation.evaluation.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class used in evaluation process to represent a goldstandard tag cloud.
 * @author Robert Illers
 */
@XmlRootElement
public class TagCloud {

	private List<Tag> tags = new ArrayList<Tag>();

	// for evaluation output
	private Integer tagCloudIndex;
	private Integer cloudPrecision;
	private Integer nearByUriHits;

	/**
	*
	* @return
	*/
	@XmlElement(name = "tag")
	public List<Tag> getTags() {
		return tags;
	}

	/**
	 *
	 * @param tags
	 */
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	/**
	 *
	 * @param tag
	 */
	public void addTag(Tag tag) {
		this.tags.add(tag);
	}

	/**
	 *
	 * @param tagName
	 * @return
	 */
	public Tag getTag(String tagName) {

		for (Tag tag : this.tags) {
			if (tag.getName().equals(tagName)) {
				return tag;
			}
		}
		return null;
	}

	/**
	 *
	 * @return
	 */
	@XmlElement(name = "tagCloudIndex")
	public Integer getTagCloudIndex() {
		return this.tagCloudIndex;
	}

	/**
	 *
	 * @param tagCloudIndex
	 */
	public void setTagCloudIndex(Integer tagCloudIndex) {
		this.tagCloudIndex = tagCloudIndex;
	}

	/**
	 *
	 * @return
	 */
	@XmlElement(name = "cloudPrecision")
	public Integer getCloudPrecision() {
		return cloudPrecision;
	}

	/**
	 *
	 * @param cloudPrecision
	 */
	public void setCloudPrecision(Integer cloudPrecision) {
		this.cloudPrecision = cloudPrecision;
	}

	/**
	 *
	 * @return
	 */
	@XmlElement(name = "nearByUriHits")
	public Integer getNearByUriHits() {
		return nearByUriHits;
	}

	/**
	 *
	 * @param nearByUriHits
	 */
	public void setNearByUriHits(Integer nearByUriHits) {
		this.nearByUriHits = nearByUriHits;
	}
}
