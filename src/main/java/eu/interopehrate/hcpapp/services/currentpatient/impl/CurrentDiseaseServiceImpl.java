package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityCurrentDisease;
import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandCurrentDisease;
import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandCurrentDisease;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.CurrentDiseaseEntity;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.CurrentDiseaseRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.CurrentDiseaseService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CurrentDiseaseServiceImpl implements CurrentDiseaseService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandCurrentDisease hapiToCommandCurrentDisease;
    private final List<String> listOfNotes = new ArrayList<>();
    private final CommandToEntityCurrentDisease commandToEntityCurrentDisease;
    private final CurrentDiseaseRepository currentDiseaseRepository;
    private final EntityToCommandCurrentDisease entityToCommandCurrentDisease;
    private final CurrentD2DConnection currentD2DConnection;

    public CurrentDiseaseServiceImpl(CurrentPatient currentPatient, HapiToCommandCurrentDisease hapiToCommandCurrentDisease,
                                     CommandToEntityCurrentDisease commandToEntityCurrentDisease, CurrentDiseaseRepository currentDiseaseRepository,
                                     EntityToCommandCurrentDisease entityToCommandCurrentDisease, CurrentD2DConnection currentD2DConnection) {
        this.currentPatient = currentPatient;
        this.hapiToCommandCurrentDisease = hapiToCommandCurrentDisease;
        this.commandToEntityCurrentDisease = commandToEntityCurrentDisease;
        this.currentDiseaseRepository = currentDiseaseRepository;
        this.entityToCommandCurrentDisease = entityToCommandCurrentDisease;
        this.currentD2DConnection = currentD2DConnection;
    }

    @Override
    public CurrentPatient getCurrentPatient() {
        return this.currentPatient;
    }

    @Override
    public CurrentD2DConnection getCurrentD2DConnection() {
        return this.currentD2DConnection;
    }

    @Override
    public CurrentDiseaseCommand currentDiseasesSection() {
        var currentDiseasesList = this.currentPatient.conditionsList()
                .stream()
                .map(hapiToCommandCurrentDisease::convert)
                .collect(Collectors.toList());

        return CurrentDiseaseCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .currentDiseaseInfoCommand(currentDiseasesList)
                .listOfNotes(this.listOfNotes)
                .build();
    }

    @Override
    public void insertCurrentDisease(CurrentDiseaseInfoCommand currentDiseaseInfoCommand) {
        this.currentDiseaseRepository.save(this.commandToEntityCurrentDisease.convert(currentDiseaseInfoCommand));
    }

    @Override
    public void insertNote(String note) {
        if (note != null && !note.trim().equals("") && !this.listOfNotes.contains(note)) {
            this.listOfNotes.add(note);
        }
    }

    @Override
    public void deleteNote(String note) {
        this.listOfNotes.removeIf(x -> x.equals(note));
    }

    @Override
    public List<CurrentDiseaseInfoCommand> listNewCurrentDiseases() {
        return this.currentDiseaseRepository.findAll()
                .stream()
                .map(this.entityToCommandCurrentDisease::convert)
                .collect(Collectors.toList());
    }

    public CurrentDiseaseInfoCommand currentDiseaseById(String id) {
        var currentDiseaseId = this.currentPatient.getPatientSummaryBundleTranslated().getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.Condition) && resource.getId().equals(id)).findFirst();
        return currentDiseaseId.map(resource -> this.hapiToCommandCurrentDisease.convert((Condition) resource)).orElse(null);
    }

    @Override
    public void updateCurrentDisease(CurrentDiseaseInfoCommand currentDiseaseInfoCommand) {
        // update original bundle
        var optional = this.currentPatient.getPatientSummaryBundle().getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.Condition) && resource.getId().equals(currentDiseaseInfoCommand.getIdFHIR())).findFirst();
        updateCurrentDiseaseDetails(optional, currentDiseaseInfoCommand);

        // update translated bundle
        optional = this.currentPatient.getPatientSummaryBundleTranslated().getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.Condition) && resource.getId().equals(currentDiseaseInfoCommand.getIdFHIR())).findFirst();
        updateCurrentDiseaseDetails(optional, currentDiseaseInfoCommand);
    }

    @Override
    public void deleteCurrentDisease(String id) {
        // delete from Original Bundle
        this.currentPatient.getPatientSummaryBundle()
                .getEntry()
                .removeIf(bundleEntryComponent -> bundleEntryComponent.getResource().getResourceType().equals(ResourceType.Condition) && bundleEntryComponent.getResource().getId().equals(id));

        // delete from Translated Bundle
        this.currentPatient.getPatientSummaryBundleTranslated()
                .getEntry()
                .removeIf(bundleEntryComponent -> bundleEntryComponent.getResource().getResourceType().equals(ResourceType.Condition) && bundleEntryComponent.getResource().getId().equals(id));
    }

    private static void updateCurrentDiseaseDetails(Optional<Resource> optional, CurrentDiseaseInfoCommand currentDiseaseInfoCommand) {
        if (optional.isPresent()) {
            ((Condition) optional.get()).getCode().getCodingFirstRep().setDisplay(currentDiseaseInfoCommand.getDisease());
            ((Condition) optional.get()).getNoteFirstRep().setText(currentDiseaseInfoCommand.getComment());
            ((Condition) optional.get()).setOnset(new DateTimeType(Date.from(currentDiseaseInfoCommand.getDateOfDiagnosis().atStartOfDay(ZoneId.systemDefault()).toInstant())));
            if (Objects.nonNull(currentDiseaseInfoCommand.getEndDateOfDiagnosis())) {
                ((Condition) optional.get()).setAbatement(new DateTimeType(Date.from(currentDiseaseInfoCommand.getEndDateOfDiagnosis().atStartOfDay(ZoneId.systemDefault()).toInstant())));
            }
        } else {
            log.error("Cannot be updated. Resource not found.");
        }
    }

    @Override
    public void deleteNewCurrentDisease(Long id) {
        this.currentDiseaseRepository.deleteById(id);
    }

    @Override
    public void updateNewCurrentDisease(CurrentDiseaseInfoCommand currentDiseaseInfoCommand) {
        CurrentDiseaseEntity currentDiseaseEntity = this.currentDiseaseRepository.getOne(currentDiseaseInfoCommand.getId());
        currentDiseaseEntity.setDisease(currentDiseaseInfoCommand.getDisease());
        currentDiseaseEntity.setDateOfDiagnosis(currentDiseaseInfoCommand.getDateOfDiagnosis());
        currentDiseaseEntity.setEndDateOfDiagnosis(currentDiseaseInfoCommand.getEndDateOfDiagnosis());
        currentDiseaseEntity.setComment(currentDiseaseInfoCommand.getComment());
        this.currentDiseaseRepository.save(currentDiseaseEntity);
    }

    @Override
    public CurrentDiseaseInfoCommand retrieveNewCurrentDiseaseById(Long id) {
        return this.entityToCommandCurrentDisease.convert(this.currentDiseaseRepository.getOne(id));
    }

    public void refresh() {
        this.currentD2DConnection.getIPS();
    }

    @Override
    public Condition callSendCurrentDiseases() {
        if (Objects.nonNull(this.currentD2DConnection.getTd2D())) {
            for (int i = 0; i < this.currentDiseaseRepository.findAll().size(); i++) {
                Condition condition = createCurrentDiseasesFromEntity(this.currentDiseaseRepository.findAll().get(i));
                this.currentPatient.getPrescription().getEntry().add(new Bundle.BundleEntryComponent().setResource(condition));
                this.currentPatient.getPrescriptionTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(condition));
                return condition;
            }
            log.info("Current Diseases sent to S-EHR");
            this.currentDiseaseRepository.deleteAll();
        } else {
            log.error("The connection with S-EHR is not established.");
        }
        return null;
    }

    @Override
    @SneakyThrows
    public void sendCurrentDiseases(Bundle condition) {
        this.currentD2DConnection.getTd2D().sendHealthData(condition);
        log.info("Current Diseases sent to S-EHR");
        this.currentDiseaseRepository.deleteAll();
    }

    private static Condition createCurrentDiseasesFromEntity(CurrentDiseaseEntity currentDiseaseEntity) {
        Condition condition = new Condition();

        condition.setCode(new CodeableConcept().setCoding(new ArrayList<>())
                .addCoding(new Coding()
                        .setSystem("http://loinc.org")
                        .setCode("75326-9")
                        .setDisplay(currentDiseaseEntity.getDisease())));

        UUID uniqueKey = UUID.randomUUID();
        condition.setId(currentDiseaseEntity.getPatientId() + "/" + uniqueKey);

        List<CodeableConcept> d2 = new ArrayList<>();
        d2.add(new CodeableConcept().setText(currentDiseaseEntity.getComment()));
        condition.getNoteFirstRep().setText(currentDiseaseEntity.getComment());

        condition.getOnsetDateTimeType().setValue(Date.from(currentDiseaseEntity.getDateOfDiagnosis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        condition.getAbatementDateTimeType().setValue(Date.from(currentDiseaseEntity.getEndDateOfDiagnosis().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        return condition;
    }
}
