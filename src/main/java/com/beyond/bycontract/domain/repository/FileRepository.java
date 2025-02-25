package com.beyond.bycontract.domain.repository;

import com.beyond.bycontract.domain.model.File;

import java.util.Optional;

public interface FileRepository {

    File createFile(File file);

    Optional<File> getFileById(String id);

    void deleteFileById(String id);

    File updateFile(String idFileToUpdate, File file);
}
