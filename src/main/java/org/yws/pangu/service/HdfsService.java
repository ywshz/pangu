package org.yws.pangu.service;

import java.io.IOException;
import java.util.List;

import org.yws.pangu.domain.HdfsFile;

/**
 * Created by ywszjut on 14-6-22.
 */
public interface HdfsService {

	public List<HdfsFile> getFiles(String path);

	public boolean isFile(String path) throws IOException;

	public String get(String fileName, int size) throws IOException;

	public HdfsFile get(String fileName) throws IOException;

	public boolean delete(String path) throws IOException;

	public boolean rename(String src, String dest) throws IOException;

}
