package io.github.hvogel.clientes.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.hvogel.clientes.service.DocService;

@RestController
@RequestMapping("/api/doc")
public class DocController {
	
	private final DocService docService;
	
	public DocController(DocService docService) {
		super();
		this.docService = docService;
	}
	
	@GetMapping("add-bookmarks")
	public void addBookMarks() {
		this.docService.addBookmarks();		
	}	
	
	@GetMapping("replace-bookmark")
	public void replaceBookmark() {
		this.docService.replaceBookmark();		
	}
	
	@GetMapping("remove-bookmark")
	public void removeBookmark() {
		this.docService.removeBookmark();		
	}
	
	@GetMapping("change-html")
	public void changeHTML() {
		this.docService.changeHTML();		
	}
	
	@GetMapping("load-html")
	public byte[] loadHTML() {
		return this.docService.loadHTML();		
	}

}
