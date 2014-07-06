package org.yws.pangu.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.yws.pangu.domain.OutputRedirector;
import org.yws.pangu.utils.MemoryHelper;

/*
 * 
 */
public class FileServiceImpl {
	public int execute(String content) throws IOException {
		// write file
		File file = File.createTempFile(UUID.randomUUID().toString(), ".hive");
		file.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write(content);
		out.close();

		// execute file
		ProcessBuilder builder = new ProcessBuilder(
				"/Users/ywszjut/Documents/dev/hive-0.12.0/bin/hive", "-f",
				file.getAbsolutePath());
		builder.environment().put("HADOOP_HOME",
				"/Users/ywszjut/Documents/dev/hadoop-1.2.1");
		builder.environment().put("HIVE_HOME",
				"/Users/ywszjut/Documents/dev/hive-0.12.0");
		Process process = builder.start();
		final InputStream inputStream = process.getInputStream();
		final InputStream errorStream = process.getErrorStream();
		
		//TODO:
		MemoryHelper.LOG_MAP.put("1", new StringBuffer("PANGU> Job start...\n"));
		MemoryHelper.JOB_STATUS_MAP.put("1", "RUNNING");
		OutputRedirector outRedirect = new OutputRedirector(
				inputStream, "CONSOLE");
        OutputRedirector outToConsole = new OutputRedirector(
        		errorStream, "CONSOLE");
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

	public static void main(String[] args) throws IOException {
		new FileServiceImpl().execute("show tables;");
	}
}
