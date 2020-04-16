package tfg.licensoft.tools;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderTool {
	

	@Value("${spring.mail.host}")
	private String host;
	@Value("${spring.mail.port}")
	private int port;
	@Value("${spring.mail.username}")
	private String username;
	@Value("${spring.mail.password}")
	private String password;
	
	public void sendMsgEmail(String subject,String msg, String email) {
		try {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setHost(this.host);
			mailSender.setPort(this.port);
			mailSender.setUsername(this.username);
			mailSender.setPassword(this.password);
			mailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", "true");
			
			//Create mail instance
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom(this.username);
			mailMessage.setTo(email);
			mailMessage.setSubject(subject);
			mailMessage.setText(msg);
			
			mailSender.send(mailMessage);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendHtmlEmail(String subject,String message, String email) {
		try {
			  Properties properties = new Properties();
		        properties.put("mail.smtp.host", host);
		        properties.put("mail.smtp.port", port);
		        properties.put("mail.smtp.auth", "true");
		        properties.put("mail.smtp.starttls.enable", "true");
		 
		        // creates a new session with an authenticator
		        Authenticator auth = new Authenticator() {
		            public PasswordAuthentication getPasswordAuthentication() {
		                return new PasswordAuthentication(username, password);
		            }
		        };
		 
		        Session session = Session.getInstance(properties, auth);
		 
		        // creates a new e-mail message
		        Message msg = new MimeMessage(session);
		 
		        msg.setFrom(new InternetAddress(username));
		        InternetAddress[] toAddresses = { new InternetAddress(email) };
		        msg.setRecipients(Message.RecipientType.TO, toAddresses);
		        msg.setSubject(subject);
		        msg.setSentDate(new Date());
		        // set plain text message		        
		        msg.setContent(message, "text/html");
		 
		        // sends the e-mail
		        Transport.send(msg);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
