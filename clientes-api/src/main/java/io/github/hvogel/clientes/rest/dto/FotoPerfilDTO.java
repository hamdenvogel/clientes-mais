package io.github.hvogel.clientes.rest.dto;

import java.util.Date;

import io.github.hvogel.clientes.model.entity.FotoPerfil;

public class FotoPerfilDTO {

    private Integer id;
    private String ownerType;
    private Integer ownerId;
    private String objectKey;
    private String fileNameOriginal;
    private String mimeType;
    private Long sizeBytes;
    private boolean ativa;
    private Date createdAt;
    private Date updatedAt;

    public FotoPerfilDTO() {
    }

    public FotoPerfilDTO(FotoPerfil fotoPerfil) {
        if (fotoPerfil != null) {
            this.id = fotoPerfil.getId();
            this.ownerType = fotoPerfil.getOwnerType();
            this.ownerId = fotoPerfil.getOwnerId();
            this.objectKey = fotoPerfil.getObjectKey();
            this.fileNameOriginal = fotoPerfil.getFileNameOriginal();
            this.mimeType = fotoPerfil.getMimeType();
            this.sizeBytes = fotoPerfil.getSizeBytes();
            this.ativa = fotoPerfil.isAtiva();
            this.createdAt = fotoPerfil.getCreatedAt();
            this.updatedAt = fotoPerfil.getUpdatedAt();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getFileNameOriginal() {
        return fileNameOriginal;
    }

    public void setFileNameOriginal(String fileNameOriginal) {
        this.fileNameOriginal = fileNameOriginal;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(Long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}