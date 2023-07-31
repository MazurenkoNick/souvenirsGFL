package org.practice.filerepository;

import lombok.SneakyThrows;
import org.practice.model.Souvenir;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SouvenirFileRepository {

    private static SouvenirFileRepository INSTANCE = getInstance();
    private static final String PATH = "souvenirs.csv";
    private final BufferedReader reader;

    @SneakyThrows
    private SouvenirFileRepository() {
        Path path = Paths.get(PATH);
        this.reader = Files.newBufferedReader(
                path, StandardCharsets.UTF_8);
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
        return souvenirStream()
                .collect(Collectors.toList());
    }

    public List<Souvenir> readAll(Predicate<Souvenir> predicate) {
        return souvenirStream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    private Stream<Souvenir> souvenirStream() {
        return reader.lines()
                .skip(1)
                .map(Souvenir::fromString);
    }
}
