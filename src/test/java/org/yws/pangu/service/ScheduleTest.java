package org.yws.pangu.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.yws.pangu.domain.JobBean;
import org.yws.pangu.service.impl.JobServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class ScheduleTest {

	@Autowired
	JobManager jobManager;
	@Autowired
	JobServiceImpl jobService;
	@Test
	public void schedule() throws InterruptedException, SchedulerException {

		JobBean jobBean = new JobBean();
		jobBean.setCron("*/2 * * * * ?");
		jobBean.setId(1);
		jobBean.setRunType("hive");
		jobBean.setScheduleType(JobBean.RUN_BY_TIME);
		jobBean.setOwner("1");
		
		try {
			jobManager.scheduleJob(jobBean);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		Thread.sleep(10000);
		
//		jobManager.removeJob(jobBean);
		
		Thread.sleep(10000);
		
		jobManager.scheduleJob(jobBean);
		
		Thread.sleep(10000);
	}
	
	@Test
	public void schedule1() throws Exception{
		JobBean job5 = jobService.getJob(5);
		JobBean job6 = jobService.getJob(6);
		JobBean job7 = jobService.getJob(7);
		JobBean job8 = jobService.getJob(8);
		JobBean job9 = jobService.getJob(9);
		jobManager.scheduleJob(job5);
		jobManager.scheduleJob(job6);
		jobManager.scheduleJob(job7);
		jobManager.scheduleJob(job8);
		jobManager.scheduleJob(job9);
		
		Thread.sleep(10000);
		System.out.println("*********************");
		jobManager.removeJob(job9);
		
		Thread.sleep(60000);
	}
	
}
