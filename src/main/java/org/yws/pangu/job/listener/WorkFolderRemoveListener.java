package org.yws.pangu.job.listener;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yws.pangu.job.HiveJob;
import org.yws.pangu.service.JobManager;

public class WorkFolderRemoveListener implements JobListener {
	private static Logger logger = LoggerFactory.getLogger(WorkFolderRemoveListener.class);
	
	@Override
	public String getName() {
		return "WorkFolderRemoveListener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		String workFolder = (String)context.get("WORK_FOLDER_PATH");
		File folder = new File(workFolder);
		if(folder.exists()){
			try {
				FileUtils.deleteDirectory(folder);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
}
