package com.beyond.bycontract.company.application.dto;

import java.util.UUID;

public record CreateParticularStakeholderCommand(
        UUID idRequester,
        String firstName,
        String lastName,
        String email,
        String phone,
        String address
) {}