package eu.adainius.newsfocused.headline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import eu.adainius.newsfocused.ApplicationException;

public class HeadlineTest {

    @Test
    public void has_date_title_website_htmlLink_urlLink_fields() {
        String title = "title";
        String urlLink = "urlLink";
        String htmlLink = "htmlLink";
        String website = "website";
        LocalDate date = LocalDate.now();

        Headline headline = Headline.builder().date(date).htmlLink(htmlLink).title(title).urlLink(urlLink)
                .website(website).build();

        assertEquals(title, headline.title());
        assertEquals(urlLink, headline.urlLink());
        assertEquals(htmlLink, headline.htmlLink());
        assertEquals(website, headline.website());
        assertEquals(date, headline.date());
    }

    @Test
    public void appendsProtocolToWebsite() {
        String title = "title";
        String urlLink = "urlLink";
        String htmlLink = "htmlLink";
        String website = "www.bbc.com";
        LocalDate date = LocalDate.now();

        Headline headline = Headline.builder().date(date).htmlLink(htmlLink).title(title).urlLink(urlLink)
                .website(website).build();

        assertTrue(headline.website().contains("http"));
    }

    @Test
    void headlines_are_equal_when_content_is_equal() {
        Headline headline1 = Headline.builder().date(LocalDate.parse("2023-09-17")).htmlLink("www.bbc.com")
                .title("title").urlLink("www.sdasdbbc.com").website("www.bbc.com").build();
        Headline headline2 = Headline.builder().date(LocalDate.parse("2023-09-17")).htmlLink("www.bbc.com")
                .title("title").urlLink("www.sdasdbbc.com").website("www.bbc.com").build();

        assertEquals(headline1, headline2);
    }

    @Test
    void headlines_are_not_equal_when_content_is_not_equal() {
        Headline headline1 = Headline.builder().date(LocalDate.parse("1999-09-17")).htmlLink("www.bbc.com")
                .title("title").urlLink("www.weqweqwe.com").website("www.bbc.com").build();
        Headline headline2 = Headline.builder().date(LocalDate.parse("2023-09-17")).htmlLink("www.bbc.com")
                .title("title").urlLink("www.sdasdbbc.com").website("www.bbc.com").build();

        assertNotEquals(headline1, headline2);
    }

    @ParameterizedTest
    @MethodSource("provideIncompleteBuilders")
    public void does_not_build_without_all_fields(Headline.HeadlineBuilder invalidBuilder) {
        assertThrows(ApplicationException.class, () -> {
            invalidBuilder.build();
        });
    }

    private static Stream<Arguments> provideIncompleteBuilders() {
        return Stream.of(Arguments.of(Headline.builder()), Arguments.of(Headline.builder().date(LocalDate.now())),
                Arguments.of(Headline.builder().date(LocalDate.now()).website("sss")),
                Arguments.of(Headline.builder().date(LocalDate.now()).website("sss").htmlLink("aaa")),
                Arguments.of(Headline.builder().date(LocalDate.now()).website("sss").htmlLink("aaa").title("qqqq")));
    }
}
