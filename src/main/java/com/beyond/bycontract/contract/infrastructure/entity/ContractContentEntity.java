package com.beyond.bycontract.contract.infrastructure.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "contract_contents")
public class ContractContentEntity {

	@Id
	private UUID id;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name = "id_contract")
	private ContractEntity contract;

	@Column(name = "body", columnDefinition = "jsonb")
	private String body;

	@Column(name = "plain_text", columnDefinition = "TEXT")
	private String plainText;

	@Column(name = "signed_pdf_url")
	private String signedPdfUrl;

	@Column(name = "version", nullable = false)
	private Integer version = 1;

	@UpdateTimestamp
	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;

	public ContractContentEntity() {
	}

	public ContractContentEntity(UUID id, ContractEntity contract, String body, String plainText, String signedPdfUrl, Integer version, LocalDateTime modifiedAt) {
		this.id = id;
		this.contract = contract;
		this.body = body;
		this.plainText = plainText;
		this.signedPdfUrl = signedPdfUrl;
		this.version = version;
		this.modifiedAt = modifiedAt;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(LocalDateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getSignedPdfUrl() {
		return signedPdfUrl;
	}

	public void setSignedPdfUrl(String signedPdfUrl) {
		this.signedPdfUrl = signedPdfUrl;
	}

	public String getPlainText() {
		return plainText;
	}

	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public ContractEntity getContract() {
		return contract;
	}

	public void setContract(ContractEntity contract) {
		this.contract = contract;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ContractContentEntity{" +
				"id=" + id +
				", contract=" + contract +
				", body='" + body + '\'' +
				", plainText='" + plainText + '\'' +
				", signedPdfUrl='" + signedPdfUrl + '\'' +
				", version=" + version +
				", modifiedAt=" + modifiedAt +
				'}';
	}
}
