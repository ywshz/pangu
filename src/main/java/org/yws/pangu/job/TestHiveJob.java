package org.yws.pangu.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.yws.pangu.domain.JobHistory;
import org.yws.pangu.enums.EJobTriggerType;
import org.yws.pangu.service.impl.JobServiceImpl;

public class TestHiveJob implements org.quartz.Job {
	public static Map<Long, StringBuffer> jobLogMemoryHelper = new HashMap<Long, StringBuffer>();
	public static Map<Long, String> jobStatusMemoryHelper = new HashMap<Long, String>();
	public static final String RUNNING = "RUNNING";
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobServiceImpl jobService = (JobServiceImpl) context.getJobDetail().getJobDataMap()
				.get("JobService");

		Integer jobId = Integer.valueOf(context.getJobDetail().getKey().getName());
		System.out.println("Job : " + context.getJobDetail().getKey().getName() + " Running.");

		JobHistory history = jobService.createJobHistory(jobId,  EJobTriggerType.AUTO_TRIGGER.getValue());

		final Long HISTORY_ID = history.getId();

		jobLogMemoryHelper.put(HISTORY_ID, new StringBuffer("Job start...\n"));
		jobStatusMemoryHelper.put(HISTORY_ID, RUNNING);
		ProcessBuilder builder = new ProcessBuilder("pwd");

		Process process = null;
		try {
			process = builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		final InputStream inputStream = process.getInputStream();
		final InputStream errorStream = process.getErrorStream();

		Thread normal = new Thread() {
			@Override
			public void run() {
				try {
					InputStreamReader isr = new InputStreamReader(inputStream);
					BufferedReader br = new BufferedReader(isr);
					String line = null;
					while ((line = br.readLine()) != null) {
						jobLogMemoryHelper.get(HISTORY_ID).append(line + "\n");
					}
				} catch (IOException ioE) {
					ioE.printStackTrace();
				}
			}
		};

		Thread error = new Thread() {
			@Override
			public void run() {
				try {
					InputStreamReader isr = new InputStreamReader(errorStream);
					BufferedReader br = new BufferedReader(isr);
					String line = null;
					while ((line = br.readLine()) != null) {
						jobLogMemoryHelper.get(HISTORY_ID).append(line + "\n");
					}
				} catch (IOException ioE) {
					ioE.printStackTrace();
				}
			}
		};

		normal.start();
		error.start();

		int exitCode = -999;
		try {
			exitCode = process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			process = null;
		}

		if (exitCode == 0) {
			jobLogMemoryHelper.get(HISTORY_ID).append("Job run SUCCESS \n");

			history.setEndTime(new Date());
			history.setLog(jobLogMemoryHelper.get(HISTORY_ID).toString());
			history.setStatus(SUCCESS);
			jobService.updateHistory(history);

			jobStatusMemoryHelper.put(HISTORY_ID, SUCCESS);
		} else {
			jobLogMemoryHelper.get(HISTORY_ID).append("Job run FAILED \n");

			history.setEndTime(new Date());
			history.setLog(jobLogMemoryHelper.get(HISTORY_ID).toString());
			history.setStatus(FAILED);
			jobService.updateHistory(history);

			jobStatusMemoryHelper.put(HISTORY_ID, FAILED);
		}

		jobLogMemoryHelper.remove(HISTORY_ID);
		jobStatusMemoryHelper.remove(HISTORY_ID);
	}

}