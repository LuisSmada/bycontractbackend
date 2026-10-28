package com.beyond.bycontract.company.presentation;

import com.beyond.bycontract.company.application.dto.CompanyResponse;
import com.beyond.bycontract.company.application.dto.CreateCompanyCommand;
import com.beyond.bycontract.company.application.dto.UpdateCompanyCommand;
import com.beyond.bycontract.company.application.service.CompanyService;
import com.beyond.bycontract.company.presentation.dto.CreateCompanyRequest;
import com.beyond.bycontract.company.presentation.dto.UpdateCompanyRequest;
import com.beyond.bycontract.shared.utils.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies")
// VERROU GLOBAL : Par défaut, il faut au moins être connecté pour toucher à ce contrôleur
@PreAuthorize("isAuthenticated()")
public class CompanyController {

	private final CompanyService service;

	@Autowired
	public CompanyController(CompanyService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CompanyResponse create(@Valid @RequestBody CreateCompanyRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) {
		CreateCompanyCommand command = request.toCommand(currentUser.getId());
		return service.create(command);
	}

	@GetMapping("/all-companies")
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.OK)
	public List<CompanyResponse> getAllCompanies() {
		return service.getAllCompanies(null);
	}

	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public CompanyResponse getCompanyById(@PathVariable UUID id,@AuthenticationPrincipal CustomUserDetails currentUser) {
		return service.getCompanyById(id, currentUser.getId());
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails currentUser) {
		service.deleteById(id, currentUser.getId());
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<CompanyResponse> getMyCompanies(@AuthenticationPrincipal CustomUserDetails currentUser) {
		return service.getAllCompanies(currentUser.getId());
	}

	@PatchMapping("{idCompany}")
	public CompanyResponse updateCompanyById(@PathVariable UUID idCompany, @Valid @RequestBody UpdateCompanyRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) {
		UpdateCompanyCommand command = request.toCommand(idCompany, currentUser.getId());
		return service.updateCompanyById(command);
	}

	@GetMapping("/test-role")
	public ResponseEntity<String> testRole() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("LES AUTORITÉS DE L'UTILISATEUR SONT : " + auth.getAuthorities());
		return ResponseEntity.ok("Regarde la console !");
	}
}

