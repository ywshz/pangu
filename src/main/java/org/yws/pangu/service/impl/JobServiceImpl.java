package org.yws.pangu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yws.pangu.dao.mysql.MySqlJobDao;
import org.yws.pangu.dao.mysql.MySqlJobGroupDao;
import org.yws.pangu.domain.JobBean;
import org.yws.pangu.domain.JobGroup;

@Service
public class JobServiceImpl {

	@Autowired
	private MySqlJobGroupDao groupDao;
	@Autowired
	private MySqlJobDao jobDao;

	public List<JobBean> jobList(Integer groupId) {
		return jobDao.list(groupId);
	}

	public List<JobGroup> groupList(Integer parentId) {
		return groupDao.list(parentId);
	}

	public JobGroup getRootGroup(String owner) {
		return groupDao.getRootGroup(owner);
	}

	public JobBean getJob(Long id) {
		return jobDao.getJob(id);
	}

	public void updateJob(JobBean job) {
		jobDao.update(job);
	}
}
