package com.bpi.beneficiary.manager;

import com.bpi.beneficiary.client.Person;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PersonService {

    private final Map<UUID, Person> people = new HashMap<>();

    public Person getPerson(UUID id) {
        return people.get(id);
    }

    public UUID addPerson(Person person) {
        UUID id = UUID.randomUUID();
        people.put(id, new Person(id, person.firstName(), person.lastName(), person.birthDate()));
        return id;
    }

    public void addPeople(Map<UUID, Person> people){
        this.people.putAll(people);
    }

}