package org.yws.pangu.domain;

import java.io.Serializable;

public class TaskNode implements Serializable {

	/**  */
	private static final long serialVersionUID = -381056403637611703L;
	private String hostName;
	private String hostAddress;

	public TaskNode(String hostName, String hostAddress) {
		super();
		this.hostName = hostName;
		this.hostAddress = hostAddress;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}
}
