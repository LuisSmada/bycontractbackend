package com.beyond.bycontract.company.presentation.dto;

import com.beyond.bycontract.company.application.dto.CreateParticularStakeholderCommand;

public record CreateParticularStakeholderRequest(
        String firstName,
        String lastName,
        String email,
        String phone,
        String address
) {
    public CreateParticularStakeholderCommand toCommand() {
        return new CreateParticularStakeholderCommand(
                this.firstName(),
                this.lastName(),
                this.email(),
                this.phone(),
                this.address()
        );
    }
}