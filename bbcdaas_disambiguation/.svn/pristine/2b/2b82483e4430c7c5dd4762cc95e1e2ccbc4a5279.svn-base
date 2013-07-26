package de.bbcdaas.disambiguation.evaluation.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean representing the GoldStandard for disambiguated tag clouds.
 * @author Robert Illers
 */
@XmlRootElement(name = "GoldStandard")
public class GoldStandard {
	
	private List<TagCloud> tagClouds = new ArrayList<TagCloud>();
	
	/**
	 * 
	 * @return 
	 */
	@XmlElement(name = "tagCloud")
	public List<TagCloud> getClouds() {
		return tagClouds;
	}

	/**
	 * 
	 * @param tagClouds 
	 */
	public void setTagClouds(List<TagCloud> tagClouds) {
		this.tagClouds = tagClouds;
	}
}
