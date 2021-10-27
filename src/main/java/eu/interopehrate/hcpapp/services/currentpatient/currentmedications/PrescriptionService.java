package eu.interopehrate.hcpapp.services.currentpatient.currentmedications;

import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.PrescriptionEntity;
import eu.interopehrate.hcpapp.jpa.repositories.PrescriptionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.PrescriptionTypesRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionInfoCommand;
import lombok.SneakyThrows;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface PrescriptionService {
    PrescriptionTypesRepository getPrescriptionTypesRepository();
    CurrentPatient getCurrentPatient();
    PrescriptionRepository getPrescriptionRepository();
    void setFiltered(boolean filtered);
    void setEmpty(boolean empty);
    boolean isFiltered();
    boolean isEmpty();
    CurrentD2DConnection getCurrentD2DConnection();
    PrescriptionCommand prescriptionCommand(int pageNo, int pageSize, String keyword) throws IOException;
    PrescriptionInfoCommand prescriptionInfoCommandById(Long id);
    void insertPrescription(PrescriptionInfoCommand prescriptionInfoCommand);
    void deletePrescription(Long drugId);
    void updatePrescription(PrescriptionInfoCommand prescriptionInfoCommand);
    void callSendPrescription() throws IOException;
    void sendPrescription(Bundle medicationRequest) throws IOException;
    Page<PrescriptionEntity> findPaginated(int pageNo, int pageSize);
    void refreshData();
    PrescriptionInfoCommand retrievePrescriptionFromSEHRById(String id);
    void updatePrescriptionFromSEHR(PrescriptionInfoCommand prescriptionInfoCommand);
    void getPrescriptionsTests();
}
