package com.beyond.bycontract.adapter.infrastructure.mapper;

import com.beyond.bycontract.adapter.infrastructure.entity.FolderEntity;
import com.beyond.bycontract.domain.model.Folder;

public class FolderMapper {

    public static Folder entityToDomain(FolderEntity folderEntity) {
        return new Folder(folderEntity.getIdFolder(), folderEntity.getFolderName(), folderEntity.getCreatedAt(), folderEntity.getSize(), UserMapper.entityToDomain(folderEntity.getUser()), entityToDomain(folderEntity.getParentFolder()));
    }

    public static FolderEntity domainToEntity(Folder folder) {
        return new FolderEntity(folder.getIdFolder(), folder.getFolderName(), folder.getCreatedAt(), folder.getSize(), UserMapper.domainToEntity(folder.getUser()), domainToEntity(folder.getParentFolder()));
    }
}
