package org.practice.service;

import java.util.List;
import java.util.function.Predicate;

public interface Service<T> {

    T save(T entity);
    boolean update(T entity);
    T read(Long id);
    List<T> readAll();
    List<T> readAll(Predicate<T> predicate);
    boolean delete(Long id);
}
