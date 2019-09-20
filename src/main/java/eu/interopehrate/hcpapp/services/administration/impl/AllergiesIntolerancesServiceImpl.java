package eu.interopehrate.hcpapp.services.administration.impl;

import eu.interopehrate.hcpapp.mvc.commands.AllergiesIntolerancesInfoCommand;
import eu.interopehrate.hcpapp.services.administration.AllergiesIntolerancesService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AllergiesIntolerancesServiceImpl implements AllergiesIntolerancesService {
    @Override
    public List<AllergiesIntolerancesInfoCommand> allergiesIntolerancesSection() {
        AllergiesIntolerancesInfoCommand allergiesIntolerances = new AllergiesIntolerancesInfoCommand();
        allergiesIntolerances.setIdentifier("XXXXXX");
        allergiesIntolerances.setName("Penicillin");
        allergiesIntolerances.setClinicalStatus("Active");
        allergiesIntolerances.setType("Allergy");
        allergiesIntolerances.setCategory("Medication");
        allergiesIntolerances.setCriticality("High");

        return Collections.singletonList(allergiesIntolerances);
    }
}
