package eu.adainius.newsfocused;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        String sitesFile = args[0];
        String email = args[1];
        System.out.println("Sites will be read from: " + sitesFile);
        System.out.println("News will be sent to: " + email);
    }
}
