package dev.jlkeesh.service;

import dev.jlkeesh.dto.Dto;
import dev.jlkeesh.criteria.GenericCriteria;

import java.io.Serializable;
import java.util.List;

public interface GenericCrudService<
        D extends Dto,
        CD extends Dto,
        UD extends Dto,
        C extends GenericCriteria,
        ID extends Serializable> {
    List<D> getAll(C criteria);

    D get(ID id);

    ID create(CD dto);

    boolean update(ID id, UD dto);

    boolean deleteById(ID id);
}
