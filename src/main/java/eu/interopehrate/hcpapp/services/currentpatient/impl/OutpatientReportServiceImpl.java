package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.currentsession.CloudConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.OutpatientReportCommand;
import eu.interopehrate.hcpapp.services.currentpatient.*;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.MedicationService;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

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

    public OutpatientReportServiceImpl(PrescriptionService prescriptionService, VitalSignsService vitalSignsService,
                                       MedicationService medicationService, CurrentDiseaseService currentDiseaseService,
                                       AllergyService allergyService, DiagnosticConclusionService diagnosticConclusionService, InstrumentsExaminationService instrumentsExaminationService, CurrentD2DConnection currentD2DConnection, CloudConnection cloudConnection) {
        this.prescriptionService = prescriptionService;
        this.vitalSignsService = vitalSignsService;
        this.medicationService = medicationService;
        this.currentDiseaseService = currentDiseaseService;
        this.allergyService = allergyService;
        this.diagnosticConclusionService = diagnosticConclusionService;
        this.instrumentsExaminationService = instrumentsExaminationService;
        this.currentD2DConnection = currentD2DConnection;
        this.cloudConnection = cloudConnection;
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
        //bundleEvaluation.setEntry(new ArrayList<>(1));

        MedicationStatement medicationStatement = prescriptionService.callSendPrescription();
        Observation observation = vitalSignsService.callVitalSigns();
        Condition condition = currentDiseaseService.callSendCurrentDiseases();
        Condition diagnosticConclusion = diagnosticConclusionService.callSendConclusion();
        CarePlan treatmentPlan = diagnosticConclusionService.callSendTreatment();
        Composition composition = new Composition();
        //bundleEvaluation.getEntry().add(new Bundle.BundleEntryComponent().setResource(composition));
        composition.setType(new CodeableConcept().addCoding(new Coding().setSystem("http://loinc.org").setCode("81214-9")));

        // composition.addSection().addEntry().setResource(prescriptionService.callSendPrescription());
        composition.addSection().addEntry(new Reference(medicationStatement));
        // composition.addSection().addEntry().setResource(vitalSignsService.callVitalSigns());
        composition.addSection().addEntry(new Reference(observation));
        // composition.addSection().addEntry().setResource(currentDiseaseService.callSendCurrentDiseases());
        composition.addSection().addEntry(new Reference(condition));
        composition.addSection().addEntry(new Reference(diagnosticConclusion));
        composition.addSection().addEntry(new Reference(treatmentPlan));

        bundleEvaluation.addEntry().setResource(composition);
        bundleEvaluation.addEntry().setResource(medicationStatement);
        bundleEvaluation.addEntry().setResource(observation);
        bundleEvaluation.addEntry().setResource(condition);
        bundleEvaluation.addEntry().setResource(diagnosticConclusion);
        bundleEvaluation.addEntry().setResource(treatmentPlan);


//        composition.addExtension().setValue(new Provenance().addSignature()
//                .setWho(composition.getSubject().setReference(String.valueOf(composition.getAuthor())))
//                .setWhen(Date.from(Instant.now()))
//                .setTargetFormat("json").setSigFormat("application/jose")
//                .setData(cloudConnection.signingData().getBytes()))
//                .setUrl("http://interopehrate.eu/fhir/StructureDefinition/SignatureExtension-IEHR");

        this.currentD2DConnection.getTd2D().sendHealthData(bundleEvaluation);
        log.info("Prescription sent to S-EHR");
        log.info("VitalSigns sent to S-EHR");
        log.info("Current Diseases sent to S-EHR");
    }
}
