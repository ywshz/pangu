package org.yws.pangu.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yws.pangu.domain.JobBean;
import org.yws.pangu.domain.JobGroup;
import org.yws.pangu.service.impl.JobServiceImpl;
import org.yws.pangu.web.webbean.JobTreeNodeWebBean;
import org.yws.pangu.web.webbean.JobTreeWebBean;
import org.yws.pangu.web.webbean.UpdateJobWebBean;

@RequestMapping(value = "/jobs")
@Controller
public class JobController {

	@Autowired
	private JobServiceImpl jobService;;

	@RequestMapping(value = "list.do")
	public @ResponseBody Object list(Integer id) {
		List<JobTreeNodeWebBean> l = new ArrayList<JobTreeNodeWebBean>();

		// TODO:
		String owner = "1";

		if (id == null) {
			JobGroup g = jobService.getRootGroup(owner);
			JobTreeWebBean tree = new JobTreeWebBean();
			tree.setId(g.getId().toString());
			tree.setName(g.getName());
			tree.setFolder(true);

			for (JobGroup group : jobService.groupList(g.getId())) {
				JobTreeNodeWebBean wb = new JobTreeNodeWebBean();
				wb.setId(group.getId().toString());
				wb.setFolder(true);
				wb.setName(group.getName());
				l.add(wb);
			}

			tree.setChildren(l);
			return tree;
		} else {

			for (JobBean job : jobService.jobList(id)) {
				JobTreeNodeWebBean wb = new JobTreeNodeWebBean();
				wb.setId(job.getId().toString());
				wb.setName(job.getName() + "[" + job.getId() + "]");
				l.add(wb);
			}
		}

		return l;
	}

	@RequestMapping(value = "get.do")
	public @ResponseBody JobBean get(Long id) {

		JobBean job = jobService.getJob(id);

		return job;
	}

	@RequestMapping(value = "update.do")
	public @ResponseBody boolean update(UpdateJobWebBean uJob) {

		JobBean job = jobService.getJob(uJob.getId());

		job.setGmtModified(new Date());
		job.setRunType(uJob.getRunType());
		job.setScheduleType(uJob.getScheduleType());
		if ("1".equals(job.getScheduleType())) {
			job.setCron(uJob.getCron());
		} else {
			job.setDependencies(uJob.getDependencies());
		}
		job.setScript(uJob.getScript());
		job.setName(uJob.getName());

		try {
			jobService.updateJob(job);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}