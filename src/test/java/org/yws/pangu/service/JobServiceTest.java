package org.yws.pangu.service;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.yws.pangu.domain.JobBean;
import org.yws.pangu.domain.JobGroup;
import org.yws.pangu.domain.JobHistory;
import org.yws.pangu.enums.ETimeOrderType;
import org.yws.pangu.service.impl.JobServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class JobServiceTest {
	@Autowired
	JobServiceImpl jobService;

	@Test
	public void jobList() {

		for (JobBean g : jobService.jobList(2)) {
			System.out.println(g.getName());
		}
	}

	@Test
	public void groupList() {
		for (JobGroup g : jobService.groupList(1)) {
			System.out.println(g.getName());
		}
	}

	@Test
	public void hisList() {
		for (JobHistory g : jobService.listJobHistory((1))) {
			System.out.println(g.getStartTime());
		}
	}
	
	@Test
	public void jobCostTime() {
		for(String s : jobService.jobCostTimeInNDays(7, 100)){
			System.out.println(s+" s");
			
		}
	}

	@Test
	public void hisList1() {
		// int[] rs=jobService.getSuccessFailedJobsByDate(-3);
		// System.out.println(rs[0]);
		// System.out.println(rs[1]);
		Map<String, int[]> m = jobService.getSuccessFailedJobsInNDays(5);

		StringBuilder sb_s = new StringBuilder("[");
		StringBuilder sb_f = new StringBuilder("[");

		int size = m.size();
		Set<String> s = new TreeSet<String>(m.keySet());
		Iterator<String> it = s.iterator();
		while (it.hasNext()) {
			String key = it.next();
			int[] sf = m.get(key);

			sb_s.append("['" + key + "','" + m.get(key)[0] + "']");
			sb_f.append("['" + key + "','" + m.get(key)[1] + "']");

			if (it.hasNext()) {
				sb_s.append(",");
				sb_f.append(",");
			}
		}

		sb_s.append("]");
		sb_f.append("]");

		System.out.println(sb_s.toString());
		System.out.println(sb_f.toString());
	}
}
