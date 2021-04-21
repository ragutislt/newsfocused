package eu.adainius.newsfocused.headline;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

public class BBCParserTest {
    @Test
    public void parses_bbc_headlines() throws IOException {
        List<String> headlinesExpected = List.of(
                "<a class=\"media__link\" href=\"http://www.bbc.com/news/world-asia-56395085\" rev=\"hero1|headline\"> Bloody day in Myanmar's main city sees 14 killed </a>",
                "<a class=\"media__link\" href=\"http://www.bbc.com/news/world-asia-56391445\" rev=\"hero3|headline\"> North Korea 'not responding' to US contact efforts </a>");

        String bbcContentLocation = "src/test/resources/bbc.html";
        String bbcContent = Files.readString(Paths.get(bbcContentLocation));

        HeadlineParser bbcParser = new BBCParser();

        List<Headline> headlines = bbcParser.parseFrom(bbcContent);

        assertTrue(headlines.stream().anyMatch(headline -> headlinesExpected.get(0).equals(headline.htmlLink())));
        assertTrue(headlines.stream().anyMatch(headline -> headlinesExpected.get(1).equals(headline.htmlLink())));
    }

    @Test
    public void parses_title() throws IOException {
        List<String> headlineTitlesExpected = List.of("Bloody day in Myanmar's main city sees 14 killed",
                "North Korea 'not responding' to US contact efforts");

        String bbcContentLocation = "src/test/resources/bbc.html";
        String bbcContent = Files.readString(Paths.get(bbcContentLocation));

        HeadlineParser bbcParser = new BBCParser();

        List<Headline> headlines = bbcParser.parseFrom(bbcContent);

        assertTrue(headlines.stream().anyMatch(headline -> headlineTitlesExpected.get(0).equals(headline.title())));
        assertTrue(headlines.stream().anyMatch(headline -> headlineTitlesExpected.get(1).equals(headline.title())));
    }

    @Test
    public void parses_urlLink() throws IOException {
        List<String> headlineUrlsExpected = List.of("http://www.bbc.com/news/world-asia-56395085",
                "http://www.bbc.com/news/world-asia-56391445");

        String bbcContentLocation = "src/test/resources/bbc.html";
        String bbcContent = Files.readString(Paths.get(bbcContentLocation));

        HeadlineParser bbcParser = new BBCParser();

        List<Headline> headlines = bbcParser.parseFrom(bbcContent);

        assertTrue(headlines.stream().anyMatch(headline -> headlineUrlsExpected.get(0).equals(headline.urlLink())));
        assertTrue(headlines.stream().anyMatch(headline -> headlineUrlsExpected.get(1).equals(headline.urlLink())));
    }
    
    @Test
    public void includes_site_address_and_protocol_in_html_link() throws IOException {
        List<String> headlineUrlsExpectedInHtml = List.of("http://www.bbc.com/news/world-asia-56395085",
                "http://www.bbc.com/news/world-asia-56391445");

        String bbcContentLocation = "src/test/resources/bbc.html";
        String bbcContent = Files.readString(Paths.get(bbcContentLocation));

        HeadlineParser bbcParser = new BBCParser();

        List<Headline> headlines = bbcParser.parseFrom(bbcContent);

        assertTrue(headlines.stream().anyMatch(headline -> headline.htmlLink().contains(headlineUrlsExpectedInHtml.get(0))));
        assertTrue(headlines.stream().anyMatch(headline -> headline.htmlLink().contains(headlineUrlsExpectedInHtml.get(1))));

        assertTrue(headlines.stream().allMatch(headline -> headline.htmlLink().contains("http") && headline.htmlLink().contains("://")));
    }
}
