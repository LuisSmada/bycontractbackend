package com.beyond.bycontract.application.dto.file;

public class UpdateFileDto {
    private String fileName;
    private int size;
    private Long idUser;
    private String idParentFolder;

    public UpdateFileDto() {}

    public UpdateFileDto(String idFile, String fileName, int size, Long idUser, String idParentFolder) {
        this.fileName = fileName;
        this.size = size;
        this.idUser = idUser;
        this.idParentFolder = idParentFolder;
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
        return "UpdateFileDto{" +
                "fileName='" + fileName + '\'' +
                ", size=" + size +
                ", idUser=" + idUser +
                ", idParentFolder='" + idParentFolder + '\'' +
                '}';
    }
}
