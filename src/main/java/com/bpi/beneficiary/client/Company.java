package com.bpi.beneficiary.client;

import java.util.List;
import java.util.UUID;

public record Company(UUID id, String name, List<Beneficiary> beneficiaries) {
    public Company(String name) {
        this(UUID.randomUUID(), name, new java.util.ArrayList<>());
    }
}