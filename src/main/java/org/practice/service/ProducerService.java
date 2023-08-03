package org.practice.service;

import org.practice.filerepository.FileRepository;
import org.practice.filerepository.ProducerFileRepository;
import org.practice.model.Producer;
import org.practice.model.Souvenir;

import java.util.List;
import java.util.function.Predicate;

public class ProducerService implements Service<Producer> {

    private static ProducerService INSTANCE = getInstance();
    private final FileRepository<Producer> fileRepository;
    private SouvenirService souvenirService;

    private ProducerService() {
        this.fileRepository = ProducerFileRepository.getInstance();
    }

    public static ProducerService getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (ProducerService.class) {
            if (INSTANCE == null) {
                INSTANCE = new ProducerService();
                INSTANCE.setSouvenirService(SouvenirService.getInstance());
            }
        }
        return INSTANCE;
    }

    private void setSouvenirService(SouvenirService souvenirService) {
        this.souvenirService = souvenirService;
    }

    public List<Producer> readProducersWithPricesLessThan(double price) {
        List<Long> producersId = souvenirService.readAll(souvenir -> souvenir.getPrice() < price)
                .stream()
                .map(Souvenir::getProducerId)
                .distinct()
                .toList();

        return readAll(producer -> producersId.contains(producer.getId()));
    }

    public List<Producer> readProductsWithYear(int year) {
        List<Long> producersId = souvenirService.readAllByYear(year).stream()
                .map(Souvenir::getProducerId)
                .distinct()
                .toList();

        return readAll(producer -> producersId.contains(producer.getId()));
    }

    public boolean deleteProductAndSouvenirs(long id) {
        boolean productDeleted = delete(id);
        boolean souvenirsDeleted = souvenirService.delete(souvenir -> souvenir.getProducerId().equals(id));
        return productDeleted && souvenirsDeleted;
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
    public Producer read(Long id) {
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
    public boolean delete(Long id) {
        return fileRepository.delete(id);
    }
    @Override
    public boolean delete(Predicate<Producer> predicate) {
        return fileRepository.delete(predicate);
    }
}
