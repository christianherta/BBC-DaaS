package de.bbcdaas.common.dao.exceptions;

/**
 * Standard Exception that is thrown by dao API's.
 * This Exception type covers all other exception types of the specific api implementations
 * to separate the applications code from the specific api implementations
 * @author Robert Illers
 */
public class ApiException extends Exception {
	
	private String methodName;
	/**
	 * constructor
	 * @param message 
	 */
	public ApiException(String methodName, String message) {
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
