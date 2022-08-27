package eu.adainius.newsfocused.admin.site.back.domain;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class User {
    private final String email;
    private final Preferences preferences;

    @Getter
    static class Preferences {
        private Set<String> daysToSendOn;
        private Set<String> sites;
        private int headlineCount;
        // TODO private LocalTime time to send at

        @Builder
        private Preferences(Set<String> daysToSendOn, Set<String> sites, int headlineCount) {
            if (daysToSendOn == null || daysToSendOn.isEmpty()) {
                throw new IllegalArgumentException("Days to send emails on is mandatory!");
            }

            this.daysToSendOn = daysToSendOn;
            this.sites = sites;
            this.headlineCount = headlineCount;
        }
    }
}
