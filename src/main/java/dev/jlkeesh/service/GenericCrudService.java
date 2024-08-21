package dev.jlkeesh.service;

import dev.jlkeesh.criteria.GenericCriteria;
import dev.jlkeesh.dto.Dto;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * @param <D>
 * @param <CD>
 * @param <UD>
 * @param <C>
 * @param <ID>
 */
public interface GenericCrudService<
        D extends Dto,
        CD extends Dto,
        UD extends Dto,
        C extends GenericCriteria,
        ID extends Serializable> {
    List<D> getAll(@NonNull C criteria);

    D get(@NonNull ID id);

    ID create(@NonNull CD dto);

    boolean update(@NonNull ID id, @NonNull UD dto);

    boolean deleteById(@NonNull ID id);
}
