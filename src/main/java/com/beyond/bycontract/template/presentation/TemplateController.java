package com.beyond.bycontract.template.presentation;

import com.beyond.bycontract.template.application.dto.FindTemplateResponse;
import com.beyond.bycontract.template.application.dto.TemplateResponse;
import com.beyond.bycontract.template.application.service.TemplateService;
import com.beyond.bycontract.template.presentation.dto.CreateTemplateRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/templates")
public class TemplateController {
	private final TemplateService service;

	@Autowired
	public TemplateController(TemplateService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TemplateResponse createTemplate(@Valid @RequestBody CreateTemplateRequest request) {
		return service.createTemplate(request.toCommand());
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<TemplateResponse> getAllTemplates() {
		return service.getAllTemplates();
	}

	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public FindTemplateResponse findTemplateById(@PathVariable UUID id) {
		return service.findTemplateById(id);
	}
}
