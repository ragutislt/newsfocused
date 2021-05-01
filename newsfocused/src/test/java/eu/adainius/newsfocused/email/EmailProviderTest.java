package eu.adainius.newsfocused.email;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import eu.adainius.newsfocused.headline.BBCParser;
import eu.adainius.newsfocused.headline.HeadlineParser;
import eu.adainius.newsfocused.headline.Headlines;
import jakarta.mail.Message;
import jakarta.mail.Transport;

public class EmailProviderTest {
    @Test
    void uses_java_mail() throws Exception {
        String emailAddress = "sample@email.com";
        String template = "email_template.ftl";

        String bbcContentLocation = "src/test/resources/bbc.html";
        String bbcContent = Files.readString(Paths.get(bbcContentLocation));

        HeadlineParser bbcParser = new BBCParser();
        Headlines headlines = Headlines.of(bbcParser.parseFrom(bbcContent));

        Email email = new Email(template, headlines, emailAddress);

        // verify Transport.send is executed
        try (MockedStatic<Transport> mockedTransport = mockStatic(Transport.class)) {
            EmailProvider.sendEmail(email);
            ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
            mockedTransport.verify(() -> Transport.send(messageCaptor.capture()));

            Message messageSent = messageCaptor.getValue();
            String emailSentBody = messageSent.getContent().toString();

            assertTrue(emailSentBody.equals(email.body()));
        }
    }
}
