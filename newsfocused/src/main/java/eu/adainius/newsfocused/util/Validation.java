package eu.adainius.newsfocused.util;

public class Validation {
    public static boolean empty(final String s) {
        return s == null || s.trim().isEmpty();
    }
}
