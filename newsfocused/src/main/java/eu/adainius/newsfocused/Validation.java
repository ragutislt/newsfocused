package eu.adainius.newsfocused;

public class Validation {
    public static boolean empty(final String s) {
        // Null-safe, short-circuit evaluation.
        return s == null || s.trim().isEmpty();
    }
}
