package com.bpi.beneficiary.client;

import java.util.UUID;

public record Beneficiary(UUID id, Object beneficiary, double capitalPercentage) {
    public Beneficiary(Object beneficiary, double capitalPercentage) {
        this(UUID.randomUUID(), beneficiary, capitalPercentage);
    }
}