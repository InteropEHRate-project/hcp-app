package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.VitalSignsCommand;
import org.hl7.fhir.r4.model.Bundle;

import java.io.IOException;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.VitalSignsInfoCommand;

public interface VitalSignsService {
    VitalSignsCommand vitalSignsCommand ();

    void sendVitalSigns(Bundle vitalSigns) throws IOException;
    void insertPrescription(VitalSignsInfoCommand vitalSignsInfoCommand);
}
