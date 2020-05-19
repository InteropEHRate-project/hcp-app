package eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryCommandAnalyte;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryCommandSample;

public interface ObservationLaboratoryService {
    ObservationLaboratoryCommandAnalyte observationLaboratoryInfoCommand();
    ObservationLaboratoryCommandSample observationLaboratoryInfoCommandSample();
}
