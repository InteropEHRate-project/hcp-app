package eu.interopehrate.hcpapp.currentpatient;

import org.hl7.fhir.r4.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class BundleProcessor {
    private Bundle bundle;

    public BundleProcessor(Bundle bundle) {
        this.bundle = bundle;
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

    public List<Observation> observationList() {
        return bundle.getEntry()
                .stream()
                .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.Observation))
                .map(Bundle.BundleEntryComponent::getResource)
                .map(Observation.class::cast)
                .collect(Collectors.toList());
    }
}
