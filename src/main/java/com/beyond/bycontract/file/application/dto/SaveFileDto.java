package com.beyond.bycontract.file.application.dto;

import java.util.UUID;

public class SaveFileDto {

	private UUID id;
	private String name;
	private long size;
	private UUID idAuthor;
	private UUID idParentFolder;

	public SaveFileDto() {
	}

	public SaveFileDto(UUID id, String name, long size, UUID idAuthor, UUID idParentFolder) {
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

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
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
}
