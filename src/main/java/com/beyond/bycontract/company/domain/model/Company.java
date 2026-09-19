package com.beyond.bycontract.company.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Company {
	private UUID id;
	private UUID idCreator;
	private String name;
	private String siret;
	private String address;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public Company() {
	}

	//CONSTRUCTOR FOR THE CREATION OF A NEW COMPANY
	public Company(String name, UUID idCreator,  String siret, String address, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.idCreator = idCreator;
		this.name = name;
		this.siret = siret;
		this.address = address;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public Company(UUID id,  String name,  UUID idCreator, String siret, String address, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.id = id;
		this.idCreator = idCreator;
		this.name = name;
		this.siret = siret;
		this.address = address;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public static Company create(String name, UUID idCreator,  String siret, String address) {
		return new Company( name, idCreator, siret, address, LocalDateTime.now(), LocalDateTime.now());
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
