package de.bbcdaas.common.util;

/**
 *
 * @author Robert Illers
 */
public final class StringOperations {
	
	public static final String ABC = "abcdefghijklmnopqrstuvwxyz";
	
	private StringOperations() {}
	
	public static boolean checkForCapitalLetter(String stringToCheck, int position) {
		
		if (stringToCheck != null && stringToCheck.length()-1 >= position && position >= 0) {
			
			String firstLetter = stringToCheck.substring(0, 1);
			if (ABC.contains(firstLetter.toLowerCase()) && firstLetter.equals(firstLetter.toUpperCase())) {
				return true;
			}
		}
		return false;
	}
}
