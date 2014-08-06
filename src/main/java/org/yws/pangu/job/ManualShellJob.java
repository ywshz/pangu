package org.yws.pangu.job;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yws.pangu.domain.JobBean;
import org.yws.pangu.domain.JobHistory;
import org.yws.pangu.schedule.RunShellJob;
import org.yws.pangu.service.impl.JobServiceImpl;
import org.yws.pangu.utils.DateRender;
import org.yws.pangu.utils.JobExecutionMemoryHelper;

public class ManualShellJob implements Job {
	protected static final int MAX_STORE_LINES = 10000;
	private static Logger logger = LoggerFactory.getLogger(RunShellJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobServiceImpl jobService = (JobServiceImpl) context.getJobDetail().getJobDataMap()
				.get("JobService");
		Integer jobId = Integer.valueOf(context.getJobDetail().getKey().getName()
				.replace("MANUAL_", ""));

		JobBean jobBean = jobService.getJob(jobId);

		File file = null;
		try {
			String script = jobBean.getScript();
			script = DateRender.render(script);
			file = File.createTempFile(UUID.randomUUID().toString(), ".hive");
			file.createNewFile();
			file.setExecutable(true);
			file.setReadable(true);
			file.setWritable(true);
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(script);
			out.close();
		} catch (IOException e) {
			logger.error("Write File {} error", e.getMessage());
			return;
		}

		JobHistory history = jobService.createJobHistory(jobId, JobHistory.AUTO_TRIGGER);

		final Long HISTORY_ID = history.getId();

		JobExecutionMemoryHelper.jobLogMemoryHelper.put(HISTORY_ID, new StringBuffer(
				"Job start...\n"));
		JobExecutionMemoryHelper.jobStatusMemoryHelper.put(HISTORY_ID,
				JobExecutionMemoryHelper.RUNNING);

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

		final AtomicInteger lineCount = new AtomicInteger(0);

		Thread normal = new Thread() {
			@Override
			public void run() {
				try {
					InputStreamReader isr = new InputStreamReader(inputStream);
					BufferedReader br = new BufferedReader(isr);
					String line = null;
					while ((line = br.readLine()) != null) {
						int curr = lineCount.getAndIncrement();
						if (curr < MAX_STORE_LINES) {
							JobExecutionMemoryHelper.jobLogMemoryHelper.get(HISTORY_ID).append(
									line + "\n");
						} else if (curr == MAX_STORE_LINES) {
							JobExecutionMemoryHelper.jobLogMemoryHelper.get(HISTORY_ID).append(
									"该任务LOG已有1万条,为减少内存占用,停止记录\n");
						}
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
						int curr = lineCount.getAndIncrement();
						if (curr < MAX_STORE_LINES) {
							JobExecutionMemoryHelper.jobLogMemoryHelper.get(HISTORY_ID).append(
									line + "\n");
						} else if (curr == MAX_STORE_LINES) {
							JobExecutionMemoryHelper.jobLogMemoryHelper.get(HISTORY_ID).append(
									"该任务LOG已有1万条,为减少内存占用,停止记录\n");
						}
					}
				} catch (IOException ioE) {
					ioE.printStackTrace();
				}
			}
		};

		normal.start();
		error.start();

		while (normal.isAlive() || error.isAlive()) {
			try {
				Thread.sleep(1000);
				System.out.println("Runing , waiting 1s");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		int exitCode = -999;
		try {
			exitCode = process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			process = null;
		}

		if (exitCode == 0) {
			JobExecutionMemoryHelper.jobLogMemoryHelper.get(HISTORY_ID)
					.append("Job run SUCCESS \n");

			history.setEndTime(new Date());
			history.setLog(JobExecutionMemoryHelper.jobLogMemoryHelper.get(HISTORY_ID).toString());
			history.setStatus(JobExecutionMemoryHelper.SUCCESS);
			jobService.updateHistory(history);

			JobExecutionMemoryHelper.jobStatusMemoryHelper.put(HISTORY_ID,
					JobExecutionMemoryHelper.SUCCESS);
		} else {
			JobExecutionMemoryHelper.jobLogMemoryHelper.get(HISTORY_ID).append("Job run FAILED \n");

			history.setEndTime(new Date());
			history.setLog(JobExecutionMemoryHelper.jobLogMemoryHelper.get(HISTORY_ID).toString());
			history.setStatus(JobExecutionMemoryHelper.FAILED);
			jobService.updateHistory(history);

			JobExecutionMemoryHelper.jobStatusMemoryHelper.put(HISTORY_ID,
					JobExecutionMemoryHelper.FAILED);
		}

		JobExecutionMemoryHelper.jobLogMemoryHelper.remove(HISTORY_ID);
		JobExecutionMemoryHelper.jobStatusMemoryHelper.remove(HISTORY_ID);
	}

}
