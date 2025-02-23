package com.beyond.bycontract.adapter.infrastructure.repository.folder;

import com.beyond.bycontract.adapter.infrastructure.entity.FolderEntity;
import com.beyond.bycontract.adapter.infrastructure.mapper.FolderMapper;
import com.beyond.bycontract.domain.model.Folder;
import com.beyond.bycontract.domain.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JpaFolderRepository implements FolderRepository {

    @Autowired
    private SpringDataFolderRepository springDataFolderRepository;

    public Folder createFolder(Folder folder) {
        FolderEntity folderEntity = FolderMapper.domainToEntity(folder);
        return FolderMapper.entityToDomain(springDataFolderRepository.save(folderEntity));
    }

}
