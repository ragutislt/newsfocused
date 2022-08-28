package eu.adainius.newsfocused.admin.site.back.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import io.vavr.control.Either;

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
        assertThat(dateToString(userRegistered.registrationDate())).isEqualTo(dateToString(new Date()));
        assertThat((userRegistered.emailsSent())).isEmpty();
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
        // GIVEN
        Admin admin = new Admin("admin");
        String userEmail = "email111";
        Set<User> allUsers = generateTestUsers();

        // WHEN
        UserSearchResults searchResults = admin.searchForUser(userEmail, allUsers, 6, 1);

        // THEN
        assertThat(searchResults.usersFound().size()).isEqualTo(1);
        assertThat(searchResults.usersFound()).extracting("email").containsExactly(userEmail);
    }

    @Test
    public void searches_users_by_incomplete_email() {
        // GIVEN
        Admin admin = new Admin("admin");
        String userEmail = "email1";
        Set<User> allUsers = generateTestUsers();

        // WHEN
        UserSearchResults searchResults = admin.searchForUser(userEmail, allUsers, 6, 1);

        // THEN
        assertThat(searchResults.usersFound().size()).isEqualTo(2);
        assertThat(searchResults.usersFound()).extracting("email")
                .containsExactlyInAnyOrder("email111", "email12222");
    }

    @ParameterizedTest
    @CsvSource({
            "2,1,2",
            "2,2,2",
            "2,3,2",
            "2,4,0",
            "3,1,3",
            "4,2,2",
            "4,3,0",
            "5,1,5",
            "5,2,1",
            "10,1,6",
            "6,1,6",
            "6,2,0",
            "1,1,1",
            "1,6,1"
    })
    public void searches_users_by_email_does_pagination(int pageSize, int pageRequested, int expectedNumberOfRows) {
        // GIVEN
        Admin admin = new Admin("admin");
        String userEmail = "email";
        Set<User> allUsers = generateTestUsers();

        // WHEN
        UserSearchResults userSearchResults = admin.searchForUser(userEmail, allUsers, pageSize, pageRequested);

        // THEN
        assertThat(userSearchResults.totalCount()).isEqualTo(6);
        assertThat(userSearchResults.usersFound().size()).isEqualTo(expectedNumberOfRows);
        assertThat(userSearchResults.pageNumber()).isEqualTo(pageRequested);
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
                User.builder().email("email111")
                        .preferences(
                                User.Preferences.builder().daysToSendOn(Set.of("Monday")).sites(Set.of("BBC"))
                                        .headlineCount(123).build())
                        .build());

        users.add(
                User.builder().email("email12222")
                        .preferences(
                                User.Preferences.builder().daysToSendOn(Set.of("Monday", "Friday")).sites(Set.of("BBC"))
                                        .headlineCount(157).build())
                        .build());

        users.add(
                User.builder().email("email8984984")
                        .preferences(
                                User.Preferences.builder().daysToSendOn(Set.of("Sunday")).sites(Set.of("LRT"))
                                        .headlineCount(19999).build())
                        .build());

        users.add(
                User.builder().email("email21321313")
                        .preferences(
                                User.Preferences.builder().daysToSendOn(Set.of("Sunday")).sites(Set.of("LRT"))
                                        .headlineCount(19999).build())
                        .build());

        users.add(
                User.builder().email("email6666666")
                        .preferences(
                                User.Preferences.builder().daysToSendOn(Set.of("Thursday")).sites(Set.of("LRT"))
                                        .headlineCount(19999).build())
                        .build());

        users.add(
                User.builder().email("email777777")
                        .preferences(
                                User.Preferences.builder().daysToSendOn(Set.of("Friday")).sites(Set.of("LRT"))
                                        .headlineCount(19999).build())
                        .build());

        return users;
    }

    private String dateToString(Date date) {
        return new SimpleDateFormat("yyyy-mm-dd").format(date);
    }

}
