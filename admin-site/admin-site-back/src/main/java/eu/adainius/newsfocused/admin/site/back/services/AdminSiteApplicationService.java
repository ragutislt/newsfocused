package eu.adainius.newsfocused.admin.site.back.services;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import eu.adainius.newsfocused.admin.site.back.domain.User;
import eu.adainius.newsfocused.admin.site.back.domain.UserSearchResults;
import io.vavr.control.Either;

@Service
public class AdminSiteApplicationService {
    public Either<String, User> registerUser(String adminUsername, String email, Set<String> daysToSendOn,
            Set<String> sites,
            int headlineCount) {
        return null;
    }

    public Either<String, User> updateUser(String adminUsername, String userToUpdate, Set<String> daysToSendOn,
            Set<String> sites,
            int headlineCount) {
        return null;
    }

    public UserSearchResults searchUser(String adminUsername, String emailSearchTerm,
            int pageSize, int pageRequested) {
        return null;
    }

    public Optional<User> retrieveUser(String adminUsername, String email) {
        return null;
    }
}
