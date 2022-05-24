package eu.interopehrate.hcpapp.services.currentpatient.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.fhir.provenance.ResourceSigner;
import eu.interopehrate.hcpapp.currentsession.CloudConnection;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.AllergyRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.CurrentDiseaseRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.PrescriptionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.DiagnosticConclusionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.VitalSignsRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.HospitalDischargeReportCommand;
import eu.interopehrate.hcpapp.services.administration.HealthCareOrganizationService;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import eu.interopehrate.hcpapp.services.currentpatient.*;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import eu.interopehrate.protocols.common.FHIRResourceCategory;
import eu.interopehrate.protocols.provenance.ProvenanceBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class HospitalDischargeReportServiceImpl implements HospitalDischargeReportService {
    private String reasons;
    private String findings;
    private String procedures;
    private String conditions;
    private String instructions;
    private String hospitalName;
    private String hospitalAddress;
    private String patientName;
    private String patientDateBirth;
    private String patientGender;
    private String hcpName;
    private String format;
    private final PrescriptionService prescriptionService;
    private final PrescriptionRepository prescriptionRepository;
    private final VitalSignsRepository vitalSignsRepository;
    private final CurrentDiseaseRepository currentDiseaseRepository;
    private final AllergyRepository allergyRepository;
    private final CloudConnection cloudConnection;
    private final DiagnosticConclusionRepository diagnosticConclusionRepository;
    private final CurrentDiseaseService currentDiseaseService;
    private final AllergyService allergyService;
    private final DiagnosticConclusionService diagnosticConclusionService;
    @Autowired
    private HealthCareProfessionalService healthCareProfessionalService;
    @Autowired
    private HealthCareOrganizationService healthCareOrganizationService;
    private final VitalSignsService vitalSignsService;

    public HospitalDischargeReportServiceImpl(PrescriptionService prescriptionService, PrescriptionRepository prescriptionRepository, VitalSignsRepository vitalSignsRepository,
                                              CurrentDiseaseRepository currentDiseaseRepository, AllergyRepository allergyRepository, CloudConnection cloudConnection, DiagnosticConclusionRepository diagnosticConclusionRepository, CurrentDiseaseService currentDiseaseService, AllergyService allergyService, DiagnosticConclusionService diagnosticConclusionService, VitalSignsService vitalSignsService) {
        this.prescriptionService = prescriptionService;
        this.prescriptionRepository = prescriptionRepository;
        this.vitalSignsRepository = vitalSignsRepository;
        this.currentDiseaseRepository = currentDiseaseRepository;
        this.allergyRepository = allergyRepository;
        this.cloudConnection = cloudConnection;
        this.diagnosticConclusionRepository = diagnosticConclusionRepository;
        this.currentDiseaseService = currentDiseaseService;
        this.allergyService = allergyService;
        this.diagnosticConclusionService = diagnosticConclusionService;
        this.vitalSignsService = vitalSignsService;
    }

    @Override
    public CurrentDiseaseRepository getCurrentDiseaseRepository() {
        return currentDiseaseRepository;
    }

    @Override
    public AllergyRepository getAllergyRepository() {
        return allergyRepository;
    }

    @Override
    public PrescriptionRepository getPrescriptionRepository() {
        return prescriptionRepository;
    }

    @Override
    public VitalSignsRepository getVitalSignsRepository() {
        return vitalSignsRepository;
    }

    @Override
    public DiagnosticConclusionRepository getDiagnosticConclusionRepository() {
        return diagnosticConclusionRepository;
    }

    @Override
    public HospitalDischargeReportCommand hospitalDischargeReportCommand() {
        return new HospitalDischargeReportCommand(reasons, findings, procedures, conditions, instructions, hospitalName, hospitalAddress, patientName, patientDateBirth,
                patientGender, hcpName, format, prescriptionService, currentDiseaseService, allergyService, diagnosticConclusionService, vitalSignsService);
    }

    @Override
    public void insertDetails(HospitalDischargeReportCommand hospitalDischargeReportCommand) {
        this.reasons = hospitalDischargeReportCommand.getReasons();
        this.findings = hospitalDischargeReportCommand.getFindings();
        this.procedures = hospitalDischargeReportCommand.getProcedures();
        this.conditions = hospitalDischargeReportCommand.getConditions();
        this.instructions = hospitalDischargeReportCommand.getInstructions();
        this.hospitalName = hospitalDischargeReportCommand.getHospitalName();
        this.hospitalAddress = hospitalDischargeReportCommand.getHospitalAddress();
        this.patientName = hospitalDischargeReportCommand.getPatientName();
        this.patientDateBirth = hospitalDischargeReportCommand.getPatientDateBirth();
        this.patientGender = hospitalDischargeReportCommand.getPatientGender();
        this.hcpName = hospitalDischargeReportCommand.getHcpName();
        this.format = hospitalDischargeReportCommand.getFormat();
    }

    @Override
    @SneakyThrows
    public Boolean saveInCloud(byte[] bytes) {
        String content = SendToOtherHcpServiceImpl.convertBundleIntoString(createBundle(bytes));
        try {
            String result = this.cloudConnection.getR2dEmergency().create(this.cloudConnection.getEmergencyToken(), FHIRResourceCategory.DOCUMENT_REFERENCE, content);

            if (result.toUpperCase().contains("ERROR")) {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error in saving data into Cloud", e);
            return Boolean.FALSE;
        }
    }

    @SneakyThrows
    private Bundle createBundle(byte[] bytes) {
        Bundle bundle = new Bundle();
        bundle.setEntry(new ArrayList<>(1));

        Practitioner author = new Practitioner();
        author.setId(UUID.randomUUID().toString());
        author.addName().setFamily(healthCareProfessionalService.getHealthCareProfessional().getFirstName() +
                " " + healthCareProfessionalService.getHealthCareProfessional().getLastName());
        bundle.addEntry().setResource(author);

        Organization hospital = new Organization();
        hospital.setId(UUID.randomUUID().toString());
        hospital.setName(healthCareOrganizationService.getHealthCareOrganization().getName());

//      BundleProvenanceBuilder builder = new BundleProvenanceBuilder(hospital);
//      List<Provenance> provenances = builder.addProvenanceToBundleItems(bundle);
        bundle.addEntry().setResource(hospital);

        Patient patient = new Patient();
        patient.setId(UUID.randomUUID().toString());
        //patient.addName().setFamily(indexService.indexCommand().getPatientDataCommand().getFirstName());
        bundle.addEntry().setResource(patient);

        DocumentReference doc = new DocumentReference();
        bundle.getEntry().add(new Bundle.BundleEntryComponent().setResource(doc));

        doc.getContent().add(new DocumentReference.DocumentReferenceContentComponent());
        doc.setId(UUID.randomUUID().toString());
        doc.setType(new CodeableConcept().addCoding(new Coding().setSystem("http://loinc.org").setCode("18842-5")));
        doc.getContentFirstRep().getAttachment().setContentType("application/pdf");
        doc.getContentFirstRep().getAttachment().setLanguage("en");
        doc.getContentFirstRep().getAttachment().setData(bytes);
        doc.getContentFirstRep().getAttachment().setTitle("Hospital Discharge Report");
        doc.getContentFirstRep().getAttachment().setCreationElement(DateTimeType.now());

        Meta profileDischarge = new Meta();
        profileDischarge.addProfile("http://interopehrate.eu/fhir/StructureDefinition/Composition-VisitReport-IEHR");
        bundle.setMeta(profileDischarge);

        //set encounter
        Encounter encounter = new Encounter();
        encounter.setId(UUID.randomUUID().toString());
        encounter.setMeta(profileDischarge);
        encounter.setStatus(Encounter.EncounterStatus.FINISHED);
        encounter.setClass_(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "AMB", "ambulatory"));

        Period period = new Period();
        period.setStart(new Date());

        encounter.setPeriod(period);
        //doc.setEncounter(new Reference(encounter));
        ProvenanceBuilder.addProvenanceExtension(doc, encounter);
        bundle.addEntry().setResource(encounter);

        IParser parser1 = FhirContext.forR4().newJsonParser();
        ResourceSigner.INSTANCE.initialize("keystore.p12", "healthorganization", parser1);

        IParser parser = FhirContext.forR4().newJsonParser().setPrettyPrint(false);
        String parseResource = parser.encodeResourceToString(doc);
        Provenance prov = ProvenanceBuilder.build(doc, author, hospital);

        PrivateKey privateKey = cloudConnection.cryptoManagement.getPrivateKey(cloudConnection.alias);
        byte[] certificateData = cloudConnection.cryptoManagement.getUserCertificate(cloudConnection.alias);
        String signed = cloudConnection.cryptoManagement.signPayload(parseResource, privateKey);
        String jwsToken = cloudConnection.cryptoManagement.createDetachedJws(certificateData, signed);

        prov.getSignatureFirstRep().setData(jwsToken.getBytes());
        bundle.addEntry().setResource(prov);

        System.out.println(FhirContext.forR4().newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

        return bundle;
    }
}
