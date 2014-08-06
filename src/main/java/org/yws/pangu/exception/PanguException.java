package org.yws.pangu.exception;

public class PanguException extends Exception{

	/**  */
	private static final long serialVersionUID = 7700377478690055734L;

	public PanguException() {
		super();
	}

	public PanguException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PanguException(String message, Throwable cause) {
		super(message, cause);
	}

	public PanguException(String message) {
		super(message);
	}

	public PanguException(Throwable cause) {
		super(cause);
	}

	
}
