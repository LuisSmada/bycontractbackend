package com.beyond.bycontract.template.infrastructure.mapper;

import com.beyond.bycontract.template.domain.model.Template;
import com.beyond.bycontract.template.infrastructure.entity.TemplateEntity;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;

public class TemplatePersistenceMapper {

	public static Template reconstituteDomain(TemplateEntity entity) {
		return new Template(
				entity.getId(),
				entity.getName(),
				entity.getBody(),
				entity.getVariablesDefintion(),
				entity.getUser().getId(),
				entity.getCreatedAt(),
				entity.getModifiedAt()
		);
	}

	public static TemplateEntity toEntity(Template template) {
		TemplateEntity entity = new TemplateEntity();

		entity.setName(template.getName());
		entity.setBody(template.getBody());
		entity.setVariablesDefintion(template.getVariablesDefinition());

		if (template.getIdAuthor() != null) {
			UserEntity userEntity = new UserEntity();
			userEntity.setId(template.getIdAuthor());
			entity.setUser(userEntity);
		}

		return entity;
	}
}
