package eu.adainius.newsfocused.admin.site.back.domain;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class User {
    private String email;
    private Preferences preferences;

    @Builder
    @Getter
    static class Preferences {
        private List<String> daysToSendOn;
        private String[] sites;
        private int headlineCount;
        // TODO private LocalTime time to send at
    }
}
