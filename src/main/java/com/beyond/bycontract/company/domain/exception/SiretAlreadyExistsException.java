package com.beyond.bycontract.company.domain.exception;

import jakarta.persistence.EntityExistsException;

public class SiretAlreadyExistsException extends EntityExistsException {
	public SiretAlreadyExistsException(String siret) {
		super("A company already exists with the siret " + siret);
	}
}
