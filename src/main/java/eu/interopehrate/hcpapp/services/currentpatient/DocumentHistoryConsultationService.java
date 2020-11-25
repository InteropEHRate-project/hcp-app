package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationInfoCommand;

import java.util.List;

public interface DocumentHistoryConsultationService {
    boolean isFiltered();

    boolean isEmpty();

    DocumentHistoryConsultationCommand documentHistoryConsultationCommand(String speciality);

    List<DocumentHistoryConsultationInfoCommand> filterBetween(List<DocumentHistoryConsultationInfoCommand> list, String start, String end);
}