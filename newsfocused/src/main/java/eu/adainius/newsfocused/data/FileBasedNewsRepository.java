package eu.adainius.newsfocused.data;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import eu.adainius.newsfocused.ApplicationException;
import eu.adainius.newsfocused.headline.Headlines;

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
            reader = new FileReader(repoFile);
            return gson.fromJson(reader, Headlines.class);
        } catch (IOException e) {
            throw new ApplicationException(String.format("Failure when deserializing data from file %s", this.repoFile),
                    e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
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
            throw new ApplicationException(String.format("Failure when serializing data to file %s", this.repoFile), e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                throw new ApplicationException(
                        String.format("Failure when deserializing data from file %s", this.repoFile), e);
            }
        }
    }

}
