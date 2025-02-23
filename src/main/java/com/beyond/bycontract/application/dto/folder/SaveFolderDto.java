package com.beyond.bycontract.application.dto.folder;

public class SaveFolderDto {

    private String idFolder;
    private String fileName;
    private int size;
    private String idUser;
    private String idParentFolder;

    public SaveFolderDto() {}

    public SaveFolderDto(String idFolder, String fileName, int size, String idUser, String idParentFolder) {
        this.idFolder = idFolder;
        this.fileName = fileName;
        this.size = size;
        this.idUser = idUser;
        this.idParentFolder = idParentFolder;
    }

    public String getIdFolder() {
        return idFolder;
    }

    public void setIdFolder(String idFolder) {
        this.idFolder = idFolder;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdParentFolder() {
        return idParentFolder;
    }

    public void setIdParentFolder(String idParentFolder) {
        this.idParentFolder = idParentFolder;
    }

    @Override
    public String toString() {
        return "SaveFolderDto{" +
                "idFolder='" + idFolder + '\'' +
                ", fileName='" + fileName + '\'' +
                ", size=" + size +
                ", idUser='" + idUser + '\'' +
                ", idParentFolder='" + idParentFolder + '\'' +
                '}';
    }
}
