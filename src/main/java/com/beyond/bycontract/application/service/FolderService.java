package com.beyond.bycontract.application.service;

import com.beyond.bycontract.domain.model.Folder;
import com.beyond.bycontract.domain.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;

    public Folder createFolder(Folder folder) {
        return folderRepository.createFolder(folder);
    }

    public Optional<Folder> getFolderById(String id) {
        return folderRepository.getFolderById(id);
    }

    public void deleteFolderById(String id) {
        folderRepository.deleteFolderById(id);
    }

    public Folder updateFolder(String idFolderToUpdate, Folder folder) {
        return folderRepository.updateFolder(idFolderToUpdate, folder);
    }
}
