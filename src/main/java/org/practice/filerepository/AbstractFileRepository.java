package org.practice.filerepository;

import org.practice.model.Entity;

import java.io.*;
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
     * This method will be used to convert a row from the file to the entity.
     * E.g. if it's a csv line, you need to implement this method to retrieve all the
     * properties and set it into {@link T} entity, then return it.
     *
     * @param line is used to be converted to the {@link T} entity
     * @return T entity that will be returned after conversion from string line
     */
    abstract T fromString(String line);

    /**
     * @return first line of the file which should represent properties of the current entity.
     * By default, there is no properties header in the file
     */
    String filePropertiesHeader() {
        return null;
    };

    /**
     *
     * @return {@link List<T>} - all objects which were found in the file repository
     */
    @Override
    public List<T> readAll() {
        return entityList();
    }

    /**
     * @param predicate will determine what entity must be returned
     * @return {@link List<T>} - objects which were found in the file repository using the predicate
     */
    @Override
    public List<T> readAll(Predicate<T> predicate) {
        return entityList()
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * @param id the value which will be used during searching the {@link T} object in the file
     * @return null if the entity was not found, instance of the {@link T} from the file object otherwise
     */
    @Override
    public T read(long id) {
        List<T> entityList = readAll(s -> s.getId() == id);
        if (entityList.isEmpty()) {
            return null;
        }
        return entityList.get(0);
    }

    /**
     * @param entity which must be inserted in the file.
     * @return persisted entity with id
     */
    @Override
    public T add(T entity) {
        long id = generateUniqueId();

        try (PrintWriter writer = new PrintWriter(new FileWriter(getFilePath(), true));
             BufferedReader reader = new BufferedReader(new FileReader(getFilePath()))) {

            String lastLine = getLastLine(reader);
            // if last line is not empty - create a new empty line, where we will write new entity
            if (!lastLine.isBlank()) {
                writer.println();
            }
            entity.setId(id);
            writer.print(entity.format());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    /**
     * @param entity which must be updated.
     * @return false if id of the {@param entity} is null or is not in the file.
     * true if the updated is ok
     */
    @Override
    public boolean update(T entity) {
        Long id = entity.getId();

        // if id is free, there is no such entity in the file
        if (id == null || isFreeId(id)) {
            return false;
        }
        List<T> entities = readAll().stream()
                .map(e -> {
                    if (Objects.equals(e.getId(), id)) {
                        return entity;
                    } return e;
                })
                .toList();

        return replaceAll(entities);
    }

    /**
     * @param predicate will determine what entity must be deleted
     * @return true if everything was deleted properly, false otherwise
     */
    @Override
    public boolean delete(Predicate<T> predicate) {
        List<T> entities = readAll().stream()
                .filter(predicate.negate())
                .toList();

        return replaceAll(entities);
    }

    @Override
    public boolean delete(Long id) {
        // if id is free, there is no such entity in the file
        if (id == null || isFreeId(id)) {
            return false;
        }
        return delete(entity -> Objects.equals(entity.getId(), id));
    }

    /**
     * @param entities will be inserted in the file, rewriting all its content
     * @return true if everything was replaced properly, false otherwise
     */
    @Override
    public boolean replaceAll(List<T> entities) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(getFilePath()))) {
            if (filePropertiesHeader() != null) {
                writer.println(filePropertiesHeader());
            }
            for (T entity : entities) {
                writer.println(entity.format());
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
                    .skip(filePropertiesHeader() == null ? 0 : 1)
                    .filter(s -> !s.isBlank())
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

    private String getLastLine(BufferedReader reader) throws IOException {
        String lastLine = "";
        String currentLine;

        while ((currentLine = reader.readLine()) != null) {
            lastLine = currentLine;
        }
        return lastLine;
    }
}
