package org.practice.service;

import org.practice.filerepository.SouvenirFileRepository;
import org.practice.model.Souvenir;

import java.util.List;
import java.util.function.Predicate;

public class SouvenirService implements Service<Souvenir> {

    private static SouvenirService INSTANCE = getInstance();
    private final SouvenirFileRepository fileRepository;

    private SouvenirService(SouvenirFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public static SouvenirService getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (SouvenirService.class) {
            if (INSTANCE == null) {
                INSTANCE = new SouvenirService(SouvenirFileRepository.getInstance());
            }
        }
        return INSTANCE;
    }

    @Override
    public Souvenir save(Souvenir entity) {
        // TODO: 7/30/2023 implement save functionality
        return null;
    }

    @Override
    public boolean update(Souvenir entity) {
        // TODO: 7/30/2023 implement update functionality
        return false;
    }

    @Override
    public Souvenir read(long id) {
        // TODO: 7/30/2023 implement read functionality
        return null;
    }

    @Override
    public List<Souvenir> readAll() {
        return fileRepository.readAll();
    }

    @Override
    public List<Souvenir> readAll(Predicate<Souvenir> predicate) {
        return null;
    }

    @Override
    public Souvenir delete(long id) {
        // TODO: 7/30/2023 implement delete functionality
        return null;
    }
}
