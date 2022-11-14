package eu.adainius.newsfocused.admin.site.back.domain.services;

import java.util.Optional;
import java.util.Set;

import eu.adainius.newsfocused.admin.site.back.domain.User;
import eu.adainius.newsfocused.admin.site.back.domain.UserSearchResults;
import io.vavr.control.Either;

public interface AdminSiteDomainService {
    public Either<String, User> registerUser(String adminUsername, String email, Set<String> daysToSendOn,
            Set<String> sites,
            int headlineCount);

    public Either<String, User> updateUser(String adminUsername, String userToUpdate, Set<String> daysToSendOn,
            Set<String> sites,
            int headlineCount);

    public Either<String, UserSearchResults> searchUser(String adminUsername, String emailSearchTerm,
            int pageSize, int pageRequested);

    public Either<String, Optional<User>> retrieveUser(String adminUsername, String userToRetrieve);
}
