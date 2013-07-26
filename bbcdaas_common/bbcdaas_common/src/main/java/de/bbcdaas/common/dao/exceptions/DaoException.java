package de.bbcdaas.common.dao.exceptions;

/**
 * Standard Exception that is thrown by the dao implementations. Covers all Exceptions
 * from APIs that are used in the DAO's so that the application only needs to evaluate
 * this type of exception.
 * @author Robert Illers
 */
public class DaoException extends Exception {
	
	private String methodName;
	/**
	 * constructor
	 * @param message 
	 */
	public DaoException(String methodName, String message) {
		super(message);
		this.methodName = methodName; 
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public String getCompleteMessage() {
		return "Error in "+methodName+"(): "+this.getMessage();
	}
}
