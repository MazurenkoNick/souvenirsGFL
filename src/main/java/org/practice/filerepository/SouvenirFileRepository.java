package org.practice.filerepository;

import org.practice.model.Souvenir;
import org.practice.utils.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SouvenirFileRepository {

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

    public List<Souvenir> readAll() {
        return souvenirList();
    }

    public List<Souvenir> readAll(Predicate<Souvenir> predicate) {
        return souvenirList()
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public Souvenir addSouvenir(Souvenir souvenir) {
        long id = generateUniqueId();

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(PATH, true))) {

            souvenir.setId(id);
            writer.newLine();
            writer.write(souvenir.toString());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return souvenir;
    }

    private long generateUniqueId() {
        long id = Utils.generateId();

        // if id is free, save a new souvenir to the file
        while (!isFreeId(id)) {
            id = Utils.generateId();
        }
        return id;
    }

    private boolean isFreeId(long id) {
        return readAll(s -> s.getId() == id).isEmpty();
    }

    private List<Souvenir> souvenirList() {
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
}
