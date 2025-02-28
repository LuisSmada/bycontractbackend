package com.beyond.bycontract.adapter.infrastructure.repository.file;

import com.beyond.bycontract.adapter.infrastructure.entity.FileEntity;
import com.beyond.bycontract.adapter.infrastructure.mapper.FileMapper;
import com.beyond.bycontract.adapter.infrastructure.mapper.FolderMapper;
import com.beyond.bycontract.adapter.infrastructure.repository.folder.SpringDataFolderRepository;
import com.beyond.bycontract.adapter.infrastructure.repository.user.SpringDataUserRepository;
import com.beyond.bycontract.domain.model.File;
import com.beyond.bycontract.domain.model.Folder;
import com.beyond.bycontract.domain.model.User;
import com.beyond.bycontract.domain.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Repository
public class JpaFileRepository implements FileRepository {


    private final SpringDataFileRepository springDataFileRepository;

    @Autowired
    public JpaFileRepository(SpringDataFileRepository springDataFileRepository) {
        this.springDataFileRepository = springDataFileRepository;
    }


    @Override
    public File createFile(File file) {
        File fileFound = springDataFileRepository.findById(file.getIdFile()).map(FileMapper::entityToDomain).get();
        if(fileFound != null) {
            throw new RuntimeException("This file already exists");
        }
        FileEntity fileEntity = FileMapper.domainToEntity(file);
        return FileMapper.entityToDomain(springDataFileRepository.save(fileEntity));
    }

    @Override
    public File updloadFile(File file) throws IOException {
        FileEntity fileEntity = FileMapper.domainToEntity(file);
        return FileMapper.entityToDomain(springDataFileRepository.save(fileEntity));
    }

    @Override
    public Optional<File> getFileById(String id) {
        return springDataFileRepository.findById(id).map(FileMapper::entityToDomain);
    }

    @Override
    public void deleteFileById(String id) {
        springDataFileRepository.deleteById(id);
    }

    @Override
    public File updateFile(String idFileToUpdate, File file) {
        File fileFound = springDataFileRepository.findById(idFileToUpdate).map(FileMapper::entityToDomain).orElseThrow(() -> new RuntimeException("File not found"));

        if(file.getFileName() != null && !file.getFileName().equals(fileFound.getFileName())) {
            fileFound.setFileName(file.getFileName());
        }

        if(file.getParentFolder() != null && !file.getParentFolder().equals(fileFound.getParentFolder())) {
            fileFound.setParentFolder(file.getParentFolder());
        }else if(file.getParentFolder() == null) {
            fileFound.setParentFolder(file.getParentFolder());
        }

        if(file.getSize() != 0) {
            fileFound.setSize(file.getSize());
        }

        FileEntity fileEntity = FileMapper.domainToEntity(fileFound);

        return FileMapper.entityToDomain(springDataFileRepository.save(fileEntity));
    }
}
