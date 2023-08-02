package org.practice.filerepository;

import org.practice.model.Souvenir;
import org.practice.utils.Utils;

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
    Souvenir fromString(String line) {
        return new Souvenir().fromCsvString(line);
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
