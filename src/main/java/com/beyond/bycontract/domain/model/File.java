package com.beyond.bycontract.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

public class File {
    private String idFile;
    private String fileName;
    private LocalDateTime createdAt;
    private int size;
    private User user;
    private Folder parentFolder;

    public File() {}

    public File(String idFile, String fileName, LocalDateTime createdAt, int size, User user, Folder parentFolder) {
        this.idFile = idFile;
        this.fileName = fileName;
        this.createdAt = createdAt;
        this.size = size;
        this.user = user;
        this.parentFolder = parentFolder;
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

    @Override
    public String toString() {
        return "File{" +
                "idFile=" + idFile +
                ", fileName='" + fileName + '\'' +
                ", createdAt=" + createdAt +
                ", size=" + size +
                ", user=" + user +
                ", parentFolder=" + parentFolder +
                '}';
    }
}
