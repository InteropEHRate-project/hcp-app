package eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.RequestCommand;

public interface RequestService {
    RequestCommand requestInfoCommand(String id);
}
