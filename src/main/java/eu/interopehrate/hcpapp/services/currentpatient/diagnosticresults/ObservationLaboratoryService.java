package eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryCommandAnalysis;

public interface ObservationLaboratoryService {
    ObservationLaboratoryCommandAnalysis observationLaboratoryInfoCommandAnalysis(int pageNo, int pageSize);
}
