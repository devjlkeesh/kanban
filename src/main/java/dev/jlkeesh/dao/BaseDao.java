package dev.jlkeesh.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BaseDao<T, ID> {
    T insert(T t);

    T update(T t);

    void delete(T t);

    void deleteById(ID id);

    Optional<T> findById(ID id);

    List<T> findAll();
}
