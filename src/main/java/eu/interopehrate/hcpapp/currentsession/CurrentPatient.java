package eu.interopehrate.hcpapp.currentsession;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class CurrentPatient {
    private Bundle patientSummaryBundle;

    public void intiFromJsonString(String patientSummaryJson) {
        patientSummaryBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(patientSummaryJson);
    }

    public void reset() {
        patientSummaryBundle = null;
    }

    public List<AllergyIntolerance> allergyIntoleranceList() {
        if (Objects.isNull(patientSummaryBundle)) {
            return Collections.emptyList();
        } else {
            return new BundleProcessor(patientSummaryBundle).allergyIntoleranceList();
        }
    }
}