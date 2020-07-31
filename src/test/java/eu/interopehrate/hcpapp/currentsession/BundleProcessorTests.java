package eu.interopehrate.hcpapp.currentsession;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BundleProcessorTests {
    private static BundleProcessor bundleProcessor;

    @BeforeClass
    public static void beforeClass() throws Exception {
        File file = new ClassPathResource("fhir/sample-patient-summary.json").getFile();
        String patientSummaryJson = Files.readString(file.toPath());
        Bundle patientSummaryBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(patientSummaryJson);
        bundleProcessor = new BundleProcessor(patientSummaryBundle);
    }

    @Test
    public void testPatient() {
        Optional<Patient> optionalPatient = bundleProcessor.patient();
        assertTrue(optionalPatient.isPresent());
    }

    @Test
    public void testAllergyIntoleranceList() {
        List<AllergyIntolerance> allergyIntolerance = bundleProcessor.allergyIntoleranceList();
        assertEquals(1, allergyIntolerance.size());
    }

    @Test
    public void testMedicationStatementList() {
        List<MedicationStatement> medicationStatementList = bundleProcessor.medicationStatementList();
        assertEquals(6, medicationStatementList.size());
    }

    @Test
    public void testMedicationList() {
        List<Medication> medicationList = bundleProcessor.medicationList();
        assertEquals(0, medicationList.size());
    }

    @Test
    public void testConditionList() {
        List<Condition> conditionList = bundleProcessor.conditionList();
        assertEquals(2, conditionList.size());
    }

    @Test
    public void testObservationList() {
        List<Observation> observationList = bundleProcessor.laboratoryList();
        assertEquals(2, observationList.size());
    }

    @Test
    public void testPrescriptionList() throws IOException {
        File file = new ClassPathResource("fhir/BundlePrescription.json").getFile();
        String prescriptionJson = Files.readString(file.toPath());
        Bundle prescriptionBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(prescriptionJson);

        List<MedicationRequest> list = prescriptionBundle.getEntry()
                .stream()
                .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.MedicationRequest))
                .map(Bundle.BundleEntryComponent::getResource)
                .map(MedicationRequest.class::cast)
                .collect(Collectors.toList());
        assertEquals(1, list.size());
    }
}