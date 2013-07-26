package de.bbcdaas.common.util;

import de.bbcdaas.common.beans.TagCloudItem;
import java.util.Comparator;

/**
 * Compares two TagCloudItems.
 * @author Robert Illers
 */
public class TagCloudItemComparator implements Comparator<TagCloudItem> {

	// compare type: 1: by value, 2: by syntag
	private int type = 1;
	
	/**
	 * Constructor. The type determines how the 
	 * @param type 
	 */
	public TagCloudItemComparator(int type) {
		this.type = type;
	}
	
	@Override
	public int compare(TagCloudItem o1, TagCloudItem o2) {
		
		if (type == 1) {
			return o1.compareTo(o2);
		} else if (type == 2) {
			if (o1.getSyntaq() == o2.getSyntaq()) {
				return 0;
			}
			if (o1.getSyntaq() < o2.getSyntaq()) {
				return 1;
			}
			if (o1.getSyntaq() > o2.getSyntaq()) {
				return -1;
			}
		}
		return o1.compareTo(o2);
	}
}
