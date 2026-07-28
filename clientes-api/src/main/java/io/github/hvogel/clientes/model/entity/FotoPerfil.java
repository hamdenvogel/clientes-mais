package io.github.hvogel.clientes.model.entity;

import java.io.Serial;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "foto_perfil", schema = "meusservicos")
public class FotoPerfil implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "owner_type", nullable = false, length = 20)
    private String ownerType;

    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;

    @Column(name = "storage_provider", nullable = false, length = 30)
    private String storageProvider;

    @Column(name = "object_key", nullable = false, length = 500)
    private String objectKey;

    @Column(name = "file_name_original", length = 255)
    private String fileNameOriginal;

    @Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;

    @Column(name = "size_bytes", nullable = false)
    private Long sizeBytes;

    @Column(name = "sha256", length = 64)
    private String sha256;

    @Column(name = "largura")
    private Integer largura;

    @Column(name = "altura")
    private Integer altura;

    @Column(name = "ativa", nullable = false)
    private boolean ativa;

    @JdbcTypeCode(SqlTypes.VARBINARY)
    @Column(name = "data", columnDefinition = "bytea")
    private byte[] data;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

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

    public String getStorageProvider() {
        return storageProvider;
    }

    public void setStorageProvider(String storageProvider) {
        this.storageProvider = storageProvider;
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

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public Integer getLargura() {
        return largura;
    }

    public void setLargura(Integer largura) {
        this.largura = largura;
    }

    public Integer getAltura() {
        return altura;
    }

    public void setAltura(Integer altura) {
        this.altura = altura;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
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

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
        if (storageProvider == null) {
            storageProvider = "DB";
        }
        if (objectKey == null) {
            objectKey = UUID.randomUUID().toString();
        }
        ativa = true;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = new Date();
    }

    public static String sha256(byte[] content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content);
            StringBuilder builder = new StringBuilder();
            for (byte value : hash) {
                String hex = Integer.toHexString(0xff & value);
                if (hex.length() == 1) {
                    builder.append('0');
                }
                builder.append(hex);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 nao disponivel", e);
        }
    }

    public static String buildObjectKey(String ownerType, Integer ownerId, MultipartFile file) {
        String originalName = file.getOriginalFilename() == null ? "foto" : file.getOriginalFilename();
        String sanitizedName = originalName.replaceAll("\\s+", "-").replaceAll("[^a-zA-Z0-9._-]", "");
        return ownerType.toLowerCase() + "/" + ownerId + "/" + UUID.randomUUID() + "/" + sanitizedName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, ownerType, objectKey, storageProvider);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FotoPerfil other = (FotoPerfil) obj;
        return Objects.equals(id, other.id) && Objects.equals(ownerId, other.ownerId)
                && Objects.equals(ownerType, other.ownerType) && Objects.equals(objectKey, other.objectKey)
                && Objects.equals(storageProvider, other.storageProvider);
    }

    @Override
    public String toString() {
        return "FotoPerfil [id=" + id + ", ownerType=" + ownerType + ", ownerId=" + ownerId
                + ", storageProvider=" + storageProvider + ", objectKey=" + objectKey + ", fileNameOriginal="
                + fileNameOriginal + ", mimeType=" + mimeType + ", sizeBytes=" + sizeBytes + ", sha256=" + sha256
                + ", largura=" + largura + ", altura=" + altura + ", ativa=" + ativa + "]";
    }
}