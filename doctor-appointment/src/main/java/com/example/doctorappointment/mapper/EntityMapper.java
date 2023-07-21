package com.example.doctorappointment.mapper;

import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

public interface EntityMapper<T,D>{
    T toEntity(D dto);
    D toDTO(T entity);

    List<T> toEntityList(List<D> dto);
    List<D> toDTOList(List<T> entity);

    @Named("partialUpdate")
    void partialUpdate(@MappingTarget T entity, D dto);
}
