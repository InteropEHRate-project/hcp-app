package eu.interopehrate.hcpapp.services.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionInfoCommand;

import java.io.IOException;

public interface MedicationSummaryPrescriptionService {
    MedicationSummaryPrescriptionCommand prescriptionCommand() throws IOException;
    MedicationSummaryPrescriptionCommand prescriptionCommandUpload();
    void insertPrescription(MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand);
    void deletePrescription(String drugId);
}
