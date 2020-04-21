package eu.interopehrate.hcpapp.services.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionInfoCommand;

public interface MedicationSummaryPrescriptionService {
    MedicationSummaryPrescriptionCommand prescriptionCommand();

    void insertPrescription(MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand);
}
