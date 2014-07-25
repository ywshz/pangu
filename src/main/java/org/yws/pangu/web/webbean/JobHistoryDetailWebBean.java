package org.yws.pangu.web.webbean;

import java.io.Serializable;

public class JobHistoryDetailWebBean implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	private String id;
	private String status;
	private String startTime;
	private String endTime;
	private String log;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

}
