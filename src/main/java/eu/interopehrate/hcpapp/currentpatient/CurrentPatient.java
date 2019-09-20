package eu.interopehrate.hcpapp.currentpatient;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Component
public class CurrentPatient {
    private Bundle patientSummaryBundle;

    @PostConstruct
    private void postConstruct() throws IOException {
        File file = new ClassPathResource("sample-patient-summary.json").getFile();
        String patientSummaryJson = Files.readString(file.toPath());
        patientSummaryBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(patientSummaryJson);
    }

    public List<AllergyIntolerance> allergyIntoleranceList() {
        return new BundleProcessor(patientSummaryBundle).allergyIntoleranceList();
    }
}
