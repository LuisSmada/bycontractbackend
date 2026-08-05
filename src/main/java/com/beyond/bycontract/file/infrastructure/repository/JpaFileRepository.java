package com.beyond.bycontract.file.infrastructure.repository;

import com.beyond.bycontract.file.domain.model.File;
import com.beyond.bycontract.file.domain.repository.FileRepository;
import com.beyond.bycontract.file.infrastructure.entity.FileEntity;
import com.beyond.bycontract.file.infrastructure.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaFileRepository implements FileRepository {


	private final SpringDataFileRepository springDataFileRepository;

	@Autowired
	public JpaFileRepository(SpringDataFileRepository springDataFileRepository) {
		this.springDataFileRepository = springDataFileRepository;
	}


	@Override
	public File createFile(File file) {
		FileEntity fileEntity = FileMapper.domainToEntity(file);
		return FileMapper.entityToDomain(springDataFileRepository.save(fileEntity));
	}

	@Override
	public File updloadFile(File file) throws IOException {
		FileEntity fileEntity = FileMapper.domainToEntity(file);
		return FileMapper.entityToDomain(springDataFileRepository.save(fileEntity));
	}

	@Override
	public Optional<File> getFileById(UUID id) {
		return springDataFileRepository.findById(id).map(FileMapper::entityToDomain);
	}

	@Override
	public void deleteFileById(UUID id) {
		springDataFileRepository.deleteById(id);
	}

	@Override
	public File updateFile(File file) {

		FileEntity fileEntity = FileMapper.domainToEntity(file);
		return FileMapper.entityToDomain(springDataFileRepository.save(fileEntity));
	}
}
