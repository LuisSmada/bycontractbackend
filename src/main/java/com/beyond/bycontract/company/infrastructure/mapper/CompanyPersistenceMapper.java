package com.beyond.bycontract.company.infrastructure.mapper;

import com.beyond.bycontract.company.domain.model.Company;
import com.beyond.bycontract.company.infrastructure.entity.CompanyEntity;

public class CompanyPersistenceMapper {

	public static Company reconstituteDomain(CompanyEntity entity) {
		return new Company(
				entity.getId(),
				entity.getName(),
				entity.getSiret(),
				entity.getAddress(),
				entity.getCreatedAt(),
				entity.getModifiedAt()
		);
	}

	public static CompanyEntity toEntity(Company company) {
		CompanyEntity entity = new CompanyEntity();

		entity.setName(company.getName());
		entity.setSiret(company.getSiret());
		entity.setAddress(company.getAddress());

		return entity;
	}
}
