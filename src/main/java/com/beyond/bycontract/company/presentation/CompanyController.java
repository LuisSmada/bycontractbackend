package com.beyond.bycontract.company.presentation;

import com.beyond.bycontract.company.application.dto.CompanyResponse;
import com.beyond.bycontract.company.application.service.CompanyService;
import com.beyond.bycontract.company.presentation.dto.CreateCompanyRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

	private final CompanyService service;

	@Autowired
	public CompanyController(CompanyService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CompanyResponse create(@Valid @RequestBody CreateCompanyRequest request) {
		return service.create(request.toCommand());
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<CompanyResponse> getAllCompanies() {
		return service.getAllCompanies();
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable UUID id) {
		service.deleteById(id);
	}
}

