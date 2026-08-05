package com.beyond.bycontract.template.infrastructure.entity;

import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "templates")
public class TemplateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private UUID id;

	@Column(name = "name")
	private String name;

	//@Column(columnDefinition = "jsonb", nullable = false)
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(nullable = false)
	private JsonNode body;


	@JdbcTypeCode(SqlTypes.JSON)
	@Column(nullable = true)
	private String variablesDefintion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_author")
	private UserEntity user;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;

	public TemplateEntity() {
	}

	public TemplateEntity(UUID id, String name, JsonNode body, String variablesDefintion, UserEntity user, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.id = id;
		this.name = name;
		this.body = body;
		this.variablesDefintion = variablesDefintion;
		this.user = user;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
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

	public String getVariablesDefintion() {
		return variablesDefintion;
	}

	public void setVariablesDefintion(String variablesDefintion) {
		this.variablesDefintion = variablesDefintion;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
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
		return "TemplateEntity{" +
				"id=" + id +
				", name='" + name + '\'' +
				", body='" + body + '\'' +
				", variablesDefintion='" + variablesDefintion + '\'' +
				", user=" + user +
				", createdAt=" + createdAt +
				", modifiedAt=" + modifiedAt +
				'}';
	}
}
