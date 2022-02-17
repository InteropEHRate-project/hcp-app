package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.ConclusionCommand;
import org.hl7.fhir.r4.model.Bundle;

import java.io.IOException;

public interface ConclusionService {
    ConclusionCommand conclusionComm();
    void insertConclusionNote(String conclusionNote);
    void deleteNote(String note);
    void callSendConclusion() throws IOException;
    void sendConclusion(Bundle conclusion) throws IOException;
    CurrentD2DConnection getCurrentD2DConnection();
}
