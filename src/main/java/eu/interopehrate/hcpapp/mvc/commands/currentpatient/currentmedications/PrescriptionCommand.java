package eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
@Getter
public class PrescriptionCommand {
    private final Boolean displayTranslatedVersion;
    private final Page<PrescriptionInfoCommand> pageInfoCommand;

    public PrescriptionInfoCommand find(Long id) {
        for (PrescriptionInfoCommand pr : this.pageInfoCommand) {
            if (pr.getId().equals(id)) {
                return pr;
            }
        }
        return null;
    }
}
