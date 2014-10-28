package com.zkr.server;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.zkr.control.Controller;

/**
 * Quartz job
 * @author lihongchen
 *
 */
public class QuartzJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		
		System.out.println("  ``````````````111111111111111 ``````````````");
		Controller ct = new Controller();
		ct.execTasks();
		
	}

	
	
	
	
}
