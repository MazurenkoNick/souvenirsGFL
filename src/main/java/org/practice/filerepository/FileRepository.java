package org.practice.filerepository;

import java.util.List;
import java.util.function.Predicate;

public interface FileRepository<T> {

    List<T> readAll();
    List<T> readAll(Predicate<T> predicate);
    T read(long id);
    T add(T entity);
    boolean update(T entity);
    boolean delete(long id);
    boolean replaceAll(List<T> entities);
}
