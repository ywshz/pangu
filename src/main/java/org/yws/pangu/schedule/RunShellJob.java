package org.yws.pangu.schedule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yws.pangu.domain.LogOutputRedirector;
import org.yws.pangu.utils.DateRender;

public class RunShellJob implements Job {
	private static Logger logger = LoggerFactory.getLogger(RunShellJob.class);

	public static void main(String[] args) {
		String path = "f:/1.hive";
		try {
			String script = new RunShellJob().readFile(path);
			script = DateRender.render(script);
			logger.info("\n"+script);
		} catch (IOException e) {
			logger.error("Read File {} error", path);
			return;
		}
		logger.info(path);
		
	}
	public RunShellJob() {
		
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap map = context.getJobDetail().getJobDataMap();
		String path = (String) map.get("script-path");
		File file = null;
		try {
			String script = readFile(path);
			script = DateRender.render(script);
			logger.info(script);
			file = File.createTempFile(UUID.randomUUID().toString(), ".sh");
			file.createNewFile();
			file.setExecutable(true);
			file.setReadable(true);
			file.setWritable(true);
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(script);
			out.close();
		} catch (IOException e) {
			logger.error("Read File {} error", path);
			return;
		}
		
		logger.info(path);
		ProcessBuilder builder = new ProcessBuilder(file.getAbsolutePath());
		logger.info(file.getAbsolutePath());
		Process process = null;
		try {
			process = builder.start();
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}
		final InputStream inputStream = process.getInputStream();
		final InputStream errorStream = process.getErrorStream();

		// TODO:
		LogOutputRedirector outRedirect = new LogOutputRedirector(inputStream);
		LogOutputRedirector outToConsole = new LogOutputRedirector(errorStream);
		outRedirect.start();
		outToConsole.start();

		try {
			process.waitFor();
		} catch (InterruptedException e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			process = null;
		}
	}

	private String readFile(String path) throws IOException {
		File file = new File(path);
		FileReader fr = new FileReader(file);
		char[] chars = new char[(int) file.length()];
		fr.read(chars);
		fr.close();
		return String.valueOf(chars);
	}
	
}
