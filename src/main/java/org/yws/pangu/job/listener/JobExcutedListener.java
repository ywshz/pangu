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
	private byte[] lock = new byte[0];

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

		if(context.getJobDetail().getJobDataMap().get("RUN_SUCCESS")==Boolean.FALSE){
			return;
		}
		
		String jobId = context.getJobDetail().getKey().getName();

		boolean isEmpty = false;
		String runKey = null;
		synchronized (lock) {
			for (String key : jobManager.getWaitingMap().keySet()) {
				Set<String> set = jobManager.getWaitingMap().get(key);
				set.remove(jobId);

				if (set.isEmpty()) {
					isEmpty = true;
					runKey = key;
				}
			}
		}
		if (isEmpty) {
			try {
				// reset waiting job info
				jobManager.getWaitingMap().put(runKey,
						new HashSet<String>(jobManager.getDependenciesMap().get(runKey)));
				jobManager.noWaitJob(Integer.valueOf(runKey));
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}

	}
}
