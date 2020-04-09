package eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryInfoCommand;

public interface ObservationLaboratoryService {
    ObservationLaboratoryCommand observationLaboratoryInfoCommand();

    void insertObservationLaboratory(ObservationLaboratoryInfoCommand observationLaboratoryInfoCommand);
}
