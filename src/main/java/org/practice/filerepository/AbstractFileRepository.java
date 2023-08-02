package org.practice.filerepository;

import org.practice.model.Entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractFileRepository<T extends Entity> implements FileRepository<T> {

    /**
     * @return Path to the file where the entities will be stored
     */
    abstract String getFilePath();

    /**
     * @return unique id for the {@link T} entity
     */
    abstract long generateUniqueId();

    /**
     * @return first line of the file that will be used during rewriting the content of the file
     */
    abstract String filePropertiesHeader();

    /**
     * This method will be used to convert a row from the file to the entity.
     * E.g. if it's a csv line, you need to implement this method to retrieve all the
     * properties and set it into {@link T} entity, then return it.
     *
     * @param line is used to be converted to the {@link T} entity
     * @return T entity that will be returned after conversion from string line
     */
    abstract T fromString(String line);

    public List<T> readAll() {
        return entityList();
    }

    public List<T> readAll(Predicate<T> predicate) {
        return entityList()
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public T read(long id) {
        List<T> entityList = readAll(s -> s.getId() == id);
        if (entityList.isEmpty()) {
            return null;
        }
        return entityList.get(0);
    }

    public T add(T entity) {
        long id = generateUniqueId();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFilePath(), true))) {
            entity.setId(id);
            writer.newLine();
            writer.write(entity.format());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    public boolean update(T entity) {
        Long id = entity.getId();

        // if id is free, there is no such entity in the file
        if (id == null || isFreeId(id)) {
            return false;
        }
        List<T> entities = readAll().stream()
                .map(e -> {
                    if (e.getId() == id) {
                        return entity;
                    } return e;
                })
                .toList();

        replaceAll(entities);
        return true;
    }

    public boolean delete(Predicate<T> predicate) {
        List<T> entities = readAll().stream()
                .filter(predicate)
                .toList();

        replaceAll(entities);
        return true;
    }

    public boolean delete(Long id) {
        // if id is free, there is no such entity in the file
        if (id == null || isFreeId(id)) {
            return false;
        }
        return delete(entity -> !Objects.equals(entity.getId(), id));
    }

    public boolean replaceAll(List<T> entities) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFilePath()))) {
            writer.write(filePropertiesHeader());
            for (T entity : entities) {
                writer.newLine();
                writer.write(entity.format());
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Method retrieves all the entities from the file.
     * It skips the first row because it must contain only names of the properties,
     * which are set in the {@link this#filePropertiesHeader()} method.
     *
     * @return List of all the entities retrieved from the file
     */
    List<T> entityList() {
        try (BufferedReader reader = Files.newBufferedReader(
                Paths.get(getFilePath()), StandardCharsets.UTF_8)) {

            return reader.lines()
                    .skip(1)
                    .map(this::fromString)
                    .collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    boolean isFreeId(long id) {
        return readAll(s -> s.getId() == id).isEmpty();
    }
}
