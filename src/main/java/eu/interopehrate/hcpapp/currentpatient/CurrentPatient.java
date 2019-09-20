package eu.interopehrate.hcpapp.currentpatient;

import org.hl7.fhir.r4.model.Composition;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CurrentPatient {
    private Composition patientSummary;

    public Boolean exists() {
        return Objects.nonNull(patientSummary);
    }

    public void remove() {
        this.setPatientSummary(null);
    }

    public void setPatientSummary(Composition patientSummary) {
        this.patientSummary = patientSummary;
    }

    public Composition getPatientSummary() {
        return patientSummary;
    }
}
