package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

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

    public HospitalDischargeReportCommand(String reasons, String findings, String procedures, String conditions, String instructions) {
        this.reasons = reasons;
        this.findings = findings;
        this.procedures = procedures;
        this.conditions = conditions;
        this.instructions = instructions;
    }

    public Boolean hasData() {
        if ((Objects.nonNull(this.reasons) && !this.reasons.isBlank()) ||
                (Objects.nonNull(this.findings) && !this.findings.isBlank()) ||
                (Objects.nonNull(this.procedures) && !this.procedures.isBlank()) ||
                (Objects.nonNull(this.conditions) && !this.conditions.isBlank()) ||
                (Objects.nonNull(this.instructions) && !this.instructions.isBlank())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
