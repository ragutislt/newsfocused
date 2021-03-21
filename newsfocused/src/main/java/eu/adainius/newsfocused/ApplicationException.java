package eu.adainius.newsfocused;

public class ApplicationException extends RuntimeException {
    private static final long serialVersionUID = 14654684L;

    public ApplicationException(Exception e) {
        super("news-focused has encountered an unrecoverable error", e);
    }

    public ApplicationException(String errorMessage, Exception e) {
        super("news-focused has encountered an unrecoverable error: " + errorMessage, e);
    }

    public ApplicationException(String errorMessage) {
        super("news-focused has encountered an unrecoverable error: " + errorMessage);
    }

}
