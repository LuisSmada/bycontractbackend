package com.beyond.bycontract.file.infrastructure.repository;

import com.beyond.bycontract.file.infrastructure.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataFileRepository extends JpaRepository<FileEntity, UUID> {
}
