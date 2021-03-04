package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsInfoCommand;
import org.hl7.fhir.r4.model.Bundle;

import java.io.IOException;
import java.util.HashMap;

public interface VitalSignsService {
    CurrentPatient getCurrentPatient();
    CurrentD2DConnection getCurrentD2DConnection();
    VitalSignsCommand vitalSignsCommand() throws IOException;
    VitalSignsCommand vitalSignsUpload();
    void insertVitalSigns(VitalSignsInfoCommand vitalSignsInfoCommand);
    void callVitalSigns() throws IOException;
    void sendVitalSigns(Bundle vitalSigns) throws IOException;
    HashMap correlations();
}
