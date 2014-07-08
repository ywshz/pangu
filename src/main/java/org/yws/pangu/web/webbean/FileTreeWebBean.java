package org.yws.pangu.web.webbean;

import java.io.Serializable;
import java.util.List;

import org.yws.pangu.domain.FileBean;

public class FileTreeWebBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private boolean isFolder;
	private boolean open = true;
	private List<FileWebBean> children;

	public FileTreeWebBean(String id, String name, Short type, List<FileWebBean> children) {
		this.id = id;
		this.name = name;
		this.children = children;
		this.isFolder = FileBean.FOLDER == type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public List<FileWebBean> getChildren() {
		return children;
	}

	public void setChildren(List<FileWebBean> children) {
		this.children = children;
	}

	public boolean getFolder() {
		return isFolder;
	}

	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

}
