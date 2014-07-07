package org.yws.pangu.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import org.hibernate.SessionFactory;
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

	public int execute(String content) throws IOException {
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
		MemoryHelper.LOG_MAP.put("1", new StringBuffer("PANGU> Job start...\n"));
		MemoryHelper.JOB_STATUS_MAP.put("1", "RUNNING");
		OutputRedirector outRedirect = new OutputRedirector(inputStream, "CONSOLE");
		OutputRedirector outToConsole = new OutputRedirector(errorStream, "CONSOLE");
		outRedirect.start();
		outToConsole.start();

		int exitCode = -999;
		try {
			exitCode = process.waitFor();
			MemoryHelper.JOB_STATUS_MAP.put("1", "END");
		} catch (InterruptedException e) {
			System.out.println(e);
		} finally {
			process = null;
		}

		return exitCode;
	}
}
