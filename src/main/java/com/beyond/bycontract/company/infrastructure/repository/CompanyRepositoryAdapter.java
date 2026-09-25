package com.beyond.bycontract.company.infrastructure.repository;

import com.beyond.bycontract.company.domain.model.Company;
import com.beyond.bycontract.company.domain.repository.CompanyRepository;
import com.beyond.bycontract.company.infrastructure.entity.CompanyEntity;
import com.beyond.bycontract.company.infrastructure.entity.MainContactCompanyEntity;
import com.beyond.bycontract.company.infrastructure.mapper.CompanyPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryAdapter implements CompanyRepository {

	private final JpaCompanyRepository jpaRepository;

	@Override
	public Company create(Company company) {
		CompanyEntity savedEntity = jpaRepository.save(CompanyPersistenceMapper.toEntity(company));
		return CompanyPersistenceMapper.reconstituteDomain(savedEntity);
	}

	@Override
	public List<Company> getAllCompanies() {
		return jpaRepository.findAll().stream().map(CompanyPersistenceMapper::reconstituteDomain).toList();
	}

	@Override
	public Optional<Company> findBySiret(String siret) {
		return jpaRepository.findBySiret(siret).map(CompanyPersistenceMapper::reconstituteDomain);
	}

	@Override
	public boolean existsBySiret(String siret) {
		return jpaRepository.existsBySiret(siret);
	}

	@Override
	public void deleteById(UUID id) {
		jpaRepository.deleteById(id);
	}

	@Override
	public Optional<Company> getCompanyById(UUID id) {
		return jpaRepository.findById(id).map(CompanyPersistenceMapper::reconstituteDomain);
	}

	@Override
	public List<Company> getCompaniesByIds(Collection<UUID> ids) {
		return jpaRepository.findAllById(ids).stream().map(CompanyPersistenceMapper::reconstituteDomain).toList();
	}

	@Override
	public List<Company> getCompaniesByIdCreator(UUID idCreator) {
		return jpaRepository.findAllByIdCreator(idCreator).stream().map(CompanyPersistenceMapper::reconstituteDomain).toList();
	}

	@Override
	public Company updateCompanyById(Company company) {
		CompanyEntity entity = jpaRepository.findById(company.getId())
				.orElseThrow(() -> new RuntimeException("Company entity not found"));

		// 2. On met à jour les champs de l'entité Entreprise
		entity.setName(company.getName());
		entity.setSiret(company.getSiret());
		entity.setAddress(company.getAddress());

		// 3. On met à jour les champs de l'entité Contact Principal
		// (Puisque c'est une relation OneToOne avec @MapsId, l'entité mainContact existe déjà)
		MainContactCompanyEntity contactEntity = entity.getMainContactCompany();
		contactEntity.setFirstName(company.getMainContactCompany().getFirstName());
		contactEntity.setLastName(company.getMainContactCompany().getLastName());
		contactEntity.setEmail(company.getMainContactCompany().getEmail());
		contactEntity.setPhone(company.getMainContactCompany().getPhone());

		// 4. On sauvegarde l'entité mise à jour
		// (La cascade fera aussi l'update sur MainContactCompanyEntity)
		CompanyEntity savedEntity = jpaRepository.save(entity);

		return CompanyPersistenceMapper.reconstituteDomain(savedEntity);
	}
}
