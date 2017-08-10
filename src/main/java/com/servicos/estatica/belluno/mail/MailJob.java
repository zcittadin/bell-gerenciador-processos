package com.servicos.estatica.belluno.mail;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;

public class MailJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SchedulerContext schedulerContext = null;
		try {
			schedulerContext = context.getScheduler().getContext();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		Integer contagem = (Integer) schedulerContext.get("contagem");
		ProducaoMailService mailService = new ProducaoMailService();
		mailService.sendMailReport(contagem);
		// System.out.println("Job de envio de e-mail... " + contagem);
	}

}
