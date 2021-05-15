package eu.adainius.newsfocused.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import eu.adainius.newsfocused.ApplicationException;
import eu.adainius.newsfocused.headline.Headlines;
import eu.adainius.newsfocused.util.Today;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileBasedNewsRepository implements NewsRepository {

    final Gson gson = new Gson();
    final String repoFile;

    public FileBasedNewsRepository(String repoFile) {
        this.repoFile = repoFile;
    }

    @Override
    public Headlines getRunningWeek() {
        FileReader reader = null;
        try {
            Headlines headlines = null;

            if (new File(repoFile).exists()) {
                reader = new FileReader(repoFile);
                headlines = gson.fromJson(reader, Headlines.class);
            }

            return headlines != null ? headlines : new Headlines();
        } catch (IOException e) {
            log.error("Exception when reading file", e);
            throw new ApplicationException(String.format("Failure when deserializing data from file %s", this.repoFile),
                    e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                throw new ApplicationException(
                        String.format("Failure when deserializing data from file %s", this.repoFile), e);
            }
        }
    }

    @Override
    public void saveRunningWeek(Headlines headlines) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(repoFile);
            gson.toJson(headlines, writer);
            writer.flush();
        } catch (IOException e) {
            log.error("Exception when writing file", e);
            throw new ApplicationException(String.format("Failure when serializing data to file %s", this.repoFile), e);
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                throw new ApplicationException(
                        String.format("Failure when deserializing data from file %s", this.repoFile), e);
            }
        }
    }

    @Override
    public void resetRunningWeek() {
        // "archive" the old file
        File oldHeadlinesFile = new File(
                String.format("%s_%s.json", removeFileExtension(repoFile), Today.getDateString()));

        File currentFile = new File(repoFile);
        currentFile.renameTo(oldHeadlinesFile);
    }

    private String removeFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return filename;
        }

        String extPattern = "(?<!^)[.][^.]*$";
        return filename.replaceAll(extPattern, "");
    }

}
