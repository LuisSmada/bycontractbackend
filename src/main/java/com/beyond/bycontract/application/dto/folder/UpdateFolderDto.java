package com.beyond.bycontract.application.dto.folder;

public class UpdateFolderDto {

    private String folderName;
    private int size;
    private String idParentFolder;

    public UpdateFolderDto() {}

    public UpdateFolderDto(String folderName, int size, String idParentFolder) {
        this.folderName = folderName;
        this.size = size;
        this.idParentFolder = idParentFolder;
    }


    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getIdParentFolder() {
        return idParentFolder;
    }

    public void setIdParentFolder(String idParentFolder) {
        this.idParentFolder = idParentFolder;
    }

    @Override
    public String toString() {
        return "UpdateFolderDto{" +
                "folderName='" + folderName + '\'' +
                ", size=" + size +
                ", idParentFolder='" + idParentFolder + '\'' +
                '}';
    }
}
