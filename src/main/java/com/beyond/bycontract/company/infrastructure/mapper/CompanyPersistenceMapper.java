package com.beyond.bycontract.company.infrastructure.mapper;

import com.beyond.bycontract.company.domain.model.Company;
import com.beyond.bycontract.company.infrastructure.entity.CompanyEntity;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;

public class CompanyPersistenceMapper {

	public static Company reconstituteDomain(CompanyEntity entity) {
		return new Company(
				entity.getId(),
				entity.getName(),
				entity.getCreator().getId(),
				entity.getSiret(),
				entity.getAddress(),
				entity.getCreatedAt(),
				entity.getModifiedAt()
		);
	}

	public static CompanyEntity toEntity(Company company) {
		CompanyEntity entity = new CompanyEntity();

		entity.setName(company.getName());

		if(company.getIdCreator() != null) {
			UserEntity creator = new UserEntity();
			creator.setId(company.getIdCreator());
			entity.setCreator(creator);
		}

		entity.setSiret(company.getSiret());
		entity.setAddress(company.getAddress());

		return entity;
	}
}
