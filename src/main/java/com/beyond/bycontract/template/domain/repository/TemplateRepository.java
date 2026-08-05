package com.beyond.bycontract.template.domain.repository;

import com.beyond.bycontract.template.domain.model.Template;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TemplateRepository {

	Template createTemplate(Template template);

	List<Template> getAllTemplates();

	Optional<Template> findTemplateById(UUID id);
}
