package org.practice.filerepository;

import org.practice.model.Souvenir;
import org.practice.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class SouvenirFileRepository extends AbstractFileRepository<Souvenir> {

    private static SouvenirFileRepository INSTANCE = getInstance();
    private static final String PATH = "src/main/resources/souvenirs.csv";

    public static SouvenirFileRepository getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (SouvenirFileRepository.class) {
            if (INSTANCE == null) {
                INSTANCE = new SouvenirFileRepository();
            }
        }
        return INSTANCE;
    }

    @Override
    String filePropertiesHeader() {
        return "id, name, manufacturing date, price, producerId";
    }

    @Override
    List<Souvenir> entityList() {
        try (BufferedReader reader = Files.newBufferedReader(
                Paths.get(PATH), StandardCharsets.UTF_8)) {

            return reader.lines()
                    .skip(1)
                    .map(string -> new Souvenir().fromString(string))
                    .collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    String getFilePath() {
        return PATH;
    }

    @Override
    long generateUniqueId() {
        long id = Utils.generateId();

        // if id is free, save a new souvenir to the file
        while (!isFreeId(id)) {
            id = Utils.generateId();
        }
        return id;
    }
}
