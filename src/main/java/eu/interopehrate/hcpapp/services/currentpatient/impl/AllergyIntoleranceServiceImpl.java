package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.AllergyIntoleranceInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.AllergyIntoleranceService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AllergyIntoleranceServiceImpl implements AllergyIntoleranceService {
    @Override
    public List<AllergyIntoleranceInfoCommand> allergiesIntolerancesSection() {
        AllergyIntoleranceInfoCommand allergiesIntolerances = new AllergyIntoleranceInfoCommand();
        allergiesIntolerances.setIdentifier("XXXXXX");
        allergiesIntolerances.setName("Penicillin");
        allergiesIntolerances.setClinicalStatus("Active");
        allergiesIntolerances.setType("Allergy");
        allergiesIntolerances.setCategory("Medication");
        allergiesIntolerances.setCriticality("High");

        return Collections.singletonList(allergiesIntolerances);
    }
}
