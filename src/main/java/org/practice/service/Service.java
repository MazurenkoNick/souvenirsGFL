package org.practice.service;

import java.util.List;

public interface Service<T> {

    T save(T entity);
    boolean update(T entity);
    T read(long id);
    List<T> readAll();
    T delete(long id);
}
