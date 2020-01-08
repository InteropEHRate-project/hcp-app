package eu.interopehrate.hcpapp.services.testd2dlibrary.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.TestD2DLibraryCommand;
import eu.interopehrate.hcpapp.services.ApplicationRuntimeInfoService;
import eu.interopehrate.hcpapp.services.testd2dlibrary.TestD2DLibraryService;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TestD2DLibraryServiceImpl implements TestD2DLibraryService {
    private CurrentPatient currentPatient;
    private CurrentD2DConnection currentD2DConnection;
    private ApplicationRuntimeInfoService applicationRuntimeInfoService;
    private TestD2DLibraryCommand testD2DLibraryCommand = new TestD2DLibraryCommand();

    public TestD2DLibraryServiceImpl(CurrentPatient currentPatient,
                                     CurrentD2DConnection currentD2DConnection,
                                     ApplicationRuntimeInfoService applicationRuntimeInfoService) {
        this.currentPatient = currentPatient;
        this.currentD2DConnection = currentD2DConnection;
        this.applicationRuntimeInfoService = applicationRuntimeInfoService;
    }

    @Override
    public TestD2DLibraryCommand currentState() {
        switch (currentD2DConnection.connectionState()) {
            case PENDING_DEVICE:
            case OFF:
                testD2DLibraryCommand.setOn(Boolean.FALSE);
                break;
            case ON:
                testD2DLibraryCommand.setOn(Boolean.TRUE);
                break;
            default:
                throw new RuntimeException("No such state");
        }
        return this.testD2DLibraryCommand;
    }

    @Override
    public void sendMessageToSEHR() throws Exception {
        currentD2DConnection.sendPractitioner(applicationRuntimeInfoService.practitioner());
        testD2DLibraryCommand.setSendActionMessage("The details about organization and practitioner was sent to S-EHR.");
    }

    @Override
    public void lastSEHRMessage() {
        Patient lastPatient = currentD2DConnection.lastPatient();
        String lastSentPatientSummary = currentD2DConnection.lastPatientSummary();
        if (lastSentPatientSummary.contains("{")) {
            lastSentPatientSummary = lastSentPatientSummary.substring(lastSentPatientSummary.indexOf("{"));
            currentPatient.intiFromJsonString(lastSentPatientSummary);
        }
        testD2DLibraryCommand.setLastSEHRMessage(
                String.join("<br/><br/>",
                        this.patientToString(lastPatient),
                        lastSentPatientSummary));
        currentPatient.initPatient(lastPatient);
        testD2DLibraryCommand.setSendActionMessage(null);
    }

    private String patientToString(Patient patient) {
        if (Objects.isNull(patient)) {
            return "no patient received yet";
        }
        FhirContext fc = FhirContext.forR4();
        IParser parser = fc.newJsonParser().setPrettyPrint(true);
        return parser.encodeResourceToString(patient);
    }
}
