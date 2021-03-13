package eu.adainius.newsfocused.e2e;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import eu.adainius.newsfocused.App;

public class BBCe2eTest {
    private static Process mailServer;
    private static String mailServerName = "mailcatcher.bat";

    @BeforeAll
    public static void runMailServer() throws IOException {
        Runtime rt = Runtime.getRuntime();
        mailServer = rt.exec(mailServerName);
        System.out.println("Started mail server");
    }

    @AfterAll
    public static void stopMailServer() throws IOException, InterruptedException {
        System.out.println("Stopping mail server");

        // this stops the command line script
        mailServer.getErrorStream().close();
        mailServer.getInputStream().close();
        mailServer.getOutputStream().close();
        mailServer.destroy();

        // this stops the ruby process
        // TODO run a docker image with mailcatcher and kill it here
        Runtime.getRuntime().exec("taskkill /F /IM ruby.exe");
    }

    @Test
    public void parses_headlines_from_BBC_and_sends_email() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        String mailServerUrl = "http://127.0.0.1:1080/";

        runMailServer();

        String siteFile = "/sites.txt";
        String email = "some@email.address";
        App.main(new String[] { siteFile, email });

        // create a request
        var request = HttpRequest.newBuilder(URI.create(mailServerUrl + "messages"))
                .header("accept", "application/json").build();

        // use the client to send the request
        var response = httpClient.send(request, BodyHandlers.ofString());

        JsonArray messageArray = new Gson().fromJson(response.body(), JsonArray.class);

        assertTrue(messageArray.size() > 0);
    }

}
