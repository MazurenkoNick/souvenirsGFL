package org.practice.service;

import org.practice.filerepository.FileRepository;
import org.practice.filerepository.SouvenirFileRepository;
import org.practice.model.Producer;
import org.practice.model.Souvenir;

import java.util.List;
import java.util.function.Predicate;

public class SouvenirService implements Service<Souvenir> {

    private static SouvenirService INSTANCE = getInstance();
    private final FileRepository<Souvenir> fileRepository;
    private Service<Producer> producerService;

    private SouvenirService() {
        this.fileRepository = SouvenirFileRepository.getInstance();
    }

    public static SouvenirService getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (SouvenirService.class) {
            if (INSTANCE == null) {
                INSTANCE = new SouvenirService();
                INSTANCE.setProducerService(ProducerService.getInstance());
            }
        }
        return INSTANCE;
    }

    private void setProducerService(ProducerService producerService) {
        this.producerService = producerService;
    }

    public List<Souvenir> readAllByProducerId(long id) {
        return readAll(souvenir -> souvenir.getProducerId().equals(id));
    }

    public List<Souvenir> readAllByCountry(String country) {
        List<Long> producerIds = producerService
                .readAll(producer -> producer.getCountry().equals(country))
                .stream().map(Producer::getId)
                .toList();

        return readAll(souvenir -> producerIds.contains(souvenir.getProducerId()));
    }

    public List<Souvenir> readAllByYear(int year) {
        return readAll(souvenir -> souvenir.getManufacturingYear() == year);
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

    @Override
    public boolean delete(Predicate<Souvenir> predicate) {
        return fileRepository.delete(predicate);
    }
}
