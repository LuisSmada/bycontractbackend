package com.beyond.bycontract.contract.domain.exception;

import java.util.UUID;

public class ContractNotfoundException extends RuntimeException {
	public ContractNotfoundException(String message) {
		super(message);
	}

	public ContractNotfoundException(UUID id) {
		super("Template with id " + id + " not found");
	}
}
