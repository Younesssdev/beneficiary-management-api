package com.bpi.beneficiary.manager;

import com.bpi.beneficiary.client.PersonBeneficiary;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PersonService {

    private final Map<UUID, PersonBeneficiary> people = new HashMap<>();

    public PersonBeneficiary getPerson(UUID id) {
        return people.get(id);
    }

    public UUID addPerson(PersonBeneficiary person) {
        UUID id = UUID.randomUUID();
        people.put(id, new PersonBeneficiary(id, person.firstName(), person.lastName(), person.birthDate()));
        return id;
    }

}