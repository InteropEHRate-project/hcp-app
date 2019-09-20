package eu.interopehrate.hcpapp.mvc.commands.currentpatient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicationSummaryInfoCommand {
    private String code;
    private String inn;
    private String manufacturer;
    private String concentration;
    private String dose;
    private String startDate;
    private String status;
}
