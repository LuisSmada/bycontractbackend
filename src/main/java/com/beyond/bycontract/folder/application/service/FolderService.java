package com.beyond.bycontract.folder.application.service;

import com.beyond.bycontract.file.domain.exception.FileNotFoundException;
import com.beyond.bycontract.folder.domain.exception.FolderAlreadyExistsException;
import com.beyond.bycontract.folder.domain.exception.FolderNotFoundException;
import com.beyond.bycontract.folder.domain.model.Folder;
import com.beyond.bycontract.folder.domain.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class FolderService {

	private final FolderRepository folderRepository;
	@Value("${file.upload-dir}")
	String uploadDir;

	@Autowired
	public FolderService(FolderRepository folderRepository) {
		this.folderRepository = folderRepository;
	}

	public Folder createFolder(Folder folder) throws IOException {
		String subDir = uploadDir;

		if (folder.getId() != null) {
			Optional<Folder> folderFound = folderRepository.getFolderById(folder.getId());
			if (folderFound.isPresent()) {
				throw new FolderAlreadyExistsException("This folder already exists");
			}
		}

		if (folder.getIdParentFolder() != null) {
			Folder parentFolder = folderRepository.getFolderById(folder.getIdParentFolder())
					.orElseThrow(() -> new FolderNotFoundException("Parent folder not found in database"));

			subDir = uploadDir + "/" + parentFolder.getName();
			Path parentFolderPath = Paths.get(uploadDir, parentFolder.getName());

			if (!Files.exists(parentFolderPath)) {
				Files.createDirectories(parentFolderPath);
			}
		}

		Path folderPath = Paths.get(subDir, folder.getName());
		Files.createDirectories(folderPath);

		return folderRepository.createFolder(folder);
	}

	public Optional<Folder> getFolderById(UUID id) {
		return folderRepository.getFolderById(id);
	}

	public void deleteFolderById(UUID id) throws IOException {
		Folder folder = folderRepository.getFolderById(id).orElseThrow(() -> new FolderNotFoundException("Folder not found in the database"));

		Path folderPath = Paths.get(uploadDir, folder.getName());
		if (Files.exists(folderPath)) {
			File directory = new File(folderPath.toString());
			File[] subFiles = directory.listFiles();
			if (subFiles != null) {
				for (File subFile : subFiles) {
					Files.delete(subFile.toPath());
				}
			}
			Files.delete(folderPath);
		} else {
			throw new FolderNotFoundException("Folder not found on disk");
		}

		folderRepository.deleteFolderById(id);
	}

	public Folder updateFolder(UUID idFolderToUpdate, Folder folderUpdateData) throws IOException {

		// 1. Récupération propre via le Repository
		Folder existingFolder = folderRepository.getFolderById(idFolderToUpdate)
				.orElseThrow(() -> new FolderNotFoundException("Folder not found with ID: " + idFolderToUpdate));

		boolean folderNeedsPhysicalMove = false;

		// CRITIQUE : On calcule et on sauvegarde l'ancien chemin AVANT de modifier l'objet
		Path oldFolderPath = Paths.get(calculateFolderPath(existingFolder));

		// 2. Mise à jour du Nom
		if (folderUpdateData.getName() != null && !folderUpdateData.getName().equals(existingFolder.getName())) {
			existingFolder.setName(folderUpdateData.getName());
			folderNeedsPhysicalMove = true;
		}

		// 3. Mise à jour du Parent (via UUID)
		if (!Objects.equals(folderUpdateData.getIdParentFolder(), existingFolder.getIdParentFolder())) {
			existingFolder.setIdParentFolder(folderUpdateData.getIdParentFolder());
			folderNeedsPhysicalMove = true;
		}

		// 4. Mise à jour de la taille (On suppose que size est un Long comme vu précédemment)
		if (folderUpdateData.getSize() != null && folderUpdateData.getSize() >= 0 && !folderUpdateData.getSize().equals(existingFolder.getSize())) {
			existingFolder.setSize(folderUpdateData.getSize());
		}

		// 5. Synchronisation Physique sur le disque dur
		if (folderNeedsPhysicalMove) {
			// On calcule le NOUVEAU chemin maintenant que l'objet a ses nouvelles données
			Path newFolderPath = Paths.get(calculateFolderPath(existingFolder));

			if (Files.exists(oldFolderPath)) {
				// Sécurité : S'assurer que le dossier parent cible existe physiquement
				Files.createDirectories(newFolderPath.getParent());

				// Déplace/Renomme le dossier ET tout ce qu'il contient (fichiers, sous-dossiers)
				Files.move(oldFolderPath, newFolderPath, StandardCopyOption.REPLACE_EXISTING);
			} else {
				throw new FileNotFoundException("Physical folder missing on disk: " + oldFolderPath);
			}
		}

		// 6. Sauvegarde en Base de Données
		return folderRepository.updateFolder(existingFolder);
	}

	private String calculateFolderPath(Folder folder) {
		Path basePath = Paths.get(uploadDir);

		if (folder.getIdParentFolder() != null) {
			// Il faut récupérer le parent en base pour connaître son nom
			Folder parentFolder = folderRepository.getFolderById(folder.getIdParentFolder())
					.orElseThrow(() -> new FolderNotFoundException("Parent folder not found"));

			basePath = Paths.get(uploadDir, parentFolder.getName());
		}

		return basePath.resolve(folder.getName()).toString();
	}
}
