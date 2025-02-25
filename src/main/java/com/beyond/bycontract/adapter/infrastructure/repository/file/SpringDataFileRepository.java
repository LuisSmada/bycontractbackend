package com.beyond.bycontract.adapter.infrastructure.repository.file;

import com.beyond.bycontract.adapter.infrastructure.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataFileRepository extends JpaRepository<FileEntity, String> {
}
