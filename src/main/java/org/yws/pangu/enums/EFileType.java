package org.yws.pangu.enums;

public enum EFileType {
	FOLDER((short) 1), FILE((short) 2);

	private short value;

	private EFileType(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public boolean isEqual(short v) {
		return value == v;
	}

}
