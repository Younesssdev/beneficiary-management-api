package com.bpi.beneficiary.controller;


import com.bpi.beneficiary.client.PersonBeneficiary;

import com.bpi.beneficiary.manager.BeneficiaryManagementService;
import com.bpi.beneficiary.manager.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.UUID;


@RestController
@RequestMapping("/api")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/person-individual")
    public ResponseEntity<?> createPerson(@RequestBody PersonBeneficiary person, UriComponentsBuilder uriBuilder) {
        if(person == null || person.firstName() == null || person.firstName().isEmpty() ||
                person.lastName() == null || person.lastName().isEmpty()){
            return new ResponseEntity<>("Person name is mandatory", HttpStatus.BAD_REQUEST);
        }
        UUID id = personService.addPerson(person);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriBuilder.path("/person/individual/{id}").buildAndExpand(id).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<?> findPerson(@PathVariable UUID id){
        PersonBeneficiary person = personService.getPerson(id);
        if (person == null) {
            return new ResponseEntity<>("Person not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(person, HttpStatus.OK);
    }
}