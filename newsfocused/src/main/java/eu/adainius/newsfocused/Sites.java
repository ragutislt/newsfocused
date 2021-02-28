package eu.adainius.newsfocused;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class Sites {
    private List<String> sites;

    public Sites(String sitesFile) throws IOException {
        Path path = Paths.get(sitesFile);
        sites = Files.readAllLines(path);
    }

    public List<String> list() {
        return Collections.unmodifiableList(sites);
    }

}
