package eu.interopehrate.hcpapp.services.currentpatient.medicationsummary;


import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryMedicationCommand;

public interface MedicationSummaryMedicationService {
    MedicationSummaryMedicationCommand medicationCommand(String id);
}
