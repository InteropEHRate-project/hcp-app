package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.DocumentHistoryConsultationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentHistoryConsultationServiceImpl implements DocumentHistoryConsultationService {
    private CurrentPatient currentPatient;
    private List<DocumentHistoryConsultationInfoCommand> documentHistoryConsultationInfoCommands = new ArrayList<>();
    private DocumentHistoryConsultationInfoCommand documentHistoryConsultationInfoCommand1 = new DocumentHistoryConsultationInfoCommand();
    private DocumentHistoryConsultationInfoCommand documentHistoryConsultationInfoCommand2 = new DocumentHistoryConsultationInfoCommand();

    public DocumentHistoryConsultationServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;

        this.documentHistoryConsultationInfoCommand1.setLocalDateTimeHistoryConsultation(LocalDateTime.of(2020, 8, 2, 10, 51));
        this.documentHistoryConsultationInfoCommand1.setSpeciality("Cardiology");
        this.documentHistoryConsultationInfoCommand1.setLocationHospital("Bucharest");
        this.documentHistoryConsultationInfoCommand1.setExam("Visit");

        this.documentHistoryConsultationInfoCommand2.setLocalDateTimeHistoryConsultation(LocalDateTime.of(2020, 6, 5, 10, 31));
        this.documentHistoryConsultationInfoCommand2.setSpeciality("Psychiatry");
        this.documentHistoryConsultationInfoCommand2.setLocationHospital("Timisoara");
        this.documentHistoryConsultationInfoCommand2.setExam("Visit");

        this.documentHistoryConsultationInfoCommands.add(this.documentHistoryConsultationInfoCommand1);
        this.documentHistoryConsultationInfoCommands.add(this.documentHistoryConsultationInfoCommand2);
    }

    @Override
    public DocumentHistoryConsultationCommand documentHistoryConsultationCommand(String speciality) {
        if (speciality.equalsIgnoreCase("all")) {
            return DocumentHistoryConsultationCommand.builder()
                    .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                    .documentHistoryConsultationInfoCommandList(documentHistoryConsultationInfoCommands)
                    .build();
        }
        return DocumentHistoryConsultationCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .documentHistoryConsultationInfoCommandList(filter(this.documentHistoryConsultationInfoCommands, speciality))
                .build();
    }

    private static List<DocumentHistoryConsultationInfoCommand> filter(List<DocumentHistoryConsultationInfoCommand> list, String speciality) {
        List<DocumentHistoryConsultationInfoCommand> documentHistoryConsultationInfoCommands = new ArrayList<>();
        for (DocumentHistoryConsultationInfoCommand dc : list) {
            if (dc.getSpeciality().equalsIgnoreCase(speciality)) {
                documentHistoryConsultationInfoCommands.add(dc);
            }
        }
        return documentHistoryConsultationInfoCommands;
    }
}
