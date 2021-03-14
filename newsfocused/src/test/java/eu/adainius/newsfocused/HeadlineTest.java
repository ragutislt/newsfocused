package eu.adainius.newsfocused;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
