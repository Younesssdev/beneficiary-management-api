package com.bpi.beneficiary.client;

import java.time.LocalDate;
import java.util.UUID;

public record PersonBeneficiary(UUID id, String firstName, String lastName, LocalDate birthDate) {
    public PersonBeneficiary(String firstName, String lastName, LocalDate birthDate){
        this(UUID.randomUUID(), firstName, lastName, birthDate);
    }
}