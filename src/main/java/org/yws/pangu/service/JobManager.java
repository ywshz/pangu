package org.yws.pangu.service;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.EverythingMatcher.allJobs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Matcher;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.yws.pangu.domain.JobBean;
import org.yws.pangu.job.HiveJob;
import org.yws.pangu.job.ManualHiveJob;
import org.yws.pangu.job.ManualShellJob;
import org.yws.pangu.job.ShellJob;
import org.yws.pangu.job.listener.JobExcutedListener;
import org.yws.pangu.service.impl.JobServiceImpl;

@Service
@DependsOn("schedulerFactory")
public class JobManager {
	private static final String MANUAL = "MANUAL_";
	@Autowired
	private SchedulerFactory schedulerFactory;
	@Autowired
	private JobServiceImpl jobService;
	private Scheduler scheduler;
	private Map<String, Set<String>> dependenciesMap = new HashMap<String, Set<String>>();
	private Map<String, Set<String>> waitingMap = new HashMap<String, Set<String>>();

	@PostConstruct
	public void init() throws SchedulerException {
		this.scheduler = schedulerFactory.getScheduler();

		JobExcutedListener lis = new JobExcutedListener(this);
		scheduler.getListenerManager().addJobListener(lis, allJobs());

		scheduler.getListenerManager().addJobListener(new JobListener() {

			@Override
			public void jobWasExecuted(JobExecutionContext context,
					JobExecutionException jobException) {
				try {
					scheduler.deleteJob(context.getJobDetail().getKey());
				} catch (SchedulerException e) {
				}
			}

			@Override
			public void jobToBeExecuted(JobExecutionContext context) {
			}

			@Override
			public void jobExecutionVetoed(JobExecutionContext context) {
			}

			@Override
			public String getName() {
				return "NO_SHCEDULE_JOB";
			}
		}, new Matcher<JobKey>() {

			/**  */
			private static final long serialVersionUID = -7993829482735351176L;

			@Override
			public boolean isMatch(JobKey key) {
				return key.getName().startsWith(MANUAL);
			}
		});

		List<JobBean> autoRunJobs = jobService.getAllAutoRunJobs();

		for (JobBean job : autoRunJobs) {
			scheduleJob(job);
		}

		this.scheduler.start();
	}

	public void scheduleJob(JobBean jobBean) throws SchedulerException {
		if (JobBean.RUN_BY_TIME.equals(jobBean.getScheduleType())) {

			JobDetail job = null;
			if (JobBean.HIVE_JOB.equals(jobBean.getRunType())) {
				job = JobBuilder.newJob(HiveJob.class).withIdentity(jobBean.getId().toString())
						.build();

			} else if (JobBean.SHELL_JOB.equals(jobBean.getRunType())) {
				job = JobBuilder.newJob(ShellJob.class).withIdentity(jobBean.getId().toString())
						.build();
			}

			job.getJobDataMap().put("JobService", jobService);

			CronTrigger trigger = newTrigger().withIdentity(jobBean.getId().toString())
					.withSchedule(cronSchedule(jobBean.getCron())).build();

			scheduler.scheduleJob(job, trigger);

		} else if (JobBean.RUN_BY_DEPENDENCY.equals(jobBean.getScheduleType())) {
			Set<String> set = new HashSet<String>();
			for (String id : jobBean.getDependencies().split(",")) {
				set.add(id);
			}
			dependenciesMap.put(new String(jobBean.getId().toString()), new HashSet<String>(set));
			waitingMap.put(jobBean.getId().toString(), new HashSet<String>(set));
			set.clear();
		}

	}

	public void scheduleJob(Integer jobId) throws SchedulerException {
		scheduleJob(jobService.getJob(jobId));
	}

	public void noWaitJob(Integer jobId) throws SchedulerException {

		JobBean jobBean = jobService.getJob(jobId);

		if (scheduler.checkExists(new JobKey(jobId.toString()))) {
			scheduler.triggerJob(new JobKey(jobId.toString()));
		} else {
			JobDetail job = null;
			if (JobBean.HIVE_JOB.equals(jobBean.getRunType())) {
				job = JobBuilder.newJob(HiveJob.class).withIdentity(jobBean.getId().toString())
						.build();

			} else if (JobBean.SHELL_JOB.equals(jobBean.getRunType())) {
				job = JobBuilder.newJob(ShellJob.class).withIdentity(jobBean.getId().toString())
						.build();
			}

			job.getJobDataMap().put("JobService", jobService);
			job.getJobDataMap().put("NO_WAIT_JOB", Boolean.TRUE);

			Trigger trigger = newTrigger().withIdentity(jobBean.getId().toString()).startNow()
					.build();

			scheduler.scheduleJob(job, trigger);
		}
	}

	public void noScheduleJob(Integer jobId) throws SchedulerException {
		JobBean jobBean = jobService.getJob(jobId);
		String jobKeyId = MANUAL + jobId.toString();
		if (scheduler.checkExists(new JobKey(jobKeyId))) {
			scheduler.triggerJob(new JobKey(jobKeyId));
		} else {
			JobDetail job = null;
			if (JobBean.HIVE_JOB.equals(jobBean.getRunType())) {
				job = JobBuilder.newJob(ManualHiveJob.class).withIdentity(jobKeyId).build();

			} else if (JobBean.SHELL_JOB.equals(jobBean.getRunType())) {
				job = JobBuilder.newJob(ManualShellJob.class).withIdentity(jobKeyId).build();
			}

			job.getJobDataMap().put("JobService", jobService);

			Trigger trigger = newTrigger().withIdentity(jobKeyId).startNow().build();

			scheduler.scheduleJob(job, trigger);
		}
	}

	public void removeJob(JobBean jobBean) throws SchedulerException {
		waitingMap.remove(jobBean.getId().toString());
		dependenciesMap.remove(jobBean.getId().toString());
		scheduler.deleteJob(new JobKey(jobBean.getId().toString()));
	}

	public void removeJob(Integer jobId) throws SchedulerException {
		removeJob(jobService.getJob(jobId));
	}

	public Map<String, Set<String>> getDependenciesMap() {
		return dependenciesMap;
	}

	public Map<String, Set<String>> getWaitingMap() {
		return waitingMap;
	}
}
