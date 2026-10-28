package com.beyond.bycontract.contract.presentation;

import com.beyond.bycontract.company.application.dto.UpdateCompanyCommand;
import com.beyond.bycontract.contract.application.dto.ContractResponse;
import com.beyond.bycontract.contract.application.dto.FindContractResponse;
import com.beyond.bycontract.contract.application.dto.UpdateContractCommand;
import com.beyond.bycontract.contract.application.service.ContractService;
import com.beyond.bycontract.contract.presentation.dto.CreateContractRequest;
import com.beyond.bycontract.contract.presentation.dto.UpdateContractRequest;
import com.beyond.bycontract.shared.utils.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contracts")
public class ContractController {

	private final ContractService service;

	@Autowired
	public ContractController(ContractService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ContractResponse create(@Valid @RequestBody CreateContractRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) throws Exception {
		return service.create(request.toCommand(currentUser.getId()));
	}

	@GetMapping("/all-contracts")
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<ContractResponse> getAllContractsForAdmin() {
		return service.getAllContracts(null);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ContractResponse> getAllContracts(@AuthenticationPrincipal CustomUserDetails currentUser) {
		return service.getAllContracts(currentUser.getId());
	}

	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public FindContractResponse getContractBydId(@PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails currentUser) {
		return service.getContractById(id, currentUser.getId());
	}

	@PatchMapping("{id}")
	public FindContractResponse updateContractById(@PathVariable UUID id, @Valid @RequestBody UpdateContractRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) {
		UpdateContractCommand command = request.toCommand(id, currentUser.getId());
		return service.updateContractById(command);
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable UUID id) {
		service.deleteContractById(id);
	}

	@PostMapping(value="/{id}/signature", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public FindContractResponse uploadSignature(@PathVariable UUID id, @RequestParam("file") MultipartFile file, @AuthenticationPrincipal CustomUserDetails currentUser ) {
		if(file.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
		}

		return service.uploadSignedContract(id, file, currentUser.getId());
	}

	@GetMapping("/{id}/signature-url")
	public ResponseEntity<Map<String, String>> getSignatureUrl(
			@PathVariable UUID id,
			@AuthenticationPrincipal CustomUserDetails currentUser) {

		String presignedUrl = service.getSignedPdfUrl(id, currentUser.getId());

		// On renvoie un JSON simple : { "url": "https://s3.eu-west-3.amazonaws.com/..." }
		return ResponseEntity.ok(Map.of("url", presignedUrl));
	}
}
