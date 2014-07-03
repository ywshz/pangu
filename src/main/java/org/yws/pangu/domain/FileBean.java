package org.yws.pangu.domain;

import java.io.FileDescriptor;
import java.util.List;

public class FileBean {
	
	public static int FOLDER=1;
	public static int FILE=2;
	
	private FileBean parent;
	private List<FileBean> subFiles;
	private FileDescriptor fileDescriptor;

	public FileBean(FileDescriptor file) {
		this.fileDescriptor = file;
	}

	public void addSubFile(FileBean bean) {
		if (!subFiles.contains(bean)) {
			subFiles.add(bean);
		}
	}

	public List<FileBean> getSubFiles() {
		return subFiles;
	}

	public FileDescriptor getFileDescriptor() {
		return fileDescriptor;
	}

	public FileBean getParent() {
		return parent;
	}

	public void setParent(FileBean parent) {
		this.parent = parent;
	}
}
