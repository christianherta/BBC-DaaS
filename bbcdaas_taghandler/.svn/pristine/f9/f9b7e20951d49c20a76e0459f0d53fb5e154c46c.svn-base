package de.bbcdaas.taghandler.exception;

/**
 *
 * @author Robert Illers
 */
public class ProcessException extends Exception {
	
	public static final int ERROR_CODE_NONE = 0;
	public static final int ERROR_CODE_READ_ENTITIES = 1;
	public static final int ERROR_CODE_COMPUTE_SYNTAGMATIC_RELATIONS = 2;
	public static final int ERROR_CODE_COMPUTE_TOP_RELATED_TERMS = 3;
	public static final int ERROR_CODE_COMPUTE_TOP_SYNTAGMATIC_TERMS = 4;
	
	private int errorCode = ERROR_CODE_NONE;

	/**
	 * Constructor with error code only
	 * @param errorCode 
	 */
	public ProcessException(int errorCode) {
		this.errorCode = errorCode;
	}
	
	/**
	 * Constructor with message and error code
	 * @param errorCode 
	 */
	public ProcessException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	
	/**
	 * Determines where a process stopped on error. Can be used to continue there.
	 * @return errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}
}
