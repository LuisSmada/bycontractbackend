package com.beyond.bycontract.template.presentation;

import com.beyond.bycontract.shared.utils.CustomUserDetails;
import com.beyond.bycontract.template.application.dto.FindTemplateResponse;
import com.beyond.bycontract.template.application.dto.TemplateResponse;
import com.beyond.bycontract.template.application.service.TemplateService;
import com.beyond.bycontract.template.presentation.dto.CreateTemplateRequest;
import com.beyond.bycontract.template.presentation.dto.UpdateTemplateRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
	public TemplateResponse createTemplate(@Valid @RequestBody CreateTemplateRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) {
		return service.createTemplate(request.toCommand(currentUser.getId()));
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

	@PatchMapping("/{id}")
	public ResponseEntity<FindTemplateResponse> updateTemplate(
			@PathVariable UUID id,
			@AuthenticationPrincipal CustomUserDetails currentUser,
			@RequestBody UpdateTemplateRequest request
	) {
		return ResponseEntity.ok(
				service.updateTemplateById(request.toCommand(id, currentUser.getId()))
		);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTemplate(@PathVariable UUID id,  @AuthenticationPrincipal CustomUserDetails currentUser
	) {
		service.deleteTemplateById(id, currentUser.getId());
		return ResponseEntity.noContent().build();
	}
}
