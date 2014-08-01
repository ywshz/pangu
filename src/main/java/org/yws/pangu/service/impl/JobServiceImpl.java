package org.yws.pangu.service.impl;

import java.util.Date;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yws.pangu.dao.mysql.MySqlJobDao;
import org.yws.pangu.dao.mysql.MySqlJobGroupDao;
import org.yws.pangu.dao.mysql.MySqlJobHistoryDao;
import org.yws.pangu.domain.JobBean;
import org.yws.pangu.domain.JobGroup;
import org.yws.pangu.domain.JobHistory;
import org.yws.pangu.job.ManualHiveJob;
import org.yws.pangu.job.ManualShellJob;
import org.yws.pangu.service.JobManager;
import org.yws.pangu.utils.JobExecutionMemoryHelper;

@Service
public class JobServiceImpl {

	@Autowired
	private MySqlJobGroupDao groupDao;
	@Autowired
	private MySqlJobDao jobDao;
	@Autowired
	private MySqlJobHistoryDao jobHistoryDao;
	@Autowired
	private JobManager jobManager;

	public List<JobBean> jobList(Integer groupId) {
		return jobDao.list(groupId);
	}

	public List<JobGroup> groupList(Integer parentId) {
		return groupDao.list(parentId);
	}

	public JobGroup getRootGroup(String owner) {
		return groupDao.getRootGroup(owner);
	}

	public JobBean getJob(Integer id) {
		return jobDao.getJob(id);
	}

	public void updateJob(JobBean job) {
		jobDao.update(job);
	}

	public List<JobHistory> listJobHistory(Integer jobId) {
		return jobHistoryDao.list(jobId);
	}

	public JobHistory createJobHistory(Integer jobId, Integer triggerType) {
		JobBean job = getJob(jobId);
		JobHistory history = new JobHistory();
		history.setJobId(jobId);
		history.setOperator(job.getOwner());
		history.setStartTime(new Date());
		history.setStatus(JobExecutionMemoryHelper.RUNNING);
		history.setTriggerType(JobHistory.AUTO_TRIGGER);
		jobHistoryDao.save(history);

		job.setHistoryId(history.getId());
		updateJob(job);

		return history;
	}

	public Integer save(JobBean job) {
		return jobDao.save(job);
	}

	public Integer save(JobGroup group) {
		return groupDao.save(group);
	}

	public void updateGroupName(Integer id, String name) {
		JobGroup group = groupDao.getGroup(id);
		group.setName(name);
		groupDao.update(group);
	}

	/**
	 * 
	 * @param id
	 * @param owner
	 * @return 1=delete success. -1=exception, 0=still have child items,not
	 *         allow to delete it
	 */
	public int deleteGroup(Integer id, String owner) {
		if (this.jobDao.list(id).size() > 0) {
			return 0;
		} else {
			try {
				groupDao.delete(groupDao.getGroup(id));
			} catch (Exception e) {
				return -1;
			}
			return 1;
		}

	}

	/**
	 * 
	 * @param id
	 *            job id
	 * @return closed=job auto status set as closed, opened=job auto status set
	 *         to opened
	 */
	public String updateJobAutoStatus(Integer id) {
		String rs = null;
		JobBean job = getJob(id);

		try {
			if (JobBean.AUTO.equals(job.getAuto())) {
				job.setAuto(JobBean.MANUAL);
				rs = "closed";
				jobManager.removeJob(job);
			} else {
				job.setAuto(JobBean.AUTO);
				rs = "opened";
				jobManager.scheduleJob(job);
			}
		} catch (SchedulerException e) {
			return "error";
		}

		updateJob(job);
		return rs;
	}

	public void updateHistory(JobHistory history) {
		jobHistoryDao.update(history);
	}

	public JobHistory getHistory(Long historyId) {
		return jobHistoryDao.get(historyId);
	}

	public boolean manualRun(Integer jobId) {
		JobBean jobBean = getJob(jobId);
		try {
			if (JobBean.HIVE_JOB.equals(jobBean.getRunType())) {
				new Thread(new ManualHiveJob(this, jobId)).run();
			} else {
				new Thread(new ManualShellJob(this, jobId)).run();
			}
		} catch (Exception e) {
			return false;
		}
		return true;

	}

	public boolean resumeRun(Integer jobId) {
		try {
			jobManager.noWaitJob(jobId);
		} catch (SchedulerException e) {
			return false;
		}
		return true;
	}

	public List<JobBean> getAllAutoRunJobs() {
		return jobDao.getAutoRunJobs();
	}
}
