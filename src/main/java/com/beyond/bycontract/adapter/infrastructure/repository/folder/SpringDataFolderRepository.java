package com.beyond.bycontract.adapter.infrastructure.repository.folder;

import com.beyond.bycontract.adapter.infrastructure.entity.FolderEntity;
import com.beyond.bycontract.domain.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringDataFolderRepository extends JpaRepository<FolderEntity, String> {

}
