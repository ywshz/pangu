package org.yws.pangu.web.webbean;

import java.io.Serializable;

public class ColumnWebBean implements Serializable {
	/**  */
	private static final long serialVersionUID = -7950003122611154715L;
	private String name;
	private String type;
	private String comment;

	public ColumnWebBean(String name, String type, String comment) {
		super();
		this.name = name;
		this.type = type;
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
