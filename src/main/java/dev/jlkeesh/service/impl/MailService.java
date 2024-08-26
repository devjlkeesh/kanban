package dev.jlkeesh.service.impl;

import lombok.SneakyThrows;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class MailService {
    private static final String username = "92cab89078eaee";
    private static final String password = "8a60bb719daa7e";

    @SneakyThrows
    public void sendOtp(String code, String receiver) {
        var properties = getProperties();
        var session = getSession(properties);
        var message = new MimeMessage(session);
        message.setFrom(new InternetAddress("kanban.uz"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
        message.setSubject("This is Subject For Test Message");
        var multipart = new MimeMultipart();
        var contentMessage = new MimeBodyPart();
        String body = """
                <div>
                <h1 style="color:red;">Password ni ham esdan chiqaradimi ee kal***</h1>
                <h1 style="color:green; font-size: 60px; text-align:center">%s</h1>
                </div>
                """.formatted(code);
        contentMessage.setContent(body, "text/html");
        multipart.addBodyPart(contentMessage);
        message.setContent(multipart);
        Transport.send(message);
        System.out.println("Message Sent Successfully");
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "sandbox.smtp.mailtrap.io");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        return properties;
    }


    private static Session getSession(Properties properties) {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }
}
