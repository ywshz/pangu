package org.yws.pangu.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.yws.pangu.domain.HdfsFile;
import org.yws.pangu.service.HdfsService;
import org.yws.pangu.utils.DateUtils;

/**
 * Created by ywszjut on 14-6-22.
 */
public class HdfsServiceImpl implements HdfsService {

	private Configuration conf = null;
	private FileSystem hdfs = null;
	private static final int PREVIEW_READ_SIZE = 10240;

	public HdfsServiceImpl() {
		conf = new Configuration();
		try {
			this.hdfs = FileSystem.get(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<HdfsFile> getFiles(String path) {
		List<HdfsFile> list = new ArrayList<HdfsFile>();
		try {
			for (FileStatus fs : hdfs.listStatus(new Path(path))) {
				HdfsFile hf = new HdfsFile();
				hf.setModificationTime(DateUtils.format(fs.getModificationTime()));
				hf.setPath(fs.getPath().toString());
				hf.setName(fs.getPath().getName());
				hf.setOwner(fs.getOwner());
				hf.setSize(fs.getLen());
				hf.setType(fs.isDir() ? "Dir" : "File");
				list.add(hf);
			}
			return list;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public String get(String path, int size) throws IOException {
		FSDataInputStream in = hdfs.open(new Path(path));
		byte[] bts = new byte[size];
		in.read(bts);
		return new String(bts);
	}

	@Override
	public boolean delete(String path) throws IOException {
		return hdfs.delete(new Path(path), true);
	}

	@Override
	public boolean rename(String src, String dst) throws IOException {
		return hdfs.rename(new Path(src), new Path(dst));
	}

	@Override
	public boolean isFile(String path) throws IOException {
		return hdfs.isFile(new Path(path));
	}

	@Override
	public HdfsFile get(String path) throws IOException {
		FileStatus fs = hdfs.getFileStatus(new Path(path));
		HdfsFile hf = new HdfsFile();
		hf.setModificationTime(DateUtils.format(fs.getModificationTime()));
		hf.setPath(fs.getPath().toString());
		hf.setName(fs.getPath().getName());
		hf.setOwner(fs.getOwner());
		hf.setSize(fs.getLen());
		hf.setType(fs.isDir() ? "Dir" : "File");

		byte[] data = new byte[PREVIEW_READ_SIZE];
		FSDataInputStream fis = hdfs.open(new Path(path));
		fis.read(data);

		String d = new String(data);
		d=d.replaceAll("<", "&lt;");
		d=d.replaceAll(">", "&gt;");
		d=d.replaceAll("$", "&amp");
		hf.setContent(d);

		return hf;
	}
	
}
