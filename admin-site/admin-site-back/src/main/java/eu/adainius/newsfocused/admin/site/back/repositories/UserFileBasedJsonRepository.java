package eu.adainius.newsfocused.admin.site.back.repositories;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import eu.adainius.newsfocused.admin.site.back.domain.User;

public class UserFileBasedJsonRepository implements UserRepository {
    private final String repoFilePath;
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
        // TODO Auto-generated method stub

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
