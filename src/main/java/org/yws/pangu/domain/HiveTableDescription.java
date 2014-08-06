package org.yws.pangu.domain;

public class HiveTableDescription {
	private String columnName;
	private String columnType;
	private String comment;

	public HiveTableDescription(String columnName, String columnType, String comment) {
		super();
		this.columnName = columnName;
		this.columnType = columnType;
		this.comment = comment;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
