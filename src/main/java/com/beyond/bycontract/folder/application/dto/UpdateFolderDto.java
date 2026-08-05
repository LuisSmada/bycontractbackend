package com.beyond.bycontract.folder.application.dto;

import java.util.UUID;

public class UpdateFolderDto {

	private String name;
	private long size;
	private UUID idParentFolder;

	public UpdateFolderDto() {
	}

	public UpdateFolderDto(String name, long size, UUID idParentFolder) {
		this.name = name;
		this.size = size;
		this.idParentFolder = idParentFolder;
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

	public UUID getIdParentFolder() {
		return idParentFolder;
	}

	public void setIdParentFolder(UUID idParentFolder) {
		this.idParentFolder = idParentFolder;
	}
}
