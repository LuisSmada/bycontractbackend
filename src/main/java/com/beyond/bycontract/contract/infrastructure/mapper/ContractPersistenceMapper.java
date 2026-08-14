package com.beyond.bycontract.contract.infrastructure.mapper;

import com.beyond.bycontract.company.infrastructure.entity.CompanyEntity;
import com.beyond.bycontract.contract.domain.model.Contract;
import com.beyond.bycontract.contract.domain.model.ContractContent;
import com.beyond.bycontract.contract.infrastructure.entity.ContractContentEntity;
import com.beyond.bycontract.contract.infrastructure.entity.ContractEntity;
import com.beyond.bycontract.template.infrastructure.entity.TemplateEntity;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;

public class ContractPersistenceMapper {

	public static Contract reconstituteDomain(ContractEntity entity) {
		try {

			ContractContent contractContent = new ContractContent(
					entity.getContent().getBody(),
					entity.getContent().getPlainText(),
					entity.getContent().getSignedPdfUrl(),
					entity.getContent().getModifiedAt()
			);

			return new Contract(
					entity.getId(),
					entity.getName(),
					entity.getContractType(),
					entity.getContractStatus(),
					entity.getCompany() != null ? entity.getCompany().getId() : null,
					entity.getAuthor() != null ? entity.getAuthor().getId() : null,
					entity.getTemplate() != null ? entity.getTemplate().getId() : null,
					entity.getEffectiveDate(),
					entity.getExpirationDate(),
					entity.getAutoRenew(),
					entity.getValue(),
					contractContent,
					entity.getCreatedAt(),
					entity.getModifiedAt()
			);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de mapping BDD -> Domaine", e);
		}


		//try {
		//	java.lang.reflect.Constructor<Contract> constructor = Contract.class.getDeclaredConstructor(
		//			UUID.class, String.class, ContractType.class, ContractStatus.class, UUID.class, UUID.class, UUID.class, LocalDate.class, LocalDate.class, Boolean.class, BigDecimal.class, UUID.class, LocalDateTime.class, LocalDateTime.class
		//	);
		//	constructor.setAccessible(true);
		//	return constructor.newInstance(
		//			entity.getId(), entity.getName(), entity.getContractType(), entity.getContractStatus(),
		//			entity.getCompany() != null ? entity.getCompany().getId() : null,
		//			entity.getAuthor() != null ? entity.getAuthor().getId() : null,
		//			entity.getTemplate() != null ? entity.getTemplate().getId() : null,
		//			entity.getEffectiveDate(), entity.getExpirationDate(), entity.getAutoRenew(), entity.getValue(),
		//			entity.getContent() != null ? entity.getContent().getId() : null,
		//			entity.getCreatedAt(), entity.getModifiedAt()
		//	);
		//} catch (Exception e) {
		//	throw new RuntimeException("Erreur de mapping BDD -> Domaine", e);
		//}
	}


	public static ContractEntity toEntity(Contract contract) {
		ContractEntity contractEntity = new ContractEntity();

		contractEntity.setName(contract.getName());
		contractEntity.setContractType(contract.getContractType());
		contractEntity.setContractStatus(contract.getContractStatus());
		contractEntity.setEffectiveDate(contract.getEffectiveDate());
		contractEntity.setExpirationDate(contract.getExpirationDate());
		contractEntity.setAutoRenew(contract.getAutoRenew());
		contractEntity.setValue(contract.getValue());

		/* Note technique : Pour lier l'auteur et l'entreprise sans faire de SELECT en BDD,
          utilise entityManager.getReference(CompanyEntity.class, contract.getIdCompany());
          Ici je garde ta logique actuelle pour la simplicité */

		if (contract.getIdCompany() != null) {
			CompanyEntity companyEntity = new CompanyEntity();
			companyEntity.setId(contract.getIdCompany());

			contractEntity.setCompany(companyEntity);
		}

		if (contract.getIdAuthor() != null) {
			UserEntity userEntity = new UserEntity();
			userEntity.setId(contract.getIdAuthor());

			contractEntity.setAuthor(userEntity);
		}

		if (contract.getIdTemplate() != null) {
			TemplateEntity templateEntity = new TemplateEntity();
			templateEntity.setId(contract.getIdTemplate());

			contractEntity.setTemplate(templateEntity);
		}

		if (contract.getContractContent() != null) {
			ContractContentEntity contractContentEntity = new ContractContentEntity();
			contractContentEntity.setBody(contract.getContractContent().getBody());
			contractContentEntity.setPlainText(contract.getContractContent().getPlainText());
			contractContentEntity.setContract(contractEntity);
			contractEntity.setContent(contractContentEntity);
		}

		//createdAt and modifiedAt are not added because they are not updatable and not insertable through an entity,so it's useless to add it

		return contractEntity;
	}
}
