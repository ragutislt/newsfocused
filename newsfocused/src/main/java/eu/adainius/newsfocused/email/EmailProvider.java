package eu.adainius.newsfocused.email;

import eu.adainius.newsfocused.ApplicationException;
import jakarta.mail.MessagingException;
import jakarta.mail.Transport;

public class EmailProvider {
    public static void sendEmail(Email email) {
        try {
            Transport.send(null);
        } catch (MessagingException e) {
           throw new ApplicationException(e);
        }
    }
}
