package com.beyond.bycontract.folder.infrastructure.mapper;

import com.beyond.bycontract.folder.domain.model.Folder;
import com.beyond.bycontract.folder.infrastructure.entity.FolderEntity;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;

public class FolderMapper {

	public static Folder entityToDomain(FolderEntity folderEntity) {
		if (folderEntity == null) return null;
		return new Folder(folderEntity.getId(), folderEntity.getName(), folderEntity.getCreatedAt(), folderEntity.getModifiedAt(), folderEntity.getSize(), folderEntity.getAuthor().getId(), folderEntity.getParentFolder().getId());
	}

	public static FolderEntity domainToEntity(Folder folder) {

		if (folder == null) return null;

		FolderEntity folderEntity = new FolderEntity();

		folderEntity.setName(folder.getName());
		folderEntity.setSize(folder.getSize());

		if (folderEntity.getAuthor() != null) {
			UserEntity userRef = new UserEntity();
			userRef.setId(folderEntity.getAuthor().getId());
			folderEntity.setAuthor(userRef);
		}

		if (folderEntity.getParentFolder() != null) {
			FolderEntity folderRef = new FolderEntity();
			folderRef.setId(folderEntity.getParentFolder().getId());
			folderEntity.setParentFolder(folderRef);
		}

		//createdAt and modifiedAt are not added because they are not updatable and not insertable through an entity,so it's useless to add it

		return folderEntity;
	}
}
