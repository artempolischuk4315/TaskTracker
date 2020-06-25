package com.ua.polishchuk.service.mapper;

public interface EntityMapper <E, D> {

    D mapEntityToDto(E entity);

    E mapDtoToEntity(D dto);
}
