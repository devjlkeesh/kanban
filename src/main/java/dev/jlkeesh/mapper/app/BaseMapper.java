package dev.jlkeesh.mapper.app;

import dev.jlkeesh.domain.BaseDomain;
import dev.jlkeesh.dto.Dto;

import java.util.List;

public interface BaseMapper<E extends BaseDomain, D extends Dto, CD extends Dto, UD extends Dto> {

    D toDto(E domain);

    List<D> toDto(List<E> domains);

    E fromCreateDto(CD dto);

    E fromUpdateDto(UD dto, E domain);

}
