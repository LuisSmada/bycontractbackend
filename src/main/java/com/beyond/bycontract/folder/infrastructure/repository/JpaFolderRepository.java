package com.beyond.bycontract.folder.infrastructure.repository;

import com.beyond.bycontract.folder.domain.model.Folder;
import com.beyond.bycontract.folder.domain.repository.FolderRepository;
import com.beyond.bycontract.folder.infrastructure.entity.FolderEntity;
import com.beyond.bycontract.folder.infrastructure.mapper.FolderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaFolderRepository implements FolderRepository {

	@Autowired
	private SpringDataFolderRepository springDataFolderRepository;

	public Folder createFolder(Folder folder) {
		FolderEntity folderEntity = FolderMapper.domainToEntity(folder);
		return FolderMapper.entityToDomain(springDataFolderRepository.save(folderEntity));
	}

	@Override
	public Optional<Folder> getFolderById(UUID id) {
		return springDataFolderRepository.findById(id).map(FolderMapper::entityToDomain);
	}

	@Override
	public void deleteFolderById(UUID id) {
		springDataFolderRepository.deleteById(id);
	}

	@Override
	public Folder updateFolder(Folder folder) {
		FolderEntity folderEntity = FolderMapper.domainToEntity(folder);
		return FolderMapper.entityToDomain(springDataFolderRepository.save(folderEntity));
	}

}
