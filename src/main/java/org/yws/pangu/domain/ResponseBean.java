package org.yws.pangu.domain;

import java.io.Serializable;

public class ResponseBean implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	private boolean success;

	private String message;

	public ResponseBean(boolean success) {
		super();
		this.success = success;
		this.message = "OK";
	}

	public ResponseBean(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
