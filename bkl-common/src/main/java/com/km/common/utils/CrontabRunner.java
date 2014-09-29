package com.km.common.utils;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronExpression;

public class CrontabRunner extends Thread {
	private static int id = 1;
	private CronExpression cronExpression;
	private Runnable run; 
	public CrontabRunner(String expression, Runnable run) throws ParseException {
		super("CrontabRunner" + (id++));
		this.cronExpression = new CronExpression(expression);
		this.run = run;
	}
	
	public void run() {
		Date nextRunDate = new Date();
		while (true) {
			nextRunDate = cronExpression.getNextValidTimeAfter(nextRunDate);
			System.out.println(this.getName() + " plan to run at " + TimeUtil.fromUnixTime(nextRunDate.getTime()/1000));
			long waitIntervalInMills = nextRunDate.getTime() - new Date().getTime();
			try {
				Thread.sleep(waitIntervalInMills);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				this.run.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//System.out.println(TimeUtil.fromUnixTime(nextRunDate.getTime()/1000));
		}
	}
	
}
