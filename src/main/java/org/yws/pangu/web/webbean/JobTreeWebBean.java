package org.yws.pangu.web.webbean;

import java.io.Serializable;
import java.util.List;

public class JobTreeWebBean implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private boolean isFolder;
	private boolean open = true;

	private List<JobTreeNodeWebBean> children;

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
	
	public boolean isChkDisabled() {
		return isFolder;
	}

	public void setChkDisabled(boolean chkDisabled) {
	}

	public List<JobTreeNodeWebBean> getChildren() {
		return children;
	}
	
	public void setIsRoot(boolean isRoot){
		
	}
	
	public boolean getIsRoot(){
		return true;
	}

	public void setChildren(List<JobTreeNodeWebBean> children) {
		this.children = children;
	}

}
