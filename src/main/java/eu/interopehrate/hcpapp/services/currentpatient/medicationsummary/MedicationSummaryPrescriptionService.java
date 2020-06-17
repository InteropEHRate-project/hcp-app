package eu.interopehrate.hcpapp.services.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionInfoCommand;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface MedicationSummaryPrescriptionService {
    List<MedicationSummaryPrescriptionInfoCommand> getMedicationSummaryPrescriptionInfoCommandList();
    MedicationSummaryPrescriptionCommand prescriptionCommand() throws IOException;
    MedicationSummaryPrescriptionCommand prescriptionCommandUpload();
    void insertPrescription(MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand);
    void deletePrescription(String drugId);
    void updatePrescription(String id, String status, String timings, String drugName, String drugDosage, String notes, LocalDate dateOfPrescription);
}
