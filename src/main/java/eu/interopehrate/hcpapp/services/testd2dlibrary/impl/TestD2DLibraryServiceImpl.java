package eu.interopehrate.hcpapp.services.testd2dlibrary.impl;

import eu.interopehrate.hcpapp.jpa.entities.AddressEntity;
import eu.interopehrate.hcpapp.jpa.entities.ContactPointEntity;
import eu.interopehrate.hcpapp.jpa.entities.HealthCareOrganizationEntity;
import eu.interopehrate.hcpapp.jpa.entities.HealthCareProfessionalEntity;
import eu.interopehrate.hcpapp.jpa.repositories.HealthCareOrganizationRepository;
import eu.interopehrate.hcpapp.jpa.repositories.HealthCareProfessionalRepository;
import eu.interopehrate.hcpapp.mvc.commands.TestD2DLibraryCommand;
import eu.interopehrate.hcpapp.services.testd2dlibrary.TestD2DLibraryService;
import eu.interopehrate.td2de.BluetoothConnection;
import eu.interopehrate.td2de.ConnectedThread;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestD2DLibraryServiceImpl implements TestD2DLibraryService {
    private HealthCareProfessionalRepository healthCareProfessionalRepository;
    private HealthCareOrganizationRepository healthCareOrganizationRepository;
    private TestD2DLibraryCommand testD2DLibraryCommand = new TestD2DLibraryCommand();
    private BluetoothConnection bluetoothConnection;
    private ConnectedThread connectedThread;

    public TestD2DLibraryServiceImpl(HealthCareProfessionalRepository healthCareProfessionalRepository,
                                     HealthCareOrganizationRepository healthCareOrganizationRepository) {
        this.healthCareProfessionalRepository = healthCareProfessionalRepository;
        this.healthCareOrganizationRepository = healthCareOrganizationRepository;
    }

    @Override
    public TestD2DLibraryCommand currentState() {
        return this.testD2DLibraryCommand;
    }

    @Override
    public void openConnection() throws Exception {
        bluetoothConnection = new BluetoothConnection();
        connectedThread = bluetoothConnection.startListening();
        testD2DLibraryCommand.setOn(Boolean.TRUE);
        testD2DLibraryCommand.setLastSEHRMessage(null);
        testD2DLibraryCommand.setSendActionMessage(null);
    }

    @Override
    public void closeConnection() throws Exception {
        bluetoothConnection.closeConnection();
        testD2DLibraryCommand.setOn(Boolean.FALSE);
        testD2DLibraryCommand.setLastSEHRMessage(null);
        testD2DLibraryCommand.setSendActionMessage(null);
    }

    @Override
    public void sendMessageToSEHR() throws Exception {
        connectedThread.sendData(this.practitioner(), this.organization());
        testD2DLibraryCommand.setSendActionMessage("The details about organization and practitioner was sent to S-EHR.");
        testD2DLibraryCommand.setLastSEHRMessage(null);
    }

    @Override
    public void lastSEHRMessage() {
        testD2DLibraryCommand.setLastSEHRMessage(connectedThread.getLastSentData());
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

        return new Practitioner()
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
}
