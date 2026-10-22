package com.beyond.bycontract.company.presentation.dto;

import com.beyond.bycontract.company.application.dto.UpdateParticularStakeholderCommand;

public record UpdateParticularStakeholderRequest(
        String firstName,
        String lastName,
        String email,
        String phone,
        String address
) {
    public UpdateParticularStakeholderCommand toCommand() {
        return new UpdateParticularStakeholderCommand(
                this.firstName(),
                this.lastName(),
                this.email(),
                this.phone(),
                this.address()
        );
    }
}
