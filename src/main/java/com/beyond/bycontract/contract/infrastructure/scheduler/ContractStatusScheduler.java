package com.beyond.bycontract.contract.infrastructure.scheduler;

import com.beyond.bycontract.contract.domain.model.ContractStatus;
import com.beyond.bycontract.contract.infrastructure.entity.ContractEntity;
import com.beyond.bycontract.contract.infrastructure.repository.JpaContractRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class ContractStatusScheduler {

    private final JpaContractRepository jpaContractRepository;

    public ContractStatusScheduler(JpaContractRepository jpaContractRepository) {
        this.jpaContractRepository = jpaContractRepository;
    }

    // S'exécute tous les jours à 1h00 du matin
    @Scheduled(cron = "0 0 1 * * *")
    @Transactional // 🚨 Obligatoire pour garantir la cohérence des 3 étapes
    public void updateContractStatuses() {
        System.out.println("⏳ Le Scheduler s'est bien lancé !");
        LocalDate today = LocalDate.now();
        LocalDate oneMonthFromNow = today.plusMonths(1);

        // =================================================================
        // ÉTAPE 1 : ENTRÉE EN VIGUEUR (SIGNED -> ACTIVE)
        // On active les contrats signés dont la date d'effet est atteinte
        // =================================================================
        List<ContractEntity> contractsToActivate = jpaContractRepository
                .findByContractStatusAndEffectiveDateLessThanEqual(ContractStatus.SIGNED, today);

        if (!contractsToActivate.isEmpty()) {
            for (ContractEntity contract : contractsToActivate) {
                contract.setContractStatus(ContractStatus.ACTIVE);
            }
            jpaContractRepository.saveAll(contractsToActivate);
            System.out.println(contractsToActivate.size() + " contrats sont passés en statut ACTIVE.");
        }

        // =================================================================
        // ÉTAPE 2 : MISE À RISQUE (ACTIVE -> RISKED)
        // On alerte sur les contrats actifs qui expirent dans <= 1 mois
        // =================================================================
        List<ContractEntity> contractsToRisk = jpaContractRepository
                .findByContractStatusAndExpirationDateLessThanEqual(ContractStatus.ACTIVE, oneMonthFromNow);

        if (!contractsToRisk.isEmpty()) {
            for (ContractEntity contract : contractsToRisk) {
                contract.setContractStatus(ContractStatus.RISKED);
            }
            jpaContractRepository.saveAll(contractsToRisk);
            System.out.println(contractsToRisk.size() + " contrats sont passés en statut RISKED.");
        }

        // =================================================================
        // ÉTAPE 3 : EXPIRATION (ACTIVE/RISKED/SIGNED -> EXPIRED)
        // On expire tous les contrats dont la date de fin est dépassée
        // =================================================================
        List<ContractEntity> expiredContracts = jpaContractRepository
                .findByContractStatusInAndExpirationDateLessThanEqual(
                        List.of(ContractStatus.RISKED, ContractStatus.ACTIVE, ContractStatus.SIGNED),
                        today
                );

        if (!expiredContracts.isEmpty()) {
            for (ContractEntity contract : expiredContracts) {
                contract.setContractStatus(ContractStatus.EXPIRED);
            }
            jpaContractRepository.saveAll(expiredContracts);
            System.out.println(expiredContracts.size() + " contrats sont passés en statut EXPIRED.");
        }
    }
}
