package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandAllergyIntolerance;
import eu.interopehrate.hcpapp.currentpatient.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.AllergyIntoleranceInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.AllergyIntoleranceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AllergyIntoleranceServiceImpl implements AllergyIntoleranceService {
    private CurrentPatient currentPatient;
    private HapiToCommandAllergyIntolerance hapiToCommandAllergyIntolerance;

    public AllergyIntoleranceServiceImpl(CurrentPatient currentPatient,
                                         HapiToCommandAllergyIntolerance hapiToCommandAllergyIntolerance) {
        this.currentPatient = currentPatient;
        this.hapiToCommandAllergyIntolerance = hapiToCommandAllergyIntolerance;
    }

    @Override
    public List<AllergyIntoleranceInfoCommand> allergiesIntolerancesSection() {
        return currentPatient.allergyIntoleranceList()
                .stream()
                .map(hapiToCommandAllergyIntolerance::convert)
                .collect(Collectors.toList());
    }
}
