package org.yws.pangu.enums;

public enum EJobScheduleType {
	RUN_BY_TIME(1), RUN_BY_DEPENDENCY(2);

	private Integer value;

	private EJobScheduleType(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public boolean isEqual(Integer value) {
		return this.value.equals(value);
	}
}
