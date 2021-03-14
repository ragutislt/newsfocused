package eu.adainius.newsfocused;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Headline {
    private LocalDate date;

    public LocalDate date() {
        return date;
    }

    public Headline() {
    }

}
