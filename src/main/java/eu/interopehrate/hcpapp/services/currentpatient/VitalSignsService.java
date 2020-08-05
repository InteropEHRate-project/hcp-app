package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsInfoCommand;
import org.hl7.fhir.r4.model.Bundle;

import java.io.IOException;

public interface VitalSignsService {
    VitalSignsCommand vitalSignsCommand() throws IOException;

    void sendVitalSigns(Bundle vitalSigns) throws IOException;

    void insertVitalSigns(VitalSignsInfoCommand vitalSignsInfoCommand);
}
