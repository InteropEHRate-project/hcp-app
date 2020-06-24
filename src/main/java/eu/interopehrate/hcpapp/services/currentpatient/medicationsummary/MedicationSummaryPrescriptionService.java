package eu.interopehrate.hcpapp.services.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionInfoCommand;
import org.hl7.fhir.r4.model.MedicationRequest;

import java.io.IOException;

public interface MedicationSummaryPrescriptionService {

    MedicationSummaryPrescriptionCommand prescriptionCommand() throws IOException;

    MedicationSummaryPrescriptionCommand prescriptionCommandUpload();

    MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommandById(Long id);

    void insertPrescription(MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand);

    void deletePrescription(Long drugId);

    void updatePrescription(MedicationSummaryPrescriptionInfoCommand prescriptionInfoCommand);

    void callSendPrescription();

    void sendPrescription(MedicationRequest medicationRequest) throws IOException;
}
