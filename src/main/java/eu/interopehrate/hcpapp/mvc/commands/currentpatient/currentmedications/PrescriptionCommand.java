package eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PrescriptionCommand {
    private Boolean displayTranslatedVersion;
    private List<PrescriptionInfoCommand> prescriptionInfoCommand;
}
