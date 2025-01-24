package com.bpi.beneficiary.manager;

import com.bpi.beneficiary.client.Beneficiary;
import com.bpi.beneficiary.client.Company;
import com.bpi.beneficiary.client.PersonBeneficiary;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class BeneficiaryManagementService {

    private final Map<UUID, Company> companies = new HashMap<>();

    private final PersonService personService;

    public BeneficiaryManagementService( PersonService personService) {
        this.personService = personService;
    }

    public Company getCompany(UUID id) {
        return companies.get(id);
    }

    public Object getCompanyBeneficiaries(UUID id, String filter) {
        Company company = companies.get(id);
        if (company == null) {
            return null;
        }

        return switch (filter == null ? "all" : filter.toLowerCase()) {
            case "all", "" -> company;
            case "person" -> company.beneficiaries().stream()
                    .map(Beneficiary::beneficiary)
                    .filter(PersonBeneficiary.class::isInstance)
                    .map(PersonBeneficiary.class::cast)
                    .toList();
            case "company" -> Map.of(
                    "id", company.id(),
                    "name", company.name()
            );
            default -> Collections.emptyList();
        };
    }

    public UUID addCompany(Company company) {
        UUID id = UUID.randomUUID();
        companies.put(id, new Company(id, company.name(), new java.util.ArrayList<>()));
        return id;
    }

    public boolean addBeneficiaryToCompany(UUID companyId, UUID beneficiaryId, double percentage){
        Company company = companies.get(companyId);
        Object beneficiary = personService.getPerson(beneficiaryId);
        if(beneficiary == null){
            Company companyBeneficiary = companies.get(beneficiaryId);
            if(companyBeneficiary == null){
                return false;
            }
            beneficiary = companyBeneficiary;
        }
        if (company == null){
            return false;
        }
        if(percentage > (100 - company.beneficiaries().stream().mapToDouble(Beneficiary::capitalPercentage).sum())){
            return false;
        }
        Beneficiary newBeneficiary = new Beneficiary(beneficiary, percentage);
        company.beneficiaries().add(newBeneficiary);
        return true;
    }

}