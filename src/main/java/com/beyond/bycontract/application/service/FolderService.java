package com.beyond.bycontract.application.service;

import com.beyond.bycontract.adapter.infrastructure.exception.folder.FolderAlreadyExistsException;
import com.beyond.bycontract.adapter.infrastructure.exception.folder.FolderNotFoundException;
import com.beyond.bycontract.adapter.infrastructure.mapper.FolderMapper;
import com.beyond.bycontract.domain.model.Folder;
import com.beyond.bycontract.domain.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class FolderService {

    @Value("${file.upload-dir}")
    String uploadDir;

    private final FolderRepository folderRepository;

    @Autowired
    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public Folder createFolder(Folder folder) throws IOException {
        String subDir = uploadDir;

        Optional<Folder> folderFound = folderRepository.getFolderById(folder.getIdFolder());
        if(folderFound.isPresent()) {
            throw new FolderAlreadyExistsException("This folder already exists");
        }

        if(folder.getParentFolder() != null) {
            subDir = uploadDir + "/" + folder.getParentFolder().getFolderName();
            Path parentFolderPath = Paths.get(uploadDir, folder.getParentFolder().getFolderName());
            if(!Files.exists(parentFolderPath)) {
                Files.createDirectories(parentFolderPath);
            }


        }

        Path folderPath = Paths.get(subDir, folder.getFolderName());
        Files.createDirectories(folderPath);

        return folderRepository.createFolder(folder);
    }

    public Optional<Folder> getFolderById(String id) {
        return folderRepository.getFolderById(id);
    }

    public void deleteFolderById(String id) throws IOException {
        Optional<Folder> folderOpt = folderRepository.getFolderById(id);
        Folder folder = folderOpt.get();

        Path folderPath = Paths.get(uploadDir, folder.getFolderName());
        if(Files.exists(folderPath)) {
            File directory = new File(folderPath.toString());
            File[] subFiles = directory.listFiles();
            if(subFiles != null && subFiles.length > 0) {
                for (File subFile : subFiles) {
                    Files.delete(subFile.toPath());
                }
            }
            Files.delete(folderPath);
        } else {
            throw new FolderNotFoundException("Folder not found on disk");
        }

        folderRepository.deleteFolderById(id);
    }

    public Folder updateFolder(String idFolderToUpdate, Folder folder) {
        return folderRepository.updateFolder(idFolderToUpdate, folder);
    }
}
