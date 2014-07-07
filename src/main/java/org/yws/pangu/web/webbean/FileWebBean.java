package org.yws.pangu.web.webbean;

import java.io.Serializable;

public class FileWebBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private StateWebBean state;
	private String text;
	private String type;

	public FileWebBean(Integer integer, String text, boolean b) {
		this.state = new StateWebBean(true, false, false);
		this.id = integer;
		this.text = text;
		this.type = b ? "folder" : "file";
	}

	public FileWebBean(Integer id, String text) {
		this.id = id;
		this.text = text;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public StateWebBean getState() {
		return state;
	}

	public void setState(StateWebBean state) {
		this.state = state;
	}

}

class StateWebBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean opened = false;
	boolean disabled = false;
	boolean selected = false;

	public StateWebBean(boolean opened, boolean disabled, boolean selected) {
		this.opened = opened;
		this.disabled = disabled;
		this.selected = selected;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
