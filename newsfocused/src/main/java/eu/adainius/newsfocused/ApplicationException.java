package eu.adainius.newsfocused;

public class ApplicationException extends RuntimeException {
    private static final long serialVersionUID = 14654684L;

    ApplicationException(Exception e) {
        super("news-focused has encountered an unrecoverable error", e);
    }

    ApplicationException(String errorMessage, Exception e) {
        super("news-focused has encountered an unrecoverable error: " + errorMessage, e);
    }

}
