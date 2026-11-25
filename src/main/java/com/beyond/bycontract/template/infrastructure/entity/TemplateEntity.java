package com.beyond.bycontract.template.infrastructure.entity;

import com.beyond.bycontract.template.domain.model.TemplateStatus;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "templates")
@Getter
@Setter
@AllArgsConstructor
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
	private String variablesDefinition;

	@Enumerated(EnumType.STRING)
	@Column(name = "template_status", nullable = false)
	private TemplateStatus status;

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


	@Override
	public String toString() {
		return "TemplateEntity{" +
				"id=" + id +
				", name='" + name + '\'' +
				", body='" + body + '\'' +
				", variablesDefinition='" + variablesDefinition + '\'' +
				", user=" + user +
				", createdAt=" + createdAt +
				", modifiedAt=" + modifiedAt +
				'}';
	}
}
