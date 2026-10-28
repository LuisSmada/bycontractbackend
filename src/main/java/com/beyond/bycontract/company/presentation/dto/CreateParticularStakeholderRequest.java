package com.beyond.bycontract.company.presentation.dto;

import com.beyond.bycontract.company.application.dto.CreateParticularStakeholderCommand;

import java.util.UUID;

public record CreateParticularStakeholderRequest(
        String firstName,
        String lastName,
        String email,
        String phone,
        String address
) {
    public CreateParticularStakeholderCommand toCommand(UUID idRequester) {
        return new CreateParticularStakeholderCommand(
                idRequester,
                this.firstName(),
                this.lastName(),
                this.email(),
                this.phone(),
                this.address()
        );
    }
}