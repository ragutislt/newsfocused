package eu.adainius.newsfocused.admin.site.back.services;

import java.util.Optional;
import java.util.Set;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import eu.adainius.newsfocused.admin.site.back.domain.Admin;
import eu.adainius.newsfocused.admin.site.back.domain.User;
import eu.adainius.newsfocused.admin.site.back.domain.UserSearchResults;
import eu.adainius.newsfocused.admin.site.back.repositories.AdminRepository;
import eu.adainius.newsfocused.admin.site.back.repositories.UserRepository;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminSiteApplicationService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    public Either<String, User> registerUser(String adminUsername, String email, Set<String> daysToSendOn,
            Set<String> sites,
            int headlineCount) {
        if (!EmailValidator.getInstance().isValid(email)) {
            return Either.left("Email is not valid");
        }

        Optional<Admin> adminResult = adminRepository.retrieveByUsername(adminUsername);

        if (adminResult.isPresent()) {
            Either<String, User> registration = adminResult.get().registerNewUser(email, daysToSendOn, sites,
                    headlineCount);

            if (registration.isRight()) {
                userRepository.save(registration.get());
            }

            return registration;
        } else {
            return Either.left(String.format("Admin with a username %s was not found!", adminUsername));
        }
    }

    public Either<String, User> updateUser(String adminUsername, String userToUpdate, Set<String> daysToSendOn,
            Set<String> sites,
            int headlineCount) {
        // TODO make general this admin search
        Optional<Admin> adminResult = adminRepository.retrieveByUsername(adminUsername);

        if (adminResult.isPresent()) {
            Either<String, User> updatedUser = adminResult.get().updateUser(userToUpdate, daysToSendOn, sites,
                    headlineCount);

            if (updatedUser.isRight()) {
                userRepository.save(updatedUser.get());
            }

            return updatedUser;
        } else {
            return Either.left(String.format("Admin with a username %s was not found!", adminUsername));
        }
    }

    public Either<String, UserSearchResults> searchUser(String adminUsername, String emailSearchTerm,
            int pageSize, int pageRequested) {
        // TODO make general this admin search, also rename adminResult to admin
        Optional<Admin> adminResult = adminRepository.retrieveByUsername(adminUsername);

        if (adminResult.isPresent()) {
            Set<User> allUsers = userRepository.retrieveAll();
            UserSearchResults searchResults = adminResult.get().searchForUser(emailSearchTerm, allUsers, pageSize,
                    pageRequested);

            return Either.right(searchResults);
        } else {
            return Either.left(String.format("Admin with a username %s was not found!", adminUsername));
        }
    }

    public Either<String, Optional<User>> retrieveUser(String adminUsername, String userToRetrieve) {
        // TODO make general this admin search, also rename adminResult to admin
        Optional<Admin> adminResult = adminRepository.retrieveByUsername(adminUsername);

        if (adminResult.isPresent()) {
            Set<User> allUsers = userRepository.retrieveAll();
            Optional<User> retrievedUser = adminResult.get().openUserDetails(userToRetrieve, allUsers);
            return Either.right(retrievedUser);
        } else {
            return Either.left(String.format("Admin with a username %s was not found!", adminUsername));
        }
    }
}
