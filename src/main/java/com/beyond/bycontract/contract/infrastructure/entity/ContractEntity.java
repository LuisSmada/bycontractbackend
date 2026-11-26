package com.beyond.bycontract.contract.infrastructure.entity;

import com.beyond.bycontract.company.infrastructure.entity.CompanyEntity;
import com.beyond.bycontract.company.infrastructure.entity.ParticularStakeholderEntity;
import com.beyond.bycontract.contract.domain.model.ContractStatus;
import com.beyond.bycontract.contract.domain.model.ContractType;
import com.beyond.bycontract.template.infrastructure.entity.TemplateEntity;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "contracts")
@Check(constraints = "(id_company IS NOT NULL AND id_particular_stakeholder IS NULL) OR (id_company IS NULL AND id_particular_stakeholder IS NOT NULL)")
@Setter
@Getter
@AllArgsConstructor
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
	@JoinColumn(name = "id_company")
	private CompanyEntity company;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_particular_stakeholder")
	private ParticularStakeholderEntity particularStakeholder;

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

	@Version
	@Column(name = "version", nullable = false)
	private Integer version;

	public ContractEntity() {
	}

}
