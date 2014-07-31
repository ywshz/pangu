package org.yws.pangu.job.listener;

import java.util.HashSet;
import java.util.Set;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.SchedulerException;
import org.yws.pangu.service.JobManager;

public class JobExcutedListener implements JobListener {
	private JobManager jobManager;

	public JobExcutedListener(JobManager jobManager) {
		this.jobManager = jobManager;
	}

	@Override
	public String getName() {
		return "JobExcutedListener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {

		String jobId = context.getJobDetail().getKey().getName();

		for (String key : jobManager.getWaitingMap().keySet()) {
			Set<String> set = jobManager.getWaitingMap().get(key);
			if (!set.isEmpty()) {
				set.remove(jobId);
			}

			if (set.isEmpty()) {
				try {
					//reset waiting job info
					jobManager.getWaitingMap().put(key, new HashSet<String>(jobManager.getDependenciesMap().get(key)));
					jobManager.noWaitJob(Integer.valueOf(key));
				} catch (SchedulerException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
