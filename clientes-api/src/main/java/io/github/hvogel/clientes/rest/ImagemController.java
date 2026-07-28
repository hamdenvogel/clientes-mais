package io.github.hvogel.clientes.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.helpers.FileNameHelper;
import io.github.hvogel.clientes.model.entity.Documento;
import io.github.hvogel.clientes.model.entity.Imagem;
import io.github.hvogel.clientes.rest.dto.ImagemDTO;
import io.github.hvogel.clientes.rest.dto.InfoResponseDTO;
import io.github.hvogel.clientes.service.DocumentoService;
import io.github.hvogel.clientes.service.ImagemService;

import io.github.hvogel.clientes.util.Messages;

@RestController
@ConditionalOnProperty(prefix = "app.features", name = "legacy-image-enabled", havingValue = "true", matchIfMissing = true)
@RequestMapping("/api/imagem")
public class ImagemController {

	private static final String IMAGEM_NAO_ENCONTRADA = Messages.IMAGEM_NAO_ENCONTRADA;
	private static final String ERRO_IMAGEM_PADRAO = "Erro ao carregar imagem padrão";
	private static final String TITULO_INFORMACAO = "Informação";

	private final ImagemService imagemService;
	private FileNameHelper fileHelper = new FileNameHelper();
	private final DocumentoService documentoService;

	public ImagemController(ImagemService imagemService, FileNameHelper fileHelper, DocumentoService documentoService) {
		super();
		this.imagemService = imagemService;
		this.fileHelper = fileHelper;
		this.documentoService = documentoService;
	}

	/**
	 * Get all images information without data.
	 * 
	 * @return return list of all images information.
	 */
	@GetMapping
	public List<Imagem> obterTodos() {
		return imagemService.findAll();
	}

