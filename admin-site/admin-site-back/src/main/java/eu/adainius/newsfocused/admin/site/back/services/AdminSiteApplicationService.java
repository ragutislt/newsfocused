package eu.adainius.newsfocused.admin.site.back.services;

import java.util.Optional;
import java.util.Set;

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
