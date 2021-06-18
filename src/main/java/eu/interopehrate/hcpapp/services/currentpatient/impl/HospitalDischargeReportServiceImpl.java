package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.currentsession.CloudConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.PrescriptionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.VitalSignsRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.HospitalDischargeReportCommand;
import eu.interopehrate.hcpapp.services.currentpatient.HospitalDischargeReportService;
import eu.interopehrate.protocols.common.DocumentCategory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public Boolean saveInCloud() {
        String content = SendToOtherHcpServiceImpl.convertBundleIntoString(this.currentPatient.getPatientSummaryBundle());
        try {
            this.cloudConnection.getR2dEmergency().create(this.cloudConnection.getEmergencyToken(), DocumentCategory.PATIENT_SUMMARY, content);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error in saving data into Cloud", e);
            return Boolean.FALSE;
        }
    }
}
