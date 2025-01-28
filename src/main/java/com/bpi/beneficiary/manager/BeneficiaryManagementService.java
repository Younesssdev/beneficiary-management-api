package com.bpi.beneficiary.manager;

import com.bpi.beneficiary.client.Beneficiary;
import com.bpi.beneficiary.client.BeneficiaryOwner;
import com.bpi.beneficiary.client.Company;
import com.bpi.beneficiary.client.Person;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
public class BeneficiaryManagementService {

    private final Map<UUID, Company> companies = new HashMap<>();

    private final PersonService personService;

    @PostConstruct
    public void init(){
        // Create Persons
        Person mmeYvette = new Person("Yvette", "Mme", LocalDate.of(1980, 5, 15));
        Person mYves = new Person("Yves", "M.", LocalDate.of(1975, 10, 20));
        Person mmeZoe = new Person("Zoe", "Mme", LocalDate.of(1990, 3, 10));
        Person mXavier = new Person("Xavier", "M.", LocalDate.of(1985, 12, 5));

        Map<UUID, Person> people = new HashMap<>();
        people.put(mmeYvette.id(),mmeYvette);
        people.put(mYves.id(),mYves);
        people.put(mmeZoe.id(),mmeZoe);
        people.put(mXavier.id(),mXavier);

        personService.addPeople(people);

        // Create Companies
        Company societeA = new Company("Societe A");
        Company societeB = new Company("Societe B");
        Company societeC = new Company("Societe C");
        Company societeD = new Company("Societe D");


        //Societe A
        societeA.beneficiaries().add(new Beneficiary(mmeZoe, 10.0));
        societeA.beneficiaries().add(new Beneficiary(mXavier, 30.0));
        societeA.beneficiaries().add(new Beneficiary(societeB, 60.0));

        //Societe B
        societeB.beneficiaries().add(new Beneficiary(societeC, 50.0));
        societeB.beneficiaries().add(new Beneficiary(mmeZoe, 50.0));

        //Societe C
        societeC.beneficiaries().add(new Beneficiary(mmeYvette, 90.0));
        societeC.beneficiaries().add(new Beneficiary(societeD, 5.0));
        societeC.beneficiaries().add(new Beneficiary(mYves, 5.0));


        //Societe D
        companies.put(societeA.id(), societeA);
        companies.put(societeB.id(), societeB);
        companies.put(societeC.id(), societeC);
        companies.put(societeD.id(), societeD);
    }

    public BeneficiaryManagementService( PersonService personService) {
        this.personService = personService;
    }

    public Company getCompany(UUID id) {
        return companies.get(id);
    }

    public Map<UUID, Company> getAllCompanies() {
        return companies;
    }


    public List<Beneficiary> getCompanyBeneficiaries(UUID id, String filter) {
        Company company = companies.get(id);
        if (company == null) {
            return null;
        }

        return switch (filter == null || filter.isEmpty() ? "all" : filter) {
            case "all" -> company.beneficiaries();
            case "person" -> getAllPersonAsBeneficiaries(company);
            case "beneficial" -> getEffectiveBeneficiaries(company);
            // TODO Specify and customize error
            default -> throw new IllegalArgumentException("Invalid filter value: " + filter);
        };
    }

    private List<Beneficiary> getAllPersonAsBeneficiaries(Company company) {
        Map<Person, Double> personCapitalMap = new HashMap<>();
        company.beneficiaries().forEach(beneficiary ->
                collectAllPersonsWithCapital(beneficiary.beneficiary(), beneficiary.capitalPercentage(), personCapitalMap, new HashSet<>())
        );
        return personCapitalMap.entrySet().stream()
                .map(entry -> new Beneficiary(entry.getKey(), entry.getValue()))
                .toList();
    }

    private void collectAllPersonsWithCapital(BeneficiaryOwner owner, double currentPercentage,
                                             Map<Person, Double> personCapitalMap, Set<BeneficiaryOwner> visited) {
        if (!visited.add(owner)) {
            return;
        }

        switch (owner) {
            case Person person -> {
                personCapitalMap.merge(person, currentPercentage, Double::sum);
            }
            case Company company -> {
                company.beneficiaries().forEach(beneficiary -> {
                    double indirectPercentage = currentPercentage * (beneficiary.capitalPercentage() / 100);
                    collectAllPersonsWithCapital(beneficiary.beneficiary(), indirectPercentage, personCapitalMap, visited);
                });
            }
        }
    }

    private List<Beneficiary> getEffectiveBeneficiaries(Company company) {
        final double OWNERSHIP_THRESHOLD = 25.0;

        Map<BeneficiaryOwner, Double> individualOwnership = new HashMap<>();
        company.beneficiaries().forEach(beneficiary -> {
            BeneficiaryOwner beneficiaryOwner = beneficiary.beneficiary();
            switch (beneficiaryOwner) {
                case Person person -> individualOwnership.merge(person, beneficiary.capitalPercentage(), Double::sum);
                case Company subCompany -> calculateIndirectOwnership(
                        subCompany, beneficiary.capitalPercentage(), individualOwnership, new HashSet<>());
            }
        });

        return individualOwnership.entrySet().stream()
                .filter(entry -> entry.getValue() > OWNERSHIP_THRESHOLD && entry.getKey() instanceof Person)
                .map(entry -> new Beneficiary(entry.getKey(), entry.getValue()))
                .toList();
    }

    private void calculateIndirectOwnership(BeneficiaryOwner owner, double currentPercentage,
                                            Map<BeneficiaryOwner, Double> individualOwnership, Set<BeneficiaryOwner> visited) {
        if (!visited.add(owner)) {
            return;
        }
        if (owner instanceof Company company) {
            company.beneficiaries().forEach(beneficiary -> {
                double indirectPercentage = currentPercentage * (beneficiary.capitalPercentage() / 100);
                BeneficiaryOwner beneficiaryOwner = beneficiary.beneficiary();
                switch (beneficiaryOwner) {
                    case Person person -> individualOwnership.merge(person, indirectPercentage, Double::sum);
                    case Company subCompany -> calculateIndirectOwnership(
                            subCompany, indirectPercentage, individualOwnership, visited);
                }
            });
        }
    }

    public UUID addCompany(Company company) {
        UUID id = UUID.randomUUID();
        companies.put(id, new Company(id, company.name(), new java.util.ArrayList<>()));
        return id;
    }

    public boolean addBeneficiaryToCompany(UUID companyId, UUID beneficiaryId, double percentage){
        Company company = companies.get(companyId);
        if (company == null){
            return false;
        }
        BeneficiaryOwner beneficiary = personService.getPerson(beneficiaryId);
        if(beneficiary == null){
            beneficiary = companies.get(beneficiaryId);
            if (beneficiary == null){
                return false;
            }
        }
        double currentPercentage = company.beneficiaries().stream().mapToDouble(Beneficiary::capitalPercentage).sum();
        if(percentage + currentPercentage > 100){
            return false;
        }
        Beneficiary newBeneficiary = new Beneficiary(beneficiary, percentage);
        company.beneficiaries().add(newBeneficiary);
        return true;
    }

}