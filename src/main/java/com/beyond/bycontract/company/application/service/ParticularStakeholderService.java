package com.beyond.bycontract.company.application.service;

import com.beyond.bycontract.company.application.dto.CreateParticularStakeholderCommand;
import com.beyond.bycontract.company.application.dto.ParticularStakeholderResponse;
import com.beyond.bycontract.company.application.dto.UpdateParticularStakeholderCommand;
import com.beyond.bycontract.company.domain.exception.CompanyNotFoundException;
import com.beyond.bycontract.company.domain.model.Company;
import com.beyond.bycontract.company.domain.model.ParticularStakeholder;
import com.beyond.bycontract.company.domain.repository.ParticularStakeholderRepository;
import com.beyond.bycontract.shared.utils.AuthorizationUtils;
import com.beyond.bycontract.user.domain.exception.UserNotFoundException;
import com.beyond.bycontract.user.domain.model.User;
import com.beyond.bycontract.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParticularStakeholderService {

    private final ParticularStakeholderRepository repository;
    private final UserRepository userRepository;

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

    private void verifySubscription(UUID idUser) {
        User user = userRepository.getUserById(idUser)
                .orElseThrow(() -> new UserNotFoundException(idUser));

        // 🚨 SÉCURITÉ : Vérification de l'abonnement
        if (!user.hasActiveSubscription()) {
            throw new IllegalStateException("Un abonnement actif est requis pour effectuer cette action.");
        }
    }

    @Transactional
    public ParticularStakeholderResponse create(CreateParticularStakeholderCommand command) {
        if (repository.existsByEmail(command.email())) {
            throw new RuntimeException("Un tiers particulier avec cet email existe déjà : " + command.email());
            // Idéalement, crée une exception personnalisée comme EmailAlreadyExistsException
        }

        verifySubscription(command.idRequester());

        ParticularStakeholder stakeholder = ParticularStakeholder.create(
                command.idRequester(),
                command.firstName(),
                command.lastName(),
                command.email(),
                command.phone(),
                command.address()
        );

        ParticularStakeholder savedStakeholder = repository.create(stakeholder);
        return buildResponse(savedStakeholder);
    }

    public ParticularStakeholderResponse getById(UUID id, UUID idRequester) {
        ParticularStakeholder stakeholder = repository.getParticularStakeholderById(id)
                .orElseThrow(() -> new RuntimeException("Tiers particulier introuvable avec l'id : " + id));
        AuthorizationUtils.verifyOwnership(stakeholder.getIdCreator(), idRequester);

        return buildResponse(stakeholder);
    }

    public List<ParticularStakeholderResponse> getAll(UUID idCreator) {
        // La méthode .map() sur un objet Page (Spring Data) convertit automatiquement
        // le contenu tout en conservant les métadonnées (total elements, total pages, etc.)
        List<ParticularStakeholder> allParticularStakeholders = idCreator == null ? repository.getAllParticularStakeholders() : repository.getParticularStakeholdersByIdCreator(idCreator);

        return allParticularStakeholders.stream()
                .map(this::buildResponse)
                .toList();
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
    public void deleteById(UUID id, UUID idRequester) {
        ParticularStakeholder particularStakeholder = repository.getParticularStakeholderById(id).orElseThrow(() -> new RuntimeException("No particular found with id"+ id));
        AuthorizationUtils.verifyOwnership(particularStakeholder.getIdCreator(), idRequester);
        repository.deleteById(id);
    }
}
