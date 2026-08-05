package com.beyond.bycontract.folder.domain.repository;

import com.beyond.bycontract.folder.domain.model.Folder;

import java.util.Optional;
import java.util.UUID;

public interface FolderRepository {
	Folder createFolder(Folder folder);

	Optional<Folder> getFolderById(UUID id);

	void deleteFolderById(UUID id);

	Folder updateFolder(Folder folder);
}
