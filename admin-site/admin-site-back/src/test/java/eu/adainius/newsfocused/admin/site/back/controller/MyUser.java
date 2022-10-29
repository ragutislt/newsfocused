package eu.adainius.newsfocused.admin.site.back.controller;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import eu.adainius.newsfocused.admin.site.back.domain.EmailSent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@ToString
@EqualsAndHashCode(of = {"email"})
@Builder
// @AllArgsConstructor
public class MyUser {
    private final String email;
    @Builder.Default
    private final Date registrationDate = new Date();
    @Builder.Default
    private final Set<EmailSent> emailsSent = Collections.emptySet();
    private final MyPreferences preferences;
    // TODO we need to harmonize this with User class from the newsfocused domain (or import it)

    @Getter
    @ToString
    @EqualsAndHashCode
    public static class MyPreferences {
        private final Set<String> daysToSendOn;
        private final Set<String> sites;
        private final int headlineCount;
        // TODO private LocalTime time to send at

        @Builder
        private MyPreferences(Set<String> daysToSendOn, Set<String> sites, int headlineCount) {
            if (daysToSendOn == null || daysToSendOn.isEmpty()) {
                throw new IllegalArgumentException("Days to send emails on is mandatory!");
            }

            this.daysToSendOn = daysToSendOn;
            this.sites = sites;
            this.headlineCount = headlineCount;
        }
    }
}
