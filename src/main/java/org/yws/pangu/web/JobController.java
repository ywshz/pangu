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
import org.yws.pangu.domain.JobHistory;
import org.yws.pangu.service.impl.JobServiceImpl;
import org.yws.pangu.utils.DateUtils;
import org.yws.pangu.web.webbean.JobHistoryListItemWebBean;
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
	
	@RequestMapping(value = "history.do")
	public @ResponseBody Object history(Long jobId) {
		List<JobHistoryListItemWebBean> list = new ArrayList<JobHistoryListItemWebBean>();
		for(JobHistory his : jobService.listJobHistory(jobId)){
			JobHistoryListItemWebBean wb = new JobHistoryListItemWebBean();
			if(his.getEndTime()!=null)
				wb.setEndTime(DateUtils.format(his.getEndTime().getTime(), "yyyy/MM/dd HH:mm:ss"));
			if(his.getStartTime()!=null)
				wb.setStartTime(DateUtils.format(his.getStartTime().getTime(), "yyyy/MM/dd HH:mm:ss"));
			wb.setId(his.getId().toString());
			wb.setStatus(his.getStatus());
			list.add(wb);
		}
		return list;
	}
}