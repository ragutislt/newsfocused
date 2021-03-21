package eu.adainius.newsfocused.headline;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

public class HeadlinesTest {

    @Test
    public void constructs_from_a_list_of_headlines() {
        Headline headline1 = new Headline();
        Headline headline2 = new Headline();

        Headlines headlines1 = Headlines.of(headline1, headline2);
        Headlines headlines2 = Headlines.of(List.of(headline1, headline2));

        assertEquals(List.of(headline1, headline2), headlines1.getList());
        assertEquals(List.of(headline1, headline2), headlines2.getList());
    }
    
    @Test
    public void can_add_headlines() {
        Headline headline1 = new Headline();
        Headline headline2 = new Headline();
        Headlines headlines = Headlines.of(headline1);

        headlines.add(List.of(headline2));

        assertEquals(List.of(headline1, headline2), headlines.getList());
    }

    @Test
    public void gets_headlines_from_a_certain_day() {
        Headline headline1 = Headline.builder().date(LocalDate.parse("2018-05-05")).build();
        Headline headline2 = Headline.builder().date(LocalDate.parse("2020-09-17")).build();
        Headlines headlines = Headlines.of(headline1, headline2);

        assertEquals(List.of(headline2), headlines.from("2020-09-17"));
    }
}
