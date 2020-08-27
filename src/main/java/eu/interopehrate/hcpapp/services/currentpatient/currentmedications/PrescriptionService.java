package eu.interopehrate.hcpapp.services.currentpatient.currentmedications;

import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.jpa.entities.PrescriptionEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionInfoCommand;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface PrescriptionService {

    boolean isFiltered();

    boolean isEmpty();

    CurrentD2DConnection getCurrentD2DConnection();

    PrescriptionCommand prescriptionCommand(String keyword) throws IOException;

    PrescriptionCommand prescriptionCommandUpload(String keyword);

    PrescriptionInfoCommand prescriptionInfoCommandById(Long id);

    void insertPrescription(PrescriptionInfoCommand prescriptionInfoCommand);

    void deletePrescription(Long drugId);

    void updatePrescription(PrescriptionInfoCommand prescriptionInfoCommand);

    void callSendPrescription() throws IOException;

    void sendPrescription(Bundle medicationRequest) throws IOException;

    Page<PrescriptionEntity> findPaginated(int pageNo, int pageSize, String sortField, String sortDir);
}
