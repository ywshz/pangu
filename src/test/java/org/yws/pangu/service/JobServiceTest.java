package org.yws.pangu.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.yws.pangu.domain.JobBean;
import org.yws.pangu.domain.JobGroup;
import org.yws.pangu.domain.JobHistory;
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
	public void hisList(){
		for (JobHistory g : jobService.listJobHistory((1))) {
			System.out.println(g.getStartTime());
		}
	}
	
	@Test
	public void hisList1(){
		int[] rs=jobService.getSuccessFailedJobsByDate(-3);
		System.out.println(rs[0]);
		System.out.println(rs[1]);
	}
}
