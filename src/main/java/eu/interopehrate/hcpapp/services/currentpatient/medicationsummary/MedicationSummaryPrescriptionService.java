package eu.interopehrate.hcpapp.services.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionInfoCommand;

import java.io.IOException;
import java.util.List;

public interface MedicationSummaryPrescriptionService {
    List<MedicationSummaryPrescriptionInfoCommand> getMedicationSummaryPrescriptionInfoCommandList();

    MedicationSummaryPrescriptionCommand prescriptionCommand() throws IOException;

    MedicationSummaryPrescriptionCommand prescriptionCommandUpload();

    MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoById(String id);

    void insertPrescription(MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand);

    void deletePrescription(String drugId);

    void updatePrescription(MedicationSummaryPrescriptionInfoCommand prescriptionInfoCommand);
}
