package eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
@Getter
public class PrescriptionCommand {
    private Boolean displayTranslatedVersion;
    private Page<PrescriptionInfoCommand> pageInfoCommand;
}
