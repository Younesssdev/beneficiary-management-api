package com.bpi.beneficiary.controller;


import com.bpi.beneficiary.client.Company;
import com.bpi.beneficiary.client.Beneficiary;

import com.bpi.beneficiary.manager.BeneficiaryManagementService;
import com.bpi.beneficiary.manager.PersonService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CompanyController {

    private final BeneficiaryManagementService beneficiaryManagementService;

    private final PersonService personService;

    public CompanyController(BeneficiaryManagementService beneficiaryManagementService, PersonService personService) {
        this.beneficiaryManagementService = beneficiaryManagementService;
        this.personService = personService;
    }

    @GetMapping("/companies/{id}/beneficiaries")
    public ResponseEntity<?> getCompanyBeneficiaries(@PathVariable UUID id, @RequestParam(required = false) String filter) {
        List<Beneficiary> result = beneficiaryManagementService.getCompanyBeneficiaries(id, filter);

        if (result == null) {
            return new ResponseEntity<>("Company not found", HttpStatus.NOT_FOUND);
        }
        if (result.isEmpty()) {
            return new ResponseEntity<>("No beneficiaries found with the specified filter", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/companies/{companyId}/beneficiaries/{beneficiaryId}")
    public ResponseEntity<?> addBeneficiary(@PathVariable UUID companyId, @PathVariable UUID beneficiaryId, @RequestParam double percentage) {
        boolean success = beneficiaryManagementService.addBeneficiaryToCompany(companyId, beneficiaryId, percentage);
        if (success) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        Company company = beneficiaryManagementService.getCompany(companyId);
        if (company == null) {
            return new ResponseEntity<>("Company not found", HttpStatus.NOT_FOUND);
        }
        Object beneficiary = personService.getPerson(beneficiaryId);
        if(beneficiary == null){
            beneficiary = beneficiaryManagementService.getCompany(beneficiaryId);
            if (beneficiary == null){
                return new ResponseEntity<>("Beneficiary not found", HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>("Beneficiary percentage exceeds 100%", HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/companies")
    public ResponseEntity<?> createCompany(@RequestBody Company company, UriComponentsBuilder uriBuilder) {
        if(company == null || company.name() == null || company.name().isEmpty()){
            return new ResponseEntity<>("Company name is mandatory", HttpStatus.BAD_REQUEST);
        }

        UUID id = beneficiaryManagementService.addCompany(company);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriBuilder.path("/companies/{id}").buildAndExpand(id).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);

    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<?> findCompany(@PathVariable UUID id){
        Company company = beneficiaryManagementService.getCompany(id);
        if (company == null) {
            return new ResponseEntity<>("Company not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @GetMapping("/companies")
    public ResponseEntity<?> getAllCompanies() {
        Map<UUID, Company> allCompanies = beneficiaryManagementService.getAllCompanies();
        if (allCompanies.isEmpty()) {
            return new ResponseEntity<>("No company found", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allCompanies, HttpStatus.OK);
    }
}

