package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private String patientName;
    private String patientDateBirth;
    private String patientGender;
    private final String hcpName;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime localDateOfVisit;
    private String format;
    private final PrescriptionService prescriptionService;

    public HospitalDischargeReportCommand(String reasons, String findings, String procedures, String conditions, String instructions, String hospitalName, String hospitalAddress,
                                          String patientName, String patientDateBirth, String patientGender, String hcpName, String format, PrescriptionService prescriptionService) {
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
        this.format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
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
