package com.servicos.estatica.belluno.mail;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
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

public class ProducaoMailService {

	public void sendMailReport(Processo processo) {
		Properties props = new Properties();

		// Parâmetros de conexão com servidor Gmail
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("zct.automacao@gmail.com", "engenheiro31");
			}
		});

		session.setDebug(true);

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("zct.automacao@gmail.com"));
			message.setSubject("Relatório de parcial");

			Address[] toUser = InternetAddress.parse("z_cittadin@hotmail.com");
			// Address[] toUser = InternetAddress.parse(
			// "z_cittadin@hotmail.com, eduardo@estatica-metrologia.com.br,
			// cesar@lsy.com.br, jaison@colatech.com.br");
			// .parse("seuamigo@gmail.com, seucolega@hotmail.com,
			// seuparente@yahoo.com.br");
			message.setRecipients(Message.RecipientType.TO, toUser);

			BodyPart messageBodyPart = new MimeBodyPart();
			StringBuilder builder = new StringBuilder();
			builder.append("<style>table, th, td {border: 1px solid black;border-collapse: collapse;}");
			builder.append("th, td {padding: 5px;}");
			builder.append("th {text-align: left;}</style>");
			builder.append("<h2>Segue as leituras para o processo " + processo.getIdentificador() + "</h2>");
			builder.append("<p>Início dos registros: "
					+ new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(processo.getDhInicial()) + "</p>");
			List<Leitura> lista = processo.getLeituras();
			if (lista != null || !lista.isEmpty()) {
				builder.append("<table style=\"width:100%\"><tr><th>Horário</th><th>Temperatura</th></tr>");
				for (Leitura leitura : lista) {
					builder.append("<tr><td>" + new SimpleDateFormat("HH:mm:ss").format(leitura.getDtProc()) + "</td>");
					builder.append("<td>" + leitura.getTemp() + "</td></tr>");
				}
				builder.append("</table>");
			}
			builder.append("<br>");
			makeFooter(builder);
			String msg = builder.toString();
			messageBodyPart.setText(msg);
			messageBodyPart.setContent(msg, "text/html; charset=utf-8");

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(messageBodyPart);
			message.setContent(mp);

			/** Método para enviar a mensagem criada */
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
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

}
