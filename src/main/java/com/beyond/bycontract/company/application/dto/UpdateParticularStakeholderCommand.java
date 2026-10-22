package com.beyond.bycontract.company.application.dto;

public record UpdateParticularStakeholderCommand(
        String firstName,
        String lastName,
        String email,
        String phone,
        String address
) {}