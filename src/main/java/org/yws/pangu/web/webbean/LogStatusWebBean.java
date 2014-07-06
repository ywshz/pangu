package org.yws.pangu.web.webbean;

import java.io.Serializable;

public class LogStatusWebBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String status;
	private String log;

	public LogStatusWebBean(String status, String log) {
		this.status=status;
		this.log=log;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

}
