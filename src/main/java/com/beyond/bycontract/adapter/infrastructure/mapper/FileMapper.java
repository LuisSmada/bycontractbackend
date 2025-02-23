package com.beyond.bycontract.adapter.infrastructure.mapper;

import com.beyond.bycontract.adapter.infrastructure.entity.FileEntity;
import com.beyond.bycontract.application.dto.file.SaveFileDto;
import com.beyond.bycontract.domain.model.File;

public class FileMapper {
    public static File entityToDomain(FileEntity fileEntity) {
        return new File(fileEntity.getIdFile(), fileEntity.getFileName(), fileEntity.getCreatedAt(), fileEntity.getSize(), UserMapper.entityToDomain(fileEntity.getUser()), FolderMapper.entityToDomain(fileEntity.getParentFolder()));
    }

    public static FileEntity domainToEntity(File file) {
        return new FileEntity(file.getIdFile(), file.getFileName(), file.getCreatedAt(), file.getSize(), UserMapper.domainToEntity(file.getUser()), FolderMapper.domainToEntity(file.getParentFolder()));
    }
}
