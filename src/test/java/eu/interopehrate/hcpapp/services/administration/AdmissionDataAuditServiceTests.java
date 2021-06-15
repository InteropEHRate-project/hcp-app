package eu.interopehrate.hcpapp.services.administration;

import eu.interopehrate.hcpapp.jpa.entities.administration.AdmissionDataAuditEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import eu.interopehrate.hcpapp.jpa.repositories.administration.AdmissionDataAuditRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdmissionDataAuditServiceTests {
    @Autowired
    private AdmissionDataAuditRepository admissionDataAuditRepository;

    @Test
    public void testSaveAudit() {
        AdmissionDataAuditEntity admissionDataAuditEntity = new AdmissionDataAuditEntity();
        admissionDataAuditEntity.setDateTime(LocalDateTime.now());
        admissionDataAuditEntity.setType(AuditEventType.ADMISSION_DATA);
        admissionDataAuditEntity.setPatientName("patientName");
        admissionDataAuditEntity.setPatientId("patientId");
        admissionDataAuditEntity.setDateOfBirth(LocalDate.now());
        admissionDataAuditEntity.setGender("genderPatient");
        admissionDataAuditEntity.setHcpName("hcpName");
        admissionDataAuditEntity.setHcpId("idHCP");
        admissionDataAuditEntity.setHospitalName("hospitalName");
        admissionDataAuditEntity.setHospitalId("hospitalId");
        admissionDataAuditEntity.setDetails(admissionDataAuditEntity.toString());
        admissionDataAuditEntity = admissionDataAuditRepository.save(admissionDataAuditEntity);
        assertNotNull(admissionDataAuditEntity.getId());
    }
}
