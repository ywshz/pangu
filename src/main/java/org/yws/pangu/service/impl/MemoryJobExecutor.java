package org.yws.pangu.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yws.pangu.service.Job;
import org.yws.pangu.service.JobExecutor;

public class MemoryJobExecutor implements JobExecutor{
	public static Map<String,String> logMap = new HashMap<String,String>();

	public List<Job> jobList = new ArrayList<Job>();
	
	@Override
	public boolean addJob(Job job) {
		jobList.add(job);
		return true;
	}
	
	
}
