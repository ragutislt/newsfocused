package eu.adainius.newsfocused.user;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.google.gson.Gson;

import org.junit.jupiter.api.Test;

import eu.adainius.newsfocused.ApplicationException;

public class UsersPropertiesTest {
    private final Gson gson = new Gson();

    @Test
    public void readsPropertiesGivenFileAndReturnsInstance() {
        String fileLocation = "src/test/resources/usersProperties.json";

        List<UsersProperties> usersPropertiesList = UsersProperties.parseFromUsing(fileLocation, gson);
        UsersProperties usersProperties = usersPropertiesList.get(0);

        assertEquals("headlines_save_running_week_some-email.com.json", usersProperties.dataStorageFile());
        assertEquals("some@email.com", usersProperties.email());
        assertEquals("Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday", usersProperties.daysToSendOn());
        assertEquals(List.of("www.bbc.com", "www.lrt.lt"), usersProperties.sites());
    }

    @Test
    public void failsIfPropertyFileNotFound() {
        String fileLocation = "src/test/resources/non_existing_file.json";

        ApplicationException e = assertThrows(ApplicationException.class, () -> {
            UsersProperties.parseFromUsing(fileLocation, gson);
        });
        assertTrue(e.getMessage().contains("does not exist"));
    }

    @Test
    public void failsIfNotAllPropertiesAreFound() {
        String fileLocation = "src/test/resources/usersProperties_incomplete.json";

        ApplicationException e = assertThrows(ApplicationException.class, () -> {
            UsersProperties.parseFromUsing(fileLocation, gson);
        });
        assertTrue(e.getMessage().contains("required users properties are missing"));
        assertTrue(e.getMessage().contains("some@email_incomplete_entry.com"));
    }
}
