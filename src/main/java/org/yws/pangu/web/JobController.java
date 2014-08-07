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
import org.yws.pangu.domain.ResponseBean;
import org.yws.pangu.service.impl.JobServiceImpl;
import org.yws.pangu.utils.DateUtils;
import org.yws.pangu.utils.JobExecutionMemoryHelper;
import org.yws.pangu.web.webbean.JobHistoryListItemWebBean;
import org.yws.pangu.web.webbean.JobTreeNodeWebBean;
import org.yws.pangu.web.webbean.JobTreeWebBean;
import org.yws.pangu.web.webbean.LogStatusWebBean;
import org.yws.pangu.web.webbean.UpdateJobWebBean;

@RequestMapping(value = "/jobs")
@Controller
public class JobController {

	@Autowired
	private JobServiceImpl jobService;

	@RequestMapping(value = "addjob.do")
	public @ResponseBody ResponseBean addjob(String name, String type, boolean isParent,
			Integer groupId) {
		// TODO:
		String owner = "1";

		JobBean job = new JobBean();
		job.setAuto(JobBean.MANUAL);
		job.setGmtCreate(new Date());
		job.setGmtModified(new Date());
		job.setGroupId(groupId);
		job.setName("New Job");
		job.setOwner(owner);
		job.setRunType(JobBean.HIVE_JOB);
		job.setScheduleType(JobBean.RUN_BY_TIME);
		job.setCron("0 0 0 * * ?");
		Integer id = jobService.save(job);

		return new ResponseBean(true, id.toString());
	}

	@RequestMapping(value = "addgroup.do")
	public @ResponseBody ResponseBean addgroup(String name, Integer parentId) {
		// TODO:
		String owner = "1";

		JobGroup group = new JobGroup();
		group.setGmtCreate(new Date());
		group.setGmtModified(new Date());
		group.setName(name);
		group.setOwner(owner);
		group.setParent(parentId);
		Integer id = jobService.save(group);

		return new ResponseBean(true, id.toString());
	}

	@RequestMapping(value = "updategroupname.do")
	public @ResponseBody ResponseBean updategroupname(String name, Integer id) {
		// TODO:
		String owner = "1";

		jobService.updateGroupName(id, name);

		return new ResponseBean(true);
	}

	@RequestMapping(value = "deletegroup.do")
	public @ResponseBody ResponseBean deletegroup(Integer id) {
		// TODO:
		String owner = "1";

		JobGroup root = jobService.getRootGroup(owner);
		if(root.getId().equals(id)){
			return new ResponseBean(false, "该目录不允许删除");
		}
		
		int res = jobService.deleteGroup(id, owner);
		if (1 == res) {
			return new ResponseBean(true);
		} else if (0 == res) {
			return new ResponseBean(false, "该目录还有job存在,不能删除");
		} else {
			return new ResponseBean(false, "删除时候发生异常");
		}
	}
	
	@RequestMapping(value = "deletejob.do")
	public @ResponseBody ResponseBean deletejob(Integer id) {
		// TODO:
		String owner = "1";

		int res = jobService.deletejob(id, owner);
		if (1 == res) {
			return new ResponseBean(true);
		} else {
			return new ResponseBean(false, "删除失败");
		}
	}

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

			JobGroup g = jobService.getRootGroup(owner);
			if (g.getId().equals(id)) {
				for (JobGroup group : jobService.groupList(id)) {
					JobTreeNodeWebBean wb = new JobTreeNodeWebBean();
					wb.setId(group.getId().toString());
					wb.setFolder(true);
					wb.setName(group.getName());
					l.add(wb);
				}
			} else {
				for (JobBean job : jobService.jobList(id)) {
					JobTreeNodeWebBean wb = new JobTreeNodeWebBean();
					wb.setId(job.getId().toString());
					wb.setName(job.getName() + "[" + job.getId() + "]");
					l.add(wb);
				}
			}
		}

		return l;
	}

	@RequestMapping(value = "get.do")
	public @ResponseBody JobBean get(Integer id) {

		JobBean job = jobService.getJob(id);

		return job;
	}

	@RequestMapping(value = "update.do")
	public @ResponseBody boolean update(UpdateJobWebBean uJob) {

		JobBean job = jobService.getJob(uJob.getId());

		job.setGmtModified(new Date());
		job.setRunType(uJob.getRunType());
		job.setScheduleType(uJob.getScheduleType());
		if (JobBean.RUN_BY_TIME.equals(job.getScheduleType())) {
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

	@RequestMapping(value = "openclosejob.do")
	public @ResponseBody String openclosejob(Integer id) {
		return jobService.updateJobAutoStatus(id);
	}

	@RequestMapping(value = "history.do")
	public @ResponseBody Object history(Integer jobId) {
		List<JobHistoryListItemWebBean> list = new ArrayList<JobHistoryListItemWebBean>();
		for (JobHistory his : jobService.listJobHistory(jobId)) {
			JobHistoryListItemWebBean wb = new JobHistoryListItemWebBean();
			if (his.getEndTime() != null)
				wb.setEndTime(DateUtils.format(his.getEndTime().getTime(), "yyyy/MM/dd HH:mm:ss"));
			else
				wb.setEndTime("");
			if (his.getStartTime() != null)
				wb.setStartTime(DateUtils.format(his.getStartTime().getTime(),
						"yyyy/MM/dd HH:mm:ss"));
			wb.setId(his.getId().toString());
			wb.setStatus(his.getStatus());
			list.add(wb);
		}
		return list;
	}

	@RequestMapping(value = "gethistorylog.do")
	public @ResponseBody LogStatusWebBean gethistorylog(Long historyId) {
		LogStatusWebBean wb;

		if (JobExecutionMemoryHelper.jobLogMemoryHelper.get(historyId) != null) {
			String log = JobExecutionMemoryHelper.jobLogMemoryHelper.get(historyId).toString();
			String status = JobExecutionMemoryHelper.jobStatusMemoryHelper.get(historyId);

			wb = new LogStatusWebBean(status, log);
		} else {

			JobHistory history = jobService.getHistory(historyId);
			wb = new LogStatusWebBean(history.getStatus(), history.getLog());
		}

		return wb;

	}

	@RequestMapping(value = "manualrun.do")
	public @ResponseBody boolean manualrun(Integer jobId) {
		return jobService.manualRun(jobId);
	}

	@RequestMapping(value = "resumerun.do")
	public @ResponseBody boolean resumerun(Integer jobId) {
		return jobService.resumeRun(jobId);
	}
}