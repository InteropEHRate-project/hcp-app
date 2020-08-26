package eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DocumentHistoryConsultationCommand {
    private Boolean displayTranslatedVersion;
    private List<DocumentHistoryConsultationInfoCommand> documentHistoryConsultationInfoCommandList;
}
