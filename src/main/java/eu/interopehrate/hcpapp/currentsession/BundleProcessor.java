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
                .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.Patient))
                .map(Bundle.BundleEntryComponent::getResource)
                .map(Patient.class::cast)
                .findFirst();
    }

    public List<AllergyIntolerance> allergyIntoleranceList() {
        return bundle.getEntry()
                .stream()
                .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.AllergyIntolerance))
                .map(Bundle.BundleEntryComponent::getResource)
                .map(AllergyIntolerance.class::cast)
                .collect(Collectors.toList());
    }

    public List<MedicationStatement> medicationStatementList() {
        return bundle.getEntry()
                .stream()
                .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.MedicationStatement))
                .map(Bundle.BundleEntryComponent::getResource)
                .map(MedicationStatement.class::cast)
                .collect(Collectors.toList());
    }

    public List<Medication> medicationList() {
        return bundle.getEntry()
                .stream()
                .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.Medication))
                .map(Bundle.BundleEntryComponent::getResource)
                .map(Medication.class::cast)
                .collect(Collectors.toList());
    }

    public List<Condition> conditionList() {
        return bundle.getEntry()
                .stream()
                .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.Condition))
                .map(Bundle.BundleEntryComponent::getResource)
                .map(Condition.class::cast)
                .collect(Collectors.toList());
    }

    public List<Observation> laboratoryList() {
        return bundle.getEntry()
                .stream()
                .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.Observation))
                .map(Bundle.BundleEntryComponent::getResource)
                .map(Observation.class::cast)
                .collect(Collectors.toList());
    }

    public List<MedicationRequest> prescriptionList() {
        return this.bundle.getEntry()
                .stream()
                .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.MedicationRequest))
                .map(Bundle.BundleEntryComponent::getResource)
                .map(MedicationRequest.class::cast)
                .collect(Collectors.toList());
    }

    public List<Observation> vitalSignsList() {
        return bundle.getEntry()
                .stream()
                .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.Observation))
                .map(Bundle.BundleEntryComponent::getResource)
                .map(Observation.class::cast)
                .collect(Collectors.toList());
    }

    public List<Media> mediaList() {
        return this.bundle.getEntry()
                .stream()
                .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.Media))
                .map(Bundle.BundleEntryComponent::getResource)
                .map(Media.class::cast)
                .collect(Collectors.toList());
    }

    public List<DocumentReference> docHistoryConsultationList() {
        return this.bundle.getEntry()
                .stream()
                .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.DocumentReference))
                .map(Bundle.BundleEntryComponent::getResource)
                .map(DocumentReference.class::cast)
                .collect(Collectors.toList());
    }
}
