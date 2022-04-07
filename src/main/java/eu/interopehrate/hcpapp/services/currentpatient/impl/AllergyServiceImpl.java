package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityAllergy;
import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandAllergy;
import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandAllergy;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.AllergyEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.AllergyRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyInfoCommand;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import eu.interopehrate.hcpapp.services.currentpatient.AllergyService;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;
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
    private final CurrentD2DConnection currentD2DConnection;
    private final AuditInformationService auditInformationService;

    public AllergyServiceImpl(CurrentPatient currentPatient, HapiToCommandAllergy hapiToCommandAllergy,
                              AllergyRepository allergyRepository, CommandToEntityAllergy commandToEntityAllergy,
                              EntityToCommandAllergy entityToCommandAllergy, HealthCareProfessionalService healthCareProfessionalService,
                              CurrentD2DConnection currentD2DConnection, AuditInformationService auditInformationService) {
        this.currentPatient = currentPatient;
        this.hapiToCommandAllergy = hapiToCommandAllergy;
        this.allergyRepository = allergyRepository;
        this.commandToEntityAllergy = commandToEntityAllergy;
        this.entityToCommandAllergy = entityToCommandAllergy;
        this.healthCareProfessionalService = healthCareProfessionalService;
        this.currentD2DConnection = currentD2DConnection;
        this.auditInformationService = auditInformationService;
    }

    @Override
    public CurrentPatient getCurrentPatient() {
        return currentPatient;
    }

    @Override
    public CurrentD2DConnection getCurrentD2DConnection() {
        return currentD2DConnection;
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
        oldAllergy.setEndDate(allergyInfoCommand.getEndDate());
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
                displayElement.getExtension().clear();
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
            ((AllergyIntolerance) optional.get()).getReactionFirstRep().getManifestationFirstRep().setText(allergyInfoCommand.getSymptoms());
            ((AllergyIntolerance) optional.get()).getNoteFirstRep().setText(allergyInfoCommand.getComments());

            if ((((AllergyIntolerance) optional.get()).hasExtension() && ((AllergyIntolerance) optional.get()).getExtension().size() > 0) && Objects.nonNull(allergyInfoCommand.getEndDate())) {
                ((AllergyIntolerance) optional.get()).getExtension().get(0).setValue(new DateTimeType(Date.from(allergyInfoCommand.getEndDate()
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant())));
            } else if (Objects.nonNull(allergyInfoCommand.getEndDate())) {
                ((AllergyIntolerance) optional.get()).getExtension().add(new Extension());
                ((AllergyIntolerance) optional.get()).getExtension().get(0).setValue(new DateTimeType(Date.from(allergyInfoCommand.getEndDate()
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant())));
            }
        } else {
            log.error("Cannot be updated. Resource not found.");
        }
    }

    @Override
    public Resource callAllergies() {
        if (Objects.nonNull(this.currentD2DConnection.getTd2D())) {
            for (int i = 0; i < this.allergyRepository.findAll().size(); i++) {
                Condition vitalSigns = createAllergiesFromEntity(this.allergyRepository.findAll().get(i));
                this.currentPatient.getVitalSigns().getEntry().add(new Bundle.BundleEntryComponent().setResource(vitalSigns));
                this.currentPatient.getVitalSignsTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(vitalSigns));
                return vitalSigns;
            }
            auditInformationService.auditEvent(AuditEventType.SEND_TO_SEHR, "Auditing send Allergies to S-EHR");
            this.allergyRepository.deleteAll();
        } else {
            log.error("The connection with S-EHR is not established.");
        }
        return null;
    }

    private static Condition createAllergiesFromEntity(AllergyEntity allergyEntity) {
        Condition allergies = new Condition();

        allergies.setCode(new CodeableConcept());
        allergies.getCode().addChild("coding");
        allergies.getCode().setCoding(new ArrayList<>());
        allergies.getCode().getCoding().add(new Coding().setSystem("http://loinc").setCode(""));
        allergies.getCode().getCoding().get(0).setDisplay(allergyEntity.getName());

        allergies.addNote().setText(allergyEntity.getComments());
        allergies.addCategory().setText(allergyEntity.getCategory());
        allergies.setId(allergyEntity.getId().toString());


        allergies.getSubject().setReference(allergyEntity.getAuthor());
        allergies.addExtension().setUrl("http://interopehrate.eu/fhir/StructureDefinition/SignatureExtension-IEHR")
                .setValue(new Signature().setWho(allergies.getSubject().setReference(allergyEntity.getAuthor()))
                        .setTargetFormat("json").setSigFormat("application/jose"));

        return allergies;
    }
}
