package com.beyond.bycontract.company.infrastructure.mapper;

import com.beyond.bycontract.company.domain.model.ParticularStakeholder;
import com.beyond.bycontract.company.infrastructure.entity.ParticularStakeholderEntity;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;

public class ParticularStakeholderPersistenceMapper {

    // Constructeur privé pour empêcher l'instanciation de cette classe utilitaire
    private ParticularStakeholderPersistenceMapper() {
    }

    public static ParticularStakeholderEntity toEntity(ParticularStakeholder stakeholder) {
        ParticularStakeholderEntity entity = new ParticularStakeholderEntity();

        entity.setFirstName(stakeholder.getFirstName());
        entity.setLastName(stakeholder.getLastName());
        entity.setEmail(stakeholder.getEmail());
        entity.setPhone(stakeholder.getPhone());
        entity.setAddress(stakeholder.getAddress());
        if(stakeholder.getIdCreator() != null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(stakeholder.getIdCreator());
            entity.setCreator(userEntity);
        }
        // id, createdAt et modifiedAt ne sont pas ajoutés ici car ils sont générés automatiquement par JPA/Hibernate.

        return entity;
    }

    public static ParticularStakeholder reconstituteDomain(ParticularStakeholderEntity entity) {
        if (entity == null) {
            return null;
        }

        try {
            return new ParticularStakeholder(
                    entity.getId(),
                    entity.getCreator().getId(),
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getEmail(),
                    entity.getPhone(),
                    entity.getAddress(),
                    entity.getCreatedAt(),
                    entity.getModifiedAt()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erreur de mapping BDD -> Domaine pour ParticularStakeholder", e);
        }
    }

}
