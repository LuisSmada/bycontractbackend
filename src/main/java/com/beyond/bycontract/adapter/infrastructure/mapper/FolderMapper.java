package com.beyond.bycontract.adapter.infrastructure.mapper;

import com.beyond.bycontract.adapter.infrastructure.entity.FolderEntity;
import com.beyond.bycontract.domain.model.Folder;

public class FolderMapper {

    public static Folder entityToDomain(FolderEntity folderEntity) {
        if (folderEntity == null) return null;
        return new Folder(folderEntity.getIdFolder(), folderEntity.getFolderName(), folderEntity.getCreatedAt(), folderEntity.getModifiedAt(), folderEntity.getSize(), UserMapper.entityToDomain(folderEntity.getUser()), entityToDomain(folderEntity.getParentFolder()));
    }

    public static FolderEntity domainToEntity(Folder folder) {

        if (folder == null) return null;

        FolderEntity folderEntity = new FolderEntity();
        if(folder.getIdFolder() != null) {
            folderEntity.setIdFolder(folder.getIdFolder());
        }
        folderEntity.setFolderName(folder.getFolderName());
        folderEntity.setSize(folder.getSize());

        //createdAt and modifiedAt are not added because they are not updatable and not insertable through an entity,so it's useless to add it

        folderEntity.setUser(UserMapper.domainToEntity(folder.getUser()));
        folderEntity.setParentFolder(folder.getParentFolder() != null ? domainToEntity(folder.getParentFolder()) : null);
        return folderEntity;
    }
}
