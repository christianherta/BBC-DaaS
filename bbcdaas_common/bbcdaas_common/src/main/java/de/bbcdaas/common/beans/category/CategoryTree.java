package de.bbcdaas.common.beans.category;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Robert Illers
 */
public class CategoryTree implements Serializable {

	private Set<Category> categories = new HashSet<Category>();
	private Map<String, List<Category>> index = new HashMap<String, List<Category>>();

	public Set<Category> getCategories() {
		return categories;
	}

	public boolean insertCategory(Category newCategory) {

		if (newCategory.getParentName() == null) {

			if (!this.categories.contains(newCategory)) {
				this.categories.add(newCategory);
			}

			if (!this.index.containsKey(newCategory.getName())) {
				this.index.put(newCategory.getName(), new ArrayList<Category>());
			}

			this.index.get(newCategory.getName()).add(newCategory);

			return true;
		} else {

			if (this.index.containsKey(newCategory.getParentName())) {

				if (!this.index.containsKey(newCategory.getName())) {
					this.index.put(newCategory.getName(), new ArrayList<Category>());
				}

				this.index.get(newCategory.getName()).add(newCategory);

				for (Category parentCategory : this.index.get(newCategory.getParentName())) {

					if (!parentCategory.getSubCategories().contains(newCategory)) {
						parentCategory.getSubCategories().add(newCategory);
					}
				}

				return true;
			}

			return false;
		}
	}

	public List<Category> findCategory(String name) {

		List<Category> ret = this.index.get(name);
		if (ret == null) {
			ret = new ArrayList<Category>();
		}
		return ret;
	}



	public int getNumberOfObjects() {

		int i = 0;
		for (List<Category> categoryList : this.index.values()) {
			i += categoryList.size();
		}
		return i;
	}
}
