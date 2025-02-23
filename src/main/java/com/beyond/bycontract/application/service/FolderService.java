package com.beyond.bycontract.application.service;

import com.beyond.bycontract.domain.model.Folder;
import com.beyond.bycontract.domain.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FolderService {

    @Autowired
    private  FolderRepository folderRepository;

    public Folder createFolder(Folder folder) {
        return folderRepository.createFolder(folder);
    }
}
