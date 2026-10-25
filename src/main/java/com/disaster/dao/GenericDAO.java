package com.disaster.dao;

import java.util.List;
import java.util.Optional;

/**
 * Generic Data Access Object interface demonstrating ABSTRACTION and GENERICS.
 *
 * @param <T> Domain model entity type
 * @param <ID> Primary key identifier type
 */
public interface GenericDAO<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    T save(T entity);
    boolean update(T entity);
    boolean deleteById(ID id);
}

