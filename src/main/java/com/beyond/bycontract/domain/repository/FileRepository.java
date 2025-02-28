package com.beyond.bycontract.domain.repository;

import com.beyond.bycontract.domain.model.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface FileRepository {

    File createFile(File file);

    File updloadFile(File file) throws IOException;

    Optional<File> getFileById(String id);

    void deleteFileById(String id);

    File updateFile(String idFileToUpdate, File file);
}
