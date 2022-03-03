package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.currentsession.CloudConnection;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.AllergyRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.CurrentDiseaseRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.PrescriptionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.VitalSignsRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.HospitalDischargeReportCommand;
import eu.interopehrate.hcpapp.services.currentpatient.HospitalDischargeReportService;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import eu.interopehrate.protocols.common.FHIRResourceCategory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
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

    public HospitalDischargeReportServiceImpl(PrescriptionService prescriptionService, PrescriptionRepository prescriptionRepository, VitalSignsRepository vitalSignsRepository,
                                              CurrentDiseaseRepository currentDiseaseRepository, AllergyRepository allergyRepository, CloudConnection cloudConnection) {
        this.prescriptionService = prescriptionService;
        this.prescriptionRepository = prescriptionRepository;
        this.vitalSignsRepository = vitalSignsRepository;
        this.currentDiseaseRepository = currentDiseaseRepository;
        this.allergyRepository = allergyRepository;
        this.cloudConnection = cloudConnection;
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
    public HospitalDischargeReportCommand hospitalDischargeReportCommand() {
        return new HospitalDischargeReportCommand(reasons, findings, procedures, conditions, instructions, hospitalName, hospitalAddress, patientName, patientDateBirth,
                patientGender, hcpName, format, prescriptionService);
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

    private Bundle createBundle(byte[] bytes) {
        Bundle bundle = new Bundle();
        bundle.setEntry(new ArrayList<>(1));

        DocumentReference doc = new DocumentReference();
        bundle.getEntry().add(new Bundle.BundleEntryComponent().setResource(doc));

        doc.getContent().add(new DocumentReference.DocumentReferenceContentComponent());
        doc.setType(new CodeableConcept().addCoding(new Coding().setSystem("https://loinc.org").setCode("18842-5")));
        doc.getContentFirstRep().getAttachment().setContentType("application/pdf");
        doc.getContentFirstRep().getAttachment().setLanguage("en");
        doc.getContentFirstRep().getAttachment().setData(bytes);
        doc.getContentFirstRep().getAttachment().setTitle("Hospital Discharge Report");
        doc.getContentFirstRep().getAttachment().setCreationElement(DateTimeType.now());

        return bundle;
    }
}
