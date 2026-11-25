package com.beyond.bycontract.auth.infrastructure.repository;

import com.beyond.bycontract.auth.domain.model.PendingRegistration;
import com.beyond.bycontract.auth.domain.repository.PendingRegistrationRepository;
import com.beyond.bycontract.auth.infrastructure.entity.PendingRegistrationEntity;
import com.beyond.bycontract.auth.infrastructure.mapper.PendingRegistrationPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PendingRegistrationRepositoryAdapter implements PendingRegistrationRepository {

    private final JpaPendingRegistrationRepository repository;
    private final PendingRegistrationPersistenceMapper mapper;

    @Override
    public PendingRegistration save(PendingRegistration domain) {
        PendingRegistrationEntity entity;

        if (domain.getId() == null) {
            entity = mapper.domainToEntity(domain);
        } else {
            entity = repository.findById(domain.getId())
                    .orElseThrow(() -> new IllegalStateException("PendingRegistration introuvable pour l'id : " + domain.getId()));
            mapper.updateEntityFromDomain(domain, entity);
        }

        PendingRegistrationEntity savedEntity = repository.save(entity);
        return mapper.entityToDomain(savedEntity);
    }

    @Override
    public Optional<PendingRegistration> getByEmail(String email) {
        return repository.findByEmail(email).map(mapper::entityToDomain) ;
    }

    @Override
    public void deleteByEmail(String email) {
        repository.deleteByEmail(email);
    }

    @Override
    public void deleteAllByExpiresAtBefore(LocalDateTime now) {
        repository.deleteAllByExpiresAtBefore(now);
    }
}
