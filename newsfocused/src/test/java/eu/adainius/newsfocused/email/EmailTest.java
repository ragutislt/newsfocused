package eu.adainius.newsfocused.email;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import eu.adainius.newsfocused.ApplicationException;
import eu.adainius.newsfocused.headline.Headline;
import eu.adainius.newsfocused.headline.Headlines;
import eu.adainius.newsfocused.site.Sites;

public class EmailTest {
    @Test
    void acceptsTemplate() {
        String address = "aaa@aaa.com";
        String template = "src/test/resources/templates/email_template.ftl";
        Email email = new Email(template, null, address);

        assertEquals(template, email.template());
    }

    @Test
    void acceptsEmailAddress() {
        String address = "aaa@aaa.com";
        String template = "src/test/resources/templates/email_template.ftl";
        Email email = new Email(template, null, address);

        assertEquals(address, email.address());
    }

    @Test
    void rejectsInvalidEmailAddress() {
        String address = "aaa";
        String template = "src/test/resources/templates/email_template.ftl";

        assertThrows(ApplicationException.class, () -> new Email(template, null, address));
    }

    @Test
    void acceptsHeadlines() {
        String template = "src/test/resources/templates/email_template.ftl";
        Headline headline1 = new Headline();
        Headline headline2 = new Headline();
        String address = "aaa@aaa.com";

        Headlines headlines = Headlines.of(headline1, headline2);
        Email email = new Email(template, headlines, address);

        assertEquals(headlines, email.headlines());
    }

    @Test
    void acceptsHeadlineDailyCount() {
        String template = "src/test/resources/templates/email_template.ftl";
        Headline headline1 = new Headline();
        Headline headline2 = new Headline();
        String address = "aaa@aaa.com";
        int headlineDailyCount = 2;

        Headlines headlines = Headlines.of(headline1, headline2);
        Email email = new Email(template, headlines, address, headlineDailyCount);

        assertEquals(headlineDailyCount, email.headlineDailyCount());
    }

    @Test
    void includesFaviconLinks() {
        String template = "email_template.ftl";
        Headline headline1 = Headline.builder().title("news 1").htmlLink("<a href=\"www.mysite.com\\news\">news 1</a>")
                .urlLink("www.mysite.com\\news").date(LocalDate.now()).website(Sites.BBC).build();
        Headline headline2 = Headline.builder().title("news 2").htmlLink("<a href=\"www.mysite2.com\\news\">news 2</a>")
                .urlLink("www.mysite2.com\\news").date(LocalDate.now().minusDays(1)).website(Sites.LRT).build();
        String address = "aaa@aaa.com";

        Headlines headlines = Headlines.of(headline1, headline2);
        Email email = new Email(template, headlines, address);

        System.out.println(email.body());

        assertTrue(email.body().contains("www.bbc.com/favicon.ico"));
        assertTrue(email.body().contains("www.lrt.lt/favicon.ico"));
    }

    @Test
    void producesEmailBody() {
        String template = "email_template.ftl";
        Headline headline1 = Headline.builder().title("news 1").htmlLink("<a href=\"www.mysite.com\\news\">news 1</a>")
                .urlLink("www.mysite.com\\news").date(LocalDate.now()).website(Sites.BBC).build();
        Headline headline2 = Headline.builder().title("news 2").htmlLink("<a href=\"www.mysite2.com\\news\">news 2</a>")
                .urlLink("www.mysite2.com\\news").date(LocalDate.now().minusDays(1)).website(Sites.LRT).build();
        String address = "aaa@aaa.com";

        Headlines headlines = Headlines.of(headline1, headline2);
        Email email = new Email(template, headlines, address);

        System.out.println(email.body());

        assertTrue(email.body().contains("<html"));
        assertTrue(email.body().contains("</html>"));
        assertTrue(email.body().contains(headline1.htmlLink()));
        assertTrue(email.body().contains(headline2.htmlLink()));
        assertTrue(email.body().contains("Here's your summary of the week"));
        assertTrue(
                email.body().contains(LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
    }

    @Test
    void filtersHeadlinesByCount() {
        String template = "email_template.ftl";
        Headline headline1 = Headline.builder().title("news 1").htmlLink("<a href=\"www.mysite.com\\news\">news 1</a>")
                .urlLink("www.mysite.com\\news").date(LocalDate.now()).website(Sites.BBC).build();
        Headline headline2 = Headline.builder().title("news 2").htmlLink("<a href=\"www.mysite2.com\\news\">news 2</a>")
                .urlLink("www.mysite2.com\\news").date(LocalDate.now().minusDays(1)).website(Sites.LRT).build();
        Headline headline3 = Headline.builder().title("news 3").htmlLink("<a href=\"www.mysite3.com\\news\">news 3</a>")
                .urlLink("www.mysite2.com\\news").date(LocalDate.now().minusDays(1)).website(Sites.LRT).build();
        String address = "aaa@aaa.com";

        Headlines headlines = Headlines.of(headline1, headline2, headline3);
        int headlineDailyCount = 1;
        Email email = new Email(template, headlines, address, headlineDailyCount);

        System.out.println(email.body());

        assertTrue(email.body().contains(headline1.htmlLink()));
        assertTrue(email.body().contains(headline2.htmlLink()));
        assertFalse(email.body().contains(headline3.htmlLink()));
    }

    @Test
    void cachesEmailBodyWhenBuilt() {
        String template = "email_template.ftl";
        Headline headline1 = Headline.builder().title("news 1").htmlLink("<a href=\"www.mysite.com\\news\">news 1</a>")
                .urlLink("www.mysite.com\\news").date(LocalDate.now()).website(Sites.BBC).build();
        Headline headline2 = Headline.builder().title("news 2").htmlLink("<a href=\"www.mysite2.com\\news\">news 2</a>")
                .urlLink("www.mysite2.com\\news").date(LocalDate.now().minusDays(1)).website(Sites.LRT).build();
        String address = "aaa@aaa.com";

        Headlines headlines = Mockito.spy(Headlines.of(headline1, headline2));
        Email email = new Email(template, headlines, address);

        // generate email
        email.body();
        email.body();
        email.body();
        email.body();
        Mockito.verify(headlines, Mockito.times(7)).from(any());
    }
}
