package eu.adainius.newsfocused.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import com.google.gson.Gson;

import eu.adainius.newsfocused.ApplicationException;
import eu.adainius.newsfocused.headline.Headlines;
import eu.adainius.newsfocused.util.Today;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileBasedNewsRepository implements NewsRepository {

    final Gson gson = new Gson();
    final String repoFolder;

    final static String FILE_STORAGE_PREFIX = "headlines_running_week___";

    public FileBasedNewsRepository(String repoFolder) {
        this.repoFolder = repoFolder;
    }

    @Override
    public Headlines getRunningWeekFor(String email) {
        FileReader reader = null;
        String userFilename = FileBasedNewsRepository.emailToFilename(email);
        String fullRepoPath = Paths.get(this.repoFolder, userFilename).toAbsolutePath().toString();
        try {
            Headlines headlines = null;

            if (new File(fullRepoPath).exists()) {
                reader = new FileReader(fullRepoPath);
                headlines = gson.fromJson(reader, Headlines.class);
            }

            return headlines != null ? headlines : new Headlines();
        } catch (IOException e) {
            log.error("Exception when reading file", e);
            throw new ApplicationException(String.format("Failure when deserializing data from file %s", fullRepoPath),
                    e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                throw new ApplicationException(
                        String.format("Failure when deserializing data from file %s", fullRepoPath), e);
            }
        }
    }

    @Override
    public void saveRunningWeekFor(Headlines headlines, String email) {
        FileWriter writer = null;
        String userFilename = FileBasedNewsRepository.emailToFilename(email);
        String fullRepoPath = Paths.get(this.repoFolder, userFilename).toAbsolutePath().toString();
        try {
            writer = new FileWriter(fullRepoPath);
            gson.toJson(headlines, writer);
            writer.flush();
        } catch (IOException e) {
            log.error("Exception when writing file", e);
            throw new ApplicationException(String.format("Failure when serializing data to file %s", fullRepoPath), e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                throw new ApplicationException(String.format("Failure when serializing data to file %s", fullRepoPath),
                        e);
            }
        }
    }

    @Override
    public void resetRunningWeekFor(String email) {
        // "archive" the old file
        // TODO - zip the file (or zip with a bash)
        String userFilename = FileBasedNewsRepository.emailToFilename(email);
        String fullRepoPath = Paths.get(this.repoFolder, userFilename).toAbsolutePath().toString();

        File oldHeadlinesFile = new File(
                String.format("%s_%s.json", removeFileExtension(fullRepoPath), Today.getDateString()));

        File currentFile = new File(fullRepoPath);
        currentFile.renameTo(oldHeadlinesFile);
    }

    private String removeFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return filename;
        }

        String extPattern = "(?<!^)[.][^.]*$";
        return filename.replaceAll(extPattern, "");
    }

    public static String emailToFilename(String userEmail) {
        return FILE_STORAGE_PREFIX + userEmail.replace("@", "__at__") + ".json";
    }

}
