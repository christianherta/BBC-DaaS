package de.bbcdaas.common.beans;

import java.io.Serializable;

/**
 * Represents a tag in a tag cloud.
 * @author Robert Illers
 */
public class TagCloudItem implements Comparable, Serializable {

	private String value;
	private float fontSize = 1;
	private float syntaq = 0;
	private int rank = 1;

	/**
	 *
	 * @return 
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * 
	 * @param rank 
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * A number representing the size of the tag cloud item if presented 
         * in a tag cloud.
	 * @return fontSize
	 */
	public float getFontSize() {
		return fontSize;
	}

	/**
	 * A number representing the size of the tag cloud item if presented 
         * in a tag cloud.
	 * @param fontSize 
	 */
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	/**
	 * 
	 * @return 
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 
	 * @param value 
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 
	 * @return 
	 */
	public float getSyntaq() {
		return syntaq;
	}

	/**
	 * 
	 * @param syntaq 
	 */
	public void setSyntaq(float syntaq) {
		this.syntaq = syntaq;
	}
	
	/**
	 * 
	 * @param o
	 * @return 
	 */
	@Override
	public int compareTo(Object o) {
		return this.value.compareTo(((TagCloudItem)o).getValue());
	}
}
