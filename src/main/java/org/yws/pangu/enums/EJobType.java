package org.yws.pangu.enums;

public enum EJobType {
	SHELL("shell"), HIVE("hive");

	private String type;

	private EJobType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
