package eu.interopehrate.hcpapp.services.administration.impl;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.administration.AdmissionDataAuditEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import eu.interopehrate.hcpapp.jpa.repositories.administration.AdmissionDataAuditRepository;
import eu.interopehrate.hcpapp.services.ApplicationRuntimeInfoService;
import eu.interopehrate.hcpapp.services.administration.AdmissionDataAuditService;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AdmissionDataAuditServiceImpl implements AdmissionDataAuditService {
    private final AdmissionDataAuditRepository admissionDataAuditRepository;
    private final CurrentPatient currentPatient;
    private final ApplicationRuntimeInfoService applicationRuntimeInfoService;

    public AdmissionDataAuditServiceImpl(AdmissionDataAuditRepository admissionDataAuditRepository,
                                         CurrentPatient currentPatient,
                                         ApplicationRuntimeInfoService applicationRuntimeInfoService) {
        this.admissionDataAuditRepository = admissionDataAuditRepository;
        this.currentPatient = currentPatient;
        this.applicationRuntimeInfoService = applicationRuntimeInfoService;
    }

    @Override
    public void saveAdmissionData() {
        AdmissionDataAuditEntity admissionDataAuditEntity = new AdmissionDataAuditEntity();

        admissionDataAuditEntity.setDateTime(LocalDateTime.now());
        admissionDataAuditEntity.setType(AuditEventType.ADMISSION_DATA);

        String firstNamePatient;
        String lastNamePatient;
        String idPatient;
        LocalDate dateOfBirthPatient = null;
        String genderPatient;
        if (Objects.nonNull(currentPatient.getPatient()) && Objects.nonNull(currentPatient.getPatient().getName())) {
            firstNamePatient = currentPatient.getPatient()
                    .getName()
                    .stream()
                    .map(humanName -> String.join(" ", "First Name:" + " " + humanName.getGivenAsSingleString()))
                    .collect(Collectors.joining(","));
            lastNamePatient = currentPatient.getPatient()
                    .getName()
                    .stream()
                    .map(humanName -> String.join(" ", "Family Name:" + " " + humanName.getFamily()))
                    .collect(Collectors.joining(","));
            idPatient = currentPatient.getPatient().getId();
            try {
                dateOfBirthPatient = Instant.ofEpochMilli(currentPatient.getPatient().getBirthDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            } catch (NullPointerException e) {
                System.out.println("Error thrown for null BIRTH DATE!");
            }

            genderPatient = currentPatient.getPatient().getGender().toString();
        } else {
            firstNamePatient = "First_Name";
            lastNamePatient = "Last Name";
            idPatient = "ID";
            dateOfBirthPatient = null;
            genderPatient = "Gender";
        }

        admissionDataAuditEntity.setPatientName(firstNamePatient + " " + lastNamePatient);
        admissionDataAuditEntity.setPatientId(idPatient);
        admissionDataAuditEntity.setDateOfBirth(dateOfBirthPatient);
        admissionDataAuditEntity.setGender(genderPatient);
        Practitioner practitioner = applicationRuntimeInfoService.practitioner();
        HumanName practitionerHumanName = practitioner.getName().get(0);
        Organization organization = applicationRuntimeInfoService.organization();
        admissionDataAuditEntity.setHospitalName(organization.getName());
        admissionDataAuditEntity.setHospitalId(organization.getId());
        admissionDataAuditEntity.setHcpName(String.join(" ", practitionerHumanName.getGivenAsSingleString(), practitionerHumanName.getFamily()));
        admissionDataAuditEntity.setHcpId(practitioner.getId());
        String admission = admissionDataAuditEntity.toString();
        admissionDataAuditEntity.setDetails(admission);
        admissionDataAuditRepository.save(admissionDataAuditEntity);
    }
}
