package org.practice.filerepository;

import org.practice.model.Producer;
import org.practice.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ProducerFileRepository extends AbstractFileRepository<Producer> {

    private static ProducerFileRepository INSTANCE = getInstance();
    private static final String PATH = "src/main/resources/producers.csv";

    public static ProducerFileRepository getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (ProducerFileRepository.class) {
            if (INSTANCE == null) {
                INSTANCE = new ProducerFileRepository();
            }
        }
        return INSTANCE;
    }

    @Override
    String filePropertiesHeader() {
        return "id,name,country,details";
    }

    @Override
    List<Producer> entityList() {
        try (BufferedReader reader = Files.newBufferedReader(
                Paths.get(PATH), StandardCharsets.UTF_8)) {

            return reader.lines()
                    .skip(1)
                    .map(string -> new Producer().fromString(string))
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
