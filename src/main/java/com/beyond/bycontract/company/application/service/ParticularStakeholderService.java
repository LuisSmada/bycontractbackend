package com.beyond.bycontract.company.application.service;

import com.beyond.bycontract.company.application.dto.CreateParticularStakeholderCommand;
import com.beyond.bycontract.company.application.dto.ParticularStakeholderResponse;
import com.beyond.bycontract.company.application.dto.UpdateParticularStakeholderCommand;
import com.beyond.bycontract.company.domain.model.ParticularStakeholder;
import com.beyond.bycontract.company.domain.repository.ParticularStakeholderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParticularStakeholderService {

    private final ParticularStakeholderRepository repository;

    private ParticularStakeholderResponse buildResponse(ParticularStakeholder stakeholder) {
        return new ParticularStakeholderResponse(
                stakeholder.getId(),
                stakeholder.getFirstName(),
                stakeholder.getLastName(),
                stakeholder.getEmail(),
                stakeholder.getPhone(),
                stakeholder.getAddress(),
                stakeholder.getCreatedAt(),
                stakeholder.getModifiedAt()
        );
    }

    @Transactional
    public ParticularStakeholderResponse create(CreateParticularStakeholderCommand command) {
        if (repository.existsByEmail(command.email())) {
            throw new RuntimeException("Un tiers particulier avec cet email existe déjà : " + command.email());
            // Idéalement, crée une exception personnalisée comme EmailAlreadyExistsException
        }

        ParticularStakeholder stakeholder = ParticularStakeholder.create(
                command.firstName(),
                command.lastName(),
                command.email(),
                command.phone(),
                command.address()
        );

        ParticularStakeholder savedStakeholder = repository.create(stakeholder);
        return buildResponse(savedStakeholder);
    }

    public ParticularStakeholderResponse getById(UUID id) {
        ParticularStakeholder stakeholder = repository.getParticularStakeholderById(id)
                .orElseThrow(() -> new RuntimeException("Tiers particulier introuvable avec l'id : " + id));

        return buildResponse(stakeholder);
    }

    public Page<ParticularStakeholderResponse> getAll(Pageable pageable) {
        // La méthode .map() sur un objet Page (Spring Data) convertit automatiquement
        // le contenu tout en conservant les métadonnées (total elements, total pages, etc.)
        return repository.getAllParticularStakeholders(pageable)
                .map(this::buildResponse);
    }

    @Transactional
    public ParticularStakeholderResponse updateById(UUID id, UpdateParticularStakeholderCommand command) {
        ParticularStakeholder existingStakeholder = repository.getParticularStakeholderById(id)
                .orElseThrow(() -> new RuntimeException("Tiers particulier introuvable avec l'id : " + id));

        if (command.email() != null && !command.email().equals(existingStakeholder.getEmail())) {
            if (repository.existsByEmail(command.email())) {
                throw new RuntimeException("Cet email est déjà utilisé par un autre tiers particulier.");
            }
        }

        existingStakeholder.update(
                command.firstName(),
                command.lastName(),
                command.email(),
                command.phone(),
                command.address()
        );

        ParticularStakeholder savedStakeholder = repository.updateById(existingStakeholder);
        return buildResponse(savedStakeholder);
    }

    @Transactional
    public void deleteById(UUID id) {
        if (repository.getParticularStakeholderById(id).isEmpty()) {
            throw new RuntimeException("Impossible de supprimer : tiers particulier introuvable.");
        }
        repository.deleteById(id);
    }
}
