package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.DiagnosticConclusionRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.DiagnosticConclusionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.DiagnosticConclusionInfoCommand;
import org.hl7.fhir.r4.model.Bundle;

import java.io.IOException;
import java.util.List;

public interface DiagnosticConclusionService {
    DiagnosticConclusionCommand conclusionComm();
    void insertConclusionNote(String conclusionNote);
    void insertTreatmentPlan(String treatmentPlan);
    void deleteNote(String note);
    void callSendConclusion() throws IOException;
    void sendConclusion(Bundle conclusion) throws IOException;
    CurrentD2DConnection getCurrentD2DConnection();
    DiagnosticConclusionRepository getDiagnosticRepository();
    List<DiagnosticConclusionInfoCommand> getNewConclusion();
    void insertTreatment(DiagnosticConclusionInfoCommand diagnosticConclusionInfoCommand);
}
