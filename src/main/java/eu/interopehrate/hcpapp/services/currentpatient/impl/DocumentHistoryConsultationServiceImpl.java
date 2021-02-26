package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandDocHistoryConsultation;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationCommand;
import eu.interopehrate.hcpapp.services.currentpatient.DocumentHistoryConsultationService;
import eu.interopehrate.hcpapp.services.index.impl.ContinueExistingVisitServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DocumentHistoryConsultationServiceImpl implements DocumentHistoryConsultationService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandDocHistoryConsultation hapiToCommandDocHistoryConsultation;
    private boolean isFiltered = false;
    private boolean isEmpty = false;
    private final CurrentD2DConnection currentD2DConnection;

    @SneakyThrows
    public DocumentHistoryConsultationServiceImpl(CurrentPatient currentPatient, HapiToCommandDocHistoryConsultation hapiToCommandDocHistoryConsultation, CurrentD2DConnection currentD2DConnection) {
        this.currentPatient = currentPatient;
        this.hapiToCommandDocHistoryConsultation = hapiToCommandDocHistoryConsultation;
        this.currentD2DConnection = currentD2DConnection;
    }

    @Override
    public CurrentD2DConnection getCurrentD2DConnection() {
        return currentD2DConnection;
    }

    @Override
    public boolean isFiltered() {
        return isFiltered;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public DocumentHistoryConsultationCommand documentHistoryConsultationCommand(String speciality, String date, String start, String end) throws Exception {
        if (this.currentPatient.getWithoutConnection()) {
            var documentList = this.currentPatient.docHistoryConsultationList()
                    .stream()
                    .map(this.hapiToCommandDocHistoryConsultation::convert)
                    .collect(Collectors.toList());
            return DocumentHistoryConsultationCommand.builder()
                    .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                    .documentHistoryConsultationInfoCommandList(documentList)
                    .build();
        }

        if (Objects.isNull(speciality) || Objects.isNull(date) || Objects.isNull(start) || Objects.isNull(end)) {
            if (ContinueExistingVisitServiceImpl.isExtractedData) {
                var documentList = this.currentPatient.docHistoryConsultationList().stream()
                        .map(this.hapiToCommandDocHistoryConsultation::convert)
                        .collect(Collectors.toList());
                return DocumentHistoryConsultationCommand.builder()
                        .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                        .documentHistoryConsultationInfoCommandList(documentList)
                        .build();
            }
            return DocumentHistoryConsultationCommand.builder()
                    .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                    .documentHistoryConsultationInfoCommandList(Collections.emptyList())
                    .build();
        }
        LocalDate startDate;
        LocalDate endDate;
        if (!start.equals("") && !end.equals("")) {
            startDate = LocalDate.parse(start);
            endDate = LocalDate.parse(end);
        } else if (start.equals("") && end.equals("") && date.equalsIgnoreCase("all")) {
            startDate = LocalDate.MIN;
            endDate = LocalDate.now();
        } else if (start.equals("") && end.equals("") && date.equalsIgnoreCase("lastYear")) {
            startDate = LocalDate.now().minusYears(1);
            endDate = LocalDate.now();
        } else if (start.equals("") && end.equals("") && date.equalsIgnoreCase("last5Years")) {
            startDate = LocalDate.now().minusYears(5);
            endDate = LocalDate.now();
        } else if (start.equals("") && !end.equals("")) {
            startDate = LocalDate.MIN;
            endDate = LocalDate.parse(end);
        } else {
            startDate = LocalDate.parse(start);
            endDate = LocalDate.now();
        }
        this.currentD2DConnection.sendMedicalDocumentRequest(startDate, endDate, speciality);
        this.currentD2DConnection.waitForDocumentHistoryInit();

        var list = this.currentPatient.docHistoryConsultationList()
                .stream()
                .map(this.hapiToCommandDocHistoryConsultation::convert)
                .collect(Collectors.toList());
        return DocumentHistoryConsultationCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .documentHistoryConsultationInfoCommandList(list)
                .build();
    }

}
