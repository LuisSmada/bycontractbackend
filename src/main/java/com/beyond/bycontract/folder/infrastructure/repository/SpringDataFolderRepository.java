package com.beyond.bycontract.folder.infrastructure.repository;

import com.beyond.bycontract.folder.infrastructure.entity.FolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataFolderRepository extends JpaRepository<FolderEntity, UUID> {

}
