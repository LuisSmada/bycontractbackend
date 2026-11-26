package com.beyond.bycontract.contract.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Contract {
	private UUID id;
	private String name;
	private ContractType contractType;
	private ContractStatus contractStatus;
	private UUID idCompany;
	private UUID idParticularStakeholder;
	private UUID idAuthor;
	private UUID idTemplate;
	private LocalDate effectiveDate;
	private LocalDate expirationDate;
	private Boolean autoRenew;
	//Montant du contract
	private BigDecimal value;
	private ContractContent contractContent;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private Integer version;


	private static void validateStakeholderXor(UUID idCompany, UUID idParticularStakeholder) {
		boolean hasCompany = idCompany != null;
		boolean hasParticular = idParticularStakeholder != null;
		if (hasCompany == hasParticular) {
			throw new IllegalArgumentException("Un contrat doit être associé soit à une entreprise, soit à un particulier (pas les deux, ni aucun).");
		}
	}

	public static Contract create(
			String name, ContractType contractType, ContractStatus contractStatus,
			UUID idCompany, UUID idParticularStakeholder,
			UUID idAuthor, UUID idTemplate, LocalDate effectiveDate,
			LocalDate expirationDate, Boolean autoRenew, BigDecimal value,
			JsonNode body, String plainText
	) {
		validateStakeholderXor(idCompany, idParticularStakeholder);

		if (body == null && plainText == null) {
			throw new IllegalStateException("Cannot create a contract without text");
		}

		ContractContent newContent = new ContractContent(body, plainText, null, LocalDateTime.now());
		LocalDate finalExpirationDate = Boolean.TRUE.equals(autoRenew) ? null : expirationDate;

		return new Contract(
				null,
				name,
				contractType,
				contractStatus,
				idCompany,
				idParticularStakeholder,
				idAuthor,
				idTemplate,
				effectiveDate,
				finalExpirationDate,
				autoRenew,
				value,
				newContent,
				LocalDateTime.now(),
				LocalDateTime.now(),
				null
		);
	}

	public void update(String newName, ContractType newContractType, ContractStatus newContractStatus, UUID newIdCompany, UUID newIdParticularStakeholder, LocalDate newEffectiveDate, LocalDate newExpirationDate, Boolean newAutoRenew, BigDecimal newValue, JsonNode newBody, String newPlainText) {

		if(newName != null && !newName.trim().isEmpty()) {
			this.name = newName;
		}

		if(newContractType != null) {
			this.contractType = newContractType;
		}

		if(newContractStatus != null) {
			this.contractStatus = newContractStatus;
		}

		if (newIdCompany != null && newIdParticularStakeholder != null) {
			throw new IllegalArgumentException("Impossible d'associer à la fois une entreprise et un particulier.");
		}

		// Bascule propre (XOR préservé)
		if (newIdCompany != null) {
			this.idCompany = newIdCompany;
			this.idParticularStakeholder = null;
		} else if (newIdParticularStakeholder != null) {
			this.idParticularStakeholder = newIdParticularStakeholder;
			this.idCompany = null;
		}

		if(newEffectiveDate != null) {
			this.effectiveDate = newEffectiveDate;
		}

		if(newValue != null) {
			this.value = newValue;
		}

		//EXPIRATION DATE CAN BE NULL BECAUSE THE CONTRACT CAN BE AUTORENEWED AND AUTORENEW CAN BE FALSE
		if (newAutoRenew != null) {
			this.autoRenew = newAutoRenew;
			if (Boolean.TRUE.equals(newAutoRenew)) {
				this.expirationDate = null;
			} else if (newExpirationDate != null) {
				this.expirationDate = newExpirationDate;
			}
		}

		if (newBody != null && newPlainText != null) {
			this.contractContent = new ContractContent(newBody, newPlainText, this.contractContent.getSignedPdfKey(), LocalDateTime.now());
		}

		this.modifiedAt = LocalDateTime.now();
	}


	public void markAsSigned(String s3Key)  {
		if (contractStatus != ContractStatus.PENDING) {
			throw new IllegalStateException("Only pending contracts can be signed");
		}

		if(s3Key.isEmpty()) {
			throw new IllegalStateException("The key of s3  should not be null");
		}

		this.contractStatus = ContractStatus.SIGNED;
		this.modifiedAt = LocalDateTime.now();

		this.contractContent.setSignedPdfKey(s3Key);
		this.contractContent.setModifiedAt(this.modifiedAt);
	}
}
