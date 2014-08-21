package org.yws.pangu.enums;

public enum EJobTriggerType {
	AUTO_TRIGGER(1), MANUAL_TRIGGER(2);

	private Integer value;

	private EJobTriggerType(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public boolean isEqual(Integer value) {
		return this.value.equals(value);
	}
}
