package com.beyond.bycontract.company.infrastructure.entity;

import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "companies")
public class CompanyEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_creator", nullable = false)
	private UserEntity creator;

	@OneToOne(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private MainContactCompanyEntity mainContactCompany;

	@Column(name = "name")
	private String name;

	@Column(name = "siret")
	private String siret;

	@Column(name = "address")
	private String address;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;


	public CompanyEntity() {
	}

	public CompanyEntity(UUID id, UserEntity creator, String name, String siret, String address, MainContactCompanyEntity mainContactCompany, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.id = id;
		this.creator = creator;
		this.name = name;
		this.siret = siret;
		this.address = address;
		this.mainContactCompany = mainContactCompany;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UserEntity getCreator() {
		return creator;
	}

	public void setCreator(UserEntity creator) {
		this.creator = creator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSiret() {
		return siret;
	}

	public void setSiret(String siret) {
		this.siret = siret;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(LocalDateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public MainContactCompanyEntity getMainContactCompany() {
		return mainContactCompany;
	}

	public void setMainContactCompany(MainContactCompanyEntity contact) {
		if (contact == null) {
			if (this.mainContactCompany != null) {
				this.mainContactCompany.setCompany(null);
			}
		} else {
			contact.setCompany(this); // Règle d'or : On synchronise l'enfant vers le parent
		}
		this.mainContactCompany = contact;
	}

	@Override
	public String toString() {
		return "CompanyEntity{" +
				"id=" + id +
				", name='" + name + '\'' +
				", siret='" + siret + '\'' +
				", address='" + address + '\'' +
				", createdAt=" + createdAt +
				", modifiedAt=" + modifiedAt +
				'}';
	}
}
