package org.practice.filerepository;

import org.practice.model.Producer;
import org.practice.utils.Utils;

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

//    @Override
//    String filePropertiesHeader() {
//        return "id,name,country,details";
//    }

    @Override
    Producer fromString(String line) {
        return new Producer().fromCsvString(line);
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
