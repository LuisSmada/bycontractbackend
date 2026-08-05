package com.beyond.bycontract.folder.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class ContractContent {
	private UUID id;
	private UUID idContract;
	// Utilisé pour stocker le JSON généré par l'éditeur texte (TipTap/ProseMirror)
	private String body;
	// Le texte brut sans formatage, essentiel pour la recherche et l'analyse IA
	private String plainText;
	// L'URL vers le fichier final figé (sur S3 par exemple) une fois signé
	private String signedPdfUrl;

	private Integer version = 1;

	private LocalDateTime modifiedAt;

	public ContractContent() {
	}

	public ContractContent(LocalDateTime modifiedAt, Integer version, String signedPdfUrl, String plainText, String body, UUID idContract, UUID id) {
		this.modifiedAt = modifiedAt;
		this.version = version;
		this.signedPdfUrl = signedPdfUrl;
		this.plainText = plainText;
		this.body = body;
		this.idContract = idContract;
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getIdContract() {
		return idContract;
	}

	public void setIdContract(UUID idContract) {
		this.idContract = idContract;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getPlainText() {
		return plainText;
	}

	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}

	public String getSignedPdfUrl() {
		return signedPdfUrl;
	}

	public void setSignedPdfUrl(String signedPdfUrl) {
		this.signedPdfUrl = signedPdfUrl;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(LocalDateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	@Override
	public String toString() {
		return "ContractContent{" +
				"id=" + id +
				", idContract=" + idContract +
				", body='" + body + '\'' +
				", plainText='" + plainText + '\'' +
				", signedPdfUrl='" + signedPdfUrl + '\'' +
				", version=" + version +
				", modifiedAt=" + modifiedAt +
				'}';
	}
}
