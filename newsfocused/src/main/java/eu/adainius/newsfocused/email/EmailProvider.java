package eu.adainius.newsfocused.email;

import java.util.Properties;

import eu.adainius.newsfocused.ApplicationException;
import eu.adainius.newsfocused.EmailConfiguration;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailProvider {
    public static void sendEmail(Email email) {
        // TODO externalize properties to a file

        // mailcatcher - smtp://127.0.0.1:1025
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "127.0.0.1");
        props.put("mail.smtp.port", "1025");

        // create the Session object
        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("aa", "dd");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EmailConfiguration.from()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.address()));
            message.setSubject("Here's your news of the week");
            message.setText(email.body());
            Transport.send(message);
            System.out.println("Email Message Sent Successfully");
        } catch (MessagingException e) {
            throw new ApplicationException(e);
        }
    }
}
