package org.yws.pangu.service;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import javax.annotation.PostConstruct;

import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.yws.pangu.domain.JobBean;
import org.yws.pangu.job.TestJob;
import org.yws.pangu.service.impl.JobServiceImpl;

@Service
@DependsOn("schedulerFactory")
public class JobManager {
	@Autowired
	private SchedulerFactory schedulerFactory;
@Autowired
private JobServiceImpl jobService;
	private Scheduler scheduler;

	@PostConstruct
	public void init() throws SchedulerException {
		this.scheduler = schedulerFactory.getScheduler();
		this.scheduler.start();
	}

	public void scheduleJob(JobBean jobBean) throws SchedulerException {
		if (JobBean.RUN_BY_TIME.equals(jobBean.getScheduleType())) {
			JobDetail job = JobBuilder.newJob(TestJob.class)
					.withIdentity(jobBean.getId().toString(), jobBean.getOwner()).build();

			job.getJobDataMap().put("JobService", jobService);
			job.getJobDataMap().put("JobBean", jobBean);

			CronTrigger trigger = newTrigger()
					.withIdentity(jobBean.getId().toString(), jobBean.getOwner())
					.withSchedule(cronSchedule(jobBean.getCron()))
					.forJob(jobBean.getId().toString(), jobBean.getOwner()).build();

			scheduler.scheduleJob(job, trigger);
		}
	}

	public void removeJob(JobBean jobBean) throws SchedulerException {
		scheduler.deleteJob(new JobKey(jobBean.getId().toString(), jobBean.getOwner()));
	}

}
