package eu.adainius.newsfocused.email;

import java.util.Properties;

import eu.adainius.newsfocused.ApplicationException;
import eu.adainius.newsfocused.config.EmailConfiguration;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailProvider {
    public static void sendEmail(Email email) {
        Properties props = EmailConfiguration.emailProtocolProperties();

        // create the Session object
        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(props.getProperty("email.server.login"), props.getProperty("email.server.password"));
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EmailConfiguration.from()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.address()));
            message.setSubject("Your news of the week");
            message.setContent(email.body(), "text/html; charset=UTF-8");
            Transport.send(message);
            log.info("Email Message Sent Successfully");
        } catch (MessagingException e) {
            throw new ApplicationException(e);
        }
    }
}
