package com.beyond.bycontract.company.application.dto;

public record CreateParticularStakeholderCommand(
        String firstName,
        String lastName,
        String email,
        String phone,
        String address
) {}