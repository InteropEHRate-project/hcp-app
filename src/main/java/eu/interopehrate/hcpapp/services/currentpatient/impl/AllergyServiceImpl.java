package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityAllergy;
import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandAllergy;
import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandAllergy;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.AllergyEntity;
import eu.interopehrate.hcpapp.jpa.repositories.AllergyRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyInfoCommand;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import eu.interopehrate.hcpapp.services.currentpatient.AllergyService;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
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
        allergyInfoCommand.setAuthor(this.healthCareProfessionalService.getHealthCareProfessional().getFirstName() + " " + this.healthCareProfessionalService.getHealthCareProfessional().getLastName());
        this.allergyRepository.save(this.commandToEntityAllergy.convert(allergyInfoCommand));
    }

    @Override
    public List<AllergyInfoCommand> listOfNewAllergies() {
        return this.allergyRepository.findAll()
                .stream()
                .map(this.entityToCommandAllergy::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteNewAllergy(Long id) {
        this.allergyRepository.deleteById(id);
    }

    @Override
    public AllergyInfoCommand retrieveNewAllergyById(Long id) {
        return this.entityToCommandAllergy.convert(this.allergyRepository.getOne(id));
    }

    @Override
    public void updateNewAllergy(AllergyInfoCommand allergyInfoCommand) {
        AllergyEntity oldAllergy = this.allergyRepository.getOne(allergyInfoCommand.getId());
        oldAllergy.setAuthor(this.healthCareProfessionalService.getHealthCareProfessional().getFirstName() + " " + this.healthCareProfessionalService.getHealthCareProfessional().getLastName());
        oldAllergy.setCategory(allergyInfoCommand.getCategory());
        oldAllergy.setName(allergyInfoCommand.getName());
        oldAllergy.setType(allergyInfoCommand.getType());
        oldAllergy.setSymptoms(allergyInfoCommand.getSymptoms());
        oldAllergy.setComments(allergyInfoCommand.getComments());
        this.allergyRepository.save(oldAllergy);
    }

    @Override
    public void deleteAllergyFromSEHR(String id) {
        // delete from Original Bundle
        this.currentPatient.getPatientSummaryBundle()
                .getEntry()
                .removeIf(bundleEntryComponent ->
                        bundleEntryComponent.getResource().getResourceType().equals(ResourceType.AllergyIntolerance) && bundleEntryComponent.getResource().getId().equals(id));

        // delete from Translated Bundle
        this.currentPatient.getPatientSummaryBundleTranslated()
                .getEntry()
                .removeIf(bundleEntryComponent ->
                        bundleEntryComponent.getResource().getResourceType().equals(ResourceType.AllergyIntolerance) && bundleEntryComponent.getResource().getId().equals(id));
    }

    @Override
    public AllergyInfoCommand retrieveAllergyFromSEHRById(String id) {
        // retrieving allergy from the Original Bundle
        var allergy = this.currentPatient.getPatientSummaryBundle().getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.AllergyIntolerance) && resource.getId().equals(id)).findFirst();
        return allergy.map(resource -> this.hapiToCommandAllergy.convert((AllergyIntolerance) resource)).orElse(null);
    }

    @Override
    public void updateAllergyFromSEHR(AllergyInfoCommand allergyInfoCommand) {
        // update translated bundle
        var optional = this.currentPatient.getPatientSummaryBundleTranslated().getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.AllergyIntolerance) && resource.getId().equals(allergyInfoCommand.getIdFHIR())).findFirst();
        updateAllergyDetails(optional, allergyInfoCommand);

        // update original bundle
        optional = this.currentPatient.getPatientSummaryBundle().getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.AllergyIntolerance) && resource.getId().equals(allergyInfoCommand.getIdFHIR())).findFirst();
        updateAllergyDetails(optional, allergyInfoCommand);
    }

    private static void updateAllergyDetails(Optional<Resource> optional, AllergyInfoCommand allergyInfoCommand) {
        if (optional.isPresent()) {

            // deletes translation if the allergy's name is different
            StringType displayElement = ((AllergyIntolerance) optional.get()).getCode().getCodingFirstRep().getDisplayElement();
            if (displayElement.hasExtension() && !displayElement.getValue().equalsIgnoreCase(allergyInfoCommand.getName())) {
                displayElement.getExtension().clear();  // DE SCHIMBAT AFISAREA TRADUCERII
            }
            ((AllergyIntolerance) optional.get()).getCode().getCodingFirstRep().setDisplay(allergyInfoCommand.getName());
            try {
                ((AllergyIntolerance) optional.get()).getCategory().get(0).setValue(AllergyIntolerance.AllergyIntoleranceCategory.valueOf(allergyInfoCommand.getCategory().toUpperCase()));
            } catch (IllegalArgumentException e) {
                log.error("no type available");
                e.printStackTrace();
            }
            try {
                ((AllergyIntolerance) optional.get()).setType(AllergyIntolerance.AllergyIntoleranceType.valueOf(allergyInfoCommand.getType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                log.error("no type available");
                e.printStackTrace();
            }
            // DE SETAT SI ALTE VALORI

        } else {
            log.error("Cannot be updated. Resource not found.");
        }
    }
}
