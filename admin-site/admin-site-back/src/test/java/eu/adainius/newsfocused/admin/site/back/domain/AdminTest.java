package eu.adainius.newsfocused.admin.site.back.domain;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
        Either<String, User> registration = admin.registerNewUser("email", daysToSendOn, Set.of(), 777);

        // THEN
        assertThat(registration.isLeft()).isTrue();
        assertThat(registration.getLeft()).isNotEmpty();
    }

    @Test
    public void updates_a_given_user() {
        // GIVEN
        Admin admin = new Admin("admin");
        String email = "email";
        Set<String> daysToSendOn = Set.of("Monday");
        Set<String> sites = Set.of("BBC");
        int headlineCount = 2;

        // WHEN
        User userRegistered = admin.updateUser(email, daysToSendOn, sites, headlineCount).get();

        // THEN
        assertThat(userRegistered.email()).isEqualTo(email);
        assertThat(userRegistered.preferences().daysToSendOn()).isEqualTo(daysToSendOn);
        assertThat(userRegistered.preferences().sites()).isEqualTo(sites);
        assertThat(userRegistered.preferences().headlineCount()).isEqualTo(headlineCount);
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
        // GIVEN
        Admin admin = new Admin("admin");
        String userEmail = "email111";
        Set<String> daysToSendOn = Set.of("Monday");
        Set<String> sites = Set.of("BBC");
        int headlineCount = 123;
        Set<User> allUsers = generateTestUsers();

        // WHEN
        User userFound = admin.openUserDetails(userEmail, allUsers).get();

        // THEN
        assertThat(userFound.email()).isEqualTo(userEmail);
        assertThat(userFound.preferences().daysToSendOn()).isEqualTo(daysToSendOn);
        assertThat(userFound.preferences().sites()).isEqualTo(sites);
        assertThat(userFound.preferences().headlineCount()).isEqualTo(headlineCount);
    }

    @Test
    public void opens_user_details_returns_error_message_if_user_not_found() {
        // GIVEN
        Admin admin = new Admin("admin");
        String nonExistingUserEmail = "lksdngw8094gneiudrgne";
        Set<User> allUsers = generateTestUsers();

        // WHEN
        Optional<User> userNotFound = admin.openUserDetails(nonExistingUserEmail, allUsers);

        // THEN
        assertThat(userNotFound.isPresent()).isFalse();
    }

    private Set<User> generateTestUsers() {
        Set<User> users = new HashSet<>();

        users.add(
                new User("email111", User.Preferences.builder().daysToSendOn(Set.of("Monday")).sites(Set.of("BBC"))
                        .headlineCount(123).build()));

        users.add(
                new User("email12222",
                        User.Preferences.builder().daysToSendOn(Set.of("Monday", "Friday")).sites(Set.of("BBC"))
                                .headlineCount(157).build()));

        users.add(
                new User("email8984984", User.Preferences.builder().daysToSendOn(Set.of("Sunday")).sites(Set.of("LRT"))
                        .headlineCount(19999).build()));

        return users;
    }

}
