package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandDocHistoryConsultation;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.DocumentHistoryConsultationService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DocumentHistoryConsultationServiceImpl implements DocumentHistoryConsultationService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandDocHistoryConsultation hapiToCommandDocHistoryConsultation;
    private boolean isFiltered = false;
    private boolean isEmpty = false;

    @SneakyThrows
    public DocumentHistoryConsultationServiceImpl(CurrentPatient currentPatient, HapiToCommandDocHistoryConsultation hapiToCommandDocHistoryConsultation) {
        this.currentPatient = currentPatient;
        this.hapiToCommandDocHistoryConsultation = hapiToCommandDocHistoryConsultation;
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
    public DocumentHistoryConsultationCommand documentHistoryConsultationCommand(String speciality) {
        var docHistoryConsultationCommands = this.currentPatient.docHistoryConsultationList()
                .stream()
                .map(this.hapiToCommandDocHistoryConsultation::convert)
                .collect(Collectors.toList());

        if (speciality.equalsIgnoreCase("all")) {
            this.isFiltered = false;
            this.isEmpty = false;
            return DocumentHistoryConsultationCommand.builder()
                    .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                    .documentHistoryConsultationInfoCommandList(docHistoryConsultationCommands)
                    .build();
        }
        this.isEmpty = false;
        if (!docHistoryConsultationCommands.isEmpty() && filter(docHistoryConsultationCommands, speciality).isEmpty()) {
            this.isEmpty = true;
            this.isFiltered = false;
        }
        if (!docHistoryConsultationCommands.isEmpty() && !filter(docHistoryConsultationCommands, speciality).isEmpty()) {
            this.isFiltered = true;
        }
        return DocumentHistoryConsultationCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .documentHistoryConsultationInfoCommandList(filter(docHistoryConsultationCommands, speciality))
                .build();
    }

    @Override
    public List<DocumentHistoryConsultationInfoCommand> filterByDate(List<DocumentHistoryConsultationInfoCommand> list, String style) {
        List<DocumentHistoryConsultationInfoCommand> returnedList = new ArrayList<>();
        if (!style.equals("all")) {
            if (style.equals("last-year")) {
                for (DocumentHistoryConsultationInfoCommand doc : list) {
                    if (LocalDate.now().getYear() - doc.getDate().getYear() <= 1) {
                        returnedList.add(doc);
                    }
                }
                if (!returnedList.isEmpty()) {
                    this.isEmpty = false;
                    this.isFiltered = true;
                } else {
                    this.isEmpty = true;
                    this.isFiltered = false;
                }
                return returnedList;
            }
            if (style.equals("last-5-years")) {
                for (DocumentHistoryConsultationInfoCommand doc : list) {
                    if (LocalDate.now().getYear() - doc.getDate().getYear() <= 5) {
                        returnedList.add(doc);
                    }
                }
                if (!returnedList.isEmpty()) {
                    this.isEmpty = false;
                    this.isFiltered = true;
                } else {
                    this.isEmpty = true;
                    this.isFiltered = false;
                }
                return returnedList;
            }
            this.isFiltered = false;
            this.isEmpty = false;
        }
        return list;
    }

    @Override
    public List<DocumentHistoryConsultationInfoCommand> filterBetween(List<DocumentHistoryConsultationInfoCommand> list, String start, String end) {
        LocalDate startDate;
        LocalDate endDate;
        List<DocumentHistoryConsultationInfoCommand> returnedList = new ArrayList<>();
        if (Objects.nonNull(start) && !start.equals("") && (Objects.isNull(end) || end.equals(""))) {
            startDate = LocalDate.parse(start);
            for (var doc : list) {
                if (startDate.compareTo(doc.getDate()) <= 0) {
                    returnedList.add(doc);
                }
            }
            if (!returnedList.isEmpty()) {
                this.isEmpty = false;
                this.isFiltered = true;
            } else {
                this.isEmpty = true;
                this.isFiltered = false;
            }
            return returnedList;
        }
        if (Objects.nonNull(end) && !end.equals("") && (Objects.isNull(start) || start.equals(""))) {
            endDate = LocalDate.parse(end);
            for (var doc : list) {
                if (endDate.compareTo(doc.getDate()) >= 0) {
                    returnedList.add(doc);
                }
            }
            if (!returnedList.isEmpty()) {
                this.isEmpty = false;
                this.isFiltered = true;
            } else {
                this.isEmpty = true;
                this.isFiltered = false;
            }
            return returnedList;
        }
        startDate = LocalDate.parse(start);
        endDate = LocalDate.parse(end);
        for (var doc : list) {
            if (startDate.compareTo(doc.getDate()) <= 0 && doc.getDate().compareTo(endDate) <= 0) {
                returnedList.add(doc);
            }
        }
        if (!returnedList.isEmpty()) {
            this.isEmpty = false;
            this.isFiltered = true;
        } else {
            this.isEmpty = true;
            this.isFiltered = false;
        }
        return returnedList;
    }

    private static List<DocumentHistoryConsultationInfoCommand> filter(List<DocumentHistoryConsultationInfoCommand> list, String speciality) {
        List<DocumentHistoryConsultationInfoCommand> documentHistoryConsultationInfoCommands = new ArrayList<>();
        list.forEach(dc -> {
            if (dc.getSpeciality().equalsIgnoreCase(speciality)) {
                documentHistoryConsultationInfoCommands.add(dc);
            }
        });
        return documentHistoryConsultationInfoCommands;
    }
}
