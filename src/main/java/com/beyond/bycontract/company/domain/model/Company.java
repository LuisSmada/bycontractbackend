package com.beyond.bycontract.company.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Company {
	private UUID id;
	private String name;
	private String siret;
	private String address;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public Company() {
	}

	public Company(String name, String siret, String address, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.name = name;
		this.siret = siret;
		this.address = address;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public Company(UUID id, String name, String siret, String address, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.id = id;
		this.name = name;
		this.siret = siret;
		this.address = address;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public static Company create(String name, String siret, String address) {
		return new Company(name, siret, address, LocalDateTime.now(), LocalDateTime.now());
	}

	public UUID getId() {
		return id;
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
				", name='" + name + '\'' +
				", siret='" + siret + '\'' +
				", address='" + address + '\'' +
				", createdAt=" + createdAt +
				", modifiedAt=" + modifiedAt +
				'}';
	}

}
