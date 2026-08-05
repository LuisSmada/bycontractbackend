package com.beyond.bycontract.file.infrastructure.mapper;

import com.beyond.bycontract.file.domain.model.File;
import com.beyond.bycontract.file.infrastructure.entity.FileEntity;
import com.beyond.bycontract.folder.infrastructure.entity.FolderEntity;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;

public class FileMapper {
	public static File entityToDomain(FileEntity fileEntity) {
		// UUID id, String name, LocalDateTime createdAt, LocalDateTime modifiedAt, Long size, UUID idAuthor, UUID idParentFolder, String filePath
		return new File(fileEntity.getId(), fileEntity.getName(), fileEntity.getCreatedAt(), fileEntity.getModifiedAt(), fileEntity.getSize(), fileEntity.getUser().getId(), fileEntity.getParentFolder().getId(), fileEntity.getFilePath());
	}

	public static FileEntity domainToEntity(File file) {

		if (file == null) {
			return null;
		}
		FileEntity fileEntity = new FileEntity();
		fileEntity.setName(file.getName());
		fileEntity.setSize(file.getSize());
		fileEntity.setFilePath(file.getFilePath());

		//createdAt and modifiedAt are not added because they are not updatable and not insertable through an entity,so it's useless to add it

		if (file.getIdAuthor() != null) {
			UserEntity userReference = new UserEntity();
			userReference.setId(file.getIdAuthor());
			fileEntity.setUser(userReference);
		}

		if (file.getIdParentFolder() != null) {
			FolderEntity folderReference = new FolderEntity();
			folderReference.setId(file.getIdParentFolder());
			fileEntity.setParentFolder(folderReference);
		}

		return fileEntity;
	}

}
