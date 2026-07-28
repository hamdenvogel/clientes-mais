package io.github.hvogel.clientes.model.entity;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import javax.imageio.ImageIO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import io.github.hvogel.clientes.helpers.FileNameHelper;
import io.github.hvogel.clientes.infra.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.hvogel.clientes.util.Messages;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "imagem", schema = "meusservicos")
public class Imagem extends BaseEntity {

	private static final Logger log = LoggerFactory.getLogger(Imagem.class);
	@Column(name = "file_name")
	private String fileName;

	@Column(name = "file_type")
	private String fileType;

	@Column(name = "size")
	private long size;

	@Column(name = "uuid")
	private String uuid;

	@Column(name = "system_name")
	private String systemName;

	@Lob
	@Column(name = "data")
	private byte[] data;

	@Column(name = "chave_id")
	private Integer chaveId;

	@ManyToOne
	@JoinColumn(name = "documento_id")
	private Documento documento;

	@Column(name = "original_file_name")
	private String originalFileName;

	public Imagem() {
		// Required by JPA
	}

	private static final String DEFAULT_VALUE = Messages.DEFAULT_VALUE;

	/**
	 * Create new Image class.
	 * 
	 * @return new Image.
	 */
	@Transient
	public static Imagem build() {
		String uuid = UUID.randomUUID().toString();
		Imagem imagem = new Imagem();
		Date now = new Date();
		imagem.setUuid(uuid);
		imagem.setCreatedDate(now);
		imagem.setUpdatedDate(now);
		imagem.setCreatedBy(DEFAULT_VALUE);
		imagem.setSystemName(DEFAULT_VALUE);
		imagem.setUpdatedBy(DEFAULT_VALUE);
		imagem.setStatus(true);
		return imagem;
	}

	@Transient
	public void setFiles(MultipartFile file) {
		setFileType(file.getContentType());
		setSize(file.getSize());
	}

	/**
	 * Scale image data with given width and height.
	 * 
	 * @param width  scale width
	 * @param height scale height
	 * @return scaled image byte array and change to class data.
	 * @throws IOException if resizing fails
	 */
	@Transient
	public byte[] scale(int width, int height) throws IOException {

		if (width == 0 || height == 0)
			return data;

		ByteArrayInputStream in = new ByteArrayInputStream(data);

		try {
			BufferedImage img = ImageIO.read(in);

			java.awt.Image scaledImage = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
			BufferedImage imgBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			imgBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			ImageIO.write(imgBuff, "jpg", buffer);
			setData(buffer.toByteArray());
			return buffer.toByteArray();

		} catch (IOException e) {
			throw new IOException("IOException in scale", e);
		}
	}

	/**
	 * Generate no context image with `notfound.jpg` image in asset.
	 * 
	 * @return create default image.
	 */
	@Transient
	public static Imagem defaultImagem() throws IOException {
		Resource resource = new ClassPathResource("images/notfound.jpg");
		InputStream is = resource.getInputStream();
		String fileType = "image/jpeg";
		byte[] bdata = FileCopyUtils.copyToByteArray(is);
		Imagem imagem = new Imagem();
		imagem.setFileType(fileType);
		imagem.setData(bdata);
		return imagem;

	}

	/**
	 * Generate scaled no context image with `notfound.jpg` image in asset with
	 * given width and height.
	 * 
	 * @param width  scale width
	 * @param height scale height
	 * @return create scaled default image.
	 */
	@Transient
	public static Imagem defaultImagem(int width, int height) throws IOException {
		Imagem defaultImagem = defaultImagem();
		defaultImagem.scale(width, height);
		return defaultImagem;
	}

	/**
	 * Generate scaled no context image with `notfound.jpg` image in asset with
	 * given width and height.
	 * 
	 * @param file   multipartfile data to build.
	 * @param helper filenamehelper class to generate name.
	 * @return return new Image class related with file.
	 */
	@Transient
	public static Imagem buildImagem(MultipartFile file, FileNameHelper helper) {
		String fileName = helper.generateDisplayName(file.getOriginalFilename());
		//
		Imagem imagem = Imagem.build();
		imagem.setFileName(fileName);
		imagem.setFiles(file);
		imagem.setOriginalFileName(file.getOriginalFilename());

		try {
			imagem.setData(file.getBytes());
		} catch (IOException e) {
			log.error("Error setting image data", e);
		}
		return imagem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(data);
		result = prime * result
				+ Objects.hash(chaveId, documento, fileName, fileType, originalFileName, size, systemName, uuid);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Imagem other = (Imagem) obj;
		return Objects.equals(chaveId, other.chaveId) && Arrays.equals(data, other.data)
				&& Objects.equals(documento, other.documento) && Objects.equals(fileName, other.fileName)
				&& Objects.equals(fileType, other.fileType) && Objects.equals(originalFileName, other.originalFileName)
				&& size == other.size && Objects.equals(systemName, other.systemName)
				&& Objects.equals(uuid, other.uuid);
	}

	@Override
	public String toString() {
		return "Imagem [fileName=" + fileName + ", fileType=" + fileType + ", size=" + size + ", uuid=" + uuid
				+ ", systemName=" + systemName + ", data=" + Arrays.toString(data) + ", chaveId=" + chaveId
				+ ", documento=" + documento + ", originalFileName=" + originalFileName + "]";
	}

}
