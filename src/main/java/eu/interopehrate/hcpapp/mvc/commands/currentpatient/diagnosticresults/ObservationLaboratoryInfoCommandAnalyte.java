package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ObservationLaboratoryInfoCommandAnalyte {
    @NotEmpty
    @NotNull
    private String analyte = "SARS CoV-2";
    @NotEmpty
    @NotNull
    private String value = "-";
}
