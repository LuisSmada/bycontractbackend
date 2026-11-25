package com.beyond.bycontract.user.infrastructure.mapper;

import com.beyond.bycontract.user.domain.model.User;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserPersistenceMapper {

    @Mapping(source = "userRole", target = "role")
    User entityToDomain(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(source = "role", target = "userRole")
    UserEntity domainToEntity(User user);

    // 🚨 POUR LA MISE À JOUR (C'est ce qui remplace tes setters)
    // MapStruct va copier les champs de 'user' vers 'entityToUpdate'
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(source = "role", target = "userRole")
    void updateEntityFromDomain(User user, @MappingTarget UserEntity entityToUpdate);
}
