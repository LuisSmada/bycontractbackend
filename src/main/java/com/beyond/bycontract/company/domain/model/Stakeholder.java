package com.beyond.bycontract.company.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Stakeholder {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
