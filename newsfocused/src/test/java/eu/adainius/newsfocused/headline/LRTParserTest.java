package eu.adainius.newsfocused.headline;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

public class LRTParserTest {
    @Test
    public void parses_lrt_headlines() throws IOException {
        List<String> headlinesExpected = List.of(
                "<a href=\"https://www.lrt.lt/naujienos/lietuvoje/2/1369517/profesorius-janeliunas-vertybiniai-klausimai-ateityje-gali-sukelti-didelius-nesutarimus-koalicijoje\" title=\"Profesorius Janeliūnas: vertybiniai klausimai ateityje gali sukelti didelius nesutarimus koalicijoje\">Profesorius Janeliūnas: vertybiniai klausimai ateityje gali sukelti didelius nesutarimus koalicijoje</a>");

        String lrtContentLocation = "src/test/resources/lrt.html";
        String lrtContent = Files.readString(Paths.get(lrtContentLocation));

        HeadlineParser lrtParser = new LRTParser();

        List<Headline> headlines = lrtParser.parseFrom(lrtContent);

        assertTrue(headlines.stream().anyMatch(headline -> headlinesExpected.get(0).equals(headline.htmlLink())));
    }

    @Test
    public void parses_lrt_headlines_ignores_horizontal_ones() throws IOException {
        List<String> headlinesExpected = List.of(
                "Hemingway`us turėjo 4 žmonas, mėgo gaidžių peštynes, barus ir troško, kad jį vertintų pagal kūrybą, o ne paklydimus");

        String lrtContentLocation = "src/test/resources/lrt.html";
        String lrtContent = Files.readString(Paths.get(lrtContentLocation));

        HeadlineParser lrtParser = new LRTParser();

        List<Headline> headlines = lrtParser.parseFrom(lrtContent);

        assertFalse(headlines.stream().anyMatch(headline -> headlinesExpected.get(0).equals(headline.title())));
    }

    @Test
    public void constructs_new_html_based_on_link_and_title() throws IOException {
        List<String> headlinesExpected = List.of(
                "<a href=\"https://www.lrt.lt/naujienos/sveikata/682/1411516/mediku-sajudzio-vadove-remia-siauliu-ligonines-vadova-prie-sienos-ar-zinote-kiek-darbuotoju-paliko-sirdies-ir-kraujagysliu-centra\" title=\"Medikų sąjūdžio vadovė remia Šiaulių ligoninės vadovą prie sienos: ar žinote, kiek darbuotojų paliko Širdies ir kraujagyslių centrą?\">Medikų sąjūdžio vadovė remia Šiaulių ligoninės vadovą prie sienos: ar žinote, kiek darbuotojų paliko Širdies ir kraujagyslių centrą?</a>");

        String content = "<h3 class=\"news__title\"><a someAttribute=\"aaaa\" href=\"/naujienos/sveikata/682/1411516/mediku-sajudzio-vadove-remia-siauliu-ligonines-vadova-prie-sienos-ar-zinote-kiek-darbuotoju-paliko-sirdies-ir-kraujagysliu-centra\" title=\"Medikų sąjūdžio vadovė remia Šiaulių ligoninės vadovą prie sienos: ar žinote, kiek darbuotojų paliko Širdies ir kraujagyslių centrą?\">Medikų sąjūdžio vadovė remia Šiaulių ligoninės vadovą prie sienos: ar žinote, kiek darbuotojų paliko Širdies ir kraujagyslių centrą?</a></h3>";

        HeadlineParser lrtParser = new LRTParser();

        List<Headline> headlines = lrtParser.parseFrom(content);

        assertTrue(headlines.stream().anyMatch(headline -> headlinesExpected.get(0).equals(headline.htmlLink())));
    }

    @Test
    public void parses_title() throws IOException {
        List<String> headlineTitlesExpected = List.of("Profesorius Janeliūnas: vertybiniai klausimai ateityje gali sukelti didelius nesutarimus koalicijoje");

        String lrtContentLocation = "src/test/resources/lrt.html";
        String lrtContent = Files.readString(Paths.get(lrtContentLocation));

        HeadlineParser lrtParser = new LRTParser();

        List<Headline> headlines = lrtParser.parseFrom(lrtContent);

        assertTrue(headlines.stream().anyMatch(headline -> headlineTitlesExpected.get(0).equals(headline.title())));
    }

    @Test
    public void parses_urlLink() throws IOException {
        List<String> headlineUrlsExpected = List.of("https://www.lrt.lt/naujienos/lietuvoje/2/1369517/profesorius-janeliunas-vertybiniai-klausimai-ateityje-gali-sukelti-didelius-nesutarimus-koalicijoje");

        String lrtContentLocation = "src/test/resources/lrt.html";
        String lrtContent = Files.readString(Paths.get(lrtContentLocation));

        HeadlineParser lrtParser = new LRTParser();

        List<Headline> headlines = lrtParser.parseFrom(lrtContent);

        assertTrue(headlines.stream().anyMatch(headline -> headlineUrlsExpected.get(0).equals(headline.urlLink())));
    }
}
