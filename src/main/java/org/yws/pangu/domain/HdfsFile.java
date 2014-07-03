package org.yws.pangu.domain;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by ywszjut on 14-6-22.
 */
public class HdfsFile {

	private String path;
	private String name;
	private String type;
	private long size;
	private String sizeInM;
	private String owner;
	private String modificationTime;
	private String content;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {

		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(String modificationTime) {
		this.modificationTime = modificationTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSizeInM() {
		if("File".equals(this.type)){
			BigDecimal bg = new BigDecimal(size);
			bg = bg.divide(new BigDecimal(1024 * 1024));
			DecimalFormat df = new DecimalFormat("#.00M");
			return df.format(bg);
		}else{
			return "";
		}
		
	}

	public void setSizeInM(String sizeInM) {
	}

}
