package eu.interopehrate.hcpapp.services.currentpatient.currentmedications;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionInfoCommand;
import org.hl7.fhir.r4.model.Bundle;

import java.io.IOException;
import java.util.List;

public interface PrescriptionService {

    void setPrescriptionsUploadedToSEHR(List<PrescriptionInfoCommand> prescriptionsUploadedToSEHR);

    List<PrescriptionInfoCommand> getPrescriptionsUploadedToSEHR();

    PrescriptionCommand prescriptionCommand() throws IOException;

    PrescriptionCommand prescriptionCommandUpload();

    PrescriptionInfoCommand prescriptionInfoCommandById(Long id);

    void insertPrescription(PrescriptionInfoCommand prescriptionInfoCommand);

    void deletePrescription(Long drugId);

    void updatePrescription(PrescriptionInfoCommand prescriptionInfoCommand);

    void callSendPrescription() throws IOException;

    void sendPrescription(Bundle medicationRequest) throws IOException;
}
