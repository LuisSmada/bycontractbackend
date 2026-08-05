package com.beyond.bycontract.file.domain.repository;

import com.beyond.bycontract.file.domain.model.File;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository {

	File createFile(File file);

	File updloadFile(File file) throws IOException;

	Optional<File> getFileById(UUID id);

	void deleteFileById(UUID id);

	File updateFile(File file);
}
