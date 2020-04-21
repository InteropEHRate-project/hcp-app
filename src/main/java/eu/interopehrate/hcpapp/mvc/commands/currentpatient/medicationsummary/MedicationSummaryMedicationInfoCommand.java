package eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MedicationSummaryMedicationInfoCommand {
    @NotEmpty
    @NotNull
    private String id;
    @NotEmpty
    @NotNull
    private String code = "-";
    @NotEmpty
    @NotNull
    private String name = "-";
}
