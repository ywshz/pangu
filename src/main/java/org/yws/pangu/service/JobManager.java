package org.yws.pangu.service;

import org.yws.pangu.domain.JobParam;

public class JobManager {
	private JobExecutor executor;
	
	boolean createJob(JobParam param) {
		return false;
	}

	boolean runJob(Job job) {
		
		return executor.addJob(job);
	}

	boolean cancel(Job job) {
		job.cancel();
		return true;
	}

}
