package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityConclusion;
import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToConclusion;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.DiagnosticConclusionEntity;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.DiagnosticConclusionRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.DiagnosticConclusionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.DiagnosticConclusionInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.DiagnosticConclusionService;
import eu.interopehrate.hcpapp.services.currentpatient.CurrentDiseaseService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DiagnosticConclusionServiceImpl implements DiagnosticConclusionService {
    private final CurrentDiseaseService currentDiseaseService;
    private final CurrentD2DConnection currentD2DConnection;
    private final CurrentPatient currentPatient;
    private final DiagnosticConclusionRepository diagnosticConclusionRepository;
    private final List<String> listOfConclusionNote = new ArrayList<>();
    private final List<String> listOfTreatmentPlan = new ArrayList<>();
    public final EntityToConclusion entityToConclusion;
    private final CommandToEntityConclusion commandToEntityConclusion;

    public DiagnosticConclusionServiceImpl(CurrentDiseaseService currentDiseaseService, CurrentD2DConnection currentD2DConnection, CurrentPatient currentPatient,
                                           DiagnosticConclusionRepository diagnosticConclusionRepository, EntityToConclusion entityToConclusion, CommandToEntityConclusion commandToEntityConclusion) {
        this.currentDiseaseService = currentDiseaseService;
        this.currentD2DConnection = currentD2DConnection;
        this.currentPatient = currentPatient;
        this.diagnosticConclusionRepository = diagnosticConclusionRepository;
        this.entityToConclusion = entityToConclusion;
        this.commandToEntityConclusion = commandToEntityConclusion;
    }

    @Override
    public Condition callSendConclusion() {
        if (Objects.nonNull(this.currentD2DConnection.getTd2D())) {
            for (int i = 0; i < this.diagnosticConclusionRepository.findAll().size(); i++) {
                Condition obs = createConclusionFromEntityObs(this.diagnosticConclusionRepository.findAll().get(i));
                this.currentPatient.getPatientSummaryBundle().getEntry().add(new Bundle.BundleEntryComponent().setResource(obs));
                this.currentPatient.getPatientSummaryBundleTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(obs));
                return obs;
            }
        } else {
            log.error("The connection with S-EHR is not established.");
        }
        return null;
    }

    @Override
    public CarePlan callSendTreatment() {
        if (Objects.nonNull(this.currentD2DConnection.getTd2D())) {
            for (int i = 0; i < this.diagnosticConclusionRepository.findAll().size(); i++) {
                CarePlan treatmentPlan = createConclusionFromEntity(this.diagnosticConclusionRepository.findAll().get(i));
                this.currentPatient.getPatientSummaryBundle().getEntry().add(new Bundle.BundleEntryComponent().setResource(treatmentPlan));
                this.currentPatient.getPatientSummaryBundleTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(treatmentPlan));
                return treatmentPlan;
            }
        } else {
            log.error("The connection with S-EHR is not established.");
        }
        return null;
    }

    @Override
    @SneakyThrows
    public void sendConclusion(Bundle conclusionTreatment) {
        this.currentD2DConnection.getTd2D().sendHealthData(conclusionTreatment);
        log.info("Diagnostic Conclusion & Treatment Plan sent to S-EHR");
        this.diagnosticConclusionRepository.deleteAll();
    }

    private static CarePlan createConclusionFromEntity(DiagnosticConclusionEntity diagnosticConclusionEntity) {
        CarePlan conclusion = new CarePlan();
        conclusion.setCategory(new ArrayList<>()).getCategory().add(new CodeableConcept().setCoding(new ArrayList<>())
                .addCoding(new Coding()
                        .setSystem("http://loinc.org")
                        .setCode("18776-5")
                        .setDisplay("Plan of care note")));
        conclusion.setTitle("Treatment Plan");
        conclusion.setDescription(diagnosticConclusionEntity.getTreatmentPlan());
        conclusion.setAuthor(conclusion.getAuthor().setReference(diagnosticConclusionEntity.getAuthor()));
        conclusion.setCreatedElement(DateTimeType.now());
        conclusion.setId(UUID.randomUUID().toString());

        return conclusion;
    }

    private static Condition createConclusionFromEntityObs(DiagnosticConclusionEntity diagnosticConclusionEntity) {
        Condition obs = new Condition();
        obs.setCode(new CodeableConcept().addCoding(new Coding()
                .setSystem("http://loinc.org")
                .setCode("55110-1")
                .setDisplay("Conclusions [Interpretation] Document")));
        obs.addNote().setText(diagnosticConclusionEntity.getConclusionNote());
        obs.setId(UUID.randomUUID().toString());

        Meta meta = new Meta();
        meta.addProfile("http://interopehrate.eu/fhir/StructureDefinition/DiagnosticConclusion-IEHR");
        obs.setMeta(meta);

        return obs;
    }

    @Override
    public DiagnosticConclusionCommand conclusionComm() {
        return DiagnosticConclusionCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .listOfConclusionNote(this.listOfConclusionNote)
                .listOfTreatmentPlan(this.listOfTreatmentPlan)
                .currentDiseaseService(this.currentDiseaseService)
                .build();
    }

    @Override
    public void insertConclusionNote(String conclusionNote) {
        if (conclusionNote != null && !conclusionNote.trim().equals("") && !this.listOfConclusionNote.contains(conclusionNote)) {
            this.listOfConclusionNote.add(conclusionNote);
        }
    }

    @Override
    public void insertTreatmentPlan(String treatmentPlan) {
        if (treatmentPlan != null && !treatmentPlan.trim().equals("") && !this.listOfTreatmentPlan.contains(treatmentPlan)) {
            this.listOfTreatmentPlan.add(treatmentPlan);
        }
    }

    @Override
    public void deleteAll(String note, String noteTreatment) {
        this.listOfConclusionNote.removeIf(x -> x.equals(note));
        this.listOfTreatmentPlan.removeIf(x -> x.equals(noteTreatment));
        this.diagnosticConclusionRepository.deleteAll();
    }

    @Override
    public CurrentD2DConnection getCurrentD2DConnection() {
        return this.currentD2DConnection;
    }

    public DiagnosticConclusionRepository getDiagnosticRepository() {
        return diagnosticConclusionRepository;
    }

    public List<DiagnosticConclusionInfoCommand> getNewConclusion() {
        return this.diagnosticConclusionRepository.findAll()
                .stream()
                .map(this.entityToConclusion::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void insertTreatment(DiagnosticConclusionInfoCommand diagnosticConclusionInfoCommand) {
        diagnosticConclusionInfoCommand.setPatientId(this.currentPatient.getPatient().getId());
        this.diagnosticConclusionRepository.save(Objects.requireNonNull(this.commandToEntityConclusion.convert(diagnosticConclusionInfoCommand)));
    }
}
