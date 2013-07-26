package de.bbcdaas.common.beans.mlp;

/**
 *
 * @author Robert Illers
 */
public class Term {
	
	private int newId = 0;
	private int oldId = 0;
	private String value = null;

	public int getOldId() {
		return oldId;
	}

	public void setOldId(int oldId) {
		this.oldId = oldId;
	}
	
	public int getNewId() {
		return newId;
	}

	public void setNewId(int id) {
		this.newId = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
