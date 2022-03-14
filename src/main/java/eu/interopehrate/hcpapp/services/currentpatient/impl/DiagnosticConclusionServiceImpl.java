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
            Bundle conclusionTreatment = new Bundle();
            conclusionTreatment.setEntry(new ArrayList<>());
            for (int i = 0; i < this.diagnosticConclusionRepository.findAll().size(); i++) {
                conclusionTreatment.getEntry().add(new Bundle.BundleEntryComponent());
                CarePlan conc = createConclusionFromEntity(this.diagnosticConclusionRepository.findAll().get(i));
                Observation obs = createConclusionFromEntityObs(this.diagnosticConclusionRepository.findAll().get(i));
                conclusionTreatment.addEntry().setResource(conc);
                conclusionTreatment.getEntry().get(i).setResource(conc);
                conclusionTreatment.addEntry().setResource(obs);
                conclusionTreatment.getEntry().get(i).setResource(obs);
                this.currentPatient.getPatientSummaryBundle().getEntry().add(new Bundle.BundleEntryComponent().setResource(conc));
                this.currentPatient.getPatientSummaryBundleTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(conc));
            }
            this.sendConclusion(conclusionTreatment);
        } else {
            log.error("The connection with S-EHR is not established.");
        }
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
                        .setSystem("https://loinc.org")
                        .setCode("18776-5")
                        .setDisplay("Plan of care note")));
        conclusion.setTitle("Treatment Plan");
        conclusion.setDescription(diagnosticConclusionEntity.getTreatmentPlan());

        return conclusion;
    }

    private static Observation createConclusionFromEntityObs(DiagnosticConclusionEntity diagnosticConclusionEntity) {
        Observation obs = new Observation();
        obs.setCode(new CodeableConcept().setCoding(new ArrayList<>())
                .addCoding(new Coding()
                        .setSystem("https://loinc.org")
                        .setCode("55110-1").setDisplay("Conclusions [Interpretation] Document"))
                .setText(diagnosticConclusionEntity.getConclusionNote()));
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
