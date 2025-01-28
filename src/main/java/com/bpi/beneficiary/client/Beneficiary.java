package com.bpi.beneficiary.client;

import java.util.UUID;

public record Beneficiary(UUID id, BeneficiaryOwner beneficiary, double capitalPercentage) {
    public Beneficiary(BeneficiaryOwner beneficiary, double capitalPercentage) {
        this(UUID.randomUUID(), beneficiary, capitalPercentage);
    }
}