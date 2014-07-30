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
		
		jobManager.removeJob(jobBean);
		
		Thread.sleep(10000);
		
		jobManager.scheduleJob(jobBean);
		
		Thread.sleep(10000);
	}
	
}
