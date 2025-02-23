package com.beyond.bycontract.application.dto.file;

public class SaveFileDto {

    private String idFile;
    private String fileName;
    private int size;
    private String idUser;
    private String idParentFolder;

    public SaveFileDto() {}

    public SaveFileDto(String idFile, String fileName, int size, String idUser, String idParentFolder) {
        this.idFile = idFile;
        this.fileName = fileName;
        this.size = size;
        this.idUser = idUser;
        this.idParentFolder = idParentFolder;
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
        return "SaveFileDto{" +
                "idFile='" + idFile + '\'' +
                ", fileName='" + fileName + '\'' +
                ", size=" + size +
                ", idUser='" + idUser + '\'' +
                ", idParentFolder='" + idParentFolder + '\'' +
                '}';
    }
}
