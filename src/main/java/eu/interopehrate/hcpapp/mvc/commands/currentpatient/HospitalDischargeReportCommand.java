package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class HospitalDischargeReportCommand {
    private final String reasons;
    private final String findings;
    private final String procedures;
    private final String conditions;
    private final String instructions;
    private final String hospitalName;
    private final String hospitalAddress;
    private final String patientName;
    private final String patientDateBirth;
    private final String patientGender;
    private final String hcpName;
    private final String dateOfVisit;
    private final String timeOfVisit;
    private final PrescriptionService prescriptionService;

    public HospitalDischargeReportCommand(String reasons, String findings, String procedures, String conditions, String instructions, String hospitalName, String hospitalAddress, String patientName, String patientDateBirth, String patientGender,
                                          String hcpName, String dateOfVisit, String timeOfVisit, PrescriptionService prescriptionService) {
        this.reasons = reasons;
        this.findings = findings;
        this.procedures = procedures;
        this.conditions = conditions;
        this.instructions = instructions;
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.patientName = patientName;
        this.patientDateBirth = patientDateBirth;
        this.patientGender = patientGender;
        this.hcpName = hcpName;
        this.dateOfVisit = dateOfVisit;
        this.timeOfVisit = timeOfVisit;
        this.prescriptionService = prescriptionService;
    }

    public Boolean hasData() {
        if ((Objects.nonNull(this.reasons) && !this.reasons.isBlank()) ||
                (Objects.nonNull(this.findings) && !this.findings.isBlank()) ||
                (Objects.nonNull(this.procedures) && !this.procedures.isBlank()) ||
                (Objects.nonNull(this.conditions) && !this.conditions.isBlank()) ||
                (Objects.nonNull(this.instructions) && !this.instructions.isBlank()) ||
                (Objects.nonNull(this.hospitalName) && !this.hospitalName.isBlank()) ||
                (Objects.nonNull(this.prescriptionService) && !this.prescriptionService.isEmpty())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
