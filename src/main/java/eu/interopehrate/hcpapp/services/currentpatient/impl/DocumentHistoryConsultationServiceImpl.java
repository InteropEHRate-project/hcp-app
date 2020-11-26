package eu.interopehrate.hcpapp.services.currentpatient.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandDocHistoryConsultation;
import eu.interopehrate.hcpapp.currentsession.BundleProcessor;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.DocumentHistoryConsultationService;
import lombok.SneakyThrows;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DocumentHistoryConsultationServiceImpl implements DocumentHistoryConsultationService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandDocHistoryConsultation hapiToCommandDocHistoryConsultation;
    private boolean isFiltered = false;
    private boolean isEmpty = false;
    private final Bundle docHistoryConsult;

    @SneakyThrows
    public DocumentHistoryConsultationServiceImpl(CurrentPatient currentPatient, HapiToCommandDocHistoryConsultation hapiToCommandDocHistoryConsultation) {
        this.currentPatient = currentPatient;
        this.hapiToCommandDocHistoryConsultation = hapiToCommandDocHistoryConsultation;

        File json = new ClassPathResource("MedicalDocumentReferenceExampleBundle2.json").getFile();
        FileInputStream file = new FileInputStream(json);
        String lineReadtest = readFromInputStream(file);
        IParser parser = FhirContext.forR4().newJsonParser();
        this.docHistoryConsult = parser.parseResource(Bundle.class, lineReadtest);
        this.currentPatient.initDocHistoryConsultation(this.docHistoryConsult);
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
    public DocumentHistoryConsultationCommand documentHistoryConsultationCommand(String speciality, String date, String start, String end) {
        if (Objects.isNull(speciality) && Objects.isNull(date)) {
            return DocumentHistoryConsultationCommand.builder()
                    .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                    .documentHistoryConsultationInfoCommandList(Collections.emptyList())
                    .build();
        }
        var docHistoryConsultationCommands = new BundleProcessor(this.docHistoryConsult).docHistoryConsultationList()
                .stream()
                .map(this.hapiToCommandDocHistoryConsultation::convert)
                .collect(Collectors.toList());
        if (Objects.nonNull(speciality) && !speciality.equals("")) {
            docHistoryConsultationCommands = filterBySpeciality(docHistoryConsultationCommands, speciality);
        }
        if (Objects.nonNull(date) && !date.equals("")) {
            if (date.equalsIgnoreCase("all")) {
                return DocumentHistoryConsultationCommand.builder()
                        .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                        .documentHistoryConsultationInfoCommandList(docHistoryConsultationCommands)
                        .build();
            }
            if (date.equalsIgnoreCase("lastYear")) {
                start = LocalDate.now().getYear() - 1 + "-01-01";
                end = LocalDate.now().toString();
                return DocumentHistoryConsultationCommand.builder()
                        .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                        .documentHistoryConsultationInfoCommandList(filterBetween(docHistoryConsultationCommands, start, end))
                        .build();
            }
            if (date.equalsIgnoreCase("last5Years")) {
                start = LocalDate.now().getYear() - 5 + "-01-01";
                end = LocalDate.now().toString();
                return DocumentHistoryConsultationCommand.builder()
                        .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                        .documentHistoryConsultationInfoCommandList(filterBetween(docHistoryConsultationCommands, start, end))
                        .build();
            }
        }
        return DocumentHistoryConsultationCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .documentHistoryConsultationInfoCommandList(filterBetween(docHistoryConsultationCommands, start, end))
                .build();
    }

    private List<DocumentHistoryConsultationInfoCommand> filterBetween(List<DocumentHistoryConsultationInfoCommand> list, String start, String end) {
        if ((Objects.isNull(start) || start.equals("")) && (Objects.isNull(end) || end.equals(""))) {
            return list;
        }
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        List<DocumentHistoryConsultationInfoCommand> returnedList = new ArrayList<>();
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

    private static List<DocumentHistoryConsultationInfoCommand> filterBySpeciality(List<DocumentHistoryConsultationInfoCommand> list, String speciality) {
        if (speciality.equalsIgnoreCase("all")) {
            return list;
        }
        List<DocumentHistoryConsultationInfoCommand> documentHistoryConsultationInfoCommands = new ArrayList<>();
        list.forEach(dc -> {
            if (dc.getSpeciality().equalsIgnoreCase(speciality)) {
                documentHistoryConsultationInfoCommands.add(dc);
            }
        });
        return documentHistoryConsultationInfoCommands;
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
