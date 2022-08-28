package eu.adainius.newsfocused.admin.site.back.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Set;

import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void validates_days_to_send_on_is_not_empty() {
        // GIVEN
        // WHEN
        // THEN
        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    User.builder().email("email")
                            .preferences(
                                    User.Preferences.builder().daysToSendOn(Set.of()).sites(Set.of("LRT"))
                                            .headlineCount(19999).build())
                            .build();
                });
    }
}
