package eu.adainius.newsfocused.user;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.google.gson.Gson;

import org.junit.jupiter.api.Test;

import eu.adainius.newsfocused.ApplicationException;

public class UserTest {
    private final Gson gson = new Gson();

    @Test
    public void reads_properties_given_file_and_returns_instance() {
        String fileLocation = "src/test/resources/users.json";

        List<User> usersList = User.parseFromUsing(fileLocation, gson);
        User users = usersList.get(0);

        assertEquals("headlines_save_running_week_some-email.com.json", users.dataStorageFile());
        assertEquals("some@email.com", users.email());
        assertEquals(List.of("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"), users.daysToSendOn());
        assertEquals(List.of("www.bbc.com", "www.lrt.lt"), users.sites());
    }

    @Test
    public void fails_if_property_file_not_found() {
        String fileLocation = "src/test/resources/non_existing_file.json";

        ApplicationException e = assertThrows(ApplicationException.class, () -> {
            User.parseFromUsing(fileLocation, gson);
        });
        assertTrue(e.getMessage().contains("does not exist"));
    }

    @Test
    public void fails_if_not_all_properties_are_found() {
        String fileLocation = "src/test/resources/users_incomplete.json";

        ApplicationException e = assertThrows(ApplicationException.class, () -> {
            User.parseFromUsing(fileLocation, gson);
        });
        assertTrue(e.getMessage().contains("required users properties are missing"));
        assertTrue(e.getMessage().contains("some@email_incomplete_entry.com"));
    }
}
