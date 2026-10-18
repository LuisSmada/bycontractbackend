package com.beyond.bycontract.contract.infrastructure.repository;

import com.beyond.bycontract.contract.domain.model.ContractStatus;
import com.beyond.bycontract.contract.infrastructure.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface JpaContractRepository extends JpaRepository<ContractEntity, UUID> {

//    findBy ➡️ SELECT * FROM contract_entity WHERE
//    ContractStatus ➡️ contract_status = ? (le premier paramètre)
//    And ➡️ AND
//    ExpirationDate ➡️ expiration_date
//    LessThanEqual ➡️ <= ? (le deuxième paramètre)

//    La seule règle à respecter
//    Pour que cette génération automatique fonctionne sans planter, les mots utilisés dans le nom de la méthode doivent correspondre exactement aux noms des propriétés (variables) définies dans ton entité ContractEntity (en mettant juste la première lettre en majuscule).
//    Si dans ContractEntity.java tu as bien écrit :
//    Java
//    private ContractStatus contractStatus;
//    private LocalDate expirationDate;

    // Pour la mise à risque (ACTIVE -> RISKED)
    List<ContractEntity> findByContractStatusAndExpirationDateLessThanEqual(ContractStatus status, LocalDate thresholdDate);

    // Pour l'entrée en vigueur (SIGNED -> ACTIVE)
    List<ContractEntity> findByContractStatusAndEffectiveDateLessThanEqual(ContractStatus status, LocalDate thresholdDate);

    // Pour l'expiration globale (RISKED/ACTIVE/SIGNED -> EXPIRED)
    List<ContractEntity> findByContractStatusInAndExpirationDateLessThanEqual(List<ContractStatus> statuses, LocalDate thresholdDate);
}
