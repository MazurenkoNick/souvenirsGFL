package org.practice;

import org.practice.model.Souvenir;
import org.practice.service.Service;

public class SouvenirService implements Service<Souvenir> {

    private static SouvenirService INSTANCE = getInstance();

    private SouvenirService() {
    }

    public static SouvenirService getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (SouvenirService.class) {
            if (INSTANCE == null) {
                INSTANCE = new SouvenirService();
            }
        }
        return INSTANCE;
    }

    @Override
    public boolean save(Souvenir entity) {
        // TODO: 7/30/2023 implement save functionality
        return false;
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
    public Souvenir delete(long id) {
        // TODO: 7/30/2023 implement delete functionality
        return null;
    }
}
