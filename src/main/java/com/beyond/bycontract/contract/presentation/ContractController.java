package com.beyond.bycontract.contract.presentation;

import com.beyond.bycontract.contract.application.dto.ContractResponse;
import com.beyond.bycontract.contract.application.dto.FindContractResponse;
import com.beyond.bycontract.contract.application.service.ContractService;
import com.beyond.bycontract.contract.presentation.dto.CreateContractRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	public ContractResponse create(@Valid @RequestBody CreateContractRequest request) {
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
}
