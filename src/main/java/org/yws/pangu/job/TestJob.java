package org.yws.pangu.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;
import org.yws.pangu.domain.JobBean;
import org.yws.pangu.service.impl.JobServiceImpl;

@Service
public class TestJob implements org.quartz.Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobServiceImpl jobService = (JobServiceImpl) context.getJobDetail().getJobDataMap()
				.get("JobService");
		JobBean jobBean = (JobBean) context.getJobDetail().getJobDataMap().get("JobBean");
		jobService.updateJobAutoStatus(6);
		System.out.println(jobBean.getName());

	}

}