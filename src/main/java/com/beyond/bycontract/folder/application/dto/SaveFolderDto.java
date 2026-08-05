package com.beyond.bycontract.folder.application.dto;

import java.util.UUID;

public class SaveFolderDto {

	private UUID id;
	private String name;
	private long size;
	private UUID idAuthor;
	private UUID idParentFolder;

	public SaveFolderDto() {
	}

	public SaveFolderDto(UUID id, String name, long size, UUID idAuthor, UUID idParentFolder) {
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
