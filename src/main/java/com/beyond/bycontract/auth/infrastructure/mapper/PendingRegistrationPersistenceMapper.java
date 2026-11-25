package com.beyond.bycontract.auth.infrastructure.mapper;

import com.beyond.bycontract.auth.domain.model.PendingRegistration;
import com.beyond.bycontract.auth.infrastructure.entity.PendingRegistrationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PendingRegistrationPersistenceMapper {

    PendingRegistration entityToDomain(PendingRegistrationEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    PendingRegistrationEntity domainToEntity(PendingRegistration domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDomain(PendingRegistration domain, @MappingTarget PendingRegistrationEntity entityToUpdate);
}