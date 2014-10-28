package com.zkr.server;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 定时执行任务 1、循环查找 RMC_CMDINFO 中要执行的任务 2、定时执行查询机器性能，网站和数据库是否可用等信息
 * 
 * @author root
 * 
 */
public class Server {

	public void run() throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		Date startTime = DateBuilder.nextGivenSecondDate(new Date(), 15);

		System.out.println(sdf.format(new Date()));
		JobDetail job = JobBuilder.newJob(QuartzJob.class)
				.withIdentity("job1", "group1").build();

		// SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
		// .withIdentity("trigger1", "group1").startAt(startTime).
		// withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever()).
		// build();

		CronTrigger trigger = (CronTrigger) TriggerBuilder
				.newTrigger()
				.withIdentity("trigger1", "group1")
				.withSchedule(
						CronScheduleBuilder.cronSchedule("0/20 * * * * ?"))
				.build();

		Date ft = sched.scheduleJob(job, trigger);
		sched.start();
		System.out.println(job.getKey() + " will run at : " + sdf.format(ft)
				+ " and repeat " +
				"  base on expression " + trigger.getCronExpression()
				
				);

//				trigger.getRepeatCount() + " times , every "
//				+ (trigger.getRepeatInterval() / 1000L) + " seconds");

		boolean exit = false;
		while(true){
			Thread.sleep(100000);
			if(exit) break;
		}

		sched.shutdown(true);
		SchedulerMetaData metaData = sched.getMetaData();

		System.out.println("Executed " + metaData.getNumberOfJobsExecuted()
				+ " jobs.");

	}

	public static void main(String[] args) {
		try {
			new Server().run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
