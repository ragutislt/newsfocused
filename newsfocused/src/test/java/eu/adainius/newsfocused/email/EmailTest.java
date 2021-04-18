package eu.adainius.newsfocused.email;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import eu.adainius.newsfocused.headline.Headline;
import eu.adainius.newsfocused.headline.Headlines;
import eu.adainius.newsfocused.site.Sites;

public class EmailTest {
    @Test
    void acceptsTemplate() {
        String template = "src/test/resources/templates/email_template.ftl";
        Email email = new Email(template, null);

        assertEquals(template, email.template());
    }

    @Test
    void acceptsHeadlines() {
        String template = "src/test/resources/templates/email_template.ftl";
        Headline headline1 = new Headline();
        Headline headline2 = new Headline();

        Headlines headlines = Headlines.of(headline1, headline2);
        Email email = new Email(template, headlines);

        assertEquals(headlines, email.headlines());
    }

    @Test
    void includesFaviconLinks() {
        String template = "email_template.ftl";
        Headline headline1 = Headline.builder().title("news 1").htmlLink("<a href=\"www.mysite.com\\news\">news 1</a>")
                .urlLink("www.mysite.com\\news").date(LocalDate.now()).website(Sites.BBC).build();
        Headline headline2 = Headline.builder().title("news 2").htmlLink("<a href=\"www.mysite2.com\\news\">news 2</a>")
                .urlLink("www.mysite2.com\\news").date(LocalDate.now().minusDays(1)).website(Sites.LRT).build();

        Headlines headlines = Headlines.of(headline1, headline2);
        Email email = new Email(template, headlines);

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

        Headlines headlines = Headlines.of(headline1, headline2);
        Email email = new Email(template, headlines);

        System.out.println(email.body());

        assertTrue(email.body().contains("<html"));
        assertTrue(email.body().contains("</html>"));
        assertTrue(email.body().contains("<a href=\"www.mysite.com\\news\">news 1</a>"));
        assertTrue(email.body().contains("<a href=\"www.mysite2.com\\news\">news 2</a>"));
        assertTrue(email.body().contains("Here's your summary of the week"));
        assertTrue(email.body().contains(LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
    }

    @Test
    void cachesEmailBodyWhenBuilt() {
        String template = "email_template.ftl";
        Headline headline1 = Headline.builder().title("news 1").htmlLink("<a href=\"www.mysite.com\\news\">news 1</a>")
                .urlLink("www.mysite.com\\news").date(LocalDate.now()).website(Sites.BBC).build();
        Headline headline2 = Headline.builder().title("news 2").htmlLink("<a href=\"www.mysite2.com\\news\">news 2</a>")
                .urlLink("www.mysite2.com\\news").date(LocalDate.now().minusDays(1)).website(Sites.LRT).build();

        Headlines headlines = Mockito.spy(Headlines.of(headline1, headline2));
        Email email = new Email(template, headlines);

        // generate email
        email.body();
        email.body();
        email.body();
        email.body();
        Mockito.verify(headlines, Mockito.times(7)).from(any());
    }
}
