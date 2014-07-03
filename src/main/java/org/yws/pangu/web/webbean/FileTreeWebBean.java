package org.yws.pangu.web.webbean;

import java.io.Serializable;
import java.util.List;

public class FileTreeWebBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id="root";
	private String text;
	private String state="opened";
	public String getState() {
		return state;
	}

	private List<FileWebBean> children;

	public FileTreeWebBean(String text, List<FileWebBean> children) {
		this.text=text;
		this.children=children;
		
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<FileWebBean> getChildren() {
		return children;
	}

	public void setChildren(List<FileWebBean> children) {
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setState(String state) {
		this.state = state;
	}

	
}
