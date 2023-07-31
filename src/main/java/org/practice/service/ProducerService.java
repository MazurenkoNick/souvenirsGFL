package org.practice.service;

import org.practice.model.Producer;

import java.util.List;
import java.util.function.Predicate;

public class ProducerService implements Service<Producer> {

    private static ProducerService INSTANCE = getInstance();

    private ProducerService() {
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
    public Producer save(Producer entity) {
        return null;
    }

    @Override
    public boolean update(Producer entity) {
        return false;
    }

    @Override
    public Producer read(long id) {
        return null;
    }

    @Override
    public List<Producer> readAll() {
        return null;
    }

    @Override
    public List<Producer> readAll(Predicate<Producer> predicate) {
        return null;
    }

    @Override
    public Producer delete(long id) {
        return null;
    }
}
