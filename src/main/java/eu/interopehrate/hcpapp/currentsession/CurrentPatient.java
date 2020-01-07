package eu.interopehrate.hcpapp.currentsession;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class CurrentPatient {
    private Bundle patientSummaryBundle;
    private Patient patient;

    public void intiFromJsonString(String patientSummaryJson) {
        patientSummaryBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(patientSummaryJson);
    }

    public void initPatient(Patient patient) {
        this.patient = patient;
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

    public List<Observation> observationList(){
        if (Objects.isNull(patientSummaryBundle)){
            return Collections.emptyList();
        }else{
            return new BundleProcessor(patientSummaryBundle).observationList();
        }
    }

    public Patient getPatient() {
        return patient;
    }
}