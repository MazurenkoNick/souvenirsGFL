package org.practice.service;

import org.practice.filerepository.FileRepository;
import org.practice.filerepository.SouvenirFileRepository;
import org.practice.model.Souvenir;

import java.util.List;
import java.util.function.Predicate;

public class SouvenirService implements Service<Souvenir> {

    private static SouvenirService INSTANCE = getInstance();
    private final FileRepository<Souvenir> fileRepository;

    private SouvenirService(FileRepository<Souvenir> fileRepository) {
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
    public Souvenir save(Souvenir souvenir) {
        return fileRepository.add(souvenir);
    }

    @Override
    public boolean update(Souvenir souvenir) {
        return fileRepository.update(souvenir);
    }

    @Override
    public Souvenir read(Long id) {
        return fileRepository.read(id);
    }

    @Override
    public List<Souvenir> readAll() {
        return fileRepository.readAll();
    }

    @Override
    public List<Souvenir> readAll(Predicate<Souvenir> predicate) {
        return fileRepository.readAll(predicate);
    }

    @Override
    public boolean delete(Long id) {
        return fileRepository.delete(id);
    }
}
