package com.bpi.beneficiary.client;

import java.time.LocalDate;
import java.util.UUID;

public record Person(UUID id, String firstName, String lastName, LocalDate birthDate) implements BeneficiaryOwner {
    public Person(String firstName, String lastName, LocalDate birthDate){
        this(UUID.randomUUID(), firstName, lastName, birthDate);
    }
}