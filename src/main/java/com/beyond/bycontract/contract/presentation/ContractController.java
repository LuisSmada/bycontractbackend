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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
	public ContractResponse create(@Valid @RequestBody CreateContractRequest request) throws Exception {
		return service.create(request.toCommand());
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ContractResponse> getAllContracts() {
		return service.getAllContracts();
	}

	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public FindContractResponse getContractBydId(@PathVariable UUID id) {
		return service.getContractById(id);
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
}
