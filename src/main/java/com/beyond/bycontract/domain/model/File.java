package com.beyond.bycontract.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

public class File {
    private String idFile;
    private String fileName;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int size;
    private User user;
    private Folder parentFolder;
    private String filePath;

    public File() {}

    public File(String idFile, String fileName, LocalDateTime createdAt, LocalDateTime modifiedAt, int size, User user, Folder parentFolder, String filePath) {
        this.idFile = idFile;
        this.fileName = fileName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.size = size;
        this.user = user;
        this.parentFolder = parentFolder;
        this.filePath = filePath;
    }

    public File(String idFile, String fileName, int size, User user, Folder parentFolder) {
        this.idFile = idFile;
        this.fileName = fileName;
        this.size = size;
        this.user = user;
        this.parentFolder = parentFolder;
    }

    public String getIdFile() {
        return idFile;
    }

    public void setIdFile(String idFile) {
        this.idFile = idFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "File{" +
                "idFile='" + idFile + '\'' +
                ", fileName='" + fileName + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", size=" + size +
                ", user=" + user +
                ", parentFolder=" + parentFolder +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
