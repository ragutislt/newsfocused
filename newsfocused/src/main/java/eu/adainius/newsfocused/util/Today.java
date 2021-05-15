package eu.adainius.newsfocused.util;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class Today {
    public static boolean is(String day) {
        return LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US).equals(day);
    }

    public static boolean isOneOf(List<String> days) {
        for (String day : days) {
            if (Today.is(day)) {
                return true;
            }
        }
        return false;
    }
}
