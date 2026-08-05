package com.beyond.bycontract.template.infrastructure.repository;

import com.beyond.bycontract.template.domain.model.Template;
import com.beyond.bycontract.template.domain.repository.TemplateRepository;
import com.beyond.bycontract.template.infrastructure.entity.TemplateEntity;
import com.beyond.bycontract.template.infrastructure.mapper.TemplatePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TemplateRepositoryAdapter implements TemplateRepository {

	private final JpaTemplateRepository repository;

	@Override
	public Template createTemplate(Template template) {
		TemplateEntity entity = repository.save(TemplatePersistenceMapper.toEntity(template));
		return TemplatePersistenceMapper.reconstituteDomain(entity);
	}

	@Override
	public List<Template> getAllTemplates() {
		return repository.findAll().stream().map(TemplatePersistenceMapper::reconstituteDomain).toList();
	}

	@Override
	public Optional<Template> findTemplateById(UUID id) {
		return repository.findById(id).map(TemplatePersistenceMapper::reconstituteDomain);
	}
}