	@GetMapping("{id}")
	public Imagem obterPorId(@PathVariable Integer id) {
		return imagemService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						IMAGEM_NAO_ENCONTRADA));
	}

	@DeleteMapping("{id}")
	public InfoResponseDTO deletar(@PathVariable Integer id) {
		Imagem imagem = imagemService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						IMAGEM_NAO_ENCONTRADA));
		imagemService.deletar(imagem);

		return InfoResponseDTO.builder()
				.withMensagem("Imagem deletada com sucesso.")
				.withTitulo(TITULO_INFORMACAO)
				.build();
	}

	@GetMapping("consulta/{idDocumento}/{idChave}")
	public List<Imagem> obterPorDocumentoEChave(@PathVariable Integer idDocumento, @PathVariable Integer idChave) {
		return imagemService.findByChaveIdAndDocumentoId(idChave, idDocumento);
	}

	@DeleteMapping("documento-chave/{idDocumento}/{idChave}")
	public InfoResponseDTO deletar(@PathVariable Integer idDocumento, @PathVariable Integer idChave) {

		if (imagemService.getByChaveIdAndDocumentoId(idChave, idDocumento).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, IMAGEM_NAO_ENCONTRADA);
		}
		imagemService.deleteByChaveIdAndDocumentoId(idChave, idDocumento);

		return InfoResponseDTO.builder()
				.withMensagem("Imagem deletada com sucesso.")
				.withTitulo(TITULO_INFORMACAO)
				.build();
	}

	/**
	 * Upload single file to database.
	 * 
	 * @param file file data
	 * @return return saved image info with ImageResponse class.
	 */
	@PostMapping("upload/{idDocumento}/{idChave}")
	@ResponseStatus(HttpStatus.CREATED)
	public ImagemDTO uploadSingleFile(@RequestParam("file") MultipartFile file,
			@PathVariable Integer idDocumento,
			@PathVariable Integer idChave) {

		Documento documento = documentoService
				.obterPorId(idDocumento)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Código do Documento inexistente."));

		Imagem imagem = Imagem.buildImagem(file, fileHelper);
		imagem.setDocumento(documento);
		imagem.setChaveId(idChave);
		imagemService.save(imagem);
		return new ImagemDTO(imagem);
	}

	/**
	 * Upload multiple files to database.
	 * 
	 * @param files files data
	 * @return return saved images info list with ImageResponse class.
	 */
	@PostMapping("uploads/{idDocumento}/{idChave}")
	@ResponseStatus(HttpStatus.CREATED)
	public List<ImagemDTO> uploadMultiFiles(@RequestParam("files") MultipartFile[] files,
			@PathVariable Integer idDocumento,
			@PathVariable Integer idChave) {
		return Arrays.stream(files).map(file -> uploadSingleFile(file, idDocumento, idChave))
				.toList();
	}

	/**
	 * Sends valid or default image bytes with given fileName pathVariable.
	 * 
	 * @param fileName
	 * @return return valid byte array
	 */
	@GetMapping("show/{fileName}")
	public ResponseEntity<byte[]> getImagem(@PathVariable String fileName) {
		Imagem imagem = getImagemByName(fileName);
		return ResponseEntity.ok().contentType(MediaType.valueOf(imagem.getFileType())).body(imagem.getData());
	}

	/**
	 * Sends valid or default image bytes with given fileName request
	 * params.
	 * 
	 * @param name image name
	 * @return return valid byte array
	 */
	@GetMapping("show")
	public ResponseEntity<byte[]> getImageWithRequestParam(
			@RequestParam(required = false, value = "name") String name) {

		if (name != null) {
			Imagem imagem = getImagemByName(name);
			if (imagem == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,
						IMAGEM_NAO_ENCONTRADA);
			}
			return ResponseEntity.ok().contentType(MediaType.valueOf(imagem.getFileType())).body(imagem.getData());
		}
		try {
			Imagem defaultImage = Imagem.defaultImagem();
			return ResponseEntity.ok().contentType(MediaType.valueOf(defaultImage.getFileType()))
					.body(defaultImage.getData());
		} catch (java.io.IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERRO_IMAGEM_PADRAO, e);
		}
	}

	/**
	 * Sends valid or default scaled image bytes with given file name or uuid
	 * request params.
	 * 
	 * @param name   image name
	 * @param uuid   image uuid
	 * @param width  image width
	 * @param height image height
	 * @return return scaled valid byte array
	 */
	@GetMapping("show/{width}/{height}")
	public ResponseEntity<byte[]> getScaledImageWithRequestParam(@PathVariable int width, @PathVariable int height,
			@RequestParam(required = false, value = "uuid") String uuid,
			@RequestParam(required = false, value = "name") String name) {

		if (uuid != null) {
			Imagem imagem = getImagemByUuid(uuid, width, height);
			return ResponseEntity.ok().contentType(MediaType.valueOf(imagem.getFileType())).body(imagem.getData());
		}
		if (name != null) {
			Imagem imagem = getImagemByName(name, width, height);
			return ResponseEntity.ok().contentType(MediaType.valueOf(imagem.getFileType())).body(imagem.getData());
		}
		try {
			Imagem defImage = Imagem.defaultImagem(width, height);
			return ResponseEntity.ok().contentType(MediaType.valueOf(defImage.getFileType())).body(defImage.getData());
		} catch (java.io.IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERRO_IMAGEM_PADRAO, e);
		}
	}

	/**
	 * Sends valid or default scaled image bytes with given fileName.
	 * 
	 * @param fileName image name
	 * @param width    image width
	 * @param height   image height
	 * @return return valid byte array
	 */
	@GetMapping("show/{width}/{height}/{fileName:.+}")
	public ResponseEntity<byte[]> getScaledImagem(@PathVariable int width, @PathVariable int height,
			@PathVariable String fileName) {
		Imagem imagem = getImagemByName(fileName, width, height);
		return ResponseEntity.ok().contentType(MediaType.valueOf(imagem.getFileType())).body(imagem.getData());
	}

	/**
	 * get Image by name. If image is null return default image from asset.
	 * 
	 * @param name the name of image
	 * @return valid image or default image
	 */
	public Imagem getImagemByName(String name) {
		Imagem imagem = imagemService.findByFileName(name);
		if (imagem == null) {
			try {
				return Imagem.defaultImagem();
			} catch (java.io.IOException e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERRO_IMAGEM_PADRAO,
						e);
			}
		}
		return imagem;
	}

	/**
	 * get scaled Image by name, width and height. If image is null return default
	 * image from asset.
	 * 
	 * @param name   the name of image
	 * @param width  width size of image
	 * @param height height size of image
	 * @return valid scaled image or default scaled image
	 */
	public Imagem getImagemByName(String name, int width, int height) {
		Imagem imagem = imagemService.findByFileName(name);
		try {
			if (imagem == null) {
				Imagem defImage = Imagem.defaultImagem();
				defImage.scale(width, height);
				return defImage;
			}
			imagem.scale(width, height);
			return imagem;
		} catch (java.io.IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao processar imagem", e);
		}
	}

	/**
	 * get Image by uuid. If image is null return default image from asset.
	 * 
	 * @param uuid the uuid of image
	 * @return valid image or default image
	 */
	public Imagem getImagemByUuid(String uuid) {
		Imagem imagem = imagemService.findByUuid(uuid);
		if (imagem == null) {
			try {
				return Imagem.defaultImagem();
			} catch (java.io.IOException e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERRO_IMAGEM_PADRAO,
						e);
			}
		}
		return imagem;
	}

	/**
	 * get scaled Image by uuid, width and height. If image is null return default
	 * image from asset.
	 * 
	 * @param name   the uuid of image
	 * @param width  width size of image
	 * @param height height size of image
	 * @return valid scaled image or default scaled image
	 */
	public Imagem getImagemByUuid(String uuid, int width, int height) {
		Imagem imagem = imagemService.findByUuid(uuid);
		try {
			if (imagem == null) {
				Imagem defImage = Imagem.defaultImagem();
				defImage.scale(width, height);
				return defImage;
			}
			imagem.scale(width, height);
			return imagem;
		} catch (java.io.IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao processar imagem", e);
		}
	}

}
