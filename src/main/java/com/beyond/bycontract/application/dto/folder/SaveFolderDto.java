package com.beyond.bycontract.application.dto.folder;

public class SaveFolderDto {

    private String idFolder;
    private String folderName;
    private int size;
    private Long idUser;
    private String idParentFolder;

    public SaveFolderDto() {}

    public SaveFolderDto(String idFolder, String folderName, int size, Long idUser, String idParentFolder) {
        this.idFolder = idFolder;
        this.folderName = folderName;
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

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
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
                ", fileName='" + folderName + '\'' +
                ", size=" + size +
                ", idUser='" + idUser + '\'' +
                ", idParentFolder='" + idParentFolder + '\'' +
                '}';
    }
}
