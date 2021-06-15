package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.PrescriptionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.VitalSignsRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.HospitalDischargeReportCommand;
import eu.interopehrate.hcpapp.services.currentpatient.HospitalDischargeReportService;
import org.springframework.stereotype.Service;

@Service
public class HospitalDischargeReportServiceImpl implements HospitalDischargeReportService {
    private String reasons;
    private String findings;
    private String procedures;
    private String conditions;
    private String instructions;
    private final PrescriptionRepository prescriptionRepository;
    private final VitalSignsRepository vitalSignsRepository;

    public HospitalDischargeReportServiceImpl(PrescriptionRepository prescriptionRepository, VitalSignsRepository vitalSignsRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.vitalSignsRepository = vitalSignsRepository;
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
}
