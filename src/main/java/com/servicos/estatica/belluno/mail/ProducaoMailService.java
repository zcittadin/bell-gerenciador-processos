package com.servicos.estatica.belluno.mail;

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
			builder.append("<p>Segue as leituras para o processo " + processo.getIdentificador() + "</p>");
			builder.append("<p>Foram produzidas " + processo + " sacas até a última hora.</p>");
			builder.append("<p>Início dos registros: " + processo.getDhInicial().toString() + "</p>");
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

}
