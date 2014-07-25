package org.yws.pangu.web.webbean;

import java.io.Serializable;

public class JobTreeNodeWebBean implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private boolean isFolder;
	private boolean open = true;

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

	public boolean isFolder() {
		return isFolder;
	}

	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean getIsParent() {
		return isFolder;
	}

	public void setIsParent() {

	}

	public boolean isChkDisabled() {
		return isFolder;
	}

	public void setChkDisabled(boolean chkDisabled) {
	}
}
