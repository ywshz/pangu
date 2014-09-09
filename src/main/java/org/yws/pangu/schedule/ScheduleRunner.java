package org.yws.pangu.schedule;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yws.pangu.utils.DateRender;
@Deprecated
public class ScheduleRunner {
	private static Logger logger = LoggerFactory.getLogger(ScheduleRunner.class);
	
	public static void main(String[] args) throws SchedulerException, IOException {
		Properties prop = new Properties();
		prop.load(ScheduleRunner.class.getResourceAsStream("/jobs.properties"));

		String jobIds = prop.getProperty("jobs.id");
		if (jobIds == null) {
			System.out.println("jobs.id not found");
			logger.error("jobs.id not found");
			return;
		}

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		String[] jobIdArray = jobIds.split(",");
		for (String jobId : jobIdArray) {
			String name = prop.getProperty("job." + jobId + ".name");
			String type = prop.getProperty("job." + jobId + ".type");
			String cron = prop.getProperty("job." + jobId + ".cron");
			String script = prop.getProperty("job." + jobId + ".script");

			String jobName = "job.name." + jobId;
			String groupName = "group.name." + jobId;

			String triggerName = "trigger.name." + jobId;
			String triggerGroup = "trigger.groupname." + jobId;
			
			if(!validateScript(script)){
				logger.error("JobId input Script file format error, please check!");
				return ;
			}
			
			if ("hive".equals(type)) {

				JobDetail job = JobBuilder.newJob(RunHiveJob.class)
						.withIdentity(jobName, groupName).build();
				job.getJobDataMap().put("script-path", script);

				CronTrigger trigger = newTrigger().withIdentity(triggerName, triggerGroup)
						.withSchedule(cronSchedule(cron)).forJob(jobName, groupName).build();

				sched.scheduleJob(job, trigger);
			}else if("shell".equals(type)) {
				JobDetail job = JobBuilder.newJob(RunShellJob.class)
						.withIdentity(jobName, groupName).build();
				job.getJobDataMap().put("script-path", script);

				CronTrigger trigger = newTrigger().withIdentity(triggerName, triggerGroup)
						.withSchedule(cronSchedule(cron)).forJob(jobName, groupName).build();

				sched.scheduleJob(job, trigger);
			}
		}

		// Trigger trigger = newTrigger().withIdentity(groupName, groupName)
		// .forJob(groupName, groupName).build();

		sched.start();
	}

	private static boolean validateScript(String path) {
		try {
			String script = readFile(path);
			script = DateRender.render(script);
			logger.info("Rendered script:\n"+script);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private static String readFile(String path) throws IOException {
		File file = new File(path);
		FileReader fr = new FileReader(file);
		char[] chars = new char[(int) file.length()];
		fr.read(chars);
		fr.close();
		return String.valueOf(chars);
	}

}
