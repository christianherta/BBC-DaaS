package de.bbcdaas.disambiguation.core.exceptions;

/**
 *
 * @author Robert Illers
 */
public class DisambiguationException extends Exception {

	private String methodName;
	/**
	 * constructor
	 * @param message
	 */
	public DisambiguationException(String methodName, String message) {
		super(message);
		this.methodName = methodName;
	}

	/**
	 *
	 * @return
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 *
	 * @return
	 */
	public String getCompleteMessage() {
		return "Error in "+methodName+"(): "+this.getMessage();
	}

}