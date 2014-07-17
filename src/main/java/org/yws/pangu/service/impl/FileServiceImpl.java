package org.yws.pangu.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yws.pangu.dao.mysql.MySqlFileDao;
import org.yws.pangu.domain.FileBean;
import org.yws.pangu.domain.FileDescriptor;
import org.yws.pangu.domain.OutputRedirector;
import org.yws.pangu.utils.MemoryHelper;

/*
 * 
 */
@Service
public class FileServiceImpl {
	private final String HIVE_HOME;
	private final String HADOOP_HOME;

	@Autowired
	private MySqlFileDao fileDao;

	public FileServiceImpl() {
		Properties props = new Properties();
		try {
			props.load(FileServiceImpl.class.getResourceAsStream("/pangu-config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		HIVE_HOME = (String) props.get("HIVE_HOME");
		HADOOP_HOME = (String) props.get("HADOOP_HOME");
	}

	public FileBean getFile(Integer id, String owner) {
		FileDescriptor fd = fileDao.getFile(id, owner);
		if (!fd.getOwner().equals(owner)) {
			return null;
		}

		if (fd.getType() == FileBean.FOLDER) {
			return fileDao.fillFile(fd);
		} else {
			return new FileBean(fd);
		}
	}

	public int execute(Integer fileId, String content) throws IOException {
		// write file
		File file = File.createTempFile(UUID.randomUUID().toString(), ".hive");
		file.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write(content);
		out.close();

		// execute file
		ProcessBuilder builder = new ProcessBuilder(HIVE_HOME + "/bin/hive", "-f",
				file.getAbsolutePath());
		builder.environment().put("HADOOP_HOME", HADOOP_HOME);
		builder.environment().put("HIVE_HOME", HIVE_HOME);
		Process process = builder.start();
		final InputStream inputStream = process.getInputStream();
		final InputStream errorStream = process.getErrorStream();

		// TODO:
		MemoryHelper.LOG_MAP.put(fileId.toString(), new StringBuffer("PANGU> Job start...\n"));
		MemoryHelper.JOB_STATUS_MAP.put(fileId.toString(), "RUNNING");
		OutputRedirector outRedirect = new OutputRedirector(inputStream, "CONSOLE");
		OutputRedirector outToConsole = new OutputRedirector(errorStream, "CONSOLE");
		outRedirect.start();
		outToConsole.start();

		int exitCode = -999;
		try {
			exitCode = process.waitFor();
			MemoryHelper.JOB_STATUS_MAP.put(fileId.toString(), "END");
		} catch (InterruptedException e) {
			System.out.println(e);
		} finally {
			process = null;
		}

		return exitCode;
	}

	public String getContent(Integer fileId, String owner) {
		return fileDao.getFile(fileId, owner).getContent();
	}

	public boolean updateContent(Integer fileId, String content, String owner) {
		FileDescriptor fd = fileDao.getFile(fileId, owner);
		fd.setGmtModified(new Date());
		fd.setContent(content);
		try {
			fileDao.update(fd);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public Integer save(FileDescriptor fd) {
		Date d = new Date();
		fd.setGmtCreate(d);
		fd.setGmtModified(d);
		return fileDao.save(fd);
	}
}
