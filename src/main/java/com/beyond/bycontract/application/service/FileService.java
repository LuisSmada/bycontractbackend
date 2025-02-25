package com.beyond.bycontract.application.service;

import com.beyond.bycontract.domain.model.File;
import com.beyond.bycontract.domain.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public File createFile(File file) {
        return fileRepository.createFile(file);
    }

    public Optional<File> getFileById(String id) {
        return fileRepository.getFileById(id);
    }

    public void deleteFileById(String id) {
        fileRepository.deleteFileById(id);
    }

    public File updateFile(String idFileToUpdate, File file) {
        return fileRepository.updateFile(idFileToUpdate, file);
    }
}
