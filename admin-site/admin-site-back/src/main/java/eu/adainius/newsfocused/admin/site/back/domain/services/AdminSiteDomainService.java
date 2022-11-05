package eu.adainius.newsfocused.admin.site.back.domain.services;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import eu.adainius.newsfocused.admin.site.back.domain.Admin;
import eu.adainius.newsfocused.admin.site.back.domain.User;
import eu.adainius.newsfocused.admin.site.back.domain.UserSearchResults;
import eu.adainius.newsfocused.admin.site.back.repositories.AdminRepository;
import eu.adainius.newsfocused.admin.site.back.repositories.UserRepository;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

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
