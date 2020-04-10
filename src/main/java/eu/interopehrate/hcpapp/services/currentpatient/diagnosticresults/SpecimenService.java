package eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.SpecimenCommand;

public interface SpecimenService {
    SpecimenCommand specimenInfoCommand(String id);
}
