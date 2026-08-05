package com.beyond.bycontract.contract.presentation;

import com.beyond.bycontract.contract.application.dto.ContractResponse;
import com.beyond.bycontract.contract.application.service.ContractService;
import com.beyond.bycontract.contract.presentation.dto.CreateContractRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
