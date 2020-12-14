package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationCommand;


public interface DocumentHistoryConsultationService {
    CurrentD2DConnection getCurrentD2DConnection();

    boolean isFiltered();

    boolean isEmpty();

    DocumentHistoryConsultationCommand documentHistoryConsultationCommand(String speciality, String date, String start, String end) throws Exception;
}