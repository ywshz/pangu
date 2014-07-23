package org.yws.pangu.schedule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yws.pangu.domain.LogOutputRedirector;

public class RunShellJob implements Job {
	private static Logger logger = LoggerFactory.getLogger(RunShellJob.class);

	public RunShellJob() {

	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap map = context.getJobDetail().getJobDataMap();
		String path = (String) map.get("script-path");
		File file = new File(path);

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

}
