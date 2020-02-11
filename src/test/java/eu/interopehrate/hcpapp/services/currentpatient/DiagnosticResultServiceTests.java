package eu.interopehrate.hcpapp.services.currentpatient;

import ca.uhn.fhir.context.FhirContext;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiagnosticResultServiceTests {
    private static String initialJsonFhir;
    @Autowired
    private CurrentPatient currentPatient;

    @BeforeClass
    public static void init() throws IOException {
        File file = new ClassPathResource("fhir/sample-patient-summary.json").getFile();
        initialJsonFhir = Files.readString(file.toPath());
    }

    @Test
    public void testDiagnosticResultSection() {
        Bundle patientSummaryBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(initialJsonFhir);
        currentPatient.initPatientSummary(patientSummaryBundle);
        List<Observation> diagnosticResultInfoCommands = currentPatient.observationList();
        assertTrue(diagnosticResultInfoCommands.size() > 0);
    }


    @Test
    public void testDiagnosticResultCode() {
        System.out.println(currentPatient.observationList().get(4).getCode().getCoding().get(0).getCode());
    }

}
