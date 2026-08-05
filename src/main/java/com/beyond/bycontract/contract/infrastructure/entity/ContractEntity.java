package com.beyond.bycontract.contract.infrastructure.entity;

import com.beyond.bycontract.company.infrastructure.entity.CompanyEntity;
import com.beyond.bycontract.contract.domain.model.ContractStatus;
import com.beyond.bycontract.contract.domain.model.ContractType;
import com.beyond.bycontract.template.infrastructure.entity.TemplateEntity;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "contracts")
public class ContractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private UUID id;

	@Column(name = "name")
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "contract_type", nullable = false)
	private ContractType contractType;

	@Enumerated(EnumType.STRING)
	@Column(name = "contract_status", nullable = false)
	private ContractStatus contractStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_company", nullable = false)
	private CompanyEntity company;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_author", nullable = false)
	private UserEntity author;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_template")
	private TemplateEntity template;

	@Column(name = "effective_date")
	private LocalDate effectiveDate;

	@Column(name = "expiration_date")
	private LocalDate expirationDate;

	@Column(name = "auto_renew")
	private Boolean autoRenew;
	//Montant du contract
	@Column(precision = 12, scale = 2)
	private BigDecimal value;

	@OneToOne(mappedBy = "contract", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private ContractContentEntity content;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;

	public ContractEntity() {
	}

	public ContractEntity(UUID id, String name, ContractType contractType, ContractStatus contractStatus, CompanyEntity company, UserEntity author, TemplateEntity template, LocalDate effectiveDate, LocalDate expirationDate, Boolean autoRenew, BigDecimal value, ContractContentEntity content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.id = id;
		this.name = name;
		this.contractType = contractType;
		this.contractStatus = contractStatus;
		this.company = company;
		this.author = author;
		this.template = template;
		this.effectiveDate = effectiveDate;
		this.expirationDate = expirationDate;
		this.autoRenew = autoRenew;
		this.value = value;
		this.content = content;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ContractType getContractType() {
		return contractType;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

	public ContractStatus getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(ContractStatus contractStatus) {
		this.contractStatus = contractStatus;
	}

	public CompanyEntity getCompany() {
		return company;
	}

	public void setCompany(CompanyEntity company) {
		this.company = company;
	}

	public UserEntity getAuthor() {
		return author;
	}

	public void setAuthor(UserEntity author) {
		this.author = author;
	}

	public TemplateEntity getTemplate() {
		return template;
	}

	public void setTemplate(TemplateEntity template) {
		this.template = template;
	}

	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Boolean getAutoRenew() {
		return autoRenew;
	}

	public void setAutoRenew(Boolean autoRenew) {
		this.autoRenew = autoRenew;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public ContractContentEntity getContent() {
		return content;
	}

	public void setContent(ContractContentEntity content) {
		this.content = content;
	}

	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(final LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getModifiedAt() {
		return this.modifiedAt;
	}

	public void setModifiedAt(final LocalDateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
}
