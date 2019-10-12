package eu.interopehrate.hcpapp.services.testd2dlibrary.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.*;
import eu.interopehrate.hcpapp.jpa.repositories.HealthCareOrganizationRepository;
import eu.interopehrate.hcpapp.jpa.repositories.HealthCareProfessionalRepository;
import eu.interopehrate.hcpapp.mvc.commands.TestD2DLibraryCommand;
import eu.interopehrate.hcpapp.services.testd2dlibrary.TestD2DLibraryService;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TestD2DLibraryServiceImpl implements TestD2DLibraryService {
    private HealthCareProfessionalRepository healthCareProfessionalRepository;
    private HealthCareOrganizationRepository healthCareOrganizationRepository;
    private CurrentPatient currentPatient;
    private CurrentD2DConnection currentD2DConnection;
    private TestD2DLibraryCommand testD2DLibraryCommand = new TestD2DLibraryCommand();

    public TestD2DLibraryServiceImpl(HealthCareProfessionalRepository healthCareProfessionalRepository,
                                     HealthCareOrganizationRepository healthCareOrganizationRepository,
                                     CurrentPatient currentPatient,
                                     CurrentD2DConnection currentD2DConnection) {
        this.healthCareProfessionalRepository = healthCareProfessionalRepository;
        this.healthCareOrganizationRepository = healthCareOrganizationRepository;
        this.currentPatient = currentPatient;
        this.currentD2DConnection = currentD2DConnection;
    }

    @Override
    public TestD2DLibraryCommand currentState() {
        return this.testD2DLibraryCommand;
    }

    @Override
    public void openConnection() throws Exception {
        currentD2DConnection.open();
        testD2DLibraryCommand.setOn(Boolean.TRUE);
        testD2DLibraryCommand.setLastSEHRMessage(null);
        testD2DLibraryCommand.setSendActionMessage(null);
        currentPatient.reset();
    }

    @Override
    public void closeConnection() throws Exception {
        currentD2DConnection.close();
        testD2DLibraryCommand.setOn(Boolean.FALSE);
        testD2DLibraryCommand.setLastSEHRMessage(null);
        testD2DLibraryCommand.setSendActionMessage(null);
        currentPatient.reset();
    }

    @Override
    public void sendMessageToSEHR() throws Exception {
        currentD2DConnection.sendPractitioner(this.practitioner());
        testD2DLibraryCommand.setSendActionMessage("The details about organization and practitioner was sent to S-EHR.");
    }

    @Override
    public void lastSEHRMessage() {
        String lastSentPatientSummary = currentD2DConnection.lastPatientSummary();
        if (lastSentPatientSummary.contains("{")) {
            lastSentPatientSummary = lastSentPatientSummary.substring(lastSentPatientSummary.indexOf("{"));
            currentPatient.intiFromJsonString(lastSentPatientSummary);
        }
        testD2DLibraryCommand.setLastSEHRMessage(
                String.join("<br/><br/>",
                        this.patientToString(currentD2DConnection.lastPatient()),
                        lastSentPatientSummary));
        testD2DLibraryCommand.setSendActionMessage(null);
    }

    private Practitioner practitioner() {
        HealthCareProfessionalEntity healthCareProfessionalEntity = healthCareProfessionalRepository.findAll().get(0);
        AddressEntity addressEntity = healthCareProfessionalEntity.getAddresses().get(0);

        HumanName humanName = new HumanName()
                .setFamily(healthCareProfessionalEntity.getLastName())
                .addGiven(healthCareProfessionalEntity.getFirstName());
        Enumerations.AdministrativeGender gender = Enumerations.AdministrativeGender.valueOf(healthCareProfessionalEntity.getGender().name());
        Date birthDate = Date.from(healthCareProfessionalEntity.getBirthDate()
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
        Address address = buildAddressFromEntity(addressEntity);
        Practitioner.PractitionerQualificationComponent qualification = new Practitioner.PractitionerQualificationComponent();
        qualification.setCode(this.buildQualificationCode(healthCareProfessionalEntity.getOccupation()));
        qualification.setIssuerTarget(organization());
        Attachment photo = new Attachment()
                .setContentType(MimeTypeUtils.IMAGE_PNG_VALUE)
                .setData(healthCareProfessionalEntity.getPicture());

        return new Practitioner()
                .setPhoto(Collections.singletonList(photo))
                .setQualification(Collections.singletonList(qualification))
                .setBirthDate(birthDate)
                .setName(Collections.singletonList(humanName))
                .setGender(gender)
                .setAddress(Collections.singletonList(address));
    }

    private Organization organization() {
        HealthCareOrganizationEntity healthCareOrganizationEntity = healthCareOrganizationRepository.findAll().get(0);
        AddressEntity addressEntity = healthCareOrganizationEntity.getAddresses().get(0);
        List<ContactPointEntity> contactPointsEntities = healthCareOrganizationEntity.getContactPoints();

        Address address = buildAddressFromEntity(addressEntity);

        return new Organization()
                .setActive(Boolean.TRUE)
                .setName(healthCareOrganizationEntity.getName())
                .setAddress(Collections.singletonList(address))
                .setTelecom(contactPointsEntities
                        .stream()
                        .map(this::buildContactPointFromEntity)
                        .collect(Collectors.toList()));
    }

    private Address buildAddressFromEntity(AddressEntity addressEntity) {
        return new Address()
                .setLine(Collections.singletonList(new StringType(addressEntity.getDetails())))
                .setCity(addressEntity.getCity().getName())
                .setState(addressEntity.getCity().getCountry().getName())
                .setPostalCode(addressEntity.getPostalCode())
                .setUse(Address.AddressUse.valueOf(addressEntity.getUse().name()));
    }

    private ContactPoint buildContactPointFromEntity(ContactPointEntity contactPointEntity) {
        return new ContactPoint()
                .setSystem(ContactPoint.ContactPointSystem.valueOf(contactPointEntity.getType().name()))
                .setValue(contactPointEntity.getValue())
                .setUse(ContactPoint.ContactPointUse.valueOf(contactPointEntity.getUse().name()));
    }

    private CodeableConcept buildQualificationCode(HealthCareOccupationEntity healthCareOccupationEntity) {
        return new CodeableConcept()
                .setText(healthCareOccupationEntity.getName());
    }

    private String patientToString(Patient patient) {
        if (Objects.isNull(patient)) {
            return "no patient received yet";
        }
        FhirContext fc = FhirContext.forR4();
        IParser parser = fc.newJsonParser().setPrettyPrint(true);
        return parser.encodeResourceToString(patient);
    }
}
