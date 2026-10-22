package com.beyond.bycontract.company.infrastructure.repository;

import com.beyond.bycontract.company.domain.model.ParticularStakeholder;
import com.beyond.bycontract.company.domain.repository.ParticularStakeholderRepository;
import com.beyond.bycontract.company.infrastructure.entity.ParticularStakeholderEntity;
import com.beyond.bycontract.company.infrastructure.mapper.ParticularStakeholderPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ParticularStakeholderRepositoryAdapter implements ParticularStakeholderRepository {

    private final JpaParticularStakeholderRepository jpaRepository;

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public ParticularStakeholder create(ParticularStakeholder particularStakeholder) {
        ParticularStakeholderEntity entity = ParticularStakeholderPersistenceMapper.toEntity(particularStakeholder);
        ParticularStakeholderEntity savedEntity = jpaRepository.save(entity);
        return ParticularStakeholderPersistenceMapper.reconstituteDomain(savedEntity);
    }

    @Override
    public Page<ParticularStakeholder> getAllParticularStakeholders(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(ParticularStakeholderPersistenceMapper::reconstituteDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<ParticularStakeholder> getParticularStakeholderById(UUID id) {
        return jpaRepository.findById(id).map(ParticularStakeholderPersistenceMapper::reconstituteDomain);
    }

    @Override
    public ParticularStakeholder updateById(ParticularStakeholder particularStakeholder) {
        ParticularStakeholderEntity existingEntity = jpaRepository.findById(particularStakeholder.getId())
                .orElseThrow(() -> new RuntimeException("Tiers particulier introuvable avec l'ID : " + particularStakeholder.getId()));

        existingEntity.setFirstName(particularStakeholder.getFirstName());
        existingEntity.setLastName(particularStakeholder.getLastName());
        existingEntity.setEmail(particularStakeholder.getEmail());
        existingEntity.setPhone(particularStakeholder.getPhone());
        existingEntity.setAddress(particularStakeholder.getAddress());

        ParticularStakeholderEntity savedEntity = jpaRepository.save(existingEntity);

        return ParticularStakeholderPersistenceMapper.reconstituteDomain(savedEntity);
    }
}
