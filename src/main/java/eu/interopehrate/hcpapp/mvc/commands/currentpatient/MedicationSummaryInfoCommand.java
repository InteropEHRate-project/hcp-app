package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MedicationSummaryInfoCommand {
    private String code;
    private String inn;
    private String manufacturer;
    private String concentration;
    private String dose;
    private LocalDate startDate;
    private String status;
}
