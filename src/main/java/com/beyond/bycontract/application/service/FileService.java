package com.beyond.bycontract.application.service;

import com.beyond.bycontract.domain.model.File;
import com.beyond.bycontract.domain.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public File createFile(File file) {
        return fileRepository.createFile(file);
    }
}
