package de.bbcdaas.disambiguation.evaluation.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Marshallable representation of a tag (surfaceForm) for evaluation purpose.
 * @author Robert Illers
 */
@XmlRootElement
public class Tag {

	private String name;
	private String expectedUri;
	private List<String> nearByUris = new ArrayList<String>();

	// for evalutation output only
	private List<String> bestResultUris = new ArrayList<String>();
	private Boolean expectedUriMatches = null;
	private Boolean nearByUriMatches = null;

	/**
	* The name (surfaceForm) of the Entity defined by its expected wikipedia URI.
	* @return name
	*/
	@XmlElement( name = "name" )
	public String getName() {
		return name;
	}

	/**
	 * The name (surfaceForm) of the Entity defined by its expected wikipedia URI.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 * @return
	 */
	@XmlElement( name = "expectedUri" )
	public String getExpectedUri() {
		return expectedUri;
	}

	/**
	 *
	 * @param expectedUri
	 */
	public void setExpectedUri(String expectedUri) {
		this.expectedUri = expectedUri;
	}

	/**
	 *
	 * @return
	 */
	@XmlElement( name = "bestResultUri" )
	public List<String> getBestResultUris() {
		return bestResultUris;
	}

	/**
	 *
	 * @param resultUri
	 */
	public void addBestResultUri(String resultUri) {
		this.bestResultUris.add(resultUri);
	}

	/**
	 *
	 * @return
	 */
	@XmlElement( name = "nearByUri" )
	public List<String> getNearByUris() {
		return nearByUris;
	}

	/**
	 *
	 * @param nearByUris
	 */
	public void setNearByUris(List<String> nearByUris) {
		this.nearByUris = nearByUris;
	}

	/**
	 *
	 * @return
	 */
	@XmlElement( name = "expectedUriMatches" )
	public Boolean getExpectedUriMatches() {
		return expectedUriMatches;
	}

	/**
	 *
	 * @param expectedUriMatches
	 */
	public void setExpectedUriMatches(Boolean expectedUriMatches) {
		this.expectedUriMatches = expectedUriMatches;
	}

	/**
	 *
	 * @return
	 */
	@XmlElement( name = "nearByUriMatches" )
	public Boolean getNearByUriMatches() {
		return nearByUriMatches;
	}

	/**
	 *
	 * @param nearByUriMatches
	 */
	public void setNearByUriMatches(Boolean nearByUriMatches) {
		this.nearByUriMatches = nearByUriMatches;
	}
}
