package eu.adainius.newsfocused.admin.site.back.repositories;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import eu.adainius.newsfocused.admin.site.back.domain.User;

public class UserFileBasedJsonRepository implements UserRepository {
    private final String repoFilePath;
    // TODO - use jackson instead with the same object mapper as is used for HTTP API
    private final Gson gson = new Gson(); 

    public UserFileBasedJsonRepository(String repoFilePath) {
        this.repoFilePath = repoFilePath;

        String fullRepoPath = Paths.get(this.repoFilePath).toAbsolutePath().toString();
        if (!new File(fullRepoPath).exists()) {
            throw new RuntimeException(String.format("File %s does not exist!", fullRepoPath));
        }
    }

    @Override
    public void save(User registeredUser) {
        FileWriter writer = null;
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
            writer = new FileWriter(fullRepoPath);
            gson.toJson(allUsers, writer);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Exception when writing file", e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                throw new RuntimeException("Exception when writing file", e);
            }
        }
    }

    @Override
    public Set<User> retrieveAll() {
        FileReader reader = null;
        String fullRepoPath = Paths.get(this.repoFilePath).toAbsolutePath().toString();
        try {
            Set<User> users = null;

            if (new File(fullRepoPath).exists()) {
                Type setWithUserType = new TypeToken<HashSet<User>>() {
                }.getType();
                reader = new FileReader(fullRepoPath);
                users = gson.fromJson(reader, setWithUserType);
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
