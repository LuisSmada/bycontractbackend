package com.beyond.bycontract.file.application.service;

import com.beyond.bycontract.file.domain.model.File;
import com.beyond.bycontract.file.domain.repository.FileRepository;
import com.beyond.bycontract.folder.domain.model.Folder;
import com.beyond.bycontract.folder.domain.repository.FolderRepository;
import com.beyond.bycontract.user.domain.model.User;
import com.beyond.bycontract.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

	private final FileRepository fileRepository;
	private final FolderRepository folderRepository;
	private final UserRepository userRepository;

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Autowired
	public FileService(FileRepository fileRepository, FolderRepository folderRepository, UserRepository userRepository) {
		this.fileRepository = fileRepository;
		this.folderRepository = folderRepository;
		this.userRepository = userRepository;
	}

	public File createFile(File file) {
		File fileToSave = file;
		fileToSave.setFilePath(uploadDir + file.getName());
		return fileRepository.createFile(fileToSave);
	}

//    public List<String> getPath (Folder folder) {
//        List<String> listPath = new ArrayList<>();
//        listPath.add(folder.getName());
//        if(folder.getIdParentFolder() != null){
//            listPath.add(folder.getParentFolder().getFolderName());
//            return getPath(folder.getParentFolder());
//        }
//
//        return listPath;
//    }

	public File uploadFile(UUID idFile, UUID idAuthor, UUID idParentFolder, MultipartFile file) throws IOException {

		String subDir = uploadDir;

		//Find the parent folder
		Folder parentFolder = null;
		if (idParentFolder != null) {
			Optional<Folder> folderFound = folderRepository.getFolderById(idParentFolder);
			if (folderFound.isPresent()) {

				parentFolder = folderFound.get();

				//System.out.println(getPath(parentFolder));

				subDir = uploadDir + "/" + parentFolder.getName();
				Path parentFolderPath = Paths.get(subDir);

				//Set the new size of parent folder
				long parentFolderSize = parentFolder.getSize() + (int) file.getSize();
				parentFolder.setSize(parentFolderSize);
				parentFolder.setModifiedAt(LocalDateTime.now());
				folderRepository.updateFolder(parentFolder);

				if (!Files.exists(parentFolderPath)) {
					Files.createDirectories(parentFolderPath);
				}
			}
		}


		//Set the file path
		Path filePath = Paths.get(subDir, file.getOriginalFilename());
		Files.write(filePath, file.getBytes());

		//Find the user
		User user = userRepository.getUserById(idAuthor).get();

		File fileToUpload = new File();
		fileToUpload.setId(idFile);
		fileToUpload.setIdParentFolder(parentFolder.getId());
		fileToUpload.setIdAuthor(user.getId());
		fileToUpload.setName(file.getOriginalFilename());
		fileToUpload.setSize((int) file.getSize());
		fileToUpload.setFilePath(filePath.toString());

		return fileRepository.updloadFile(fileToUpload);
	}

	public Optional<File> getFileById(UUID id) {
		return fileRepository.getFileById(id);
	}

	public void deleteFileById(UUID id) throws IOException {
		Optional<File> fileOpt = fileRepository.getFileById(id);
		File file = fileOpt.get();
		Path path = Paths.get(file.getFilePath());
		if (Files.exists(path)) {
			Files.delete(path);
		} else {
			throw new FileNotFoundException("File not found on disk");
		}
		fileRepository.deleteFileById(id);
	}

	public File updateFile(UUID idFileToUpdate, File fileUpdateData) throws IOException {

		// 1. Appel au Repository (BDD)
		File existingFile = fileRepository.getFileById(idFileToUpdate)
				.orElseThrow(() -> new FileNotFoundException("File not found with ID: " + idFileToUpdate));

		boolean fileNeedsPhysicalMove = false;
		String oldFilePath = existingFile.getFilePath();

		// 2. Logique Métier
		if (fileUpdateData.getName() != null && !fileUpdateData.getName().equals(existingFile.getName())) {
			existingFile.setName(fileUpdateData.getName());
			fileNeedsPhysicalMove = true;
		}

		if (!Objects.equals(fileUpdateData.getIdParentFolder(), existingFile.getIdParentFolder())) {
			existingFile.setIdParentFolder(fileUpdateData.getIdParentFolder());
			fileNeedsPhysicalMove = true;
		}

		if (fileUpdateData.getSize() >= 0 && fileUpdateData.getSize() != existingFile.getSize()) {
			existingFile.setSize(fileUpdateData.getSize());
		}

		// 3. Logique d'Infrastructure Disque
		if (fileNeedsPhysicalMove) {
			String newFilePath = calculateNewFilePath(existingFile);

			Path oldPath = Paths.get(oldFilePath);
			Path newPath = Paths.get(newFilePath);

			if (Files.exists(oldPath)) {
				Files.createDirectories(newPath.getParent()); // S'assure que le dossier cible existe
				Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
				existingFile.setFilePath(newFilePath);
			} else {
				throw new FileNotFoundException("Physical file missing on disk: " + oldFilePath);
			}
		}

		// 4. Sauvegarde finale en BDD via le Repository
		return fileRepository.updateFile(existingFile);
	}

	private String calculateNewFilePath(File file) {
		Path basePath = Paths.get(uploadDir);

		if (file.getIdParentFolder() != null) {
			Folder parentFolder = folderRepository.getFolderById(file.getIdParentFolder())
					.orElseThrow(() -> new RuntimeException("Parent folder not found with ID: " + file.getIdParentFolder()));

			basePath = Paths.get(uploadDir, parentFolder.getName());
		}

		Path finalPath = basePath.resolve(file.getName());

		return finalPath.toString();
	}
}
