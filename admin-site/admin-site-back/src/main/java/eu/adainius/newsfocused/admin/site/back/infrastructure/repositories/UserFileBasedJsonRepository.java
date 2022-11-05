package eu.adainius.newsfocused.admin.site.back.infrastructure.repositories;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.adainius.newsfocused.admin.site.back.domain.User;
import eu.adainius.newsfocused.admin.site.back.domain.repositories.UserRepository;

public class UserFileBasedJsonRepository implements UserRepository {
    private final String repoFilePath;
    private final ObjectMapper objectMapper; 

    public UserFileBasedJsonRepository(String repoFilePath, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.repoFilePath = repoFilePath;

        String fullRepoPath = Paths.get(this.repoFilePath).toAbsolutePath().toString();
        if (!new File(fullRepoPath).exists()) {
            throw new RuntimeException(String.format("File %s does not exist!", fullRepoPath));
        }
    }

    @Override
    public void save(User registeredUser) {
        String fullRepoPath = Paths.get(this.repoFilePath).toAbsolutePath().toString();

        Set<User> allUsers = this.retrieveAll();
        Optional<User> existingUser = allUsers.stream().filter(u -> u.email().equals(registeredUser.email()))
                .findFirst();

        if (existingUser.isPresent()) {
            allUsers.remove(registeredUser);
            allUsers.add(registeredUser);
        } else {
            allUsers.add(registeredUser);
        }

        try {
            File file = new File(fullRepoPath);
            objectMapper.writeValue(file, allUsers);
        } catch (IOException e) {
            throw new RuntimeException("Exception when writing file", e);
        }
    }

    @Override
    public Set<User> retrieveAll() {
        FileReader reader = null;
        String fullRepoPath = Paths.get(this.repoFilePath).toAbsolutePath().toString();
        try {
            Set<User> users = null;

            if (new File(fullRepoPath).exists()) {
                reader = new FileReader(fullRepoPath);
                users = objectMapper.readValue(reader, new TypeReference<Set<User>>() {
                });
            }

            return users != null ? users : Collections.emptySet();
        } catch (IOException e) {
            throw new RuntimeException("Exception when reading file", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                throw new RuntimeException("Exception when reading file", e);
            }
        }
    }

}
