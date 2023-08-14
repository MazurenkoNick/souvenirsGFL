package org.practice.filerepository;

import org.practice.model.Souvenir;
import org.practice.utils.Utils;

import java.text.SimpleDateFormat;

public class SouvenirFileRepository extends AbstractFileRepository<Souvenir> {

    private static SouvenirFileRepository INSTANCE = getInstance();
    private static final String PATH = "src/main/resources/souvenirs.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

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
        return new Souvenir().fromCsvString(line, Souvenir.class);
    }

    @Override
    String toString(Souvenir souvenir) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return String.format("%d, %s, %s, %f, %d",
                souvenir.getId(), souvenir.getName(), formatter.format(souvenir.getManufacturingDate()),
                souvenir.getPrice(), souvenir.getProducerId()
        );
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
