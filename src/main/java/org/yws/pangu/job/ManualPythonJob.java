package org.yws.pangu.job;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yws.pangu.domain.JobBean;
import org.yws.pangu.domain.JobHistory;
import org.yws.pangu.enums.EJobTriggerType;
import org.yws.pangu.service.impl.JobServiceImpl;
import org.yws.pangu.utils.DateRender;
import org.yws.pangu.utils.JobExecutionMemoryHelper;

public class ManualPythonJob implements Job {
	protected static final int MAX_STORE_LINES = 10000;
	private static Logger logger = LoggerFactory.getLogger(ManualHiveJob.class);
	private final String HIVE_HOME;
	private final String HADOOP_HOME;

	private final String WORK_FOLDER;

	public ManualPythonJob() {

		Properties props = new Properties();
		try {
			props.load(ManualHiveJob.class
					.getResourceAsStream("/pangu-config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		HIVE_HOME = (String) props.get("HIVE_HOME");
		HADOOP_HOME = (String) props.get("HADOOP_HOME");
		WORK_FOLDER = (String) props.get("work.folder");
	}

	/**
	 * mkdir and return the absolute path
	 *
	 * @param historyId
	 * @return
	 */
	private String createJobFolder(Long historyId) {
		File folder = new File(WORK_FOLDER + File.separator
				+ historyId.toString());
		folder.mkdirs();
		return folder.getAbsolutePath();
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		JobServiceImpl jobService = (JobServiceImpl) context.getJobDetail()
				.getJobDataMap().get("JobService");
		String jobIdStr = ((String) context.getJobDetail().getJobDataMap()
				.get("JobId"));

		Integer jobId = null;
		if (jobIdStr == null) {
			jobId = Integer.valueOf(context.getJobDetail().getKey().getName()
					.replace("MANUAL_", ""));
		} else {
			jobId = Integer.valueOf(jobIdStr);
		}

		JobBean jobBean = jobService.getJob(jobId);

		JobHistory history = jobService.createJobHistory(jobId,
				EJobTriggerType.MANUAL_TRIGGER.getValue());

		final Long HISTORY_ID = -1 * history.getId();

		JobExecutionMemoryHelper.jobLogMemoryHelper.put(HISTORY_ID,
				new StringBuffer("Job start...\n"));
		JobExecutionMemoryHelper.jobStatusMemoryHelper.put(HISTORY_ID,
				JobExecutionMemoryHelper.RUNNING);

		JobExecutionMemoryHelper.jobLogMemoryHelper.put(HISTORY_ID,
				new StringBuffer("Job start...\n"));
		JobExecutionMemoryHelper.jobStatusMemoryHelper.put(HISTORY_ID,
				JobExecutionMemoryHelper.RUNNING);

		String absPath = null;
		try {

			JobExecutionMemoryHelper.jobLogMemoryHelper.get(HISTORY_ID).append(
					"创建工作目录" + "\n");

			absPath = createJobFolder(HISTORY_ID);
			context.put("WORK_FOLDER_PATH", absPath);

		} catch (Exception e1) {
			logger.error(e1.getMessage());
			JobExecutionMemoryHelper.jobLogMemoryHelper.get(HISTORY_ID).append(
					"Job run FAILED \n");

			history.setEndTime(new Date());
			history.setLog(JobExecutionMemoryHelper.jobLogMemoryHelper.get(
					HISTORY_ID).toString());
			history.setStatus(JobExecutionMemoryHelper.FAILED);
			jobService.updateHistory(history);

			JobExecutionMemoryHelper.jobStatusMemoryHelper.put(HISTORY_ID,
					JobExecutionMemoryHelper.FAILED);

			context.getJobDetail().getJobDataMap()
					.put("RUN_SUCCESS", Boolean.FALSE);

			JobExecutionMemoryHelper.jobLogMemoryHelper.remove(HISTORY_ID);
			JobExecutionMemoryHelper.jobStatusMemoryHelper.remove(HISTORY_ID);

			return;
		}

		JobExecutionMemoryHelper.jobLogMemoryHelper.get(HISTORY_ID).append(
				"读取工作脚本:\n");

		File file = null;
		String script = null;
		try {
			script = jobBean.getScript();
			script = DateRender.render(script);
			file = new File(absPath + File.separator
					+ UUID.randomUUID().toString() + ".hive");
			file.createNewFile();
			file.setExecutable(true);
			file.setReadable(true);
			file.setWritable(true);
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(script);
			out.close();
		} catch (IOException e) {
			logger.error("Write File {} error", e.getMessage());
			JobExecutionMemoryHelper.jobLogMemoryHelper.get(HISTORY_ID).append(
					"工作脚本读取失败: \n" + e.getMessage() + "\n");
			JobExecutionMemoryHelper.jobLogMemoryHelper.get(HISTORY_ID).append(
					"Job run FAILED \n");

			history.setEndTime(new Date());
			history.setLog(JobExecutionMemoryHelper.jobLogMemoryHelper.get(
					HISTORY_ID).toString());
			history.setStatus(JobExecutionMemoryHelper.FAILED);
			jobService.updateHistory(history);

			JobExecutionMemoryHelper.jobStatusMemoryHelper.put(HISTORY_ID,
					JobExecutionMemoryHelper.FAILED);

			context.getJobDetail().getJobDataMap()
					.put("RUN_SUCCESS", Boolean.FALSE);

			JobExecutionMemoryHelper.jobLogMemoryHelper.remove(HISTORY_ID);
			JobExecutionMemoryHelper.jobStatusMemoryHelper.remove(HISTORY_ID);

			return;
		}

		ProcessBuilder builder = new ProcessBuilder("python",
				file.getAbsolutePath());
		builder.directory(new File(absPath));
		builder.environment().put("HADOOP_HOME", HADOOP_HOME);
		builder.environment().put("HIVE_HOME", HIVE_HOME);
		Process process = null;
		try {
			process = builder.start();
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
			JobExecutionMemoryHelper.jobLogMemoryHelper.get(HISTORY_ID).append(
					"任务进程启动失败:" + e.getMessage() + "/n");
			JobExecutionMemoryHelper.jobLogMemoryHelper.get(HISTORY_ID).append(
					"Job run FAILED \n");

			history.setEndTime(new Date());
			history.setLog(JobExecutionMemoryHelper.jobLogMemoryHelper.get(
					HISTORY_ID).toString());
			history.setStatus(JobExecutionMemoryHelper.FAILED);
			jobService.updateHistory(history);

			JobExecutionMemoryHelper.jobStatusMemoryHelper.put(HISTORY_ID,
					JobExecutionMemoryHelper.FAILED);

			context.getJobDetail().getJobDataMap()
					.put("RUN_SUCCESS", Boolean.FALSE);

			JobExecutionMemoryHelper.jobLogMemoryHelper.remove(HISTORY_ID);
			JobExecutionMemoryHelper.jobStatusMemoryHelper.remove(HISTORY_ID);

			return;
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
							JobExecutionMemoryHelper.jobLogMemoryHelper.get(
									HISTORY_ID).append(line + "\n");
						} else if (curr == MAX_STORE_LINES) {
							JobExecutionMemoryHelper.jobLogMemoryHelper.get(
									HISTORY_ID).append(
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
							JobExecutionMemoryHelper.jobLogMemoryHelper.get(
									HISTORY_ID).append(line + "\n");
						} else if (curr == MAX_STORE_LINES) {
							JobExecutionMemoryHelper.jobLogMemoryHelper.get(
									HISTORY_ID).append(
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
			JobExecutionMemoryHelper.jobLogMemoryHelper.get(HISTORY_ID).append(
					"Job run SUCCESS \n");

			history.setEndTime(new Date());
			history.setLog(JobExecutionMemoryHelper.jobLogMemoryHelper.get(
					HISTORY_ID).toString());
			history.setStatus(JobExecutionMemoryHelper.SUCCESS);
			jobService.updateHistory(history);

			JobExecutionMemoryHelper.jobStatusMemoryHelper.put(HISTORY_ID,
					JobExecutionMemoryHelper.SUCCESS);

			context.getJobDetail().getJobDataMap()
					.put("RUN_SUCCESS", Boolean.TRUE);
		} else {
			JobExecutionMemoryHelper.jobLogMemoryHelper.get(HISTORY_ID).append(
					"Job run FAILED \n");

			history.setEndTime(new Date());
			history.setLog(JobExecutionMemoryHelper.jobLogMemoryHelper.get(
					HISTORY_ID).toString());
			history.setStatus(JobExecutionMemoryHelper.FAILED);
			jobService.updateHistory(history);

			JobExecutionMemoryHelper.jobStatusMemoryHelper.put(HISTORY_ID,
					JobExecutionMemoryHelper.FAILED);

			context.getJobDetail().getJobDataMap()
					.put("RUN_SUCCESS", Boolean.FALSE);
		}

		JobExecutionMemoryHelper.jobLogMemoryHelper.remove(HISTORY_ID);
		JobExecutionMemoryHelper.jobStatusMemoryHelper.remove(HISTORY_ID);
	}

}
