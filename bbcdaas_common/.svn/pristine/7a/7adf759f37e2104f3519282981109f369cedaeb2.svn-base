package de.bbcdaas.common.beans.category;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Robert Illers
 */
public class Category implements Serializable {

	private String name;
	private String parentName;
	private Set<Category> subCategories = new HashSet<Category>();

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Category && this.name != null && this.name.equals(((Category)obj).name);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
		hash = 89 * hash + (this.parentName != null ? this.parentName.hashCode() : 0);
		hash = 89 * hash + (this.subCategories != null ? this.subCategories.hashCode() : 0);
		return hash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Category> getSubCategories() {
		return subCategories;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
}
