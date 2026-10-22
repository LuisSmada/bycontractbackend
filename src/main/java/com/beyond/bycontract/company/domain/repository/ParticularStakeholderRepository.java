package com.beyond.bycontract.company.domain.repository;

import com.beyond.bycontract.company.domain.model.ParticularStakeholder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticularStakeholderRepository {

    ParticularStakeholder create(ParticularStakeholder particularStakeholder);
    void deleteById(UUID id);
    Optional<ParticularStakeholder> getParticularStakeholderById(UUID id);
    ParticularStakeholder updateById(ParticularStakeholder particularStakeholder);
    Page<ParticularStakeholder> getAllParticularStakeholders(Pageable pageable);
    boolean existsByEmail(String email);

}
