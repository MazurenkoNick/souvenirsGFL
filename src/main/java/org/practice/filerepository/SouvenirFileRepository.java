package org.practice.filerepository;

import lombok.SneakyThrows;
import org.practice.model.Souvenir;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SouvenirFileRepository {

    private static SouvenirFileRepository INSTANCE = getInstance();
    private static final String PATH = "src/main/resources/souvenirs.csv";

    @SneakyThrows
    private SouvenirFileRepository() {
        Path path = Paths.get(PATH);
    }

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
        long possibleId = generateSouvenirId();

        // if id is free, save a new souvenir to the file
        while (!isFreeId(possibleId)) {
            possibleId = generateSouvenirId();
        }
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(PATH, true))) {

            souvenir.setId(possibleId);
            writer.newLine();
            writer.write(souvenir.toString());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return souvenir;
    }

    private boolean isFreeId(long id) {
        return readAll(s -> s.getId() == id).isEmpty();
    }

    private List<Souvenir> souvenirList() {
        try (BufferedReader reader = Files.newBufferedReader(
                Paths.get(PATH), StandardCharsets.UTF_8)) {

            return reader.lines()
                    .skip(1)
                    .map(Souvenir::fromString)
                    .collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private  long generateSouvenirId() {
        UUID uuid = UUID.randomUUID();
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();

        // XOR the most and least significant bits to generate a long ID
        long souvenirId = mostSigBits ^ leastSigBits;

        // Ensure the ID is positive (as XOR might produce a negative value)
        if (souvenirId < 0) {
            souvenirId = -souvenirId;
        }

        return souvenirId;
    }
}
