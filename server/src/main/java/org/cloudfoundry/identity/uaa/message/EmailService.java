package org.cloudfoundry.identity.uaa.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.UnsupportedEncodingException;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService implements MessageService {
    private final Log logger = LogFactory.getLog(getClass());

    private JavaMailSender mailSender;
    private final String senderEmail;
    private final String senderName;

    public EmailService(JavaMailSender mailSender, String senderEmail, String senderName) {
        this.mailSender = mailSender;
        this.senderEmail = senderEmail;
        this.senderName = senderName;
    }

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private Address[] getSenderAddresses() throws AddressException, UnsupportedEncodingException {
        return new Address[]{new InternetAddress(senderEmail, senderName)};
    }

    @Override
    public void sendMessage(String email, MessageType messageType, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.addFrom(getSenderAddresses());
            message.addRecipients(Message.RecipientType.TO, email);
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html");
        } catch (MessagingException e) {
            logger.error("Exception raised while sending message to " + email, e);
        } catch (UnsupportedEncodingException e) {
            logger.error("Exception raised while sending message to " + email, e);
        }

        mailSender.send(message);
    }
}
