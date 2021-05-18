package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityAllergy;
import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandAllergy;
import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandAllergy;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.repositories.AllergyRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyInfoCommand;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import eu.interopehrate.hcpapp.services.currentpatient.AllergyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AllergyServiceImpl implements AllergyService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandAllergy hapiToCommandAllergy;
    private final AllergyRepository allergyRepository;
    private final CommandToEntityAllergy commandToEntityAllergy;
    private final EntityToCommandAllergy entityToCommandAllergy;
    private final HealthCareProfessionalService healthCareProfessionalService;

    public AllergyServiceImpl(CurrentPatient currentPatient, HapiToCommandAllergy hapiToCommandAllergy,
                              AllergyRepository allergyRepository, CommandToEntityAllergy commandToEntityAllergy,
                              EntityToCommandAllergy entityToCommandAllergy, HealthCareProfessionalService healthCareProfessionalService) {
        this.currentPatient = currentPatient;
        this.hapiToCommandAllergy = hapiToCommandAllergy;
        this.allergyRepository = allergyRepository;
        this.commandToEntityAllergy = commandToEntityAllergy;
        this.entityToCommandAllergy = entityToCommandAllergy;
        this.healthCareProfessionalService = healthCareProfessionalService;
    }

    @Override
    public CurrentPatient getCurrentPatient() {
        return currentPatient;
    }

    // HARCODED METHOD
    @Override
    public AllergyCommand allergyInfoCommand() {
        var allergiesIntolerances = currentPatient.allergyIntoleranceList()
                .stream()
                .map(hapiToCommandAllergy::convert)
                .collect(Collectors.toList());
        return AllergyCommand.builder()
                .displayTranslatedVersion(Boolean.FALSE)
                .allergyList(allergiesIntolerances)
                .build();
    }

    // HARCODED METHOD
    @Override
    public AllergyCommand allergyInfoCommandTranslated() {
        var allergiesIntolerances = currentPatient.allergyIntoleranceTranslatedList()
                .stream()
                .map(hapiToCommandAllergy::convert)
                .collect(Collectors.toList());
        return AllergyCommand.builder()
                .displayTranslatedVersion(Boolean.TRUE)
                .allergyList(allergiesIntolerances)
                .build();
    }

    @Override
    public void insertAllergy(AllergyInfoCommand allergyInfoCommand) {
        allergyInfoCommand.setPatientId(this.currentPatient.getPatient().getId());
        allergyInfoCommand.setAuthor(healthCareProfessionalService.getHealthCareProfessional().getFirstName() + " " + healthCareProfessionalService.getHealthCareProfessional().getLastName());
        this.allergyRepository.save(this.commandToEntityAllergy.convert(allergyInfoCommand));
    }

    @Override
    public List<AllergyInfoCommand> listOfNewAllergies() {
        return this.allergyRepository.findAll()
                .stream()
                .map(this.entityToCommandAllergy::convert)
                .collect(Collectors.toList());
    }
}
