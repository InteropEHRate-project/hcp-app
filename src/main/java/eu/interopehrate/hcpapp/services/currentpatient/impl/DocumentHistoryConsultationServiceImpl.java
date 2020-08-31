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
    private DocumentHistoryConsultationInfoCommand documentHistoryConsultationInfoCommand2 = new DocumentHistoryConsultationInfoCommand();

    public DocumentHistoryConsultationServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public DocumentHistoryConsultationCommand documentHistoryConsultationCommand(String speciality) {
        List<DocumentHistoryConsultationInfoCommand> documentHistoryConsultationInfoCommand = new ArrayList<>();
        this.documentHistoryConsultationInfoCommand1.setSpeciality("Cardiology");
        this.documentHistoryConsultationInfoCommand1.setLocationHospital("Bucharest");
        this.documentHistoryConsultationInfoCommand1.setExam("Visit");

        this.documentHistoryConsultationInfoCommand2.setSpeciality("Psychiatry");
        this.documentHistoryConsultationInfoCommand2.setLocationHospital("Bucharest");
        this.documentHistoryConsultationInfoCommand2.setExam("Visit");

        documentHistoryConsultationInfoCommand.add(this.documentHistoryConsultationInfoCommand1);
        documentHistoryConsultationInfoCommand.add(this.documentHistoryConsultationInfoCommand2);

        return DocumentHistoryConsultationCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .documentHistoryConsultationInfoCommandList(documentHistoryConsultationInfoCommand)
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
