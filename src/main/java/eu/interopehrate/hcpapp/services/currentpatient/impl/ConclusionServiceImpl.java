package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.DiagnosticConclusionEntity;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.DiagnosticConclusionRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.ConclusionCommand;
import eu.interopehrate.hcpapp.services.currentpatient.ConclusionService;
import eu.interopehrate.hcpapp.services.currentpatient.CurrentDiseaseService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ConclusionServiceImpl implements ConclusionService {
    private final CurrentDiseaseService currentDiseaseService;
    private final CurrentD2DConnection currentD2DConnection;
    private final CurrentPatient currentPatient;
    private final DiagnosticConclusionRepository diagnosticConclusionRepository;
    private final List<String> listOfConclusionNote = new ArrayList<>();

    public ConclusionServiceImpl(CurrentDiseaseService currentDiseaseService, CurrentD2DConnection currentD2DConnection, CurrentPatient currentPatient,
                                 DiagnosticConclusionRepository diagnosticConclusionRepository) {
        this.currentDiseaseService = currentDiseaseService;
        this.currentD2DConnection = currentD2DConnection;
        this.currentPatient = currentPatient;
        this.diagnosticConclusionRepository = diagnosticConclusionRepository;
    }

    @Override
    public void callSendConclusion() {
        if (Objects.nonNull(this.currentD2DConnection.getTd2D())) {
            Bundle conclusion = new Bundle();
            conclusion.setEntry(new ArrayList<>());
            for (int i = 0; i < this.diagnosticConclusionRepository.findAll().size(); i++) {
                conclusion.getEntry().add(new Bundle.BundleEntryComponent());
                DiagnosticReport conc = createConclusionFromEntity(this.diagnosticConclusionRepository.findAll().get(i));
                conclusion.getEntry().get(i).setResource(conc);
                this.currentPatient.getPrescription().getEntry().add(new Bundle.BundleEntryComponent().setResource(conc));
                this.currentPatient.getPrescriptionTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(conc));
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

    private static DiagnosticReport createConclusionFromEntity(DiagnosticConclusionEntity diagnosticConclusionEntity) {
        DiagnosticReport diagnosticReport = new DiagnosticReport();
        diagnosticReport.setConclusion(diagnosticConclusionEntity.getConclusionNote());

        return diagnosticReport;
    }

    @Override
    public ConclusionCommand conclusionComm() {
        return ConclusionCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .listOfConclusionNote(this.listOfConclusionNote)
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
    public void deleteNote(String note) {
        this.listOfConclusionNote.removeIf(x -> x.equals(note));
    }
}
