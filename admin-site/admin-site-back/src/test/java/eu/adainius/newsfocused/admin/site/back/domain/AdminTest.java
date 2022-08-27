package eu.adainius.newsfocused.admin.site.back.domain;

import static org.junit.jupiter.api.Assertions.fail;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.vavr.control.Either;

import static org.assertj.core.api.Assertions.*;

public class AdminTest {

    @Test
    public void registers_a_new_user() {
        // GIVEN
        Admin admin = new Admin("admin");
        String email = "email";
        Set<String> daysToSendOn = Set.of("Monday");
        Set<String> sites = Set.of("BBC");
        int headlineCount = 2;

        // WHEN
        User userRegistered = admin.registerNewUser(email, daysToSendOn, sites, headlineCount).get();

        // THEN
        assertThat(userRegistered.email()).isEqualTo(email);
        assertThat(userRegistered.preferences().daysToSendOn()).isEqualTo(daysToSendOn);
        assertThat(userRegistered.preferences().sites()).isEqualTo(sites);
        assertThat(userRegistered.preferences().headlineCount()).isEqualTo(headlineCount);
    }

    @Test
    public void validates_days_to_send_on_is_not_empty() {
        // GIVEN
        Admin admin = new Admin("admin");
        Set<String> daysToSendOn = Set.of();
            
        // WHEN
        Either<String,User> registration = admin.registerNewUser("email", daysToSendOn, Set.of(), 777);
        
        // THEN
        assertThat(registration.isLeft()).isTrue();
        assertThat(registration.getLeft()).isNotEmpty();
    }

    @Test
    public void updates_a_given_user() {
        fail();
    }

    @Test
    public void searches_users_by_email() {
        fail();
    }

    @Test
    public void searches_users_by_incomplete_email() {
        fail();
    }

    @Test
    public void searches_users_by_email_does_pagination() {
        fail();
    }

    @Test
    public void opens_user_details() {
        fail();
    }

}
