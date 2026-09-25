package com.beyond.bycontract.company.infrastructure.mapper;

import com.beyond.bycontract.company.domain.model.Company;
import com.beyond.bycontract.company.domain.model.MainContactCompany;
import com.beyond.bycontract.company.infrastructure.entity.CompanyEntity;
import com.beyond.bycontract.company.infrastructure.entity.MainContactCompanyEntity;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;

public class CompanyPersistenceMapper {

	public static Company reconstituteDomain(CompanyEntity entity) {

		MainContactCompanyEntity mainContactCompanyEntity = entity.getMainContactCompany();

		MainContactCompany mainContactCompany = new MainContactCompany(
				mainContactCompanyEntity.getId(),
				mainContactCompanyEntity.getFirstName(),
				mainContactCompanyEntity.getLastName(),
				mainContactCompanyEntity.getEmail(),
				mainContactCompanyEntity.getPhone()
		);

		return new Company(
				entity.getId(),
				entity.getName(),
				entity.getCreator().getId(),
				entity.getSiret(),
				entity.getAddress(),
				mainContactCompany,
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

		if(company.getMainContactCompany() != null) {
			MainContactCompanyEntity mainContactCompanyEntity = new MainContactCompanyEntity();
			mainContactCompanyEntity.setId(company.getMainContactCompany().getId());
			mainContactCompanyEntity.setFirstName(company.getMainContactCompany().getFirstName());
			mainContactCompanyEntity.setLastName(company.getMainContactCompany().getLastName());
			mainContactCompanyEntity.setEmail(company.getMainContactCompany().getEmail());
			mainContactCompanyEntity.setPhone(company.getMainContactCompany().getPhone());
			entity.setMainContactCompany(mainContactCompanyEntity);
		}

		entity.setSiret(company.getSiret());
		entity.setAddress(company.getAddress());

		return entity;
	}
}
