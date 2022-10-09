package eu.adainius.newsfocused.admin.site.back.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import eu.adainius.newsfocused.admin.site.back.domain.Admin;
import eu.adainius.newsfocused.admin.site.back.domain.User;
import eu.adainius.newsfocused.admin.site.back.domain.UserSearchResults;
import eu.adainius.newsfocused.admin.site.back.repositories.AdminRepository;
import eu.adainius.newsfocused.admin.site.back.repositories.UserRepository;
import io.vavr.control.Either;

@ExtendWith(MockitoExtension.class)
public class AdminSiteApplicationServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AdminRepository adminRepository;

    @InjectMocks
    AdminSiteApplicationServiceImpl adminSiteApplicationService;

    @Test
    public void registers_a_new_user_through_the_admin_and_saves() {
        // GIVEN
        String userEmail = "email@email.com";
        Set<String> daysToSendOn = Set.of("Monday");
        Set<String> sites = Set.of("BBC");
        int headlineCount = 8;
        User registeredUser = User.builder().email(userEmail).preferences(
                User.Preferences.builder()
                        .daysToSendOn(daysToSendOn)
                        .headlineCount(headlineCount)
                        .build())
                .build();

        String adminUsername = "admin";
        Admin mockAdmin = mock(Admin.class);
        Either<String, User> newUser = Either.right(registeredUser);
        Mockito.when(adminRepository.retrieveByUsername(adminUsername)).thenReturn(Optional.of(mockAdmin));
        Mockito.when(mockAdmin.registerNewUser(userEmail, daysToSendOn, sites, headlineCount))
                .thenReturn(newUser);

        // WHEN
        Either<String, User> registration = adminSiteApplicationService.registerUser(adminUsername, userEmail,
                daysToSendOn, sites, headlineCount);

        // THEN
        assertThat(registration).isEqualTo(newUser);
        Mockito.verify(userRepository).save(registeredUser);
    }

    @Test
    public void returns_an_error_if_user_creation_fails() {
        // GIVEN
        String userEmail = "email@domain.com";
        Set<String> daysToSendOn = Set.of("Monday");
        Set<String> sites = Set.of("BBC");
        int headlineCount = 8;

        String adminUsername = "admin";
        Admin mockAdmin = mock(Admin.class);
        Mockito.when(adminRepository.retrieveByUsername(adminUsername)).thenReturn(Optional.of(mockAdmin));
        Mockito.when(mockAdmin.registerNewUser(userEmail, daysToSendOn, sites, headlineCount))
                .thenReturn(Either.left("some system error happened"));

        // WHEN
        Either<String, User> registration = adminSiteApplicationService.registerUser(adminUsername, userEmail,
                daysToSendOn, sites, headlineCount);

        // THEN
        Mockito.verifyNoInteractions(userRepository);
        assertThat(registration.isLeft()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = { "shit", "0", "", "email", "email@email", "@", "@domain.com" })
    @NullSource
    public void register_user_returns_an_error_if_email_is_not_valid(String invalidEmail) {
        // GIVEN
        // WHEN
        Either<String, User> registration = adminSiteApplicationService
                .registerUser("admin", invalidEmail, Set.of(), Set.of(), 1);

        // THEN
        Mockito.verifyNoInteractions(adminRepository);
        Mockito.verifyNoInteractions(userRepository);
        assertThat(registration.isLeft()).isTrue();
    }

    @Test
    public void register_user_returns_an_error_if_admin_does_not_exist() {
        // GIVEN
        String nonExistingAdmin = "badAdmin";

        // WHEN
        Either<String, User> registration = adminSiteApplicationService
                .registerUser(nonExistingAdmin, "email", Set.of(), Set.of(), 1);

        // THEN
        Mockito.verifyNoInteractions(userRepository);
        assertThat(registration.isLeft()).isTrue();
    }

    @Test
    public void updates_a_user_and_saves() {
        // GIVEN
        String userEmail = "email";
        Set<String> daysToSendOn = Set.of("Monday");
        Set<String> sites = Set.of("BBC");
        int headlineCount = 8;
        User userToUpdate = User.builder().email(userEmail).preferences(
                User.Preferences.builder()
                        .daysToSendOn(daysToSendOn)
                        .headlineCount(headlineCount)
                        .build())
                .build();

        String adminUsername = "admin";
        Admin mockAdmin = mock(Admin.class);
        Mockito.when(adminRepository.retrieveByUsername(adminUsername)).thenReturn(Optional.of(mockAdmin));
        Mockito.when(mockAdmin.updateUser(userEmail, daysToSendOn, sites, headlineCount))
                .thenReturn(Either.right(userToUpdate));

        // WHEN
        adminSiteApplicationService.updateUser(adminUsername, userEmail, daysToSendOn, sites, headlineCount);

        // THEN
        Mockito.verify(userRepository).save(userToUpdate);
    }

    @Test
    public void returns_an_error_if_user_update_fails() {
        // GIVEN
        String userEmail = "email@domain.com";
        Set<String> daysToSendOn = Set.of("Monday");
        Set<String> sites = Set.of("BBC");
        int headlineCount = 8;

        String adminUsername = "admin";
        Admin mockAdmin = mock(Admin.class);
        Mockito.when(adminRepository.retrieveByUsername(adminUsername)).thenReturn(Optional.of(mockAdmin));
        Mockito.when(mockAdmin.updateUser(userEmail, daysToSendOn, sites, headlineCount))
                .thenReturn(Either.left("some system error happened"));

        // WHEN
        Either<String, User> updatedUser = adminSiteApplicationService.updateUser(adminUsername, userEmail,
                daysToSendOn, sites, headlineCount);

        // THEN
        Mockito.verifyNoInteractions(userRepository);
        assertThat(updatedUser.isLeft()).isTrue();
    }

    @Test
    public void update_user_returns_an_error_if_admin_does_not_exist() {
        // GIVEN
        String nonExistingAdmin = "badAdmin";

        // WHEN
        Either<String, User> updatedUser = adminSiteApplicationService
                .updateUser(nonExistingAdmin, "email", Set.of(), Set.of(), 1);

        // THEN
        Mockito.verifyNoInteractions(userRepository);
        assertThat(updatedUser.isLeft()).isTrue();
    }

    @Test
    public void searches_users() {
        // GIVEN
        String userEmail = "email";
        User registeredUser = User.builder().email(userEmail).preferences(
                User.Preferences.builder()
                        .daysToSendOn(Set.of("Monday"))
                        .headlineCount(111)
                        .build())
                .build();

        Set<User> allUsers = Set.of(registeredUser);
        int pageSize = 10;
        int pageRequested = 1;

        String adminUsername = "admin";
        Admin mockAdmin = mock(Admin.class);
        Mockito.when(adminRepository.retrieveByUsername(adminUsername)).thenReturn(Optional.of(mockAdmin));
        Mockito.when(mockAdmin.searchForUser(userEmail, allUsers, pageSize, pageRequested))
                .thenReturn(UserSearchResults.of(allUsers, pageRequested, 1));
        Mockito.when(userRepository.retrieveAll()).thenReturn(allUsers);

        // WHEN
        Either<String, UserSearchResults> searchResults = adminSiteApplicationService
                .searchUser(adminUsername, "email", 10, 1);

        // THEN
        Mockito.verify(userRepository).retrieveAll();
        assertThat(searchResults.get()).isEqualTo(UserSearchResults.of(allUsers, pageRequested, 1));
    }

    @Test
    public void search_user_returns_an_error_if_admin_does_not_exist() {
        // GIVEN
        String nonExistingAdmin = "badAdmin";

        // WHEN
        Either<String, UserSearchResults> searchResults = adminSiteApplicationService
                .searchUser(nonExistingAdmin, "email", 10, 1);

        // THEN
        Mockito.verifyNoInteractions(userRepository);
        assertThat(searchResults.isLeft()).isTrue();
    }

    @Test
    public void retrieves_a_user() {
        // GIVEN
        String userEmail = "email";
        User registeredUser = User.builder().email(userEmail).preferences(
                User.Preferences.builder()
                        .daysToSendOn(Set.of("Monday"))
                        .headlineCount(111)
                        .build())
                .build();

        String adminUsername = "admin";
        Admin mockAdmin = mock(Admin.class);
        Mockito.when(adminRepository.retrieveByUsername(adminUsername)).thenReturn(Optional.of(mockAdmin));
        Mockito.when(mockAdmin.openUserDetails(userEmail, Set.of(registeredUser)))
                .thenReturn(Optional.of(registeredUser));
        Mockito.when(userRepository.retrieveAll()).thenReturn(Set.of(registeredUser));

        // WHEN
        Either<String, Optional<User>> user = adminSiteApplicationService.retrieveUser(adminUsername, userEmail);

        // THEN
        assertThat(user.get().get()).isEqualTo(registeredUser);
    }

    @Test
    public void retrieves_a_user_returns_empty_if_user_not_found() {
        // GIVEN
        String userEmail = "email";
        User registeredUser = User.builder().email(userEmail).preferences(
                User.Preferences.builder()
                        .daysToSendOn(Set.of("Monday"))
                        .headlineCount(111)
                        .build())
                .build();

        String adminUsername = "admin";
        Admin mockAdmin = mock(Admin.class);
        Mockito.when(adminRepository.retrieveByUsername(adminUsername)).thenReturn(Optional.of(mockAdmin));
        Mockito.when(mockAdmin.openUserDetails(userEmail, Set.of(registeredUser)))
                .thenReturn(Optional.empty());
        Mockito.when(userRepository.retrieveAll()).thenReturn(Set.of(registeredUser));

        // WHEN
        Either<String, Optional<User>> user = adminSiteApplicationService.retrieveUser(adminUsername, userEmail);

        // THEN
        assertThat(user.get().isPresent()).isFalse();
    }

    @Test
    public void retrieve_user_returns_an_error_if_admin_does_not_exist() {
        // GIVEN
        String nonExistingAdmin = "badAdmin";

        // WHEN
        Either<String, Optional<User>> searchResult = adminSiteApplicationService
                .retrieveUser(nonExistingAdmin, "email");

        // THEN
        Mockito.verifyNoInteractions(userRepository);
        assertThat(searchResult.isLeft()).isTrue();
    }
}
