package com.beyond.bycontract.template.domain.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.UUID;


public class Template {
	private UUID id;
	private String name;
	private JsonNode body;
	// Stocke la configuration du formulaire (Quelles questions poser à l'utilisateur)
	private String variablesDefinition;
	private UUID idAuthor;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public Template() {
	}

	//Constructor fot the creation
	public Template(String name, JsonNode body, String variablesDefinition, UUID idAuthor, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.name = name;
		this.body = body;
		this.idAuthor = idAuthor;
		this.variablesDefinition = variablesDefinition;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public Template(UUID id, String name, JsonNode body, String variablesDefinition, UUID idAuthor, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.id = id;
		this.name = name;
		this.body = body;
		this.variablesDefinition = variablesDefinition;
		this.idAuthor = idAuthor;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public static Template create(String name, JsonNode body, UUID idAuthor) {
		return new Template(name, body, "{}", idAuthor, LocalDateTime.now(), LocalDateTime.now());
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JsonNode getBody() {
		return body;
	}

	public void setBody(JsonNode body) {
		this.body = body;
	}

	public String getVariablesDefinition() {
		return variablesDefinition;
	}

	public void setVariablesDefinition(String variablesDefinition) {
		this.variablesDefinition = variablesDefinition;
	}

	public UUID getIdAuthor() {
		return idAuthor;
	}

	public void setIdAuthor(UUID idAuthor) {
		this.idAuthor = idAuthor;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(LocalDateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
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
