package com.beyond.bycontract.adapter.infrastructure.mapper;

import com.beyond.bycontract.adapter.infrastructure.entity.FileEntity;
import com.beyond.bycontract.domain.model.File;

public class FileMapper {
    public static File entityToDomain(FileEntity fileEntity) {
        return new File(fileEntity.getIdFile(), fileEntity.getFileName(), fileEntity.getCreatedAt(), fileEntity.getModifiedAt(), fileEntity.getSize(), UserMapper.entityToDomain(fileEntity.getUser()), FolderMapper.entityToDomain(fileEntity.getParentFolder()), fileEntity.getFilePath());
    }

    public static FileEntity domainToEntity(File file) {
        FileEntity fileEntity = new FileEntity();
        if(file.getIdFile() != null) {
            fileEntity.setIdFile(file.getIdFile());
        }
        fileEntity.setFileName(file.getFileName());
        fileEntity.setSize(file.getSize());

        //createdAt and modifiedAt are not added because they are not updatable and not insertable through an entity,so it's useless to add it

        fileEntity.setFilePath(file.getFilePath());
        fileEntity.setUser(UserMapper.domainToEntity(file.getUser()));
        fileEntity.setParentFolder(FolderMapper.domainToEntity(file.getParentFolder()));
        return fileEntity;
    }

}
