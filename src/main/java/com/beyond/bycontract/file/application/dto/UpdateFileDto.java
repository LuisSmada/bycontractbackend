package com.beyond.bycontract.file.application.dto;

import java.util.UUID;

public class UpdateFileDto {
	private String name;
	private int size;
	private UUID idAuthor;
	private UUID idParentFolder;

	public UpdateFileDto() {
	}

	public UpdateFileDto(UUID id, String name, int size, UUID idAuthor, UUID idParentFolder) {
		this.name = name;
		this.size = size;
		this.idAuthor = idAuthor;
		this.idParentFolder = idParentFolder;
	}

	public UpdateFileDto(String name, int size, UUID idAuthor, UUID idParentFolder) {
		this.name = name;
		this.size = size;
		this.idAuthor = idAuthor;
		this.idParentFolder = idParentFolder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
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
