package com.beyond.bycontract.adapter.infrastructure.repository.file;

import com.beyond.bycontract.adapter.infrastructure.entity.FileEntity;
import com.beyond.bycontract.adapter.infrastructure.mapper.FileMapper;
import com.beyond.bycontract.domain.model.File;
import com.beyond.bycontract.domain.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaFileRepository implements FileRepository {

    @Autowired
    private SpringDataFileRepository springDataFileRepository;

    @Override
    public File createFile(File file) {
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

        if(file.getSize() != 0 && fileFound.getSize() != 0) {
            fileFound.setSize(file.getSize());
        }

        FileEntity fileEntity = FileMapper.domainToEntity(fileFound);

        return FileMapper.entityToDomain(springDataFileRepository.save(fileEntity));
    }
}
