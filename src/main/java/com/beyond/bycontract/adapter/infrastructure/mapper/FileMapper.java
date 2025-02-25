package com.beyond.bycontract.adapter.infrastructure.mapper;

import com.beyond.bycontract.adapter.infrastructure.entity.FileEntity;
import com.beyond.bycontract.domain.model.File;

public class FileMapper {
    public static File entityToDomain(FileEntity fileEntity) {
        return new File(fileEntity.getIdFile(), fileEntity.getFileName(), fileEntity.getCreatedAt(), fileEntity.getSize(), UserMapper.entityToDomain(fileEntity.getUser()), FolderMapper.entityToDomain(fileEntity.getParentFolder()));
    }

    public static FileEntity domainToEntity(File file) {
        FileEntity fileEntity = new FileEntity();
        if(file.getIdFile() != null) {
            fileEntity.setIdFile(file.getIdFile());
        }
        fileEntity.setFileName(file.getFileName());
        fileEntity.setCreatedAt(file.getCreatedAt());
        fileEntity.setSize(file.getSize());
        fileEntity.setUser(UserMapper.domainToEntity(file.getUser()));
        fileEntity.setParentFolder(FolderMapper.domainToEntity(file.getParentFolder()));
        return fileEntity;
    }

}
