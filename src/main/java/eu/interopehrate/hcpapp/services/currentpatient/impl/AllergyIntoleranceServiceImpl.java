package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandAllergyIntolerance;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.AllergyIntoleranceInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.AllergyIntoleranceService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AllergyIntoleranceServiceImpl implements AllergyIntoleranceService {
    private CurrentPatient currentPatient;
    private HapiToCommandAllergyIntolerance hapiToCommandAllergyIntolerance;
    private List<AllergyIntoleranceInfoCommand> allergyIntoleranceInfoCommandList = new ArrayList<>();

    public AllergyIntoleranceServiceImpl(CurrentPatient currentPatient,
                                         HapiToCommandAllergyIntolerance hapiToCommandAllergyIntolerance) {
        this.currentPatient = currentPatient;
        this.hapiToCommandAllergyIntolerance = hapiToCommandAllergyIntolerance;
    }

    @Override
    public List<AllergyIntoleranceInfoCommand> allergyIntoleranceSection() {
        List<AllergyIntoleranceInfoCommand> allergyIntoleranceList = new ArrayList<>(allergyIntoleranceInfoCommandList);
        allergyIntoleranceList.addAll(currentPatient.allergyIntoleranceList()
                .stream()
                .map(hapiToCommandAllergyIntolerance::convert)
                .collect(Collectors.toList()));
        return allergyIntoleranceList;
    }

    @Override
    public void insertAllergyIntolerance(AllergyIntoleranceInfoCommand allergyIntoleranceInfoCommand) {
        allergyIntoleranceInfoCommandList.add(allergyIntoleranceInfoCommand);
    }
}
