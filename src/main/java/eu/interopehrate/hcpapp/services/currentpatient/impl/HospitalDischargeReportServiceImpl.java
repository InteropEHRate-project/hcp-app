package eu.interopehrate.hcpapp.services.currentpatient.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.fhir.provenance.BundleProvenanceBuilder;
import eu.interopehrate.fhir.provenance.ResourceSigner;
import eu.interopehrate.hcpapp.currentsession.CloudConnection;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.AllergyRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.CurrentDiseaseRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.LaboratoryTestsRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.PrescriptionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.DiagnosticConclusionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.PHExamRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.ReasonRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.VitalSignsRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.HospitalDischargeReportCommand;
import eu.interopehrate.hcpapp.services.administration.HealthCareOrganizationService;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import eu.interopehrate.hcpapp.services.currentpatient.*;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import eu.interopehrate.hcpapp.services.currentpatient.impl.laboratorytests.ObservationLaboratoryServiceImpl;
import eu.interopehrate.hcpapp.services.index.IndexService;
import eu.interopehrate.protocols.common.FHIRResourceCategory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private IndexService indexService;
    private final VitalSignsService vitalSignsService;
    private final PHExamService phExamService;
    private final PHExamRepository phExamRepository;
    private final ObservationLaboratoryServiceImpl observationLaboratoryService;
    private final LaboratoryTestsRepository laboratoryTestsRepository;
    private final ReasonRepository reasonRepository;
    private final ReasonService reasonService;

    public HospitalDischargeReportServiceImpl(PrescriptionService prescriptionService, PrescriptionRepository prescriptionRepository, VitalSignsRepository vitalSignsRepository,
                                              CurrentDiseaseRepository currentDiseaseRepository, AllergyRepository allergyRepository, CloudConnection cloudConnection,
                                              DiagnosticConclusionRepository diagnosticConclusionRepository, CurrentDiseaseService currentDiseaseService, AllergyService allergyService,
                                              DiagnosticConclusionService diagnosticConclusionService, VitalSignsService vitalSignsService, PHExamService phExamService, PHExamRepository phExamRepository,
                                              ObservationLaboratoryServiceImpl observationLaboratoryService, LaboratoryTestsRepository laboratoryTestsRepository, ReasonRepository reasonRepository, ReasonService reasonService) {
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
        this.phExamService = phExamService;
        this.phExamRepository = phExamRepository;
        this.observationLaboratoryService = observationLaboratoryService;
        this.laboratoryTestsRepository = laboratoryTestsRepository;
        this.reasonRepository = reasonRepository;
        this.reasonService = reasonService;
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
    public PHExamRepository getPhExamRepository() {
        return phExamRepository;
    }

    @Override
    public LaboratoryTestsRepository getLaboratoryTestsRepository() { return laboratoryTestsRepository; }

    @Override
    public ReasonRepository getReasonRepository(){ return reasonRepository;}

    @Override
    public HospitalDischargeReportCommand hospitalDischargeReportCommand() {
        return new HospitalDischargeReportCommand(reasons, findings, procedures, conditions, instructions, hospitalName, hospitalAddress, patientName, patientDateBirth,
                patientGender, hcpName, format, prescriptionService, currentDiseaseService, allergyService, diagnosticConclusionService, vitalSignsService, phExamService, observationLaboratoryService, reasonService);
    }

    @Override
    public void insertDetails(HospitalDischargeReportCommand hospitalDischargeReportCommand) {
        this.reasons = hospitalDischargeReportCommand.getReasons();
        this.findings = hospitalDischargeReportCommand.getFindings();
        this.procedures = hospitalDischargeReportCommand.getProcedures();
        this.conditions = hospitalDischargeReportCommand.getConditions();
        this.instructions = hospitalDischargeReportCommand.getInstructions();
        this.hospitalName = this.healthCareOrganizationService.getHealthCareOrganization().getName();
        this.hospitalAddress = this.healthCareOrganizationService.getHealthCareOrganization().getAddress();
        this.patientName = this.indexService.getCurrentPatient().getPatient().getNameFirstRep().getNameAsSingleString();
        this.patientDateBirth = String.valueOf(this.indexService.getCurrentPatient().getPatient().getBirthDate());
        this.patientGender = this.indexService.getCurrentPatient().getPatient().getGender().toString();
        this.hcpName = this.healthCareProfessionalService.getHealthCareProfessional().getFirstName() + " " +
                this.healthCareProfessionalService.getHealthCareProfessional().getLastName();
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
        bundle.setId(UUID.randomUUID().toString());

        Practitioner author = new Practitioner();
        author.setId(UUID.randomUUID().toString());
        author.addName().setFamily(healthCareProfessionalService.getHealthCareProfessional().getFirstName() +
                " " + healthCareProfessionalService.getHealthCareProfessional().getLastName());
        bundle.addEntry().setResource(author);

        Organization hospital = new Organization();
        hospital.setId(UUID.randomUUID().toString());
        hospital.setName(healthCareOrganizationService.getHealthCareOrganization().getName());
        bundle.addEntry().setResource(hospital);

        Patient patient = new Patient();
        patient.setId(UUID.randomUUID().toString());
        patient.addName().setFamily(indexService.indexCommand().getPatientDataCommand().getFirstName() +
                " " + indexService.indexCommand().getPatientDataCommand().getLastName());
        bundle.addEntry().setResource(patient);

        Composition composition = new Composition();
        composition.setStatus(Composition.CompositionStatus.FINAL);
        composition.setId(UUID.randomUUID().toString());
        composition.setType(new CodeableConcept().addCoding(new Coding().setSystem("http://loinc.org").setCode("81214-9")));
        composition.setDate(new Date());
        composition.setTitle("Discharge Report");

        Meta profile = new Meta();
        profile.addProfile("http://interopehrate.eu/fhir/StructureDefinition/Composition-HospitalDischargeReport-IEHR");
        composition.setMeta(profile);

        Composition.SectionComponent docReferenceSection = new Composition.SectionComponent();
        docReferenceSection.setCode(new CodeableConcept(new Coding("http://loinc.org", "81218-0", "Discharge Report")));
        DocumentReference doc = new DocumentReference();

        doc.getContent().add(new DocumentReference.DocumentReferenceContentComponent());
        doc.setId(UUID.randomUUID().toString());
        doc.setType(new CodeableConcept().addCoding(new Coding().setSystem("http://loinc.org").setCode("81218-0")));
        doc.getContentFirstRep().getAttachment().setContentType("application/pdf");
        doc.getContentFirstRep().getAttachment().setLanguage("en");
        doc.getContentFirstRep().getAttachment().setData(bytes);
        doc.getContentFirstRep().getAttachment().setTitle("Hospital Discharge Report");
        doc.getContentFirstRep().getAttachment().setCreationElement(DateTimeType.now());
        docReferenceSection.addEntry().setResource(doc);
        composition.addSection(docReferenceSection);
        bundle.addEntry().setResource(doc);

        Meta profileDischarge = new Meta();
        profileDischarge.addProfile("http://interopehrate.eu/fhir/StructureDefinition/DocumentReference-IEHR");
        doc.setMeta(profileDischarge);

        //set encounter
        Encounter encounter = new Encounter();
        encounter.setId(UUID.randomUUID().toString());
        encounter.setMeta(profileDischarge);
        encounter.setStatus(Encounter.EncounterStatus.FINISHED);
        encounter.setClass_(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "AMB", "ambulatory"));

        Period period = new Period();
        period.setStart(new Date());

        encounter.setPeriod(period);
        composition.setEncounter(new Reference(encounter));
        bundle.addEntry().setResource(encounter);

        IParser parser1 = FhirContext.forR4().newJsonParser();
        ResourceSigner.INSTANCE.initialize("FTGM_iehr.p12", "FTGM_iehr", parser1);

        BundleProvenanceBuilder builder = new BundleProvenanceBuilder(hospital);
        builder.addProvenanceToBundleItems(bundle);
        System.out.println(FhirContext.forR4().newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

        return bundle;
    }
}
