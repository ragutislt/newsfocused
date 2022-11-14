package eu.adainius.newsfocused.admin.site.back.domain.services;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.validator.routines.EmailValidator;

import eu.adainius.newsfocused.admin.site.back.domain.Admin;
import eu.adainius.newsfocused.admin.site.back.domain.User;
import eu.adainius.newsfocused.admin.site.back.domain.UserSearchResults;
import eu.adainius.newsfocused.admin.site.back.domain.repositories.AdminRepository;
import eu.adainius.newsfocused.admin.site.back.domain.repositories.UserRepository;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AdminSiteDomainServiceImpl implements AdminSiteDomainService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Override
    public Either<String, User> registerUser(String adminUsername, String email, Set<String> daysToSendOn,
            Set<String> sites,
            int headlineCount) {
        if (!EmailValidator.getInstance().isValid(email)) {
            return Either.left("Email is not valid");
        }

        return ifAdminExists(adminUsername, admin -> {
            Either<String, User> registration = admin.registerNewUser(email, daysToSendOn, sites,
                    headlineCount);

            if (registration.isRight()) {
                userRepository.save(registration.get());
            }

            return registration;
        });
    }

    @Override
    public Either<String, User> updateUser(String adminUsername, String userToUpdate, Set<String> daysToSendOn,
            Set<String> sites,
            int headlineCount) {
        return ifAdminExists(adminUsername, admin -> {
            Either<String, User> updatedUser = admin.updateUser(userToUpdate, daysToSendOn, sites,
                    headlineCount);

            if (updatedUser.isRight()) {
                userRepository.save(updatedUser.get());
            }

            return updatedUser;
        });
    }

    @Override
    public Either<String, UserSearchResults> searchUser(String adminUsername, String emailSearchTerm,
            int pageSize, int pageRequested) {
        return ifAdminExists(adminUsername, admin -> {
            Set<User> allUsers = userRepository.retrieveAll();
            // TODO potentially, since we use Set everywhere, the ordering might get screwed
            // up if on next save it does a different order
            UserSearchResults searchResults = admin.searchForUser(emailSearchTerm, allUsers, pageSize,
                    pageRequested);
            return Either.right(searchResults);
        });
    }

    @Override
    public Either<String, Optional<User>> retrieveUser(String adminUsername, String userToRetrieve) {
        return ifAdminExists(adminUsername, admin -> {
            Set<User> allUsers = userRepository.retrieveAll();
            Optional<User> retrievedUser = admin.openUserDetails(userToRetrieve, allUsers);
            return Either.right(retrievedUser);
        });
    }

    private <K> Either<String, K> ifAdminExists(String adminUsername, Function<Admin, Either<String, K>> ifYes) {
        Optional<Admin> adminResult = adminRepository.retrieveByUsername(adminUsername);

        if (adminResult.isPresent()) {
            return ifYes.apply(adminResult.get());
        } else {
            return Either.left(String.format("Admin with a username %s was not found!", adminUsername));
        }
    }
}
