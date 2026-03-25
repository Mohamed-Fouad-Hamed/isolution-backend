package com.alf.auth.email;

import com.alf.auth.config.EmailProviderConfig;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
public class EmailSender {

    @Autowired
    private EmailProviderConfig emailProviderConfig;
    private static final Properties PROPERTIES = new Properties();


    static {
        PROPERTIES.put("mail.smtp.host", "smtp.gmail.com");
        PROPERTIES.put("mail.smtp.port", "587");
        PROPERTIES.put("mail.smtp.auth", "true");
        PROPERTIES.put("mail.smtp.starttls.enable", "true");
    }

    public  void sendPlainTextEmail(String from, String to, String subject, List<String> messages, boolean debug) {

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication( emailProviderConfig.getUsername(), emailProviderConfig.getPassword());
            }
        };

        Session session = Session.getInstance(PROPERTIES, authenticator);
        session.setDebug(debug);

        try {

            // create a message with headers
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // create message body
            Multipart mp = new MimeMultipart();
            for (String message : messages) {
                MimeBodyPart mbp = new MimeBodyPart();
                mbp.setText(message, "us-ascii");
                mp.addBodyPart(mbp);
            }
            msg.setContent(mp);

            // send the message
            Transport.send(msg);

        } catch (MessagingException mex) {
            mex.printStackTrace();
            Exception ex = null;
            if ((ex = mex.getNextException()) != null) {
                ex.printStackTrace();
            }
        }
    }
}