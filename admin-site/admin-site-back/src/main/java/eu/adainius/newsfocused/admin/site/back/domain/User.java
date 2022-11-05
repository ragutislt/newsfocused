package eu.adainius.newsfocused.admin.site.back.domain;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@ToString
@EqualsAndHashCode(of = { "email" })
@Builder
public class User {
    private final String email;
    @Builder.Default
    private final Date registrationDate = new Date();
    @Builder.Default
    private final Set<EmailSent> emailsSent = Collections.emptySet();
    private final Preferences preferences;
    // TODO we need to harmonize this with User class from the newsfocused domain
    // (or import it)

    @Getter
    @ToString
    @EqualsAndHashCode
    public static class Preferences {
        private final Set<String> daysToSendOn;
        private final Set<String> sites;
        private final int headlineCount;
        // TODO private LocalTime time to send at

        @Builder
        private Preferences(Set<String> daysToSendOn, Set<String> sites, int headlineCount) {
            if (daysToSendOn == null || daysToSendOn.isEmpty()) {
                throw new IllegalArgumentException("Days to send emails on is mandatory!");
            }
            if (sites == null || sites.isEmpty()) {
                throw new IllegalArgumentException("Sites are mandatory!");
            }

            this.daysToSendOn = daysToSendOn;
            this.sites = sites;
            this.headlineCount = headlineCount == 0 ? 5 : headlineCount;
        }
    }
}
