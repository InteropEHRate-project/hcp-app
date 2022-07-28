package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityPatHistory;
import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandPatHistory;
import eu.interopehrate.hcpapp.converters.fhir.pathistory.HapiToCommandDiagnosis;
import eu.interopehrate.hcpapp.converters.fhir.pathistory.HapiToCommandRiskFactor;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.PatHistoryEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.PatHistoryRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory.PatHistoryCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory.PatHistoryInfoCommandDiagnosis;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import eu.interopehrate.hcpapp.services.currentpatient.PatHistoryService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PatHistoryServiceImpl implements PatHistoryService {
    private final CurrentPatient currentPatient;
    private final List<String> listOfPatHis = new ArrayList<>();
    private final List<String> listOfSocHis = new ArrayList<>();
    private final List<String> listOfFamHis = new ArrayList<>();
    private final HapiToCommandRiskFactor hapiToCommandRiskFactor;
    private final HapiToCommandDiagnosis hapiToCommandDiagnosis;
    private final CurrentD2DConnection currentD2DConnection;
    private final PatHistoryRepository patHistoryRepository;
    private final EntityToCommandPatHistory entityToCommandPatHistory;
    private final CommandToEntityPatHistory commandToEntityPatHistory;
    @Autowired
    private HealthCareProfessionalService healthCareProfessionalService;
    private final AuditInformationService auditInformationService;

    @SneakyThrows
    public PatHistoryServiceImpl(CurrentPatient currentPatient, HapiToCommandRiskFactor hapiToCommandRiskFactor, HapiToCommandDiagnosis hapiToCommandDiagnosis,
                                 CurrentD2DConnection currentD2DConnection, PatHistoryRepository patHistoryRepository, EntityToCommandPatHistory entityToCommandPatHistory, CommandToEntityPatHistory commandToEntityPatHistory, AuditInformationService auditInformationService) {
        this.currentPatient = currentPatient;
        this.hapiToCommandRiskFactor = hapiToCommandRiskFactor;
        this.hapiToCommandDiagnosis = hapiToCommandDiagnosis;
        this.currentD2DConnection = currentD2DConnection;
        this.patHistoryRepository = patHistoryRepository;
        this.entityToCommandPatHistory = entityToCommandPatHistory;
        this.commandToEntityPatHistory = commandToEntityPatHistory;
        this.auditInformationService = auditInformationService;
    }

    @Override
    public PatHistoryCommand patHistorySection() {
        var riskFactorInfoCommands = this.currentPatient.patHisConsultationRiskFactorsList()
                .stream()
                .filter(pathHistory -> ((pathHistory.hasId() && pathHistory.getId().contains("11329-0")) ||
                        (pathHistory.hasCode() && pathHistory.getCode().getCodingFirstRep().getCode().contains("11329-0"))))
                .map(hapiToCommandRiskFactor::convert)
                .collect(Collectors.toList());
        var diagnosisInfoCommands = this.currentPatient.patHisConsultationDiagnosesList()
                .stream()
                .filter(pathHistory -> (pathHistory.hasId() && pathHistory.getId().contains("11329-0")) ||
                        (pathHistory.hasCode() && pathHistory.getCode().getCodingFirstRep().getCode().contains("11329-0")))
                .map(hapiToCommandDiagnosis::convert).filter(Objects::nonNull)
                .collect(Collectors.toList());

        return PatHistoryCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .patHistoryInfoCommandRiskFactors(riskFactorInfoCommands)
                .patHistoryInfoCommandDiagnoses(diagnosisInfoCommands)
                .build();
    }

    @Override
    public void insertPatHis(String patHis) {
        if (patHis != null && !patHis.trim().equals("") && !this.listOfPatHis.contains(patHis)) {
            this.listOfPatHis.add(patHis);
        }
    }

    @Override
    public void deletePatHis(String patHis) {
        this.listOfPatHis.removeIf(x -> x.equals(patHis));
    }

    @Override
    public void insertSocHis(String socHis) {
        if (socHis != null && !socHis.trim().equals("") && !this.listOfSocHis.contains(socHis)) {
            this.listOfSocHis.add(socHis);
        }
    }

    @Override
    public void deleteSocHis(String socHis) {
        this.listOfSocHis.removeIf(x -> x.equals(socHis));
    }

    @Override
    public void insertFamHis(String famHis) {
        if (famHis != null && !famHis.trim().equals("") && !this.listOfFamHis.contains(famHis)) {
            this.listOfFamHis.add(famHis);
        }
    }

    @Override
    public void deleteFamHis(String famHis) {
        this.listOfFamHis.removeIf(x -> x.equals(famHis));
    }

    @Override
    public void editRisk(Boolean value, String id) {
        // update original bundle
        Bundle bundle = this.currentPatient.getPatHisBundle();
        for (Bundle.BundleEntryComponent b : bundle.getEntry()) {
            if (b.getResource().getResourceType().equals(ResourceType.Observation) && b.getResource().getId().equals(id)) {
                ((BooleanType) ((Observation) b.getResource()).getValue()).setValue(value);
            }
        }
        // update translated bundle
        Bundle bundleTranslated = this.currentPatient.getPatHisBundleTranslated();
        for (Bundle.BundleEntryComponent b : bundleTranslated.getEntry()) {
            if (b.getResource().getResourceType().equals(ResourceType.Observation) && b.getResource().getId().equals(id)) {
                ((BooleanType) ((Observation) b.getResource()).getValue()).setValue(value);
            }
        }
    }

    @Override
    public PatHistoryInfoCommandDiagnosis patHisInfoCommandById(String id) {
        var patHisDiagnosis = this.currentPatient.getPatHisBundleTranslated().getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.Condition) && resource.getId().equals(id)).findFirst();
        return patHisDiagnosis.map(resource -> this.hapiToCommandDiagnosis.convert((Condition) resource)).orElse(null);
    }

    @Override
    public void updateDiagnosis(PatHistoryInfoCommandDiagnosis patHisInfoCommand) {
        // update original bundle
        var optional = this.currentPatient.getPatHisBundle().getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.Condition) && resource.getId().equals(patHisInfoCommand.getId())).findFirst();
        updateDiagnosisDetails(optional, patHisInfoCommand);

        // update translated bundle
        optional = this.currentPatient.getPatHisBundleTranslated().getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.Condition) && resource.getId().equals(patHisInfoCommand.getId())).findFirst();
        updateDiagnosisDetails(optional, patHisInfoCommand);
    }

    @Override
    public void deleteDiagnosis(String id) {
        // delete from Original Bundle
        this.currentPatient.getPatHisBundle()
                .getEntry()
                .removeIf(bundleEntryComponent -> bundleEntryComponent.getResource().getResourceType().equals(ResourceType.Condition) && bundleEntryComponent.getResource().getId().equals(id));

        // delete from Translated Bundle
        this.currentPatient.getPatHisBundleTranslated()
                .getEntry()
                .removeIf(bundleEntryComponent -> bundleEntryComponent.getResource().getResourceType().equals(ResourceType.Condition) && bundleEntryComponent.getResource().getId().equals(id));
    }

    private static void updateDiagnosisDetails(Optional<Resource> optional, PatHistoryInfoCommandDiagnosis patHisInfoCommand) {
        if (optional.isPresent()) {

            // deletes translation if the allergy's name is different
            StringType displayElement = ((Condition) optional.get()).getCode().getCodingFirstRep().getDisplayElement();
            if (displayElement.hasExtension() && !displayElement.getValue().equalsIgnoreCase(patHisInfoCommand.getDiagnosis())) {
                displayElement.getExtension().clear();
            }

            ((Condition) optional.get()).getCode().getCodingFirstRep().setDisplay(patHisInfoCommand.getDiagnosis());

            ((Condition) optional.get()).getNoteFirstRep().setText(patHisInfoCommand.getComments());
            if (Objects.isNull(((Condition) optional.get()).getOnset())) {
                ((Condition) optional.get()).setOnset(new DateTimeType(new Date()));
                ((DateTimeType) ((Condition) optional.get()).getOnset()).setYear(patHisInfoCommand.getYearOfDiagnosis());
            } else {
                ((DateTimeType) ((Condition) optional.get()).getOnset()).setYear(patHisInfoCommand.getYearOfDiagnosis());
            }
            if (Objects.nonNull(patHisInfoCommand.getEndDateOfDiagnosis())) {
                ((Condition) optional.get()).setAbatement(new DateTimeType(Date.from(patHisInfoCommand.getEndDateOfDiagnosis().atStartOfDay(ZoneId.systemDefault()).toInstant())));
            }
        } else {
            log.error("Cannot be updated. Resource not found.");
        }
    }

    @Override
    public void refresh() {
        this.currentD2DConnection.getPathologyHistory();
    }

    @Override
    public void insertPathHistory(PatHistoryInfoCommandDiagnosis patHistoryInfoCommandDiagnosis) {
        patHistoryInfoCommandDiagnosis.setPatientId(this.currentPatient.getPatient().getId());
        patHistoryInfoCommandDiagnosis.setAuthor(healthCareProfessionalService.getHealthCareProfessional().getFirstName() + " "
                + healthCareProfessionalService.getHealthCareProfessional().getLastName());
        this.patHistoryRepository.save(Objects.requireNonNull(this.commandToEntityPatHistory.convert(patHistoryInfoCommandDiagnosis)));
    }

    @Override
    public List<PatHistoryInfoCommandDiagnosis> getNewPat() {
        return this.patHistoryRepository.findAll()
                .stream()
                .map(this.entityToCommandPatHistory::convert)
                .collect(Collectors.toList());
    }

    @Override
    public PatHistoryCommand patHistoryCommand() {
        return PatHistoryCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .listOfFamHis(this.listOfFamHis)
                .listOfPatHis(this.listOfPatHis)
                .listOfSocHis(this.listOfSocHis)
                .build();
    }

    private static Condition createPatHisFromEntity(PatHistoryEntity patHistoryEntity) {
        Condition patHis = new Condition();

        patHis.setId(UUID.randomUUID().toString());
        patHis.setCode(new CodeableConcept());
        patHis.getCode().addChild("coding");
        patHis.getCode().setCoding(new ArrayList<>());
        patHis.getCode().getCoding().add(new Coding()
                .setSystem("http://loinc.org")
                .setCode("113929-0")
                .setDisplay("History general Narrative"));

        patHis.addNote().setText(patHistoryEntity.getPatHis());

        return patHis;
    }

    @Override
    public Condition callPatHis() {
        if (Objects.nonNull(this.currentD2DConnection.getTd2D())) {
            for (int i = 0; i < this.patHistoryRepository.findAll().size(); i++) {
                Condition patHistory = createPatHisFromEntity(this.patHistoryRepository.findAll().get(i));
                this.currentPatient.getPatientSummaryBundle().getEntry().add(new Bundle.BundleEntryComponent().setResource(patHistory));
                this.currentPatient.getPatientSummaryBundleTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(patHistory));
                return patHistory;
            }
            auditInformationService.auditEvent(AuditEventType.SEND_TO_SEHR, "Auditing send Pathology History to S-EHR");
            this.patHistoryRepository.deleteAll();
        } else {
            log.error("The connection with S-EHR is not established.");
        }
        return null;
    }

    @Override
    public CurrentPatient getCurrentPatient() { return currentPatient; }

    @Override
    public CurrentD2DConnection getCurrentD2DConnection() { return this.currentD2DConnection; }

    @Override
    public PatHistoryRepository getPatHistoryRepository() { return this.patHistoryRepository; }
}
