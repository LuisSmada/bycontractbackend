package com.beyond.bycontract.folder.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Folder {
	private UUID id;
	private String name;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private Long size;
	private UUID idAuthor;
	private UUID idParentFolder;

	public Folder() {
	}

	public Folder(UUID id, String name, LocalDateTime createdAt, LocalDateTime modifiedAt, Long size, UUID idAuthor, UUID idParentFolder) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.size = size;
		this.idAuthor = idAuthor;
		this.idParentFolder = idParentFolder;
	}

	public Folder(UUID id, String name, Long size, UUID idAuthor, UUID idParentFolder) {
		this.id = id;
		this.name = name;
		this.size = size;
		this.idAuthor = idAuthor;
		this.idParentFolder = idParentFolder;
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

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public UUID getIdAuthor() {
		return idAuthor;
	}

	public void setIdAuthor(UUID idAuthor) {
		this.idAuthor = idAuthor;
	}

	public UUID getIdParentFolder() {
		return idParentFolder;
	}

	public void setIdParentFolder(UUID idParentFolder) {
		this.idParentFolder = idParentFolder;
	}

	@Override
	public String toString() {
		return "Folder{" +
				"id=" + id +
				", name='" + name + '\'' +
				", createdAt=" + createdAt +
				", modifiedAt=" + modifiedAt +
				", size=" + size +
				", idAuthor=" + idAuthor +
				", idParentFolder=" + idParentFolder +
				'}';
	}
}
