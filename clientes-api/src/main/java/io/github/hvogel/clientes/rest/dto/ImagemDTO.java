package io.github.hvogel.clientes.rest.dto;

import io.github.hvogel.clientes.model.entity.Documento;
import io.github.hvogel.clientes.model.entity.Imagem;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImagemDTO {
	private String uuid;
	private String fileName;
	private String fileType;
	private long size;
	private Integer chaveId;
	private Documento documentoId;
	private String originalFileName;

	public ImagemDTO(Imagem imagem) {
		setUuid(imagem.getUuid());
		setFileName(imagem.getFileName());
		setFileType(imagem.getFileType());
		setSize(imagem.getSize());
		setChaveId(imagem.getChaveId());
		setDocumentoId(imagem.getDocumento());
		setOriginalFileName(imagem.getOriginalFileName());
	}
}
