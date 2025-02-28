package com.beyond.bycontract.application.service;

import com.beyond.bycontract.adapter.infrastructure.mapper.FolderMapper;
import com.beyond.bycontract.domain.model.File;
import com.beyond.bycontract.domain.model.Folder;
import com.beyond.bycontract.domain.model.User;
import com.beyond.bycontract.domain.repository.FileRepository;
import com.beyond.bycontract.domain.repository.FolderRepository;
import com.beyond.bycontract.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        fileToSave.setFilePath(uploadDir + file.getFileName());
        return fileRepository.createFile(fileToSave);
    }

    public List<String> getPath (Folder folder) {
        List<String> listPath = new ArrayList<>();
        listPath.add(folder.getFolderName());
        if(folder.getParentFolder() != null){
            listPath.add(folder.getParentFolder().getFolderName());
            return getPath(folder.getParentFolder());
        }

        return listPath;
    }

    public File uploadFile(String idFile, Long idUser, String idParentFolder, MultipartFile file) throws IOException {

        String subDir = uploadDir;

        //Find the parent folder
        Folder parentFolder = null;
        if(idParentFolder != null) {
            Optional<Folder> folderFound = folderRepository.getFolderById(idParentFolder);
            if(folderFound.isPresent()) {

                parentFolder = folderFound.get();

                //System.out.println(getPath(parentFolder));

                subDir = uploadDir + "/" + parentFolder.getFolderName();
                Path parentFolderPath = Paths.get(subDir);

                //Set the new size of parent folder
                int parentFolderSize = parentFolder.getSize() + (int) file.getSize();
                parentFolder.setSize(parentFolderSize);
                parentFolder.setModifiedAt(LocalDateTime.now());
                folderRepository.updateFolder(idParentFolder, parentFolder);

                if(!Files.exists(parentFolderPath)) {
                    Files.createDirectories(parentFolderPath);
                }
            }
        }


        //Set the file path
        Path filePath = Paths.get(subDir, file.getOriginalFilename());
        Files.write(filePath, file.getBytes());

        //Find the user
        User user = userRepository.getUserByid(idUser).get();

        File fileToUpload = new File();
        fileToUpload.setIdFile(idFile);
        fileToUpload.setParentFolder(parentFolder);
        fileToUpload.setUser(user);
        fileToUpload.setFileName(file.getOriginalFilename());
        fileToUpload.setSize((int) file.getSize());
        fileToUpload.setFilePath(filePath.toString());

        return fileRepository.updloadFile(fileToUpload);
    }

    public Optional<File> getFileById(String id) {
        return fileRepository.getFileById(id);
    }

    public void deleteFileById(String id) throws IOException {
        Optional<File> fileOpt = fileRepository.getFileById(id);
        File file = fileOpt.get();
        Path path = Paths.get(file.getFilePath());
        if(Files.exists(path)) {
            Files.delete(path);
        } else {
            throw new FileNotFoundException("File not found on disk");
        }
        fileRepository.deleteFileById(id);
    }

    public File updateFile(String idFileToUpdate, File file) {
        return fileRepository.updateFile(idFileToUpdate, file);
    }
}
