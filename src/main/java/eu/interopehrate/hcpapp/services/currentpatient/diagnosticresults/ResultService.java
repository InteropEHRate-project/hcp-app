package eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ResultCommand;

public interface ResultService {
    ResultCommand resultInfoCommand(String id);
}
