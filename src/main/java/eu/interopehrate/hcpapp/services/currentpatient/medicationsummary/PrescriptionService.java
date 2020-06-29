package eu.interopehrate.hcpapp.services.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.PrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.PrescriptionInfoCommand;
import org.hl7.fhir.r4.model.MedicationRequest;

import java.io.IOException;

public interface PrescriptionService {

    PrescriptionCommand prescriptionCommand() throws IOException;

    PrescriptionCommand prescriptionCommandUpload();

    PrescriptionInfoCommand prescriptionInfoCommandById(Long id);

    void insertPrescription(PrescriptionInfoCommand prescriptionInfoCommand);

    void deletePrescription(Long drugId);

    void updatePrescription(PrescriptionInfoCommand prescriptionInfoCommand);

    void callSendPrescription() throws IOException;

    void sendPrescription(MedicationRequest medicationRequest) throws IOException;
}
