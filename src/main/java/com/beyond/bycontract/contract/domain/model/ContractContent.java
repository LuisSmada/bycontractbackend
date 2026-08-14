package com.beyond.bycontract.contract.domain.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.UUID;


public class ContractContent {

	private UUID id;
	private JsonNode body;
	private String plainText;
	private String signedPdfUrl;
	private LocalDateTime modifiedAt;

	public ContractContent() {
	}

	public ContractContent(JsonNode body, String plainText, String signedPdfUrl, LocalDateTime modifiedAt) {
		this.body = body;
		this.plainText = plainText;
		this.signedPdfUrl = signedPdfUrl;
		this.modifiedAt = modifiedAt;
	}

	public ContractContent(UUID id, JsonNode body, String plainText, String signedPdfUrl, LocalDateTime modifiedAt) {
		this.id = id;
		this.body = body;
		this.plainText = plainText;
		this.signedPdfUrl = signedPdfUrl;
		this.modifiedAt = modifiedAt;
	}

	public ContractContent create(JsonNode body, String plainText, LocalDateTime modifiedAt) {
		return new ContractContent(
				body, plainText, null, modifiedAt
		);
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(final UUID id) {
		this.id = id;
	}

	public JsonNode getBody() {
		return this.body;
	}

	public void setBody(final JsonNode body) {
		this.body = body;
	}

	public String getPlainText() {
		return this.plainText;
	}

	public void setPlainText(final String plainText) {
		this.plainText = plainText;
	}

	public String getSignedPdfUrl() {
		return this.signedPdfUrl;
	}

	public void setSignedPdfUrl(final String signedPdfUrl) {
		this.signedPdfUrl = signedPdfUrl;
	}

	public LocalDateTime getModifiedAt() {
		return this.modifiedAt;
	}

	public void setModifiedAt(final LocalDateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
}
