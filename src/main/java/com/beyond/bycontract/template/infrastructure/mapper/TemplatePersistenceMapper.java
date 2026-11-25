package com.beyond.bycontract.template.infrastructure.mapper;

import com.beyond.bycontract.template.domain.model.Template;
import com.beyond.bycontract.template.infrastructure.entity.TemplateEntity;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import com.beyond.bycontract.user.infrastructure.repository.SpringDataUserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class TemplatePersistenceMapper {

    @Autowired
    protected SpringDataUserRepository springDataUserRepository;

    @Mapping(source = "user.id", target = "idAuthor")
    public abstract  Template entityToDomain(TemplateEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(source="idAuthor", target="user")
    public abstract TemplateEntity domainToEntity(Template domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(source="idAuthor", target="user")
    public abstract  void updateEntityFromDomain(Template domain, @MappingTarget TemplateEntity entityToUpdate);

    protected UserEntity idToUserEntity(UUID idAuthor) {
        if (idAuthor == null) {
            return null;
        }
        return springDataUserRepository.getReferenceById(idAuthor);
    }
}
