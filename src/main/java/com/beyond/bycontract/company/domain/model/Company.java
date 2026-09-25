package com.beyond.bycontract.company.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Company {
	private UUID id;
	private UUID idCreator;
	private String name;
	private String siret;
	private String address;
	private MainContactCompany mainContactCompany;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public Company() {
	}

	//CONSTRUCTOR FOR THE CREATION OF A NEW COMPANY
	public Company(String name, UUID idCreator,  String siret, String address, MainContactCompany mainContactCompany, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.idCreator = idCreator;
		this.name = name;
		this.siret = siret;
		this.address = address;
		this.mainContactCompany = mainContactCompany;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public Company(UUID id,  String name,  UUID idCreator, String siret, String address, MainContactCompany mainContactCompany, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.id = id;
		this.idCreator = idCreator;
		this.name = name;
		this.siret = siret;
		this.address = address;
		this.mainContactCompany = mainContactCompany;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public static Company create(String name, UUID idCreator,  String siret, String address, MainContactCompany mainContactCompany) {
		return new Company( name, idCreator, siret, address, mainContactCompany, LocalDateTime.now(), LocalDateTime.now());
	}

	public void update(String newName, String newSiret, String newAddress) {
		if (newName != null && !newName.trim().isEmpty()) {
			this.name = newName;
		}
		if (newSiret != null && !newSiret.trim().isEmpty()) {
			this.siret = newSiret;
		}
		if (newAddress != null && !newAddress.trim().isEmpty()) {
			this.address = newAddress;
		}
	}

	public UUID getId() {
		return id;
	}

	public UUID getIdCreator() {
		return idCreator;
	}

	public String getName() {
		return name;
	}

	public String getSiret() {
		return siret;
	}

	public String getAddress() {
		return address;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public MainContactCompany getMainContactCompany() {
		return mainContactCompany;
	}

	@Override
	public String toString() {
		return "Company{" +
				"id=" + id +
				", idCreator=" + idCreator +
				", name='" + name + '\'' +
				", siret='" + siret + '\'' +
				", address='" + address + '\'' +
				", createdAt=" + createdAt +
				", modifiedAt=" + modifiedAt +
				'}';
	}
}
