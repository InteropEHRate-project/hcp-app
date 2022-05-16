package eu.interopehrate.hcpapp.services.currentpatient.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.hcpapp.currentsession.CloudConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.administration.HealthCareOrganizationEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.OutpatientReportCommand;
import eu.interopehrate.hcpapp.mvc.commands.index.IndexPatientDataCommand;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import eu.interopehrate.hcpapp.services.currentpatient.*;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.MedicationService;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import eu.interopehrate.protocols.provenance.ProvenanceBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class OutpatientReportServiceImpl implements OutpatientReportService {
    private final PrescriptionService prescriptionService;
    private final VitalSignsService vitalSignsService;
    private final MedicationService medicationService;
    private final CurrentDiseaseService currentDiseaseService;
    private final AllergyService allergyService;
    private final DiagnosticConclusionService diagnosticConclusionService;
    private final InstrumentsExaminationService instrumentsExaminationService;
    private final CurrentD2DConnection currentD2DConnection;
    private final CloudConnection cloudConnection;
    private final CurrentPatient currentPatient;
    @Autowired
    private HealthCareProfessionalService healthCareProfessionalService;

    public OutpatientReportServiceImpl(PrescriptionService prescriptionService, VitalSignsService vitalSignsService,
                                       MedicationService medicationService, CurrentDiseaseService currentDiseaseService,
                                       AllergyService allergyService, DiagnosticConclusionService diagnosticConclusionService, InstrumentsExaminationService instrumentsExaminationService, CurrentD2DConnection currentD2DConnection, CloudConnection cloudConnection, CurrentPatient currentPatient) {
        this.prescriptionService = prescriptionService;
        this.vitalSignsService = vitalSignsService;
        this.medicationService = medicationService;
        this.currentDiseaseService = currentDiseaseService;
        this.allergyService = allergyService;
        this.diagnosticConclusionService = diagnosticConclusionService;
        this.instrumentsExaminationService = instrumentsExaminationService;
        this.currentD2DConnection = currentD2DConnection;
        this.cloudConnection = cloudConnection;
        this.currentPatient = currentPatient;
    }

    @Override
    public OutpatientReportCommand outpatientReportCommand() {
        return OutpatientReportCommand.builder()
                .prescriptionService(this.prescriptionService)
                .vitalSignsService(this.vitalSignsService)
                .medicationService(this.medicationService)
                .currentDiseaseService(this.currentDiseaseService)
                .allergyService(this.allergyService)
                .diagnosticConclusionService(this.diagnosticConclusionService)
                .instrumentsExaminationService(this.instrumentsExaminationService)
                .build();
    }

    @SneakyThrows
    @Override
    public void createBundle() {
        Bundle bundleEvaluation = new Bundle();

        Practitioner author = new Practitioner();
        author.setId(UUID.randomUUID().toString());
        author.addName().setFamily(healthCareProfessionalService.getHealthCareProfessional().getFirstName() +
                " " + healthCareProfessionalService.getHealthCareProfessional().getLastName());
        bundleEvaluation.addEntry().setResource(author);

        HealthCareOrganizationEntity healthCareOrganizationEntity = new HealthCareOrganizationEntity();
        Organization hospital = new Organization();
        hospital.setId(UUID.randomUUID().toString());
        hospital.setName(healthCareOrganizationEntity.getName());
        bundleEvaluation.addEntry().setResource(hospital);

        IndexPatientDataCommand patientDataCommand = new IndexPatientDataCommand();
        Patient patient = new Patient();
        patient.setId(UUID.randomUUID().toString());
//        patient.addName().setFamily(currentPatient.getPatient()
//                .getName()
//                .stream()
//                .map(humanName -> String.join(" ", humanName.getFamily(), humanName.getGivenAsSingleString()))
//                .collect(Collectors.joining(",")));
//
//        bundleEvaluation.addEntry().setResource(patient);

        MedicationStatement medicationStatement = prescriptionService.callSendPrescription();
        Observation observation = vitalSignsService.callVitalSigns();
        Condition currentDiseases = currentDiseaseService.callSendCurrentDiseases();
        Condition diagnosticConclusion = diagnosticConclusionService.callSendConclusion();
        CarePlan treatmentPlan = diagnosticConclusionService.callSendTreatment();
        DocumentReference instrumentalExamination = instrumentsExaminationService.callSendInstrumentalExamination();
        AllergyIntolerance allergies = allergyService.callAllergies();

        Composition composition = new Composition();
        composition.setStatus(Composition.CompositionStatus.FINAL);
        composition.setId(UUID.randomUUID().toString());
        composition.setType(new CodeableConcept().addCoding(new Coding().setSystem("http://loinc.org").setCode("81214-9")));
        composition.setDate(new Date());
        composition.setTitle("Medical Visit");

        bundleEvaluation.addEntry().setResource(composition);

        Meta profile = new Meta();
        profile.addProfile("http://interopehrate.eu/fhir/StructureDefinition/Composition-VisitReport-IEHR");
        composition.setMeta(profile);

        //set encounter
        Encounter encounter = new Encounter();
        encounter.setId(UUID.randomUUID().toString());
        encounter.setMeta(profile);
        encounter.setStatus(Encounter.EncounterStatus.FINISHED);
        encounter.setClass_(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "AMB", "ambulatory"));

        Period period = new Period();
        period.setStart(new Date());

        encounter.setPeriod(period);
        composition.setEncounter(new Reference(encounter));
        bundleEvaluation.addEntry().setResource(encounter);

        Composition.SectionComponent vSignsSection = new Composition.SectionComponent();
        vSignsSection.setCode(new CodeableConcept(new Coding("http://loinc.org", "8716-3", "Vital signs")));
        vSignsSection.addEntry().setResource(observation);
        composition.addSection(vSignsSection);
        bundleEvaluation.addEntry().setResource(observation);

        Composition.SectionComponent medicationSection = new Composition.SectionComponent();
        medicationSection.setCode(new CodeableConcept(new Coding("http://www.whocc.no/atc", "10183-2", "Hospital discharge medications")));
        medicationSection.addEntry().setResource(medicationStatement);
        composition.addSection(medicationSection);
        bundleEvaluation.addEntry().setResource(medicationStatement);
        try {
            if (Objects.nonNull(medicationStatement.getMedicationReference().getResource())) {
                bundleEvaluation.addEntry().setResource((Resource) medicationStatement.getMedicationReference().getResource());
            }
        } catch (NullPointerException exception) {
            System.out.println("Reference of Medication Statement is null.");
        }
        //bundleEvaluation.addEntry().setResource((Resource) medicationStatement.getMedicationReference().getResource());
        // ProvenanceBuilder.addProvenanceExtension(composition, medicationStatement);

        Composition.SectionComponent currentDiseasesSection = new Composition.SectionComponent();
        currentDiseasesSection.setCode(new CodeableConcept(new Coding("http://loinc.org", "75326-9", "Current Diseases")));
        currentDiseasesSection.addEntry().setResource(currentDiseases);
        composition.addSection(currentDiseasesSection);
        bundleEvaluation.addEntry().setResource(currentDiseases);

        Composition.SectionComponent diagnosticConclusionSection = new Composition.SectionComponent();
        diagnosticConclusionSection.setCode(new CodeableConcept(new Coding("http://loinc.org", "55110-1", "Diagnostic Conclusion")));
        diagnosticConclusionSection.addEntry().setResource(diagnosticConclusion);
        composition.addSection(diagnosticConclusionSection);
        bundleEvaluation.addEntry().setResource(diagnosticConclusion);

        Composition.SectionComponent treatmentPlanSection = new Composition.SectionComponent();
        treatmentPlanSection.setCode(new CodeableConcept(new Coding("http://loinc.org", "18776-5", "Treatment Plan")));
        treatmentPlanSection.addEntry().setResource(treatmentPlan);
        composition.addSection(treatmentPlanSection);
        bundleEvaluation.addEntry().setResource(treatmentPlan);

        Composition.SectionComponent instrumentExaminationSection = new Composition.SectionComponent();
        instrumentExaminationSection.setCode(new CodeableConcept(new Coding("http://loinc.org", "29545-1", "Instrument Examination")));
        instrumentExaminationSection.addEntry().setResource(instrumentalExamination);
        composition.addSection(instrumentExaminationSection);
        bundleEvaluation.addEntry().setResource(instrumentalExamination);

        Composition.SectionComponent allergiesSection = new Composition.SectionComponent();
        allergiesSection.setCode(new CodeableConcept(new Coding("http://loinc.org", "48765-2", "Allergies and adverse reactions Document")));
        allergiesSection.addEntry().setResource(allergies);
        composition.addSection(allergiesSection);
        bundleEvaluation.addEntry().setResource(allergies);

        IParser parser = FhirContext.forR4().newJsonParser().setPrettyPrint(false);
        String parseResource = parser.encodeResourceToString(composition);
        Provenance prov = ProvenanceBuilder.build(composition, author, hospital);

        PrivateKey privateKey = cloudConnection.cryptoManagement.getPrivateKey(cloudConnection.alias);
        byte[] certificateData = cloudConnection.cryptoManagement.getUserCertificate(cloudConnection.alias);
        String signed = cloudConnection.cryptoManagement.signPayload(parseResource, privateKey);
        String jwsToken = cloudConnection.cryptoManagement.createDetachedJws(certificateData, signed);

        prov.getSignatureFirstRep().setData(jwsToken.getBytes());
        bundleEvaluation.addEntry().setResource(prov);

        System.out.println(FhirContext.forR4().newJsonParser().setPrettyPrint(true).encodeResourceToString(bundleEvaluation));
        this.currentD2DConnection.getTd2D().sendHealthData(bundleEvaluation);
        log.info("Prescription sent to S-EHR");
        log.info("VitalSigns sent to S-EHR");
        log.info("Current Diseases sent to S-EHR");
    }
}
