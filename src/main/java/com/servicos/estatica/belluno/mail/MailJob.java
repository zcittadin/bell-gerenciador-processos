package com.servicos.estatica.belluno.mail;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;

import com.servicos.estatica.belluno.model.Processo;

public class MailJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SchedulerContext schedulerContext = null;
		try {
			schedulerContext = context.getScheduler().getContext();
			Processo processo = (Processo) schedulerContext.get("processo");
			ProducaoMailService mailService = new ProducaoMailService();
			mailService.sendMailReport(processo);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
