package com.servicos.estatica.belluno.mail;

import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.servicos.estatica.belluno.model.Leitura;
import com.servicos.estatica.belluno.model.Processo;

public class MailSender {

	private static final String SMTP_HOST_NAME = "smtp.sendgrid.net";
	private static final String SMTP_AUTH_USER = "zcittadin";
	private static final String SMTP_AUTH_PWD = "lufter0312";

	public void sendMail(Processo processo) throws Exception {
		Properties properties = new Properties();
		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.host", SMTP_HOST_NAME);
		properties.put("mail.smtp.port", 587);
		properties.put("mail.smtp.auth", "true");

		Authenticator auth = new SMTPAuthenticator();
		Session mailSession = Session.getDefaultInstance(properties, auth);

		MimeMessage message = new MimeMessage(mailSession);
		Multipart multipart = new MimeMultipart("alternative");
		StringBuilder builder = new StringBuilder();
		builder.append("<style>table, th, td {border: 1px solid black;border-collapse: collapse;}");
		builder.append("th, td {padding: 5px;}");
		builder.append("th {text-align: left;}</style>");
		builder.append("<h3>A seguir são apresentados os registros de temperatura para o processo: "
				+ processo.getIdentificador() + "</h3>");
		builder.append("<p>Início dos registros: "
				+ new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(processo.getDhInicial()) + "</p>");
		if (processo.getLeituras() != null || !processo.getLeituras().isEmpty()) {
			builder.append("<table style=\"width:100%\"><tr><th>Horário</th><th>Temperatura</th></tr>");
			for (Leitura leitura : processo.getLeituras()) {
				builder.append("<tr><td>" + new SimpleDateFormat("HH:mm:ss").format(leitura.getDtProc()) + "</td>");
				builder.append("<td>" + leitura.getTemp() + "</td></tr>");
			}
			builder.append("</table>");
		}
		builder.append("<br>");
		makeFooter(builder);
		String msg = builder.toString();

		BodyPart part2 = new MimeBodyPart();
		part2.setContent(msg, "text/html; charset=utf-8");
		multipart.addBodyPart(part2);
		message.setFrom(new InternetAddress("zct.automacao@gmail.com"));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress("z_cittadin@hotmail.com"));
		message.setSubject("Produção de coque: relatório parcial");
		message.setContent(multipart);

		Transport transport = mailSession.getTransport();
		transport.connect();
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
		System.out.println("Email enviado com sucesso.");
	}

	private void makeFooter(StringBuilder builder) {
		builder.append("<table width=\"351\" cellspacing=\"0\" cellpadding=\"0\">");
		builder.append("<tr>");
		builder.append(
				"<td style=\"text-align:left;padding-bottom:10px\"><img style=\"border:none\" src=\"https://s1g.s3.amazonaws.com/e8a1c0bcf0416ff133189ac8cb69d534.jpg\"></td> ");
		builder.append("</tr>");
		builder.append("<tr>");
		builder.append(
				"<td style=\"vertical-align: top; text-align:left;color:#000000;font-size:12px;font-family:helvetica, arial; text-align:left\">");
		builder.append(
				"<span style=\"margin-right:5px;color:#000000;font-size:15px;font-family:helvetica, arial\">Carbon&#xED;fera Belluno Ltda.</span>");
		builder.append("<br><br> Rod. SC 445, Km 05 - s/n, Crici&uacute;ma - SC, CEP 88810-300<br><br>");
		builder.append("</td></tr>");
		builder.append("</table>");
		builder.append(
				"<p style=\"text-align:left;color:#aaaaaa;font-size:10px;font-family:helvetica, arial\">E-mail enviado automaticamente. Favor não responder.</p>");
		builder.append("<br><br>");
	}

	private class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}
}
