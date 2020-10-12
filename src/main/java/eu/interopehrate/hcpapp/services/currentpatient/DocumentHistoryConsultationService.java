package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationCommand;

public interface DocumentHistoryConsultationService {
    boolean isFiltered();

    boolean isEmpty();

    DocumentHistoryConsultationCommand documentHistoryConsultationCommand(String speciality);
}