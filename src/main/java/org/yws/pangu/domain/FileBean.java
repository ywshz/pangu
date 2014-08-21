package org.yws.pangu.domain;

import java.util.List;

public class FileBean {

	private List<FileDescriptor> subFiles;
	private FileDescriptor fileDescriptor;

	public FileBean(FileDescriptor file) {
		this.fileDescriptor = file;
	}

	public List<FileDescriptor> getSubFiles() {
		return subFiles;
	}

	public void setSubFiles(List<FileDescriptor> subFiles) {
		this.subFiles = subFiles;
	}

	public FileDescriptor getFileDescriptor() {
		return fileDescriptor;
	}

	public void setFileDescriptor(FileDescriptor fileDescriptor) {
		this.fileDescriptor = fileDescriptor;
	}

}
