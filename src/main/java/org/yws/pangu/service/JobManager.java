package org.yws.pangu.service;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.EverythingMatcher.allJobs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.yws.pangu.domain.JobBean;
import org.yws.pangu.job.TestJob;
import org.yws.pangu.job.listener.JobExcutedListener;
import org.yws.pangu.service.impl.JobServiceImpl;

@Service
@DependsOn("schedulerFactory")
public class JobManager {
	private static final String NORMAL_JOB = "NORMAL_JOB";
	private static final String NOWAIT_JOB = "NOWAIT_JOB";
	private static final String NORMAL_TRIGGER = "NORMAL_TRIGGER";
	private static final String NOWAIT_TRIGGER = "NOWAIT_TRIGGER";

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
		this.scheduler.start();
		JobExcutedListener lis = new JobExcutedListener(this);
		scheduler.getListenerManager().addJobListener(lis, allJobs());
		// scheduler.getListenerManager().addJobListener(lis, new
		// Matcher<JobKey>() {
		//
		// /** */
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public boolean isMatch(JobKey key) {
		// return "NORMAL_JOB".equals(key.getGroup());
		// }
		// });
		//
		// scheduler.getListenerManager().addJobListener(new JobListener() {
		//
		// @Override
		// public void jobWasExecuted(JobExecutionContext context,
		// JobExecutionException jobException) {
		// }
		//
		// @Override
		// public void jobToBeExecuted(JobExecutionContext context) {
		// }
		//
		// @Override
		// public void jobExecutionVetoed(JobExecutionContext context) {
		// try {
		// removeNoWaitJob(Integer.valueOf(context.getJobDetail().getKey().getName()));
		// } catch (NumberFormatException | SchedulerException e) {
		// e.printStackTrace();
		// }
		// }
		//
		// @Override
		// public String getName() {
		// return "NOWAIT_JOB_LITENER";
		// }
		// }, new Matcher<JobKey>() {
		//
		// /** */
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public boolean isMatch(JobKey key) {
		// return "NO_WAIT_JOB".equals(key.getGroup());
		// }
		// });

		// TODO: init all auto=1 jobs
	}

	public void scheduleJob(JobBean jobBean) throws SchedulerException {
		if (JobBean.RUN_BY_TIME.equals(jobBean.getScheduleType())) {

			JobDetail job = null;
			if (JobBean.HIVE_JOB.equals(jobBean.getRunType())) {
				job = JobBuilder.newJob(TestJob.class).withIdentity(jobBean.getId().toString())
						.build();

			} else if (JobBean.SHELL_JOB.equals(jobBean.getRunType())) {
				job = JobBuilder.newJob(TestJob.class).withIdentity(jobBean.getId().toString())
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
				job = JobBuilder.newJob(TestJob.class).withIdentity(jobBean.getId().toString())
						.build();

			} else if (JobBean.SHELL_JOB.equals(jobBean.getRunType())) {
				job = JobBuilder.newJob(TestJob.class).withIdentity(jobBean.getId().toString())
						.build();
			}

			job.getJobDataMap().put("JobService", jobService);
			job.getJobDataMap().put("NO_WAIT_JOB", Boolean.TRUE);

			Trigger trigger = newTrigger().withIdentity(jobBean.getId().toString()).startNow()
					.build();

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

	//
	// public void removeNoWaitJob(Integer jobId) throws SchedulerException {
	// scheduler.deleteJob(new JobKey(jobId.toString()));
	// }
	//

	public Map<String, Set<String>> getDependenciesMap() {
		return dependenciesMap;
	}

	public Map<String, Set<String>> getWaitingMap() {
		return waitingMap;
	}

}
