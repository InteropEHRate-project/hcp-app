package eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryCommandAnalysis;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryCommandSample;

public interface ObservationLaboratoryService {
    ObservationLaboratoryCommandAnalysis observationLaboratoryInfoCommandAnalysis();
    ObservationLaboratoryCommandSample observationLaboratoryInfoCommandSample();
}
