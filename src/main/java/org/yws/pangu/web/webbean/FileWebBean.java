package org.yws.pangu.web.webbean;

import java.io.Serializable;

import org.yws.pangu.enums.EFileType;

public class FileWebBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private boolean isFolder;
	private boolean isParent;
	
	public FileWebBean(Integer id, String name, Short type) {
		this.id = id;
		this.name = name;
		this.isFolder = EFileType.FOLDER.isEqual(type);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFolder() {
		return isFolder;
	}

	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

	public boolean getIsParent(){
		return isFolder;
	}
	
	public void setIsParent(){
		
	}
}
