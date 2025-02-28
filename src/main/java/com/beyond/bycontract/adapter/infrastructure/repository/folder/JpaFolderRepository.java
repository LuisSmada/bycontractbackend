package com.beyond.bycontract.adapter.infrastructure.repository.folder;

import com.beyond.bycontract.adapter.infrastructure.entity.FolderEntity;
import com.beyond.bycontract.adapter.infrastructure.exception.folder.FolderAlreadyExistsException;
import com.beyond.bycontract.adapter.infrastructure.mapper.FolderMapper;
import com.beyond.bycontract.domain.model.Folder;
import com.beyond.bycontract.domain.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaFolderRepository implements FolderRepository {

    @Autowired
    private SpringDataFolderRepository springDataFolderRepository;

    public Folder createFolder(Folder folder) {
        FolderEntity folderEntity = FolderMapper.domainToEntity(folder);
        return FolderMapper.entityToDomain(springDataFolderRepository.save(folderEntity));
    }

    @Override
    public Optional<Folder> getFolderById(String id) {
        return springDataFolderRepository.findById(id).map(FolderMapper::entityToDomain);
    }

    @Override
    public void deleteFolderById(String id) {
        springDataFolderRepository.deleteById(id);
    }

    @Override
    public Folder updateFolder(String idFolderToUpdate, Folder folder) {
        Folder folderFound = springDataFolderRepository.findById(idFolderToUpdate).map(FolderMapper::entityToDomain).orElseThrow(() -> new RuntimeException("Folder not found"));

        if(folder.getFolderName() != null && !folder.getFolderName().equals(folderFound.getFolderName())) {
            folderFound.setFolderName(folder.getFolderName());
        }
        if(folder.getParentFolder() != null && !folder.getParentFolder().equals(folderFound.getParentFolder())) {
            folderFound.setParentFolder(folder.getParentFolder());
        }else if(folder.getParentFolder() == null) {
            folderFound.setParentFolder(folder.getParentFolder());
        }

        if(folder.getSize() != 0 && folder.getSize() != folderFound.getSize()) {
            folderFound.setSize(folder.getSize());
        }

        FolderEntity folderEntity = FolderMapper.domainToEntity(folderFound);

        return FolderMapper.entityToDomain(springDataFolderRepository.save(folderEntity));
    }

}
