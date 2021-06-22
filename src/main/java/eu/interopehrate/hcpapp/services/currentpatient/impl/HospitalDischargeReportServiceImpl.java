package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.currentsession.CloudConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.PrescriptionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.VitalSignsRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.HospitalDischargeReportCommand;
import eu.interopehrate.hcpapp.services.currentpatient.HospitalDischargeReportService;
import eu.interopehrate.protocols.common.FHIRResourceCategory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DocumentReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class HospitalDischargeReportServiceImpl implements HospitalDischargeReportService {
    private String reasons;
    private String findings;
    private String procedures;
    private String conditions;
    private String instructions;
    private final PrescriptionRepository prescriptionRepository;
    private final VitalSignsRepository vitalSignsRepository;
    private final CloudConnection cloudConnection;
    private final CurrentPatient currentPatient;

    public HospitalDischargeReportServiceImpl(PrescriptionRepository prescriptionRepository, VitalSignsRepository vitalSignsRepository,
                                              CloudConnection cloudConnection, CurrentPatient currentPatient) {
        this.prescriptionRepository = prescriptionRepository;
        this.vitalSignsRepository = vitalSignsRepository;
        this.cloudConnection = cloudConnection;
        this.currentPatient = currentPatient;
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
    public HospitalDischargeReportCommand hospitalDischargeReportCommand() {
        return new HospitalDischargeReportCommand(reasons, findings, procedures, conditions, instructions);
    }

    @Override
    public void insertDetails(HospitalDischargeReportCommand hospitalDischargeReportCommand) {
        this.reasons = hospitalDischargeReportCommand.getReasons();
        this.findings = hospitalDischargeReportCommand.getFindings();
        this.procedures = hospitalDischargeReportCommand.getProcedures();
        this.conditions = hospitalDischargeReportCommand.getConditions();
        this.instructions = hospitalDischargeReportCommand.getInstructions();
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

    private Bundle createBundle(byte[] bytes) {
        Bundle bundle = new Bundle();
        bundle.setEntry(new ArrayList<>(1));

        DocumentReference doc = new DocumentReference();
        bundle.getEntry().add(new Bundle.BundleEntryComponent().setResource(doc));

        doc.getContent().add(new DocumentReference.DocumentReferenceContentComponent());
        doc.getContentFirstRep().getAttachment().setContentType("application/pdf");
        doc.getContentFirstRep().getAttachment().setLanguage("en");
        doc.getContentFirstRep().getAttachment().setData(bytes);
        doc.getContentFirstRep().getAttachment().setTitle("Hospital Discharge Report");
        doc.getContentFirstRep().getAttachment().setCreationElement(DateTimeType.now());

        return bundle;
    }
}
