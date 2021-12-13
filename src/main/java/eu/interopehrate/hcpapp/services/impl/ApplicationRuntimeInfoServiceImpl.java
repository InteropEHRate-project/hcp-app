package eu.interopehrate.hcpapp.services.impl;

import eu.interopehrate.hcpapp.jpa.entities.administration.*;
import eu.interopehrate.hcpapp.jpa.repositories.administration.HealthCareOrganizationRepository;
import eu.interopehrate.hcpapp.jpa.repositories.administration.HealthCareProfessionalRepository;
import eu.interopehrate.hcpapp.services.ApplicationRuntimeInfoService;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApplicationRuntimeInfoServiceImpl implements ApplicationRuntimeInfoService {
    private final HealthCareOrganizationRepository healthCareOrganizationRepository;
    private final HealthCareProfessionalRepository healthCareProfessionalRepository;

    public ApplicationRuntimeInfoServiceImpl(HealthCareOrganizationRepository healthCareOrganizationRepository,
                                             HealthCareProfessionalRepository healthCareProfessionalRepository) {
        this.healthCareOrganizationRepository = healthCareOrganizationRepository;
        this.healthCareProfessionalRepository = healthCareProfessionalRepository;
    }

    @Override
    public Organization organization() {
        HealthCareOrganizationEntity healthCareOrganizationEntity = healthCareOrganizationRepository.findAll().get(0);
        AddressEntity addressEntity = healthCareOrganizationEntity.getAddresses().iterator().next();
        Set<ContactPointEntity> contactPointsEntities = healthCareOrganizationEntity.getContactPoints();

        Address address = buildAddressFromEntity(addressEntity);

        Organization organization = new Organization()
                .setActive(Boolean.TRUE)
                .setName(healthCareOrganizationEntity.getName())
                .setAddress(Collections.singletonList(address))
                .setTelecom(contactPointsEntities
                        .stream()
                        .map(this::buildContactPointFromEntity)
                        .collect(Collectors.toList()));
        organization.setId(healthCareOrganizationEntity.getCode());
        return organization;
    }

    @Override
    public Practitioner practitioner() {
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

        Practitioner practitioner = new Practitioner()
                .setQualification(Collections.singletonList(qualification))

                .setBirthDate(birthDate)
                .setName(Collections.singletonList(humanName))
                .setGender(gender)
                .setAddress(Collections.singletonList(address));
        practitioner.setId(healthCareProfessionalEntity.getId().toString());
        return practitioner;
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
}
