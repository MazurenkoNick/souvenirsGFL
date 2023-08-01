package org.practice.service;

import org.practice.filerepository.ProducerFileRepository;
import org.practice.filerepository.SouvenirFileRepository;
import org.practice.model.Producer;

import java.util.List;
import java.util.function.Predicate;

public class ProducerService implements Service<Producer> {

    private static ProducerService INSTANCE = getInstance();
    private final ProducerFileRepository fileRepository;

    private ProducerService(ProducerFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public static ProducerService getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (ProducerService.class) {
            if (INSTANCE == null) {
                INSTANCE = new ProducerService();
            }
        }
        return INSTANCE;
    }

    @Override
    public Producer save(Producer producer) {
        return fileRepository.add(producer);
    }

    @Override
    public boolean update(Producer producer) {
        return fileRepository.update(producer);
    }

    @Override
    public Producer read(long id) {
        return fileRepository.read(id);
    }

    @Override
    public List<Producer> readAll() {
        return fileRepository.readAll();
    }

    @Override
    public List<Producer> readAll(Predicate<Producer> predicate) {
        return fileRepository.readAll(predicate);
    }

    @Override
    public boolean delete(long id) {
        return fileRepository.delete(id);
    }
}
