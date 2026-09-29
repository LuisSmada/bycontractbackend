package com.beyond.bycontract.contract.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


public class Contract {
	private UUID id;
	private String name;
	private ContractType contractType;
	private ContractStatus contractStatus;
	private UUID idCompany;
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

	public Contract() {
	}

	//CONSTRUCTOR FOR THE CREATION OF A CONTRACT
	public Contract(String name, ContractType contractType, ContractStatus contractStatus, UUID idCompany, UUID idAuthor, UUID idTemplate, LocalDate effectiveDate, LocalDate expirationDate, Boolean autoRenew, BigDecimal value, ContractContent contractContent, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.name = name;
		this.contractType = contractType;
		this.contractStatus = contractStatus;
		this.idCompany = idCompany;
		this.idAuthor = idAuthor;
		this.idTemplate = idTemplate;
		this.effectiveDate = effectiveDate;
		this.expirationDate = expirationDate;
		this.autoRenew = autoRenew;
		this.value = value;
		this.contractContent = contractContent;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public Contract(UUID id, String name, ContractType contractType, ContractStatus contractStatus, UUID idCompany, UUID idAuthor, UUID idTemplate, LocalDate effectiveDate, LocalDate expirationDate, Boolean autoRenew, BigDecimal value, ContractContent contractContent, LocalDateTime createdAt, LocalDateTime modifiedAt, Integer version ) {
		this.id = id;
		this.name = name;
		this.contractType = contractType;
		this.contractStatus = contractStatus;
		this.idCompany = idCompany;
		this.idAuthor = idAuthor;
		this.idTemplate = idTemplate;
		this.effectiveDate = effectiveDate;
		this.expirationDate = expirationDate;
		this.autoRenew = autoRenew;
		this.value = value;
		this.contractContent = contractContent;
		this.createdAt = createdAt;
		this.version = version;
	}

	public static Contract create(String name, ContractType contractType, ContractStatus contractStatus, UUID idCompany, UUID idAuthor, UUID idTemplate, LocalDate effectiveDate, LocalDate expirationDate, Boolean autoRenew, BigDecimal value, JsonNode body, String plainText) throws Exception {

		if (body == null && plainText == null) {
			throw new IllegalStateException("Cannot create a contract without text");
		}

		ContractContent newContent = new ContractContent(body, plainText, null, LocalDateTime.now());

		LocalDate finalExpirationDate = autoRenew == true ? null : expirationDate;

		return new Contract(
				name,
				contractType,
				contractStatus,
				idCompany,
				idAuthor,
				idTemplate,
				effectiveDate,
				finalExpirationDate,
				autoRenew,
				value,
				newContent,
				LocalDateTime.now(),
				LocalDateTime.now()
		);
	}

	public void update(String newName, ContractType newContractType, ContractStatus newContractStatus, UUID newIdCompany, LocalDate newEffectiveDate, LocalDate newExpirationDate, Boolean newAutoRenew, BigDecimal newValue, JsonNode newBody, String newPlainText) {

		if(newName != null && !newName.trim().isEmpty()) {
			this.name = newName;
		}

		if(newContractType != null) {
			this.contractType = newContractType;
		}

		if(newContractStatus != null) {
			this.contractStatus = newContractStatus;
		}

		if(newIdCompany != null) {
			this.idCompany = newIdCompany;
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
			this.contractContent = new ContractContent(newBody, newPlainText, this.contractContent.getSignedPdfUrl(), LocalDateTime.now());
		}

		this.modifiedAt = LocalDateTime.now();
	}


	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public ContractType getContractType() {
		return contractType;
	}

	public ContractStatus getContractStatus() {
		return contractStatus;
	}

	public UUID getIdCompany() {
		return idCompany;
	}

	public UUID getIdAuthor() {
		return idAuthor;
	}

	public UUID getIdTemplate() {
		return idTemplate;
	}

	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public Boolean getAutoRenew() {
		return autoRenew;
	}

	public BigDecimal getValue() {
		return value;
	}

	public ContractContent getContractContent() {
		return contractContent;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public Integer getVersion() { return version; }

	@Override
	public String toString() {
		return "Contract{" +
				"id=" + id +
				", name='" + name + '\'' +
				", contractType=" + contractType +
				", contractStatus=" + contractStatus +
				", idCompany=" + idCompany +
				", idAuthor=" + idAuthor +
				", idTemplate=" + idTemplate +
				", effectiveDate=" + effectiveDate +
				", expirationDate=" + expirationDate +
				", autoRenew=" + autoRenew +
				", value=" + value +
				", idContractContent=" + contractContent.getId() +
				", createdAt=" + createdAt +
				", modifiedAt=" + modifiedAt +
				'}';
	}


	public void sign() throws Exception {
		if (contractStatus != ContractStatus.PENDING) {
			throw new IllegalStateException("Only pending contracts can be signed");
		}

		contractStatus = ContractStatus.SIGNED;
	}
}
