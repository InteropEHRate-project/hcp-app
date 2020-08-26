package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.DocumentHistoryConsultationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentHistoryConsultationServiceImpl implements DocumentHistoryConsultationService {
    private CurrentPatient currentPatient;

    private DocumentHistoryConsultationInfoCommand documentHistoryConsultationInfoCommand1 = new DocumentHistoryConsultationInfoCommand();

    public DocumentHistoryConsultationServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public DocumentHistoryConsultationCommand documentHistoryConsultationCommand() {
        List<DocumentHistoryConsultationInfoCommand> documentHistoryConsultationInfoCommand = new ArrayList<>();
        this.documentHistoryConsultationInfoCommand1.setLocationHospital("Bucuresti");
        this.documentHistoryConsultationInfoCommand1.setExam("Visit");
        documentHistoryConsultationInfoCommand.add(this.documentHistoryConsultationInfoCommand1);

        return DocumentHistoryConsultationCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .documentHistoryConsultationInfoCommandList(documentHistoryConsultationInfoCommand)
                .build();
    }
}
