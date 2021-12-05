package eu.adainius.newsfocused.user;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import java.io.File;

import eu.adainius.newsfocused.ApplicationException;
import eu.adainius.newsfocused.util.Validation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class User {
    private String email;
    private String dataStorageFile;
    private List<String> daysToSendOn;
    private String[] sites;

    public String dataStorageFile() {
        return dataStorageFile;
    }

    public String email() {
        return email;
    }

    public List<String> sites() {
        return List.of(sites);
    }

    public List<String> daysToSendOn() {
        return daysToSendOn;
    }

    public static List<User> parseFromUsing(String propertiesFileLocation, Gson gson) {
        FileReader reader = null;
        User[] allUsers;
        try {
            if (new File(propertiesFileLocation).exists()) {
                reader = new FileReader(propertiesFileLocation);
                allUsers = gson.fromJson(reader, User[].class);

                validateProperties(allUsers, propertiesFileLocation);

                return Arrays.asList(allUsers);
            } else {
                throw new ApplicationException(
                        String.format("Users properties file %s does not exist", propertiesFileLocation));
            }
        } catch (IOException e) {
            log.error("Exception when reading file", e);
            throw new ApplicationException(String.format(
                    "Failure when deserializing data from users properties file %s", propertiesFileLocation), e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                throw new ApplicationException(
                        String.format("Failure when closing file when deserializing data from users properties file %s",
                                propertiesFileLocation),
                        e);
            }
        }
    }

    private static void validateProperties(User[] allUsers, String propertiesFileLocation) {
        List<User> usersInError = new ArrayList<>();
        for (User user : allUsers) {
            if (Validation.empty(user.dataStorageFile) || user.daysToSendOn().isEmpty()
                    || Validation.empty(user.email) || user.sites == null
                    || user.sites.length == 0) {
                usersInError.add(user);
            }

        }

        if (!usersInError.isEmpty()) {
            throw new ApplicationException(String.format(
                    "One or more of the required users properties are missing from file %s, faulty properties entries: %s",
                    propertiesFileLocation, usersInError.toString()));
        }
    }

    @Override
    public String toString() {
        return "Users [dataStorageFile=" + dataStorageFile + ", daysToSendOn=" + daysToSendOn + ", email="
                + email + ", sites=" + Arrays.toString(sites) + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataStorageFile == null) ? 0 : dataStorageFile.hashCode());
        result = prime * result + ((daysToSendOn == null) ? 0 : daysToSendOn.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + Arrays.hashCode(sites);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (dataStorageFile == null) {
            if (other.dataStorageFile != null)
                return false;
        } else if (!dataStorageFile.equals(other.dataStorageFile))
            return false;
        if (daysToSendOn == null) {
            if (other.daysToSendOn != null)
                return false;
        } else if (!daysToSendOn.equals(other.daysToSendOn))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (!Arrays.equals(sites, other.sites))
            return false;
        return true;
    }
}
