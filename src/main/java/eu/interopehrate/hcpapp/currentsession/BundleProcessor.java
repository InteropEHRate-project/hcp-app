package eu.interopehrate.hcpapp.currentsession;

import org.hl7.fhir.r4.model.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BundleProcessor {
    private Bundle bundle;

    public BundleProcessor(Bundle bundle) {
        this.bundle = bundle;
    }

    public Optional<Patient> patient() {
        return bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.Patient))
                .map(Patient.class::cast)
                .findFirst();
    }

    public List<AllergyIntolerance> allergyIntoleranceList() {
        return bundle.getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.AllergyIntolerance))
                .map(AllergyIntolerance.class::cast)
                .collect(Collectors.toList());
    }

    public List<MedicationStatement> medicationStatementList() {
        return bundle.getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.MedicationStatement))
                .map(MedicationStatement.class::cast)
                .collect(Collectors.toList());
    }

    public List<Medication> medicationList() {
        return bundle.getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.Medication))
                .map(Medication.class::cast)
                .collect(Collectors.toList());
    }

    public List<Condition> conditionList() {
        return bundle.getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.Condition))
                .map(Condition.class::cast)
                .collect(Collectors.toList());
    }

    public List<Observation> laboratoryList() {
        return bundle.getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.Observation))
                .map(Observation.class::cast)
                .collect(Collectors.toList());
    }

    public List<MedicationRequest> prescriptionList() {
        return this.bundle.getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.MedicationRequest))
                .map(MedicationRequest.class::cast)
                .collect(Collectors.toList());
    }

    public List<Observation> vitalSignsList() {
        return bundle.getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.Observation))
                .map(Observation.class::cast)
                .collect(Collectors.toList());
    }

    public List<Media> mediaList() {
        return this.bundle.getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.Media))
                .map(Media.class::cast)
                .collect(Collectors.toList());
    }

    public List<DocumentReference> docHistoryConsultationList() {
        return this.bundle.getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.DocumentReference))
                .map(DocumentReference.class::cast)
                .collect(Collectors.toList());
    }

    public List<Observation> patHisConsultationObservationsList() {
        return this.bundle.getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.Observation))
                .map(Observation.class::cast)
                .collect(Collectors.toList());
    }

    public List<Condition> patHisConsultationConditionsList() {
        return this.bundle.getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.Condition))
                .map(Condition.class::cast)
                .collect(Collectors.toList());
    }

    public List<DiagnosticReport> diagnosticReportList() {
        return this.bundle.getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.DiagnosticReport))
                .map(DiagnosticReport.class::cast)
                .collect(Collectors.toList());
    }
}
