package com.beyond.bycontract.adapter.infrastructure.repository.folder;

import com.beyond.bycontract.adapter.infrastructure.entity.FolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataFolderRepository extends JpaRepository<FolderEntity, String> {
}
