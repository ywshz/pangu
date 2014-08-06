package org.yws.pangu.web.webbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TableWebBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dbName;
	private String tableName;
	private String tableType;
	private List<ColumnWebBean> columns = new ArrayList<ColumnWebBean>();
	private List<ColumnWebBean> partitions = new ArrayList<ColumnWebBean>();
	private String inputFormat;
	private String outputFormat;
	private String location;
	private String createTime;
	private String lastAccess;
	private String owner;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public List<ColumnWebBean> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnWebBean> columns) {
		this.columns = columns;
	}

	public List<ColumnWebBean> getPartitions() {
		return partitions;
	}

	public void setPartitions(List<ColumnWebBean> partitions) {
		this.partitions = partitions;
	}

	public String getInputFormat() {
		return inputFormat;
	}

	public void setInputFormat(String inputFormat) {
		this.inputFormat = inputFormat;
	}

	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(String lastAccess) {
		this.lastAccess = lastAccess;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

}
