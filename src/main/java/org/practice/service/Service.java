package org.practice.service;

public interface Service<T> {

    boolean save(T entity);
    boolean update(T entity);
    T read(long id);
    T delete(long id);
}
