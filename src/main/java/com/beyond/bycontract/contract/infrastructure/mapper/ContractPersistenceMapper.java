package com.beyond.bycontract.contract.infrastructure.mapper;

import com.beyond.bycontract.company.infrastructure.entity.CompanyEntity;
import com.beyond.bycontract.company.infrastructure.entity.ParticularStakeholderEntity;
import com.beyond.bycontract.company.infrastructure.repository.JpaCompanyRepository;
import com.beyond.bycontract.company.infrastructure.repository.JpaParticularStakeholderRepository;
import com.beyond.bycontract.contract.domain.model.Contract;
import com.beyond.bycontract.contract.domain.model.ContractContent;
import com.beyond.bycontract.contract.infrastructure.entity.ContractContentEntity;
import com.beyond.bycontract.contract.infrastructure.entity.ContractEntity;
import com.beyond.bycontract.template.infrastructure.entity.TemplateEntity;
import com.beyond.bycontract.template.infrastructure.repository.JpaTemplateRepository;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import com.beyond.bycontract.user.infrastructure.repository.SpringDataUserRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class ContractPersistenceMapper {

	@Autowired
	protected SpringDataUserRepository userJpaRepository;

	@Autowired
	protected JpaCompanyRepository companyJpaRepository;

	@Autowired
	protected JpaParticularStakeholderRepository particularStakeholderJpaRepository;

	@Autowired
	protected JpaTemplateRepository templateJpaRepository;

	// 1. BDD -> Domaine
	@Mapping(source = "company.id", target = "idCompany")
	@Mapping(source = "particularStakeholder.id", target = "idParticularStakeholder")
	@Mapping(source = "author.id", target = "idAuthor")
	@Mapping(source = "template.id", target = "idTemplate")
	@Mapping(source = "content", target = "contractContent")
	public abstract Contract entityToDomain(ContractEntity entity);

	// 2. Domaine -> BDD (Création)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "modifiedAt", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(source = "idCompany", target = "company")
	@Mapping(source = "idParticularStakeholder", target = "particularStakeholder")
	@Mapping(source = "idAuthor", target = "author")
	@Mapping(source = "idTemplate", target = "template")
	@Mapping(source = "contractContent", target = "content")
	public abstract ContractEntity domainToEntity(Contract domain);

	// 3. Domaine -> BDD (Mise à jour)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "modifiedAt", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(source = "idCompany", target = "company")
	@Mapping(source = "idParticularStakeholder", target = "particularStakeholder")
	@Mapping(source = "idAuthor", target = "author")
	@Mapping(source = "idTemplate", target = "template")
	@Mapping(source = "contractContent", target = "content")
	public abstract void updateEntityFromDomain(Contract domain, @MappingTarget ContractEntity entityToUpdate);

	// --- Sous-mappings pour ContractContent ---

	protected abstract ContractContent contentEntityToDomain(ContractContentEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "contract", ignore = true)
	protected abstract ContractContentEntity contentDomainToEntity(ContractContent domain);

	// Garantit la liaison bidirectionnelle @OneToOne avant la sauvegarde Hibernate
	@AfterMapping
	protected void linkContent(@MappingTarget ContractEntity contractEntity) {
		if (contractEntity.getContent() != null) {
			contractEntity.getContent().setContract(contractEntity);
		}
	}

	// --- Conversion UUID -> Référence JPA (sans SELECT en BDD) ---

	protected UserEntity idToUserEntity(UUID idAuthor) {
		return idAuthor == null ? null : userJpaRepository.getReferenceById(idAuthor);
	}

	protected CompanyEntity idToCompanyEntity(UUID idCompany) {
		return idCompany == null ? null : companyJpaRepository.getReferenceById(idCompany);
	}

	protected ParticularStakeholderEntity idToParticularStakeholderEntity(UUID idParticularStakeholder) {
		return idParticularStakeholder == null ? null : particularStakeholderJpaRepository.getReferenceById(idParticularStakeholder);
	}

	protected TemplateEntity idToTemplateEntity(UUID idTemplate) {
		return idTemplate == null ? null : templateJpaRepository.getReferenceById(idTemplate);
	}
}
