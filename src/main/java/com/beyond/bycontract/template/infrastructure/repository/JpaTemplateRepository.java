package com.beyond.bycontract.template.infrastructure.repository;

import com.beyond.bycontract.template.infrastructure.entity.TemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaTemplateRepository extends JpaRepository<TemplateEntity, UUID> {
}
