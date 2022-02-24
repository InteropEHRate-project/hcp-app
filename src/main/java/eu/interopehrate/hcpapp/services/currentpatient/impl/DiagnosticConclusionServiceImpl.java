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
    public void callSendConclusion() {
        if (Objects.nonNull(this.currentD2DConnection.getTd2D())) {
            Bundle conclusion = new Bundle();
            conclusion.setEntry(new ArrayList<>());
            for (int i = 0; i < this.diagnosticConclusionRepository.findAll().size(); i++) {
                conclusion.getEntry().add(new Bundle.BundleEntryComponent());
                CarePlan conc = createConclusionFromEntity(this.diagnosticConclusionRepository.findAll().get(i));
                conclusion.getEntry().get(i).setResource(conc);
                this.currentPatient.getPatientSummaryBundle().getEntry().add(new Bundle.BundleEntryComponent().setResource(conc));
                this.currentPatient.getPatientSummaryBundleTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(conc));
            }
            this.sendConclusion(conclusion);
        } else {
            log.error("The connection with S-EHR is not established.");
        }
    }

    @Override
    @SneakyThrows
    public void sendConclusion(Bundle medicationRequest) {
        this.currentD2DConnection.getTd2D().sendHealthData(medicationRequest);
        log.info("Diagnostic Conclusion sent to S-EHR");
        this.diagnosticConclusionRepository.deleteAll();
    }

    private static CarePlan createConclusionFromEntity(DiagnosticConclusionEntity diagnosticConclusionEntity) {
        CarePlan conclusion = new CarePlan();
        conclusion.setDescription(diagnosticConclusionEntity.getConclusionNote());
        conclusion.setDescription(diagnosticConclusionEntity.getTreatmentPlan());

        return conclusion;
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
    public void deleteNote(String note) {
        this.listOfConclusionNote.removeIf(x -> x.equals(note));
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
        this.diagnosticConclusionRepository.save(this.commandToEntityConclusion.convert(diagnosticConclusionInfoCommand));
    }
}
