package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.ConclusionCommand;

public interface ConclusionService {
    ConclusionCommand conclusionComm();
    void insertConclusionNote(String conclusionNote);
}
