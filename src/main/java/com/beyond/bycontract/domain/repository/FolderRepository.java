package com.beyond.bycontract.domain.repository;

import com.beyond.bycontract.domain.model.Folder;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface FolderRepository {
    Folder createFolder(Folder folder);

    Optional<Folder> getFolderById(String id);

    void deleteFolderById(String id);

    Folder updateFolder(String idFolderToUpdate, Folder folder);
}
