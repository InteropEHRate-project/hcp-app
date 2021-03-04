package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandAllergyIntolerance;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyIntoleranceCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyIntoleranceInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.AllergyIntoleranceService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AllergyIntoleranceServiceImpl implements AllergyIntoleranceService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandAllergyIntolerance hapiToCommandAllergyIntolerance;
    private final List<AllergyIntoleranceInfoCommand> allergyIntoleranceInfoCommands = new ArrayList<>();

    public AllergyIntoleranceServiceImpl(CurrentPatient currentPatient,
                                         HapiToCommandAllergyIntolerance hapiToCommandAllergyIntolerance) {
        this.currentPatient = currentPatient;
        this.hapiToCommandAllergyIntolerance = hapiToCommandAllergyIntolerance;
    }

    @Override
    public AllergyIntoleranceCommand allergyIntoleranceInfoCommand() {
        var allergiesIntolerances = currentPatient.allergyIntoleranceList()
                .stream()
                .map(hapiToCommandAllergyIntolerance::convert)
                .collect(Collectors.toList());
        allergiesIntolerances.addAll(allergyIntoleranceInfoCommands);
        return AllergyIntoleranceCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .allergyIntoleranceList(allergiesIntolerances)
                .build();
    }

    @Override
    public void insertAllergyIntolerance(AllergyIntoleranceInfoCommand allergyIntoleranceInfoCommand) {
        allergyIntoleranceInfoCommands.add(allergyIntoleranceInfoCommand);
    }
}
