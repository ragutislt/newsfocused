package eu.adainius.newsfocused.admin.site.back.controller;

import java.util.Set;

import static org.assertj.core.api.Assertions.catchException;

import javax.ws.rs.WebApplicationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

import eu.adainius.newsfocused.admin.site.back.domain.User;
import eu.adainius.newsfocused.admin.site.back.domain.UserSearchResults;
import eu.adainius.newsfocused.admin.site.back.domain.User.Preferences;
import eu.adainius.newsfocused.admin.site.back.services.AdminSiteApplicationService;
import io.vavr.control.Either;

@ExtendWith(MockitoExtension.class)
public class ApiControllerTest {

    @Mock
    AdminSiteApplicationService adminSiteApplicationService;

    @InjectMocks
    ApiController apiController;

    @Test
    public void test_searchUsers() {
        // GIVEN
        String emailSearchTerm = "email";
        String adminUsername = "admin";
        int pageSize = 2;
        int pageRequested = 1;
        String sampleEmail = "email@email.com";
        Mockito.when(adminSiteApplicationService.searchUser(adminUsername, emailSearchTerm, pageSize, pageRequested)).thenReturn(Either.right(UserSearchResults.of(Set.of(User.builder().email(sampleEmail).build()), pageSize, pageRequested)));
        
        // WHEN
        Set<User> usersFound = apiController.searchUser(emailSearchTerm, pageSize, pageRequested);
        
        // THEN
        assertThat(usersFound).isNotEmpty();
        assertThat(usersFound)
        .filteredOn(user -> user.email().equals(sampleEmail)).isNotEmpty();
    }

    @Test
    public void test_searchUsers_throws_WAE_if_admin_not_found() {
        // GIVEN
        String emailSearchTerm = "email";
        String adminUsername = "admin";
        int pageSize = 2;
        int pageRequested = 1;
        Mockito.when(adminSiteApplicationService.searchUser(adminUsername, emailSearchTerm, pageSize, pageRequested)).thenReturn(Either.left("some error"));
        
        // WHEN
        Exception thrown = catchException(() -> {
            apiController.searchUser(emailSearchTerm, pageSize, pageRequested);
        });

        // THEN
        assertThat(thrown).isInstanceOf(WebApplicationException.class);
        assertThat(thrown).hasMessage("Admin not found");
        assertThat(thrown).extracting(ex -> ((WebApplicationException)ex).getResponse().getStatus()).isEqualTo(401);
    }

    @Test
    public void test_registerNewUser() {
        // GIVEN
        String email = "email";
        String adminUsername = "admin";
        Set<String> daysToSendOn = Set.of("Monday");
        Set<String> sites = Set.of("www.bbc.com");
        int headlineCount = 4;
        
        Mockito.when(
            adminSiteApplicationService.registerUser(adminUsername, email, daysToSendOn, sites, headlineCount)
            )
            .thenReturn(Either.right(
                User.builder().email(email).preferences(
                        Preferences.builder().daysToSendOn(daysToSendOn).sites(sites).headlineCount(headlineCount)
                        .build()
                    ).build()
                )
            );

        RegisterUserRequest request = new RegisterUserRequest(email, daysToSendOn, sites, headlineCount);
        
        // WHEN
        User userCreated = apiController.registerUser(request);
        
        // THEN
        assertThat(userCreated.email()).isEqualTo(email);
    }

    @Test
    public void test_registerNewUser_throws_WAE_if_admin_not_found() {
        // GIVEN
        String email = "email";
        String adminUsername = "admin";
        Set<String> daysToSendOn = Set.of("Monday");
        Set<String> sites = Set.of("www.bbc.com");
        int headlineCount = 4;
        Mockito.when(adminSiteApplicationService.registerUser(adminUsername, email, daysToSendOn, sites, headlineCount)
        ).thenReturn(Either.left("some error"));

        RegisterUserRequest request = new RegisterUserRequest(email, daysToSendOn, sites, headlineCount);
        
        // WHEN
        Exception thrown = catchException(() -> {
            apiController.registerUser(request);
        });

        // THEN
        assertThat(thrown).isInstanceOf(WebApplicationException.class);
        assertThat(thrown).hasMessage("Admin not found");
        assertThat(thrown).extracting(ex -> ((WebApplicationException)ex).getResponse().getStatus()).isEqualTo(401);
    }

    @Test
    public void test_updateExistingUser() {
        // GIVEN
        String email = "email";
        String adminUsername = "admin";
        Set<String> daysToSendOn = Set.of("Monday");
        Set<String> sites = Set.of("www.bbc.com");
        int headlineCount = 4;
        
        Mockito.when(
            adminSiteApplicationService.updateUser(adminUsername, email, daysToSendOn, sites, headlineCount)
            )
            .thenReturn(Either.right(
                User.builder().email(email).preferences(
                        Preferences.builder().daysToSendOn(daysToSendOn).sites(sites).headlineCount(headlineCount)
                        .build()
                    ).build()
                )
            );

        RegisterUserRequest request = new RegisterUserRequest(email, daysToSendOn, sites, headlineCount);
        
        // WHEN
        User userCreated = apiController.updateUser(request);
        
        // THEN
        assertThat(userCreated.email()).isEqualTo(email);
    }

    @Test
    public void test_updateExistingUser_throws_WAE_if_admin_not_found() {
        // GIVEN
        String email = "email";
        String adminUsername = "admin";
        Set<String> daysToSendOn = Set.of("Monday");
        Set<String> sites = Set.of("www.bbc.com");
        int headlineCount = 4;
        Mockito.when(adminSiteApplicationService.updateUser(adminUsername, email, daysToSendOn, sites, headlineCount)
        ).thenReturn(Either.left("some error"));

        RegisterUserRequest request = new RegisterUserRequest(email, daysToSendOn, sites, headlineCount);
        
        // WHEN
        Exception thrown = catchException(() -> {
            apiController.updateUser(request);
        });

        // THEN
        assertThat(thrown).isInstanceOf(WebApplicationException.class);
        assertThat(thrown).hasMessage("Admin not found");
        assertThat(thrown).extracting(ex -> ((WebApplicationException)ex).getResponse().getStatus()).isEqualTo(401);
    }
}