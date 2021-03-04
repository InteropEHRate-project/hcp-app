package eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DocumentHistoryConsultationCommand {
    private final Boolean displayTranslatedVersion;
    private final List<DocumentHistoryConsultationInfoCommand> documentHistoryConsultationInfoCommandList;

    public DocumentHistoryConsultationInfoCommand find(String exam, String date) {
        for (DocumentHistoryConsultationInfoCommand doc : this.documentHistoryConsultationInfoCommandList) {
            if (doc.getExam().equals(exam) && doc.getDate().toString().equals(date)) {
                return doc;
            }
        }
        return null;
    }
}
