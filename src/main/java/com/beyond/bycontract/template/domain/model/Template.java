package com.beyond.bycontract.template.domain.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Template {
	private UUID id;
	private String name;
	private JsonNode body;
	private String variablesDefinition;
	private TemplateStatus status;
	private UUID idAuthor;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public Template() {
	}

	public static Template create(String name, TemplateStatus status , JsonNode body, UUID idAuthor) {
		return new Template(null, name, body, "{}", status, idAuthor, LocalDateTime.now(), LocalDateTime.now());
	}

	public void update(String name, TemplateStatus status, JsonNode body, String variablesDefinition) {
		if (name != null && !name.isBlank()) {
			this.name = name;
		}
		if (status != null) {
			this.status = status;
		}
		if (body != null) {
			this.body = body;
		}
		if (variablesDefinition != null) {
			this.variablesDefinition = variablesDefinition;
		}
		this.modifiedAt = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return "Template{" +
				"id=" + id +
				", name='" + name + '\'' +
				", body='" + body + '\'' +
				", variablesDefinition='" + variablesDefinition + '\'' +
				", idAuthor=" + idAuthor +
				", createdAt=" + createdAt +
				", modifiedAt=" + modifiedAt +
				'}';
	}
}
